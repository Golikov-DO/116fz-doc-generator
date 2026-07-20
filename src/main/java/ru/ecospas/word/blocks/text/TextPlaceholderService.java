package ru.ecospas.word.blocks.text;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.ReferenceTableTitleRepository;
import ru.ecospas.domain.util.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TextPlaceholderService {

    private final ReferenceTableTitleRepository tableTitleRepository;

    public Map<String, String> build(ObjectModel obj) throws SQLException {
        if (obj == null) {
            return new HashMap<>();
        }

        Map<String, String> map = new HashMap<>();
        Organization org = obj.getOrganization();
        Asf asf = obj.getAsf();

        // ---------- ORGANIZATION TEXT BLOCK ----------
        OrganizationAddress orgAddr = org != null ? org.getAddress() : null;
        OrganizationSigner orgSigner = org != null ? org.getSigners().stream()
                .filter(s -> Boolean.TRUE.equals(s.getIsPrimary()))
                .findFirst()
                .orElse(null) : null;

        map.put("ORG_ADDRESS_FULL", orgAddr != null ? ObjectAddressFormatter.format(orgAddr) : "");
        map.put("ORG_NAME", safe(org != null ? org.getOrganizationName() : null));
        map.put("ORG_SHORT_NAME", safe(org != null ? org.getOrganizationShortName() : null));
        map.put("ORG_SIGNER_NAME", orgSigner != null ? safe(orgSigner.getName()) : "");
        map.put("ORG_SIGNER_POSITION", orgSigner != null ? safe(orgSigner.getPosition()) : "");
        map.put("ORG_TYPE_ACTIVITY", safe(org != null ? org.getOrganizationTypeActivity() : null));

        // ---------- ASF TEXT BLOCK ----------
        if (asf != null) {
            AsfSigner asfSigner = asf.getSigners().stream()
                    .filter(s -> s.getId() != null && obj.getAsfSignerId() != null && s.getId().equals(obj.getAsfSignerId()))
                    .findFirst()
                    .orElse(null);

            AsfCertificate cert = asf.getCertificate();
            AsfCompositionDeploymentFunds funds = asf.getDeployment();
            AsfPersonnel personnel = asf.getPersonnel();
            AsfSpecialists specialists = asf.getSpecialists();
            List<AsfWorkType> types = asf.getWorkTypes();

            map.put("ASF_AREA_RESPONSIBILITY", funds != null ? safe(funds.getResponsibilityArea()) : "");
            map.put("ASF_ARRIVAL_TIME", DocumentOutputFormatter.format(String.valueOf(obj.getArrivalTime())));
            map.put("ASF_AVAILABLE_SPECIALISTS", specialists != null ? AsfSpecialistsTextBuilder.build(specialists) : "");
            map.put("ASF_CERTIFICATE_TEXT", cert != null ? AsfCertificateTextBuilder.build(cert) : "");
            map.put("ASF_CERTIFIED_RESCUERS", personnel != null ? AsfPersonnelTextBuilder.build(personnel) : "");
            map.put("ASF_CONTACT_NUMBER", funds != null ? safe(funds.getDutyOfficerTelephone()) : "");
            map.put("ASF_DUTY_OFFICER_PHONE", funds != null ? safe(funds.getDutyOfficerTelephone()) : "");
            map.put("ASF_E_MAIL", funds != null ? safe(funds.getEMail()) : "");
            map.put("ASF_FULL_NAME", safe(asf.getFullName()));
            map.put("ASF_FULL_NAME_GEN", safe(asf.getFullNameGen()));
            map.put("ASF_NUMBER_BUILDINGS", funds != null ? safe(funds.getNumberBuildings()) : "");
            map.put("ASF_NUMBER_PERSONNEL_LIST", personnel != null ? String.valueOf(personnel.getStaffByList()) : "");
            map.put("ASF_NUMBER_PERSONNEL_STAFF", personnel != null ? String.valueOf(personnel.getStaffByStaffing()) : "");
            map.put("ASF_PLACE_LOCATION", funds != null ? safe(funds.getDeploymentPlace()) : "");
            map.put("ASF_RECEPTION_PHONE", funds != null ? safe(funds.getContactTelephone()) : "");
            map.put("ASF_SHORT_NAME", safe(asf.getShortName()));
            map.put("ASF_SIGNER_NAME", asfSigner != null ? safe(asfSigner.getName()) : "");
            map.put("ASF_SIGNER_POSITION", asfSigner != null ? safe(asfSigner.getPosition()) : "");
            map.put("ASF_STATUS_SHORT", safe(asf.getStatusShort()));
            map.put("ASF_TOTAL_BUILDING_AREA", funds != null ? safe(funds.getTotalArea()) : "");
            map.put("ASF_WORK_TYPES", types != null ? types.stream()
                    .map(AsfWorkType::getName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(", ")) : "");
        } else {
            // ASF null — все ASF поля пустые
            String[] asfFields = {
                    "ASF_AREA_RESPONSIBILITY", "ASF_ARRIVAL_TIME", "ASF_AVAILABLE_SPECIALISTS",
                    "ASF_CERTIFICATE_TEXT", "ASF_CERTIFIED_RESCUERS", "ASF_CONTACT_NUMBER",
                    "ASF_DUTY_OFFICER_PHONE", "ASF_E_MAIL", "ASF_FULL_NAME", "ASF_FULL_NAME_GEN",
                    "ASF_NUMBER_BUILDINGS", "ASF_NUMBER_PERSONNEL_LIST", "ASF_NUMBER_PERSONNEL_STAFF",
                    "ASF_PLACE_LOCATION", "ASF_RECEPTION_PHONE", "ASF_SHORT_NAME",
                    "ASF_SIGNER_NAME", "ASF_SIGNER_POSITION", "ASF_STATUS_SHORT",
                    "ASF_TOTAL_BUILDING_AREA", "ASF_WORK_TYPES"
            };
            for (String field : asfFields) {
                map.put(field, "");
            }
        }

        // ---------- OBJECT TEXT BLOCK ----------
        ObjectOrderMinimumBalance balance = obj.getMinimumBalance();
        ObjectAddress objAddr = obj.getAddress();
        ObjectInsurancePolicy policy = obj.getInsurancePolicy();
        ReferenceHazardousSubstance substance = obj.getHazardousSubstance();
        int techBlocks = obj.getTechnologicalBlocks().size();
        ReferenceType type = obj.getType();

        map.put("OBJ_ADDRESS_FULL", objAddr != null ? ObjectAddressFormatter.format(objAddr) : "");
        map.put("OBJ_AMOUNT_HAZARDOUS_SUBSTANCE", safe(obj.getAmountOfHazardousSubstance()));
        map.put("OBJ_AMOUNT_TECHNOLOGICAL_BLOCK", DocumentOutputFormatter.format(techBlocks + " технологический блок"));
        map.put("OBJ_DEPARTMENT_GOCHS_CITY", safe(obj.getDepartmentGoChsCity()));
        map.put("OBJ_EMERGENCY_COMMISSION", obj.isEmergencyCommission() ? "создана" : "не создана");
        map.put("OBJ_HAZARDOUS_SUBSTANCE", substance != null ? safe(substance.getName()) : "");
        map.put("OBJ_HAZARDOUS_SUBSTANCE_GEN", substance != null ? safe(substance.getNameGen()) : "");
        map.put("OBJ_HAZARD_CLASS", DocumentOutputFormatter.toRoman(String.valueOf(obj.getHazardClass())));
        map.put("OBJ_INSURANCE_POLICY_DATE", policy != null ? DocumentOutputFormatter.russDate(String.valueOf(policy.getValidUntil())) : "");
        map.put("OBJ_INSURANCE_POLICY_NUMBER", policy != null ? String.valueOf(policy.getNumber()) : "");
        map.put("OBJ_NAME", safe(obj.getObjectFullName()));
        map.put("OBJ_NEAREST_FIRE_STATION", safe(obj.getNearestFireStation()));
        map.put("OBJ_ORDER_MINIMUM_BALANCE_DATE", balance != null ? DocumentOutputFormatter.russDate(String.valueOf(balance.getDate())) : "");
        map.put("OBJ_ORDER_MINIMUM_BALANCE_NUMBER", balance != null ? String.valueOf(balance.getNumber()) : "");
        map.put("OBJ_SHORT_NAME", type != null ? safe(type.getType()) : "");
        map.put("OBJ_TYPE_DIFINITION", type != null ? safe(type.getTypeDefinition()) : "");

        // ---------- IMAGE & CAPTION TEXT BLOCK ----------
        List<ObjectImage> objImages = obj.getImages();

        int currentImageDisplayNum = 1;

        for (int i = 1; i <= 4; i++) {
            String currentIdx = String.valueOf(i);
            String linkTextKey = "OBJ_LINC_TEXT_" + i + "_IMAGE";
            String linkNumKey = "OBJ_NUM_" + i + "_IMAGE";
            String captureTextKey = "OBJ_TEXT_CAPTURE_" + i + "_IMAGE";

            var firstInGroupOpt = objImages.stream()
                    .filter(img -> currentIdx.equals(img.getGroupKey()))
                    .findFirst();

            if (firstInGroupOpt.isPresent()) {
                ObjectImage firstInGroup = firstInGroupOpt.get();
                map.put(linkTextKey, safe(firstInGroup.getLinkText()));
                map.put(linkNumKey, String.valueOf(currentImageDisplayNum));
                map.put(captureTextKey, safe(firstInGroup.getCaption()));
                currentImageDisplayNum++;
            } else {
                map.put(linkTextKey, "DELETE_ME");
                map.put(linkNumKey, "");
                map.put(captureTextKey, "DELETE_ME");
            }
        }

        // ---------- TABLE & NAME LINC TEXT ----------
        var titles = tableTitleRepository.findAll();

        Map<Integer, Boolean> presenceMap = new HashMap<>();
        presenceMap.put(1, !obj.getTechnologicalEquipments().isEmpty());
        presenceMap.put(2, true);
        presenceMap.put(3, true);
        presenceMap.put(4, true);
        presenceMap.put(5, !obj.getFireEquipments().isEmpty());
        presenceMap.put(6, true);
        presenceMap.put(7, !obj.getResponsiblePersons().isEmpty());
        presenceMap.put(8, !obj.getCompositionKchs().isEmpty());
        presenceMap.put(9, true);

        int currentDisplayNum = 1;

        for (int i = 1; i <= 9; i++) {
            final int currentId = i;
            String numKey = "OBJ_NUM_" + i + "_TABLE";
            String linkKey = "OBJ_LINC_TEXT_" + i + "_TABLE";
            String nameKey = "OBJ_TEXT_NAME_" + i + "_TABLE";

            boolean hasData = presenceMap.getOrDefault(i, false);

            if (hasData) {
                var titleOpt = titles.stream().filter(tableTitle -> tableTitle.getId() == currentId).findFirst();
                if (titleOpt.isPresent()) {
                    map.put(numKey, String.valueOf(currentDisplayNum));
                    map.put(linkKey, safe(titleOpt.get().getTableTextLinc()));
                    map.put(nameKey, safe(titleOpt.get().getTableTextName()));
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

    private String safe(String value) {
        return value != null ? value : "";
    }
}