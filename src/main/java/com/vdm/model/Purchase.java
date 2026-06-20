package com.vdm.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Purchase {
    private int id;
    private Date orderDate;
    private int vouchersCount;
    private BigDecimal totalCost;
    private int clientId;

    public Purchase(int id, Date orderDate, int vouchersCount, BigDecimal totalCost, int clientId) {
        this.id = id;
        this.orderDate = orderDate;
        this.vouchersCount = vouchersCount;
        this.totalCost = totalCost;
        this.clientId = clientId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public int getVouchersCount() { return vouchersCount; }
    public void setVouchersCount(int vouchersCount) { this.vouchersCount = vouchersCount; }
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
}
