package com.vdm.controller;

import com.vdm.dao.UserDAO;
import com.vdm.model.User;
import com.vdm.model.UserSession;
import com.vdm.util.PasswordValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.vdm.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProfileController {
    @FXML private TextField nameField;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        User user = UserSession.getInstance().getUser();
        if (user != null) {
            nameField.setText(user.getName());
            loginField.setText(user.getLogin());
        }
    }

    @FXML
    public void handleUpdate() {
        User user = UserSession.getInstance().getUser();
        String newName = nameField.getText();
        String newPassword = passwordField.getText();

        if (!PasswordValidator.isValid(newPassword)) {
            showAlert("Error", "Password does not meet requirements.");
            return;
        }

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Users SET name = ?, password = ? WHERE id_user = ?")) {
            stmt.setString(1, newName);
            stmt.setString(2, newPassword);
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();
            
            user.setName(newName);
            user.setPassword(newPassword);
            showAlert("Success", "Profile updated successfully.");
        } catch (SQLException e) {
            showAlert("Error", "Database error during update.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
