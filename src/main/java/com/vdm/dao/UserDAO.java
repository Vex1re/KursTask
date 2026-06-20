package com.vdm.dao;

import com.vdm.model.User;
import com.vdm.model.Role;
import com.vdm.util.DatabaseConnection;

import java.sql.*;

public class UserDAO {
    public User findByLogin(String login) throws SQLException {
        String query = "SELECT u.*, r.name as role_name FROM Users u JOIN Roles r ON u.id_role = r.id_role WHERE u.login = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role role = new Role(rs.getInt("id_role"), rs.getString("role_name"));
                return new User(
                    rs.getInt("id_user"),
                    rs.getString("name"),
                    rs.getString("login"),
                    rs.getString("password"),
                    role
                );
            }
        }
        return null;
    }

    public boolean registerUser(User user) throws SQLException {
        String query = "INSERT INTO Users (id_user, name, login, password, id_role) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            stmt.setString(4, user.getPassword());
            stmt.setInt(5, user.getRole().getId());
            return stmt.executeUpdate() > 0;
        }
    }
}
