package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import dk.aau.cs.ds306e18.tournament.utility.SaveLoad;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ExitProgramController extends DraggablePopupWindow {

    @FXML
    private Button cancelBtn;

    @FXML
    void cancelClose(ActionEvent event) {
        Stage exitStage = (Stage) cancelBtn.getScene().getWindow();
        exitStage.close();
    }

    @FXML
    void exitProgram(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void saveTournament() {
        /* TODO: Handle if filechooser was closed during selection */

        Stage stage = (Stage) cancelBtn.getScene().getWindow();

        try {
            SaveLoad.saveTournamentWithFileChooser(stage);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            Alerts.errorNotification("Error while saving", "Something went wrong while saving the tournament: " + e.getMessage());
            Main.LOGGER.log(System.Logger.Level.ERROR, "Error while saving tournament", e);
        }
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
