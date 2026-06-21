package com.vdm.controller;

import com.vdm.dao.DiscountDAO;
import com.vdm.dao.PurchaseDAO;
import com.vdm.dao.VoucherDAO;
import com.vdm.model.*;
import com.vdm.util.PriceCalculator;
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
    @FXML private DatePicker departureDatePicker;
    @FXML private DatePicker orderDatePicker;
    @FXML private ComboBox<Discount> discountCombo;
    @FXML private ListView<VoucherPurchaseItem> voucherList;
    @FXML private Label totalCostLabel;

    private Client client;
    private List<VoucherPurchaseItem> currentVouchers = new ArrayList<>();
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
            List<Discount> discounts = new ArrayList<>();
            discounts.add(new Discount(0, "Без скидки", BigDecimal.ZERO));
            discounts.addAll(discountDAO.getAll());
            discountCombo.setItems(FXCollections.observableArrayList(discounts));
            discountCombo.getSelectionModel().selectFirst();
            discountCombo.setOnAction(e -> calculateTotal());
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
        Date orderDate = (orderDatePicker.getValue() != null) ? Date.valueOf(orderDatePicker.getValue()) : new Date(System.currentTimeMillis());
        Date depDate = (departureDatePicker.getValue() != null) ? Date.valueOf(departureDatePicker.getValue()) : null;
        
        if (v == null || depDate == null) return;
        
        BigDecimal price = PriceCalculator.calculate(v.getHotel(), v.getDuration(), orderDate, depDate);
        VoucherPurchaseItem item = new VoucherPurchaseItem(v, price, depDate);
        currentVouchers.add(item);
        voucherList.getItems().add(item);
        calculateTotal();
    }

    private void calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (VoucherPurchaseItem item : currentVouchers) {
            total = total.add(item.getPrice());
        }
        Discount d = discountCombo.getValue();
        BigDecimal discountSize = (d != null && d.getId() != 0) ? d.getSize().divide(new BigDecimal(100)) : BigDecimal.ZERO;
        total = total.multiply(BigDecimal.ONE.subtract(discountSize));
        totalCostLabel.setText("Итого: " + total.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @FXML
    public void handleCreatePurchase(ActionEvent event) {
        if (client == null) return;
        try {
            Date orderDate = Date.valueOf(orderDatePicker.getValue());
            BigDecimal total = new BigDecimal(totalCostLabel.getText().replace("Итого: ", ""));
            
            int newId = purchaseDAO.getNextId();
            Purchase p = new Purchase(newId, orderDate, client, currentVouchers.size(), total);
            purchaseDAO.createPurchase(p, currentVouchers, discountCombo.getValue());
            com.vdm.util.ViewUtils.loadView("/view/main-view.fxml", event);
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    @FXML
    public void handleBack(ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/client.fxml", event);
    }
}
