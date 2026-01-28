package com.caseo.domain.model;

public class HazardousSubstance {

    private Integer id;
    private String code;
    private String name;
    private String name_gen;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
