package com.vdm.controller;

import com.vdm.dao.ClientDAO;
import com.vdm.model.Client;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;

public class ClientController {
    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, String> nameColumn;
    @FXML private TableColumn<Client, String> phoneColumn;

    private ClientDAO clientDAO = new ClientDAO();

    @FXML
    public void handleBack(javafx.event.ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/main-view.fxml", event);
    }

    @FXML
    public void handleCreatePurchase(javafx.event.ActionEvent event) throws Exception {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/purchase.fxml"));
        com.vdm.util.ViewUtils.loadView("/view/purchase.fxml", event);
        PurchaseController controller = loader.getController();
        controller.setClient(selectedClient);
    }

