package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.*;
import dk.aau.cs.ds306e18.tournament.oldui.Tabs.BracketOverview;
import dk.aau.cs.ds306e18.tournament.oldui.bracketObjects.VisualMatch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BracketOverviewTabController {

    @FXML private GridPane bracketOverviewTab;
    @FXML private VBox selectedMatchVBox;
    @FXML private VBox overviewVBox;
    @FXML private Button nextMatchBtn;
    @FXML private Button nextStageBtn;
    @FXML private Button prevStageBtn;
    @FXML private Button prevMatchBtn;

    private SwissStage swissStage;
    private MatchVisualController selectedMatch;

    @FXML
    private void initialize(){
        selectedMatch = null;
        initializeSwissBracket();
        updateView(swissStage);
        //ArrayList<VBox> matches = getAllVisualMatches(swissStage);
        //drawAllMatches(matches);
    }

    private void updateView(Format format){

        overviewVBox.getChildren().clear();
        overviewVBox.getChildren().add(format.getJavaFxNode(this));
    }

    /** Adds the given matches to the content of the overviewVBox. */
    private void drawAllMatches(ArrayList<VBox> matches){
        overviewVBox.getChildren().addAll(matches);
    }

    /** @param format the format to generate matches from.
     * @return an arrayList containing gridpanes, where each is a visual representation of a match in the given format. */
    private ArrayList<VBox> getAllVisualMatches(Format format){
        ArrayList<Match> allMatches = new ArrayList<>(format.getAllMatches());

        ArrayList<VBox> visualMatches = new ArrayList<>();

        for(Match match : allMatches){
            visualMatches.add(loadVisualMatch(match));
        }

        return visualMatches;
    }

    /** @param match the match to be visualised
     * @return a gridPane containing the visualisation of the given match. */
    public VBox loadVisualMatch(Match match) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../layout/MatchVisual.fxml"));
        VBox root = null;
        MatchVisualController mvc = null;

        try {
            root = loader.load();
            mvc = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        match.setScores(2,4,true);
        mvc.setMatch(match);
        mvc.setBoc(this);
        //root.setId("matchplayed"); //TODO should be done based on the status of the match
        //root.setId("TBD");
        //root.setId("upcomming");
        //TODO add winner style functionality

        return root;
    }

    /** a temperate method that generates a swiss bracket. */
    private void initializeSwissBracket(){
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

        for (Match match : swissStage.getUpcomingMatches())
            match.setScores(2, 4, true);
        swissStage.createNewRound();
    }

    /** Meant to update the created matches on button click -> this method. */
    @FXML
    void updateBracket(ActionEvent event) {
        System.out.println("Clioced");
        overviewVBox.getChildren().clear();
        overviewVBox.getChildren().add(swissStage.getJavaFxNode(null));
        //bracketOverviewTab.requestLayout();
    }

    public void setSelectedMatch(MatchVisualController match){
        this.selectedMatch = match;
        System.out.println(match.toString());
    }
}
