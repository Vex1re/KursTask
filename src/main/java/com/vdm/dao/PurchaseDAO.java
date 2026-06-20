package com.vdm.dao;

import com.vdm.model.*;
import com.vdm.util.DatabaseConnection;
import java.sql.*;
import java.util.List;

public class PurchaseDAO {
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
            try (PreparedStatement vStmt = conn.prepareStatement(vQuery)) {
                for (VoucherPurchaseItem item : vouchers) {
                    vStmt.setInt(1, item.getVoucher().getId());
                    vStmt.setInt(2, purchase.getId());
                    vStmt.setBigDecimal(3, item.getPrice());
                    vStmt.setDate(4, item.getDepartureDate());
                    vStmt.executeUpdate();
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
