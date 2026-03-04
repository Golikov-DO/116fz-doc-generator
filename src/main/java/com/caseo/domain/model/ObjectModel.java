package com.caseo.domain.model;

public record ObjectModel(
        int id,
        int orgId,
        int asfId,
        int asf_signer_id,
        int object_city_id,
        int hazardousSubstanceId,
        int hazardClass,
        String objectFullName,
        String amountOfHazardousSubstance,
        String nearestFireStation,
        String objectShortName,
        String departmentGoChsCity,
        boolean emergencyCommission
) {
}