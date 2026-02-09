package com.caseo.app;

import com.caseo.domain.repository.*;

record RepositoryContext(
        AsfCertificateRepository asfCertificateRepository,
        AsfRepository asfRepository,
        AsfSignerRepository asfSignerRepository,
        AsfWorkTypeRepository asfWorkTypeRepository,
        DocumentSetRepository documentSetRepository,
        HazardousParamRepository hazardousParamRepository,
        HazardousParamValueRepository hazardousParamValueRepository,
        HazardousSubstanceRepository hazardousSubstanceRepository,
        ObjectRepository objectRepository,
        ObjectStructureRepository objectStructureRepository,
        ObjectCityRepository objectCityRepository,
        ObjectAddressRepository objectAddressRepository,
        ObjectTypeRepository objectTypeRepository,
        ObjectInsurancePolicyRepository objectInsurancePolicyRepository,
        ObjectOrderMinimumBalanceRepository objectOrderMinimumBalanceRepository,
        OrganizationAddressRepository organizationAddressRepository,
        OrganizationRepository organizationRepository,
        OrganizationSignerRepository organizationSignerRepository,
        TechnologicalEquipmentRepository technologicalEquipmentRepository,
        TechnologicalBlockRepository technologicalBlockRepository
) {
}
