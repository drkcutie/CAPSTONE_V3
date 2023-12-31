package com.example.capstone_2;

import com.example.capstone_2.util.Albums;
import com.example.capstone_2.util.Artist;
import com.example.capstone_2.util.Functions;
import com.example.capstone_2.util.Playlist;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.net.URL;
import java.util.*;
public class PlaylistTabController implements Initializable {
    @FXML
    private MainController main;

    // Necessary to handle refreshes by library controller.
    @FXML
    private LibraryController LibraryController;



    @FXML
    private ListView<String> albumContentList;


    @FXML
    private ListView<String> artistContentList;



    @FXML
    private ListView<String> playlistContentList;



    private File playlistDirectory;
    private String currentSongs;

    File defaultFolderImagePath = new File("src/img/default/folder.png");
    File defaultArtistImagePath = new File("src/img/default/artist.png");
    File defaultAlbumImagePath = new File("src/img/default/Music_album.png");

    private Image defaultPlaylistImage;
    private Image defaultArtistImage;
    private Image defaultAlbumImage;






    public void init(MainController mainController) {
        main = mainController;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        refresh();
    }
    public void refresh()
    {
        playlistContentList.getItems().removeAll(Playlist.getAllPlaylist());
        artistContentList.getItems().removeAll(Artist.getAllArtists());
        albumContentList.getItems().removeAll(Albums.getAllAlbums());

        String absolutePathPlaylist = defaultFolderImagePath.getAbsolutePath();
        absolutePathPlaylist = absolutePathPlaylist.replace("\\", "/");
        String absolutePathArtist = defaultArtistImagePath.getAbsolutePath();
        absolutePathArtist = absolutePathArtist.replace("\\", "/");
        String absolutePathAlbum = defaultAlbumImagePath.getAbsolutePath();
        absolutePathAlbum = absolutePathAlbum.replace("\\", "/");


        LibraryController.setPlaylistController(this);


        try {

            defaultPlaylistImage = new Image(new File(absolutePathPlaylist).toURI().toString());
            defaultArtistImage = new Image(new File(absolutePathArtist).toURI().toString());
            defaultAlbumImage = new Image(new File(absolutePathAlbum).toURI().toString());
        } catch (Exception e) {
            System.err.println("Encountered a problem with the default images.");
        }




        try {
            playlistDirectory = new File("src/Music");

        } catch (Exception e) {
            System.out.println("File not found!!!!!!!!!!!!!!!!!!!");
        }

        File[] files = playlistDirectory.listFiles();


        Set<String> artists = new HashSet<>();
        Set<String> albums = new HashSet<>();
        Set<String> playlists = new HashSet<>();


        if(files != null){
            for (File folder : files) {
                if (folder.isDirectory()) {
                    String playlist = folder.getName();
                    File[]  playListFiles =  folder.listFiles();
                    assert playListFiles != null;
                    for (File file : playListFiles) {
                        if (Functions.checkFile(file)) {
                            String filepath = file.getPath();

                            playlists.add(playlist);

                            if (!Playlist.playlistMap.containsKey(playlist)) {
                                Playlist.playlistMap.put(playlist, new ArrayList<>());
                            }
                            Playlist.playlistMap.get(playlist).add(filepath);
                        }

                    }
                }else{
                    System.out.println("Not a folder");
                }
            }

            for (File playlist : files) {
                File[] playListFiles = playlist.listFiles(); // playlist folders

                if(playListFiles != null){
                    for(File file : playListFiles){
                        if (Functions.checkFile(file)) {
                            Map<String, String> map = Functions.extractMetadata(file.getPath()); // check audio file foreach playlist
                           String artist = map.get("Artist");
                           String album = map.get("Album");
                           String filepath = file.getPath();


                           artists.add(artist);
                           albums.add(album);

                            if (!Artist.artistMap.containsKey(artist)) {
                                Artist.artistMap.put(artist, new ArrayList<>());
                            }
                            Artist.artistMap.get(artist).add(filepath);

                            if (!Albums.albumMap.containsKey(album)) {
                                Albums.albumMap.put(album, new ArrayList<>());
                            }
                            Albums.albumMap.get(album).add(filepath);

                        }



                    }
                }// end of metadata

            }
        }








        playlistContentList.getItems().addAll(Playlist.getAllPlaylist());
        playlistContentList.setCellFactory(param -> new ListCell<String>() {
            final ImageView img = new ImageView();

            @Override
            protected void updateItem(String name, boolean empty) {

                super.updateItem(name, empty);
                if (empty) {
                    img.setImage(null);
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(name);
                    img.setFitWidth(50);
                    img.setFitHeight(50);
                    img.setPreserveRatio(true);
                    img.setImage(defaultPlaylistImage);
                    setGraphic(img);
                }
            }
        });


        artistContentList.getItems().addAll(Artist.getAllArtists());
        artistContentList.setCellFactory(param -> new ListCell<String>() {
            final ImageView img = new ImageView();

            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);

                if (empty) {
                    img.setImage(null);
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(name);

                    if (Artist.artistMap.containsKey(name) && !Artist.artistMap.get(name).isEmpty()) {
                        String firstFilePath = Artist.artistMap.get(name).get(0);
                        Image image = Functions.extractAndDisplayAlbumCover(firstFilePath);

                        if (image != null) {
                            img.setFitWidth(50);
                            img.setFitHeight(50);
                            img.setPreserveRatio(true);
                            img.setImage(image);
                            setGraphic(img);
                        }
                    } else {
                        img.setFitWidth(50);
                        img.setFitHeight(50);
                        img.setPreserveRatio(true);
                        img.setImage(defaultArtistImage);
                        setGraphic(img);
                    }
                }
            }
        });

        albumContentList.getItems().addAll(Albums.getAllAlbums());
        albumContentList.setCellFactory(param -> new ListCell<String>() {
            final ImageView img = new ImageView();


            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);

                if (empty) {
                    img.setImage(null);
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(name);
                    if (Albums.albumMap.containsKey(name) && !Albums.albumMap.get(name).isEmpty()) {
                        String firstFilePath = Albums.albumMap.get(name).get(0);
                        Image image = Functions.extractAndDisplayAlbumCover(firstFilePath);

                        if (image != null) {
                            img.setFitWidth(40);
                            img.setFitHeight(40);
                            img.setPreserveRatio(true);
                            img.setImage(image);
                            setGraphic(img);
                        }
                    } else {
                        img.setFitWidth(40);
                        img.setFitHeight(40);
                        img.setPreserveRatio(true);
                        img.setImage(defaultAlbumImage);
                        setGraphic(img);
                    }
                }
            }
        });


        playlistContentList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                currentSongs = playlistContentList.getSelectionModel().getSelectedItem();
                main.SelectionController.setFiles(currentSongs, SelectionController.MediaType.PLAYLIST);

            }
        });

        artistContentList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                currentSongs = artistContentList.getSelectionModel().getSelectedItem();
                main.SelectionController.setFiles(currentSongs, SelectionController.MediaType.ARTIST);

            }
        });

        albumContentList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                currentSongs = albumContentList.getSelectionModel().getSelectedItem();
                System.out.println(currentSongs);
                main.SelectionController.setFiles(currentSongs, SelectionController.MediaType.ALBUM);

            }
        });



    }


}
