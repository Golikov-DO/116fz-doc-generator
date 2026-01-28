package com.caseo.domain.model;

public class ObjectModel {
    private int id;
    private int orgId;
    private int hazardousSubstanceId;
    private byte[] planAndDiagram;
    private String objectName;
    private String objectAddress;
    private String amountOfHazardousSubstance;
    private int hazardClass;

    public ObjectModel(int id, int orgId, int hazardousSubstanceId, byte[] planAndDiagram, String objectName, String objectAddress, String amountOfHazardousSubstance, int hazardClass) {
        this.id = id;
        this.orgId = orgId;
        this.hazardousSubstanceId = hazardousSubstanceId;
        this.planAndDiagram = planAndDiagram;
        this.objectName = objectName;
        this.objectAddress = objectAddress;
        this.amountOfHazardousSubstance = amountOfHazardousSubstance;
        this.hazardClass = hazardClass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getHazardousSubstanceId() {
        return hazardousSubstanceId;
    }

    public void setHazardousSubstanceId(int hazardousSubstanceId) {
        this.hazardousSubstanceId = hazardousSubstanceId;
    }

    public byte[] getPlanAndDiagram() {
        return planAndDiagram;
    }

    public void setPlanAndDiagram(byte[] planAndDiagram) {
        this.planAndDiagram = planAndDiagram;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectAddress() {
        return objectAddress;
    }

    public void setObjectAddress(String objectAddress) {
        this.objectAddress = objectAddress;
    }

    public String getAmountOfHazardousSubstance() {
        return amountOfHazardousSubstance;
    }

    public void setAmountOfHazardousSubstance(String amountOfHazardousSubstance) {
        this.amountOfHazardousSubstance = amountOfHazardousSubstance;
    }

    public int getHazardClass() {
        return hazardClass;
    }

    public void setHazardClass(int hazardClass) {
        this.hazardClass = hazardClass;
    }
}