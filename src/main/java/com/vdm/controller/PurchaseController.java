package com.vdm.controller;

import com.vdm.dao.PurchaseDAO;
import com.vdm.model.Purchase;
import javafx.fxml.FXML;
import java.sql.SQLException;

public class PurchaseController {
    private PurchaseDAO purchaseDAO = new PurchaseDAO();

    @FXML
    public void handleCreatePurchase() {
    }
}
