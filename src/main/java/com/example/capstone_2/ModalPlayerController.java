package com.example.capstone_2;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.example.capstone_2.util.Functions;
import com.example.capstone_2.util.StylesHandler;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.util.Duration;

//TODO FIX LAGGY AND DUPLICATING AUDIO MIGHT BE CREATING NEW INSTANCES BUT DONT KNOW WHERE

public class ModalPlayerController {
    private MainController main;

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
    private ImageView songImage;

    @FXML
    private Button PlayButton;

    @FXML
    private Label Title;

    @FXML
    private Button nxtButton;

    @FXML
    private Button prevButton;

    @FXML
    public Slider progressSlider;

    @FXML
    private ImageView repeatButton;

    @FXML
    private ImageView shuffleButton;
    FooterController footerController;

    private Timer timer;
    private  TimerTask task;
    private Parent root;
    private  Timeline timeline;
    private Map<String, Image> images = new HashMap<String, Image>();
    private File songs_directory, icon_directory;
    int repeatState  = 0; //1 for no repeat //2 for repeat whole playlist // 3 for repeat song;

    boolean running = false , shuffle = false;

    public void init(MainController mainController)
    {
        main = mainController;
        footerController = main.FooterController;
        modal.setStyle("-fx-background-color: " + StylesHandler.getLinearGradient());
        setData();

    }

    public void setData()
    {

        shuffle = footerController.shuffle;

        Platform.runLater(() -> {
            progressSlider.setValue(footerController.progressSlider.getValue());
            Artist.setText(footerController.artistLabel.getText());
            Title.setText(footerController.songLabel.getText());
            songImage.setImage(footerController.songImage.getImage());
        });
        if(footerController.shuffle)
        {
            shuffleButton.setImage(images.get("shuffle-toggled"));
        }
        else
        {
            shuffleButton.setImage(images.get("shuffle-untoggled"));
        }
        repeatState = footerController.repeatState;
        if(footerController.repeatState == 0)
        {
            repeatButton.setImage(images.get("repeat-untoggled"));
        }
        else if (footerController.repeatState == 1)
        {
            repeatButton.setImage(images.get("repeat-toggled1"));
        }
        else
        {
            repeatButton.setImage(images.get("repeat-toggled2"));
        }
        playProgress(new ActionEvent());

    }
    @FXML
    void initialize() {
        assert Artist != null : "fx:id=\"Artist\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert CloseButton != null : "fx:id=\"CloseButton\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert songImage != null : "fx:id=\"MusicPic\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert PlayButton != null : "fx:id=\"PlayButton\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert Title != null : "fx:id=\"Title\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert modal != null : "fx:id=\"modal\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert nxtButton != null : "fx:id=\"nxtButton\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert prevButton != null : "fx:id=\"prevButton\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert progressSlider != null : "fx:id=\"progressSlider\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert repeatButton != null : "fx:id=\"repeatButton\" was not injected: check your FXML file 'ModalPlayer.fxml'.";
        assert shuffleButton != null : "fx:id=\"shuffleButton\" was not injected: check your FXML file 'ModalPlayer.fxml'.";

        progressSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (progressSlider.isValueChanging() && footerController.mediaPlayer != null) {
                    double end = footerController.mediaPlayer.getTotalDuration().toSeconds();
                    double newPosition = progressSlider.getValue() * 0.01 * end;
                    footerController.mediaPlayer.seek(Duration.seconds(newPosition));
                    footerController.progressSlider.setValue(newPosition/ end * 100);
                }
            }
        });

        progressSlider.setOnMouseClicked(event -> {
            if (footerController.mediaPlayer != null) {
                double end = footerController.mediaPlayer.getTotalDuration().toSeconds();
                double newPosition = progressSlider.getValue() * 0.01 * end;
                footerController.mediaPlayer.seek(Duration.seconds(newPosition));

                footerController.progressSlider.setValue(newPosition / end * 100);

            }

        });

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
        songImage.setImage(images.get("wuht"));
    }
    void playProgress(ActionEvent actionEvent) {
        if (footerController.mediaPlayer != null) {
            synchronized (footerController.mediaPlayer) {
                if (timer != null) {
                    timer.cancel(); // Cancel the previous task
                }

                timer = new Timer();
                task = new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            // Duration and Label
                            Duration duration = footerController.mediaPlayer.getMedia().getDuration();

                            double current = footerController.mediaPlayer.getCurrentTime().toSeconds();
                            double end = footerController.mediaPlayer.getTotalDuration().toSeconds();
                            double progress = current / end * 100;
                            double targetProgress = progressSlider.getValue() + 0.1;

                            // UPDATES THE UI IF THERE'S A LATENCY ON LOADING THE DATA
                            if (footerController.songImage != songImage) {
                                setData();
                            }

                            if (targetProgress <= progress) {
                                progressSlider.setValue(targetProgress);
                            }
                            if (current / end == 1.0) {
                                if (repeatState == 2) {
                                    footerController.setCurrentSong();
                                } else {
                                    forwardMusic();
                                }
                            }
                        });
                    }
                };
                timer.scheduleAtFixedRate(task, 1000, 1000);
            }
        }
    }


    @FXML
    void toggleRepeat(MouseEvent event) {
        main.FooterController.toggleRepeat(event);
        if(repeatState == 0)
        {
            repeatState = 1;
            repeatButton.setImage(images.get("repeat-toggled1"));
        }
        else if (repeatState == 1)
        {
            repeatState = 2;
            repeatButton.setImage(images.get("repeat-toggled2"));
        }
        else
        {
            repeatState = 0;
            repeatButton.setImage(images.get("repeat-untoggled"));
        }

    }

    @FXML
    void toggleShuffle(MouseEvent event) {
        main.FooterController.toggleShuffle(event);
        shuffle = !shuffle;
        if(shuffle)
        {
            shuffleButton.setImage(images.get("shuffle-toggled"));
        }
        else
        {
            shuffleButton.setImage(images.get("shuffle-untoggled"));
        }
    }





    void forwardMusic()
    {

        try {
            footerController.forwardMusic(new ActionEvent());
        } catch (IOException e) {
            System.err.println("Cannot Forward Music");
        }
        Platform.runLater(new Runnable() {
            @Override public void run() {
                setData();
            }
        });
        System.out.println("Setting data");
    }
    @FXML
    void forwardMusic(MouseEvent event) throws IOException {

        try {
            footerController.forwardMusic(new ActionEvent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(new Runnable() {
            @Override public void run() {
                setData();
            }
        });


        System.out.println("Setting data");


    }

    @FXML
    void playMusic(MouseEvent event) {
        footerController.playMusic(new ActionEvent());
    }

    @FXML
    void prevMusic(MouseEvent event) throws IOException {
        footerController.prevMusic(new ActionEvent());
        setData();
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