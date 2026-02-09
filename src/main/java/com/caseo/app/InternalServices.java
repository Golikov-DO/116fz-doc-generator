package com.caseo.app;

import com.caseo.domain.service.*;
import com.caseo.word.layout.HazardTableLayoutService;

public record InternalServices(
        AsfCertificateService asfCertificateService,
        AsfService asfService,
        AsfSignerService asfSignerService,
        AsfWorkTypeService asfWorkTypeService,
        DocumentSetService documentSetService,
        HazardService hazardService,
        HazardTableLayoutService hazardTableLayoutService,
        HazardousSubstanceService hazardousSubstanceService,
        ObjectAddressService objectAddressService,
        ObjectCityService objectCityService,
        ObjectInsurancePolicyService objectInsurancePolicyService,
        ObjectOrderMinimumBalanceService objectOrderMinimumBalanceService,
        ObjectService objectService,
        ObjectStructureService objectStructureService,
        ObjectTypeService objectTypeService,
        OrganizationAddressService organizationAddressService,
        OrganizationService organizationService,
        OrganizationSignerService organizationSignerService,
        TechnologicalBlockService technologicalBlockService,
        TechnologicalEquipmentService technologicalEquipmentService
) {
}
