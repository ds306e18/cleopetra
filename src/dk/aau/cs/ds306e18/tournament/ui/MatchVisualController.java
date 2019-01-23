package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MatchVisualController implements MatchChangeListener {

    @FXML private HBox matchRoot;
    @FXML private Label indexLabel;
    @FXML private AnchorPane indexHolder;
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
        hboxBlueTeam.getStyleClass().add("blue");
        hboxOrangeTeam.getStyleClass().add("orange");
        textBlueName.setText("");
        textOrangeName.setText("");
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

        // Show index
        if (showedMatch.hasIndex()) {
            indexHolder.setVisible(true);
            indexHolder.setManaged(true);
            indexLabel.setText("" + showedMatch.getIndex());
        } else {
            indexHolder.setVisible(false);
            indexHolder.setManaged(false);
        }

        Team blueTeam = showedMatch.getBlueTeam();
        Team orangeTeam = showedMatch.getOrangeTeam();

        // Set tags and id based on the given match and its status
        switch (showedMatch.getStatus()) {
            case NOT_PLAYABLE:
                // css id
                matchRoot.setId("pending");

                // Show known team or where they come from
                textBlueName.setText(showedMatch.getBlueTeamAsString());
                textOrangeName.setText(showedMatch.getOrangeTeamAsString());
                if (blueTeam == null) hboxBlueTeam.getStyleClass().add("tbd");
                if (orangeTeam == null) hboxOrangeTeam.getStyleClass().add("tbd");
                break;

            case READY_TO_BE_PLAYED: case DRAW:
                // css id
                matchRoot.setId("ready");

                // Names and scores
                textBlueName.setText(showedMatch.getBlueTeam().getTeamName());
                textOrangeName.setText(showedMatch.getOrangeTeam().getTeamName());
                teamBlueScore.setText(String.valueOf(showedMatch.getBlueScore()));
                teamOrangeScore.setText(String.valueOf(showedMatch.getOrangeScore()));
                break;

            case BLUE_WINS:
                // css id
                matchRoot.setId("played");
                hboxBlueTeam.getStyleClass().add("winner");

                // Names and scores
                textBlueName.setText(showedMatch.getBlueTeam().getTeamName());
                textOrangeName.setText(showedMatch.getOrangeTeam().getTeamName());
                teamBlueScore.setText(String.valueOf(showedMatch.getBlueScore()));
                teamOrangeScore.setText(String.valueOf(showedMatch.getOrangeScore()));
                break;

            case ORANGE_WINS:
                // css id
                matchRoot.setId("played");
                hboxOrangeTeam.getStyleClass().add("winner");

                // Names and scores
                textBlueName.setText(showedMatch.getBlueTeam().getTeamName());
                textOrangeName.setText(showedMatch.getOrangeTeam().getTeamName());
                teamBlueScore.setText(String.valueOf(showedMatch.getBlueScore()));
                teamOrangeScore.setText(String.valueOf(showedMatch.getOrangeScore()));
                break;

            default: throw new IllegalStateException();
        }
    }

    public HBox getRoot() {
        return matchRoot;
    }

    /** Decouples the controller from the model, allowing the controller to be thrown to the garbage collector. */
    public void decoupleFromModel() {
        showedMatch.unregisterMatchChangeListener(this);
    }
}
