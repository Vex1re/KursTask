package com.vdm.controller;

import com.vdm.dao.PurchaseDAO;
import com.vdm.model.Purchase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class PurchaseHistoryController {
    @FXML private TableView<Purchase> purchaseTable;
    @FXML private TableColumn<Purchase, Integer> idColumn;
    @FXML private TableColumn<Purchase, String> dateColumn;
    @FXML private TableColumn<Purchase, Double> costColumn;
    @FXML private TableColumn<Purchase, String> clientColumn;

    private PurchaseDAO purchaseDAO = new PurchaseDAO();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        clientColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClient().getName()));
        
        try {
            purchaseTable.setItems(FXCollections.observableArrayList(purchaseDAO.getAllPurchases()));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleBack(javafx.event.ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/main-view.fxml", event);
    }
}
