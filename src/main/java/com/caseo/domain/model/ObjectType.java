package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "object_type")
public class ObjectType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    @Column(name = "object_type_definitions")
    private String typeDefinition;

    public ObjectType() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ObjectModel getObject() { return object; }
    public void setObject(ObjectModel object) { this.object = object; }

    public String getTypeDefinition() { return typeDefinition; }
    public void setTypeDefinition(String typeDefinition) { this.typeDefinition = typeDefinition; }
}