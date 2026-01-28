package com.caseo.domain.model;

public class Table1 {

    private int id;
    private int num;
    private String name;
    private String characteristics;
    private int objectId;

    public Table1(int id, int num, String name, String characteristics, int objectId) {
        this.id = id;
        this.num = num;
        this.name = name;
        this.characteristics = characteristics;
        this.objectId = objectId;
    }

    public int getId() { return id; }
    public int getNum() { return num; }
    public String getName() { return name; }
    public String getCharacteristics() { return characteristics; }
    public int getObjectId() { return objectId; }
}