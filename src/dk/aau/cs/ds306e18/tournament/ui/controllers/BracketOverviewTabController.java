package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.format.SwissFormat;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
import dk.aau.cs.ds306e18.tournament.model.match.MatchPlayedListener;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreakerBySeed;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.CleanableUI;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BracketOverviewTabController implements MatchChangeListener {

    public static BracketOverviewTabController instance;

    @FXML
    private VBox startTournamentInstructionsHolder;
    @FXML
    private GridPane bracketOverviewTab;
    @FXML
    private VBox selectedMatchVBox;
    @FXML
    private VBox overviewVBox;
    @FXML
    private Button nextMatchBtn;
    @FXML
    private Button prevMatchBtn;
    @FXML
    private Button playMatchBtn;
    @FXML
    private Button editMatchBtn;
    @FXML
    private Label blueTeamNameLabel;
    @FXML
    private TextField blueTeamScore;
    @FXML
    private ListView<Bot> blueTeamListView;
    @FXML
    private Label orangeTeamNameLabel;
    @FXML
    private TextField orangeTeamScore;
    @FXML
    private ListView<Bot> orangeTeamListView;
    @FXML
    private ScrollPane overviewScrollPane;
    @FXML
    private GridPane selectedMatchInfo;
    @FXML
    private HBox selectedMatchButtonHolder;
    @FXML
    private HBox stageNavigationButtonsHolder;
    @FXML
    private VBox bracketLeaderboard;
    @FXML
    private TableView<Team> leaderboardTableview;
    @FXML
    private Button nextStageBtn;
    @FXML
    private Button prevStageBtn;

    private int showedStageIndex = -1;
    private CleanableUI cleanableBracket;
    private Format showedFormat;
    private MatchVisualController selectedMatch;

    @FXML
    private void initialize() {
        instance = this; // TODO Make references to other controllers work in MainController
    }

    /**
     * Updates all elements depending on the state of the tournament and the shown stage.
     */
    public void update() {
        Tournament tournament = Tournament.get();
        showLeaderboard(false);

        if (!tournament.hasStarted()) {
            showedStageIndex = -1;
            showedFormat = null;
            showStartTournamentInstructions(true);
            setSelectedMatch(null);
            updateStageNavigationButtons();
        } else {
            if (showedStageIndex == -1) showedStageIndex = 0;
            showStartTournamentInstructions(false);
            showFormat(tournament.getCurrentStage().getFormat());
            updateStageNavigationButtons();
        }
    }

    public void showLeaderboard(boolean state) {
        bracketLeaderboard.setVisible(state);
        bracketOverviewTab.getColumnConstraints().get(0).setMaxWidth(state ? 200 : 0);
    }

    private void showStartTournamentInstructions(boolean show) {
        startTournamentInstructionsHolder.setManaged(show);
        startTournamentInstructionsHolder.setVisible(show);
        overviewScrollPane.setManaged(!show);
        overviewScrollPane.setVisible(!show);
    }

    public void showFormat(Format format) {
        if (cleanableBracket != null) {
            cleanableBracket.clean();
        }
        showedFormat = format;
        if (format != null) {
            Node bracket = format.getBracketFXNode(this);
            overviewScrollPane.setContent(bracket);
            cleanableBracket = (CleanableUI) bracket;
        }
    }

    /**
     * @param match the match to be visualised
     * @return a gridPane containing the visualisation of the given match.
     */
    public MatchVisualController loadVisualMatch(Match match) {

        //Load the fxml document into the Controller and JavaFx node.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../layout/MatchVisual.fxml"));
        VBox root = null;
        MatchVisualController mvc = null;

        try {
            root = loader.load();
            mvc = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mvc.setBoc(this);
        mvc.setShowedMatch(match);

        return mvc;
    }

    /**
     * Sets the selected match.
     */
    public void setSelectedMatch(MatchVisualController match) {
        if (selectedMatch != null) {
            selectedMatch.getRoot().getStyleClass().remove("selectedMatch");
        }
        this.selectedMatch = match;
        updateTeamViewer(match == null ? null : match.getShowedMatch());
    }

    /**
     * Refreshes the leaderboard by clearing its content and then creating columns with data retrieved
     * from the HashMap provided. The list is sorted based upon the team points in a descending order.
     *
     * @param pointMap The HashMap containing the teams and their points.
     */
    public void refreshLeaderboard(HashMap<Team, Integer> pointMap) {
        // Clear everything inside the tableView
        leaderboardTableview.getItems().clear();
        leaderboardTableview.getColumns().clear();

        // Assign teams to the listView from the HashMap provided.
        leaderboardTableview.getItems().addAll(pointMap.keySet());

        // Create columns and assign values based on the provided HashMap.
        TableColumn<Team, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeamName()));

        TableColumn<Team, Integer> pointColumn = new TableColumn<>("Points");
        pointColumn.setCellValueFactory(cellData -> {
            int points = pointMap.get(cellData.getValue());
            return new SimpleIntegerProperty(points).asObject();
        });

        // Styling - Descending order and centering text.
        pointColumn.setSortType(TableColumn.SortType.DESCENDING);
        pointColumn.setStyle("-fx-alignment: CENTER;");

        // Add sorting order and columns to the tableview.
        leaderboardTableview.getColumns().add(nameColumn);
        leaderboardTableview.getColumns().add(pointColumn);
        leaderboardTableview.getSortOrder().add(pointColumn);
    }

    /**
     * Updates the team viewer on match clicked in overviewTab
     */
    private void updateTeamViewer(Match match) {
        boolean disable = (match == null);
        selectedMatchInfo.setDisable(disable);
        selectedMatchButtonHolder.setDisable(disable);

        if (match != null && match.getBlueTeam() != null) {
            // Blue team
            blueTeamNameLabel.setText(match.getBlueTeam().getTeamName());
            blueTeamScore.setText(Integer.toString(match.getBlueScore()));
            blueTeamListView.setItems(FXCollections.observableArrayList(match.getBlueTeam().getBots()));
            blueTeamListView.refresh();
        } else {
            // Orange team is unknown
            blueTeamNameLabel.setText("Blue team");
            blueTeamScore.setText("");
            blueTeamListView.setItems(null);
            blueTeamListView.refresh();
        }
        if (match != null && match.getOrangeTeam() != null) {
            // Orange team
            orangeTeamNameLabel.setText(match.getOrangeTeam().getTeamName());
            orangeTeamScore.setText(Integer.toString(match.getOrangeScore()));
            orangeTeamListView.setItems(FXCollections.observableArrayList(match.getOrangeTeam().getBots()));
            orangeTeamListView.refresh();
        } else {
            // Orange team is unknown
            orangeTeamNameLabel.setText("Orange team");
            orangeTeamScore.setText("");
            orangeTeamListView.setItems(null);
            orangeTeamListView.refresh();
        }
    }

    /**
     * Toggles edit for match scores
     */
    @FXML
    void editMatchBtnOnAction(ActionEvent event) {
        try {
            Stage editMatchScoreStage = new Stage();
            editMatchScoreStage.initStyle(StageStyle.TRANSPARENT);
            editMatchScoreStage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../layout/EditMatchScore.fxml"));
            AnchorPane editMatchStageRoot = loader.load();
            EditMatchScoreController emsc = loader.getController();
            emsc.setMatch(selectedMatch.getShowedMatch());
            editMatchScoreStage.setScene(new Scene(editMatchStageRoot));
            editMatchScoreStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onStartTournamentButtonPressed(ActionEvent actionEvent) {
        if (Tournament.get().canStart()) {
            Tournament.get().start();
            update();
        } else {
            // TODO Show error message to user
            System.out.println("Can't start tournament.");
        }
    }

    @Override
    public void onMatchChanged(Match match) {

    }

    private void updateStageNavigationButtons() {
        System.out.println("Oi");
        if (showedStageIndex == -1 || showedFormat == null) {
            nextStageBtn.setDisable(true);
            prevStageBtn.setDisable(true);
        } else {
            int stageCount = Tournament.get().getStages().size();
            prevStageBtn.setDisable(showedStageIndex == 0);

            boolean concluded = Tournament.get().getStages().get(showedStageIndex).getFormat().getStatus() == StageStatus.CONCLUDED;
            nextStageBtn.setDisable(!concluded); // TODO Should also update onMatchPlayed
            // TODO Swap next/prev stage button
        }
    }
}
