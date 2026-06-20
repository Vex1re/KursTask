package com.vdm.controller;

import com.vdm.dao.CountryDAO;
import com.vdm.dao.HotelDAO;
import com.vdm.dao.PurchaseDAO;
import com.vdm.model.Client;
import com.vdm.model.Country;
import com.vdm.model.Hotel;
import com.vdm.model.Purchase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class PurchaseController {
    @FXML private Label clientLabel;
    @FXML private ComboBox<Country> countryCombo;
    @FXML private ComboBox<Hotel> hotelCombo;
    @FXML private ComboBox<String> durationCombo;
    @FXML private DatePicker datePicker;
    @FXML private CheckBox discountCheck;

    private Client client;
    private PurchaseDAO purchaseDAO = new PurchaseDAO();

    public void setClient(Client client) {
        this.client = client;
        clientLabel.setText("Client: " + client.getName());
    }

    @FXML
    public void initialize() {
        durationCombo.setItems(FXCollections.observableArrayList("1 week", "2 weeks", "4 weeks"));
        try {
            countryCombo.setItems(FXCollections.observableArrayList(new CountryDAO().getAll()));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleCountrySelect() {
        Country selected = countryCombo.getValue();
        if (selected != null) {
        }
    }

    @FXML
    public void handleCreatePurchase() {
    }
    
    @FXML
    public void handleBack(javafx.event.ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/client.fxml", event);
    }
}
