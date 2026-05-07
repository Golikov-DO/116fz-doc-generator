package ru.ecospas.web.helper;

import jakarta.servlet.http.HttpServletRequest;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.util.MapListUtils;
import ru.ecospas.web.util.RequestIndexContext;

import java.util.ArrayList;
import java.util.List;

import static ru.ecospas.web.util.RequestUtils.*;

public class ObjectSaveHelper {
    public void mapObject(HttpServletRequest req, ObjectModel object) {
        object.setObjectFullName(param(req, "object_full_name"));
        object.setHazardClass(paramInt(req, "hazard_class"));
        object.setAmountOfHazardousSubstance(param(req, "amount_of_hazardous_substance"));
        object.setNearestFireStation(param(req, "nearest_fire_station"));
        object.setDepartmentGoChsCity(param(req, "department_gochs"));
        object.setEmergencyCommission(paramBool(req, "emergency_commission"));
        object.setAsfSignerId(paramInt(req,"object_signer_id"));
        object.setArrivalTime(paramTime(req, "arrival_hours", "arrival_minutes"));
    }

    public void mapAddress(HttpServletRequest req, ObjectAddress address) {
        address.setAddressIndex(paramInteger(req, "object_index"));
        address.setConstituentEntity(param(req, "object_constituent_entity"));
        address.setAreaHierarchy(param(req, "object_area"));
        address.setCity(param(req, "object_city"));
        address.setStreet(param(req, "object_street"));
        address.setHouse(param(req, "object_house"));
        address.setCoordinates(param(req, "object_coordinates"));
    }

    public void mapKchs(RequestIndexContext ctx, ObjectCompositionKchs kchs) {
        int i = ctx.index;

        int num = paramInt(ctx.req, "kchs_number[]", i);
        if (num <= 0) kchs.setNumber(i + 1);
        else kchs.setNumber(num);
        kchs.setPosition(param(ctx.req, "kchs_position[]", i));
        kchs.setFullName(param(ctx.req, "kchs_name[]", i));
        kchs.setCellPhone(param(ctx.req, "kchs_phone[]", i));
        kchs.setWorkPhone(param(ctx.req, "kchs_work_phone[]", i));
        kchs.setHomeAddress(param(ctx.req, "kchs_address[]", i));
    }

    public void mapEquipment(RequestIndexContext ctx, ObjectTechnologicalEquipment equipment) {
        int i = ctx.index;

        int num = paramInt(ctx.req, "techno_number[]", i);
        if (num <= 0) equipment.setNum(i + 1);
        else equipment.setNum(num);

        equipment.setName(param(ctx.req, "techno_name[]", i));
        equipment.setCharacteristics(param(ctx.req, "techno_characteristics[]", i));
    }

    public void mapStructure(RequestIndexContext ctx, ObjectStructure structure) {
        int i = ctx.index;

        int num = paramInt(ctx.req, "structure_number[]", i);
        if (num <= 0) structure.setNum(i + 1);
        else structure.setNum(num);

        structure.setName(param(ctx.req, "structure_name[]", i));
        structure.setLikelyIds(param(ctx.req, "likely[]", i));
        structure.setDangerousIds(param(ctx.req, "dangerous[]", i));
    }

    public void mapTechnoBlock(RequestIndexContext ctx, ObjectTechnologicalBlock technoBlock) {
        int i = ctx.index;

        int num = paramInt(ctx.req, "techno_block_number[]", i);
        if (num <= 0) technoBlock.setNum(i + 1);
        else technoBlock.setNum(num);

        technoBlock.setName(param(ctx.req, "techno_block_name[]", i));
    }

    public void mapPersonsResponse(RequestIndexContext ctx, ObjectPersonsResponsible personsResponse) {
        int i = ctx.index;

        int num = paramInt(ctx.req, "persons_response_number[]", i);
        if (num <= 0) personsResponse.setNumber(i + 1);
        else personsResponse.setNumber(num);

        personsResponse.setFullName(param(ctx.req, "persons_response_full_name[]", i));
        personsResponse.setPosition(param(ctx.req, "persons_response_position[]", i));
    }

    public void mapInsurancePolicy(HttpServletRequest req, ObjectInsurancePolicy policy) {
        policy.setNumber(param(req, "insurance_number"));
        policy.setValidUntil(paramDate(req, "insurance_valid_until"));
    }

    public void mapOrderMinimumBalance(HttpServletRequest req, ObjectOrderMinimumBalance balance) {
        balance.setNumber(param(req, "balance_number"));
        balance.setDate(paramDate(req, "balance_date"));
    }

    public void mapFireEquipment(RequestIndexContext ctx, ObjectFireEquipment fireEquipment) {
        int i = ctx.index;

        int num = paramInt(ctx.req, "fire_equipment_number[]", i);
        if (num <= 0) fireEquipment.setNumber(i + 1);
        else fireEquipment.setNumber(num);

        fireEquipment.setProductName(param(ctx.req, "fire_equipment_name_product[]", i));
        fireEquipment.setQuantity(param(ctx.req, "fire_equipment_quantity[]", i));
        fireEquipment.setLocation(param(ctx.req, "fire_equipment_location[]", i));
    }

    public List<ObjectCompositionKchs> mapKchsList(HttpServletRequest req) {
        return MapListUtils.mapList(
                req,
                "kchs_id[]",
                ObjectCompositionKchs::new,
                this::mapKchs
        );
    }

    public List<ObjectTechnologicalEquipment> mapEquipmentList(HttpServletRequest req) {
        return MapListUtils.mapList(
                req,
                "techno_id[]",
                ObjectTechnologicalEquipment::new,
                this::mapEquipment
        );
    }

    public List<ObjectStructure> mapStructureList(HttpServletRequest req) {
        return MapListUtils.mapList(
                req,
                "structure_id[]",
                ObjectStructure::new,
                this::mapStructure
        );
    }

    public List<ObjectTechnologicalBlock> mapTechnoBlockList(HttpServletRequest req) {
        return MapListUtils.mapList(
                req,
                "techno_block_id[]",
                ObjectTechnologicalBlock::new,
                this::mapTechnoBlock
        );
    }

    public List<ObjectPersonsResponsible> mapPersonsResponseList(HttpServletRequest req) {
        return MapListUtils.mapList(
                req,
                "persons_response_id[]",
                ObjectPersonsResponsible::new,
                this::mapPersonsResponse
        );
    }

    public List<ObjectFireEquipment> mapFireEquipmentList(HttpServletRequest req) {
        return MapListUtils.mapList(
                req,
                "fire_equipment_id[]",
                ObjectFireEquipment::new,
                this::mapFireEquipment
        );
    }

    public void mapImages(HttpServletRequest req, List<ObjectImage> images) {

        if (images == null || images.isEmpty()) return;

        for (int g = 1; g <= 4; g++) {

            String caption = param(req, "caption_" + g);
            String link = param(req, "link_" + g);

            for (ObjectImage img : images) {
                if (img.getGroupKey() != null &&
                        img.getGroupKey().equals(String.valueOf(g))) {

                    img.setCaption(caption);
                    img.setLinkText(link);
                    break;
                }
            }
        }
    }

    public List<ObjectScenario> mapScenarios(
            HttpServletRequest req,
            ObjectStructure structure,
            ParentService<Scenario> scenarioService,
            int index
    ) {

        // LIKELY
        String likely = param(req, "likely[]", index);
        List<ObjectScenario> result = new ArrayList<>(parseScenarioList(likely, structure, scenarioService, ScenarioType.LIKELY));

        // DANGEROUS
        String dangerous = param(req, "dangerous[]", index);
        result.addAll(parseScenarioList(dangerous, structure, scenarioService, ScenarioType.DANGEROUS));

        return result;
    }

    private List<ObjectScenario> parseScenarioList(
            String value,
            ObjectStructure structure,
            ParentService<Scenario> scenarioService,
            ScenarioType type
    ) {
        List<ObjectScenario> list = new ArrayList<>();

        if (value == null || value.isEmpty()) return list;

        String[] ids = value.split(",");

        for (String idStr : ids) {
            int id = Integer.parseInt(idStr);

            ObjectScenario item = new ObjectScenario();
            item.setStructure(structure);
            item.setScenario(scenarioService.getOneById(id));
            item.setType(type);

            list.add(item);
        }

        return list;
    }
}