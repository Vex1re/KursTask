package com.vdm.dao;

import com.vdm.model.Country;
import com.vdm.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {
    public List<Country> getAll() throws SQLException {
        List<Country> countries = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Countries")) {
            while (rs.next()) {
                countries.add(new Country(rs.getInt("id_country"), rs.getString("name"), rs.getString("climate")));
            }
        }
        return countries;
    }

    public void add(Country country) throws SQLException {
        String query = "INSERT INTO Countries (name, climate) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, country.getName());
            stmt.setString(2, country.getClimate());
            stmt.executeUpdate();
        }
    }

    public void update(Country country) throws SQLException {
        String query = "UPDATE Countries SET name = ?, climate = ? WHERE id_country = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, country.getName());
            stmt.setString(2, country.getClimate());
            stmt.setInt(3, country.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Countries WHERE id_country = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
