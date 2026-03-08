package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "object_city")
public class ReferenceCity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String geoCoords;
    private String geoRelief;
    private String geoGeology;
    private String climatDesc;
    private String hydroDesc;
    private String infraTransport;
    private String infraEngineering;
    private String infraOrganizations;
    private String nearbyTowns;
    private String massPeoplePlaces;
    private String adminStatus;
    private String distCenters;
    private String cityName;
    
    public ReferenceCity() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getGeoCoords() {
        return geoCoords;
    }

    public void setGeoCoords(String geoCoords) {
        this.geoCoords = geoCoords;
    }

    public String getGeoRelief() {
        return geoRelief;
    }

    public void setGeoRelief(String geoRelief) {
        this.geoRelief = geoRelief;
    }

    public String getGeoGeology() {
        return geoGeology;
    }

    public void setGeoGeology(String geoGeology) {
        this.geoGeology = geoGeology;
    }

    public String getClimateDesc() {
        return climatDesc;
    }

    public void setClimateDesc(String climateDesc) {
        this.climatDesc = climateDesc;
    }

    public String getHydroDesc() {
        return hydroDesc;
    }

    public void setHydroDesc(String hydroDesc) {
        this.hydroDesc = hydroDesc;
    }

    public String getInfraTransport() {
        return infraTransport;
    }

    public void setInfraTransport(String infraTransport) {
        this.infraTransport = infraTransport;
    }

    public String getInfraEngineering() {
        return infraEngineering;
    }

    public void setInfraEngineering(String infraEngineering) {
        this.infraEngineering = infraEngineering;
    }

    public String getInfraOrganizations() {
        return infraOrganizations;
    }

    public void setInfraOrganizations(String infraOrganizations) {
        this.infraOrganizations = infraOrganizations;
    }

    public String getNearbyTowns() {
        return nearbyTowns;
    }

    public void setNearbyTowns(String nearbyTowns) {
        this.nearbyTowns = nearbyTowns;
    }

    public String getMassPeoplePlaces() {
        return massPeoplePlaces;
    }

    public void setMassPeoplePlaces(String massPeoplePlaces) {
        this.massPeoplePlaces = massPeoplePlaces;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getDistCenters() {
        return distCenters;
    }

    public void setDistCenters(String distCenters) {
        this.distCenters = distCenters;
    }
}