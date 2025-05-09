module test.textboxes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    
    opens test.textboxes to javafx.fxml;
    exports test.textboxes;
}
