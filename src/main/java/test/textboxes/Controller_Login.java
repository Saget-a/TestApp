package test.textboxes;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

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

    private static final String BASE_URL = "http://localhost:8080/api/auth";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML private VBox loginPane;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label loginError;

    @FXML private VBox registerPane;
    @FXML private TextField registerUsername;
    @FXML private PasswordField registerPassword;
    @FXML private Label registerStatus;

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

        try {
            URL url = new URL(BASE_URL + "/login");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String json = objectMapper.writeValueAsString(new User(username, password));
            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes());
            }

            int status = con.getResponseCode();
            if (status == 200 && objectMapper.readValue(con.getInputStream(), Boolean.class)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("application-view.fxml"));
                Parent root = loader.load();
                Controller_App Controller_App = loader.getController();
                Controller_App.setWelcomeMessage("Добро пожаловать, " + username + "!");
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene scene = new Scene(root, 800, 500);
                scene.getStylesheets().add(getClass().getResource("app_style.css").toExternalForm());
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();
            } else {
                loginError.setText("Неверный логин или пароль");
            }

        } catch (Exception e) {
            e.printStackTrace();
            loginError.setText("Ошибка соединения с сервером");
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

        try {
            URL url = new URL(BASE_URL + "/register");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String json = objectMapper.writeValueAsString(new User(username, password));
            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes());
            }

            int status = con.getResponseCode();
            if (status == 200) {
                String response = new String(con.getInputStream().readAllBytes());
                if (response.contains("successfully")) {
                    registerStatus.setText("Регистрация успешна!");
                    registerStatus.setTextFill(javafx.scene.paint.Color.GREEN);
                    switchToLogin();
                } else {
                    registerStatus.setText("Пользователь уже существует.");
                    registerStatus.setTextFill(javafx.scene.paint.Color.RED);
                }
            } else {
                registerStatus.setText("Ошибка регистрации.");
                registerStatus.setTextFill(javafx.scene.paint.Color.RED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            registerStatus.setText("Ошибка соединения с сервером");
            registerStatus.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    // Простой класс User для сериализации JSON
    public static class User {
        public String username;
        public String password;

        public User() {}
        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
