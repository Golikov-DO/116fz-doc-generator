package ru.ecospas.web.dto.request.object;

import java.time.LocalTime;
import java.util.List;

public record SaveObjectRequest(

        Integer organizationId,
        Integer asfId,
        Integer cityId,
        Integer typeId,
        Integer hazardousSubstanceId,

        Integer asfSignerId,
        Integer hazardClass,

        String objectFullName,
        String amountOfHazardousSubstance,
        String nearestFireStation,
        String departmentGoChsCity,
        boolean emergencyCommission,
        LocalTime arrivalTime,

        ObjectAddressRequest address,
        ObjectInsurancePolicyRequest insurancePolicy,
        ObjectOrderMinimumBalanceRequest minimumBalance,

        List<ObjectCompositionKchsRequest> compositionKchs,
        List<ObjectPersonsResponsibleRequest> responsiblePersons,
        List<ObjectFireEquipmentRequest> fireEquipments,
        List<ObjectTechnologicalEquipmentRequest> technologicalEquipments,
        List<ObjectTechnologicalBlockRequest> technologicalBlocks,
        List<ObjectStructureRequest> structures,
        List<ObjectImageRequest> images
) {
}