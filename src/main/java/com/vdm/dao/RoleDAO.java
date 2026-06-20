package com.vdm.dao;

import com.vdm.model.Role;
import com.vdm.util.DatabaseConnection;
import java.sql.*;

public class RoleDAO {
    public Role getByRoleName(String name) throws SQLException {
        String query = "SELECT * FROM Roles WHERE name = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Role(rs.getInt("id_role"), rs.getString("name"));
            }
        }
        return null;
    }
    
    public Role getById(int id) throws SQLException {
        String query = "SELECT * FROM Roles WHERE id_role = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Role(rs.getInt("id_role"), rs.getString("name"));
            }
        }
        return null;
    }
}
