package test.textboxes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class Controller {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField inputField;

    @FXML
    private VBox loginPane;

    @FXML
    private AnchorPane mainPane;

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
    protected void onHelloButtonClick() {
        try {
            int number = Integer.parseInt(inputField.getText());
            int result = integer.square(number);
            welcomeText.setText("number: " + result);
        } catch (NumberFormatException e) {
            welcomeText.setText("Пожалуйста, введите корректное число");
        }
    }
    
    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
    
        if (Database.checkLogin(username, password)) {
            loginPane.setVisible(false);
            mainPane.setVisible(true);
            welcomeText.setText("Добро пожаловать, " + username + "!");

        // обход логина по руту
        } else if ("root".equals(username) && "root".equals(password)){
            loginPane.setVisible(false);
            mainPane.setVisible(true);
            welcomeText.setText("Добро пожаловать путник!");

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
