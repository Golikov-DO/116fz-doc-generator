package ru.ecospas.app;

import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.ChildRepository;
import ru.ecospas.domain.repository.ParentRepository;
import ru.ecospas.domain.service.ObjectHazardService;
import ru.ecospas.domain.service.ScenarioNumberService;
import ru.ecospas.domain.service.WordGenerationService;
import ru.ecospas.infrastructure.db.repository.GenericHibernateRepository;
import ru.ecospas.word.blocks.text.TextPlaceholderService;
import ru.ecospas.word.factory.ImageBlockFactory;
import ru.ecospas.word.factory.ListBlockFactory;
import ru.ecospas.word.factory.TableBlockFactory;
import ru.ecospas.word.factory.UnifiedBlockFactory;
import ru.ecospas.word.layout.ContactTableLayoutService;
import ru.ecospas.word.layout.HazardTableLayoutService;
import ru.ecospas.word.layout.ObjectScenarioTableLayoutService;
import ru.ecospas.word.pipeline.PipelineConfiguration;
import ru.ecospas.word.strategy.PlaceholderFillStrategy;

import java.util.HashMap;
import java.util.Map;

public class Bootstrap {

    public static ApplicationContext init() {
        // 1. Infrastructure (Repositories)
        RepositoryContext repoContext = initRepositories();

        // 2. Business logic (Services)
        InternalServices services = initServices(repoContext);

        // 3. Document generation
        var wordService = initWordService(services);

        return new ApplicationContext(wordService, services);
    }

    private static RepositoryContext initRepositories() {
        Map<Class<?>, ParentRepository<?>> parentRepos = new HashMap<>();
        Map<Class<?>, ChildRepository<?>> childRepos = new HashMap<>();

        // PARENTS
        parent(parentRepos, Organization.class, "organizationShortName");
        parent(parentRepos, Asf.class,"shortName");
        parent(parentRepos, ReferenceHazardousSubstance.class, "name");
        parent(parentRepos, ReferenceCity.class,"cityName");
        parent(parentRepos, ReferenceEmergencyServices.class,"id");
        parent(parentRepos, ReferenceTableTitle.class,"id");
        parent(parentRepos, ObjectType.class, "id");
        parent(parentRepos, ReferenceHazardousParam.class,"id");
        parent(parentRepos, Scenario.class, "id");

        // SPECIAL CASE: ObjectModel acts as both parent and child
        var objectRepo = new GenericHibernateRepository<>(ObjectModel.class, "organization", "id");
        parentRepos.put(ObjectModel.class, objectRepo);  // PARENT
        childRepos.put(ObjectModel.class, objectRepo); // CHILD

        // CHILDREN
        // SINGLE RECORDS (OneToOne)
        child(childRepos, AsfCertificate.class, "asf", "id"  );
        child(childRepos, AsfCompositionDeploymentFunds.class, "asf", "id");
        child(childRepos, AsfPersonnel.class, "asf", "id");
        child(childRepos, AsfSpecialists.class, "asf", "id");
        child(childRepos, AsfWorkType.class, "asf", "id");
        child(childRepos, ObjectAddress.class, "object", "id");
        child(childRepos, ObjectInsurancePolicy.class, "object", "id");
        child(childRepos, ObjectFireEquipment.class, "object", "id");
        child(childRepos, ObjectOrderMinimumBalance.class, "object", "id");
        child(childRepos, ObjectPersonsResponsible.class, "object", "id");
        child(childRepos, ObjectRegionalAuthorities.class, "objectCity", "id");
        child(childRepos, OrganizationAddress.class, "organization", "id");

        // LISTS
        child(childRepos, AsfSigner.class, "asf", "name");
        child(childRepos, ObjectTechnologicalEquipment.class, "object", "num");
        child(childRepos, ObjectCompositionKchs.class, "object", "number");
        child(childRepos, ObjectHazardousParamValue.class, "substance", "id");
        child(childRepos, OrganizationContact.class, "organization", "id");
        child(childRepos, OrganizationSigner.class, "organization", "id");
        child(childRepos, AsfDocumentImage.class, "asf", "groupKey, id");
        child(childRepos, ObjectStructure.class, "object", "id");
        child(childRepos, ObjectTechnologicalBlock.class, "object", "num");
        child(childRepos, ObjectImage.class, "object", "groupKey, id");
        child(childRepos, ObjectScenario.class, "structure", "id");

        return new RepositoryContext(parentRepos, childRepos);
    }

    private static InternalServices initServices(RepositoryContext repoContext) {
        return new InternalServices(repoContext);
    }

    private static WordGenerationService initWordService(InternalServices services) {
        // Services for Word document generation
        ObjectHazardService objectHazardService = new ObjectHazardService(
                services.getParentService(ReferenceHazardousParam.class),
                services.getChildService(ObjectHazardousParamValue.class)
        );

        HazardTableLayoutService hazardTableLayoutService = new HazardTableLayoutService(
                services.getParentService(ObjectModel.class),
                objectHazardService
        );

        ContactTableLayoutService contactTableLayoutService = new ContactTableLayoutService(
                services.getParentService(Organization.class),
                services.getParentService(ObjectModel.class),
                services.getParentService(ReferenceEmergencyServices.class),
                services.getChildService(ObjectRegionalAuthorities.class),
                services.getChildService(OrganizationContact.class)
        );

        ScenarioNumberService scenarioNumberService = new ScenarioNumberService();

        ObjectScenarioTableLayoutService objectScenarioTableLayoutService =
                new ObjectScenarioTableLayoutService(
                        services.getChildService(ObjectStructure.class),
                        services.getParentService(Scenario.class)
                );

        TextPlaceholderService textPlaceholderService = new TextPlaceholderService(services);

        UnifiedBlockFactory unifiedFactory = new UnifiedBlockFactory(
                new TableBlockFactory(
                        services.getChildService(ObjectCompositionKchs.class),
                        services.getParentService(ObjectModel.class),
                        services.getChildService(ObjectTechnologicalEquipment.class),
                        services.getChildService(ObjectFireEquipment.class),
                        services.getChildService(ObjectPersonsResponsible.class)
                ),
                new PlaceholderFillStrategy(textPlaceholderService),
                new ImageBlockFactory(
                        services.getChildService(ObjectImage.class),
                        services.getChildService(AsfDocumentImage.class),
                        services.getParentService(ObjectModel.class)
                ),
                new ListBlockFactory(
                        services.getParentService(ObjectModel.class),
                        services.getChildService(ObjectStructure.class),
                        services.getChildService(ObjectTechnologicalBlock.class),
                        scenarioNumberService,
                        services.getParentService(Scenario.class)
                ),
                hazardTableLayoutService,
                contactTableLayoutService,
                objectScenarioTableLayoutService,
                services.getParentService(ObjectModel.class)
        );

        return new WordGenerationService(
                PipelineConfiguration.createDocumentBuilder(),
                PipelineConfiguration.createTagStrategy(unifiedFactory),
                PipelineConfiguration.createPlaceholderStrategy(unifiedFactory)
        );
    }

    private static <T extends BaseEntity> void parent(
            Map<Class<?>, ParentRepository<?>> map,
            Class<T> clazz,
            String sortField
    ) {
        map.put(clazz, new GenericHibernateRepository<>(clazz, null, sortField));
    }

    private static <T extends BaseEntity> void child(
            Map<Class<?>, ChildRepository<?>> map,
            Class<T> clazz,
            String parentField,
            String sortField
    ) {
        map.put(clazz, new GenericHibernateRepository<>(clazz, parentField, sortField));
    }
}