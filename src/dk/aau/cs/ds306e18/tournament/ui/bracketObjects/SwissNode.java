package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.format.SwissFormat;
import dk.aau.cs.ds306e18.tournament.ui.controllers.BracketOverviewTabController;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.Arrays;

/** Used to display the a swiss stage. */
public class SwissNode extends HBox {

    private BracketOverviewTabController boc;

    /** Used to display the a swiss stage. */
    public SwissNode(SwissFormat format, BracketOverviewTabController boc){
        this.boc = boc;

        refreshMatches(format, boc);
    }
    
    /** Refreshes this node to represent the given swiss stage.
     * @param format the swiss stage to represent. */ //TODO Should be rename and reworked.
    private void refreshMatches(SwissFormat format, BracketOverviewTabController boc){

        //Get number of rounds
        int numberOfRounds = format.getRounds().size();

        //Create that amount of VBoxs matching the number of swiss rounds.
        ArrayList<VBox> roundBoxs = new ArrayList<>();
        for(int i = 0; i < numberOfRounds; i++){
            roundBoxs.add(new VBox());
            roundBoxs.get(i).getChildren().add(new Label("Round " + (i+1))); //Add labels to the VBox'
        }

        //If there can be generated another round, then add a vbox that allows this (Button for generating).
        if(format.getMaxRounds() > numberOfRounds)
            roundBoxs.add(getNextRoundVBox(format));

        //Get all matches from each round and add them to the matching vbox
        for(int i = 0; i < format.getRounds().size(); i++){
            for (Match match : format.getRounds().get(i)) {
                roundBoxs.get(i).getChildren().add(boc.loadVisualMatch(match)); //Create a visual match
            }
        }

        ArrayList<VBox> arrowBoxes = new ArrayList<>();

        //Check if there is atleast two boxes present in roundBoxes
        if(roundBoxs.size() >= 2){
            //Create arrows
            for(int i = 0; i < roundBoxs.size() -1; i++){
                arrowBoxes.add(getTriangleBox());
            }
        }

        //Add all vboxs to this
        for (VBox roundBox : roundBoxs) {
            this.getChildren().addAll(roundBox);
            if (arrowBoxes.size() != 0) {
                this.getChildren().add(arrowBoxes.get(0));
                arrowBoxes.remove(0);
            }
        }
    }

    /** @return the vbox that has the "generate next round" button. */
    private VBox getNextRoundVBox(SwissFormat swissFormat){

        int numberOfRounds = swissFormat.getRounds().size();

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
        if(!swissFormat.isGenerateNewRoundAllowed())
            generateButton.setDisable(true);
        else
            generateButton.setDisable(false);

        lastVBox.getChildren().add(generateButton);

        return lastVBox;
    }

    /** @return a vbox containing the triangle that is shown between matches. */
    private VBox getTriangleBox(){

        VBox arrowBox = new VBox();
        VBox topSpacer = new VBox();
        VBox content = new VBox();
        VBox bottomSpacer = new VBox();
        topSpacer.setVgrow(topSpacer, Priority.ALWAYS); //TODO maybe first parameter wrong.
        arrowBox.setVgrow(bottomSpacer, Priority.ALWAYS); //TODO maybe first parameter wrong.

        Polygon roundArrow = new Polygon();
        //double matchVBoxHeight = roundBoxs.get(1).getLayoutBounds().getHeight(); //Get window height //TODO does not work, the height will first calculate on stage.show
        //double matchVBoxHeight = roundBoxs.get(0).getChildren().size() * roundBoxs.get(0).getChildren().get(1).  ; //Get window height
        double matchVBoxHeight = 500;
        double spacing = matchVBoxHeight/10; //The spacing in the top and bottom of the triangle.
        double triangleWidth = matchVBoxHeight/10;
        roundArrow.getPoints().addAll(Arrays.asList(
                0d, 0d + spacing,
                triangleWidth, matchVBoxHeight/2,
                0d, matchVBoxHeight - spacing));

        content.getChildren().add(roundArrow);
        arrowBox.getChildren().addAll(topSpacer, content, bottomSpacer);
        //this.getChildren().add(arrowBox);

        return arrowBox;
    }
}
