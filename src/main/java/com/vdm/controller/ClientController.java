package com.vdm.controller;

import com.vdm.dao.ClientDAO;
import com.vdm.model.Client;
import com.vdm.model.Country;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.util.Objects;
import com.vdm.util.Validator;


public class ClientController {
    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, String> nameColumn;
    @FXML private TableColumn<Client, String> phoneColumn;
    @FXML private TableColumn<Client, String> addressColumn;
    @FXML private TextField nameField;
    @FXML private TextField numField;
    @FXML private TextField adressField;

    private ClientDAO clientDAO = new ClientDAO();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        refreshTable();
    }

    private void refreshTable() {
        try {
            clientTable.setItems(FXCollections.observableArrayList(clientDAO.getAll()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(javafx.event.ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/main-view.fxml", event);
    }

    @FXML public void handleAdd() {
        try{
            if (Objects.equals(nameField.getText(), "") || Objects.equals(numField.getText(), "") || Objects.equals(adressField.getText(), "")){
                showAlert("Ошибка", "Необходимо заполнить все поля");
                return;
            }
            if (!Validator.isValidNumber(numField.getText())){
                showAlert("Ошибка", "Неверный номер телефона");
                return;
            }
            if (!Validator.isValidName(nameField.getText())){
                showAlert("Ошибка", "Неверные ФИО");
                return;
            }
            clientDAO.add(new Client(nameField.getText(), numField.getText(), adressField.getText()));
            refreshTable();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML public void handleEdit() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null){
            try{
                if (!Validator.isValidNumber(numField.getText())){
                    showAlert("Ошибка", "Неверный номер телефона");
                    return;
                }
                if (!Validator.isValidName(nameField.getText())){
                    showAlert("Ошибка", "Неверные ФИО");
                    return;
                }
                selected.setName(nameField.getText());
                selected.setPhoneNumber(numField.getText());
                selected.setAddress(adressField.getText());
                clientDAO.update(selected);
                refreshTable();
            } catch (SQLException e) {e.printStackTrace();}
        }
    }

    @FXML public void handleDelete() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null){
            try{
                clientDAO.delete(selected.getId());
                refreshTable();
            } catch (SQLException e){
                if (e.getErrorCode() == 1451 || e.getMessage().contains("foreign key constraint")) {
                    com.vdm.util.ViewUtils.showAlert("Ошибка", "Невозможно удалить клиента.");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void handleCreatePurchase(javafx.event.ActionEvent event) throws Exception {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/purchase.fxml"));
        javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
        PurchaseController controller = loader.getController();
        controller.setClient(selectedClient);
        
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
