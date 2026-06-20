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
    @FXML private TableColumn<Voucher, BigDecimal> priceColumn;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> durationCombo;
    @FXML private ComboBox<Hotel> hotelCombo;

    private VoucherDAO voucherDAO = new VoucherDAO();
    private HotelDAO hotelDAO = new HotelDAO();

    @FXML
    public void initialize() {
        hotelColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHotel().getName()));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
        
        durationCombo.setItems(FXCollections.observableArrayList("1 неделя", "2 недели", "4 недели"));
    }

    @FXML
    public void handleAdd() {
        Hotel selectedHotel = hotelCombo.getValue();
        int weeks = getWeeksFromCombo();
        try {
            BigDecimal price = new BigDecimal(priceField.getText());
            voucherDAO.add(new Voucher((int)(System.currentTimeMillis()%10000), weeks, selectedHotel, price));
            refreshTable();
        } catch (SQLException | NumberFormatException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleUpdate() {
        Voucher selected = voucherTable.getSelectionModel().getSelectedItem();
        Hotel selectedHotel = hotelCombo.getValue();
        int weeks = getWeeksFromCombo();
        if (selected == null || selectedHotel == null || weeks == 0) return;
        try {
            selected.setDuration(weeks);
            selected.setHotel(selectedHotel);
            voucherDAO.update(selected);
            refreshTable();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleDelete() {
        Voucher selected = voucherTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                voucherDAO.delete(selected.getId());
                refreshTable();
            } catch (SQLException e) {
                if (e.getErrorCode() == 1451 || e.getMessage().contains("foreign key constraint")) {
                    com.vdm.util.ViewUtils.showAlert("Ошибка", "Невозможно удалить путевку, так как она используется в покупках.");
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
