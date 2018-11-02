package dk.aau.cs.ds306e18.tournament.ui.controllers;

import javafx.fxml.FXML;

public class MainController {

    @FXML public TournamentSettingsTabController tournamentSettingsTabController;
    @FXML public ParticipantSettingsTabController participantSettingsTabController;
    @FXML public BracketOverviewTabController bracketOverviewTabController;

    @FXML
    private void initialize() {
        System.out.println("Initialized UI");
    }
}
