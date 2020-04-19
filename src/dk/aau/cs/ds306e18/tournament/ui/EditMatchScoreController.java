package dk.aau.cs.ds306e18.tournament.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EditMatchScoreController {

    @FXML private VBox root;
    @FXML private Spinner<Integer> teamOneScoreSpinner;
    @FXML private Spinner<Integer> teamTwoScoreSpinner;

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
    private void setupScoreSpinner(final Spinner<Integer> spinner, Runnable onChange) {

        // Values allowed in the spinner
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99));
        spinner.setEditable(true);
        spinner.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            // We allow empty strings and all positive numbers between 0 and 99 (inclusive) to be in the text field
            if (!(newText.equals("") || newText.matches("^([0-9]|[1-9][0-9])$"))) {
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

    public Integer getTeamOneScore() {
        String scoreText = teamOneScoreSpinner.getEditor().getText();
        return "".equals(scoreText) ? null : Integer.parseInt(scoreText);
    }

    public Integer getTeamTwoScore() {
        String scoreText = teamTwoScoreSpinner.getEditor().getText();
        return "".equals(scoreText) ? null : Integer.parseInt(scoreText);
    }

    public void setScores(int teamOneScore, int teamTwoScore) {
        teamOneScoreSpinner.getValueFactory().setValue(teamOneScore);
        teamTwoScoreSpinner.getValueFactory().setValue(teamTwoScore);
    }
}
