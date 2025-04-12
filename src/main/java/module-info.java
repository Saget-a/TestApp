module test.textboxes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens test.textboxes to javafx.fxml;
    exports test.textboxes;
}
