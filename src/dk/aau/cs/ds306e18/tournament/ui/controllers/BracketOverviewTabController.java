package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.SwissStage;
import dk.aau.cs.ds306e18.tournament.model.Team;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class BracketOverviewTabController {

    @FXML private GridPane bracketOverviewTab;
    @FXML private VBox overviewVBox;
    @FXML private VBox selectedMatchVBox;
    @FXML private Button nextMatchBtn;
    @FXML private Button nextStageBtn;
    @FXML private Button prevStageBtn;
    @FXML private Button prevMatchBtn;
    @FXML private Label selectedMatchLabel;

    private SwissStage swissStage;

    @FXML
    private void initialize(){

        createSwiss();
    }

    private void createSwiss(){
        swissStage = new SwissStage();
        ArrayList<Team> teams = new ArrayList<Team>();
        ArrayList<Bot> team1 = new ArrayList<>();
        team1.add(new Bot("t1b1", "mk", null));
        team1.add(new Bot("t1b2", "mk", null));
        teams.add(new Team("Team 1", team1, 1, "hello"));

        ArrayList<Bot> team2 = new ArrayList<>();
        team1.add(new Bot("t2b1", "mk", null));
        team1.add(new Bot("t2b2", "mk", null));
        teams.add(new Team("Team 2", team2, 2, "hello"));

        ArrayList<Bot> team3 = new ArrayList<>();
        team1.add(new Bot("t3b1", "mk", null));
        team1.add(new Bot("t3b2", "mk", null));
        teams.add(new Team("Team 3", team3, 3, "hello"));

        ArrayList<Bot> team4 = new ArrayList<>();
        team1.add(new Bot("t4b1", "mk", null));
        team1.add(new Bot("t4b2", "mk", null));
        teams.add(new Team("Team 4", team4, 4, "hello"));

        swissStage.start(teams);
    }

    @FXML void setSelectedMatch() {
        this.selectedMatchLabel.setText("Clicked");

    }



    @FXML void changeSelectedMatch(){

    }
}
