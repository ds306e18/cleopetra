package dk.aau.cs.ds306e18.tournament.ui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;

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
}
