package com.vdm.dao;

import com.vdm.model.Hotel;
import com.vdm.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {
    public List<Hotel> getAll() throws SQLException {
        List<Hotel> hotels = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Hotels")) {
            while (rs.next()) {
                hotels.add(new Hotel(rs.getInt("id_hotel"), rs.getString("name"), rs.getString("class"), rs.getInt("id_country")));
            }
        }
        return hotels;
    }

    public void add(Hotel hotel) throws SQLException {
        String query = "INSERT INTO Hotels (id_hotel, name, class, id_country) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, hotel.getId());
            stmt.setString(2, hotel.getName());
            stmt.setString(3, hotel.getHotelClass());
            stmt.setInt(4, hotel.getCountryId());
            stmt.executeUpdate();
        }
    }

    public void update(Hotel hotel) throws SQLException {
        String query = "UPDATE Hotels SET name = ?, class = ?, id_country = ? WHERE id_hotel = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getHotelClass());
            stmt.setInt(3, hotel.getCountryId());
            stmt.setInt(4, hotel.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Hotels WHERE id_hotel = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
