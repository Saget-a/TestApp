package test.textboxes;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {

        //подключение файла разметки
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("application-view.fxml"));
        //ограничение сцены
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        //подключение css
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        //назввание окна
        stage.setTitle("TEST APP");
        //применение настроек сцены
        stage.setScene(scene);
        //отключение изменения розмера окна
        stage.setResizable(false);
        //запуск сцены
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}