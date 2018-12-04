package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import dk.aau.cs.ds306e18.tournament.utility.SaveLoad;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
        System.out.println("Initialized ui");
    }

    public void onTabSelectionChanged(Event event) {
        if (bracketOverviewTab.isSelected()) {

            BracketOverviewTabController.instance.update();
        }
    }

    @FXML
    void onSaveIconClicked(MouseEvent event) {

        Stage stage = (Stage) participantSettingsTabContent.getScene().getWindow();

        if (SaveLoad.saveTournament(stage)){
            Alerts.infoNotification("Saved", "Tournament was successfully saved.");
        } else {
            Alerts.errorNotification("Error while saving", "Something went wrong while saving the tournament.");
        }
    }
}
