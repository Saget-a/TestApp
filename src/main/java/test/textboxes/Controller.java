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
    protected void onHelloButtonClick() {
        try {
            int number = Integer.parseInt(inputField.getText());
            int result = integer.square(number);
            welcomeText.setText("number: " + result);
        } catch (NumberFormatException e) {
            welcomeText.setText("Пожалуйста, введите корректное число");
        }
    }
    
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
    
        if (Database.checkLogin(username, password)) {
            loginPane.setVisible(false);
            mainPane.setVisible(true);
            welcomeText.setText("Добро пожаловать, " + username + "!");
        } else {
            loginError.setText("Неверный логин или пароль");
        }
    }
}