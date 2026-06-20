package com.vdm.controller;

import com.vdm.model.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class MainController {
    @FXML private Button editCountriesButton;
    @FXML private Button editHotelsButton;
    @FXML private Button editVouchersButton;
    @FXML private Button editClientsButton;
    @FXML private Button createPurchaseButton;
    @FXML private Button purchaseHistoryButton;

    @FXML
    public void initialize() {
        if (!UserSession.getInstance().isPrivileged()) {
            editCountriesButton.setDisable(true);
            editHotelsButton.setDisable(true);
            editVouchersButton.setDisable(true);
            purchaseHistoryButton.setDisable(true);
        }
        if (!UserSession.getInstance().canManageClients()) {
            editClientsButton.setDisable(true);
        }
    }

    @FXML
    public void handlePurchaseHistory(ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/purchase-history.fxml", event);
    }

    @FXML
    public void handleLogout(ActionEvent event) throws Exception {
        UserSession.getInstance().clearSession();
        com.vdm.util.ViewUtils.loadView("/view/auth.fxml", event);
    }

    @FXML
    public void handleManageCountries(ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/country.fxml", event);
    }

    @FXML
    public void handleManageHotels(ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/hotel.fxml", event);
    }

    @FXML
    public void handleManageVouchers(ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/voucher.fxml", event);
    }

    @FXML
    public void handleManageClients(ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/client.fxml", event);
    }
    
    @FXML
    public void handleCreatePurchase(ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/purchase.fxml", event);
    }
}
