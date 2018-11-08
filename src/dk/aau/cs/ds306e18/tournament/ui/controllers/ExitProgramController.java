package dk.aau.cs.ds306e18.tournament.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ExitProgramController {

    double x = 0;
    double y = 0;

    @FXML
    private Button cancelBtn;

    @FXML
    void cancelClose(ActionEvent event) {    // get a handle to the stage
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    void exitProgram(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void saveTournament(ActionEvent event) {
        /* TODO: Add functionality
         * TODO: Handle if filechooser was closed during selection */

        Stage stage = (Stage) cancelBtn.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose save destination and name");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tournament format (*.rlts)", "*.rlts"));
        fileChooser.setInitialFileName("tournamentName.rlts");

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            if (!file.getName().endsWith(".rlts")) {
                file = new File(file.getAbsolutePath() + ".rlts");
            }
        }

        /* TEST */
        try {
            FileWriter write = new FileWriter(file, true);
            PrintWriter printer = new PrintWriter(write);

            printer.printf("Hello");
            printer.close();

        } catch (IOException e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
        }

        /* TODO: Create check if file was written successfully */
        System.exit(0);


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
