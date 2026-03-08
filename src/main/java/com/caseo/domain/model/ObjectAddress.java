package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "object_address")
public class ObjectAddress implements Addressable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;  // вместо objectId

    private Integer addressIndex;
    private String constituentEntity;
    private String areaHierarchy;
    @Column(name = "city_name")  // в БД city_name
    private String city;
    private String street;
    private String house;
    private String coordinates;
    private String rawAddress;

    public ObjectAddress() {}

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ObjectModel getObject() { return object; }
    public void setObject(ObjectModel object) { this.object = object; }

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

    public String getCoordinates() { return coordinates; }
    public void setCoordinates(String coordinates) { this.coordinates = coordinates; }

    @Override
    public String rawAddress() { return rawAddress; }
    public void setRawAddress(String rawAddress) { this.rawAddress = rawAddress; }
}

