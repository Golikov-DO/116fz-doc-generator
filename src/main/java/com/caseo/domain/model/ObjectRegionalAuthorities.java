package com.caseo.domain.model;

public record ObjectRegionalAuthorities(
        int objectId,
        String name,
        String department,
        String phone_number,
        String address
) {
}
