package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.Stage;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TournamentSettingsTabController {

    @FXML
    private void initialize(){

    }

    @FXML
    private GridPane tournamentSettingsTab;

    @FXML
    private TextField nameTextField;

    @FXML
    private ChoiceBox<TieBreaker> tieBreakerBtn;

    @FXML
    private ListView<Stage> stagesListView;

    @FXML
    private Button addStageBtn;

    @FXML
    private Button deleteStageBtn;

    @FXML
    private VBox stageSettingsVBox;
}
