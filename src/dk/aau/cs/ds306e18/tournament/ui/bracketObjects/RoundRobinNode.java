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

public class RoundRobinNode extends VBox implements ModelCoupledUI {

    private final Insets MARGINS = new Insets(0, 0, 8, 0);
    private final Insets ROUND_PADDING = new Insets(10,0,10,0);
    private final Insets LABEL_PADDING = new Insets(0,10,0,0);
    private final int COLUMN_WIDTH = 500;

    private final RoundRobinFormat roundRobin;
    private final BracketOverviewTabController boc;

    private ArrayList<MatchVisualController> mvcs = new ArrayList<>();

    public RoundRobinNode(RoundRobinFormat roundRobin, BracketOverviewTabController boc) {
        this.roundRobin = roundRobin;
        this.boc = boc;
        update();
    }

    /** Updates all UI elements for the round robin stage. */
    private void update() {
        removeElements();

        ArrayList<RoundRobinGroup> groups = roundRobin.getGroups();

        for(int i = 0; i < groups.size(); i++)
            getChildren().add(getGroupBox(groups.get(i), i));
    }

    /** Creates a hbox containing a group with rounds of matches.
     * @param rrgroup the RoundRobinGroup that the box should contain.
     * @param groupNumber the number of the group.
     * @return a hbox containing a group with rounds of matches. */
    private HBox getGroupBox(RoundRobinGroup rrgroup, int groupNumber){

        HBox box = new HBox();
        box.setMinWidth(COLUMN_WIDTH);
        box.setPadding(ROUND_PADDING);

        //Set up label and its box
        Label groupLabel = new Label("Group " + (groupNumber + 1));
        HBox labelBox = new HBox();
        labelBox.setPadding(LABEL_PADDING);
        labelBox.getChildren().add(groupLabel);
        box.getChildren().add(labelBox);

        //Add rounds to this group
        for(int i = 0; i < rrgroup.getRounds().size(); i++)
            box.getChildren().add(getRoundBox(rrgroup.getRounds().get(i), i));

        return box;
    }

    /** Returns a vbox that contains a round of matches.
     * @param matches the matches in the round.
     * @param roundNumber the number of the round.
     * @return a vbox that contains a round of matches. */
    private VBox getRoundBox(ArrayList<Match> matches, int roundNumber){

        VBox box = new VBox();
        box.getChildren().add(new Label("Round " + (roundNumber + 1)));

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
