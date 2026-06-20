package com.vdm.controller;

import com.vdm.dao.RoleDAO;
import com.vdm.model.Role;
import com.vdm.dao.UserDAO;
import com.vdm.model.User;
import com.vdm.model.UserSession;
import com.vdm.util.PasswordValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AuthController {
    @FXML private TextField nameField;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void showRegisterView(javafx.event.ActionEvent event) throws Exception {
        loadView("/view/register.fxml", event);
    }

    @FXML
    public void showLoginView(javafx.event.ActionEvent event) throws Exception {
        loadView("/view/auth.fxml", event);
    }

    private void loadView(String fxml, javafx.event.ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleRegister() {
        String login = loginField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        int roleId = 0; 

        if (name.isEmpty() || login.isEmpty()) {
            showAlert("Error", "Name and Login are required.");
            return;
        }

        if (!PasswordValidator.isValid(password)) {
            showAlert("Error", "Password does not meet requirements.");
            return;
        }

        try {
            Role managerRole = new RoleDAO().getByRoleName("Менеджер");
            User newUser = new User((int)(System.currentTimeMillis() % 100000), name, login, password, managerRole);
            if (userDAO.registerUser(newUser)) {
                showAlert("Успех", "Регистрация прошла успешно.");
            } else {
                showAlert("Ошибка", "Регистрация не удалась.");
            }
        } catch (SQLException e) {
            showAlert("Ошибка базы данных", "Регистрация не удалась: " + e.getMessage());
        }
    }
@FXML
public void handleLogin(javafx.event.ActionEvent event) {
    String login = loginField.getText();
    String password = passwordField.getText();

    if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
        showAlert("Error", "Login and password are required.");
        return;
    }

    try {
        User user = userDAO.findByLogin(login);
        if (user != null && user.getPassword().equals(password)) {
            UserSession.getInstance().setUser(user);
            loadView("/view/main-view.fxml", event);
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
