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
}
