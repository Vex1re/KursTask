package com.vdm.controller;

import com.vdm.dao.CountryDAO;
import com.vdm.model.Country;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class CountryController {
    @FXML private TableView<Country> countryTable;
    @FXML private TableColumn<Country, String> nameColumn;
    @FXML private TableColumn<Country, String> climateColumn;

    private CountryDAO countryDAO = new CountryDAO();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        climateColumn.setCellValueFactory(new PropertyValueFactory<>("climate"));
        try {
            countryTable.setItems(FXCollections.observableArrayList(countryDAO.getAll()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
