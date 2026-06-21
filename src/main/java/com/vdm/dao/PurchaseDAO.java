package com.vdm.dao;

import com.vdm.model.*;
import com.vdm.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO {
    public List<Purchase> getAllPurchases() throws SQLException {
        List<Purchase> purchases = new ArrayList<>();
        String query = "SELECT p.*, c.name as client_name, c.phone_number, c.address FROM Purchases p JOIN Clients c ON p.id_client = c.id_client";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Client client = new Client(rs.getInt("id_client"), rs.getString("client_name"), rs.getString("phone_number"), rs.getString("address"));
                purchases.add(new Purchase(rs.getInt("id_purchase"), rs.getDate("order_date"), client, rs.getInt("vouchers_count"), rs.getBigDecimal("total_cost")));
            }
        }
        return purchases;
    }

    public int getNextId() throws SQLException {
        String query = "SELECT MAX(id_purchase) FROM Purchases";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
            return 1;
        }
    }

    public void createPurchase(Purchase purchase, List<VoucherPurchaseItem> vouchers, Discount discount) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        try {
            String pQuery = "INSERT INTO Purchases (id_purchase, order_date, vouchers_count, total_cost, id_client) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pStmt = conn.prepareStatement(pQuery)) {
                pStmt.setInt(1, purchase.getId());
                pStmt.setDate(2, purchase.getOrderDate());
                pStmt.setInt(3, purchase.getVouchersCount());
                pStmt.setBigDecimal(4, purchase.getTotalCost());
                pStmt.setInt(5, purchase.getClient().getId());
                pStmt.executeUpdate();
            }

            String vQuery = "INSERT INTO Voucher_Purchase (id_tour, id_purchase, cost, departure_date) VALUES (?, ?, ?, ?)";
            String hcQuery = "INSERT IGNORE INTO Hotel_Client (id_hotel, id_client) VALUES (?, ?)";
            
            try (PreparedStatement vStmt = conn.prepareStatement(vQuery);
                 PreparedStatement hcStmt = conn.prepareStatement(hcQuery)) {
                for (VoucherPurchaseItem item : vouchers) {
                    vStmt.setInt(1, item.getVoucher().getId());
                    vStmt.setInt(2, purchase.getId());
                    vStmt.setBigDecimal(3, item.getPrice());
                    vStmt.setDate(4, item.getDepartureDate());
                    vStmt.executeUpdate();

                    hcStmt.setInt(1, item.getVoucher().getHotel().getId());
                    hcStmt.setInt(2, purchase.getClient().getId());
                    hcStmt.executeUpdate();
                }
            }
            
            if (discount != null && discount.getId() != 0) {
                String dQuery = "INSERT INTO Purchase_Discount (id_sale, id_purchase) VALUES (?, ?)";
                try (PreparedStatement dStmt = conn.prepareStatement(dQuery)) {
                    dStmt.setInt(1, discount.getId());
                    dStmt.setInt(2, purchase.getId());
                    dStmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
