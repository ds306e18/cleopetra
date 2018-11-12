package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.utility.FileOperations;
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
    void saveTournament(ActionEvent event) {
        /* TODO: Handle if filechooser was closed during selection */

        boolean writeStatus = false;
        String extension = "rlts";

        Stage stage = (Stage) cancelBtn.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file name and save destination");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tournament format (*." + extension + ")", "*." + extension));
        fileChooser.setInitialFileName(Tournament.get().getName() + "." + extension);

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                writeStatus = FileOperations.writeTournamentToFilesystem(file.getParent(), file.getName(), extension, Tournament.get());
            } catch (IOException e) {
                System.out.println("ERROR: Caught IOException when writing to " + file.getAbsolutePath() + ". " + e.getMessage());
            }
        }

        if (writeStatus) {
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
