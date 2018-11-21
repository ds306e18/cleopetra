package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class EditMatchScoreController {

    @FXML private CheckBox matchOverCheckBox;
    @FXML private  Label blueTeamNameLabel;
    @FXML private  Label orangeTeamNameLabel;
    @FXML private  Spinner<Integer> blueScoreSpinner;
    @FXML private  Spinner<Integer> orangeScoreSpinner;

    private Match match;
    private double x = 0;
    private double y = 0;

    @FXML
    private void initialize() {
        blueScoreSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1));
        orangeScoreSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1));

        //Add listeners to spinners to check if matchIsOver should be disabled
        blueScoreSpinner.valueProperty().addListener(e -> isTeamScoresEqual());
        orangeScoreSpinner.valueProperty().addListener(e -> isTeamScoresEqual());

        matchOverCheckBox.setDisable(true);
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

    /** Used to determine if the matchOverCheckBox should be disabled. */
    private void isTeamScoresEqual(){
        if(blueScoreSpinner.getValue() == orangeScoreSpinner.getValue()){
            matchOverCheckBox.setDisable(true);
            matchOverCheckBox.setSelected(false);
        } else
            matchOverCheckBox.setDisable(false);
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
        // TODO Check if this cause problems (e.g. changing the winner if match when it was already played) and warn user
        match.setScores(
                blueScoreSpinner.getValue(),
                orangeScoreSpinner.getValue(),
                matchOverCheckBox.isSelected()
        );
        closeWindow();
    }
}
