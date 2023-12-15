package com.example.capstone_2;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

public class ModalSelectionController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    private File playlistDirectory;
    private Set<String> artists;
    private  File[] files;

    @FXML
    private ListView<String> DisplaySongs;

    File defaultArtistImagePath = new File("src/img/default/artist.png");
    private Image defaultArtistImage;
    @FXML
    void initialize() {
        assert DisplaySongs != null : "fx:id=\"DisplaySongs\" was not injected: check your FXML file 'ModalSongSelection.fxml'.";


        String absolutePathArtist = defaultArtistImagePath.getAbsolutePath();
        absolutePathArtist = absolutePathArtist.replace("\\", "/");


        try {

            defaultArtistImage = new Image(new File(absolutePathArtist).toURI().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            playlistDirectory = new File("src/Music");

        } catch (Exception e) {
            System.out.println("File not found!!!!!!!!!!!!!!!!!!!");
        }

        files = playlistDirectory.listFiles();
        artists = new HashSet<>();
    }

}
