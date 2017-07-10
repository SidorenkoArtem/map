package com.mapsapp.domain;

import java.util.HashMap;

public class DataAvailability {
    private long id;
    private HashMap<String, String> dataExistence;

    public DataAvailability(long id) {
        this.id = id;
        dataExistence = new HashMap<>();
    }

    public long getId() {
        return id;
    }

    public HashMap<String, String> getDataExistence() {
        return dataExistence;
    }

    public void addValue(String key, String value) {
        dataExistence.put(key, value);
    }

    public boolean containsKey(String key) {
        return dataExistence.containsKey(key);
    }
}
