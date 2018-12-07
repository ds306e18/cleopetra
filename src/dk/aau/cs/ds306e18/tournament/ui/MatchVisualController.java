package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MatchVisualController implements MatchChangeListener {

    @FXML private VBox matchRoot;
    @FXML private Label textOrangeName;
    @FXML private Text teamOrangeScore;
    @FXML private Label textBlueName;
    @FXML private Text teamBlueScore;
    @FXML private HBox hboxOrangeTeam;
    @FXML private HBox hboxBlueTeam;

    private BracketOverviewTabController boc;
    private Match showedMatch;

    @FXML
    private void initialize() { }

    /** Gets called when a match is clicked. */
    @FXML
    void matchClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)){
            if (event.getClickCount() == 2){
                boc.openEditMatchPopup();
            }
        }

        matchRoot.getStyleClass().add("selectedMatch");
        boc.setSelectedMatch(this);
    }

    /** Used to set the BracketOverviewController. Is used to ref that this is clicked/Selected. */
    public void setBoc(BracketOverviewTabController boc){
        this.boc = boc;
    }

    /** Clears the visuals match for both text and css. */
    private void clearFields(){
        matchRoot.setId("");
        hboxBlueTeam.getStyleClass().clear();
        hboxOrangeTeam.getStyleClass().clear();
        textBlueName.setText("TBD");
        textOrangeName.setText("TBD");
        teamOrangeScore.setText("");
        teamBlueScore.setText("");
    }

    /** Updates the state/ui of this match. */
    public void setShowedMatch(Match match){
        if (showedMatch != null) showedMatch.unregisterMatchChangeListener(this);
        showedMatch = match;
        showedMatch.registerMatchChangeListener(this);
        updateFields();
    }

    /** @return the match that this shows. */
    public Match getShowedMatch() {
        return showedMatch;
    }

    @Override
    public void onMatchChanged(Match match) {
        updateFields();
    }

    public void updateFields() {
        clearFields();

        if (showedMatch == null) {
            return;
        }

        Team blueTeam = showedMatch.getBlueTeam();
        Team orangeTeam = showedMatch.getOrangeTeam();

        //Set tags and id based on the given match and its status
        switch (showedMatch.getStatus()) {
            case NOT_PLAYABLE:
                //CSS
                matchRoot.setId("matchTBD");

                //DATA
                if(blueTeam != null)
                    textBlueName.setText(showedMatch.getBlueTeam().getTeamName());
                if(orangeTeam != null)
                    textOrangeName.setText(showedMatch.getOrangeTeam().getTeamName());

                break;
            case READY_TO_BE_PLAYED: case DRAW:
                //CSS
                matchRoot.setId("matchUpcoming");
                hboxBlueTeam.getStyleClass().add("blue");
                hboxOrangeTeam.getStyleClass().add("orange");

                //Data
                textBlueName.setText(showedMatch.getBlueTeam().getTeamName());
                textOrangeName.setText(showedMatch.getOrangeTeam().getTeamName());
                teamBlueScore.setText(String.valueOf(showedMatch.getBlueScore()));
                teamOrangeScore.setText(String.valueOf(showedMatch.getOrangeScore()));
                break;
            case BLUE_WINS:
                //CSS
                matchRoot.setId("matchPlayed");
                hboxBlueTeam.getStyleClass().add("blue");
                hboxBlueTeam.getStyleClass().add("winner");
                hboxOrangeTeam.getStyleClass().add("orange");

                //Data
                textBlueName.setText(showedMatch.getBlueTeam().getTeamName());
                textOrangeName.setText(showedMatch.getOrangeTeam().getTeamName());
                teamBlueScore.setText(String.valueOf(showedMatch.getBlueScore()));
                teamOrangeScore.setText(String.valueOf(showedMatch.getOrangeScore()));
                break;
            case ORANGE_WINS:
                //CSS
                matchRoot.setId("matchPlayed");
                hboxBlueTeam.getStyleClass().add("blue");
                hboxOrangeTeam.getStyleClass().add("orange");
                hboxOrangeTeam.getStyleClass().add("winner");

                //Data
                textBlueName.setText(showedMatch.getBlueTeam().getTeamName());
                textOrangeName.setText(showedMatch.getOrangeTeam().getTeamName());
                teamBlueScore.setText(String.valueOf(showedMatch.getBlueScore()));
                teamOrangeScore.setText(String.valueOf(showedMatch.getOrangeScore()));
                break;
            default: throw new IllegalStateException();
        }
    }

    public VBox getRoot() {
        return matchRoot;
    }

    /** Decouples the controller from the model, allowing the controller to be thrown to the garbage collector. */
    public void decoupleFromModel() {
        showedMatch.unregisterMatchChangeListener(this);
    }
}
