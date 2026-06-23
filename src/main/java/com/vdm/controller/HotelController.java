package com.vdm.controller;

import com.vdm.dao.CountryDAO;
import com.vdm.dao.HotelDAO;
import com.vdm.model.Country;
import com.vdm.model.Hotel;
import com.vdm.model.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class HotelController {
    @FXML private TableView<Hotel> hotelTable;
    @FXML private TableColumn<Hotel, String> nameColumn;
    @FXML private TableColumn<Hotel, String> classColumn;
    @FXML private TableColumn<Hotel, String> countryColumn;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> classCombo;
    @FXML private ComboBox<Country> countryCombo;
    @FXML private Button addHotelButton;
    @FXML private Button editHotelButton;
    @FXML private Button deleteHotelButton;


    private HotelDAO hotelDAO = new HotelDAO();
    private CountryDAO countryDAO = new CountryDAO();

    @FXML
    public void initialize() {
        if (!UserSession.getInstance().isPrivileged()){
            nameField.setDisable(true);
            classCombo.setDisable(true);
            countryCombo.setDisable(true);
            addHotelButton.setDisable(true);
            editHotelButton.setDisable(true);
            deleteHotelButton.setDisable(true);
        }

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        classColumn.setCellValueFactory(new PropertyValueFactory<>("hotelClass"));
        countryColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCountry().getName()));
        
        classCombo.setItems(FXCollections.observableArrayList("Эконом", "Стандарт", "Бизнес", "Люкс", "Премиум"));
        try {
            countryCombo.setItems(FXCollections.observableArrayList(countryDAO.getAll()));
        } catch (SQLException e) { e.printStackTrace(); }
        
        refreshTable();
    }

    private void refreshTable() {
        try {
            hotelTable.setItems(FXCollections.observableArrayList(hotelDAO.getAll()));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleAdd() {
        Country selectedCountry = countryCombo.getValue();
        String hotelClass = classCombo.getValue();
        if (selectedCountry == null || hotelClass == null) return;
        try {
            hotelDAO.add(new Hotel(nameField.getText(), hotelClass, selectedCountry));
            refreshTable();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleUpdate() {
        Hotel selected = hotelTable.getSelectionModel().getSelectedItem();
        Country selectedCountry = countryCombo.getValue();
        String hotelClass = classCombo.getValue();
        if (selected == null || selectedCountry == null || hotelClass == null) return;
        try {
            selected.setName(nameField.getText());
            selected.setHotelClass(hotelClass);
            selected.setCountry(selectedCountry);
            hotelDAO.update(selected);
            refreshTable();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleDelete() {
        Hotel selected = hotelTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                hotelDAO.delete(selected.getId());
                refreshTable();
            } catch (SQLException e) {
                if (e.getErrorCode() == 1451 || e.getMessage().contains("foreign key constraint")) {
                    com.vdm.util.ViewUtils.showAlert("Ошибка", "Невозможно удалить отель, так как он используется в путевках.");
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
