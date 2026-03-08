package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "object_development_accident_scenarios")
public class ObjectAccidentScenarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    private String scenarios;

    @Column(name = "development_scheme")
    private String scheme;

    public ObjectAccidentScenarios() {}

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ObjectModel getObject() { return object; }
    public void setObject(ObjectModel object) { this.object = object; }

    public String getScenarios() { return scenarios; }
    public void setScenarios(String scenarios) { this.scenarios = scenarios; }

    public String getScheme() { return scheme; }
    public void setScheme(String scheme) { this.scheme = scheme; }
}