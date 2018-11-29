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

public class ExitProgramController {

    private double x = 0;
    private double y = 0;

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
    void windowDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void windowPressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }
}
