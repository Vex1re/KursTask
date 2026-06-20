package com.vdm.dao;

import com.vdm.model.User;
import com.vdm.util.DatabaseConnection;

import java.sql.*;

public class UserDAO {

    public User findByLogin(String login) throws SQLException {
        String query = "SELECT * FROM Users WHERE login = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id_user"),
                    rs.getString("name"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getInt("id_role")
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
            stmt.setInt(5, user.getRoleId());
            return stmt.executeUpdate() > 0;
        }
    }
}
