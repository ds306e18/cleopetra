package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import dk.aau.cs.ds306e18.tournament.utility.SaveLoad;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

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
        Main.LOGGER.log(System.Logger.Level.INFO, "Initializing UI");
    }

    public void onTabSelectionChanged(Event event) {
        // TODO Make references to other controllers work so we can avoid using singleton instances. Might require newer version of java
        if (tournamentSettingsTab.isSelected()) {
            TournamentSettingsTabController.instance.update();
        } else if (participantSettingsTab.isSelected()) {
            ParticipantSettingsTabController.instance.update();
        } else if (rlbotSettingsTab.isSelected()) {
            RLBotSettingsTabController.instance.update();
        } else if (bracketOverviewTab.isSelected()) {
            BracketOverviewTabController.instance.update();
        }
    }

    @FXML
    void onSaveIconClicked(MouseEvent event) {
        Stage fxstage = (Stage) saveTournamentBtn.getScene().getWindow();
        try {
            SaveLoad.saveTournamentWithFileChooser(fxstage);
            Alerts.infoNotification("Saved", "Tournament was successfully saved.");
        } catch (IOException e) {
            Alerts.errorNotification("Error while saving", "Something went wrong while saving the tournament: " + e.getMessage());
            Main.LOGGER.log(System.Logger.Level.ERROR, "Error while saving tournament", e);
            e.printStackTrace();
        }
    }
}
