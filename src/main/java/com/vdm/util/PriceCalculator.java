package com.vdm.util;

import com.vdm.model.Hotel;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PriceCalculator {
    private static final Map<String, BigDecimal> CLASS_PRICES = Map.of(
        "Эконом", new BigDecimal("1000"),
        "Стандарт", new BigDecimal("2000"),
        "Бизнес", new BigDecimal("3500"),
        "Люкс", new BigDecimal("5000"),
        "Премиум", new BigDecimal("7500")
    );

    public static BigDecimal calculate(Hotel hotel, int durationWeeks, Date orderDate, Date departureDate) {
        BigDecimal base = CLASS_PRICES.getOrDefault(hotel.getHotelClass(), new BigDecimal("2000"));
        BigDecimal price = base.multiply(new BigDecimal(durationWeeks));

        long diffInMillies = Math.abs(departureDate.getTime() - orderDate.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        if (diffInDays < 30) {
            long daysLess = 30 - diffInDays;
            BigDecimal multiplier = BigDecimal.ONE.add(new BigDecimal(daysLess).multiply(new BigDecimal("0.01")));
            price = price.multiply(multiplier);
        }
        return price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
