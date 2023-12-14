package com.example.capstone_2;

import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class LibraryController {

    @FXML
    private ImageView addPlaylist;
    private PlaylistTabController playlistController;

    @FXML
    void addPlaylist(MouseEvent event) {

        TextInputDialog dialog = new TextInputDialog("Playlist");
        dialog.setTitle("New Playlist");
        dialog.setHeaderText("Enter the name for the new playlist:");
        dialog.setContentText("Playlist Name:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String playlistName = result.get();


            String fixedDirectory = "src/Music";
            File musicDirectory = new File(fixedDirectory);

            if (!musicDirectory.exists()) {
                if (musicDirectory.mkdirs()) {
                    System.out.println("Directory created: " + musicDirectory.getAbsolutePath());
                } else {
                    System.err.println("Failed to create directory: " + musicDirectory.getAbsolutePath());
                    return;
                }
            }

            File newPlaylistFolder = new File(musicDirectory, playlistName);

            if (newPlaylistFolder.mkdir()) {
                System.out.println("New playlist created: " + newPlaylistFolder.getAbsolutePath());

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select audio files to add to the playlist");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.ogg"));

                List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

                if (selectedFiles != null) {
                    for (File file : selectedFiles) {

                        try {
                            Files.copy(file.toPath(), new File(newPlaylistFolder, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("Failed to copy file: " + file.getName());
                        }
                        playlistController.refresh();
                    }

                }
            } else {

                System.err.println("Failed to create the new playlist folder.");
            }
        }
    }
    @FXML
    void initialize()
    {
        addPlaylist.setImage(FooterController.images.get("AddPlaylist"));
    }

   public void setPlaylistController(PlaylistTabController playlistController)
   {
       this.playlistController = playlistController;
   }


}
