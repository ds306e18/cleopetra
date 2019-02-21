package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import dk.aau.cs.ds306e18.tournament.utility.SaveLoad;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainController {

    @FXML public TournamentSettingsTabController tournamentSettingsTabController;
    @FXML public ParticipantSettingsTabController participantSettingsTabController;
    @FXML public RLBotSettingsTabController rlBotSettingsTabController;
    @FXML public BracketOverviewTabController bracketOverviewTabController;
    @FXML public ImageView saveTournamentBtn;
    @FXML public Tab tournamentSettingsTab;
    @FXML public Tab participantSettingsTab;
    @FXML public Tab rlbotSettingsTab;
    @FXML public Tab bracketOverviewTab;

    @FXML
    private void initialize() {
        System.out.println("Initialized ui");
    }

    public void onTabSelectionChanged(Event event) {
        // TODO Make references to other controllers work so we can avoid using singleton instances. Might require newer version of java
        if (tournamentSettingsTab.isSelected()) {

        } else if (participantSettingsTab.isSelected()) {

        } else if (rlbotSettingsTab.isSelected()) {

        } else if (bracketOverviewTab.isSelected()) {
            BracketOverviewTabController.instance.update();
        }
    }

    @FXML
    void onSaveIconClicked(MouseEvent event) {

        Stage stage = (Stage) saveTournamentBtn.getScene().getWindow();

        if (SaveLoad.saveTournament(stage)){
            Alerts.infoNotification("Saved", "Tournament was successfully saved.");
        } else {
            Alerts.errorNotification("Error while saving", "Something went wrong while saving the tournament.");
        }
    }
}
