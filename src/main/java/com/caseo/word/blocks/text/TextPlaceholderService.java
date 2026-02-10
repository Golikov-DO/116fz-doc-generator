package com.caseo.word.blocks.text;

import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.util.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TextPlaceholderService {

    private final InternalServices internalServices;

    public TextPlaceholderService(InternalServices internalServices) {
        this.internalServices = internalServices;
    }

    public Map<String, String> build(DocumentSet document) throws SQLException {
        Map<String, String> map = new HashMap<>();

        // Базовые объекты для получения ID и общих данных
        Organization org = internalServices.organizationService().getById(document.orgId());
        ObjectModel obj = internalServices.objectService().getByOrgId(org.organizationId());
        Asf asf = internalServices.asfService().getOrganizationId(document.orgId());

        // ---------- ORGANIZATION TEXT BLOCK ----------
        map.put("ORG_NAME", org.organizationName());
        map.put("ORG_SHORT_NAME", org.organizationShortName());
        var orgAddr = internalServices.organizationAddressService().getByOrganizationId(org.organizationId());
        map.put("ORG_ADDRESS_FULL", AddressFormatter.format(orgAddr));
        map.put("ORG_TYPE_ACTIVITY", org.organizationTypeActivity());
        var orgSigner = internalServices.organizationSignerService().getByOrganizationId(org.organizationId());
        map.put("ORG_SIGNER_POSITION", orgSigner.position());
        map.put("ORG_SIGNER_NAME", orgSigner.name());

        // ---------- ASF TEXT BLOCK ----------
        map.put("ASF_FULL_NAME", asf.fullName());
        map.put("ASF_FULL_NAME_GEN", asf.fullNameGen());
        map.put("ASF_SHORT_NAME", asf.shortName());
        map.put("ASF_STATUS", asf.status());
        map.put("ASF_STATUS_SHORT", asf.statusShort());
        map.put("ASF_ARRIVAL_TIME", asf.arrivalTime());
        map.put("ASF_CONTACT_NUMBER", asf.telethonNumber());
        var asfSigner = internalServices.asfSignerService().getByAsfId(asf.id());
        map.put("ASF_SIGNER_POSITION", asfSigner.position());
        map.put("ASF_SIGNER_NAME", asfSigner.name());
        var cert = internalServices.asfCertificateService().getByAsfId(asf.id());
        map.put("ASF_CERTIFICATE_TEXT", AsfCertificateTextBuilder.build(cert));

        // ---------- OBJECT TEXT BLOCK ----------
        map.put("OBJ_NAME", obj.objectFullName());
        map.put("OBJ_SHORT_NAME", obj.objectShortName());
        map.put("OBJ_HAZARD_CLASS", HazardUtils.toRoman(String.valueOf(obj.hazardClass())));
        map.put("OBJ_AMOUNT_HAZARDOUS_SUBSTANCE", obj.amountOfHazardousSubstance());
        map.put("OBJ_NEAREST_FIRE_STATION", obj.nearestFireStation());
        map.put("OBJ_DEPARTMENT_GOCHS_CITY", obj.departmentGoChsCity());
        var objAddr = internalServices.objectAddressService().getByObjectId(obj.id());
        //var city = internalServices.objectCityService().getById(objAddr.id());
        map.put("OBJ_ADDRESS_FULL", AddressFormatter.format(objAddr));
        //map.put("OBJ_AREA_LOCATION", TechnicalDescriptionFormatter.format(city));
        var type = internalServices.objectTypeService().getObjectType(obj.id());
        map.put("OBJ_TYPE_DIFINITION", type.typeDefinition());
        var substance = internalServices.hazardousSubstanceService().getById(obj.id());
        map.put("OBJ_HAZARDOUS_SUBSTANCE", substance.name());
        map.put("OBJ_HAZARDOUS_SUBSTANCE_GEN", substance.name_gen());
        var policy = internalServices.objectInsurancePolicyService().getByObjectId(obj.id());
        map.put("OBJ_INSURANCE_POLICY_NUMBER", String.valueOf(policy.number()));
        map.put("OBJ_INSURANCE_POLICY_DATE", DateFormatter.russDate(policy.validUntil()));
        var balance = internalServices.objectOrderMinimumBalanceService().getByObjectId(obj.id());
        map.put("OBJ_ORDER_MINIMUM_BALANCE_NUMBER", String.valueOf(balance.number()));
        map.put("OBJ_ORDER_MINIMUM_BALANCE_DATE", DateFormatter.russDate(balance.date()));
        int techBlocks = internalServices.technologicalBlockService().countByObjectId(obj.id());
        map.put("OBJ_AMOUNT_TECHNOLOGICAL_BLOCK", RussianPlural.technologicalBlock(techBlocks));

        return map;
    }
}