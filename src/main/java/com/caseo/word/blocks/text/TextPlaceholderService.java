package com.caseo.word.blocks.text;

import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.ChildService;
import com.caseo.domain.service.ParentService;
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

    public Map<String, String> build(int orgId, int objectId) throws SQLException  {
        Map<String, String> map = new HashMap<>();

        // Базовые объекты для получения ID и общих данных
        ParentService<Organization> orgService = internalServices.getParentService(Organization.class);
        ChildService<OrganizationAddress> addrService = internalServices.getChildService(OrganizationAddress.class);
        ChildService<OrganizationSigner> orgSignerService = internalServices.getChildService(OrganizationSigner.class);

        ParentService<ObjectModel> objService = internalServices.getParentService(ObjectModel.class);
        ChildService<ObjectAddress> objectAddressService = internalServices.getChildService(ObjectAddress.class);
        ParentService<ReferenceHazardousSubstance> objectHazardousSubstanceService = internalServices.getParentService(ReferenceHazardousSubstance.class);
        ChildService<ObjectOrderMinimumBalance> objectOrderMinimumBalanceService = internalServices.getChildService(ObjectOrderMinimumBalance.class);
        ChildService<ObjectInsurancePolicy> objectInsurancePolicyService = internalServices.getChildService(ObjectInsurancePolicy.class);
        ParentService<ObjectType> objectTypeService = internalServices.getParentService(ObjectType.class);
        ChildService<ObjectTechnologicalBlock> objectTechnologicalBlockService = internalServices.getChildService(ObjectTechnologicalBlock.class);
        ChildService<ObjectImage> objectImageService = internalServices.getChildService(ObjectImage.class);
        ChildService<ObjectTechnologicalEquipment> objectTechnologicalEquipmentService = internalServices.getChildService(ObjectTechnologicalEquipment.class);
        ChildService<ObjectAccidentScenarios> objectAccidentScenariosService = internalServices.getChildService(ObjectAccidentScenarios.class);
        ChildService<ObjectMainScenarios> objectMainScenariosService = internalServices.getChildService(ObjectMainScenarios.class);
        ChildService<ObjectFireEquipment> objectFireEquipmentService = internalServices.getChildService(ObjectFireEquipment.class);
        ChildService<ObjectCompositionKchs> objectCompositionKchsService = internalServices.getChildService(ObjectCompositionKchs.class);
        ChildService<ObjectPersonsResponsible> objectPersonsResponsibleService = internalServices.getChildService(ObjectPersonsResponsible.class);

        ParentService<Asf> asfService = internalServices.getParentService(Asf.class);
        ChildService<AsfSigner> asfSignerService = internalServices.getChildService(AsfSigner.class);
        ChildService<AsfCertificate> asfCertificateService = internalServices.getChildService(AsfCertificate.class);
        ChildService<AsfCompositionDeploymentFunds> asfCompositionDeploymentFundsService = internalServices.getChildService(AsfCompositionDeploymentFunds.class);
        ChildService<AsfPersonnel> asfPersonnelService = internalServices.getChildService(AsfPersonnel.class);
        ChildService<AsfSpecialists> asfSpecialistsService = internalServices.getChildService(AsfSpecialists.class);
        ChildService<AsfWorkType> asfWorkTypeService = internalServices.getChildService(AsfWorkType.class);

        Organization org = orgService.getOneById(orgId);
        ObjectModel obj = objService.getOneById(objectId);
        Asf asf = asfService.getOneById(obj.getAsf().getId());

        // ---------- ORGANIZATION TEXT BLOCK ----------
        OrganizationAddress orgAddr = addrService.getOneByParentId(org.getId());
        OrganizationSigner orgSigner = orgSignerService.getOneByParentId(org.getId());
        map.put("ORG_ADDRESS_FULL", ObjectAddressFormatter.format(orgAddr));
        map.put("ORG_NAME", org.getOrganizationName());
        map.put("ORG_SHORT_NAME", org.getOrganizationShortName());
        map.put("ORG_SIGNER_NAME", orgSigner.getName());
        map.put("ORG_SIGNER_POSITION", orgSigner.getPosition());
        map.put("ORG_TYPE_ACTIVITY", org.getOrganizationTypeActivity());

        // ---------- ASF TEXT BLOCK ----------
        var asfSigners = asfSignerService.getManyByParentId(asf.getId());
        AsfSigner asfSigner = asfSigners.stream()
                .filter(s -> s.getId() == obj.getAsfSignerId())
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("ASF signer not found for objectId=" + obj.getId())
                );
        var cert = asfCertificateService.getOneByParentId(asf.getId());
        var funds = asfCompositionDeploymentFundsService.getOneByParentId(asf.getId());
        var personnel = asfPersonnelService.getOneByParentId(asf.getId());
        var specialists = asfSpecialistsService.getOneByParentId(asf.getId());
        var types = asfWorkTypeService.getManyByParentId(asf.getId());

        map.put("ASF_AREA_RESPONSIBILITY", funds.getResponsibilityArea());
        map.put("ASF_ARRIVAL_TIME", DocumentOutputFormatter.format(String.valueOf(asf.getArrivalTime())));
        map.put("ASF_AVAILABLE_SPECIALISTS", AsfSpecialistsTextBuilder.build(specialists));
        map.put("ASF_CERTIFICATE_TEXT", AsfCertificateTextBuilder.build(cert));
        map.put("ASF_CERTIFIED_RESCUERS", AsfPersonnelTextBuilder.build(personnel));
        map.put("ASF_CONTACT_NUMBER", funds.getDutyOfficerTelephone());
        map.put("ASF_DUTY_OFFICER_PHONE", funds.getDutyOfficerTelephone());
        map.put("ASF_E_MAIL", funds.getEMail());
        map.put("ASF_FULL_NAME", asf.getFullName());
        map.put("ASF_FULL_NAME_GEN", asf.getFullNameGen());
        map.put("ASF_NUMBER_BUILDINGS", funds.getNumberBuildings());
        map.put("ASF_NUMBER_PERSONNEL_LIST", String.valueOf(personnel.getStaffByList()));
        map.put("ASF_NUMBER_PERSONNEL_STAFF", String.valueOf(personnel.getStaffByStaffing()));
        map.put("ASF_PLACE_LOCATION", funds.getDeploymentPlace());
        map.put("ASF_RECEPTION_PHONE", funds.getContactTelephone());
        map.put("ASF_SHORT_NAME", asf.getShortName());
        map.put("ASF_SIGNER_NAME", asfSigner.getName());
        map.put("ASF_SIGNER_POSITION", asfSigner.getPosition());
        map.put("ASF_STATUS_SHORT", asf.getStatusShort());
        map.put("ASF_TOTAL_BUILDING_AREA", funds.getTotalArea());
        map.put("ASF_WORK_TYPES", types.stream()
                .map(AsfWorkType::getName)
                .collect(Collectors.joining(", ")));

        // ---------- OBJECT TEXT BLOCK ----------
        var balance = objectOrderMinimumBalanceService.getOneByParentId(obj.getId());
        var objAddr = objectAddressService.getOneByParentId(obj.getId());
        var policy = objectInsurancePolicyService.getOneByParentId(obj.getId());
        var substance = objectHazardousSubstanceService.getOneById(obj.getHazardousSubstance().getId());
        var techBlocks = objectTechnologicalBlockService.getManyByParentId(obj.getId()).size();
        var type = objectTypeService.getOneById(obj.getType().getId());

        map.put("OBJ_ADDRESS_FULL", ObjectAddressFormatter.format(objAddr));
        map.put("OBJ_AMOUNT_HAZARDOUS_SUBSTANCE", obj.getAmountOfHazardousSubstance());
        map.put("OBJ_AMOUNT_TECHNOLOGICAL_BLOCK", DocumentOutputFormatter.format(techBlocks + " технологический блок"));
        map.put("OBJ_DEPARTMENT_GOCHS_CITY", obj.getDepartmentGoChsCity());
        map.put("OBJ_EMERGENCY_COMMISSION", obj.isEmergencyCommission() ? "создана" : "не создана");
        map.put("OBJ_HAZARDOUS_SUBSTANCE", substance.getName());
        map.put("OBJ_HAZARDOUS_SUBSTANCE_GEN", substance.getNameGen());
        map.put("OBJ_HAZARD_CLASS", DocumentOutputFormatter.toRoman(String.valueOf(obj.getHazardClass())));
        map.put("OBJ_INSURANCE_POLICY_DATE", DocumentOutputFormatter.russDate(String.valueOf(policy.getValidUntil())));
        map.put("OBJ_INSURANCE_POLICY_NUMBER", String.valueOf(policy.getNumber()));
        map.put("OBJ_NAME", obj.getObjectFullName());
        map.put("OBJ_NEAREST_FIRE_STATION", obj.getNearestFireStation());
        map.put("OBJ_ORDER_MINIMUM_BALANCE_DATE", DocumentOutputFormatter.russDate(String.valueOf(balance.getDate())));
        map.put("OBJ_ORDER_MINIMUM_BALANCE_NUMBER", String.valueOf(balance.getNumber()));
        map.put("OBJ_SHORT_NAME", type.getType());
        map.put("OBJ_TYPE_DIFINITION", type.getTypeDefinition());

        // ---------- IMAGE & CAPTION TEXT BLOCK ----------
        var objImages = objectImageService.getManyByParentId(obj.getId());

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
                map.put(linkTextKey, firstInGroup.getLinkText());
                map.put(linkNumKey, String.valueOf(currentImageDisplayNum));
                map.put(captureTextKey, firstInGroup.getCaption());
                currentImageDisplayNum++;
            } else {
                map.put(linkTextKey, "DELETE_ME");
                map.put(linkNumKey, "");
                map.put(captureTextKey, "DELETE_ME");
            }
        }

        // ---------- TABLE & NAME LINC TEXT ----------
        ParentService<ReferenceTableTitle> tableTitleService = internalServices.getParentService(ReferenceTableTitle.class);
        var titles = tableTitleService.getMany();

        Map<Integer, Boolean> presenceMap = new HashMap<>();
        presenceMap.put(1, !objectTechnologicalEquipmentService.getManyByParentId(obj.getId()).isEmpty());
        presenceMap.put(2, true);
        presenceMap.put(3, !objectAccidentScenariosService.getManyByParentId(obj.getId()).isEmpty());
        presenceMap.put(4, !objectMainScenariosService.getManyByParentId(obj.getId()).isEmpty());
        presenceMap.put(5, !objectFireEquipmentService.getManyByParentId(obj.getId()).isEmpty());
        presenceMap.put(6, true);
        presenceMap.put(7, !objectPersonsResponsibleService.getManyByParentId(obj.getId()).isEmpty());
        presenceMap.put(8, !objectCompositionKchsService.getManyByParentId(obj.getId()).isEmpty());
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
                    map.put(linkKey, titleOpt.get().getTableTextLinc());
                    map.put(nameKey, titleOpt.get().getTableTextName());
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