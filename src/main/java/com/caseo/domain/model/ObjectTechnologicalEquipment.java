package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "object_technological_equipment")
public class ObjectTechnologicalEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    private int num;
    @Column(name = "equipment_name")
    private String name;
    private String characteristics;

    public ObjectTechnologicalEquipment() {}

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ObjectModel getObject() { return object; }
    public void setObject(ObjectModel object) { this.object = object; }

    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCharacteristics() { return characteristics; }
    public void setCharacteristics(String characteristics) { this.characteristics = characteristics; }
}