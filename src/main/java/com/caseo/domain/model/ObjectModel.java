package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "object")
public class ObjectModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "asf_id")
    private Asf asf;

    @ManyToOne
    @JoinColumn(name = "object_city_id")
    private ReferenceCity city;

    @ManyToOne
    @JoinColumn(name = "hazardous_substance_id")
    private ObjectHazardousSubstance hazardousSubstance;

    private int asfSignerId;
    private int hazardClass;
    @Column(name = "full_name")
    private String objectFullName;
    private String amountOfHazardousSubstance;
    private String nearestFireStation;
    @Column(name = "short_name")
    private String objectShortName;
    @Column(name = "department_gochs_city")
    private String departmentGoChsCity;
    private boolean emergencyCommission;

    public ObjectModel() {}

    public Integer getId() {
        return id; }
    public void setId(Integer id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Asf getAsf() {
        return asf;
    }
    public void setAsf(Asf asf) {
        this.asf = asf;
    }

    public int getAsfSignerId() {
        return asfSignerId;
    }
    public void setAsfSignerId(int asfSignerId) {
        this.asfSignerId = asfSignerId;
    }

    public ReferenceCity getCity() {
        return city;
    }
    public void setCity(ReferenceCity city) {
        this.city = city;
    }

    public ObjectHazardousSubstance getHazardousSubstance() {
        return hazardousSubstance;
    }
    public void setHazardousSubstance(ObjectHazardousSubstance hazardousSubstance) {
        this.hazardousSubstance = hazardousSubstance;
    }

    public int getHazardClass() {
        return hazardClass;
    }
    public void setHazardClass(int hazardClass) {
        this.hazardClass = hazardClass;
    }

    public String getObjectFullName() {
        return objectFullName;
    }
    public void setObjectFullName(String objectFullName) {
        this.objectFullName = objectFullName;
    }

    public String getAmountOfHazardousSubstance() {
        return amountOfHazardousSubstance;
    }
    public void setAmountOfHazardousSubstance(String amountOfHazardousSubstance) {
        this.amountOfHazardousSubstance = amountOfHazardousSubstance;
    }

    public String getNearestFireStation() {
        return nearestFireStation;
     }
    public void setNearestFireStation(String nearestFireStation) {
        this.nearestFireStation = nearestFireStation;
    }

    public String getObjectShortName() {
        return objectShortName;
    }
    public void setObjectShortName(String objectShortName) {
        this.objectShortName = objectShortName;
    }

    public String getDepartmentGoChsCity() {
        return departmentGoChsCity;
    }
    public void setDepartmentGoChsCity(String departmentGoChsCity) {
        this.departmentGoChsCity = departmentGoChsCity;
    }

    public boolean isEmergencyCommission() {
        return emergencyCommission;
    }
    public void setEmergencyCommission(boolean emergencyCommission) {
        this.emergencyCommission = emergencyCommission;
    }
}