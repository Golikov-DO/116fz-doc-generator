package com.caseo.app;

import com.caseo.domain.service.*;
import com.caseo.word.layout.ContactTableLayoutService;
import com.caseo.word.layout.HazardTableLayoutService;

public record InternalServices(
        ObjectAccidentScenariosService objectAccidentScenariosService,
        AsfCertificateService asfCertificateService,
        AsfCompositionDeploymentFundsService asfCompositionDeploymentFundsService,
        AsfDocumentImageService asfDocumentImageService,
        AsfPersonnelService asfPersonnelService,
        AsfService asfService,
        AsfSignerService asfSignerService,
        AsfSpecialistsService asfSpecialistsService,
        AsfWorkTypeService asfWorkTypeService,
        ObjectCompositionKchsService objectCompositionKchsService,
        ContactTableLayoutService contactTableLayoutService,
        ReferenceEmergencyServicesService referenceEmergencyServicesService,
        ObjectFireEquipmentService objectFireEquipmentService,
        ObjectHazardService objectHazardService,
        HazardTableLayoutService hazardTableLayoutService,
        ObjectHazardousSubstanceService objectHazardousSubstanceService,
        ObjectMainScenariosService objectMainScenariosService,
        ObjectAddressService objectAddressService,
        ReferenceCityService referenceCityService,
        ObjectImageService objectImageService,
        ObjectInsurancePolicyService objectInsurancePolicyService,
        ObjectOrderMinimumBalanceService objectOrderMinimumBalanceService,
        ObjectService objectService,
        ObjectStructureService objectStructureService,
        ReferenceTableTitleService referenceTableTitleService,
        ObjectTypeService objectTypeService,
        OrganizationAddressService organizationAddressService,
        OrganizationContactService organizationContactService,
        OrganizationService organizationService,
        OrganizationSignerService organizationSignerService,
        ObjectPersonsResponsibleService objectPersonsResponsibleService,
        ObjectRegionAuthoritiesService objectRegionAuthoritiesService,
        ObjectTechnologicalBlockService objectTechnologicalBlockService,
        ObjectTechnologicalEquipmentService objectTechnologicalEquipmentService
) {
}
