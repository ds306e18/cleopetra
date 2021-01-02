package dk.aau.cs.ds306e18.tournament.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Optional;

/**
 * A controller for the EditSeriesScore window part where you input the score for a single match, i.e the one
 * that is repeated when there series is longer.
 */
public class EditMatchScoreController {

    @FXML private VBox root;
    @FXML private Spinner<String> teamOneScoreSpinner;
    @FXML private Spinner<String> teamTwoScoreSpinner;

    public static EditMatchScoreController loadNew(Runnable onChange) {
        try {
            // Load the fxml document into the Controller and JavaFx node.
            FXMLLoader loader = new FXMLLoader(EditMatchScoreController.class.getResource("layout/EditMatchScore.fxml"));
            loader.load();
            EditMatchScoreController controller = loader.getController();
            controller.setupScoreSpinner(controller.teamOneScoreSpinner, onChange);
            controller.setupScoreSpinner(controller.teamTwoScoreSpinner, onChange);
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds behaviour to a score spinner.
     */
    private void setupScoreSpinner(final Spinner<String> spinner, Runnable onChange) {

        // Values allowed in the spinner
        spinner.setValueFactory(new ScoreSpinnerValueFactory(0, 99));
        spinner.setEditable(true);
        spinner.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            // We allow empty strings, "-", and all positive numbers between 0 and 99 (inclusive) to be in the text field
            if (!(newText.equals("") || newText.matches("^(-|[0-9]|[1-9][0-9])$"))) {
                spinner.getEditor().setText(oldText);
            } else {
                onChange.run();
            }
        });

        // Select all text in text field when focused or edited
        spinner.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused){
                // Have to call using Platform::runLater because the spinner does something when it gains focus that interrupts.
                Platform.runLater(spinner.getEditor()::selectAll);
            }
        });
        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            spinner.getEditor().selectAll();
        }));
    }

    public VBox getRoot() {
        return root;
    }

    /**
     * Interpret and return the currently written score as an integer. If the written score is "" or "-" an empty
     * value is returned.
     */
    public Optional<Integer> getTeamOneScore() {
        String scoreText = teamOneScoreSpinner.getEditor().getText();
        return "".equals(scoreText) || "-".equals(scoreText) ? Optional.empty() : Optional.of(Integer.parseInt(scoreText));
    }

    /**
     * Interpret and return the currently written score as an integer. If the written score is "" or "-" an empty
     * value is returned.
     */
    public Optional<Integer> getTeamTwoScore() {
        String scoreText = teamTwoScoreSpinner.getEditor().getText();
        return "".equals(scoreText) || "-".equals(scoreText) ? Optional.empty() : Optional.of(Integer.parseInt(scoreText));
    }

    /**
     * Set the displayed scores.
     */
    public void setScores(Optional<Integer> teamOneScore, Optional<Integer> teamTwoScore) {
        teamOneScoreSpinner.getEditor().setText(teamOneScore.map(Object::toString).orElse("-"));
        teamTwoScoreSpinner.getEditor().setText(teamTwoScore.map(Object::toString).orElse("-"));
    }
}
