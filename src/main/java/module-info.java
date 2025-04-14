module test.textboxes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    
    opens test.textboxes to javafx.fxml;
    exports test.textboxes;
}
