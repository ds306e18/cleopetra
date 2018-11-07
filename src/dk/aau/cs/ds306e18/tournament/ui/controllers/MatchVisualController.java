package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MatchVisualController {

    @FXML private Text textOrangeName;
    @FXML private Text teamOrangeScore;
    @FXML private Text textBlueName;
    @FXML private Text teamBlueScore;

    private BracketOverviewTabController boc;
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

    public void setMatch(Match match){
        this.lastSetMatch = match;
        this.textBlueName.setText(match.getBlueTeam().getTeamName());
        this.textOrangeName.setText(match.getOrangeTeam().getTeamName());
        this.teamBlueScore.setText(String.valueOf(match.getBlueScore()));
        this.teamOrangeScore.setText(String.valueOf(match.getOrangeScore()));
    }

    public Match getLastSetMatch() {
        return lastSetMatch;
    }
}
