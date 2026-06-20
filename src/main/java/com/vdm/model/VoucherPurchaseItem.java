package com.vdm.model;

import java.math.BigDecimal;
import java.sql.Date;

public class VoucherPurchaseItem {
    private Voucher voucher;
    private BigDecimal price;
    private Date departureDate;

    public VoucherPurchaseItem(Voucher voucher, BigDecimal price, Date departureDate) {
        this.voucher = voucher;
        this.price = price;
        this.departureDate = departureDate;
    }

    public Voucher getVoucher() { return voucher; }
    public BigDecimal getPrice() { return price; }
    public Date getDepartureDate() { return departureDate; }

    @Override
    public String toString() {
        return voucher.getHotel().getName() + " (" + price + " руб., " + departureDate + ")";
    }
}
