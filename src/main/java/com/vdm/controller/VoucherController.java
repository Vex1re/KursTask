package com.vdm.controller;

import com.vdm.dao.HotelDAO;
import com.vdm.dao.VoucherDAO;
import com.vdm.model.Hotel;
import com.vdm.model.UserSession;
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
    @FXML private ComboBox<String> durationCombo;
    @FXML private ComboBox<Hotel> hotelCombo;
    @FXML private Button addVoucherButton;
    @FXML private Button editVoucherButton;
    @FXML private Button deleteVoucherButton;

    private VoucherDAO voucherDAO = new VoucherDAO();
    private HotelDAO hotelDAO = new HotelDAO();

    @FXML
    public void initialize() {
        if (!UserSession.getInstance().isPrivileged()){
            durationCombo.setDisable(true);
            hotelCombo.setDisable(true);
            addVoucherButton.setDisable(true);
            editVoucherButton.setDisable(true);
            deleteVoucherButton.setDisable(true);
        }
        hotelColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHotel().getName()));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        
        durationCombo.setItems(FXCollections.observableArrayList("1 неделя", "2 недели", "4 недели"));
        try {
            hotelCombo.setItems(FXCollections.observableArrayList(hotelDAO.getAll()));
        } catch (SQLException e) { e.printStackTrace(); }
        
        refreshTable();
    }

    private int getWeeksFromCombo() {
        String selected = durationCombo.getValue();
        if (selected == null) return 0;
        return (selected.contains("1")) ? 1 : (selected.contains("2")) ? 2 : 4;
    }

    private void refreshTable() {
        try {
            voucherTable.setItems(FXCollections.observableArrayList(voucherDAO.getAll()));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    public void handleAdd() {
        Hotel selectedHotel = hotelCombo.getValue();
        int weeks = getWeeksFromCombo();
        if (selectedHotel == null || weeks == 0) return;
        try {
            voucherDAO.add(new Voucher(weeks, selectedHotel));
            refreshTable();
        } catch (SQLException e) { e.printStackTrace(); }
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
