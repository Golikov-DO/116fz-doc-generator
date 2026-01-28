package com.caseo.domain.model;

public class OrgSigner {
    private int id;
    private int orgId;
    private String name;
    private String position;

    public OrgSigner(int id, int orgId, String name, String position) {
        this.id = id;
        this.orgId = orgId;
        this.name = name;
        this.position = position;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}