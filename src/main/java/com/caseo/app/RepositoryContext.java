package com.caseo.app;

import com.caseo.domain.repository.*;

record RepositoryContext(
        AccidentScenariosRepository accidentScenariosRepository,
        AsfCertificateRepository asfCertificateRepository,
        AsfDocumentImageRepository asfDocumentImageRepository,
        AsfRepository asfRepository,
        AsfSignerRepository asfSignerRepository,
        AsfWorkTypeRepository asfWorkTypeRepository,
        CompositionKchsRepository compositionKchsRepository,
        DocumentSetRepository documentSetRepository,
        EmergencyServicesRepository emergencyServicesRepository,
        FireEquipmentRepository fireEquipmentRepository,
        HazardousParamRepository hazardousParamRepository,
        HazardousParamValueRepository hazardousParamValueRepository,
        HazardousSubstanceRepository hazardousSubstanceRepository,
        MainScenariosRepository mainScenariosRepository,
        ObjectRepository objectRepository,
        ObjectStructureRepository objectStructureRepository,
        ObjectCityRepository objectCityRepository,
        ObjectAddressRepository objectAddressRepository,
        ObjectTableTitleRepository objectTableTitleRepository,
        ObjectTypeRepository objectTypeRepository,
        ObjectImageRepository objectImageRepository,
        ObjectInsurancePolicyRepository objectInsurancePolicyRepository,
        ObjectOrderMinimumBalanceRepository objectOrderMinimumBalanceRepository,
        OrganizationAddressRepository organizationAddressRepository,
        OrganizationContactRepository organizationContactRepository,
        OrganizationRepository organizationRepository,
        OrganizationSignerRepository organizationSignerRepository,
        PersonsResponsibleRepository personsResponsibleRepository,
        RegionAuthoritiesRepository regionAuthoritiesRepository,
        TechnologicalEquipmentRepository technologicalEquipmentRepository,
        TechnologicalBlockRepository technologicalBlockRepository
) {
}
