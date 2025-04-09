package test.textboxes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {

    //подключениеопамврыгшнмвшщоамык9г8ырга
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
    //подключение css к основе
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("TEST APP");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}