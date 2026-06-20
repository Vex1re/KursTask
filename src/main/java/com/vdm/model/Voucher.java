package com.vdm.model;

import java.math.BigDecimal;

public class Voucher {
    private int id;
    private int duration;
    private Hotel hotel;
    private BigDecimal basePrice;

    public Voucher(int id, int duration, Hotel hotel, BigDecimal basePrice) {
        this.id = id;
        this.duration = duration;
        this.hotel = hotel;
        this.basePrice = basePrice;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    @Override
    public String toString() {
        return "Тур #" + id + " (Отель: " + hotel.getName() + ", Цена: " + basePrice + ")";
    }
}
