package test.textboxes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Controller_App {

    private static final String PRODUCT_URL = "http://localhost:8081/api/products";
    private static final String SUBSCRIPTION_URL = "http://localhost:8083/subscriptions";
    private String username;

    @FXML private Label welcomeText;
    @FXML private AnchorPane mainPane;
    @FXML private TextField productNameField;
    @FXML private TextField priceField;
    @FXML private TextField amountField;
    @FXML private TextField descriptionField;
    @FXML private TextField searchField;
    @FXML private VBox addProductPane;
    @FXML private VBox subscriptionPane;
    @FXML private VBox productListContainer;

    @FXML private TextArea subscriptionListArea;
    @FXML private TextField subProductField;
    @FXML private TextField subTypeField;
    @FXML private TextField subAddressField;
    @FXML private TextField subContactField;
    @FXML private Label productErrorLabel;


    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    public void handleAddProduct() {
        try {
            String productName = productNameField.getText();
            int amount = Integer.parseInt(amountField.getText());
            double price = Double.parseDouble(priceField.getText());
            String description = descriptionField.getText();

            Product product = new Product(productName, amount, price, description);
            String requestBody = objectMapper.writeValueAsString(product);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PRODUCT_URL + "/create/" + username))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                handleLoadCatalog();
                productNameField.clear();
                amountField.clear();
                priceField.clear();
                descriptionField.clear();
                addProductPane.setVisible(false);
            } else {
                showAlert("Помилка при додаванні товару: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Виникла помилка при додаванні товару.");
        }
    }

    @FXML
    public void handleLoadCatalog() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PRODUCT_URL + "/catalog/" + username))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Product> products = objectMapper.readValue(response.body(), new TypeReference<>() {});
                String query = searchField.getText().trim().toLowerCase();

                productListContainer.getChildren().clear();

                for (Product p : products) {
                    if (query.isEmpty() || p.productName.toLowerCase().contains(query)) {
                        HBox row = new HBox(10);
                        Label info = new Label(p.productName + " - " + p.amount + " кг - " + p.price + " грн/кг");
                        Button subscribeBtn = new Button("Підписатись");
                        subscribeBtn.setOnAction(e -> openSubscriptionDialog(p));
                        row.getChildren().addAll(info, subscribeBtn);
                        productListContainer.getChildren().add(row);
                    }
                }
            } else {
                showAlert("Не вдалося завантажити каталог: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Помилка при завантаженні каталогу.");
        }
    }

    private void openSubscriptionDialog(Product product) {
        subProductField.setText(product.productName);
        subscriptionPane.setVisible(true);
    }

    @FXML private TextField subFrequencyField; // нове поле

    @FXML
    public void handleCreateSubscription() {
        try {
            String product = subProductField.getText();
            String type = subTypeField.getText();
            String address = subAddressField.getText();
            String contact = subContactField.getText();
            String frequency = subFrequencyField.getText();

            // Очистити попередню помилку
            productErrorLabel.setVisible(false);
            subProductField.setStyle(""); // скинути стиль

            String body = objectMapper.writeValueAsString(
                    new SubscriptionRequest(username, type, address, contact, product, frequency)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8083/subscriptions/create"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                subProductField.clear();
                subTypeField.clear();
                subAddressField.clear();
                subContactField.clear();
                subFrequencyField.clear();
            } else if (response.statusCode() == 400 && response.body().contains("Продукт не існує")) {
                subProductField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                productErrorLabel.setText("Цей товар не існує");
                productErrorLabel.setVisible(true);
            } else {
                showAlert("Помилка при створенні підписки: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Сталася помилка під час створення підписки.");
        }
    }




    @FXML
    public void handleLoadSubscriptions() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SUBSCRIPTION_URL + "/user/" + username))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Subscription> subscriptions = objectMapper.readValue(response.body(), new TypeReference<>() {});
                StringBuilder builder = new StringBuilder();
                for (Subscription sub : subscriptions) {
                    builder.append(sub.productName).append(" - ").append(sub.deliveryType)
                            .append(" - ").append(sub.active ? "Активна" : "Неактивна")
                            .append("\n").append(sub.address).append(" / ").append(sub.contactInfo).append("\n\n");
                }
                subscriptionListArea.setText(builder.toString());
            } else {
                subscriptionListArea.setText("Не вдалося завантажити підписки.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            subscriptionListArea.setText("Помилка при завантаженні підписок.");
        }
    }

    @FXML
    public void openSubscriptionsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("subscriptions-view.fxml"));
            Parent root = loader.load();

            Controller_Subscriptions controller = loader.getController();
            controller.setUsername(username);

            Stage stage = new Stage();
            stage.setTitle("Ваші підписки");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginRegister-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("login_style.css").toExternalForm());
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showAddProductPane() {
        addProductPane.setVisible(true);
    }

    @FXML
    public void hideAddProductPane() {
        addProductPane.setVisible(false);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }

    public static class Product {
        public Long id;
        public int sellerID;
        public String productName;
        public int amount;
        public double price;
        public String productDescription;

        public Product() {}

        public Product(String productName, int amount, double price, String productDescription) {
            this.productName = productName;
            this.amount = amount;
            this.price = price;
            this.productDescription = productDescription;
        }
    }

    public static class SubscriptionRequest {
        public String username;
        public String deliveryType;
        public String address;
        public String contactInfo;
        public String productName;
        public String frequency; // додано

        public SubscriptionRequest(String username, String deliveryType, String address, String contactInfo, String productName, String frequency) {
            this.username = username;
            this.deliveryType = deliveryType;
            this.address = address;
            this.contactInfo = contactInfo;
            this.productName = productName;
            this.frequency = frequency;
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
    }
}
