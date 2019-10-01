package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.stats.Stats;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TableColumn;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * StatsTableWithPoints differs from StatsTable in that it also has a column with points. These points can be awarded
 * due to the special rules in a particular format. The loses and goals column is not shown in the
 * StatsTableWithPoints as they are typically not important when points are used.
 */
public class StatsTableWithPoints extends StatsTable {

    private IdentityHashMap<Team, Integer> pointsMap;

    /**
     * Create a StatsTable to show points and stats for the given teams in the given stage/format. If format is null global stats
     * will be shown instead.
     */
    public StatsTableWithPoints(List<Team> teams, Format format, IdentityHashMap<Team, Integer> pointsMap) {
        super(teams, format);
        this.pointsMap = pointsMap;
    }

    @Override
    protected void calculateSize() {
        super.calculateSize();
        setPrefWidth(258);
    }

    @Override
    public void update() {
        clear();
        prepareItems();
        addTeamNameColumn();
        addPointsColumn();
        addWinsColumn();
        addGoalDiffColumn();
    }

    private void addPointsColumn() {
        TableColumn<Stats, Integer> pointsColumn = new TableColumn<>("Points");
        pointsColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(pointsMap == null ? 0 : pointsMap.get(cell.getValue().getTeam())).asObject());
        pointsColumn.setSortType(TableColumn.SortType.DESCENDING);
        pointsColumn.setStyle("-fx-alignment: CENTER;");
        getColumns().add(pointsColumn);
        getSortOrder().add(pointsColumn);
    }
}
