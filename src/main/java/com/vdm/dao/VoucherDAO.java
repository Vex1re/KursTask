package com.vdm.dao;

import com.vdm.model.Hotel;
import com.vdm.model.Country;
import com.vdm.model.Voucher;
import com.vdm.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {
    public List<Voucher> getAll() throws SQLException {
        List<Voucher> vouchers = new ArrayList<>();
        String query = "SELECT v.id_tour, v.duration, h.id_hotel, h.name as hotel_name, h.class as hotel_class, c.id_country, c.name as country_name, c.climate " +
                       "FROM Vouchers v " +
                       "JOIN Hotels h ON v.id_hotel = h.id_hotel " +
                       "JOIN Countries c ON h.id_country = c.id_country";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Country country = new Country(rs.getInt("id_country"), rs.getString("country_name"), rs.getString("climate"));
                Hotel hotel = new Hotel(rs.getInt("id_hotel"), rs.getString("hotel_name"), rs.getString("hotel_class"), country);
                vouchers.add(new Voucher(rs.getInt("id_tour"), rs.getInt("duration"), hotel));
            }
        }
        return vouchers;
    }

    public void add(Voucher voucher) throws SQLException {
        String query = "INSERT INTO Vouchers (id_tour, duration, id_hotel) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, voucher.getId());
            stmt.setInt(2, voucher.getDuration());
            stmt.setInt(3, voucher.getHotel().getId());
            stmt.executeUpdate();
        }
    }

    public void update(Voucher voucher) throws SQLException {
        String query = "UPDATE Vouchers SET duration = ?, id_hotel = ? WHERE id_tour = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, voucher.getDuration());
            stmt.setInt(2, voucher.getHotel().getId());
            stmt.setInt(3, voucher.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Vouchers WHERE id_tour = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
