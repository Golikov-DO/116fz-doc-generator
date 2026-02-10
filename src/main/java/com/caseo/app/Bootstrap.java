package com.caseo.app;

import com.caseo.domain.repository.*;
import com.caseo.domain.service.*;
import com.caseo.infrastructure.db.repository.*;
import com.caseo.word.blocks.text.TextPlaceholderService;
import com.caseo.word.factory.*;
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
                new JdbcAsfCertificateRepository(),
                new JdbcAsfRepository(),
                new JdbcAsfSignerRepository(),
                new JdbcAsfWorkTypeRepository(),
                new JdbcDocumentSetRepository(),
                new JdbcHazardousParamRepository(),
                new JdbcHazardousParamValueRepository(),
                new JdbcHazardousSubstanceRepository(),
                new JdbcObjectRepository(),
                new JdbcObjectStructureRepository(),
                new JdbcObjectCityRepository(),
                new JdbcObjectAddressRepository(),
                new JdbcObjectTypeRepository(),
                new JdbcObjectImageRepository(),
                new JdbcObjectInsurancePolicyRepository(),
                new JdbcObjectOrderMinimumBalanceRepository(),
                new JdbcOrganizationAddressRepository(),
                new JdbcOrganizationRepository(),
                new JdbcOrganizationSignerRepository(),
                new JdbcTechnologicalEquipmentRepository(),
                new JdbcTechnologicalBlockRepository()
        );
    }

    private static InternalServices initServices(RepositoryContext repositoryContext) {

        HazardService hazardService = new HazardService(repositoryContext.hazardousParamRepository(), repositoryContext.hazardousParamValueRepository());
        ObjectService objectService = new ObjectService(repositoryContext.objectRepository());
        HazardTableLayoutService hazardTableLayoutService = new HazardTableLayoutService(objectService, hazardService);
        return new InternalServices(
                new AsfCertificateService(repositoryContext.asfCertificateRepository()),
                new AsfService(repositoryContext.asfRepository()),
                new AsfSignerService(repositoryContext.asfSignerRepository()),
                new AsfWorkTypeService(repositoryContext.asfWorkTypeRepository()),
                new DocumentSetService(repositoryContext.documentSetRepository()),
                hazardService,
                hazardTableLayoutService,
                new HazardousSubstanceService(repositoryContext.hazardousSubstanceRepository()),
                new ObjectAddressService(repositoryContext.objectAddressRepository()),
                new ObjectCityService(repositoryContext.objectCityRepository()),
                new ObjectImageService(repositoryContext.objectImageRepository()),
                new ObjectInsurancePolicyService(repositoryContext.objectInsurancePolicyRepository()),
                new ObjectOrderMinimumBalanceService(repositoryContext.objectOrderMinimumBalanceRepository()),
                objectService,
                new ObjectStructureService(repositoryContext.objectStructureRepository()),
                new ObjectTypeService(repositoryContext.objectTypeRepository()),
                new OrganizationAddressService(repositoryContext.organizationAddressRepository()),
                new OrganizationService(repositoryContext.organizationRepository()),
                new OrganizationSignerService(repositoryContext.organizationSignerRepository()),
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
                new TableBlockFactory(s.organizationService(), s.objectService(), s.technologicalEquipmentService()),
                new PlaceholderFillStrategy(textService),
                new ImageBlockFactory(s.objectImageService(), s.objectService()),
                new ListBlockFactory(s.objectService(), s.objectStructureService(), s.technologicalBlockService(), s.objectAddressService(), s.objectCityService()),
                s.hazardTableLayoutService()
        );
    }
}