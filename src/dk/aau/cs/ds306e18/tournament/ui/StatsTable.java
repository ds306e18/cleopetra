package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.stats.Stats;
import dk.aau.cs.ds306e18.tournament.model.stats.StatsChangeListener;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.ModelCoupledUI;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatsTable extends TableView<Stats> implements StatsChangeListener, ModelCoupledUI {

    private List<Team> teams;
    private Format format;

    /**
     * Create a StatsTable to show stats for the given teams in the given stage/format. If format is null global stats
     * will be shown instead.
     */
    public StatsTable(List<Team> teams, Format format) {
        super();
        this.teams = new ArrayList<>(teams);
        this.format = format;
        setFocusTraversable(false);
        setMouseTransparent(true);
        calculateSize();
        update();
        registerAsListener();
    }

    protected void calculateSize() {
        int items = teams.size();
        setPrefHeight(23.5 + 24.5 * items); // Table heights are a nightmare. This creates an okay result.
        setPrefWidth(306);
        setMinHeight(USE_PREF_SIZE);
        setMaxHeight(USE_PREF_SIZE);
    }

    /**
     * Clears the table for columns and cells.
     */
    public void clear() {
        getItems().clear();
        getColumns().clear();
        getSortOrder().clear();
    }

    /**
     * Adds the relevant Stats items to the table. If format is null global stats will be used.
     */
    protected void prepareItems() {
        List<Stats> stats = teams.stream()
                .map(t -> format == null ? t.getStatsManager().getGlobalStats() : t.getStatsManager().getStats(format))
                .collect(Collectors.toList());
        getItems().addAll(stats);
    }

    protected void addTeamNameColumn() {
        TableColumn<Stats, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTeam().getTeamName()));
        nameColumn.setMinWidth(110);
        nameColumn.setMaxWidth(110);
        getColumns().add(nameColumn);
    }

    protected void addWinsColumn() {
        TableColumn<Stats, Integer> winsColumn = new TableColumn<>("Wins");
        winsColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getWins()).asObject());
        winsColumn.setSortType(TableColumn.SortType.DESCENDING);
        winsColumn.setStyle("-fx-alignment: CENTER;");
        getColumns().add(winsColumn);
        getSortOrder().add(winsColumn);
    }

    protected void addLosesColumn() {
        TableColumn<Stats, Integer> losesColumn = new TableColumn<>("Loses");
        losesColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getLoses()).asObject());
        losesColumn.setSortType(TableColumn.SortType.ASCENDING);
        losesColumn.setStyle("-fx-alignment: CENTER;");
        getColumns().add(losesColumn);
        getSortOrder().add(losesColumn);
    }

    protected void addGoalDiffColumn() {
        TableColumn<Stats, Integer> goalDifferenceColumn = new TableColumn<>("GDiff");
        goalDifferenceColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getGoalDifference()).asObject());
        goalDifferenceColumn.setSortType(TableColumn.SortType.DESCENDING);
        goalDifferenceColumn.setStyle("-fx-alignment: CENTER;");
        getColumns().add(goalDifferenceColumn);
        getSortOrder().add(goalDifferenceColumn);
    }

    protected void addGoalsColumn() {
        TableColumn<Stats, Integer> goalsColumn = new TableColumn<>("Goals");
        goalsColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getGoals()).asObject());
        goalsColumn.setSortType(TableColumn.SortType.DESCENDING);
        goalsColumn.setStyle("-fx-alignment: CENTER;");
        getColumns().add(goalsColumn);
        getSortOrder().add(goalsColumn);
    }

    /**
     * Updates all values in the table.
     */
    public void update() {
        clear();
        prepareItems();
        addTeamNameColumn();
        addWinsColumn();
        addLosesColumn();
        addGoalDiffColumn();
        addGoalsColumn();
    }

    private void registerAsListener() {
        for (Team team : teams) {
            Stats stats = format == null ?
                    team.getStatsManager().getGlobalStats() :
                    team.getStatsManager().getStats(format);
            stats.registerStatsChangeListener(this);
        }
    }

    private void unregisterAsListener() {
        for (Team team : teams) {
            Stats stats = format == null ?
                    team.getStatsManager().getGlobalStats() :
                    team.getStatsManager().getStats(format);
            stats.unregisterStatsChangeListener(this);
        }
    }

    @Override
    public void statsChanged(Stats stats) {
        update();
    }

    @Override
    public void decoupleFromModel() {
        unregisterAsListener();
        teams = null;
        format = null;
    }
}
