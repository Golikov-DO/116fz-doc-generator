package ru.ecospas.web.dto.response.object;

import java.time.LocalTime;
import java.util.List;

public record ObjectResponse(

        Integer id,

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

        ObjectAddressResponse address,

        ObjectInsurancePolicyResponse insurancePolicy,

        ObjectOrderMinimumBalanceResponse minimumBalance,

        List<ObjectCompositionKchsResponse> compositionKchs,

        List<ObjectPersonsResponsibleResponse> responsiblePersons,

        List<ObjectFireEquipmentResponse> fireEquipments,

        List<ObjectTechnologicalEquipmentResponse> technologicalEquipments,

        List<ObjectTechnologicalBlockResponse> technologicalBlocks,

        List<ObjectStructureResponse> structures,

        List<ObjectImageResponse> images

) {
}