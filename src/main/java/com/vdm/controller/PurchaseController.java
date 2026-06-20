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
import java.sql.Date;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import com.vdm.model.Voucher;

public class PurchaseController {
    @FXML private Label clientLabel;
    @FXML private ComboBox<Country> countryCombo;
    @FXML private ComboBox<Hotel> hotelCombo;
    @FXML private ComboBox<String> durationCombo;
    @FXML private DatePicker datePicker;
    @FXML private CheckBox discount5Check;
    @FXML private CheckBox discount20Check;
    @FXML private ListView<Voucher> voucherList;
    @FXML private Label totalCostLabel;

    private Client client;
    private List<Voucher> currentVouchers = new ArrayList<>();
    private PurchaseDAO purchaseDAO = new PurchaseDAO();
    private HotelDAO hotelDAO = new HotelDAO();

    public void setClient(Client client) {
        this.client = client;
        clientLabel.setText("Клиент: " + client.getName());
    }

    @FXML
    public void initialize() {
        durationCombo.setItems(FXCollections.observableArrayList("1 неделя", "2 недели", "4 недели"));
        try {
            countryCombo.setItems(FXCollections.observableArrayList(new CountryDAO().getAll()));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleCountrySelect() {
        Country selected = countryCombo.getValue();
        if (selected != null) {
            try {
                hotelCombo.setItems(FXCollections.observableArrayList(hotelDAO.getAll().stream().filter(h -> h.getCountry().getId() == selected.getId()).toList()));
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @FXML
    public void handleAddVoucher() {
        Hotel selectedHotel = hotelCombo.getValue();
        int duration = durationCombo.getSelectionModel().getSelectedIndex();
        if (selectedHotel == null || duration == -1) return;
        
        int weeks = (duration == 0) ? 1 : (duration == 1) ? 2 : 4;
        Voucher v = new Voucher((int)(System.currentTimeMillis()%10000), weeks, selectedHotel);
        currentVouchers.add(v);
        voucherList.getItems().add(v);
        calculateTotal();
    }

    private void calculateTotal() {
        double base = currentVouchers.size() * 1000.0;
        double discount = 0;
        if (discount5Check.isSelected()) discount += 0.05;
        if (discount20Check.isSelected()) discount += 0.20;
        double total = base * (1 - discount);
        totalCostLabel.setText("Итого: " + total);
    }

    @FXML
    public void handleCreatePurchase(ActionEvent event) {
        try {
            Purchase p = new Purchase((int)(System.currentTimeMillis()%100000), Date.valueOf(datePicker.getValue()), client);
            purchaseDAO.createPurchase(p, currentVouchers, false, false);
            com.vdm.util.ViewUtils.loadView("/view/main-view.fxml", event);
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    @FXML
    public void handleBack(ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/client.fxml", event);
    }
}
