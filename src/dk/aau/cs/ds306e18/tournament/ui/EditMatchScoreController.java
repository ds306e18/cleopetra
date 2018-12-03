package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
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
        blueScoreSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        orangeScoreSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));

        blueScoreSpinner.setEditable(true);
        orangeScoreSpinner.setEditable(true);

        setSpinnerListeners();

        matchOverCheckBox.setDisable(true);

        isBlueScoreLegit = true;
        isOrangeScoreLegit = true;
    }

    /** Adds listeners to spinners to check if matchIsOver should be disabled or
     * the save button should be disabled on invalid input.*/
    private void setSpinnerListeners(){
        blueScoreSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                Integer.valueOf(newValue); //This will throw the exception if the value not only contains numbers
                isBlueScoreLegit = true;
                isTeamScoresEqual();
                if (isOrangeScoreLegit && isBlueScoreLegit){
                    saveButton.setDisable(false);
                }
            } catch (NumberFormatException e) {
                isTeamScoresEqual();
                isBlueScoreLegit = false;
                saveButton.setDisable(true);
            }
        });

        orangeScoreSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                Integer.valueOf(newValue); //This will throw the exception if the value not only contains numbers
                isOrangeScoreLegit = true;
                isTeamScoresEqual();
                if (isOrangeScoreLegit && isBlueScoreLegit){
                    saveButton.setDisable(false);
                }
            } catch (NumberFormatException e) {
                isTeamScoresEqual();
                isOrangeScoreLegit = false;
                saveButton.setDisable(true);
            }
        });
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
        if(blueScoreSpinner.getEditor().getText().equals(orangeScoreSpinner.getEditor().getText())){
            matchOverCheckBox.setDisable(true);
            matchOverCheckBox.setSelected(false);
        } else {
            matchOverCheckBox.setDisable(false);
            matchOverCheckBox.setSelected(true);
        }
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
                Integer.parseInt(blueScoreSpinner.getEditor().getText()),
                Integer.parseInt(orangeScoreSpinner.getEditor().getText()),
                matchOverCheckBox.isSelected()
        );
        closeWindow();
    }
}
