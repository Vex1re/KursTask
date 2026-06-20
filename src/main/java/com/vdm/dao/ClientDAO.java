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
}
