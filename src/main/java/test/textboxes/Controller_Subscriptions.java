package test.textboxes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Controller_Subscriptions {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private String username;

    public void setUsername(String username) {
        this.username = username;
        loadSubscriptions();
    }

    @FXML private VBox subscriptionListContainer;

    public void loadSubscriptions() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8083/subscriptions/user/" + username))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Subscription> subscriptions = mapper.readValue(response.body(), new TypeReference<>() {});
                subscriptionListContainer.getChildren().clear();

                for (Subscription sub : subscriptions) {
                    VBox subBox = new VBox(5);
                    subBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 6; -fx-background-radius: 6; -fx-background-color: #f9f9f9;");

                    StringBuilder text = new StringBuilder();
                    text.append("ID: ").append(sub.id).append("\n")
                            .append(sub.productName).append(" - ").append(sub.deliveryType)
                            .append(" (Старт: ").append(sub.startDate).append(") - ")
                            .append(sub.active ? "Активна" : "Неактивна").append("\n")
                            .append("Частота: ").append((sub.frequency == null || sub.frequency.isBlank()) ? "не вказано" : sub.frequency).append("\n")
                            .append("Адреса: ").append(sub.address).append("\n")
                            .append("Контакт: ").append(sub.contactInfo);

                    Label label = new Label(text.toString());
                    label.setWrapText(true);

                    subBox.getChildren().add(label);

                    if (sub.active) {
                        Button cancelButton = new Button("Скасувати підписку");
                        cancelButton.setOnAction(e -> cancelSubscriptionById(sub.id));
                        subBox.getChildren().add(cancelButton);
                    }

                    subscriptionListContainer.getChildren().add(subBox);
                }
            } else {
                subscriptionListContainer.getChildren().clear();
                Label error = new Label("Не вдалося завантажити підписки");
                subscriptionListContainer.getChildren().add(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            subscriptionListContainer.getChildren().clear();
            Label error = new Label("Сталася помилка при завантаженні підписок.");
            subscriptionListContainer.getChildren().add(error);
        }
    }


    public void cancelSubscriptionById(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8083/subscriptions/" + id))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                loadSubscriptions(); // оновити список після видалення
            } else {
                System.out.println("Помилка при видаленні: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static class Subscription {
        public Long id;
        public String username;
        public String productName;
        public String deliveryType;
        public boolean active;
        public String address;
        public String contactInfo;
        public String startDate;
        public String frequency;
    }
}
