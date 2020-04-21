package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.RoundRobinFormat;
import dk.aau.cs.ds306e18.tournament.model.format.RoundRobinGroup;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.ui.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.SeriesVisualController;
import dk.aau.cs.ds306e18.tournament.ui.StatsTable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class RoundRobinNode extends VBox implements ModelCoupledUI {

    private final Insets MARGINS = new Insets(0, 8, 8, 0);
    private final Insets ROUND_PADDING = new Insets(0,5,28,0);
    private final Insets LABEL_PADDING = new Insets(0,16,0,0);
    private final Insets TABLE_MARGIN = new Insets(0,0,0,64);

    private final RoundRobinFormat roundRobin;
    private final BracketOverviewTabController boc;

    private ArrayList<SeriesVisualController> mvcs = new ArrayList<>();
    private ArrayList<StatsTable> statsTables = new ArrayList<>();

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
        box.setPadding(ROUND_PADDING);

        //Set up label and its box
        Label groupLabel = new Label("G" + (groupNumber + 1));
        groupLabel.setFont(new Font("Calibri", 28));
        groupLabel.setTextFill(Color.valueOf("#C1C1C1"));
        HBox labelBox = new HBox();
        labelBox.setAlignment(Pos.CENTER);
        labelBox.setPadding(LABEL_PADDING);
        labelBox.getChildren().add(groupLabel);
        box.getChildren().add(labelBox);

        // Add rounds to this group. Each round is a contained in a VBox
        for(int i = 0; i < rrgroup.getRounds().size(); i++)
            box.getChildren().add(getRoundBox(rrgroup.getRounds().get(i), i));

        // Leaderboard for the group
        StatsTable table = new StatsTable(rrgroup.getTeams(), roundRobin);
        box.getChildren().add(table);
        HBox.setMargin(table, TABLE_MARGIN);
        statsTables.add(table);

        return box;
    }

    /** Returns a vbox that contains a round of matches.
     * @param series the matches in the round.
     * @param roundNumber the number of the round.
     * @return a vbox that contains a round of matches. */
    private VBox getRoundBox(ArrayList<Series> series, int roundNumber){

        VBox box = new VBox();
        box.getChildren().add(new Label("Round " + (roundNumber + 1)));

        //Add matches
        for (Series serie : series) {
            SeriesVisualController vmatch = boc.loadSeriesVisual(serie);
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
        for (SeriesVisualController mvc : mvcs) {
            mvc.decoupleFromModel();
        }
        for (StatsTable table : statsTables) {
            table.decoupleFromModel();
        }
        getChildren().clear();
        mvcs.clear();
        statsTables.clear();
    }
}
