package com.vdm.dao;

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
}
