package com.example.capstone_2;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class mainController {
    public BorderPane Main;
    @FXML
private PlaylistTabController PlaylistController;
@FXML
SelectionController SelectionController;
@FXML
FooterController FooterController;



@FXML
    private void initialize()
{


    PlaylistController.init(this);
    SelectionController.init(this);
    FooterController.init(this);
}
}
