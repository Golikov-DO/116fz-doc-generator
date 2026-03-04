package com.caseo.word.blocks.text;

import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.util.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TextPlaceholderService {

    private final InternalServices internalServices;

    public TextPlaceholderService(InternalServices internalServices) {
        this.internalServices = internalServices;
    }

    public Map<String, String> build(DocumentSet document) throws SQLException {
        Map<String, String> map = new HashMap<>();

        // Базовые объекты для получения ID и общих данных
        Organization org = internalServices.organizationService().getById(document.orgId());
        //ObjectModel obj = internalServices.objectService().getByOrgId(org.organizationId());
        ObjectModel obj = internalServices.objectService().getById(document.objectId());
        Asf asf = internalServices.asfService().getObjectId(document.orgId());

        // ---------- ORGANIZATION TEXT BLOCK ----------
        var orgAddr = internalServices.organizationAddressService().getByOrganizationId(org.organizationId());
        var orgSigner = internalServices.organizationSignerService().getByOrganizationId(org.organizationId());
        map.put("ORG_ADDRESS_FULL", ObjectAddressFormatter.format(orgAddr));
        map.put("ORG_NAME", org.organizationName());
        map.put("ORG_SHORT_NAME", org.organizationShortName());
        map.put("ORG_SIGNER_NAME", orgSigner.name());
        map.put("ORG_SIGNER_POSITION", orgSigner.position());
        map.put("ORG_TYPE_ACTIVITY", org.organizationTypeActivity());

        // ---------- ASF TEXT BLOCK ----------
        var asfSigner = internalServices.asfSignerService().getByAsfId(asf.id());
        var cert = internalServices.asfCertificateService().getByAsfId(asf.id());
        var funds = internalServices.asfCompositionDeploymentFundsService().getByAsfId(asf.id());
        var personnel = internalServices.asfPersonnelService().getByAsfId(asf.id());
        var specialists = internalServices.asfSpecialistsService().getByAsfId(asf.id());
        var types = internalServices.asfWorkTypeService().getByAsfId(asf.id());

        map.put("ASF_AREA_RESPONSIBILITY", funds.responsibilityArea());
        map.put("ASF_ARRIVAL_TIME", DocumentOutputFormatter.format(asf.arrivalTime()));
        map.put("ASF_AVAILABLE_SPECIALISTS", AsfSpecialistsTextBuilder.build(specialists));
        map.put("ASF_CERTIFICATE_TEXT", AsfCertificateTextBuilder.build(cert));
        map.put("ASF_CERTIFIED_RESCUERS", AsfPersonnelTextBuilder.build(personnel));
        map.put("ASF_CONTACT_NUMBER", funds.dutyOfficerTelephone());
        map.put("ASF_DUTY_OFFICER_PHONE", funds.dutyOfficerTelephone());
        map.put("ASF_E_MAIL", funds.eMail());
        map.put("ASF_FULL_NAME", asf.fullName());
        map.put("ASF_FULL_NAME_GEN", asf.fullNameGen());
        map.put("ASF_NUMBER_BUILDINGS", funds.numberBuildings());
        map.put("ASF_NUMBER_PERSONNEL_LIST", String.valueOf(personnel.staffByList()));
        map.put("ASF_NUMBER_PERSONNEL_STAFF", String.valueOf(personnel.staffByStaffing()));
        map.put("ASF_PLACE_LOCATION", funds.deploymentPlace());
        map.put("ASF_RECEPTION_PHONE", funds.contactTelephone());
        map.put("ASF_SHORT_NAME", asf.shortName());
        map.put("ASF_SIGNER_NAME", asfSigner.name());
        map.put("ASF_SIGNER_POSITION", asfSigner.position());
        map.put("ASF_STATUS_SHORT", asf.statusShort());
        map.put("ASF_TOTAL_BUILDING_AREA", funds.totalArea());
        map.put("ASF_WORK_TYPES", types.stream()
                .map(AsfWorkType::name)
                .collect(Collectors.joining(", ")));

        // ---------- OBJECT TEXT BLOCK ----------
        var balance = internalServices.objectOrderMinimumBalanceService().getByObjectId(obj.id());
        var objAddr = internalServices.objectAddressService().getByObjectId(obj.id());
        var policy = internalServices.objectInsurancePolicyService().getByObjectId(obj.id());
        var substance = internalServices.objectHazardousSubstanceService().getById(obj.id());
        var techBlocks = internalServices.objectTechnologicalBlockService().countByObjectId(obj.id());
        var type = internalServices.objectTypeService().getObjectType(obj.id());

        map.put("OBJ_ADDRESS_FULL", ObjectAddressFormatter.format(objAddr));
        map.put("OBJ_AMOUNT_HAZARDOUS_SUBSTANCE", obj.amountOfHazardousSubstance());
        map.put("OBJ_AMOUNT_TECHNOLOGICAL_BLOCK", DocumentOutputFormatter.format(techBlocks + " технологический блок"));
        map.put("OBJ_DEPARTMENT_GOCHS_CITY", obj.departmentGoChsCity());
        map.put("OBJ_EMERGENCY_COMMISSION", obj.emergencyCommission() == true ? "создана" : "не создана");
        map.put("OBJ_HAZARDOUS_SUBSTANCE", substance.name());
        map.put("OBJ_HAZARDOUS_SUBSTANCE_GEN", substance.name_gen());
        map.put("OBJ_HAZARD_CLASS", DocumentOutputFormatter.toRoman(String.valueOf(obj.hazardClass())));
        map.put("OBJ_INSURANCE_POLICY_DATE", DocumentOutputFormatter.russDate(policy.validUntil()));
        map.put("OBJ_INSURANCE_POLICY_NUMBER", String.valueOf(policy.number()));
        map.put("OBJ_NAME", obj.objectFullName());
        map.put("OBJ_NEAREST_FIRE_STATION", obj.nearestFireStation());
        map.put("OBJ_ORDER_MINIMUM_BALANCE_DATE", DocumentOutputFormatter.russDate(balance.date()));
        map.put("OBJ_ORDER_MINIMUM_BALANCE_NUMBER", String.valueOf(balance.number()));
        map.put("OBJ_SHORT_NAME", obj.objectShortName());
        map.put("OBJ_TYPE_DIFINITION", type.typeDefinition());

        // ---------- IMAGE & CAPTION TEXT BLOCK ----------
        var objImages = internalServices.objectImageService().getByObjectId(obj.id());

        int currentImageDisplayNum = 1;

        for (int i = 1; i <= 4; i++) {
            String currentIdx = String.valueOf(i);
            String linkTextKey = "OBJ_LINC_TEXT_" + i + "_IMAGE";
            String linkNumKey = "OBJ_NUM_" + i + "_IMAGE";
            String captureTextKey = "OBJ_TEXT_CAPTURE_" + i + "_IMAGE";

            var firstInGroupOpt = objImages.stream()
                    .filter(img -> currentIdx.equals(img.groupKey()))
                    .findFirst();

            if (firstInGroupOpt.isPresent()) {
                ObjectImage firstInGroup = firstInGroupOpt.get();
                map.put(linkTextKey, firstInGroup.linkText());
                map.put(linkNumKey, String.valueOf(currentImageDisplayNum));
                map.put(captureTextKey, firstInGroup.caption());
                currentImageDisplayNum++;
            } else {
                map.put(linkTextKey, "DELETE_ME");
                map.put(linkNumKey, "");
                map.put(captureTextKey, "DELETE_ME");
            }
        }

        // ---------- TABLE & NAME LINC TEXT ----------
        var titles = internalServices.referenceTableTitleService().getAll();

        Map<Integer, Boolean> presenceMap = new HashMap<>();
        presenceMap.put(1, !internalServices.objectTechnologicalEquipmentService().getByObjectId(obj.id()).isEmpty());
        presenceMap.put(2, true);
        presenceMap.put(3, !internalServices.objectAccidentScenariosService().getByObjectId(obj.id()).isEmpty());
        presenceMap.put(4, !internalServices.objectMainScenariosService().getByObjectId(obj.id()).isEmpty());
        presenceMap.put(5, !internalServices.objectFireEquipmentService().getByObjectId(obj.id()).isEmpty());
        presenceMap.put(6, true);
        presenceMap.put(7, !internalServices.objectPersonsResponsibleService().getByObjectId(obj.id()).isEmpty());
        presenceMap.put(8, !internalServices.objectCompositionKchsService().getByObjectId(obj.id()).isEmpty());
        presenceMap.put(9, true);

        int currentDisplayNum = 1;

        for (int i = 1; i <= 9; i++) {
            final int currentId = i;
            String numKey = "OBJ_NUM_" + i + "_TABLE";
            String linkKey = "OBJ_LINC_TEXT_" + i + "_TABLE";
            String nameKey = "OBJ_TEXT_NAME_" + i + "_TABLE";

            boolean hasData = presenceMap.getOrDefault(i, false);

            if (hasData) {
                var titleOpt = titles.stream().filter(t -> t.id() == currentId).findFirst();
                if (titleOpt.isPresent()) {
                    map.put(numKey, String.valueOf(currentDisplayNum));
                    map.put(linkKey, titleOpt.get().tableTextLinc());
                    map.put(nameKey, titleOpt.get().tableTextName());
                    currentDisplayNum++;
                }
            } else {
                map.put(numKey, "DELETE_ME");
                map.put(linkKey, "DELETE_ME");
                map.put(nameKey, "DELETE_ME");
            }
        }
        return map;
    }
}