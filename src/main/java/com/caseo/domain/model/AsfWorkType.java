package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asf_work_type")
public class AsfWorkType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "asf_id")
    private Asf asf;  // вместо int asfId

    private String name;

    public AsfWorkType() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Asf getAsf() { return asf; }
    public void setAsf(Asf asf) { this.asf = asf; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}