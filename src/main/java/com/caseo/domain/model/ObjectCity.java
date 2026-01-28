package com.caseo.domain.model;

public class ObjectCity {
    private int id;   // ← тот же id что и в object_address

    private String name;
    private String region;
    private String countryPart;

    private String status;
    private String adminCenter;
    private Integer foundedYear;

    private String geography;
    private String distanceInfo;
    private String transport;
    private String climate;
    private String resortZone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountryPart() {
        return countryPart;
    }

    public void setCountryPart(String countryPart) {
        this.countryPart = countryPart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminCenter() {
        return adminCenter;
    }

    public void setAdminCenter(String adminCenter) {
        this.adminCenter = adminCenter;
    }

    public Integer getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(Integer foundedYear) {
        this.foundedYear = foundedYear;
    }

    public String getGeography() {
        return geography;
    }

    public void setGeography(String geography) {
        this.geography = geography;
    }

    public String getDistanceInfo() {
        return distanceInfo;
    }

    public void setDistanceInfo(String distanceInfo) {
        this.distanceInfo = distanceInfo;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getResortZone() {
        return resortZone;
    }

    public void setResortZone(String resortZone) {
        this.resortZone = resortZone;
    }
}
