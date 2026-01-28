package com.caseo.app;

import com.caseo.domain.repository.*;
import com.caseo.domain.service.*;

/**
 * @param asfRepository         ---------- repositories ----------
 * @param asfService            ---------- services ----------
 * @param wordGenerationService ---------- word ----------
 */
public record ApplicationContext(AsfRepository asfRepository, AsfSignerRepository asfSignerRepository,
                                 DocumentSetRepository documentSetRepository,
                                 HazardousParamRepository hazardousParamRepository,
                                 HazardousParamValueRepository hazardousParamValueRepository,
                                 HazardousSubstanceRepository hazardousSubstanceRepository,
                                 ObjectRepository objectRepository, ObjectStructureRepository objectStructureRepository,
                                 OrganizationRepository organizationRepository, OrgSignerRepository orgSignerRepository,
                                 Table1Repository table1Repository,
                                 TechnologicalBlockRepository technologicalBlockRepository, AsfService asfService,
                                 AsfSignerService asfSignerService, DocumentSetService documentSetService,
                                 HazardousSubstanceService hazardousSubstanceService, HazardService hazardService,
                                 ObjectService objectService, ObjectStructureService objectStructureService,
                                 OrganizationService organizationService, OrgSignerService orgSignerService,
                                 Table1Service table1Service, TechnologicalBlockService technologicalBlockService,
                                 WordGenerationService wordGenerationService) {

    // repositories
    // services
    // word
}