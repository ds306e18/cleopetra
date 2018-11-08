package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.Match;
import dk.aau.cs.ds306e18.tournament.model.Team;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MatchVisualController {

    @FXML private Text textOrangeName;
    @FXML private Text teamOrangeScore;
    @FXML private Text textBlueName;
    @FXML private Text teamBlueScore;
    @FXML private HBox hboxOrangeTeam;
    @FXML private HBox hboxBlueTeam;

    private BracketOverviewTabController boc;
    private Node myNodeObj;
    private Match lastSetMatch;

    @FXML
    private void initialize(){
        System.out.println("Match Visual Initialized");
    }

    /** Gets called when a match is clicked. */
    @FXML
    void matchClicked() {
        boc.setSelectedMatch(this);
    }

    /** Used to set the BracketOverviewController. Is used to ref that this is clicked/Selected. */
    public void setBoc(BracketOverviewTabController boc){
        this.boc = boc;
    }

    public void setMyNodeObj(Node node){
        this.myNodeObj = node;
    }

    public void updateMatch(Match match){
        this.myNodeObj.setId("TBD"); //Get status of match and set visual representation to match. //TODO TeMP
        this.myNodeObj.setId("matchplayed");
        this.lastSetMatch = match;
        this.textBlueName.setText(match.getBlueTeam().getTeamName());
        this.textOrangeName.setText(match.getOrangeTeam().getTeamName());
        this.teamBlueScore.setText(String.valueOf(match.getBlueScore()));
        this.teamOrangeScore.setText(String.valueOf(match.getOrangeScore()));
    }

    /** Clears the visuals match for both text and css. */
    private void clearFields(){
        myNodeObj.setId("");
        hboxBlueTeam.getStyleClass().clear();
        hboxOrangeTeam.getStyleClass().clear();
        textBlueName.setText("TBD");
        textOrangeName.setText("TBD");
        teamOrangeScore.setText("");
        teamBlueScore.setText("");
    }

    public void updateMatch2(Match match){

        clearFields();
        this.lastSetMatch = match;

        Team blueTeam = null;
        Team orangeTeam = null;
        try {
            blueTeam = match.getBlueTeam();
            orangeTeam = match.getOrangeTeam();
        }catch (IllegalStateException e){

        }

        switch (match.getStatus()) {
            case NOT_PLAYABLE:          //CSS
                                        myNodeObj.setId("matchTBD");

                                        //DATA
                                        if(blueTeam != null)
                                            textBlueName.setText(match.getBlueTeam().getTeamName());
                                        if(orangeTeam != null)
                                            textOrangeName.setText(match.getOrangeTeam().getTeamName());

                break;
            case READY_TO_BE_PLAYED:    //CSS
                                        myNodeObj.setId("matchUpcoming");
                                        hboxBlueTeam.getStyleClass().add("blue");
                                        hboxBlueTeam.getStyleClass().add("team1");
                                        hboxOrangeTeam.getStyleClass().add("orange");
                                        hboxOrangeTeam.getStyleClass().add("team2");

                                        //Data
                                        textBlueName.setText(match.getBlueTeam().getTeamName());
                                        textOrangeName.setText(match.getOrangeTeam().getTeamName());
                break;
            case BLUE_WINS:             //CSS
                                        myNodeObj.setId("matchPlayed");
                                        hboxBlueTeam.getStyleClass().add("blue");
                                        hboxBlueTeam.getStyleClass().add("team1");
                                        hboxBlueTeam.getStyleClass().add("winner");
                                        hboxOrangeTeam.getStyleClass().add("orange");
                                        hboxOrangeTeam.getStyleClass().add("team2");

                                        //Data
                                        textBlueName.setText(match.getBlueTeam().getTeamName());
                                        textOrangeName.setText(match.getOrangeTeam().getTeamName());
                                        teamBlueScore.setText(String.valueOf(match.getBlueScore()));
                                        teamOrangeScore.setText(String.valueOf(match.getOrangeScore()));
                break;
            case ORANGE_WINS:           //CSS
                                        myNodeObj.setId("matchPlayed");
                                        hboxBlueTeam.getStyleClass().add("blue");
                                        hboxBlueTeam.getStyleClass().add("team1");
                                        hboxOrangeTeam.getStyleClass().add("orange");
                                        hboxOrangeTeam.getStyleClass().add("team2");
                                        hboxOrangeTeam.getStyleClass().add("winner");

                                        //Data
                                        textBlueName.setText(match.getBlueTeam().getTeamName());
                                        textOrangeName.setText(match.getOrangeTeam().getTeamName());
                                        teamBlueScore.setText(String.valueOf(match.getBlueScore()));
                                        teamOrangeScore.setText(String.valueOf(match.getOrangeScore()));
                break;
            case DRAW:
            default: throw new IllegalStateException();
        }
    }

    public Match getLastSetMatch() {
        return lastSetMatch;
    }
}
