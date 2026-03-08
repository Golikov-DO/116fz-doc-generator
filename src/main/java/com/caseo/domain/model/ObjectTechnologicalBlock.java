package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "object_technological_block")
public class ObjectTechnologicalBlock implements NumberedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "object_id")
    private ObjectModel object;

    private int num;
    private String name;

    public ObjectTechnologicalBlock() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ObjectModel getObject() { return object; }
    public void setObject(ObjectModel object) { this.object = object; }

    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }

    @Override
    public String name() { return name; }
    public void setName(String name) { this.name = name; }
}