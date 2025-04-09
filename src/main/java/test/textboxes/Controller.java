package test.textboxes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField inputField;

    @FXML
    protected void onHelloButtonClick() {
        try {
            int number = Integer.parseInt(inputField.getText());
            int result = integer.square(number);
            welcomeText.setText("number; " + result);
        } catch (NumberFormatException e) {
            welcomeText.setText("Пожалуйста, введите корректное число");
        }
    }
}