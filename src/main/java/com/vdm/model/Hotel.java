package com.vdm.model;

public class Hotel {
    private int id;
    private String name;
    private String hotelClass;
    private Country country;

    public Hotel(int id, String name, String hotelClass, Country country) {
        this.id = id;
        this.name = name;
        this.hotelClass = hotelClass;
        this.country = country;
    }

    public Hotel(String name, String hotelClass, Country country) {
        this.name = name;
        this.hotelClass = hotelClass;
        this.country = country;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getHotelClass() { return hotelClass; }
    public void setHotelClass(String hotelClass) { this.hotelClass = hotelClass; }
    public Country getCountry() { return country; }
    public void setCountry(Country country) { this.country = country; }

    @Override
    public String toString() {
        return name;
    }
}
