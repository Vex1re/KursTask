package com.vdm.controller;

import com.vdm.dao.HotelDAO;
import com.vdm.dao.VoucherDAO;
import com.vdm.model.Hotel;
import com.vdm.model.Voucher;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class VoucherController {
    @FXML private TableView<Voucher> voucherTable;
    @FXML private TableColumn<Voucher, String> hotelColumn;
    @FXML private TableColumn<Voucher, Integer> durationColumn;
    @FXML private TextField durationField;
    @FXML private ComboBox<Hotel> hotelCombo;

    private VoucherDAO voucherDAO = new VoucherDAO();
    private HotelDAO hotelDAO = new HotelDAO();

    @FXML
    public void initialize() {
        hotelColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHotel().getName()));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        
        try {
            hotelCombo.setItems(FXCollections.observableArrayList(hotelDAO.getAll()));
        } catch (SQLException e) { e.printStackTrace(); }
        
        refreshTable();
    }

    private void refreshTable() {
        try {
            voucherTable.setItems(FXCollections.observableArrayList(voucherDAO.getAll()));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleAdd() {
        Hotel selectedHotel = hotelCombo.getValue();
        if (selectedHotel == null) return;
        try {
            int duration = Integer.parseInt(durationField.getText());
            voucherDAO.add(new Voucher((int)(System.currentTimeMillis()%10000), duration, selectedHotel));
            refreshTable();
        } catch (SQLException | NumberFormatException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleUpdate() {
        Voucher selected = voucherTable.getSelectionModel().getSelectedItem();
        Hotel selectedHotel = hotelCombo.getValue();
        if (selected == null || selectedHotel == null) return;
        try {
            selected.setDuration(Integer.parseInt(durationField.getText()));
            selected.setHotel(selectedHotel);
            voucherDAO.update(selected);
            refreshTable();
        } catch (SQLException | NumberFormatException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleDelete() {
        Voucher selected = voucherTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                voucherDAO.delete(selected.getId());
                refreshTable();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @FXML
    public void handleBack(javafx.event.ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/main-view.fxml", event);
    }
}
