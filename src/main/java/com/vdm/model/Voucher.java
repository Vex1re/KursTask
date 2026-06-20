package com.vdm.model;

public class Voucher {
    private int id;
    private int duration;
    private int hotelId;

    public Voucher(int id, int duration, int hotelId) {
        this.id = id;
        this.duration = duration;
        this.hotelId = hotelId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public int getHotelId() { return hotelId; }
    public void setHotelId(int hotelId) { this.hotelId = hotelId; }
}
