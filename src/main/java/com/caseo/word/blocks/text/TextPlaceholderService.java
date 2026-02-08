package com.caseo.word.blocks.text;

import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.domain.util.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TextPlaceholderService {

    private final OrganizationService organizationService;
    private final OrganizationSignerService organizationSignerService;
    private final ObjectService objectService;
    private final ObjectAddressService objectAddressService;
    private final ObjectCityService objectCityService;
    private final ObjectTypeService objectTypeService;
    private final ObjectInsurancePolicyService objectInsurancePolicyService;
    private final ObjectOrderMinimumBalanceService objectOrderMinimumBalanceService;
    private final TechnologicalBlockService technologicalBlockService;
    private final HazardousSubstanceService hazardousSubstanceService;
    private final AsfService asfService;
    private final AsfSignerService asfSignerService;
    private final AsfWorkTypeService asfWorkTypeService;

    public TextPlaceholderService(
            OrganizationService organizationService,
            OrganizationSignerService organizationSignerService,
            ObjectService objectService,
            ObjectAddressService objectAddressService,
            ObjectCityService objectCityService,
            ObjectTypeService objectTypeService,
            ObjectInsurancePolicyService objectInsurancePolicyService,
            ObjectOrderMinimumBalanceService objectOrderMinimumBalanceService,
            TechnologicalBlockService technologicalBlockService,
            HazardousSubstanceService hazardousSubstanceService,
            AsfService asfService,
            AsfSignerService asfSignerService,
            AsfWorkTypeService asfWorkTypeService
    ) {
        this.organizationService = organizationService;
        this.organizationSignerService = organizationSignerService;
        this.objectService = objectService;
        this.objectAddressService = objectAddressService;
        this.objectCityService = objectCityService;
        this.objectTypeService = objectTypeService;
        this.objectInsurancePolicyService = objectInsurancePolicyService;
        this.objectOrderMinimumBalanceService = objectOrderMinimumBalanceService;
        this.technologicalBlockService = technologicalBlockService;
        this.hazardousSubstanceService = hazardousSubstanceService;
        this.asfService = asfService;
        this.asfSignerService = asfSignerService;
        this.asfWorkTypeService = asfWorkTypeService;
    }

    public Map<String, String> build(DocumentSet document) throws SQLException {

        Map<String, String> map = new HashMap<>();

        Organization org = organizationService.getById(document.orgId());
        OrganizationSigner organizationSigner = organizationSignerService.getByOrganizationId(org.organizationId());
        ObjectModel objectModel = objectService.getByOrgId(org.organizationId());
        ObjectType objectType = objectTypeService.getObjectType(objectModel.id());
        ObjectInsurancePolicy objectInsurancePolicy = objectInsurancePolicyService.getByObjectId(objectModel.id());
        ObjectOrderMinimumBalance objectOrderMinimumBalance = objectOrderMinimumBalanceService.getByObjectId(objectModel.id());
        HazardousSubstance hazardousSubstance = hazardousSubstanceService.getById(objectModel.id());
        Asf asf = asfService.getOrganizationId (document.orgId());
        AsfSigner asfSigner = asfSignerService.getByAsfId(asf.id());
        //AsfWorkType asfWorkType = asfWorkTypeService.getByAsfId(asf.id());

        // ---------- ORGANIZATION TEXT BLOCK ----------
        map.put("ORG_NAME", org.organizationName());
        map.put("ORG_SHORT_NAME", org.organizationShortName());
        map.put("ORG_ADDRESS", org.organizationAddress());
        map.put("ORG_SIGNER_POSITION", organizationSigner.position());
        map.put("ORG_SIGNER_NAME", organizationSigner.name());

        // ---------- ASF TEXT BLOCK ----------
        map.put("ASF_FULL_NAME", asf.fullName());
        map.put("ASF_FULL_NAME_GEN", asf.fullNameGen());
        map.put("ASF_SHORT_NAME", asf.shortName());
        map.put("ASF_STATUS", asf.status());
        map.put("ASF_STATUS_SHORT", asf.statusShort());
        map.put("ASF_SIGNER_POSITION", asfSigner.position());
        map.put("ASF_SIGNER_NAME", asfSigner.name());
        //map.put("ASF_CERTIFICATE_TEXT", AsfCertificateTextBuilder.build(Certificate.certificate()));
        //map.put("ASF_WORK_TYPES",asfWorkType);
        map.put("ASF_ARRIVAL_TIME", asf.arrivalTime());
        map.put("ASF_CONTACT_NUMBER", asf.telethonNumber());

        // ---------- OBJECT TEXT BLOCK ----------

        ObjectAddress address = objectAddressService.getByObjectId(objectModel.id());
        ObjectCity city = objectCityService.getByObjectId(address.id());
        String objectAddressText = ObjectAddressFormatter.format(address, city);
        String cityText = ObjectCityTextBuilder.buildFullDescription(city);
        int count = technologicalBlockService.countByObjectId(objectModel.id());

        map.put("OBJ_NAME", objectModel.objectFullName());
        map.put("OBJ_SHORT_NAME", objectModel.objectShortName());
        map.put("OBJ_ADDRESS", objectAddressText);
        map.put("OBJ_TYPE_DIFINITION", objectType.typeDefinition());
        map.put("OBJ_HAZARD_CLASS", HazardUtils.toRoman(String.valueOf(objectModel.hazardClass())));
        map.put("OBJ_HAZARDOUS_SUBSTANCE", hazardousSubstance.name());
        map.put("OBJ_AMOUNT_HAZARDOUS_SUBSTANCE", objectModel.amountOfHazardousSubstance());
        map.put("OBJ_HAZARDOUS_SUBSTANCE_GEN", hazardousSubstance.name_gen());
        map.put("OBJ_AMOUNT_TECHNOLOGICAL_BLOCK", RussianPlural.technologicalBlock(count));
        map.put("OBJ_CITY_FULL", cityText);
        map.put("OBJ_ORDER_MINIMUM_BALANCE_NUMBER", String.valueOf(objectOrderMinimumBalance.number()));
        map.put("OBJ_ORDER_MINIMUM_BALANCE_DATE", DateFormatter.russDate(objectOrderMinimumBalance.date()));
        map.put("OBJ_INSURANCE_POLICY_NUMBER", String.valueOf(objectInsurancePolicy.number()));
        map.put("OBJ_INSURANCE_POLICY_DATE", DateFormatter.russDate(objectInsurancePolicy.validUntil()));
        map.put("OBJ_NEAREST_FIRE_STATION", objectModel.nearestFireStation());
        map.put("OBJ_DEPARTMENT_GOCHS_CITY", objectModel.departmentGoChsCity());

        return map;
    }
}