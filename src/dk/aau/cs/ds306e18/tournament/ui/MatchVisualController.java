package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
import dk.aau.cs.ds306e18.tournament.model.match.MatchStatus;
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
    @FXML private Label textOrangeName;
    @FXML private Text teamOrangeScore;
    @FXML private Label textBlueName;
    @FXML private Text teamBlueScore;
    @FXML private HBox hboxOrangeTeam;
    @FXML private HBox hboxBlueTeam;

    private BracketOverviewTabController boc;
    private Match showedMatch;
    private boolean showIdentifier = false;
    private boolean isDoubleEliminationExtra = false;

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
        hboxBlueTeam.getStyleClass().remove("winner");
        hboxOrangeTeam.getStyleClass().remove("winner");
        hboxBlueTeam.getStyleClass().remove("tbd");
        hboxOrangeTeam.getStyleClass().remove("tbd");
        textBlueName.setText(" ");
        textOrangeName.setText(" ");
        teamOrangeScore.setText(" ");
        teamBlueScore.setText(" ");
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

        // Show identifier
        if (showIdentifier) {
            identifierHolder.setVisible(true);
            identifierHolder.setManaged(true);
            identifierLabel.setText("" + showedMatch.getIdentifier());
        } else {
            identifierHolder.setVisible(false);
            identifierHolder.setManaged(false);
        }

        MatchStatus status = showedMatch.getStatus();
        Team blueTeam = showedMatch.getBlueTeam();
        Team orangeTeam = showedMatch.getOrangeTeam();

        // Set tags and id based on the given match and its status
        if (isDoubleEliminationExtra) {

            // css id
            matchRoot.setId("de-extra");

        } else if (status == MatchStatus.NOT_PLAYABLE) {
            // css id
            matchRoot.setId("pending");

            // Show known team or where they come from
            textBlueName.setText(showedMatch.getBlueTeamAsString());
            textOrangeName.setText(showedMatch.getOrangeTeamAsString());
            if (blueTeam == null) hboxBlueTeam.getStyleClass().add("tbd");
            if (orangeTeam == null) hboxOrangeTeam.getStyleClass().add("tbd");

        } else {

            // Names and scores
            textBlueName.setText(showedMatch.getBlueTeam().getTeamName());
            textOrangeName.setText(showedMatch.getOrangeTeam().getTeamName());
            teamBlueScore.setText(String.valueOf(showedMatch.getBlueScore()));
            teamOrangeScore.setText(String.valueOf(showedMatch.getOrangeScore()));

            // css ids
            if (status == MatchStatus.READY_TO_BE_PLAYED || status == MatchStatus.DRAW) {
                matchRoot.setId("ready");
            } else if (status == MatchStatus.BLUE_WINS) {
                matchRoot.setId("played");
                hboxBlueTeam.getStyleClass().add("winner");
            } else if (status == MatchStatus.ORANGE_WINS) {
                matchRoot.setId("played");
                hboxOrangeTeam.getStyleClass().add("winner");
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

    public boolean isDoubleEliminationExtra() {
        return isDoubleEliminationExtra;
    }

    /** As an extra it will be disabled unless needed. */
    public void setDoubleEliminationExtra(boolean doubleEliminationExtra) {
        isDoubleEliminationExtra = doubleEliminationExtra;
        updateFields();
    }

    /** Decouples the controller from the model, allowing the controller to be thrown to the garbage collector. */
    public void decoupleFromModel() {
        showedMatch.unregisterMatchChangeListener(this);
    }
}
