package com.example.capstone_2;


import com.example.capstone_2.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.io.File;

import java.util.ArrayList;
import java.util.Objects;



public class SelectionController {
    private MainController main;
    private static final ArrayList<File> songs = new ArrayList<>();



    @FXML
    private TextFlow playlistName;

    @FXML
    private ScrollPane scrollPane = new ScrollPane();


    @FXML
    private Pane playPicture;


    @FXML
    private  TableColumn<Cells, Integer> Number;

    @FXML
    private  TableColumn<Cells, String> album;

    @FXML
    private  TableView<Cells> tableMusic;

    @FXML
    private  TableColumn<Cells, String> timeDuration;

    @FXML
    private  TableColumn<Cells, String> title;
    @FXML
    private TableColumn<Cells,Image> SongImg;
    @FXML
    private ImageView playlistImage;

    @FXML
    //Used to justify items.. acts like a div.
    private HBox backgroundImage;

    public String key;

    public void init(MainController mainController) {
        main = mainController;
    }

    public enum MediaType {
        PLAYLIST,
        ALBUM
        ,ARTIST
    }
    static ObservableList<Cells> data = FXCollections.observableArrayList();


    @FXML
    void initialize() {
        assert Number != null : "fx:id=\"Number\" was not injected: check your FXML file 'Selection.fxml'.";
        assert album != null : "fx:id=\"album\" was     not injected: check your FXML file 'Selection.fxml'.";
        assert playPicture != null : "fx:id=\"playPicture\" was not injected: check your FXML file 'Selection.fxml'.";
        assert tableMusic != null : "fx:id=\"tableMusic\" was not injected: check your FXML file 'Selection.fxml'.";
        assert timeDuration != null : "fx:id=\"timeDuration\" was not injected: check your FXML file 'Selection.fxml'.";
        assert title != null : "fx:id=\"title\" was not injected: check your FXML file 'Selection.fxml'.";
        assert playlistName != null : "fx:id=\"playlistName\" was not injected: check your FXML file 'Selection.fxml'.";

        setCells();


        tableMusic.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Cells selectedCells = tableMusic.getSelectionModel().getSelectedItem();
                if (selectedCells != null) {
                    int index = handleDoubleClick(selectedCells);
                    main.FooterController.setSongfromPlaylist(index, songs);

                }
            }



        });

    }

    public void setCells()
    {
        Number.setCellValueFactory(new PropertyValueFactory<>("number"));  // Use "number" instead of "Number"
        setupHandCursorForColumn(Number, true);
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        setupHandCursorForColumn(title, false);
        album.setCellValueFactory(new PropertyValueFactory<>("album"));
        setupHandCursorForColumn(album, false);
        timeDuration.setCellValueFactory(new PropertyValueFactory<>("timeDuration"));
        setupHandCursorForColumn(timeDuration, true);
        SongImg.setCellValueFactory(new PropertyValueFactory<>("Image"));
        tableMusic.setItems(data);

    }
    private int handleDoubleClick(Cells selectedCells) {
        // Obtain the file path from the selected example

        // Do something with the file path on double-click, for example, print it
        System.out.println("Double-clicked on row with file path: " + selectedCells.getNumber());
        // Add your logic to handle the file path as needed on double-click

        // Return the index
        return selectedCells.getNumber()-1;
    }


    public void updatePlaylistBar(String name, String path) {
        Text text = new Text(name);
        double initialFontSize = 70;
        Font initialFont = new Font("Arial Black", initialFontSize);
        text.setFont(initialFont);

        double textWidth = text.getBoundsInLocal().getWidth();
        System.out.println("Original Font Size: " + initialFont.getSize());
        System.out.println("Text Width: " + textWidth);

        if (textWidth > playlistName.getPrefWidth()) {
            double scaleFactor = playlistName.getPrefWidth() / textWidth;
            double newFontSize = initialFont.getSize() * scaleFactor;

            System.out.println("New Font Size: " + newFontSize);

            double minimumFontSize = 45;
            if (newFontSize <= initialFontSize && newFontSize >= minimumFontSize) {
                text.setFont(new Font(initialFont.getFamily(), newFontSize));
                System.out.println("Setting new font size...");
            }
        }

        playlistName.getChildren().setAll(text);

        Image img = Functions.extractAndDisplayAlbumCover(path);
        playlistImage.setImage(img);

        // Check if the text is too long and enable scrolling
        if (textWidth > playlistName.getPrefWidth()) {
            scrollPane.setContent(playlistName);
        } else {
            scrollPane.setContent(null); // Disable scrolling if not needed
        }

        backgroundImage.setStyle("-fx-background-color: " + StylesHandler.getLinearGradient());
    }

    public void setFiles(String key, MediaType type) {
        if(Objects.equals(this.key, key))
            return;
        songs.clear();
        data.clear();

        this.key = key;
        ArrayList<String> temp = switch (type) {
            case PLAYLIST -> Playlist.getSongsfromPlaylist(key);
            case ARTIST -> Artist.getSongsfromArtist(key);
            case ALBUM -> Albums.getSongsfromAlbum(key);
        };
        for(String path : temp)
        {
            File file = new File(path);
            System.out.println("File Path = " +path + "\n" );
            songs.add(file);
        }

        updatePlaylistBar(key, songs.get(0).getPath());

        setPlaylist();
        setCells();

    }
    private <T> void setupHandCursorForColumn(TableColumn<Cells, T> column, boolean centerContent) {
        column.setCellFactory(col -> new TableCell<Cells, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setCursor(Cursor.DEFAULT);
                } else {
                    setText(item.toString());
                    setCursor(Cursor.HAND); // Set the cursor to hand when the cell is not empty
                    if (centerContent) {
                        setAlignment(Pos.CENTER); // Center the content
                    }
                }
            }

            {
                // Handle mouse events directly in the cell factory
                setOnMouseEntered(event -> {
                    // Change the cursor to hand when the mouse enters the cell
                    if (getItem() != null) {
                        getScene().setCursor(Cursor.HAND);
                    }
                });

                setOnMouseExited(event -> {
                    // Change the cursor back to the default when the mouse exits the cell
                    if (getItem() != null) {
                        getScene().setCursor(Cursor.DEFAULT);
                    }
                });
            }
        });
    }


    public static void setPlaylist()
    {

        for (int i = 0; i < songs.size(); i++) {
            final int index = i + 1;  // Create a final variable to capture the correct value of i
            File song = songs.get(i);

            // Create a Media object for each file
            Media songMedia = new Media(song.toURI().toString());
            String album = Functions.extractMetadata(song.getPath()).get("Album");

            // Create a MediaPlayer for the Media object
            MediaPlayer songPlayer = new MediaPlayer(songMedia);

            // Set the onReady event
            songPlayer.setOnReady(() -> {
                // Get the duration of the media
                Duration duration = songMedia.getDuration();

                // Convert duration to minutes and seconds
                long totalSeconds = (long) duration.toSeconds();
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;
                String formattedDuration = String.format("%02d:%02d", minutes, seconds);
                Image img = Functions.extractAndDisplayAlbumCover(song.getPath());
                // Create example object and add it to the data list
                Cells songCells = new Cells(index, Functions.nameWithoutExtension(song.getName()), album, formattedDuration,img);
                data.add(songCells);

                // Dispose of the MediaPlayer after obtaining the duration
                songPlayer.dispose();
            });

        }
    }



}