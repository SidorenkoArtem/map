package com.mapsapp.domain;

public class DistrictPowerStation {
    //S_RES

    private long id;
    private String name;
    private long idBelEnergoCompany;

    public DistrictPowerStation() {}

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdBelEnergoCompany(long idBelEnergoCompany) {
        this.idBelEnergoCompany = idBelEnergoCompany;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getIdBelEnergoCompany() {
        return idBelEnergoCompany;
    }
}
