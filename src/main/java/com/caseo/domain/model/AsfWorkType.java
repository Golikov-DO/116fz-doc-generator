package com.caseo.domain.model;

public class AsfWorkType {

    private final int id;
    private final String name;

    public AsfWorkType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}