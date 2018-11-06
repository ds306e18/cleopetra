package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MatchVisualController {

    @FXML private Text teamTwoScore;
    @FXML private Text textTeam1;
    @FXML private Text teamOneScore;
    @FXML private Text textTeam2;

    @FXML
    private void initialize(){
        System.out.println("Match Visual Initialized");
    }

    /** Gets called when a match is clicked. */
    @FXML
    void matchClicked() {
        System.out.println("Clicked");
    }

    public void setMatch(Match match){
        this.textTeam1.setText(match.getOrangeTeam().getTeamName());
        this.textTeam2.setText(match.getBlueTeam().getTeamName());
        this.teamOneScore.setText(String.valueOf(match.getOrangeScore()));
        this.teamTwoScore.setText(String.valueOf(match.getBlueScore()));
    }
}
