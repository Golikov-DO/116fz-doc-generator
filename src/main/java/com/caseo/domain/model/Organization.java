package com.caseo.domain.model;

public class Organization {
    private int organizationId;
    private String organizationName;
    private String organizationShortName;
    private String organizationAddress;

    public Organization(String organizationName, String organizationShortName, String organizationAddress, int organizationId) {
        this.organizationName = organizationName;
        this.organizationShortName = organizationShortName;
        this.organizationAddress = organizationAddress;
        this.organizationId = organizationId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationShortName() {
        return organizationShortName;
    }

    public void setOrganizationShortName(String organizationShortName) {
        this.organizationShortName = organizationShortName;
    }

    public String getOrganizationAddress() {
        return organizationAddress;
    }

    public void setOrganizationAddress(String organizationAddress) {
        this.organizationAddress = organizationAddress;
    }
}
