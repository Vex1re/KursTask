package com.vdm.model;

import java.math.BigDecimal;

public class Discount {
    private int id;
    private String name;
    private BigDecimal size;

    public Discount(int id, String name, BigDecimal size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getSize() { return size; }
    public void setSize(BigDecimal size) { this.size = size; }
}
