package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.SingleEliminationFormat;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.ui.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.SeriesVisualController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import static dk.aau.cs.ds306e18.tournament.utility.PowMath.pow2;

/** Used to display the a single elimination stage. */
public class SingleEliminationNode extends GridPane implements ModelCoupledUI {

    private final Insets MARGINS = new Insets(0, 0, 16, 0);
    private final int CELL_HEIGHT = 48;

    private final SingleEliminationFormat singleElimination;
    private final BracketOverviewTabController boc;

    private ArrayList<SeriesVisualController> mvcs = new ArrayList<>();

    /** Used to display the a single elimination stage. */
    public SingleEliminationNode(SingleEliminationFormat singleElimination, BracketOverviewTabController boc){
        this.singleElimination = singleElimination;
        this.boc = boc;
        update();
    }

    /** Updates all ui elements for the single elimination stage. */
    private void update() {
        removeElements();

        Series[] seriesArray = singleElimination.getMatchesAsArray();
        int rounds = singleElimination.getRounds();

        int m = 0; // match index
        for (int r = 0; r < rounds; r++) {
            int matchesInRound = pow2(r);
            int column = rounds - 1 - r;
            int cellSpan = pow2(column);

            // Add matches for round r
            for (int i = 0; i < matchesInRound; i++) {
                Series series = seriesArray[m];
                m++;
                VBox box = new VBox();

                // Some matches can be null
                if (series != null) {
                    SeriesVisualController mvc = boc.loadSeriesVisual(series);
                    mvcs.add(mvc);
                    box.getChildren().add(mvc.getRoot());
                    mvc.setShowIdentifier(true);
                }

                box.setAlignment(Pos.CENTER);
                box.setMinHeight(CELL_HEIGHT * cellSpan);

                add(box, column, (matchesInRound - 1 - i) * cellSpan);
                setRowSpan(box, cellSpan);
                setMargin(box, MARGINS);
                setValignment(box, VPos.CENTER);
            }
        }
    }

    @Override
    public void decoupleFromModel() {
        removeElements();
    }

    /** Completely remove all ui elements. */
    public void removeElements() {
        getChildren().clear();
        for (SeriesVisualController mvc : mvcs) {
            mvc.decoupleFromModel();
        }
        mvcs.clear();
    }
}
