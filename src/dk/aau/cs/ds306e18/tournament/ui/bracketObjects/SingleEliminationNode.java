package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.SingleEliminationFormat;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.ui.controllers.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.controllers.MatchVisualController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/** Used to display the a single elimination stage. */
public class SingleEliminationNode extends GridPane implements ModelCoupledUI {

    private final Insets MARGINS = new Insets(0, 0, 8, 0);
    private final int CELL_SIZE = 50;

    private final SingleEliminationFormat singleElimination;
    private final BracketOverviewTabController boc;

    private ArrayList<MatchVisualController> mvcs = new ArrayList<>();

    /** Used to display the a single elimination stage. */
    public SingleEliminationNode(SingleEliminationFormat singleElimination, BracketOverviewTabController boc){
        this.singleElimination = singleElimination;
        this.boc = boc;
        update();
    }

    /** Updates all UI elements for the single elimination stage. */
    private void update() {
        removeElements();

        Match[] matchArray = singleElimination.getMatchesAsArray();
        int rounds = singleElimination.getRounds();

        int m = 0; // match index
        for (int r = 0; r < rounds; r++) {
            int matchesInRound = pow2(r);
            int column = rounds - 1 - r;
            int cellSpan = pow2(column);

            // Add matches for round r
            for (int i = 0; i < matchesInRound; i++) {
                Match match = matchArray[m];
                m++;
                VBox box = new VBox();

                // Some matches can be null
                if (match != null) {
                    MatchVisualController mvc = boc.loadVisualMatch(match);
                    mvcs.add(mvc);
                    box.getChildren().add(mvc.getRoot());
                }

                box.setAlignment(Pos.CENTER);
                box.setMinHeight(CELL_SIZE * cellSpan);

                add(box, column, (matchesInRound - 1 - i) * cellSpan);
                setRowSpan(box, cellSpan);
                setMargin(box, MARGINS);
                setValignment(box, VPos.CENTER);
            }
        }
    }

    /** Returns 2 to the power of n. */
    private int pow2(int n) {
        int res = 1;
        for (int i = 0; i < n; i++) {
            res *= 2;
        }
        return res;
    }

    @Override
    public void decoupleFromModel() {
        removeElements();
    }

    /** Completely remove all UI elements. */
    public void removeElements() {
        getChildren().clear();
        for (MatchVisualController mvc : mvcs) {
            mvc.decoupleFromModel();
        }
        mvcs.clear();
    }
}
