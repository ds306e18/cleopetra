package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.format.StageStatusChangeListener;
import dk.aau.cs.ds306e18.tournament.model.match.MatchResultDependencyException;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.rlbot.MatchControl;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.format.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.ModelCoupledUI;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class BracketOverviewTabController implements StageStatusChangeListener, MatchChangeListener {

    public static BracketOverviewTabController instance;

    @FXML private VBox startTournamentInstructionsHolder;
    @FXML private GridPane bracketOverviewTab;
    @FXML private VBox selectedMatchVBox;
    @FXML private VBox overviewVBox;
    @FXML private Button playMatchBtn;
    //@FXML private Button modifyConfigBtn;
    @FXML private Button fetchScoresBtn;
    @FXML private Button editMatchBtn;
    @FXML private Button switchColorsBtn;
    @FXML private Button extendSeriesBtn;
    @FXML private Button shortenSeriesBtn;
    @FXML private Label blueTeamNameLabel;
    @FXML private ListView<Bot> blueTeamListView;
    @FXML private Label orangeTeamNameLabel;
    @FXML private ListView<Bot> orangeTeamListView;
    @FXML private ScrollPane overviewScrollPane;
    @FXML private GridPane selectedMatchInfo;
    @FXML private HBox stageNavigationButtonsHolder;
    @FXML private Button nextStageBtn;
    @FXML private Button prevStageBtn;
    @FXML private Label startRequirementsLabel;
    @FXML private Button startTournamentBtn;
    @FXML private Label stageNameLabel;
    @FXML private Label botNameLabel;
    @FXML private Label botDeveloperLabel;
    @FXML private Label botDescriptionLabel;
    @FXML private VBox botInfoBox;

    private int showedStageIndex = -1;
    private ModelCoupledUI coupledBracket;
    private Format showedFormat;
    private SeriesVisualController selectedSeries;

    @FXML
    private void initialize() {
        instance = this;

        // Listeners for the listviews. Handles the clear of selection of the other listview and updates the
        // bot info box according to the selection.
        blueTeamListView.getSelectionModel().selectedItemProperty().addListener(observable -> {
            if (blueTeamListView.getItems() != null){
                if (!(blueTeamListView.getItems().isEmpty())){
                    if (getSelectedBot(blueTeamListView) != null){
                        botInfoBox.setVisible(true);
                        updateBotInfo(getSelectedBot(blueTeamListView));
                        clearSelectionOfTableview(orangeTeamListView);
                    }
                }
            }
        });

        orangeTeamListView.getSelectionModel().selectedItemProperty().addListener(observable -> {
            if (orangeTeamListView.getItems() != null){
                if (!(orangeTeamListView.getItems().isEmpty())){
                    if (getSelectedBot(orangeTeamListView) != null){
                        botInfoBox.setVisible(true);
                        updateBotInfo(getSelectedBot(orangeTeamListView));
                        clearSelectionOfTableview(blueTeamListView);
                    }
                }
            }
        });
    }

    /**
     * Clears the selection of a given ListView.
     */
    private void clearSelectionOfTableview(ListView<Bot> listView){
        if (getSelectedBot(listView) != null) {
            listView.getSelectionModel().clearSelection();
        }
    }

    /**
     * Updates the labels on boxInfo according to a given bot.
     * @param selectedBot The given bot wished to receive details from.
     */
    private void updateBotInfo(Bot selectedBot){
        botNameLabel.setText(selectedBot.getName());
        botDeveloperLabel.setText(selectedBot.getDeveloper());
        botDescriptionLabel.setText(selectedBot.getDescription());
    }

    /**
     * Updates all elements depending on the state of the tournament and the shown stage.
     */
    public void update() {
        Tournament tournament = Tournament.get();

        if (!tournament.hasStarted()) {
            showedStageIndex = -1;
            showedFormat = null;
            showStartTournamentInstructions(true);
            setSelectedSeries(null);
            updateStageNavigationButtons();
        } else {
            if (showedStageIndex == -1) showedStageIndex = 0;
            showStartTournamentInstructions(false);
            showStage(tournament.getStages().get(showedStageIndex));
            updateStageNavigationButtons();
        }

        updateTeamViewer(selectedSeries == null ? null : selectedSeries.getShowedSeries());
    }

    /** @return a string that contains text describing the requirements for starting the tournament. */
    private String getRequirementsText(){

        //Are requirements met
        if(Tournament.get().canStart()) return "You have met the requirements for starting a tournament.";

        int numberOfStages = Tournament.get().getStages().size();
        int numberOfTeams = Tournament.get().getTeams().size();

        StringBuilder sb = new StringBuilder();
        sb.append("Before you start, you need to have ");

        if (numberOfStages < Tournament.START_REQUIREMENT_STAGES) {
            sb.append("at least ").append(Tournament.START_REQUIREMENT_STAGES).append(Tournament.START_REQUIREMENT_STAGES > 1 ? " stages" : " stage");
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
            showedFormat.unregisterStatusChangedListener(this);
        }

        showedFormat = stage.getFormat();
        showedFormat.registerStatusChangedListener(this);

        stageNameLabel.setText(stage.getName());
        Node bracket = showedFormat.getBracketFXNode(this);
        overviewScrollPane.setContent(bracket);
        if (bracket instanceof ModelCoupledUI) {
            coupledBracket = (ModelCoupledUI) bracket;
        } else {
            coupledBracket = null;
            Main.LOGGER.log(System.Logger.Level.ERROR, "PANIC! " + bracket.getClass() + " does not implement ModelCoupledUI.");
        }
    }

    /**
     * @param series the match to be visualised
     * @return a gridPane containing the visualisation of the given match.
     */
    public SeriesVisualController loadSeriesVisual(Series series) {

        //Load the fxml document into the Controller and JavaFx node.
        FXMLLoader loader = new FXMLLoader(BracketOverviewTabController.class.getResource("layout/SeriesVisual.fxml"));
        HBox root = null;
        SeriesVisualController mvc = null;

        try {
            root = loader.load();
            mvc = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mvc.setBoc(this);
        mvc.setShowedSeries(series);

        return mvc;
    }

    /**
     * Sets the selected match.
     */
    public void setSelectedSeries(SeriesVisualController match) {
        if (selectedSeries != null){
            selectedSeries.getShowedSeries().unregisterMatchChangeListener(this);
            selectedSeries.getRoot().getStyleClass().remove("selectedMatch");
        }
        this.selectedSeries = match;
        updateTeamViewer(match == null ? null : match.getShowedSeries());
        if(selectedSeries != null) { selectedSeries.getShowedSeries().registerMatchChangeListener(this); }
    }

    /**
     * Deselects the match when a right click is registered within the scrollpane.
     */
    @FXML
    void deselectOnRightClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.SECONDARY)){
            setSelectedSeries(null);
        }
    }

    /**
     * Updates the team viewer on match clicked in overviewTab
     */
    private void updateTeamViewer(Series series) {
        boolean disable = (series == null);
        botInfoBox.setVisible(false);
        selectedMatchInfo.setDisable(disable);

        if (series != null && series.getBlueTeam() != null) {
            // Blue team
            blueTeamNameLabel.setText(series.getBlueTeam().getTeamName());
            blueTeamListView.setItems(FXCollections.observableArrayList(series.getBlueTeam().getBots()));
            blueTeamListView.refresh();
        } else {
            // Orange team is unknown
            blueTeamNameLabel.setText("Blue team");
            blueTeamListView.setItems(null);
            blueTeamListView.refresh();
        }
        if (series != null && series.getOrangeTeam() != null) {
            // Orange team
            orangeTeamNameLabel.setText(series.getOrangeTeam().getTeamName());
            orangeTeamListView.setItems(FXCollections.observableArrayList(series.getOrangeTeam().getBots()));
            orangeTeamListView.refresh();
        } else {
            // Orange team is unknown
            orangeTeamNameLabel.setText("Orange team");
            orangeTeamListView.setItems(null);
            orangeTeamListView.refresh();
        }

        updateMatchPlayAndEditButtons();
    }

    /** Disables/Enables the play and edit match buttons */
    public void updateMatchPlayAndEditButtons() {
        if (selectedSeries == null || selectedSeries.getShowedSeries().getStatus() == Series.Status.NOT_PLAYABLE) {
            editMatchBtn.setDisable(true);
            playMatchBtn.setDisable(true);
            //modifyConfigBtn.setDisable(true);
            fetchScoresBtn.setDisable(true);
            switchColorsBtn.setDisable(true);
        } else {
            editMatchBtn.setDisable(false);
            // If match can't be played an error popup is displayed explaining why
            playMatchBtn.setDisable(false);
            //modifyConfigBtn.setDisable(false);
            // Only allow fetching of scores if the series is not over
            fetchScoresBtn.setDisable(selectedSeries.getShowedSeries().hasBeenPlayed());
            // color switching is disabled when match has been played to avoid confusion when comparing replays and bracket
            switchColorsBtn.setDisable(selectedSeries.getShowedSeries().hasBeenPlayed());
        }

        if (selectedSeries == null) {
            extendSeriesBtn.setDisable(true);
            shortenSeriesBtn.setDisable(true);
        } else {
            extendSeriesBtn.setDisable(false);
            shortenSeriesBtn.setDisable(selectedSeries.getShowedSeries().getSeriesLength() == 1);
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

            FXMLLoader loader = new FXMLLoader(BracketOverviewTabController.class.getResource("layout/EditSeriesScore.fxml"));
            AnchorPane editMatchStageRoot = loader.load();
            EditSeriesScoreController emsc = loader.getController();
            emsc.setSeries(selectedSeries.getShowedSeries());
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
            Main.LOGGER.log(System.Logger.Level.INFO, "Tournament started.");
            update();
        } else {
            Alerts.errorNotification("Failed to start tournament", "You must have at least two teams and one stage to start a tournament.");
            Main.LOGGER.log(System.Logger.Level.ERROR, "Failed to start tournament due to requirements not being met.");
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
    public void onStageStatusChanged(Format format, StageStatus oldStatus, StageStatus newStatus) {
        updateStageNavigationButtons();
    }

    @Override
    public void onMatchChanged(Series series) {
        updateTeamViewer(selectedSeries.getShowedSeries());
    }

    public void prevStageBtnOnAction(ActionEvent actionEvent) {
        showedStageIndex--;
        showStage(Tournament.get().getStages().get(showedStageIndex));
        setSelectedSeries(null);
        updateStageNavigationButtons();
    }

    public void nextStageBtnOnAction(ActionEvent actionEvent) {
        int latestStageIndex = Tournament.get().getCurrentStageIndex();
        if (showedStageIndex == latestStageIndex) {
            Tournament.get().startNextStage();
        }

        showedStageIndex++;
        showStage(Tournament.get().getStages().get(showedStageIndex));

        setSelectedSeries(null);
        updateStageNavigationButtons();
    }

    public void onPlayMatchBtnAction(ActionEvent actionEvent) {
        MatchControl.get().startMatch(Tournament.get().getRlBotSettings().getMatchConfig(), selectedSeries.getShowedSeries());
    }

    public void fetchScoresButtonOnAction(ActionEvent actionEvent) {
        var match = MatchControl.get();
        if (selectedSeries != null && match.hasLatestScore()) {
            Series series = selectedSeries.getShowedSeries();
            int blueScore = match.getLatestBlueScore();
            int orangeScore = match.getLatestOrangeScore();
            Main.LOGGER.log(System.Logger.Level.INFO, "Fetched scores: " + blueScore + "-" + orangeScore);
            try {
                // Insert scores if possible
                if (series.isTeamOneBlue()) {
                    series.setScoresOfUnplayedMatch(blueScore, orangeScore);
                } else {
                    series.setScoresOfUnplayedMatch(orangeScore, blueScore);
                }

                // Is series over now?
                Series.Outcome outcome = Series.winnerIfScores(series.getTeamOneScores(), series.getTeamTwoScores());
                if (outcome != Series.Outcome.DRAW && outcome != Series.Outcome.UNKNOWN) {
                    series.setScores(series.getTeamOneScores(), series.getTeamTwoScores(), true);
                }
            } catch (IllegalStateException ex) {
                Alerts.errorNotification("No missing results", "The selected series does not contain any matches without scores.");
                Main.LOGGER.log(System.Logger.Level.INFO, "The selected series does not contain any matches without scores.");
            }
        } else {
            Alerts.errorNotification("Fetching score failed", "No scores have been received from RLBot yet.");
            Main.LOGGER.log(System.Logger.Level.ERROR, "Failed to fetch scores.");
        }
    }

    public void modifyConfigButtonOnAction(ActionEvent actionEvent) {
        // TODO: Remove
    }

    /**
     * Method to return the selected bot in a given Listview
     * @param listView the listview to be checked for selection
     * @return the bot that is selected.
     */
    private Bot getSelectedBot(ListView<Bot> listView) {
        return listView.getSelectionModel().getSelectedItem();
    }

    public void onSwitchColorsBtnAction(ActionEvent actionEvent) {
        selectedSeries.getShowedSeries().setTeamOneToBlue(!selectedSeries.getShowedSeries().isTeamOneBlue());
    }

    public void extendSeriesBtnOnAction(ActionEvent actionEvent) {
        carefullyChangeSeriesLength(selectedSeries.getShowedSeries().getSeriesLength() + 2);
    }

    public void shortenSeriesBtnOnAction(ActionEvent actionEvent) {
        // We assume the button is only clickable if the match length is greater than 1
        carefullyChangeSeriesLength(selectedSeries.getShowedSeries().getSeriesLength() - 2);
    }

    /**
     * Changes the selected series length. In case this change the outcome of the series (and other series
     * depends on this outcome) an alert prompt is shown, and the user must confirm if they want to proceed.
     */
    public void carefullyChangeSeriesLength(int length) {
        Series series = selectedSeries.getShowedSeries();
        boolean force = false;
        boolean cancelled = false;
        while (!cancelled) {
            try {
                series.setSeriesLength(length, force);
                break;
            } catch (MatchResultDependencyException e) {
                // An MatchResultDependencyException is thrown if the outcome has changed and subsequent matches depends on this outcome
                // Ask if the user wants to proceed
                force = Alerts.confirmAlert("The outcome of this match has changed", "This change will reset the subsequent matches. Do you want to proceed?");
                cancelled = !force;
            }
        }
    }
}
