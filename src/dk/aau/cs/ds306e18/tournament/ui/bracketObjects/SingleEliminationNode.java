package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.SingleEliminationFormat;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.ui.controllers.BracketOverviewTabController;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/** Used to display the a swiss stage. */
public class SingleEliminationNode extends VBox {

    private BracketOverviewTabController boc;

    /** Used to display the a swiss stage. */
    public SingleEliminationNode(SingleEliminationFormat singleEliStage, BracketOverviewTabController boc){
        this.boc = boc;
        refreshMatches(singleEliStage, boc);
    }
    
    /** Refreshes this node to represent the given swiss stage.
     * @param swissStage the swiss stage to represent. */ //TODO Should be rename and reworked.
    private void refreshMatches(SingleEliminationFormat swissStage, BracketOverviewTabController boc){

        //TODO THIS IS VERY WORK IN PROGRESS! CREATED FOR TESTING PURPOSE

        //Clear content
        this.getChildren().clear();

        //Create that amount of VBoxs
        ArrayList<Match> allMatches = new ArrayList<>(swissStage.getAllMatches());

        //Get all matches from each round and add them to matching vbox
        for (Match match : allMatches) {
            this.getChildren().add(boc.loadVisualMatch(match).getRoot());
        }
    }
}
