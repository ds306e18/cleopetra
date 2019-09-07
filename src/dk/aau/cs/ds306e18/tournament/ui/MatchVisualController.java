package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MatchVisualController implements MatchChangeListener {

    @FXML private HBox matchRoot;
    @FXML private Label identifierLabel;
    @FXML private AnchorPane identifierHolder;
    @FXML private Label textTeamTwoName;
    @FXML private Text textTeamTwoScore;
    @FXML private Label textTeamOneName;
    @FXML private Text textTeamOneScore;
    @FXML private HBox hboxTeamTwo;
    @FXML private HBox hboxTeamOne;

    private BracketOverviewTabController boc;
    private Match showedMatch;
    private boolean showIdentifier = false;
    private boolean disabled = false;

    @FXML
    private void initialize() { }

    /** Gets called when a match is clicked. */
    @FXML
    void matchClicked(MouseEvent event) {
        if (!disabled) {
            matchRoot.getStyleClass().add("selectedMatch");
            boc.setSelectedMatch(this);

            if (showedMatch.isReadyToPlay()
                    && event.getButton().equals(MouseButton.PRIMARY)
                    && event.getClickCount() == 2) {

                boc.openEditMatchPopup();
            }
        }
    }

    /** Used to set the BracketOverviewController. Is used to ref that this is clicked/Selected. */
    public void setBoc(BracketOverviewTabController boc){
        this.boc = boc;
    }

    /** Clears the visuals match for both text and css. */
    private void clearFields(){
        matchRoot.setId("");
        hboxTeamOne.getStyleClass().clear();
        hboxTeamTwo.getStyleClass().clear();
        textTeamOneName.setText(" ");
        textTeamTwoName.setText(" ");
        textTeamTwoScore.setText(" ");
        textTeamOneScore.setText(" ");
    }

    /** @return the match that this shows. */
    public Match getShowedMatch() {
        return showedMatch;
    }

    /** Updates the state/ui of this match. */
    public void setShowedMatch(Match match){
        if (showedMatch != null) showedMatch.unregisterMatchChangeListener(this);
        showedMatch = match;
        showedMatch.registerMatchChangeListener(this);
        updateFields();
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

        // Show identifier
        if (showIdentifier) {
            identifierHolder.setVisible(true);
            identifierHolder.setManaged(true);
            identifierLabel.setText("" + showedMatch.getIdentifier());
        } else {
            identifierHolder.setVisible(false);
            identifierHolder.setManaged(false);
        }

        Match.Status status = showedMatch.getStatus();
        Team teamOne = showedMatch.getTeamOne();
        Team teamTwo = showedMatch.getTeamTwo();

        // Set tags and id based on the given match and its status
        if (disabled) {

            // css id
            matchRoot.setId("disabled");

        } else if (status == Match.Status.NOT_PLAYABLE) {
            // css id
            matchRoot.setId("pending");

            // Show known team or where they come from
            textTeamOneName.setText(showedMatch.getTeamOneAsString());
            textTeamTwoName.setText(showedMatch.getTeamTwoAsString());
            if (teamOne == null) hboxTeamOne.getStyleClass().add("tbd");
            if (teamTwo == null) hboxTeamTwo.getStyleClass().add("tbd");

        } else {

            // Names and scores
            textTeamOneName.setText(teamOne.getTeamName());
            textTeamTwoName.setText(teamTwo.getTeamName());
            textTeamOneScore.setText(String.valueOf(showedMatch.getTeamOneScore()));
            textTeamTwoScore.setText(String.valueOf(showedMatch.getTeamTwoScore()));

            Match.Outcome outcome = showedMatch.getOutcome();

            // css ids
            if (status == Match.Status.READY_TO_BE_PLAYED || outcome == Match.Outcome.DRAW) {
                matchRoot.setId("ready");
            } else if (outcome == Match.Outcome.TEAM_ONE_WINS) {
                matchRoot.setId("played");
                hboxTeamOne.getStyleClass().add("winner");
            } else if (outcome == Match.Outcome.TEAM_TWO_WINS) {
                matchRoot.setId("played");
                hboxTeamTwo.getStyleClass().add("winner");
            }
        }

        // Set colors
        if (showedMatch.isTeamOneBlue()) {
            hboxTeamOne.getStyleClass().add("blue");
            hboxTeamTwo.getStyleClass().add("orange");
        } else {
            hboxTeamOne.getStyleClass().add("orange");
            hboxTeamTwo.getStyleClass().add("blue");
        }
    }

    public HBox getRoot() {
        return matchRoot;
    }

    public boolean isIdentifierShown() {
        return showIdentifier;
    }

    public void setShowIdentifier(boolean showIdentifier) {
        this.showIdentifier = showIdentifier;
        updateFields();
    }

    public boolean isDisabled() {
        return disabled;
    }

    /** When disabled the match is visible but blank and cannot be interacted with. */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        updateFields();
        if (disabled) {
            matchRoot.getStyleClass().remove("selectable");
        } else {
            matchRoot.getStyleClass().add("selectable");
        }
    }

    /** Decouples the controller from the model, allowing the controller to be thrown to the garbage collector. */
    public void decoupleFromModel() {
        showedMatch.unregisterMatchChangeListener(this);
    }
}
