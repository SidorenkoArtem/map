package com.mapsapp.domain;

public class Coordinates {
    //gis$geodatastore

    private long id;
    private String name;
    private double latitude1;
    private double longitude1;
    private double latitude2;
    private double longitude2;
    private int code;
    private long idLine;
    private long idLineParent;

    public Coordinates() {}

    public void setId(long id) { this.id = id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude1(double latitude1) { this.latitude1 = latitude1; }

    public void setLongitude1(double longitude1) { this.longitude1 = longitude1; }

    public void setLatitude2(double latitude2) { this.latitude2 = latitude2; }

    public void setLongitude2(double longitude2) { this.longitude2 = longitude2; }

    public void setCode(int code) { this.code = code; }

    public void setIdLine(long idLine) { this.idLine = idLine; }

    public void setIdLineParent(long idLineParent) { this.idLineParent = idLineParent; }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude1() {
        return latitude1;
    }

    public double getLongitude1() {
        return longitude1;
    }

    public double getLatitude2() {
        return latitude2;
    }

    public double getLongitude2() {
        return longitude2;
    }

    public int getCode() {
        return code;
    }

    public long getIdLine() { return idLine; }

    public long getIdLineParent() { return idLineParent; }
}
