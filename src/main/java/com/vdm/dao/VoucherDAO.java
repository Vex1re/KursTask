package com.vdm.dao;

import com.vdm.model.Hotel;
import com.vdm.model.Voucher;
import com.vdm.model.Country;
import com.vdm.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {
    public List<Voucher> getAll() throws SQLException {
        String query = "SELECT v.id_tour, v.duration, h.id_hotel, h.name as hotel_name, h.class as hotel_class, c.id_country, c.name as country_name, c.climate " +
                       "FROM Vouchers v " +
                       "JOIN Hotels h ON v.id_hotel = h.id_hotel " +
                       "JOIN Countries c ON h.id_country = c.id_country";
        List<Voucher> vouchers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Country country = new Country(rs.getInt("id_country"), rs.getString("country_name"), rs.getString("climate"));
                Hotel hotel = new Hotel(rs.getInt("id_hotel"), rs.getString("hotel_name"), rs.getString("hotel_class"), country);
                vouchers.add(new Voucher(rs.getInt("id_tour"), rs.getInt("duration"), hotel));
            }
        }
        return vouchers;
    }
}
