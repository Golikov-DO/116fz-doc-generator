package com.caseo.app;

import com.caseo.domain.repository.OrganizationContactRepository;
import com.caseo.domain.service.*;
import com.caseo.word.layout.ContactTableLayoutService;
import com.caseo.word.layout.HazardTableLayoutService;

public record InternalServices(
        AccidentScenariosService accidentScenariosService,
        AsfCertificateService asfCertificateService,
        AsfDocumentImageService asfDocumentImageService,
        AsfService asfService,
        AsfSignerService asfSignerService,
        AsfWorkTypeService asfWorkTypeService,
        CompositionKchsService compositionKchsService,
        ContactTableLayoutService contactTableLayoutService,
        DocumentSetService documentSetService,
        EmergencyServicesService emergencyServicesService,
        FireEquipmentService fireEquipmentService,
        HazardService hazardService,
        HazardTableLayoutService hazardTableLayoutService,
        HazardousSubstanceService hazardousSubstanceService,
        MainScenariosService mainScenariosService,
        ObjectAddressService objectAddressService,
        ObjectCityService objectCityService,
        ObjectImageService objectImageService,
        ObjectInsurancePolicyService objectInsurancePolicyService,
        ObjectOrderMinimumBalanceService objectOrderMinimumBalanceService,
        ObjectService objectService,
        ObjectStructureService objectStructureService,
        ObjectTableTitleService objectTableTitleService,
        ObjectTypeService objectTypeService,
        OrganizationAddressService organizationAddressService,
        OrganizationContactService organizationContactService,
        OrganizationService organizationService,
        OrganizationSignerService organizationSignerService,
        PersonsResponsibleService personsResponsibleService,
        RegionAuthoritiesService regionAuthoritiesService,
        TechnologicalBlockService technologicalBlockService,
        TechnologicalEquipmentService technologicalEquipmentService
) {
}
