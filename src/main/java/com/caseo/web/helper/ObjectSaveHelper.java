package com.caseo.web.helper;

import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import jakarta.servlet.http.HttpServletRequest;

public class ObjectSaveHelper {

    private final ObjectAddressService objectAddressService;
    private final ObjectCompositionKchsService objectCompositionKchsService;
    private final ObjectTechnologicalEquipmentService objectTechnologicalEquipmentService;
    private final ObjectTypeService objectTypeService;
    private final ObjectInsurancePolicyService objectInsurancePolicyService;
    private final ObjectOrderMinimumBalanceService objectOrderMinimumBalanceService;

    public ObjectSaveHelper(
            ObjectAddressService objectAddressService,
            ObjectCompositionKchsService objectCompositionKchsService,
            ObjectTechnologicalEquipmentService objectTechnologicalEquipmentService,
            ObjectTypeService objectTypeService,
            ObjectInsurancePolicyService objectInsurancePolicyService,
            ObjectOrderMinimumBalanceService objectOrderMinimumBalanceService) {
        this.objectAddressService = objectAddressService;
        this.objectCompositionKchsService = objectCompositionKchsService;
        this.objectTechnologicalEquipmentService = objectTechnologicalEquipmentService;
        this.objectTypeService = objectTypeService;
        this.objectInsurancePolicyService = objectInsurancePolicyService;
        this.objectOrderMinimumBalanceService = objectOrderMinimumBalanceService;
    }

    public void saveObjectDetails(HttpServletRequest req, int index, int objectId) throws Exception {
        // КЧС
        String[] kchsPositions = req.getParameterValues("kchs_position[]");
        String[] kchsNames = req.getParameterValues("kchs_name[]");
        String[] kchsPhones = req.getParameterValues("kchs_phone[]");
        String[] kchsAddresses = req.getParameterValues("kchs_address[]");

        if (kchsPositions != null) {
            for (int i = 0; i < kchsPositions.length; i++) {
                if (kchsPositions[i] != null && !kchsPositions[i].trim().isEmpty()) {
                    ObjectCompositionKchs kchs = new ObjectCompositionKchs(
                            objectId,
                            0,
                            kchsPositions[i],
                            kchsNames != null && kchsNames.length > i ? kchsNames[i] : null,
                            kchsPhones != null && kchsPhones.length > i ? kchsPhones[i] : null,
                            null,
                            kchsAddresses != null && kchsAddresses.length > i ? kchsAddresses[i] : null
                    );
                    objectCompositionKchsService.save(kchs, objectId);
                }
            }
        }

        // Оборудование
        String[] technoNames = req.getParameterValues("techno_name[]");
        String[] technoCharacteristics = req.getParameterValues("techno_characteristics[]");

        if (technoNames != null) {
            for (int i = 0; i < technoNames.length; i++) {
                if (technoNames[i] != null && !technoNames[i].trim().isEmpty()) {
                    ObjectTechnologicalEquipment eq = new ObjectTechnologicalEquipment(
                            0,
                            objectId,
                            0,
                            technoNames[i],
                            technoCharacteristics != null && technoCharacteristics.length > i ? technoCharacteristics[i] : null
                    );
                    objectTechnologicalEquipmentService.save(eq, objectId);
                }
            }
        }

        // Тип объекта
        String[] typeDefinitions = req.getParameterValues("object_type_definition[]");
        if (typeDefinitions != null && typeDefinitions.length > index) {
            ObjectType type = new ObjectType(0, objectId, typeDefinitions[index]);
            objectTypeService.save(type, objectId);
        }

        // Страховка
        String[] insuranceNumbers = req.getParameterValues("insurance_number[]");
        String[] insuranceValidUntil = req.getParameterValues("insurance_valid_until[]");
        if (insuranceNumbers != null && insuranceNumbers.length > index) {
            ObjectInsurancePolicy policy = new ObjectInsurancePolicy(
                    objectId,
                    insuranceNumbers[index],
                    insuranceValidUntil != null && insuranceValidUntil.length > index ? insuranceValidUntil[index] : null
            );
            objectInsurancePolicyService.save(policy, objectId);
        }

        // Приказ
        String[] balanceNumbers = req.getParameterValues("balance_number[]");
        String[] balanceDates = req.getParameterValues("balance_date[]");
        if (balanceNumbers != null && balanceNumbers.length > index) {
            ObjectOrderMinimumBalance balance = new ObjectOrderMinimumBalance(
                    objectId,
                    Integer.parseInt(balanceNumbers[index]),
                    balanceDates != null && balanceDates.length > index ? balanceDates[index] : "1970-01-01"
            );
            objectOrderMinimumBalanceService.save(balance, objectId);
        }

        // Адрес объекта
        String[] indices = req.getParameterValues("object_index[]");
        String[] constituentEntities = req.getParameterValues("object_constituent_entity[]");
        String[] areas = req.getParameterValues("object_area[]");
        String[] cities = req.getParameterValues("object_city[]");
        String[] streets = req.getParameterValues("object_street[]");
        String[] houses = req.getParameterValues("object_house[]");
        String[] coordinates = req.getParameterValues("object_coordinates[]");

        if (indices != null && indices.length > index) {
            int indexValue = 0;
            if (indices[index] != null && !indices[index].isEmpty()) {
                try {
                    indexValue = Integer.parseInt(indices[index]);
                } catch (NumberFormatException e) {
                    indexValue = 0;
                }
            }

            ObjectAddress addr = new ObjectAddress(
                    objectId,
                    indexValue,
                    constituentEntities != null && constituentEntities.length > index ? constituentEntities[index] : null,
                    areas != null && areas.length > index ? areas[index] : null,
                    cities != null && cities.length > index ? cities[index] : null,
                    streets != null && streets.length > index ? streets[index] : null,
                    houses != null && houses.length > index ? houses[index] : null,
                    coordinates != null && coordinates.length > index ? coordinates[index] : null,
                    null // raw_address пока не используем
            );
            objectAddressService.save(addr, objectId);

        }
    }
}