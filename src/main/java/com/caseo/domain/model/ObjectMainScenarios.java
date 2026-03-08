package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "object_main_scenarios")
public class ObjectMainScenarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;  // вместо objectId

    @Column(name = "name_equipment")
    private String equipmentName;

    private String event;

    @Column(name = "list_scenarios")
    private String scenariosList;

    public ObjectMainScenarios() {}

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ObjectModel getObject() { return object; }
    public void setObject(ObjectModel object) { this.object = object; }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }

    public String getScenariosList() { return scenariosList; }
    public void setScenariosList(String scenariosList) { this.scenariosList = scenariosList; }
}