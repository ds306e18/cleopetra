package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

public class MatchVisualController implements MatchChangeListener {

    @FXML private HBox matchRoot;
    @FXML private Label identifierLabel;
    @FXML private AnchorPane identifierHolder;
    @FXML private Label textTeamTwoName;
    @FXML private HBox teamOneScoreContainer;
    @FXML private Label textTeamOneName;
    @FXML private HBox teamTwoScoreContainer;
    @FXML private HBox hboxTeamTwo;
    @FXML private HBox hboxTeamOne;

    private BracketOverviewTabController boc;
    private Series showedSeries;
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

            if (showedSeries.isReadyToPlay()
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
        teamOneScoreContainer.getChildren().clear();
        teamTwoScoreContainer.getChildren().clear();
    }

    /** @return the match that this shows. */
    public Series getShowedSeries() {
        return showedSeries;
    }

    /** Updates the state/ui of this match. */
    public void setShowedSeries(Series series){
        if (showedSeries != null) showedSeries.unregisterMatchChangeListener(this);
        showedSeries = series;
        showedSeries.registerMatchChangeListener(this);
        updateFields();
    }

    @Override
    public void onMatchChanged(Series series) {
        updateFields();
    }

    public void updateFields() {
        clearFields();

        if (showedSeries == null) {
            return;
        }

        // Show identifier
        if (showIdentifier) {
            identifierHolder.setVisible(true);
            identifierHolder.setManaged(true);
            identifierLabel.setText("" + showedSeries.getIdentifier());
        } else {
            identifierHolder.setVisible(false);
            identifierHolder.setManaged(false);
        }

        Series.Status status = showedSeries.getStatus();
        Team teamOne = showedSeries.getTeamOne();
        Team teamTwo = showedSeries.getTeamTwo();

        // Set tags and id based on the given match and its status
        if (disabled) {

            // css id
            matchRoot.setId("disabled");
            setupBlankScores(teamOneScoreContainer, showedSeries.getSeriesLength());
            setupBlankScores(teamTwoScoreContainer, showedSeries.getSeriesLength());

        } else if (status == Series.Status.NOT_PLAYABLE) {
            // css id
            matchRoot.setId("pending");

            // Show known team or where they come from
            textTeamOneName.setText(showedSeries.getTeamOneAsString());
            textTeamTwoName.setText(showedSeries.getTeamTwoAsString());
            setupBlankScores(teamOneScoreContainer, showedSeries.getSeriesLength());
            setupBlankScores(teamTwoScoreContainer, showedSeries.getSeriesLength());
            if (teamOne == null) hboxTeamOne.getStyleClass().add("tbd");
            if (teamTwo == null) hboxTeamTwo.getStyleClass().add("tbd");

        } else {

            // Names and scores
            textTeamOneName.setText(teamOne.getTeamName());
            textTeamTwoName.setText(teamTwo.getTeamName());
            setupScores(teamOneScoreContainer, showedSeries.getTeamOneScores());
            setupScores(teamTwoScoreContainer, showedSeries.getTeamTwoScores());

            Series.Outcome outcome = showedSeries.getOutcome();

            // css ids
            if (status == Series.Status.READY_TO_BE_PLAYED || outcome == Series.Outcome.DRAW) {
                matchRoot.setId("ready");
            } else if (outcome == Series.Outcome.TEAM_ONE_WINS) {
                matchRoot.setId("played");
                hboxTeamOne.getStyleClass().add("winner");
            } else if (outcome == Series.Outcome.TEAM_TWO_WINS) {
                matchRoot.setId("played");
                hboxTeamTwo.getStyleClass().add("winner");
            }
        }

        // Set colors
        if (showedSeries.isTeamOneBlue()) {
            hboxTeamOne.getStyleClass().add("blue");
            hboxTeamTwo.getStyleClass().add("orange");
        } else {
            hboxTeamOne.getStyleClass().add("orange");
            hboxTeamTwo.getStyleClass().add("blue");
        }
    }

    private void setupBlankScores(HBox container, int seriesLength) {
        for (int i = 0; i < seriesLength; i++) {
            MatchScoreController msc = MatchScoreController.loadNew();
            if (msc != null) {
                msc.setScoreText("-");
                container.getChildren().add(msc.getRoot());
            }
        }
    }

    private void setupScores(HBox container, List<Optional<Integer>> scores) {
        for (Optional<Integer> score : scores) {
            MatchScoreController msc = MatchScoreController.loadNew();
            if (msc != null) {
                msc.setScoreText(score.map(Object::toString).orElse("-"));
                container.getChildren().add(msc.getRoot());
            }
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
        showedSeries.unregisterMatchChangeListener(this);
    }
}
