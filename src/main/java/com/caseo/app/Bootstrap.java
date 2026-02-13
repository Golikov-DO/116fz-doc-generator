package com.caseo.app;

import com.caseo.domain.repository.*;
import com.caseo.domain.service.*;
import com.caseo.infrastructure.db.repository.*;
import com.caseo.word.blocks.text.TextPlaceholderService;
import com.caseo.word.factory.*;
import com.caseo.word.layout.ContactTableLayoutService;
import com.caseo.word.layout.HazardTableLayoutService;
import com.caseo.word.pipeline.PipelineConfiguration;

import com.caseo.word.strategy.PlaceholderFillStrategy;

public class Bootstrap {

    public static ApplicationContext init() {
        // 1. Инфраструктура (Репозитории)
        var repos = initRepositories();

        // 2. Бизнес-логика (Сервисы)
        var services = initServices(repos);

        // 3. Генератор документов
        var wordService = initWordService(services);

        return new ApplicationContext(
                repos.asfSignerRepository(),
                wordService,
                services.documentSetService(),
                services.organizationService()
        );
    }

    private static RepositoryContext initRepositories() {
        return new RepositoryContext(
                new JdbcAccidentScenariosRepository(),
                new JdbcAsfCertificateRepository(),
                new JdbcAsfDocumentImageRepository(),
                new JdbcAsfRepository(),
                new JdbcAsfSignerRepository(),
                new JdbcAsfWorkTypeRepository(),
                new JdbcCompositionKchsRepository(),
                new JdbcDocumentSetRepository(),
                new JdbcEmergencyServicesRepository(),
                new JdbcFireEquipmentRepository(),
                new JdbcHazardousParamRepository(),
                new JdbcHazardousParamValueRepository(),
                new JdbcHazardousSubstanceRepository(),
                new JdbcMainScenariosRepository(),
                new JdbcObjectRepository(),
                new JdbcObjectStructureRepository(),
                new JdbcObjectCityRepository(),
                new JdbcObjectAddressRepository(),
                new JdbcObjectTableTitleRepository(),
                new JdbcObjectTypeRepository(),
                new JdbcObjectImageRepository(),
                new JdbcObjectInsurancePolicyRepository(),
                new JdbcObjectOrderMinimumBalanceRepository(),
                new JdbcOrganizationAddressRepository(),
                new JdbcOrganizationContactRepository(),
                new JdbcOrganizationRepository(),
                new JdbcOrganizationSignerRepository(),
                new JdbcPersonsResponsibleRepository(),
                new JdbcRegionAuthoritiesRepository(),
                new JdbcTechnologicalEquipmentRepository(),
                new JdbcTechnologicalBlockRepository()
        );
    }

    private static InternalServices initServices(RepositoryContext repositoryContext) {

        EmergencyServicesService emergencyServicesService = new EmergencyServicesService(repositoryContext.emergencyServicesRepository());
        HazardService hazardService = new HazardService(repositoryContext.hazardousParamRepository(), repositoryContext.hazardousParamValueRepository());
        ObjectService objectService = new ObjectService(repositoryContext.objectRepository());
        OrganizationService organizationService = new OrganizationService(repositoryContext.organizationRepository());
        OrganizationContactService organizationContactService = new OrganizationContactService(repositoryContext.organizationContactRepository());
        RegionAuthoritiesService regionAuthoritiesService = new RegionAuthoritiesService(repositoryContext.regionAuthoritiesRepository());

        HazardTableLayoutService hazardTableLayoutService = new HazardTableLayoutService(objectService, hazardService);
        ContactTableLayoutService contactTableLayoutService = new ContactTableLayoutService(
                objectService, emergencyServicesService, regionAuthoritiesService, organizationContactService, organizationService);

        return new InternalServices(
                new AccidentScenariosService(repositoryContext.accidentScenariosRepository()),
                new AsfCertificateService(repositoryContext.asfCertificateRepository()),
                new AsfDocumentImageService(repositoryContext.asfDocumentImageRepository()),
                new AsfService(repositoryContext.asfRepository()),
                new AsfSignerService(repositoryContext.asfSignerRepository()),
                new AsfWorkTypeService(repositoryContext.asfWorkTypeRepository()),
                new CompositionKchsService(repositoryContext.compositionKchsRepository()),
                contactTableLayoutService,
                new DocumentSetService(repositoryContext.documentSetRepository()),
                emergencyServicesService,
                new FireEquipmentService(repositoryContext.fireEquipmentRepository()),
                hazardService,
                hazardTableLayoutService,
                new HazardousSubstanceService(repositoryContext.hazardousSubstanceRepository()),
                new MainScenariosService(repositoryContext.mainScenariosRepository()),
                new ObjectAddressService(repositoryContext.objectAddressRepository()),
                new ObjectCityService(repositoryContext.objectCityRepository()),
                new ObjectImageService(repositoryContext.objectImageRepository()),
                new ObjectInsurancePolicyService(repositoryContext.objectInsurancePolicyRepository()),
                new ObjectOrderMinimumBalanceService(repositoryContext.objectOrderMinimumBalanceRepository()),
                objectService,
                new ObjectStructureService(repositoryContext.objectStructureRepository()),
                new ObjectTableTitleService(repositoryContext.objectTableTitleRepository()),
                new ObjectTypeService(repositoryContext.objectTypeRepository()),
                new OrganizationAddressService(repositoryContext.organizationAddressRepository()),
                organizationContactService,
                organizationService,
                new OrganizationSignerService(repositoryContext.organizationSignerRepository()),
                new PersonsResponsibleService(repositoryContext.personsResponsibleRepository()),
                regionAuthoritiesService,
                new TechnologicalBlockService(repositoryContext.technologicalBlockRepository()),
                new TechnologicalEquipmentService(repositoryContext.technologicalEquipmentRepository())

        );
    }

    private static WordGenerationService initWordService(InternalServices internalServices) {
        TextPlaceholderService textPlaceholderService = new TextPlaceholderService(internalServices);
        UnifiedBlockFactory unifiedFactory = initUnifiedFactory(internalServices, textPlaceholderService);

        return new WordGenerationService(
                PipelineConfiguration.createDocumentBuilder(),
                PipelineConfiguration.createTagStrategy(unifiedFactory),
                PipelineConfiguration.createPlaceholderStrategy(unifiedFactory)
        );
    }

    private static UnifiedBlockFactory initUnifiedFactory(InternalServices s, TextPlaceholderService textService) {
        return new UnifiedBlockFactory(
                new TableBlockFactory(
                        s.organizationService(),
                        s.objectService(),
                        s.technologicalEquipmentService(),
                        s.accidentScenariosService(),
                        s.mainScenariosService(),
                        s.fireEquipmentService(),
                        s.personsResponsibleService(),
                        s.compositionKchsService()
                ),
                new PlaceholderFillStrategy(textService),
                new ImageBlockFactory(s.objectImageService(), s.objectService(), s.asfDocumentImageService(), s.asfService()),
                new ListBlockFactory(s.objectService(), s.objectStructureService(), s.technologicalBlockService(), s.objectAddressService(), s.objectCityService()),
                s.hazardTableLayoutService(),
                s.contactTableLayoutService()
        );
    }
}