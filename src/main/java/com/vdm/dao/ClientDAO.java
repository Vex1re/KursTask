package com.vdm.dao;

import com.vdm.model.Client;
import com.vdm.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    public List<Client> getAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Clients")) {
            while (rs.next()) {
                clients.add(new Client(rs.getInt("id_client"), rs.getString("name"), rs.getString("phone_number"), rs.getString("address")));
            }
        }
        return clients;
    }

    public void add(Client client) throws SQLException {
        String query = "INSERT INTO Clients (name, phone_number, address) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPhoneNumber());
            stmt.setString(3, client.getAddress());
            stmt.executeUpdate();
        }
    }

    public void update(Client client) throws SQLException {
        String query = "UPDATE Clients SET name = ?, phone_number = ?, address = ? WHERE id_client = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPhoneNumber());
            stmt.setString(3, client.getAddress());
            stmt.setInt(4, client.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Clients WHERE id_client = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
