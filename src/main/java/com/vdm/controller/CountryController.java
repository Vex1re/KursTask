package com.vdm.controller;

import com.vdm.dao.CountryDAO;
import com.vdm.model.Country;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import com.vdm.model.UserSession;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class CountryController {
    @FXML private TableView<Country> countryTable;
    @FXML private TableColumn<Country, String> nameColumn;
    @FXML private TableColumn<Country, String> climateColumn;
    @FXML private TextField nameField;
    @FXML private TextField climateField;
    @FXML private Button addCountryButton;
    @FXML private Button editCountryButton;
    @FXML private Button deleteCountryButton;

    private CountryDAO countryDAO = new CountryDAO();

    @FXML
    public void initialize() {
        if (!UserSession.getInstance().isPrivileged()){
            nameField.setDisable(true);
            climateField.setDisable(true);
            addCountryButton.setDisable(true);
            editCountryButton.setDisable(true);
            deleteCountryButton.setDisable(true);
        }
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        climateColumn.setCellValueFactory(new PropertyValueFactory<>("climate"));
        refreshTable();
    }

    private void refreshTable() {
        try {
            countryTable.setItems(FXCollections.observableArrayList(countryDAO.getAll()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAdd() {
        try {
            countryDAO.add(new Country(nameField.getText(), climateField.getText()));
            refreshTable();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleUpdate() {
        Country selected = countryTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setName(nameField.getText());
                selected.setClimate(climateField.getText());
                countryDAO.update(selected);
                refreshTable();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @FXML
    public void handleDelete() {
        Country selected = countryTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                countryDAO.delete(selected.getId());
                refreshTable();
            } catch (SQLException e) {
                if (e.getErrorCode() == 1451 || e.getMessage().contains("foreign key constraint")) {
                    com.vdm.util.ViewUtils.showAlert("Ошибка", "Невозможно удалить страну, так как она используется в отелях.");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void handleBack(javafx.event.ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/main-view.fxml", event);
    }
}
