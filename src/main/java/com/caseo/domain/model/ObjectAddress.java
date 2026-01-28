package com.caseo.domain.model;

public class ObjectAddress {
    private int id;
    private Long objectId;

    private Integer index;
    private String constituentEntity;
    private String city;      // может остаться, но не используется как связь
    private String street;
    private String house;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getConstituentEntity() {
        return constituentEntity;
    }

    public void setConstituentEntity(String constituentEntity) {
        this.constituentEntity = constituentEntity;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }
}
