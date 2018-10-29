package dk.aau.cs.ds306e18.tournament.oldui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.oldui.Tabs.BracketOverview;
import dk.aau.cs.ds306e18.tournament.model.Match;
import dk.aau.cs.ds306e18.tournament.model.SwissStage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/** Used to display the a swiss stage. */
public class SwissNode extends VBox {

    /** Used to display the a swiss stage. */
    public SwissNode(SwissStage swissStage, BracketOverview bracketOverview){
        refreshMatches(swissStage, bracketOverview);
    }
    
    /** Refreshes this vbox to represent the given swiss stage.
     * @param swissStage the swiss stage to represent. */ //TODO Should be rename and reworked.
    private void refreshMatches(SwissStage swissStage, BracketOverview bracketOverview){

        ArrayList<ArrayList<Match>> rounds = swissStage.getRounds();

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
