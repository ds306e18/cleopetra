package dk.aau.cs.ds306e18.tournament.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class BracketOverviewTabController {

    @FXML
    private void initialize(){

    }

    @FXML
    private GridPane bracketOverviewTab;

    @FXML
    private VBox overviewVBox;

    @FXML
    private VBox selectedMatchVBox;

    @FXML
    private Button nextMatchBtn;

    @FXML
    private Button nextStageBtn;

    @FXML
    private Button prevStageBtn;

    @FXML
    private Button prevMatchBtn;
}
