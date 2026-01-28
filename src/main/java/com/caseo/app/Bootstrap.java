package com.caseo.app;

import com.caseo.document.DocumentGenerator;
import com.caseo.domain.repository.*;
import com.caseo.domain.service.*;
import com.caseo.domain.util.DocumentPathUtil;
import com.caseo.infrastructure.db.repository.*;
import com.caseo.word.blocks.text.TextPlaceholderService;
import com.caseo.word.factory.*;
import com.caseo.word.layout.HazardTableLayoutService;
import com.caseo.word.strategy.TagFillStrategyDocx4j;
import com.caseo.word.tag.ObjectSchemeImageTagBuilder;
import com.caseo.word.tag.ObjectStructureListTagBuilder;
import com.caseo.word.tag.Table1TagDataBuilder;
import com.caseo.word.tag.TechnologicalBlockListTagBuilder;

public class Bootstrap {

    public static ApplicationContext init() {

        // ---------- REPOSITORIES ----------
        AsfRepository asfRepository = new JdbcAsfRepository();
        AsfSignerRepository asfSignerRepository = new JdbcAsfSignerRepository();
        DocumentSetRepository documentSetRepository = new JdbcDocumentSetRepository();
        HazardousParamRepository hazardousParamRepository = new JdbcHazardousParamRepository();
        HazardousParamValueRepository hazardousParamValueRepository = new JdbcHazardousParamValueRepository();
        HazardousSubstanceRepository hazardousSubstanceRepository = new JdbcHazardousSubstanceRepository();
        ObjectRepository objectRepository = new JdbcObjectRepository();
        ObjectStructureRepository objectStructureRepository = new JdbcObjectStructureRepository();
        ObjectCityRepository objectCityRepository = new JdbcObjectCityRepository();
        ObjectAddressRepository objectAddressRepository = new JdbcObjectAddressRepository();
        OrganizationRepository organizationRepository = new JdbcOrganizationRepository();
        OrgSignerRepository orgSignerRepository = new JdbcOrgSignerRepository();
        Table1Repository table1Repository = new JdbcTable1Repository();
        TechnologicalBlockRepository technologicalBlockRepository = new JdbcTechnologicalBlockRepository();

        // ---------- SERVICES ----------
        AsfService asfService = new AsfService(asfRepository);
        AsfSignerService asfSignerService = new AsfSignerService(asfSignerRepository);
        DocumentSetService documentSetService = new DocumentSetService(documentSetRepository);
        HazardousSubstanceService hazardousSubstanceService = new HazardousSubstanceService(hazardousSubstanceRepository);
        HazardService hazardService = new HazardService(hazardousParamRepository, hazardousParamValueRepository);
        ObjectService objectService = new ObjectService(objectRepository);
        ObjectStructureService objectStructureService = new ObjectStructureService(objectStructureRepository);
        ObjectCityService objectCityService = new ObjectCityService(objectCityRepository);
        ObjectAddressService objectAddressService = new ObjectAddressService(objectAddressRepository);
        OrganizationService organizationService = new OrganizationService(organizationRepository);
        OrgSignerService orgSignerService = new OrgSignerService(orgSignerRepository);
        Table1Service table1Service = new Table1Service(table1Repository);
        TechnologicalBlockService technologicalBlockService = new TechnologicalBlockService(technologicalBlockRepository);

// ---------- SERVICES ----------
        TextPlaceholderService textPlaceholderService =
                new TextPlaceholderService(
                        organizationService,
                        orgSignerService,
                        objectService,
                        objectStructureService,
                        objectAddressService,
                        objectCityService,
                        technologicalBlockService,
                        hazardousSubstanceService,
                        asfService,
                        asfSignerService
                );

// ---------- LAYOUT ----------
        HazardTableLayoutService hazardTableLayoutService =
                new HazardTableLayoutService();

// ---------- FACTORIES ----------
        TextBlockFactory placeholderTextBlockFactory =
                new TextBlockFactory(textPlaceholderService);

        TagFillStrategyDocx4j tagFillStrategy =
                new TagFillStrategyDocx4j(
                        organizationService,
                        textPlaceholderService,
                        new Table1TagDataBuilder(
                                organizationService,
                                objectService,
                                table1Service
                        ),
                        new ObjectStructureListTagBuilder(
                                objectService,
                                objectStructureService
                        ),
                        new TechnologicalBlockListTagBuilder(
                                objectService,
                                technologicalBlockService
                        ),
                        new ObjectSchemeImageTagBuilder(
                                objectService
                        ),
                        DocumentPathUtil.TAG_TEMPLATE_PATH
                );


        ListBlockFactory listBlockFactory =
                new ListBlockFactory(
                        objectService,
                        objectStructureService,
                        technologicalBlockService
                );

        SimpleTableBlockFactory simpleTableBlockFactory =
                new SimpleTableBlockFactory(
                        organizationService,
                        objectService,
                        table1Service
                );

        HazardTableBlockFactory hazardTableBlockFactory =
                new HazardTableBlockFactory(
                        objectService,
                        hazardService,
                        hazardTableLayoutService
                );

        ImageBlockFactory imageBlockFactory =
                new ImageBlockFactory(objectService);

// ---------- WORD GENERATOR ----------
        DocumentGenerator placeholderDocumentGenerator = new DocumentGenerator(
                organizationService,
                placeholderTextBlockFactory,
                listBlockFactory,
                hazardTableBlockFactory,
                imageBlockFactory,
                simpleTableBlockFactory
        );

// ---------- WORD GENERATION SERVICE ----------
        WordGenerationService wordGenerationService =
                new WordGenerationService(
                        documentSetService,
                        placeholderDocumentGenerator,
                        tagFillStrategy
                );

// ---------- CONTEXT ----------
        return new ApplicationContext(
                // repositories
                asfRepository,
                asfSignerRepository,
                documentSetRepository,
                hazardousParamRepository,
                hazardousParamValueRepository,
                hazardousSubstanceRepository,
                objectRepository,
                objectStructureRepository,
                organizationRepository,
                orgSignerRepository,
                table1Repository,
                technologicalBlockRepository,

                // services
                asfService,
                asfSignerService,
                documentSetService,
                hazardousSubstanceService,
                hazardService,
                objectService,
                objectStructureService,
                organizationService,
                orgSignerService,
                table1Service,
                technologicalBlockService,

                // word
                wordGenerationService
        );
    }
}