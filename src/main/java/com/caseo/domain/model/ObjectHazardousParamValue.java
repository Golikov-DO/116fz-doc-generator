package com.caseo.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hazardous_param_value")
public class ObjectHazardousParamValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "param_id")
    private ObjectHazardousParam param;

    private String valueText;
    private String sourceInfo;

    public ObjectHazardousParamValue() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }

    public String getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(String sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    public ObjectHazardousParam getParam() {
        return param;
    }

    public void setParam(ObjectHazardousParam param) {
        this.param = param;
    }
}

