package com.vdm.controller;

import com.vdm.dao.ClientDAO;
import com.vdm.model.Client;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        try {
            clientTable.setItems(FXCollections.observableArrayList(clientDAO.getAll()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
