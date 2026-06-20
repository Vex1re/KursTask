package com.vdm.controller;

import com.vdm.dao.UserDAO;
import com.vdm.model.User;
import com.vdm.model.UserSession;
import com.vdm.util.PasswordValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AuthController {
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void handleLogin() {
        String login = loginField.getText();
        String password = passwordField.getText();

        try {
            User user = userDAO.findByLogin(login);
            if (user != null && user.getPassword().equals(password)) {
                UserSession.getInstance().setUser(user);
            } else {
                showAlert("Error", "Invalid credentials");
            }
        } catch (SQLException e) {
            showAlert("Error", "Database error");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
