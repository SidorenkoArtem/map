package com.mapsapp.domain;

public class AirLine {
    //ВЛ

    private long id;
    private long id1; //indfig
    private String name;

    public AirLine() {}

    public void setId(long id) { this.id = id; }

    public void setId1(long id1) {
        this.id1 = id1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public long getId1() { return id1; }

    public String getName() {
        return name;
    }
}
