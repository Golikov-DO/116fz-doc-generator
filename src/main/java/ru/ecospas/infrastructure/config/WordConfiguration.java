package ru.ecospas.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.service.ObjectHazardService;
import ru.ecospas.domain.service.ScenarioNumberService;
import ru.ecospas.domain.service.WordGenerationService;
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

@Configuration
@RequiredArgsConstructor
public class WordConfiguration {

    private final InternalServices services;

    @Bean
    public WordGenerationService wordGenerationService() {

        ObjectHazardService objectHazardService = new ObjectHazardService(
                services.getParentService(ReferenceHazardousParam.class),
                services.getChildService(ObjectHazardousParamValue.class)
        );

        HazardTableLayoutService hazardTableLayoutService =
                new HazardTableLayoutService(
                        services.getParentService(ObjectModel.class),
                        objectHazardService
                );

        ContactTableLayoutService contactTableLayoutService =
                new ContactTableLayoutService(
                        services.getParentService(Organization.class),
                        services.getParentService(ObjectModel.class),
                        services.getParentService(ReferenceEmergencyServices.class),
                        services.getChildService(ObjectRegionalAuthorities.class),
                        services.getChildService(OrganizationContact.class)
                );

        ScenarioNumberService scenarioNumberService =
                new ScenarioNumberService();

        ObjectScenarioTableLayoutService objectScenarioTableLayoutService =
                new ObjectScenarioTableLayoutService(
                        services.getChildService(ObjectStructure.class),
                        services.getParentService(Scenario.class)
                );

        TextPlaceholderService textPlaceholderService =
                new TextPlaceholderService(services);

        UnifiedBlockFactory unifiedFactory =
                new UnifiedBlockFactory(
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
}