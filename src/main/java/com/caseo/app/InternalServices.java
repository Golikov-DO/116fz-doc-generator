package com.caseo.app;

import com.caseo.domain.service.*;
import com.caseo.word.layout.HazardTableLayoutService;

record InternalServices(
        AsfService asfService,
        AsfSignerService asfSignerService,
        AsfWorkTypeService asfWorkTypeService,
        DocumentSetService documentSetService,
        HazardousSubstanceService hazardousSubstanceService,
        HazardService hazardService,
        HazardTableLayoutService hazardTableLayoutService,
        ObjectAddressService objectAddressService,
        ObjectCityService objectCityService,
        ObjectInsurancePolicyService objectInsurancePolicyService,
        ObjectOrderMinimumBalanceService objectOrderMinimumBalanceService,
        ObjectService objectService,
        ObjectStructureService objectStructureService,
        ObjectTypeService objectTypeService,
        OrganizationService organizationService,
        OrganizationSignerService organizationSignerService,
        TechnologicalBlockService technologicalBlockService,
        TechnologicalEquipmentService technologicalEquipmentService
) {}
