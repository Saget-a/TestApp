package test.textboxes;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Controller_App {

    private static final String BASE_URL = "http://localhost:8080/api/auth";
    
    @FXML
    private Label welcomeText;

    @FXML
    private TextField inputField;

    @FXML
    private AnchorPane mainPane;
    
    @FXML 
    public void setWelcomeMessage(String message) {
        welcomeText.setText(message);
    }

    @FXML
    protected void onHelloButtonClick() {
        try {
            // Ввод имени пользователя
            String user = inputField.getText();

            // Формируем URL для запроса
            URI uri = URI.create(BASE_URL + "/getId/" + user);

            HttpClient client = HttpClient.newHttpClient();

            // запрос GET
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .GET()
                    .build();

            // запрос ответ
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200:
                    Long userId = Long.parseLong(response.body());
                    welcomeText.setText("Received user ID: " + userId);
                    break;
                case 404:
                    welcomeText.setText("User not found.");
                    break;
                default:
                    welcomeText.setText("Request failed with response code: " + response.statusCode());
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            welcomeText.setText("");
        }
    }
}
