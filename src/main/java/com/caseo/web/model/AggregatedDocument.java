package com.caseo.web.model;

import com.caseo.domain.model.*;

import java.util.List;

public record AggregatedDocument(
        Organization organization,
        OrganizationAddress organizationAddress,
        OrganizationSigner organizationSigner,
        List<ObjectType> objectTypes,
        List<OrganizationContact> contacts,

        List<ObjectModel> objects,
        List<ObjectAddress> objectAddresses,

        List<List<ObjectCompositionKchs>> kchsLists,
        List<List<ObjectTechnologicalEquipment>> equipmentLists,
        List<List<ObjectStructure>> structureLists,
        List<List<ObjectFireEquipment>> fireLists,
        List<List<ObjectRegionalAuthorities>> authoritiesLists,

        List<ObjectInsurancePolicy> policyList,
        List<ObjectOrderMinimumBalance> balanceList
) {}