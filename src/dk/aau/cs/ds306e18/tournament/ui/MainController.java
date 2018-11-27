package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.utility.FileOperations;
import dk.aau.cs.ds306e18.tournament.utility.SaveLoad;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML public GridPane tournamentSettingsTabContent;
    @FXML public GridPane participantSettingsTabContent;
    @FXML public GridPane bracketOverviewTabContent;
    @FXML public TournamentSettingsTabController tournamentSettingsTabController;
    @FXML public ParticipantSettingsTabController participantSettingsTabController;
    @FXML public BracketOverviewTabController bracketOverviewTabController;
    @FXML public Tab bracketOverviewTab;

    @FXML
    private void initialize() {
        System.out.println("Initialized UI");
    }

    public void onTabSelectionChanged(Event event) {
        if (bracketOverviewTab.isSelected()) {

            BracketOverviewTabController.instance.update();
        }
    }


    @FXML
    void onSaveIconClicked(MouseEvent event) {
        // TODO: Add a unobtrusive message when the user has saved the file.

        Stage stage = (Stage) participantSettingsTabContent.getScene().getWindow();

        if (SaveLoad.saveTournament(stage)){
            System.out.println("Successfully saved the file.");
        } else {
            System.out.println("Something went wrong while saving the file.");
        }
    }
}
