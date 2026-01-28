package com.caseo.domain.model;

public class ObjectStructureP1 implements NumberedItem {

    private int num;
    private String name;

    public ObjectStructureP1(int num, String name) {
        this.num = num;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
