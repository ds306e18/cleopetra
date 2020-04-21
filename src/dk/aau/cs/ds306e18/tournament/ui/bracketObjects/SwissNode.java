package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.model.format.SwissFormat;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
import dk.aau.cs.ds306e18.tournament.model.match.MatchPlayedListener;
import dk.aau.cs.ds306e18.tournament.ui.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.SeriesVisualController;
import dk.aau.cs.ds306e18.tournament.ui.StatsTableWithPoints;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/** Used to display the a swiss stage. */
public class SwissNode extends HBox implements MatchPlayedListener, MatchChangeListener, ModelCoupledUI {

    private final Insets MARGINS = new Insets(0, 8, 8, 0);
    private final Insets COLUMN_MARGINS = new Insets(0, 32, 8, 0);
    private final Insets TABLE_MARGINS = new Insets(0, 64, 0, 0);

    private final SwissFormat swiss;
    private final BracketOverviewTabController boc;

    private Button generateRoundButton;
    private ArrayList<SeriesVisualController> mvcs = new ArrayList<>();
    private StatsTableWithPoints table;

    public SwissNode(SwissFormat swiss, BracketOverviewTabController boc) {
        this.boc = boc;
        this.swiss = swiss;
        // Register to events from all matches
        for (Series series : swiss.getAllMatches()) {
            series.registerMatchChangeListener(this);
            series.registerMatchPlayedListener(this);
        }
        update();
    }

    /** Updates all ui elements for the swiss stage. */
    private void update() {
        removeElements();

        table = new StatsTableWithPoints(swiss.getTeams(), swiss, swiss.getTeamPointsMap());
        HBox.setMargin(table, TABLE_MARGINS);
        getChildren().add(table);

        ArrayList<ArrayList<Series>> rounds = swiss.getRounds();
        int numberOfRounds = rounds.size();

        // Create that amount of columns matching the number of generated rounds.
        for (int i = 0; i < numberOfRounds; i++) {
            VBox column = new VBox();

            // Round Label
            Label roundLabel = new Label("Round " + (i + 1));
            VBox.setMargin(roundLabel, MARGINS);
            column.getChildren().add(roundLabel);

            // Get all matches from round and add them to the column
            ArrayList<Series> round = rounds.get(i);
            for (Series series : round) {

                SeriesVisualController vmatch = boc.loadSeriesVisual(series);
                VBox.setMargin(vmatch.getRoot(), MARGINS);
                column.getChildren().add(vmatch.getRoot());
                mvcs.add(vmatch);
            }

            getChildren().add(column);
            HBox.setMargin(column, COLUMN_MARGINS);
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
        for (Series m : swiss.getAllMatches()) {
            m.unregisterMatchChangeListener(this);
            m.unregisterMatchPlayedListener(this);
        }
    }

    /** Completely remove all ui elements. */
    public void removeElements() {
        for (SeriesVisualController mvc : mvcs) {
            mvc.decoupleFromModel();
        }
        getChildren().clear();
        mvcs.clear();
        generateRoundButton = null;
        if (table != null) {
            table.decoupleFromModel();
            table = null;
        }
    }

    /** @return the vbox that has the "generate next round" button. */
    private VBox getNextRoundVBox(int roundNumber) {

        // Column vbox
        VBox column = new VBox();

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
            for (Series m : swiss.getRounds().get(swiss.getRounds().size() - 1)) {
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
    public void onMatchPlayed(Series series) {
        updateGenerateRoundButton();
    }

    @Override
    public void onMatchChanged(Series series) {
    }
}
