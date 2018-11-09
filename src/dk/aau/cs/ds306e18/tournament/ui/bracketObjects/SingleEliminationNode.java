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
public class SingleEliminationNode extends ScrollPane {

    private BracketOverviewTabController boc;

    /** Used to display the a swiss stage. */
    public SingleEliminationNode(SingleEliminationFormat singleEliStage, BracketOverviewTabController boc){
        this.boc = boc;

        this.setContent(getContentTable(singleEliStage, boc));
        this.setPannable(true); //Enables dragging with the mouse
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
    }

    /** Refreshes this node to represent the given swiss stage.
     * @param singleEliFormat the swiss stage to represent. */ //TODO Should be rename and reworked.
    private GridPane getContentTable(SingleEliminationFormat singleEliFormat, BracketOverviewTabController boc){

        GridPane content = new GridPane();

        int numberOfMatches = singleEliFormat.getAllMatches().size();
        if(numberOfMatches < 1)
            //TODO
            ;



        //int treeDepth = singleEliFormat.g

        //singleEliFormat.


        return content;
    }

    private int getTreeDepth(int numberOfMatches){
        return 0; //TODO
    }
}
