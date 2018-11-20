package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;
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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private Button nextStageBtn;
    @FXML
    private Button prevStageBtn;
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


    private MatchVisualController selectedMatch;

    @FXML
    private void initialize(){
        instance = this; // TODO Make references to other controllers work in MainController
    }

    /** Updates all elements depending on the state of the tournament and the shown stage. */
    public void update() {
        Tournament tournament = Tournament.get();
        if (!tournament.hasStarted()) {
            showStartTournamentInstructions(true);
            setSelectedMatch(null);
            stageNavigationButtonsHolder.setDisable(true);

            bracketLeaderboard.setVisible(false);
            bracketOverviewTab.getColumnConstraints().get(0).setMaxWidth(0);
        } else {
            showStartTournamentInstructions(false);
            Format format = tournament.getCurrentStage().getFormat();
            showFormat(format);

            if (format instanceof SwissFormat){
                bracketLeaderboard.setVisible(true);
                bracketOverviewTab.getColumnConstraints().get(0).setMaxWidth(200);

                refreshLeaderboard();
                ((SwissFormat) format).registerMatchChangedListener(this);
            }
        }
    }

    public void refreshLeaderboard(){
        leaderboardTableview.getItems().clear();
        leaderboardTableview.getColumns().clear();

        Tournament tournament = Tournament.get();
        int totalNumberTeams = tournament.getTeams().size();
        Format format = tournament.getCurrentStage().getFormat();

        HashMap<Team, Integer> pointsHashmap = ((SwissFormat) format).getTeamPointsMap();
        List<Team> sortedTeams = format.getTopTeams(totalNumberTeams, new TieBreakerBySeed());

        leaderboardTableview.getItems().addAll(sortedTeams);

        TableColumn<Team, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getTeamName());
        });

        TableColumn<Team, Integer> pointColumn = new TableColumn<>("Score");
        pointColumn.setCellValueFactory(cellData -> {
            int value = pointsHashmap.get(cellData.getValue());
            ObservableValue<Integer> obsInt = new SimpleIntegerProperty(value).asObject();
            return obsInt;
        });

        leaderboardTableview.getColumns().addAll(nameColumn, pointColumn);
    }

    private void showStartTournamentInstructions(boolean show) {
        startTournamentInstructionsHolder.setManaged(show);
        startTournamentInstructionsHolder.setVisible(show);
        overviewScrollPane.setManaged(!show);
        overviewScrollPane.setVisible(!show);
    }

    public void showFormat(Format format) {
        overviewScrollPane.setContent(format.getBracketFXNode(this));
        if (Tournament.get().getCurrentStage().getFormat() instanceof SwissFormat) {
            ((SwissFormat) Tournament.get().getCurrentStage().getFormat()).unregisterMatchChangedListener(this);
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
        if (selectedMatch != null){
            selectedMatch.getRoot().getStyleClass().remove("selectedMatch");
        }
        this.selectedMatch = match;
        updateTeamViewer(match == null ? null : match.getShowedMatch());
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
        refreshLeaderboard();
    }
}
