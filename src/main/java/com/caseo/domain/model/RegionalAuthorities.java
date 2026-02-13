package com.caseo.domain.model;

public record RegionalAuthorities(
        int objectId,
        String name,
        String department,
        String phone_number,
        String address
) {
}
