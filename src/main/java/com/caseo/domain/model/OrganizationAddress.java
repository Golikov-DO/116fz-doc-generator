package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "organization_address")
public class OrganizationAddress implements Addressable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private Integer addressIndex;
    private String constituentEntity;
    private String areaHierarchy;
    @Column(name = "city_name")
    private String city;
    private String street;
    private String house;
    private String rawAddress;

    public OrganizationAddress() {}

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public Integer addressIndex() { return addressIndex; }
    public void setAddressIndex(Integer index) { this.addressIndex = index; }

    @Override
    public String constituentEntity() { return constituentEntity; }
    public void setConstituentEntity(String constituentEntity) { this.constituentEntity = constituentEntity; }

    @Override
    public String areaHierarchy() { return areaHierarchy; }
    public void setAreaHierarchy(String areaHierarchy) { this.areaHierarchy = areaHierarchy; }

    @Override
    public String city() { return city; }
    public void setCity(String city) { this.city = city; }

    @Override
    public String street() { return street; }
    public void setStreet(String street) { this.street = street; }

    @Override
    public String house() { return house; }
    public void setHouse(String house) { this.house = house; }

    @Override
    public String rawAddress() { return rawAddress; }
    public void setRawAddress(String rawAddress) { this.rawAddress = rawAddress; }
}