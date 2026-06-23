package com.vdm.dao;

import com.vdm.model.Country;
import com.vdm.model.Discount;
import com.vdm.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAO {
    public List<Discount> getAll() throws SQLException {
        String query = "SELECT * FROM Discounts";
        List<Discount> discounts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                discounts.add(new Discount(rs.getInt("id_sale"), rs.getString("name"), rs.getBigDecimal("size")));
            }
        }
        return discounts;
    }

    public void add(Discount discount) throws SQLException {
        String query = "INSERT INTO Discounts (name, size) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, discount.getName());
            stmt.setBigDecimal(2, discount.getSize());
            stmt.executeUpdate();
        }
    }

    public void update(Discount discount) throws SQLException {
        String query = "UPDATE Discounts SET name = ?, size = ? WHERE id_sale = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, discount.getName());
            stmt.setBigDecimal(2, discount.getSize());
            stmt.setInt(3, discount.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Discounts WHERE id_sale = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
