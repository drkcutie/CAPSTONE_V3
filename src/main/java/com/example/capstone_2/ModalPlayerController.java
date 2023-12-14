package com.example.capstone_2;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.almasb.fxgl.audio.Music;
import com.example.capstone_2.util.Functions;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class ModalPlayerController {
    private mainController main;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label Artist;

    @FXML
    private BorderPane modal;

    @FXML
    private Button CloseButton;

    @FXML
    private ImageView MusicPic;

    @FXML
    private Button PlayButton;

    @FXML
    private Label Title;

    @FXML
    private Button nxtButton;

    @FXML
    private Button prevButton;

    @FXML
    private Slider progressSlider;

    @FXML
    private ImageView repeatButton;

    @FXML
    private ImageView shuffleButton;
    FooterController footerControllerController;
    private Parent root;

    private Map<String, Image> images = new HashMap<String, Image>();
    private File songs_directory, icon_directory;
    int repeatState  = 0; //1 for no repeat //2 for repeat whole playlist // 3 for repeat song;

    boolean running = false , shuffle = false;



    @FXML
    void initialize() {
        assert Artist != null : "fx:id=\"Artist\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert CloseButton != null : "fx:id=\"CloseButton\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert MusicPic != null : "fx:id=\"MusicPic\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert PlayButton != null : "fx:id=\"PlayButton\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert Title != null : "fx:id=\"Title\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert modal != null : "fx:id=\"modal\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert nxtButton != null : "fx:id=\"nxtButton\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert prevButton != null : "fx:id=\"prevButton\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert progressSlider != null : "fx:id=\"progressSlider\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert repeatButton != null : "fx:id=\"repeatButton\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert shuffleButton != null : "fx:id=\"shuffleButton\" was not injected: check your FXML file 'ModalPlayer.fxml'.";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Footer.fxml"));

        try {
            root = loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        footerControllerController = loader.getController();


        try {
            icon_directory = new File("src/img/Icons");
        } catch (Exception e) {
            System.out.println("File not found!!!!!!!!!!!!!!!!!!!");
        }
        File[] temp = icon_directory.listFiles();
        for(File file: temp)
        {
            Image image = new Image(file.toURI().toString());
            String name = Functions.nameWithoutExtension(file);
            images.put(name,image);
        }

        repeatButton.setImage(images.get("repeat-untoggled"));
        shuffleButton.setImage(images.get("shuffle-untoggled"));
        MusicPic.setImage(images.get("wuht"));
    }

    @FXML
    void toggleRepeat(MouseEvent event) {
        main.FooterController.toggleRepeat(event);

    }
    @FXML
    void toggleShuffle(MouseEvent event) {
        main.FooterController.toggleShuffle(event);
    }

    @FXML
    void closeScene(MouseEvent event) {
        Scene currentScene = CloseButton.getScene();

        // Get the stage associated with the current scene
        Stage currentStage = (Stage) currentScene.getWindow();

        // Close the stage
        currentStage.close();

    }
}
