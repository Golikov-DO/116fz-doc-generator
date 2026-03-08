package com.caseo.domain.model;

public interface Addressable {
    Integer addressIndex();
    String constituentEntity();
    String areaHierarchy();
    String city();
    String street();
    String house();
    String rawAddress();
}