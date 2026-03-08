package com.caseo.web.helper;

import com.caseo.domain.model.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static com.caseo.web.util.RequestUtils.*;

public class ObjectSaveHelper {
    public void mapObject(HttpServletRequest req, int index, ObjectModel object) {
        object.setObjectFullName(param(req, "object_full_name[]", index));
        object.setObjectShortName(param(req, "object_short_name[]", index));
        object.setHazardClass(paramInt(req, "hazard_class[]", index));
        object.setAmountOfHazardousSubstance(param(req, "amount_of_hazardous_substance[]", index));
        object.setNearestFireStation(param(req, "nearest_fire_station[]", index));
        object.setDepartmentGoChsCity(param(req, "department_gochs[]", index));
        object.setEmergencyCommission(paramBool(req, "emergency_commission[]", index));
        object.setAsfSignerId(paramInt(req,"object_signer_id[]", index));
    }

    public void mapAddress(HttpServletRequest req, int index, ObjectAddress address) {
        address.setAddressIndex(paramInt(req, "object_index[]", index));
        address.setConstituentEntity(param(req, "object_constituent_entity[]", index));
        address.setAreaHierarchy(param(req, "object_area[]", index));
        address.setCity(param(req, "object_city[]", index));
        address.setStreet(param(req, "object_street[]", index));
        address.setHouse(param(req, "object_house[]", index));
        address.setCoordinates(param(req, "object_coordinates[]", index));
    }

    public void mapKchs(HttpServletRequest req, int index, ObjectCompositionKchs kchs) {
        kchs.setPosition(param(req, "kchs_position[]", index));
        kchs.setFullName(param(req, "kchs_name[]", index));
        kchs.setCellPhone(param(req, "kchs_phone[]", index));
        kchs.setHomeAddress(param(req, "kchs_address[]", index));
    }

    public void mapEquipment(HttpServletRequest req, int index, ObjectTechnologicalEquipment equipment) {
        equipment.setNum(paramInt(req, "techno_number[]", index));
        equipment.setName(param(req, "techno_name[]", index));
        equipment.setCharacteristics(param(req, "techno_characteristics[]", index));
    }

    public void mapObjectType(HttpServletRequest req, int index, ObjectType type) {
        type.setTypeDefinition(param(req, "object_type_definition[]", index));
    }

    public void mapInsurancePolicy(HttpServletRequest req, int index, ObjectInsurancePolicy policy) {
        policy.setNumber(param(req, "insurance_number[]", index));
        policy.setValidUntil(paramDate(req, "insurance_valid_until[]", index));
    }

    public void mapOrderMinimumBalance(HttpServletRequest req, int index, ObjectOrderMinimumBalance balance) {
        balance.setNumber(param(req, "balance_number[]", index));
        balance.setDate(paramDate(req, "balance_date[]", index));
    }

    public List<ObjectCompositionKchs> mapKchsList(HttpServletRequest req, int currentObjectIndex) {

        List<ObjectCompositionKchs> list = new ArrayList<>();

        String[] index = req.getParameterValues("kchs_object_index[]");
        String[] ids = req.getParameterValues("kchs_id[]");
        if (index == null) return list;

        for (int i = 0; i < index.length; i++) {
            if (index[i] != null && Integer.parseInt(index[i]) == currentObjectIndex) {
                ObjectCompositionKchs kchs = new ObjectCompositionKchs();

                // Если ID есть, сетим его (Hibernate поймет, что это UPDATE)
                if (ids != null && i < ids.length && ids[i] != null && !ids[i].isEmpty()) {
                    kchs.setId(Integer.parseInt(ids[i]));
                }

                mapKchs(req, i, kchs);
                list.add(kchs);
            }
        }
        System.out.println("KCHS COUNT = " + list.size());
        return list;
    }

    public List<ObjectTechnologicalEquipment> mapEquipmentList(HttpServletRequest req, int currentObjectIndex) {
        List<ObjectTechnologicalEquipment> list = new ArrayList<>();

        String[] index = req.getParameterValues("techno_object_index[]");
        String[] ids = req.getParameterValues("techno_id[]");
        if (index == null) return list;

        for (int i = 0; i < index.length; i++) {
            if (index[i] != null && Integer.parseInt(index[i]) == currentObjectIndex) {
                ObjectTechnologicalEquipment equipment = new ObjectTechnologicalEquipment();

                // Если ID есть, сетим его (Hibernate поймет, что это UPDATE)
                if (ids != null && i < ids.length && ids[i] != null && !ids[i].isEmpty()) {
                    equipment.setId(Integer.parseInt(ids[i]));
                }

                mapEquipment(req, i, equipment);
                list.add(equipment);
            }
        }
        System.out.println("KCHS COUNT = " + list.size());
        return list;
    }
}