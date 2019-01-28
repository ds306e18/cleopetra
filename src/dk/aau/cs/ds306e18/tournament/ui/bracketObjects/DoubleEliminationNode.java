package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.*;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
import dk.aau.cs.ds306e18.tournament.ui.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.MatchVisualController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import static dk.aau.cs.ds306e18.tournament.utility.PowMath.pow2;

public class DoubleEliminationNode extends VBox implements ModelCoupledUI, StageStatusChangeListener {

    private final Insets MARGINS = new Insets(0, 0, 16, 0);
    private final int SPACE_BETWEEN_BRACKETS = 48;
    private final int CELL_HEIGHT = 50;

    private final DoubleEliminationFormat doubleElimination;
    private final BracketOverviewTabController boc;
    private final GridPane upperGrid = new GridPane();
    private final GridPane lowerGrid = new GridPane();
    private MatchVisualController extraMatchMVC;

    private ArrayList<MatchVisualController> mvcs = new ArrayList<>();

    /** Used to display the a double elimination stage. */
    public DoubleEliminationNode(DoubleEliminationFormat doubleElimination, BracketOverviewTabController boc){
        this.doubleElimination = doubleElimination;
        this.boc = boc;

        doubleElimination.registerStatusChangedListener(this);

        getChildren().add(upperGrid);
        getChildren().add(lowerGrid);
        setSpacing(SPACE_BETWEEN_BRACKETS);

        update();
    }

    /** Updates all ui elements for the double elimination stage. */
    private void update() {
        removeElements();

        // Upper bracket
        Match[] upperBracket = doubleElimination.getUpperBracket();
        int rounds = doubleElimination.getUpperBracketRounds();
        int m = 0; // match index
        for (int r = 0; r < rounds; r++) {
            int matchesInRound = pow2(r);
            int column = rounds - 1 - r;
            int rowSpan = pow2(column);

            // Add matches for round r
            for (int i = 0; i < matchesInRound; i++) {
                Match match = upperBracket[m++];
                VBox box = createMatchBox(match, rowSpan, false);
                upperGrid.add(box, column, (matchesInRound - 1 - i) * rowSpan);
            }
        }

        // Final match is added to upper bracket
        VBox finalsBox = createMatchBox(doubleElimination.getFinalMatch(), pow2(rounds - 1), false);
        upperGrid.add(finalsBox, rounds, 0);

        // Extra match is only shown when needed
        VBox extraBox = createMatchBox(doubleElimination.getExtraMatch(), pow2(rounds - 1), true);
        upperGrid.add(extraBox, rounds + 1, 0);
        updateDisplayOfExtraMatch();

        // Lower bracket
        Match[] lowerBracket = doubleElimination.getLowerBracket();
        int matchesInCurrentRound = pow2(rounds - 2);
        int column = 0;
        int rowSpan = 1;
        m = 0; // match index
        while (true) {

            for (int i = 0; i < matchesInCurrentRound; i++) {
                Match match = lowerBracket[m++];
                VBox box = createMatchBox(match, rowSpan, false);
                lowerGrid.add(box, column, i * rowSpan);
            }

            // Half the number of matches in a round every other round
            if (column % 2 == 1) {
                if (matchesInCurrentRound == 1) {
                    break; // We can't do more halving
                }
                matchesInCurrentRound /= 2;
                rowSpan *= 2;
            }

            column ++;
        }
    }

    private VBox createMatchBox(Match match, int rowSpan, boolean isExtra) {

        VBox box = new VBox();

        // Some matches are null because of byes. In those cases the VBox will just be empty
        if (match != null) {
            MatchVisualController mvc = boc.loadVisualMatch(match);
            mvcs.add(mvc);
            box.getChildren().add(mvc.getRoot());
            mvc.setShowIdentifier(true);

            if (isExtra) {
                extraMatchMVC = mvc;
            }
        }

        box.setAlignment(Pos.CENTER);
        box.setMinHeight(CELL_HEIGHT * rowSpan);

        GridPane.setRowSpan(box, rowSpan);
        GridPane.setMargin(box, MARGINS);
        GridPane.setValignment(box, VPos.CENTER);

        return box;
    }

    @Override
    public void decoupleFromModel() {
        removeElements();
        doubleElimination.unregisterStatusChangedListener(this);
    }

    /** Completely remove all ui elements. */
    public void removeElements() {
        upperGrid.getChildren().clear();
        lowerGrid.getChildren().clear();
        for (MatchVisualController mvc : mvcs) {
            mvc.decoupleFromModel();
        }
        mvcs.clear();
    }

    private void updateDisplayOfExtraMatch() {
        extraMatchMVC.setDoubleEliminationExtra(!doubleElimination.isExtraMatchNeeded());
    }

    @Override
    public void onStageStatusChanged(Format format, StageStatus oldStatus, StageStatus newStatus) {
        updateDisplayOfExtraMatch();
    }
}
