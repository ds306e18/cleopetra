package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.SingleEliminationFormat;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.ui.controllers.BracketOverviewTabController;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/** Used to display the a swiss stage. */
public class SingleEliminationNode extends GridPane {

    private final SingleEliminationFormat singleElimination;
    private final BracketOverviewTabController boc;

    /** Used to display the a swiss stage. */
    public SingleEliminationNode(SingleEliminationFormat singleElimination, BracketOverviewTabController boc){
        this.singleElimination = singleElimination;
        this.boc = boc;
    }
}
