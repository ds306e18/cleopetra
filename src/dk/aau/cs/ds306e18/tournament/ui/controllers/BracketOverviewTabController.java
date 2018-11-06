package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Match;
import dk.aau.cs.ds306e18.tournament.model.SwissStage;
import dk.aau.cs.ds306e18.tournament.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BracketOverviewTabController {

    @FXML private Button nextMatchBtn;
    @FXML private GridPane bracketOverviewTab;
    @FXML private VBox selectedMatchVBox;
    @FXML private Button nextStageBtn;
    @FXML private VBox overviewVBox;
    @FXML private Button prevStageBtn;
    @FXML private Button prevMatchBtn;

    private SwissStage swissStage;

    @FXML
    private void initialize(){

        tempCreateSwissBracket();
        loadVisualMatch(swissStage.getAllMatches().get(0));
    }

    private void getMatches(){

        URL matchFxml = getClass().getResource("../layout/MatchVisual.fxml");

        try {
            GridPane root = FXMLLoader.load(matchFxml);
            overviewVBox.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadVisualMatch(Match match) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../layout/MatchVisual.fxml"));
        GridPane root = null;
        MatchVisualController mvc = null;

        try {
            root = loader.load();
            mvc = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mvc.setMatch(match);

        overviewVBox.getChildren().add(root);
    }

    private void tempCreateSwissBracket(){
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

    @FXML
    void updateBracket(ActionEvent event) {
        System.out.println("Clioced");
        overviewVBox.getChildren().clear();
        overviewVBox.getChildren().add(swissStage.getJavaFxNode(null));
        //bracketOverviewTab.requestLayout();
    }
}
