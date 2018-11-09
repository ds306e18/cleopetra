package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
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
    private void initialize(){ }

    /** Gets called when a match is clicked. */
    @FXML
    void matchClicked() {
        boc.setSelectedMatch(this);
    }

    /** Used to set the BracketOverviewController. Is used to ref that this is clicked/Selected. */
    public void setBoc(BracketOverviewTabController boc){
        this.boc = boc;
    }

    /** Sets the fields that reorientates this controllers node. */
    public void setMyNodeObj(Node node){
        this.myNodeObj = node;
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

    /** Updates the state/ui of this match. */
    public void updateMatch(Match match){

        clearFields();
        this.lastSetMatch = match;

        //Load teams
        Team blueTeam = null;
        Team orangeTeam = null;
        try {
            blueTeam = match.getBlueTeam();
            orangeTeam = match.getOrangeTeam();
        }catch (IllegalStateException e){

        }

        //Set tags and id based on the given match and its status
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

    /** @return the last match that were given to the updateMatch method. */
    public Match getLastSetMatch() {
        return lastSetMatch;
    }
}
