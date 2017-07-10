package com.mapsapp.domain;

public class Sector {
    //S_UCH

    private long id;
    private String name;
    private long idDistrictPowerStation;

    public Sector() {}

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdDistrictPowerStation(long idDistrictPowerStation) {
        this.idDistrictPowerStation = idDistrictPowerStation;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getIdDistrictPowerStation() {
        return idDistrictPowerStation;
    }
}
