package com.vdm.model;

public class Country {
    private int id;
    private String name;
    private String climate;

    public Country(int id, String name, String climate) {
        this.id = id;
        this.name = name;
        this.climate = climate;
    }

    public Country(String name, String climate) {
        this.name = name;
        this.climate = climate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getClimate() { return climate; }
    public void setClimate(String climate) { this.climate = climate; }

    @Override
    public String toString() {
        return name;
    }
}
