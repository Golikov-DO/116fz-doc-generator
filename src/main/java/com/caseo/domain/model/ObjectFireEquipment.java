package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "object_primary_fire_extinguishing_equipment")
public class ObjectFireEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    @Column(name = "num")
    private int number;

    @Column(name = "name_product")
    private String productName;

    private String quantity;
    private String location;

    public ObjectFireEquipment() {}

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ObjectModel getObject() { return object; }
    public void setObject(ObjectModel object) { this.object = object; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}