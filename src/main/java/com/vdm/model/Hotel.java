package com.vdm.model;

public class Hotel {
    private int id;
    private String name;
    private String hotelClass;
    private int countryId;

    public Hotel(int id, String name, String hotelClass, int countryId) {
        this.id = id;
        this.name = name;
        this.hotelClass = hotelClass;
        this.countryId = countryId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getHotelClass() { return hotelClass; }
    public void setHotelClass(String hotelClass) { this.hotelClass = hotelClass; }
    public int getCountryId() { return countryId; }
    public void setCountryId(int countryId) { this.countryId = countryId; }
}
