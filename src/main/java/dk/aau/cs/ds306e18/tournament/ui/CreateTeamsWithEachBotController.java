package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.rlbot.BotCollection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CreateTeamsWithEachBotController extends DraggablePopupWindow {

    @FXML private Button saveButton;
    @FXML private Spinner<Integer> teamSizeSpinner;

    @FXML
    private void initialize() {
        // Setup team size spinner
        teamSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 32, 1));
    }

    public void closeWindow() {
        Stage window = (Stage) teamSizeSpinner.getScene().getWindow();
        window.close();
    }

    public void onCancelBtnPressed(ActionEvent actionEvent) {
        closeWindow();
    }

    public void onSaveBtnPressed(ActionEvent actionEvent) {
        Tournament tournament = Tournament.get();
        int seed = tournament.getTeams().size();
        int teamSize = teamSizeSpinner.getValue();
        for (Bot bot : BotCollection.global) {
            Team team = new Team(bot.getName(), Collections.nCopies(teamSize, bot), ++seed, "");
            tournament.addTeam(team);
        }
        closeWindow();
        Platform.runLater(ParticipantSettingsTabController.instance::update);
    }

    @FXML
    public void windowDragged(MouseEvent mouseEvent) {
        super.windowDragged(mouseEvent);
    }

    @FXML
    public void windowPressed(MouseEvent mouseEvent) {
        super.windowPressed(mouseEvent);
    }
}
