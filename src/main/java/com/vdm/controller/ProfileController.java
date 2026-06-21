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
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;

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
        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        String newName = nameField.getText();

        if (!user.getPassword().equals(oldPass)) {
            showAlert("Ошибка", "Старый пароль введен неверно.");
            return;
        }

        if (newPass != null && !newPass.isEmpty()) {
            if (!PasswordValidator.isValid(newPass)) {
                showAlert("Ошибка", "Новый пароль не соответствует требованиям.");
                return;
            }
            user.setPassword(newPass);
        }

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Users SET name = ?, password = ? WHERE id_user = ?")) {
            stmt.setString(1, newName);
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();
            
            user.setName(newName);
            showAlert("Успешно", "Профиль обновлен.");
        } catch (SQLException e) {
            showAlert("Ошибка", "Ошибка БД при обновлении.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void handleBack(javafx.event.ActionEvent event) throws Exception {
        com.vdm.util.ViewUtils.loadView("/view/main-view.fxml", event);
    }
}
