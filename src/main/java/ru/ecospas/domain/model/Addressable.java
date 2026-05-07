package ru.ecospas.domain.model;

public interface Addressable {
    Integer getAddressIndex();
    String getConstituentEntity();
    String getAreaHierarchy();
    String getCity();
    String getStreet();
    String getHouse();
    String getRawAddress();
}