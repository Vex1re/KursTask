package com.vdm.controller;

import com.vdm.dao.ClientDAO;
import com.vdm.model.Client;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;

public class ClientController {
    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, String> nameColumn;
    @FXML private TableColumn<Client, String> phoneColumn;
    @FXML private TableColumn<Client, String> addressColumn;

    private ClientDAO clientDAO = new ClientDAO();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        refreshTable();
    }

    private void refreshTable() {
        try {
            clientTable.setItems(FXCollections.observableArrayList(clientDAO.getAll()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(javafx.event.ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/main-view.fxml", event);
    }

    @FXML public void handleAdd() {  }
    @FXML public void handleEdit() {  }
    @FXML public void handleDelete() {  }

    @FXML
    public void handleCreatePurchase(javafx.event.ActionEvent event) throws Exception {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/purchase.fxml"));
        javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
        PurchaseController controller = loader.getController();
        controller.setClient(selectedClient);
        
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
