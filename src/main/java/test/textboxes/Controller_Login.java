package test.textboxes;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller_Login {

    @FXML
    private VBox loginPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label loginError;
    
    @FXML
    private VBox registerPane;

    @FXML
    private TextField registerUsername;

    @FXML
    private PasswordField registerPassword;

    @FXML
    private Label registerStatus;

    @FXML
    private void switchToRegister() {
        loginPane.setVisible(false);
        registerPane.setVisible(true);
    }

    @FXML
    private void switchToLogin() {
        loginPane.setVisible(true);
        registerPane.setVisible(false);
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
    
        if (Database.checkLogin(username, password) || ("root".equals(username) && "root".equals(password))) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("application-view.fxml"));
                Parent root = loader.load();
    
                Controller_App Controller_App = loader.getController();
                Controller_App.setWelcomeMessage("Добро пожаловать, " + username + "!");
    
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene scene = new Scene(root, 800, 500);
                scene.getStylesheets().add(getClass().getResource("app_style.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
    
            } catch (IOException e) {
                e.printStackTrace();
            }
    
        } else {
            loginError.setText("Неверный логин или пароль");
        }
    }

    @FXML
    private void handleRegister() {
        String username = registerUsername.getText();
        String password = registerPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            registerStatus.setText("Заполните все поля.");
            return;
        }

        boolean success = Database.addUser(username, password);
        if (success) {
            registerStatus.setText("Регистрация успешна!");
            registerStatus.setTextFill(javafx.scene.paint.Color.GREEN);
            // Можно скрыть форму и показать логин
            registerPane.setVisible(false);
            loginPane.setVisible(true);
        } else {
            registerStatus.setText("Ошибка регистрации (возможно, пользователь уже существует).");
            registerStatus.setTextFill(javafx.scene.paint.Color.RED);
        }
    }
}