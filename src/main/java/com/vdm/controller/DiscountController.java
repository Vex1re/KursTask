package com.vdm.controller;

import com.vdm.dao.CountryDAO;
import com.vdm.dao.DiscountDAO;
import java.math.BigDecimal;
import com.vdm.model.Country;
import com.vdm.model.Discount;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import com.vdm.model.UserSession;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class DiscountController {
    @FXML private TableView<Discount> discountTable;
    @FXML private TableColumn<Discount, String> nameColumn;
    @FXML private TableColumn<Discount, Integer> sizeColumn;
    @FXML private TextField nameField;
    @FXML private TextField sizeField;
    @FXML private Button addDiscountButton;
    @FXML private Button editDiscountButton;
    @FXML private Button deleteDiscountButton;

    private DiscountDAO discountDAO = new DiscountDAO();

    @FXML
    public void initialize() {
        if (!UserSession.getInstance().isPrivileged()){
            nameField.setDisable(true);
            sizeField.setDisable(true);
            addDiscountButton.setDisable(true);
            editDiscountButton.setDisable(true);
            deleteDiscountButton.setDisable(true);
        }
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        refreshTable();
    }

    private void refreshTable() {
        try {
            discountTable.setItems(FXCollections.observableArrayList(discountDAO.getAll()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAdd() {
        try {
            BigDecimal size = new BigDecimal(sizeField.getText());
            if (size.compareTo(BigDecimal.ZERO) < 0 || size.compareTo(new BigDecimal("100")) > 0) {
                com.vdm.util.ViewUtils.showAlert("Ошибка", "Размер скидки должен быть от 0 до 100");
                return;
            }
            discountDAO.add(new Discount(0, nameField.getText(), size));
            refreshTable();
        } catch (NumberFormatException e) {
            com.vdm.util.ViewUtils.showAlert("Ошибка", "Некорректный формат размера скидки");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUpdate() {
        Discount selected = discountTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setName(nameField.getText());
                selected.setSize(new BigDecimal(sizeField.getText()));
                discountDAO.update(selected);
                refreshTable();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @FXML
    public void handleDelete() {
        Discount selected = discountTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                discountDAO.delete(selected.getId());
                refreshTable();
            } catch (SQLException e) {
                if (e.getErrorCode() == 1451 || e.getMessage().contains("foreign key constraint")) {
                    com.vdm.util.ViewUtils.showAlert("Ошибка", "Невозможно удалить скидку");
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
