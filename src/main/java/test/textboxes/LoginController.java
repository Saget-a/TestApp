package test.textboxes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "1234".equals(password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("application-view.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene scene = new Scene(root, 800, 500);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

                stage.setScene(scene);
                stage.setTitle("TEST APP");
                stage.setResizable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Неверный логин или пароль");
        }
    }
}
