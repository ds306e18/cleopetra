package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.ui.tabs.BracketOverview;
import dk.aau.cs.ds306e18.tournament.model.Match;
import dk.aau.cs.ds306e18.tournament.model.SwissFormat;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/** Used to display the a swiss stage. */
public class SwissNode extends VBox {

    /** Used to display the a swiss stage. */
    public SwissNode(SwissFormat swissFormat, BracketOverview bracketOverview){
        refreshMatches(swissFormat, bracketOverview);
    }
    
    /** Refreshes this vbox to represent the given swiss stage.
     * @param swissFormat the swiss stage to represent. */ //TODO Should be rename and reworked.
    private void refreshMatches(SwissFormat swissFormat, BracketOverview bracketOverview){

        ArrayList<ArrayList<Match>> rounds = swissFormat.getRounds();

        //Go through all rounds.
        for(int i = 0; i < rounds.size(); i++){

            //Add label and matches for each round.
            Label label = new Label("Round " + i);
            ArrayList<VisualMatch> visualMatches = new ArrayList<>();

            for(Match match : rounds.get(i)){
                visualMatches.add(new VisualMatch(match, bracketOverview));
            }

            //Add label and matches for round.
            getChildren().add(label);
            getChildren().addAll(visualMatches);
        }
    }
}
