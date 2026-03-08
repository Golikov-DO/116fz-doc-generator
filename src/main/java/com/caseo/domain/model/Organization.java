package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organization_full_name")
    private String organizationName;

    private String organizationShortName;
    private String organizationTypeActivity;
    private boolean oneTerritory;

    public Organization() {}

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

    public String getOrganizationTypeActivity() {
        return organizationTypeActivity;
    }

    public void setOrganizationTypeActivity(String organizationTypeActivity) {
        this.organizationTypeActivity = organizationTypeActivity;
    }

    public boolean isOneTerritory() {
        return oneTerritory;
    }

    public void setOneTerritory(boolean oneTerritory) {
        this.oneTerritory = oneTerritory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
