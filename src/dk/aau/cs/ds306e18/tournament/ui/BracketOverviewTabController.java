package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
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

public class BracketOverviewTabController {

    public static BracketOverviewTabController instance;

    @FXML private VBox startTournamentInstructionsHolder;
    @FXML private GridPane bracketOverviewTab;
    @FXML private VBox selectedMatchVBox;
    @FXML private VBox overviewVBox;
    @FXML private Button nextMatchBtn;
    @FXML private Button prevMatchBtn;
    @FXML private Button playMatchBtn;
    @FXML private Button editMatchBtn;
    @FXML private Label blueTeamNameLabel;
    @FXML private TextField blueTeamScore;
    @FXML private ListView<Bot> blueTeamListView;
    @FXML private Label orangeTeamNameLabel;
    @FXML private TextField orangeTeamScore;
    @FXML private ListView<Bot> orangeTeamListView;
    @FXML private ScrollPane overviewScrollPane;
    @FXML private GridPane selectedMatchInfo;
    @FXML private HBox selectedMatchButtonHolder;
    @FXML private HBox stageNavigationButtonsHolder;
    @FXML private Button nextStageBtn;
    @FXML private Button prevStageBtn;
    @FXML private Label startRequirementsLabel;
    @FXML private Button startTournamentBtn;

    private int showedStageIndex = -1;
    private CleanableUI cleanableBracket;
    private Format showedFormat;
    private MatchVisualController selectedMatch;

    @FXML
    private void initialize(){
        instance = this; // TODO Make references to other controllers work in MainController
    }

    /** Updates all elements depending on the state of the tournament and the shown stage. */
    public void update() {

        startTournamentBtn.setDisable(!Tournament.get().canStart());
        startRequirementsLabel.setText(getRequirementsText());

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
            showFormat(tournament.getCurrentStage().getFormat());
            updateStageNavigationButtons();
        }
    }

    /** @return a string that contains text describing the requirements for starting the tournament. */
    private String getRequirementsText(){

        //Are requirements met
        if(Tournament.get().canStart()) return "You have met the requirement for starting a tournament.";

        int numberOfStages = Tournament.get().getStages().size();
        int numberOfTeams = Tournament.get().getTeams().size();

        StringBuilder sb = new StringBuilder();
        sb.append("Before you start, you need to have ");

        if(numberOfStages < Tournament.get().STARTREQUIREMENT_STAGES) {
            sb.append("atleast ").append(getWordForNumber(Tournament.get().STARTREQUIREMENT_STAGES - numberOfStages)).append(" stage");
            if(numberOfTeams < Tournament.get().STARTREQUIREMENT_TEAMS) sb.append(" and ");
        }

        if(numberOfTeams < Tournament.get().STARTREQUIREMENT_TEAMS)
            sb.append(getWordForNumber(Tournament.get().STARTREQUIREMENT_TEAMS - numberOfTeams)).append((numberOfTeams == 1) ? " more team" : " more teams");

        return sb.append(".").toString();
    }

    /** @return the word matching the given number. Supports 1-10.*/
    private String getWordForNumber(int number){
        switch (number){
            case 1: return "one";
            case 2: return "two";
            case 3: return "three";
            case 4: return "four";
            case 5: return "five";
            case 6: return "six";
            case 7: return "seven";
            case 8: return "eight";
            case 9: return "nine";
            case 10: return "ten";
            default: return null;
        }
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

            FXMLLoader loader = new FXMLLoader(BracketOverviewTabController.class.getResource("layout/EditMatchScore.fxml"));
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
            // TODO Swap next/prev stage
        }
    }
}
