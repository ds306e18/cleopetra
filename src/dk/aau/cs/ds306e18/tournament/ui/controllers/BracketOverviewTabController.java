package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.*;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class BracketOverviewTabController {

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

    private MatchVisualController selectedMatch;

    @FXML
    private void initialize() {

        overviewScrollPane.setManaged(false);
        overviewScrollPane.setVisible(false);

        orangeTeamScore.textProperty().addListener((observable, oldValue, newValue) -> {
            validateScoreInput(oldValue, newValue, orangeTeamScore);
        });
        blueTeamScore.textProperty().addListener((observable, oldValue, newValue) -> {
            validateScoreInput(oldValue, newValue, blueTeamScore);
        });
    }

    /**
     * Updates the content of this element. Displays the javaFxNode from the given format.
     */
    private void updateView(Format format) {
/*        overviewVBox.getChildren().clear();
        overviewVBox.getChildren().add(format.getJavaFxNode(this));*/
        overviewScrollPane.setContent(format.getJavaFxNode(this));
        //VBox.setVgrow(overviewVBox.getChildren().get(0), Priority.ALWAYS); //TODO this shuold be handled in fxml for this
    }

    /**
     * @param match the match to be visualised
     * @return a gridPane containing the visualisation of the given match.
     */
    public VBox loadVisualMatch(Match match) {

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

        return root;
    }

    /**
     * Sets the selected match.
     */
    public void setSelectedMatch(MatchVisualController match) {
        this.selectedMatch = match;
        updateTeamViewer(match.getShowedMatch());
    }

    /**
     * Toggles edit for match scores
     */
    @FXML
    void editMatchBtnOnAction(ActionEvent event) {
        if (blueTeamScore.editableProperty().getValue()) {
            selectedMatch.getShowedMatch().setBlueScore(Integer.parseInt(blueTeamScore.getText()));
            selectedMatch.getShowedMatch().setOrangeScore(Integer.parseInt(orangeTeamScore.getText()));
            blueTeamScore.setEditable(false);
            orangeTeamScore.setEditable(false);
            editMatchBtn.setText("Edit match");
        } else {
            blueTeamScore.setEditable(true);
            orangeTeamScore.setEditable(true);
            editMatchBtn.setText("Save edit");
        }
    }

    /**
     * Updates the team viewer on match clicked in overviewTab
     */
    private void updateTeamViewer(Match match) {
        if (match.getBlueTeam() != null) {
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
        if (match.getOrangeTeam() != null) {
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
     * Ensures that the score is legal and less than 999. Then applies it to the team score.
     */
    private void validateScoreInput(String oldValue, String newValue, TextField teamScore) {
        if (newValue.length() > 1 && newValue.charAt(0) == '0') {
            teamScore.setText(newValue.replaceFirst("0", ""));
        }

        if (newValue.length() <= 3) {
            if (!newValue.matches("\\d*")) {
                teamScore.setText(newValue.replaceAll("[^\\d]", ""));
            }
        } else teamScore.setText(oldValue);
    }

    public void onStartTournamentButtonPressed(ActionEvent actionEvent) {
        if (Tournament.get().canStart()) {
            startTournamentInstructionsHolder.setManaged(false);
            startTournamentInstructionsHolder.setVisible(false);
            overviewScrollPane.setManaged(true);
            overviewScrollPane.setVisible(true);
            Tournament.get().start();
            show(Tournament.get().getCurrentStage().getFormat());
        } else {
            System.out.println("Can't start tournament.");
        }
    }

    public void show(Format format) {
        overviewScrollPane.setContent(format.getJavaFxNode(this));
    }
}
