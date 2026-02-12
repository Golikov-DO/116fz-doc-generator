package com.caseo.app;

import com.caseo.domain.repository.*;

record RepositoryContext(
        AsfCertificateRepository asfCertificateRepository,
        AsfDocumentImageRepository asfDocumentImageRepository,
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
        ObjectTableTitleRepository objectTableTitleRepository,
        ObjectTypeRepository objectTypeRepository,
        ObjectImageRepository objectImageRepository,
        ObjectInsurancePolicyRepository objectInsurancePolicyRepository,
        ObjectOrderMinimumBalanceRepository objectOrderMinimumBalanceRepository,
        OrganizationAddressRepository organizationAddressRepository,
        OrganizationRepository organizationRepository,
        OrganizationSignerRepository organizationSignerRepository,
        TechnologicalEquipmentRepository technologicalEquipmentRepository,
        TechnologicalBlockRepository technologicalBlockRepository
) {
}
