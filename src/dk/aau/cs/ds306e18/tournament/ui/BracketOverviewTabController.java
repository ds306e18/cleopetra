package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.format.StageStatusChangeListener;
import dk.aau.cs.ds306e18.tournament.rlbot.MatchRunner;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.format.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
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
import java.util.HashMap;

public class BracketOverviewTabController implements StageStatusChangeListener, MatchChangeListener {

    public static BracketOverviewTabController instance;

    @FXML private VBox startTournamentInstructionsHolder;
    @FXML private GridPane bracketOverviewTab;
    @FXML private VBox selectedMatchVBox;
    @FXML private VBox overviewVBox;
    @FXML private Button playMatchBtn;
    @FXML private Button modifyConfigBtn;
    @FXML private Button editMatchBtn;
    @FXML private Button switchColorsBtn;
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
    @FXML private Label stageNameLabel;
    @FXML private Label botNameLabel;
    @FXML private Label botDeveloperLabel;
    @FXML private Label botDescriptionLabel;
    @FXML private VBox botInfoBox;

    private int showedStageIndex = -1;
    private ModelCoupledUI coupledBracket;
    private Format showedFormat;
    private MatchVisualController selectedMatch;

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
        HBox root = null;
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
     * Deselects the match when a right click is registered within the scrollpane.
     */
    @FXML
    void deselectOnRightClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.SECONDARY)){
            setSelectedMatch(null);
        }
    }

    /**
     * Updates the team viewer on match clicked in overviewTab
     */
    private void updateTeamViewer(Match match) {
        boolean disable = (match == null);
        botInfoBox.setVisible(false);
        selectedMatchInfo.setDisable(disable);
        selectedMatchButtonHolder.setDisable(disable);

        if (match != null && match.getBlueTeam() != null) {
            // Blue team
            blueTeamNameLabel.setText(match.getBlueTeam().getTeamName());
            blueTeamScore.setText(Integer.toString(match.getTeamOneScore()));
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
            orangeTeamScore.setText(Integer.toString(match.getTeamTwoScore()));
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
        if (selectedMatch == null || selectedMatch.getShowedMatch().getStatus() == Match.Status.NOT_PLAYABLE) {
            editMatchBtn.setDisable(true);
            playMatchBtn.setDisable(true);
            modifyConfigBtn.setDisable(true);
            switchColorsBtn.setDisable(true);
        } else {
            editMatchBtn.setDisable(false);
            // If match can't be played an error popup is displayed explaining why
            playMatchBtn.setDisable(false);
            modifyConfigBtn.setDisable(false);
            // color switching is disabled when match has been played to avoid confusion when comparing replays and bracket
            switchColorsBtn.setDisable(selectedMatch.getShowedMatch().hasBeenPlayed());
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
    public void onStageStatusChanged(Format format, StageStatus oldStatus, StageStatus newStatus) {
        updateStageNavigationButtons();
    }

    @Override
    public void onMatchChanged(Match match) {
        updateTeamViewer(selectedMatch.getShowedMatch());
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
        MatchRunner.startMatch(Tournament.get().getRlBotSettings().getMatchConfig(), selectedMatch.getShowedMatch());
    }

    public void modifyConfigButtonOnAction(ActionEvent actionEvent) {
        boolean ready = MatchRunner.prepareMatch(Tournament.get().getRlBotSettings().getMatchConfig(), selectedMatch.getShowedMatch());
        if (ready) {
            Alerts.infoNotification("Modified config file", "The rlbot.cfg was successfully modified to the selected match.");
        }
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
        selectedMatch.getShowedMatch().setTeamOneToBlue(!selectedMatch.getShowedMatch().isTeamOneBlue());
    }
}
