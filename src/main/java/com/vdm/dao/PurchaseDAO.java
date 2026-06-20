package com.vdm.dao;

import com.vdm.model.Purchase;
import com.vdm.util.DatabaseConnection;
import java.sql.*;

public class PurchaseDAO {
    public void createPurchase(Purchase purchase, int[] voucherIds) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        try {
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
