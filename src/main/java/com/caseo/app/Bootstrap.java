package com.caseo.app;

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
                services.organizationService(),
                services.objectService(),
                services
        );
    }

    private static RepositoryContext initRepositories() {
        return new RepositoryContext(
                new JdbcObjectAccidentScenariosRepository(),
                new JdbcAsfCertificateRepository(),
                new JdbcAsfCompositionDeploymentFundsRepository(),
                new JdbcAsfDocumentImageRepository(),
                new JdbcAsfPersonnelRepository(),
                new JdbcAsfRepository(),
                new JdbcAsfSignerRepository(),
                new JdbcAsfSpecialistsRepository(),
                new JdbcAsfWorkTypeRepository(),
                new JdbcObjectCompositionKchsRepository(),
                new JdbcDocumentSetRepository(),
                new JdbcReferenceEmergencyServicesRepository(),
                new JdbcObjectFireEquipmentRepository(),
                new JdbcObjectHazardousParamRepository(),
                new JdbcObjectHazardousParamValueRepository(),
                new JdbcObjectHazardousSubstanceRepository(),
                new JdbcObjectMainScenariosRepository(),
                new JdbcObjectRepository(),
                new JdbcObjectStructureRepository(),
                new JdbcReferenceCityRepository(),
                new JdbcObjectAddressRepository(),
                new JdbcReferenceTableTitleRepository(),
                new JdbcObjectTypeRepository(),
                new JdbcObjectImageRepository(),
                new JdbcObjectInsurancePolicyRepository(),
                new JdbcObjectOrderMinimumBalanceRepository(),
                new JdbcOrganizationAddressRepository(),
                new JdbcOrganizationContactRepository(),
                new JdbcOrganizationRepository(),
                new JdbcOrganizationSignerRepository(),
                new JdbcObjectPersonsResponsibleRepository(),
                new JdbcObjectRegionAuthoritiesRepository(),
                new JdbcObjectTechnologicalEquipmentRepository(),
                new JdbcObjectTechnologicalBlockRepository()
        );
    }

    private static InternalServices initServices(RepositoryContext repositoryContext) {

        ReferenceEmergencyServicesService referenceEmergencyServicesService = new ReferenceEmergencyServicesService(repositoryContext.referenceEmergencyServicesRepository());
        ObjectHazardService objectHazardService = new ObjectHazardService(repositoryContext.objectHazardousParamRepository(), repositoryContext.objectHazardousParamValueRepository());
        ObjectService objectService = new ObjectService(repositoryContext.objectRepository());
        OrganizationService organizationService = new OrganizationService(repositoryContext.organizationRepository());
        OrganizationContactService organizationContactService = new OrganizationContactService(repositoryContext.organizationContactRepository());
        ObjectRegionAuthoritiesService objectRegionAuthoritiesService = new ObjectRegionAuthoritiesService(repositoryContext.objectRegionAuthoritiesRepository());

        HazardTableLayoutService hazardTableLayoutService = new HazardTableLayoutService(
                objectService,
                objectHazardService
        );
        ContactTableLayoutService contactTableLayoutService = new ContactTableLayoutService(
                objectService,
                referenceEmergencyServicesService,
                objectRegionAuthoritiesService,
                organizationContactService,
                organizationService
        );

        return new InternalServices(
                new ObjectAccidentScenariosService(repositoryContext.objectAccidentScenariosRepository()),
                new AsfCertificateService(repositoryContext.asfCertificateRepository()),
                new AsfCompositionDeploymentFundsService(repositoryContext.asfCompositionDeploymentFundsRepository()),
                new AsfDocumentImageService(repositoryContext.asfDocumentImageRepository()),
                new AsfPersonnelService(repositoryContext.asfPersonnelRepository()),
                new AsfService(repositoryContext.asfRepository()),
                new AsfSignerService(repositoryContext.asfSignerRepository()),
                new AsfSpecialistsService(repositoryContext.asfSpecialistsRepository()),
                new AsfWorkTypeService(repositoryContext.asfWorkTypeRepository()),
                new ObjectCompositionKchsService(repositoryContext.objectCompositionKchsRepository()),
                contactTableLayoutService,
                new DocumentSetService(repositoryContext.documentSetRepository()),
                referenceEmergencyServicesService,
                new ObjectFireEquipmentService(repositoryContext.objectFireEquipmentRepository()),
                objectHazardService,
                hazardTableLayoutService,
                new ObjectHazardousSubstanceService(repositoryContext.objectHazardousSubstanceRepository()),
                new ObjectMainScenariosService(repositoryContext.objectMainScenariosRepository()),
                new ObjectAddressService(repositoryContext.objectAddressRepository()),
                new ReferenceCityService(repositoryContext.referenceCityRepository()),
                new ObjectImageService(repositoryContext.objectImageRepository()),
                new ObjectInsurancePolicyService(repositoryContext.objectInsurancePolicyRepository()),
                new ObjectOrderMinimumBalanceService(repositoryContext.objectOrderMinimumBalanceRepository()),
                objectService,
                new ObjectStructureService(repositoryContext.objectStructureRepository()),
                new ReferenceTableTitleService(repositoryContext.referenceTableTitleRepository()),
                new ObjectTypeService(repositoryContext.objectTypeRepository()),
                new OrganizationAddressService(repositoryContext.organizationAddressRepository()),
                organizationContactService,
                organizationService,
                new OrganizationSignerService(repositoryContext.organizationSignerRepository()),
                new ObjectPersonsResponsibleService(repositoryContext.objectPersonsResponsibleRepository()),
                objectRegionAuthoritiesService,
                new ObjectTechnologicalBlockService(repositoryContext.objectTechnologicalBlockRepository()),
                new ObjectTechnologicalEquipmentService(repositoryContext.objectTechnologicalEquipmentRepository())

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

    private static UnifiedBlockFactory initUnifiedFactory(InternalServices internalServices, TextPlaceholderService textService) {
        return new UnifiedBlockFactory(
                new TableBlockFactory(
                        internalServices.organizationService(),
                        internalServices.objectService(),
                        internalServices.objectTechnologicalEquipmentService(),
                        internalServices.objectAccidentScenariosService(),
                        internalServices.objectMainScenariosService(),
                        internalServices.objectFireEquipmentService(),
                        internalServices.objectPersonsResponsibleService(),
                        internalServices.objectCompositionKchsService()
                ),
                new PlaceholderFillStrategy(textService),
                new ImageBlockFactory(
                        internalServices.objectImageService(),
                        internalServices.objectService(),
                        internalServices.asfDocumentImageService(),
                        internalServices.asfService()
                ),
                new ListBlockFactory(
                        internalServices.objectService(),
                        internalServices.objectStructureService(),
                        internalServices.objectTechnologicalBlockService(),
                        internalServices.objectAddressService(),
                        internalServices.referenceCityService()
                ),
                internalServices.hazardTableLayoutService(),
                internalServices.contactTableLayoutService()
        );
    }
}