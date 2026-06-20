package com.vdm.controller;

import com.vdm.dao.CountryDAO;
import com.vdm.dao.DiscountDAO;
import com.vdm.dao.HotelDAO;
import com.vdm.dao.PurchaseDAO;
import com.vdm.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class PurchaseController {
    @FXML private Label clientLabel;
    @FXML private ComboBox<Country> countryCombo;
    @FXML private ComboBox<Hotel> hotelCombo;
    @FXML private ComboBox<String> durationCombo;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Discount> discountCombo;
    @FXML private ListView<Voucher> voucherList;
    @FXML private Label totalCostLabel;

    private Client client;
    private List<Voucher> currentVouchers = new ArrayList<>();
    private PurchaseDAO purchaseDAO = new PurchaseDAO();
    private HotelDAO hotelDAO = new HotelDAO();
    private DiscountDAO discountDAO = new DiscountDAO();

    public void setClient(Client client) {
        this.client = client;
        clientLabel.setText("Клиент: " + client.getName());
    }

    @FXML
    public void initialize() {
        durationCombo.setItems(FXCollections.observableArrayList("1 неделя", "2 недели", "4 недели"));
        try {
            countryCombo.setItems(FXCollections.observableArrayList(new CountryDAO().getAll()));
            discountCombo.setItems(FXCollections.observableArrayList(discountDAO.getAll()));
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
        int durationIndex = durationCombo.getSelectionModel().getSelectedIndex();
        if (selectedHotel == null || durationIndex == -1) return;
        
        int weeks = (durationIndex == 0) ? 1 : (durationIndex == 1) ? 2 : 4;
        Voucher v = new Voucher((int)(System.currentTimeMillis()%10000), weeks, selectedHotel);
        currentVouchers.add(v);
        voucherList.getItems().add(v);
        calculateTotal();
    }

    private void calculateTotal() {
        double base = currentVouchers.size() * 1000.0;
        Discount d = discountCombo.getValue();
        double discountSize = (d != null) ? d.getSize().doubleValue() / 100.0 : 0;
        double total = base * (1 - discountSize);
        totalCostLabel.setText("Итого: " + total);
    }

    @FXML
    public void handleCreatePurchase(ActionEvent event) {
        try {
            double total = Double.parseDouble(totalCostLabel.getText().replace("Итого: ", ""));
            Purchase p = new Purchase((int)(System.currentTimeMillis()%100000), Date.valueOf(datePicker.getValue()), client, currentVouchers.size(), BigDecimal.valueOf(total));
            purchaseDAO.createPurchase(p, currentVouchers, discountCombo.getValue());
            com.vdm.util.ViewUtils.loadView("/view/main-view.fxml", event);
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    @FXML
    public void handleBack(ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/client.fxml", event);
    }
}
