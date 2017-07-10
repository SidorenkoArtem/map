package com.mapsapp.domain;

public class AirLine10 {
    //ВЛ10

    private long id;
    private long id1; //indfig
    private String name;
    private byte[] scheme;

    public AirLine10() {}

    public void setId(long id) { this.id = id; }

    public void setId1(long id1) {
        this.id1 = id1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScheme(byte[] scheme) {
        this.scheme = scheme;
    }

    public long getId() {
        return id;
    }

    public long getId1() { return id1; }

    public String getName() {
        return name;
    }

    public byte[] getScheme() {
        return scheme;
    }
}
