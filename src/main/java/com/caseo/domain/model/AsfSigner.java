package com.caseo.domain.model;

public class AsfSigner {
    private int id;
    private int asfId;
    private String name;
    private String position;

    public AsfSigner(int id, int asfId, String name, String position) {
        this.id = id;
        this.asfId = asfId;
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
        return asfId;
    }

    public void setOrgId(int orgId) {
        this.asfId = orgId;
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