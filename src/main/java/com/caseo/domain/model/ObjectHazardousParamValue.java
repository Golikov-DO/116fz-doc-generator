package com.caseo.domain.model;

public record ObjectHazardousParamValue(
        int id, Integer paramId,
        String valueText,
        String sourceInfo
) {
}

