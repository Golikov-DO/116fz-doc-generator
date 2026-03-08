package com.caseo.domain.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "object_insurance_policy")
public class ObjectInsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    private String number;
    private LocalDate validUntil;

    public ObjectInsurancePolicy() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ObjectModel getObject() { return object; }
    public void setObject(ObjectModel object) { this.object = object; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public LocalDate getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
}