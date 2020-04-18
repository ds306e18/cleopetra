package dk.aau.cs.ds306e18.tournament.ui;

import com.google.common.primitives.Ints;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import javafx.application.Platform;
import dk.aau.cs.ds306e18.tournament.model.match.MatchResultDependencyException;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class EditMatchScoreController extends DraggablePopupWindow {

    private static final Paint BLUE_FILL = Paint.valueOf("#6a82fc");
    private static final Paint ORANGE_FILL = Paint.valueOf("#f5af18");

    @FXML private CheckBox matchOverCheckBox;
    @FXML private Label teamOneNameLabel;
    @FXML private Label teamTwoNameLabel;
    @FXML private Spinner<Integer> teamOneScoreSpinner;
    @FXML private Spinner<Integer> teamTwoScoreSpinner;
    @FXML private Button saveButton;

    private Series series;

    @FXML
    private void initialize() {

        // Spinners
        setupScoreSpinner(teamOneScoreSpinner);
        setupScoreSpinner(teamTwoScoreSpinner);

        matchOverCheckBox.setDisable(true);
    }

    /** Adds behaviour to a score spinner. */
    private void setupScoreSpinner(final Spinner<Integer> spinner) {

        // Values allowed in the spinner
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99));
        spinner.setEditable(true);
        spinner.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            // We allow empty strings and all positive numbers below 100, including 0, to be in the text field
            if (!(newText.equals("") || newText.matches("^([0-9]|[1-9][0-9])$"))) {
                spinner.getEditor().setText(oldText);
            } else {
                matchOverCheckBox.setSelected(true);
            }
            checkScoresAndUpdateUI();
        });

        // Select all text in text field when focused or edited
        spinner.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused){
                // Have to call using Platform.Runlater because the spinner does something when it gains focus that interrupts.
                Platform.runLater(spinner.getEditor()::selectAll);
            }
        });
        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            spinner.getEditor().selectAll();
        }));
    }

    /** Checks the scores in the spinners. If the are okay, save button is enabled, other match-is-over checkbox
     * and save button is disabled accordingly. */
    private void checkScoresAndUpdateUI() {

        String teamOneScoreText = teamOneScoreSpinner.getEditor().getText();
        String teamTwoScoreText = teamTwoScoreSpinner.getEditor().getText();

        // Do we have two numbers or just empty strings?
        if (!teamOneScoreText.equals("") && !teamTwoScoreText.equals("")) {

            // Get the scores
            int teamOneScore = Integer.parseInt(teamOneScoreText);
            int teamTwoScore = Integer.parseInt(teamTwoScoreText);

            saveButton.setDisable(false);

            if (teamOneScore == teamTwoScore) {
                // We won't allow the match to be over if the scores are equal
                matchOverCheckBox.setSelected(false);
                matchOverCheckBox.setDisable(true);
            } else {
                // Everything is good
                matchOverCheckBox.setDisable(false);
            }

        } else {
            saveButton.setDisable(true);
        }
    }

    public void setSeries(Series series) {
        if (series == null) {
            closeWindow();
        }
        this.series = series;

        teamOneNameLabel.setText(series.getTeamOne().getTeamName());
        teamTwoNameLabel.setText(series.getTeamTwo().getTeamName());
        if (series.isTeamOneBlue()) {
            teamOneNameLabel.setTextFill(BLUE_FILL);
            teamTwoNameLabel.setTextFill(ORANGE_FILL);
        } else {
            teamOneNameLabel.setTextFill(ORANGE_FILL);
            teamTwoNameLabel.setTextFill(BLUE_FILL);
        }

        teamOneScoreSpinner.getValueFactory().setValue(series.getTeamOneScore(0));
        teamTwoScoreSpinner.getValueFactory().setValue(series.getTeamTwoScore(0));

        matchOverCheckBox.setSelected(series.hasBeenPlayed());
    }

    @FXML
    public void windowDragged(MouseEvent mouseEvent) {
        super.windowDragged(mouseEvent);
    }

    @FXML
    public void windowPressed(MouseEvent mouseEvent) {
        super.windowPressed(mouseEvent);
    }

    @FXML
    private void onCancelBtnPressed(ActionEvent actionEvent) {
        closeWindow();
    }

    private void closeWindow() {
        Stage window = (Stage) teamOneNameLabel.getScene().getWindow();
        window.close();
    }

    @FXML
    private void FetchBtnPressed(ActionEvent actionEvent) {
        // TODO Fetch the match result directly from the game using RLBotDLl
    }

    @FXML
    private void onSaveBtnPressed(ActionEvent actionEvent) {

        int teamOneScore = Integer.parseInt(teamOneScoreSpinner.getEditor().getText());
        int teamTwoScore = Integer.parseInt(teamTwoScoreSpinner.getEditor().getText());
        boolean played = matchOverCheckBox.isSelected();

        boolean force = false;
        while (true) {
            try {

                series.setScores(
                        series.getSeriesLength(),
                        Ints.asList(teamOneScore),
                        Ints.asList(teamTwoScore),
                        played,
                        force);
                closeWindow();
                break;

            } catch (MatchResultDependencyException e) {
                // An MatchResultDependencyException is thrown if the outcome has changed and subsequent matches depends on this outcome
                // Ask if the user wants to proceed
                force = Alerts.confirmAlert("The outcome of this match has changed", "This change will reset the subsequent matches. Do you want to proceed?");
            }
        }
    }
}
