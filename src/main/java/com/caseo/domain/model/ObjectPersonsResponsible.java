package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "object_responsible_persons")
public class ObjectPersonsResponsible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;  // вместо objectId

    private int number;
    private String fullName;
    private String position;

    public ObjectPersonsResponsible() {}

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ObjectModel getObject() { return object; }
    public void setObject(ObjectModel object) { this.object = object; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
}
