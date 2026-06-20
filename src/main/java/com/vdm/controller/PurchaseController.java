package com.vdm.controller;

import com.vdm.dao.DiscountDAO;
import com.vdm.dao.PurchaseDAO;
import com.vdm.dao.VoucherDAO;
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
    @FXML private ComboBox<Voucher> voucherPoolCombo;
    @FXML private TextArea voucherInfoArea;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Discount> discountCombo;
    @FXML private ListView<Voucher> voucherList;
    @FXML private Label totalCostLabel;

    private Client client;
    private List<Voucher> currentVouchers = new ArrayList<>();
    private PurchaseDAO purchaseDAO = new PurchaseDAO();
    private VoucherDAO voucherDAO = new VoucherDAO();
    private DiscountDAO discountDAO = new DiscountDAO();

    public void setClient(Client client) {
        this.client = client;
        clientLabel.setText("Клиент: " + client.getName());
    }

    @FXML
    public void initialize() {
        try {
            voucherPoolCombo.setItems(FXCollections.observableArrayList(voucherDAO.getAll()));
            discountCombo.setItems(FXCollections.observableArrayList(discountDAO.getAll()));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleVoucherSelect() {
        Voucher v = voucherPoolCombo.getValue();
        if (v != null) {
            voucherInfoArea.setText("Отель: " + v.getHotel().getName() + 
                                   "\nКласс: " + v.getHotel().getHotelClass() + 
                                   "\nСтрана: " + v.getHotel().getCountry().getName() + 
                                   "\nКлимат: " + v.getHotel().getCountry().getClimate());
        }
    }

    @FXML
    public void handleAddVoucher() {
        Voucher v = voucherPoolCombo.getValue();
        if (v == null) return;
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
        if (client == null) {
            System.err.println("Client is null!");
            return;
        }
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
