package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.format.SwissFormat;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
import dk.aau.cs.ds306e18.tournament.model.match.MatchPlayedListener;
import dk.aau.cs.ds306e18.tournament.ui.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.MatchVisualController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/** Used to display the a swiss stage. */
public class SwissNode extends HBox implements MatchPlayedListener, MatchChangeListener, ModelCoupledUI {

    private final Insets MARGINS = new Insets(0, 0, 8, 0);
    private final int COLUMN_WIDTH = 175;

    private final SwissFormat swiss;
    private final BracketOverviewTabController boc;

    private Button generateRoundButton;
    private ArrayList<MatchVisualController> mvcs = new ArrayList<>();

    public SwissNode(SwissFormat swiss, BracketOverviewTabController boc) {
        this.boc = boc;
        this.swiss = swiss;
        // Register to events from all matches
        for (Match match : swiss.getAllMatches()) {
            match.registerMatchChangeListener(this);
            match.registerMatchPlayedListener(this);
        }
        boc.showLeaderboard(true);
        update();
    }

    /** Updates all ui elements for the swiss stage. */
    private void update() {
        removeElements();

        /* Assign initial teams and points to leaderboard */
        boc.refreshLeaderboard(swiss.getTeamPointsMap());

        ArrayList<ArrayList<Match>> rounds = swiss.getRounds();
        int numberOfRounds = rounds.size();

        // Create that amount of columns matching the number of generated rounds.
        for (int i = 0; i < numberOfRounds; i++) {
            VBox column = new VBox();
            column.setMinWidth(COLUMN_WIDTH);
            column.setPrefWidth(COLUMN_WIDTH);

            // Round Label
            Label roundLabel = new Label("Round " + (i + 1));
            VBox.setMargin(roundLabel, MARGINS);
            column.getChildren().add(roundLabel);

            // Get all matches from round and add them to the column
            ArrayList<Match> round = rounds.get(i);
            for (Match match : round) {

                MatchVisualController vmatch = boc.loadVisualMatch(match);
                VBox.setMargin(vmatch.getRoot(), MARGINS);
                column.getChildren().add(vmatch.getRoot());
                mvcs.add(vmatch);
            }

            getChildren().add(column);
        }

        // If there can be generated another round, then add a column more that contains a button for generating.
        if (swiss.hasUnstartedRounds()) {
            getChildren().add(getNextRoundVBox(numberOfRounds + 1));
        }
    }

    @Override
    public void decoupleFromModel() {
        removeElements();
        // Unregister from events from all matches
        for (Match m : swiss.getAllMatches()) {
            m.unregisterMatchChangeListener(this);
            m.unregisterMatchPlayedListener(this);
        }
        boc.showLeaderboard(false); // TODO Move leaderboard to swiss node
    }

    /** Completely remove all ui elements. */
    public void removeElements() {
        for (MatchVisualController mvc : mvcs) {
            mvc.decoupleFromModel();
        }
        getChildren().clear();
        mvcs.clear();
        generateRoundButton = null;
    }

    /** @return the vbox that has the "generate next round" button. */
    private VBox getNextRoundVBox(int roundNumber) {

        // Column vbox
        VBox column = new VBox();
        column.setMinWidth(COLUMN_WIDTH);
        column.setPrefWidth(COLUMN_WIDTH);

        // Round Label
        Label roundLabel = new Label("Round " + roundNumber);
        VBox.setMargin(roundLabel, MARGINS);
        column.getChildren().add(roundLabel);

        // Generate next round button
        generateRoundButton = new Button();
        generateRoundButton.setText("Generate Round");
        generateRoundButton.setOnMouseClicked(e -> {
            swiss.startNextRound();
            // Register to events from newest round
            for (Match m : swiss.getRounds().get(swiss.getRounds().size() - 1)) {
                m.registerMatchPlayedListener(this);
                m.registerMatchChangeListener(this);
            }
            update();
        });
        column.getChildren().add(generateRoundButton);
        updateGenerateRoundButton();

        return column;
    }

    /** Enables or disables generate round button depending on the state. */
    private void updateGenerateRoundButton() {
        if (generateRoundButton != null)
            generateRoundButton.setDisable(!swiss.canStartNextRound());
    }

    @Override
    public void onMatchPlayed(Match match) {
        updateGenerateRoundButton();
    }

    @Override
    public void onMatchChanged(Match match) {
        boc.refreshLeaderboard(swiss.getTeamPointsMap());
    }
}
