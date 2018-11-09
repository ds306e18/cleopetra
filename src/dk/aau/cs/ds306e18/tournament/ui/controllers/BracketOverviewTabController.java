package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.*;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.format.SingleEliminationFormat;
import dk.aau.cs.ds306e18.tournament.model.format.SwissFormat;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

public class BracketOverviewTabController {

    @FXML private GridPane bracketOverviewTab;
    @FXML private VBox selectedMatchVBox;
    @FXML private VBox overviewVBox;
    @FXML private Button nextMatchBtn;
    @FXML private Button nextStageBtn;
    @FXML private Button prevStageBtn;
    @FXML private Button prevMatchBtn;
    @FXML private Button playMatchBtn;
    @FXML private Button editMatchBtn;
    @FXML private Label blueTeamNameLabel;
    @FXML private TextField blueTeamScore;
    @FXML private ListView blueTeamListView;
    @FXML private Label orangeTeamNameLabel;
    @FXML private TextField orangeTeamScore;
    @FXML private ListView orangeTeamListView;

    private SwissFormat swissFormat; //TODO temp
    private SingleEliminationFormat singleEli; //TODO temp

    private MatchVisualController selectedMatch;

    @FXML
    private void initialize(){
        selectedMatch = null;
        initializeSwissBracket(); //TODO temp
        initializeSingleEliBracket(); //TODO temp
        updateView(singleEli);
        orangeTeamScore.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(observable);
            checkIntegerScore(oldValue,newValue,orangeTeamScore);
        });
        blueTeamScore.textProperty().addListener((observable, oldValue, newValue) -> {
            checkIntegerScore(oldValue,newValue, blueTeamScore);
        });
    }

    /** Updates the content of this element. Displays the javaFxNode from the given format. */
    private void updateView(Format format){
        overviewVBox.getChildren().clear();
        overviewVBox.getChildren().add(format.getJavaFxNode(this));
    }

    /** a temperate method that generates a single elimination bracket. */
    private void initializeSingleEliBracket(){

        ArrayList<Team> teams = new ArrayList<>();

        ArrayList<Bot> team1 = new ArrayList<>();
        team1.add(new Bot("t1b1", "mk", null));
        team1.add(new Bot("t1b2", "mk", null));
        teams.add(new Team("Team 1", team1, 1, "hello"));

        ArrayList<Bot> team2 = new ArrayList<>();
        team2.add(new Bot("t2b1", "mk", null));
        team2.add(new Bot("t2b2", "mk", null));
        teams.add(new Team("Team 2", team2, 2, "hello"));

        ArrayList<Bot> team3 = new ArrayList<>();
        team3.add(new Bot("t3b1", "mk", null));
        team3.add(new Bot("t3b2", "mk", null));
        teams.add(new Team("Team 3", team3, 3, "hello"));

        ArrayList<Bot> team4 = new ArrayList<>();
        team4.add(new Bot("t4b1", "mk", null));
        team4.add(new Bot("t4b2", "mk", null));
        teams.add(new Team("Team 4", team4, 4, "hello"));

        ArrayList<Bot> team5 = new ArrayList<>();
        team5.add(new Bot("t5b1", "mk", null));
        team5.add(new Bot("t5b2", "mk", null));
        teams.add(new Team("Team 5", team4, 5, "hello"));

        ArrayList<Bot> team6 = new ArrayList<>();
        team6.add(new Bot("t6b1", "mk", null));
        team6.add(new Bot("t6b2", "mk", null));
        teams.add(new Team("Team 6", team4, 6, "hello"));

        ArrayList<Bot> team7 = new ArrayList<>();
        team7.add(new Bot("t7b1", "mk", null));
        team7.add(new Bot("t7b2", "mk", null));
        teams.add(new Team("Team 7", team7, 7, "hello"));

        ArrayList<Bot> team8 = new ArrayList<>();
        team8.add(new Bot("t8b1", "mk", null));
        team8.add(new Bot("t8b2", "mk", null));
        teams.add(new Team("Team 8", team8, 8, "hello"));

        singleEli = new SingleEliminationFormat();
        singleEli.start(teams);

        //Set matches
        ArrayList<Match> matches = new ArrayList<Match>(singleEli.getAllMatches());
        matches.get(5).setScores(2,4,true);
        matches.get(4).setScores(3,1,true);


    }

    /** @param match the match to be visualised
     * @return a gridPane containing the visualisation of the given match. */
    public VBox loadVisualMatch(Match match) {

        //Load the fxml document into the Controller and JavaFx node.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../layout/MatchVisual.fxml"));
        VBox root = null;
        MatchVisualController mvc = null;

        try {
            root = loader.load();
            mvc = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }


        mvc.setBoc(this);
        mvc.setMyNodeObj(root);
        mvc.updateMatch(match);

        return root;
    }

    /** a temperate method that generates a swiss bracket. */
    private void initializeSwissBracket(){
        swissFormat = new SwissFormat();
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

        swissFormat.start(teams);

        for (Match match : swissFormat.getUpcomingMatches())
            match.setScores(2, 4, true);
        swissFormat.createNewRound();
    }

    /** WIP: Meant to update the created matches on button click -> this method. */
    @FXML
    void updateBracket(ActionEvent event) {
        System.out.println("Clioced");
        overviewVBox.getChildren().clear();
        overviewVBox.getChildren().add(swissFormat.getJavaFxNode(null));
    }

    /** Gets called when a match in overviewTab is clicked. */
    public void setSelectedMatch(MatchVisualController match){
        this.selectedMatch = match;
        updateTeamViewer(match.getLastSetMatch());
        System.out.println("New match selected!");
    }

    /** Toggles edit for match scores */
    @FXML void editMatchBtnOnAction(ActionEvent event){
        if (blueTeamScore.editableProperty().getValue()){
            selectedMatch.getLastSetMatch().setBlueScore(Integer.parseInt(blueTeamScore.getText()));
            selectedMatch.getLastSetMatch().setOrangeScore(Integer.parseInt(orangeTeamScore.getText()));
            blueTeamScore.setEditable(false);
            orangeTeamScore.setEditable(false);
            editMatchBtn.setText("Edit match");
        }else{
            blueTeamScore.setEditable(true);
            orangeTeamScore.setEditable(true);
            editMatchBtn.setText("Save edit");
        }
    }

    /** Updates the team viewer on match clicked in overviewTab */
    private void updateTeamViewer(Match match) {
        if (match.getBlueTeam()!=null){
            //updates blue team
            blueTeamNameLabel.setText(match.getBlueTeam().getTeamName());
            blueTeamScore.setText(Integer.toString(match.getBlueScore()));
            blueTeamListView.setItems(FXCollections.observableArrayList(match.getBlueTeam().getBots()));
            blueTeamListView.refresh();
        }
        else{
            //updates blue team
            blueTeamNameLabel.setText("Blue team");
            blueTeamScore.setText("");
            blueTeamListView.setItems(null);
            blueTeamListView.refresh();
        }
        if (match.getOrangeTeam()!=null) {
            //Updates orange team
            orangeTeamNameLabel.setText(match.getOrangeTeam().getTeamName());
            orangeTeamScore.setText(Integer.toString(match.getOrangeScore()));
            orangeTeamListView.setItems(FXCollections.observableArrayList(match.getOrangeTeam().getBots()));
            orangeTeamListView.refresh();
        }
        else {
            //Updates orange team
            orangeTeamNameLabel.setText("Orange team");
            orangeTeamScore.setText("");
            orangeTeamListView.setItems(null);
            orangeTeamListView.refresh();
        }

    }

    /** Emsures that the score is legal and less than 999*/
    private void checkIntegerScore(String oldValue, String newValue, TextField teamScore){
        if (newValue.charAt(0)=='0' && newValue.length()!=1){
            System.out.println("Test");
            teamScore.setText(newValue.replaceFirst("0",""));
        }

        if(newValue.length()<=3) {
            if (!newValue.matches("\\d*")) {
                teamScore.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }else teamScore.setText(oldValue);

    }
}
