package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.format.SwissFormat;
import dk.aau.cs.ds306e18.tournament.ui.controllers.BracketOverviewTabController;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/** Used to display the a swiss stage. */
public class SwissNode extends HBox {

    private BracketOverviewTabController boc;

    /** Used to display the a swiss stage. */
    public SwissNode(SwissFormat swissStage, BracketOverviewTabController boc){
        this.boc = boc;
        refreshMatches(swissStage, boc);
    }
    
    /** Refreshes this node to represent the given swiss stage.
     * @param swissStage the swiss stage to represent. */ //TODO Should be rename and reworked.
    private void refreshMatches(SwissFormat swissStage, BracketOverviewTabController boc){

        //Clear content
        this.getChildren().clear();

        //Get number of rounds
        int numberOfRounds = swissStage.getRounds().size();

        //Create that amount of VBoxs
        ArrayList<VBox> roundBoxs = new ArrayList<>();
        for(int i = 0; i < numberOfRounds; i++){
            roundBoxs.add(new VBox());
            roundBoxs.get(i).getChildren().add(new Label("Round " + (i+1))); //Add labels to the VBox'
        }

        //If there is another round that can be generated.
        //Then add a new Vbox with label and generate button.
        if(swissStage.getMaxRounds() > numberOfRounds){
            VBox lastVBox = new VBox();
            lastVBox.setMinWidth(175); //TODO magic number, but it is the same as the matches width.
            lastVBox.getChildren().add(new Label("Round " + (numberOfRounds+1)));
            Button generateButton = new Button();
            generateButton.setText("Generate Round");
            generateButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    boc.generateNewSwissRound();
                }
            });

            //Is it not allowed to generate new round? Disable button.
            if(!swissStage.isGenerateNewRoundAllowed())
                generateButton.setDisable(true);
            else
                generateButton.setDisable(false);
            
            lastVBox.getChildren().add(generateButton);
            roundBoxs.add(lastVBox);
        }

        //Get all matches from each round and add them to matching vbox
        for(int i = 0; i < swissStage.getRounds().size(); i++){

            for (Match match : swissStage.getRounds().get(i)) {
                roundBoxs.get(i).getChildren().add(boc.loadVisualMatch(match));//Create a visual match from this.
                //Add that match to the right vbox
            }
        }

        //Add all vboxs to this
        for (VBox vBox : roundBoxs)
            this.getChildren().add(vBox);
    }
}
