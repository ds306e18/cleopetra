package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.oldui.Tabs.BracketOverview;
import dk.aau.cs.ds306e18.tournament.model.Match;
import dk.aau.cs.ds306e18.tournament.model.SwissStage;
import dk.aau.cs.ds306e18.tournament.oldui.bracketObjects.VisualMatch;
import dk.aau.cs.ds306e18.tournament.ui.controllers.BracketOverviewTabController;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/** Used to display the a swiss stage. */
public class SwissNode extends HBox {

    private BracketOverviewTabController boc;

    /** Used to display the a swiss stage. */
    public SwissNode(SwissStage swissStage, BracketOverviewTabController boc){
        this.boc = boc;
        refreshMatches(swissStage, boc);
    }
    
    /** Refreshes this vbox to represent the given swiss stage.
     * @param swissStage the swiss stage to represent. */ //TODO Should be rename and reworked.
    private void refreshMatches(SwissStage swissStage, BracketOverviewTabController boc){

        //Clear content
        //TODO ..

        //Get number of rounds
        int numberOfRounds = swissStage.getRounds().size();

        //Create that amount of VBoxs
        ArrayList<VBox> roundBoxs = new ArrayList<>();
        for(int i = 0; i < numberOfRounds; i++){
            roundBoxs.add(new VBox());
            roundBoxs.get(i).getChildren().add(new Label("Round " + i)); //Add labels to the VBox'
        }

        //Get all matches from each round and add them to matching vbox
        for(int i = 0; i < swissStage.getRounds().size(); i++){

            for (Match match : swissStage.getRounds().get(i)) {
                roundBoxs.get(i).getChildren().add(boc.loadVisualMatch(match));//Create a visual match from this.
                //Add that match to the right vbox
            }
        }

        //add all vboxs to this
        for (VBox vBox : roundBoxs)
            this.getChildren().add(vBox);



         /* ------------- BELOW IS OLD CODE ----------------- */
        ArrayList<ArrayList<Match>> rounds = swissStage.getRounds();

        /*
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
        }*/
    }
}
