package com.example.capstone_2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        /*URL css1 = getClass().getResource("footer.css");
        URL css2 = getClass().getResource("library.css");
        URL css3 = getClass().getResource("playlist.css");
        URL css4 = getClass().getResource("selection.css");
        if (css1 != null && css2 !=null) {
            scene.getStylesheets().add(css1.toExternalForm());
            scene.getStylesheets().add(css2.toExternalForm());
        } else {
            System.err.println("CSS resource 'footer.css' not found!");
        }*/
        stage.setMinHeight(600);
        stage.setMinWidth(900);
        stage.setTitle("MUSIC PLAYER ");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }
    public static void main(String[] args) {
        launch();
    }
}
