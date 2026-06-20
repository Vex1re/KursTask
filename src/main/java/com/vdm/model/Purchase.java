package com.vdm.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

public class Purchase {
    private int id;
    private Date orderDate;
    private int vouchersCount;
    private BigDecimal totalCost;
    private Client client;
    private List<Voucher> vouchers = new ArrayList<>();

    public Purchase(int id, Date orderDate, Client client, int vouchersCount, BigDecimal totalCost) {
        this.id = id;
        this.orderDate = orderDate;
        this.client = client;
        this.vouchersCount = vouchersCount;
        this.totalCost = totalCost;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public int getVouchersCount() { return vouchersCount; }
    public void setVouchersCount(int vouchersCount) { this.vouchersCount = vouchersCount; }
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public List<Voucher> getVouchers() { return vouchers; }
    public void addVoucher(Voucher voucher) { this.vouchers.add(voucher); }
}
