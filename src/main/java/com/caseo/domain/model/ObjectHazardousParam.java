package com.caseo.domain.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hazardous_param")
public class ObjectHazardousParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "substance_id")
    private ObjectHazardousSubstance substance;

    private String sectionNo;
    private String title;
    private String subtitle;

    // Связь со значениями
    @OneToMany(mappedBy = "param", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjectHazardousParamValue> values = new ArrayList<>();

    public void addValue(ObjectHazardousParamValue value) {
        values.add(value);
        value.setParam(this);
    }

    public ObjectHazardousParam() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSectionNo() {
        return sectionNo;
    }

    public void setSectionNo(String sectionNo) {
        this.sectionNo = sectionNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public ObjectHazardousSubstance getSubstance() {
        return substance;
    }

    public void setSubstance(ObjectHazardousSubstance substance) {
        this.substance = substance;
    }
}

