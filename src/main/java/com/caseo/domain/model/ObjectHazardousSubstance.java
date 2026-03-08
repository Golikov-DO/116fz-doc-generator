package com.caseo.domain.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "hazardous_substance")
public class ObjectHazardousSubstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String name_gen;

    // Связь с параметрами
    @OneToMany(mappedBy = "substance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjectHazardousParam> params = new ArrayList<>();

    // Методы для удобной работы со связью
    public void addParam(ObjectHazardousParam param) {
        params.add(param);
        param.setSubstance(this);
    }

    public void removeParam(ObjectHazardousParam param) {
        params.remove(param);
        param.setSubstance(null);
    }

    public ObjectHazardousSubstance() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_gen() {
        return name_gen;
    }

    public void setName_gen(String name_gen) {
        this.name_gen = name_gen;
    }
}
