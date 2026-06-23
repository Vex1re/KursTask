package com.vdm.model;

public class Voucher {
    private int id;
    private int duration;
    private Hotel hotel;

    public Voucher(int id, int duration, Hotel hotel) {
        this.id = id;
        this.duration = duration;
        this.hotel = hotel;
    }

    public Voucher(int duration, Hotel hotel) {
        this.duration = duration;
        this.hotel = hotel;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }

    @Override
    public String toString() {
        return "Тур #" + id + " (Отель: " + hotel.getName() + ". Длительность: " + duration + " нед.)";
    }
}
