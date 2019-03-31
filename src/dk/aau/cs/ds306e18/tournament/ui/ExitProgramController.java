package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.utility.FileOperations;
import dk.aau.cs.ds306e18.tournament.utility.SaveLoad;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

        if (SaveLoad.saveTournament(stage)) {
            System.exit(0);
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
