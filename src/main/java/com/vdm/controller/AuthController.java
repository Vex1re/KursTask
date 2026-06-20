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
    public void handleRegister() {
        String login = loginField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();

        if (name.isEmpty() || login.isEmpty()) {
            showAlert("Error", "Name and Login are required.");
            return;
        }

        if (!PasswordValidator.isValid(password)) {
            showAlert("Error", "Password does not meet requirements.");
            return;
        }

        try {
            User newUser = new User((int)(System.currentTimeMillis() % 100000), name, login, password, roleId);
            if (userDAO.registerUser(newUser)) {
                showAlert("Success", "Registered successfully.");
            } else {
                showAlert("Error", "Registration failed.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Registration failed: " + e.getMessage());
        }
    }

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
            showAlert("Database Error", "Unable to connect to the database: " + e.getMessage());
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
