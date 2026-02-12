package com.caseo.domain.model;

public record ObjectInsurancePolicy(
        int id,
        int obj_id,
        String number,
        String validUntil
) {
}
