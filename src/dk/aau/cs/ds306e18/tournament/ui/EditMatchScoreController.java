package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
import javafx.application.Platform;
import dk.aau.cs.ds306e18.tournament.model.match.MatchResultDependencyException;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class EditMatchScoreController {

    @FXML private CheckBox matchOverCheckBox;
    @FXML private Label blueTeamNameLabel;
    @FXML private Label orangeTeamNameLabel;
    @FXML private Spinner<Integer> blueScoreSpinner;
    @FXML private Spinner<Integer> orangeScoreSpinner;
    @FXML private Button saveButton;

    private boolean isBlueScoreLegit;
    private boolean isOrangeScoreLegit;

    private Match match;
    private double x = 0;
    private double y = 0;

    @FXML
    private void initialize() {

        // Spinners
        setupScoreSpinner(blueScoreSpinner);
        setupScoreSpinner(orangeScoreSpinner);

        matchOverCheckBox.setDisable(true);

        isBlueScoreLegit = true;
        isOrangeScoreLegit = true;
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

        String blueScoreText = blueScoreSpinner.getEditor().getText();
        String orangeScoreText = orangeScoreSpinner.getEditor().getText();

        // Do we have two numbers or just empty strings?
        if (!blueScoreText.equals("") && !orangeScoreText.equals("")) {

            // Get the scores
            int blueScore = Integer.parseInt(blueScoreText);
            int orangeScore = Integer.parseInt(orangeScoreText);

            saveButton.setDisable(false);

            if (blueScore == orangeScore) {
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

    public void setMatch(Match match) {
        if (match == null) {
            closeWindow();
        }
        this.match = match;

        blueTeamNameLabel.setText(match.getBlueTeam().getTeamName());
        orangeTeamNameLabel.setText(match.getOrangeTeam().getTeamName());

        blueScoreSpinner.getValueFactory().setValue(match.getBlueScore());
        orangeScoreSpinner.getValueFactory().setValue(match.getOrangeScore());

        matchOverCheckBox.setSelected(match.hasBeenPlayed());
    }

    @FXML
    private void windowDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    private void windowPressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    private void onCancelBtnPressed(ActionEvent actionEvent) {
        closeWindow();
    }

    private void closeWindow() {
        Stage window = (Stage) blueTeamNameLabel.getScene().getWindow();
        window.close();
    }

    @FXML
    private void FetchBtnPressed(ActionEvent actionEvent) {
        // TODO Fetch the match result directly from the game using RLBotDLl
    }

    @FXML
    private void onSaveBtnPressed(ActionEvent actionEvent) {

        int blueScore = Integer.parseInt(blueScoreSpinner.getEditor().getText());
        int orangeScore = Integer.parseInt(orangeScoreSpinner.getEditor().getText());
        boolean played = matchOverCheckBox.isSelected();
        try {
            match.setScores(blueScore, orangeScore, played);
            closeWindow();

        } catch (MatchResultDependencyException e) {
            // An MatchResultDependencyException is thrown if the outcome has changed and subsequent matches depends on this outcome
            // Ask if the user wants to proceed
            boolean proceed = Alerts.confirmAlert("The outcome of this match has changed", "This change will reset the subsequent matches. Do you want to proceed?");
            if (proceed) {
                match.setScores(blueScore, orangeScore, played, true);
                closeWindow();
            }
        }
    }
}
