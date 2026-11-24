package com.example.sistemska_knjiznica;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import utils.DatabaseUtils;
import utils.MessageUtils;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        try {
            DatabaseUtils.initSchema();
        } catch (Exception e) {
            LoggerFactory.getLogger(HelloApplication.class).error("Pokretanje baze nije uspjelo", e);
            MessageUtils.showWarning("Baza nije dostupna",
                    "Aplikacija se ne može spojiti na bazu: " + e.getMessage());
            Platform.exit();
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.setTitle("Knjižnica App");
        stage.setScene(scene);
        stage.show();
    }
}
