package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.SingleEliminationFormat;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchPlayedListener;
import dk.aau.cs.ds306e18.tournament.ui.controllers.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.controllers.MatchVisualController;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/** Used to display the a swiss stage. */
public class SingleEliminationNode extends GridPane {

    private final Insets MARGINS = new Insets(0, 0, 8, 0);

    private final SingleEliminationFormat singleElimination;
    private final BracketOverviewTabController boc;

    private ArrayList<MatchVisualController> mvcs = new ArrayList<>();

    /** Used to display the a swiss stage. */
    public SingleEliminationNode(SingleEliminationFormat singleElimination, BracketOverviewTabController boc){
        this.singleElimination = singleElimination;
        this.boc = boc;
        update();
    }

    /** Updates all UI elements for the single elimination stage. */
    private void update() {
        clean();

        Match[] matchArray = singleElimination.getMatchesAsArray();
        int rounds = singleElimination.getRounds();
        int matchInFirstRound = pow2(rounds);

        int m = 0; // match index
        for (int r = 0; r < rounds; r++) {
            int matchesInRound = pow2(r);
            int column = rounds - 1 - r;
            int cellSpan = pow2(column);

            // Add matches for round r
            for (int i = 0; i < matchesInRound; i++) {
                Match match = matchArray[m];
                m++;

                // Some matches can be null
                if (match != null) {
                    MatchVisualController mvc = boc.loadVisualMatch(match);
                    mvcs.add(mvc);
                    VBox box = new VBox(mvc.getRoot());
                    box.setAlignment(Pos.CENTER);
                    box.setMinHeight(85 * cellSpan);

                    add(box, column, i * cellSpan);
                    setRowSpan(box, cellSpan);
                    setMargin(box, MARGINS);
                    setValignment(box, VPos.CENTER);
                }
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

    private void clean() {

    }
}
