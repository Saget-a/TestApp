module test.textboxes {
    requires javafx.controls;
    requires javafx.fxml;


    opens test.textboxes to javafx.fxml;
    exports test.textboxes;
}