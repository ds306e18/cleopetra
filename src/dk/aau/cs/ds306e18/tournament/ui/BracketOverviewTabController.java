package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.rlbot.MatchRunner;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
import dk.aau.cs.ds306e18.tournament.model.match.MatchStatus;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.ModelCoupledUI;
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

public class BracketOverviewTabController implements MatchChangeListener {

    public static BracketOverviewTabController instance;

    @FXML private VBox startTournamentInstructionsHolder;
    @FXML private GridPane bracketOverviewTab;
    @FXML private VBox selectedMatchVBox;
    @FXML private VBox overviewVBox;
    @FXML private Button playMatchBtn;
    @FXML private Button editMatchBtn;
    @FXML private Label blueTeamNameLabel;
    @FXML private Label blueTeamScore;
    @FXML private ListView<Bot> blueTeamListView;
    @FXML private Label orangeTeamNameLabel;
    @FXML private Label orangeTeamScore;
    @FXML private ListView<Bot> orangeTeamListView;
    @FXML private ScrollPane overviewScrollPane;
    @FXML private GridPane selectedMatchInfo;
    @FXML private HBox selectedMatchButtonHolder;
    @FXML private HBox stageNavigationButtonsHolder;
    @FXML private Button nextStageBtn;
    @FXML private Button prevStageBtn;
    @FXML private Label startRequirementsLabel;
    @FXML private Button startTournamentBtn;
    @FXML private VBox bracketLeaderboard;
    @FXML private TableView<Team> leaderboardTableview;
    @FXML private Label stageNameLabel;

    private int showedStageIndex = -1;
    private ModelCoupledUI coupledBracket;
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
            showStage(tournament.getStages().get(showedStageIndex));
            updateStageNavigationButtons();
        }

        updateTeamViewer(selectedMatch == null ? null : selectedMatch.getShowedMatch());
    }

    public void showLeaderboard(boolean state) {
        bracketLeaderboard.setVisible(state);
        bracketOverviewTab.getColumnConstraints().get(0).setMaxWidth(state ? 200 : 0);
    }

    /** @return a string that contains text describing the requirements for starting the tournament. */
    private String getRequirementsText(){

        //Are requirements met
        if(Tournament.get().canStart()) return "You have met the requirement for starting a tournament.";

        int numberOfStages = Tournament.get().getStages().size();
        int numberOfTeams = Tournament.get().getTeams().size();

        StringBuilder sb = new StringBuilder();
        sb.append("Before you start, you need to have ");

        if (numberOfStages < Tournament.START_REQUIREMENT_STAGES) {
            sb.append("atleast ").append(Tournament.START_REQUIREMENT_STAGES).append(Tournament.START_REQUIREMENT_STAGES > 1 ? " stages" : " stage");
            if(numberOfTeams < Tournament.START_REQUIREMENT_TEAMS) sb.append(" and ");
        }

        if (numberOfTeams < Tournament.START_REQUIREMENT_TEAMS)
            sb.append(Tournament.START_REQUIREMENT_TEAMS - numberOfTeams).append((numberOfTeams == 1) ? " more team" : " more teams");

        return sb.append(".").toString();
    }

    private void showStartTournamentInstructions(boolean show) {
        startTournamentBtn.setDisable(!Tournament.get().canStart());
        startRequirementsLabel.setText(getRequirementsText());
        startTournamentInstructionsHolder.setManaged(show);
        startTournamentInstructionsHolder.setVisible(show);
        overviewScrollPane.setManaged(!show);
        overviewScrollPane.setVisible(!show);
        stageNameLabel.setManaged(!show);
        stageNameLabel.setVisible(!show);
    }

    public void showStage(dk.aau.cs.ds306e18.tournament.model.Stage stage) {
        if (coupledBracket != null) {
            coupledBracket.decoupleFromModel();
        }

        showedFormat = stage.getFormat();

        stageNameLabel.setText(stage.getName());
        Node bracket = showedFormat.getBracketFXNode(this);
        overviewScrollPane.setContent(bracket);
        if (bracket instanceof ModelCoupledUI) {
            coupledBracket = (ModelCoupledUI) bracket;
        } else {
            coupledBracket = null;
            System.err.println("WARNING: " + bracket.getClass().toString() + " does not implement ModelCoupledUI.");
        }
    }

    /**
     * @param match the match to be visualised
     * @return a gridPane containing the visualisation of the given match.
     */
    public MatchVisualController loadVisualMatch(Match match) {

        //Load the fxml document into the Controller and JavaFx node.
        FXMLLoader loader = new FXMLLoader(BracketOverviewTabController.class.getResource("layout/MatchVisual.fxml"));
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
            selectedMatch.getShowedMatch().unregisterMatchChangeListener(this);
            selectedMatch.getRoot().getStyleClass().remove("selectedMatch");
        }
        this.selectedMatch = match;
        updateTeamViewer(match == null ? null : match.getShowedMatch());
        if(selectedMatch != null) { selectedMatch.getShowedMatch().registerMatchChangeListener(this); }
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

        updateMatchPlayAndEditButtons();
    }

    /** Disables/Enables the play and edit match buttons */
    public void updateMatchPlayAndEditButtons() {
        if (selectedMatch == null || selectedMatch.getShowedMatch().getStatus() == MatchStatus.NOT_PLAYABLE) {
            editMatchBtn.setDisable(true);
            playMatchBtn.setDisable(true);
        } else {
            editMatchBtn.setDisable(false);
            playMatchBtn.setDisable(false); // If match can't be played an error popup is displayed explaining why
        }
    }

    /**
     * Toggles edit for match scores
     */
    @FXML
    void editMatchBtnOnAction() {
        openEditMatchPopup();
    }

    /**
     * Creates a popup window allowing to change match score and state.
     */
    public void openEditMatchPopup(){
        try {
            Stage editMatchScoreStage = new Stage();
            editMatchScoreStage.initStyle(StageStyle.TRANSPARENT);
            editMatchScoreStage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(BracketOverviewTabController.class.getResource("layout/EditMatchScore.fxml"));
            AnchorPane editMatchStageRoot = loader.load();
            EditMatchScoreController emsc = loader.getController();
            emsc.setMatch(selectedMatch.getShowedMatch());
            editMatchScoreStage.setScene(new Scene(editMatchStageRoot));

            // Calculate the center position of the main window.
            Stage mainWindow = (Stage) bracketOverviewTab.getScene().getWindow();
            double centerXPosition = mainWindow.getX() + mainWindow.getWidth()/2d;
            double centerYPosition = mainWindow.getY() + mainWindow.getHeight()/2d;

            // Assign popup window to the center of the main window.
            editMatchScoreStage.setOnShown(ev -> {
                editMatchScoreStage.setX(centerXPosition - editMatchScoreStage.getWidth()/2d);
                editMatchScoreStage.setY(centerYPosition - editMatchScoreStage.getHeight()/2d);

                editMatchScoreStage.show();
            });

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

    private void updateStageNavigationButtons() {
        if (showedStageIndex == -1 || showedFormat == null) {
            nextStageBtn.setDisable(true);
            prevStageBtn.setDisable(true);
        } else {
            prevStageBtn.setDisable(showedStageIndex == 0);

            boolean concluded = Tournament.get().getStages().get(showedStageIndex).getFormat().getStatus() == StageStatus.CONCLUDED;
            boolean isLastStage = Tournament.get().getStages().size() - 1 == showedStageIndex;
            nextStageBtn.setDisable(!concluded || isLastStage);
        }
    }

    @Override
    public void onMatchChanged(Match match) {
        updateTeamViewer(selectedMatch.getShowedMatch());
        updateStageNavigationButtons();
    }

    public void prevStageBtnOnAction(ActionEvent actionEvent) {
        showedStageIndex--;
        showStage(Tournament.get().getStages().get(showedStageIndex));
        setSelectedMatch(null);
        updateStageNavigationButtons();
    }

    public void nextStageBtnOnAction(ActionEvent actionEvent) {
        int latestStageIndex = Tournament.get().getCurrentStageIndex();
        if (showedStageIndex == latestStageIndex) {
            Tournament.get().startNextStage();
        }

        showedStageIndex++;
        showStage(Tournament.get().getStages().get(showedStageIndex));

        setSelectedMatch(null);
        updateStageNavigationButtons();
    }

    public void onPlayMatchBtnAction(ActionEvent actionEvent) {
        MatchRunner.startMatch(Tournament.get().getRlBotSettings(), selectedMatch.getShowedMatch());
    }

    public void modifyConfigButtonOnAction(ActionEvent actionEvent) {
        boolean ready = MatchRunner.prepareMatch(Tournament.get().getRlBotSettings(), selectedMatch.getShowedMatch());
        if (ready) {
            Alerts.infoNotification("Modified config file", "The rlbot.cfg was successfully modified to the selected match.");
        }
    }
}
