package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.stats.Stats;
import dk.aau.cs.ds306e18.tournament.model.stats.StatsChangeListener;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.ModelCoupledUI;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatsTable extends TableView<Stats> implements StatsChangeListener, ModelCoupledUI {

    private List<Team> teams;
    private Format format;

    public StatsTable(List<Team> teams, Format format) {
        super();
        this.teams = new ArrayList<>(teams);
        this.format = format;
        setFocusTraversable(false);
        setMouseTransparent(true);
        setPrefHeight(0); // Table heights are a nightmare. This creates an okay result.
        setPrefWidth(350);
        update();
        registerAsListener();
    }

    public void update() {
        // Clear everything inside the tableView
        getItems().clear();
        getColumns().clear();
        getSortOrder().clear();

        // Find stats objects to display
        List<Stats> stats = teams.stream()
                .map(t -> format == null ? t.getStatsManager().getGlobalStats() : t.getStatsManager().getStats(format))
                .collect(Collectors.toList());
        getItems().addAll(stats);

        // Team name column
        TableColumn<Stats, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTeam().getTeamName()));
        nameColumn.setMaxWidth(140);

        // Wins column
        TableColumn<Stats, Integer> winsColumn = new TableColumn<>("Wins");
        winsColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getWins()).asObject());

        // Wins column
        TableColumn<Stats, Integer> losesColumn = new TableColumn<>("Loses");
        losesColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getLoses()).asObject());

        // Goal difference column
        TableColumn<Stats, Integer> goalDifferenceColumn = new TableColumn<>("GDiff");
        goalDifferenceColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getGoalDifference()).asObject());

        // Goals column
        TableColumn<Stats, Integer> goalsColumn = new TableColumn<>("Goals");
        goalsColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getGoals()).asObject());

        // Column sorting
        winsColumn.setSortType(TableColumn.SortType.DESCENDING);
        losesColumn.setSortType(TableColumn.SortType.ASCENDING);
        goalDifferenceColumn.setSortType(TableColumn.SortType.DESCENDING);
        goalsColumn.setSortType(TableColumn.SortType.DESCENDING);

        // Add columns and sorting orders
        getColumns().add(nameColumn);
        getColumns().add(winsColumn);
        getColumns().add(losesColumn);
        getColumns().add(goalDifferenceColumn);
        getColumns().add(goalsColumn);
        getSortOrder().add(winsColumn);
        getSortOrder().add(losesColumn);
        getSortOrder().add(goalDifferenceColumn);
        getSortOrder().add(goalsColumn);

        // Additional styling
        winsColumn.setStyle("-fx-alignment: CENTER;");
        losesColumn.setStyle("-fx-alignment: CENTER;");
        goalDifferenceColumn.setStyle("-fx-alignment: CENTER;");
        goalsColumn.setStyle("-fx-alignment: CENTER;");
    }

    private void registerAsListener() {
        for (Team team : teams) {
            Stats stats = format == null ?
                    team.getStatsManager().getGlobalStats() :
                    team.getStatsManager().getStats(format);
            stats.registerStatsChangeListener(this);
        }
    }

    @Override
    public void statsChanged(Stats stats) {
        update();
    }

    @Override
    public void decoupleFromModel() {
        teams = null;
        format = null;
    }
}
