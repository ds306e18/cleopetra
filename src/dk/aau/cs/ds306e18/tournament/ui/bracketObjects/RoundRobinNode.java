package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.RoundRobinFormat;
import dk.aau.cs.ds306e18.tournament.model.format.RoundRobinGroup;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.ui.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.MatchVisualController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class RoundRobinNode extends HBox implements ModelCoupledUI {

    private final Insets MARGINS = new Insets(0, 0, 8, 0);
    private final int COLUMN_WIDTH = 175;

    private final RoundRobinFormat roundRobin;
    private final BracketOverviewTabController boc;

    private ArrayList<MatchVisualController> mvcs = new ArrayList<>();
    public RoundRobinNode(RoundRobinFormat roundRobin, BracketOverviewTabController boc) {
        this.roundRobin = roundRobin;
        this.boc = boc;
        update();
    }

    /** Updates all UI elements for the swiss stage. */
    private void update() {
        removeElements();

        ArrayList<RoundRobinGroup> groups = roundRobin.getGroups();
        int numberOfGroups = roundRobin.getNumberOfGroups();

        // Create that amount of columns matching the number of groups.
        for (int i = 0; i < numberOfGroups; i++) {
            VBox column = new VBox();
            column.setMinWidth(COLUMN_WIDTH);
            column.setPrefWidth(COLUMN_WIDTH);

            // Group Label
            if(numberOfGroups != 1){
                Label roundLabel = new Label("Group " + (i + 1));
                VBox.setMargin(roundLabel, MARGINS);
                column.getChildren().add(roundLabel);
            }

            // Get all matches from group and add them to the column
            for (Match match : groups.get(i).getMatches()) {

                MatchVisualController vmatch = boc.loadVisualMatch(match);
                VBox.setMargin(vmatch.getRoot(), MARGINS);
                column.getChildren().add(vmatch.getRoot());
                mvcs.add(vmatch);
            }

            getChildren().add(column);
        }
    }

    /** Creates a hbox containing a group with rounds of matches.
     * @param rrgroup a RoundRobinGroup that the box should contain.
     * @param groupName the name of the group.
     * @return a hbox containing a group with rounds of matches. */
    private HBox getGroupBox(RoundRobinGroup rrgroup, String groupName){

        HBox box = new HBox();

        box.getChildren().add(new Label(groupName));

        /*
        //Add rounds to this group
        for(int i = 0; i < rrgroup.getRound.size(); i++) {
            box.getChildren().add(getRoundBox(rrgroup.getSDASD(i)));
        }*/

        return box;
    }

    /** Returns a vbox that contains a round of matches.
     * @param matches the matches in the round.
     * @param roundName the name of the round.
     * @return a vbox that contains a round of matches. */
    private VBox getRoundBox(ArrayList<Match> matches, String roundName){

        VBox box = new VBox();

        box.getChildren().add(new Label(roundName));

        //Add matches
        for (Match match : matches) {
            MatchVisualController vmatch = boc.loadVisualMatch(match);
            VBox.setMargin(vmatch.getRoot(), MARGINS);
            box.getChildren().add(vmatch.getRoot());
            mvcs.add(vmatch);
        }

        return box;
    }

    @Override
    public void decoupleFromModel() {
        removeElements();
    }

    /** Completely remove all UI elements. */
    public void removeElements() {
        for (MatchVisualController mvc : mvcs) {
            mvc.decoupleFromModel();
        }
        getChildren().clear();
        mvcs.clear();
    }
}
