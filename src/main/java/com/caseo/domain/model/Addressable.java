package com.caseo.domain.model;

public interface Addressable {
    Integer index();
    String constituentEntity();
    String areaHierarchy();
    String city();
    String street();
    String house();
    String coordinates();
    String rawAddress();
}