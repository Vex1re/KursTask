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
}
