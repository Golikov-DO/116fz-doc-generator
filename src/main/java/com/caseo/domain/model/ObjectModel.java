package com.caseo.domain.model;

public record ObjectModel(int id, int orgId, int hazardousSubstanceId,
                          int hazardClass,
                          String objectFullName,
                          String objectAddress,
                          String amountOfHazardousSubstance,
                          String nearestFireStation,
                          String objectShortName,
                          String departmentGoChsCity) {
}