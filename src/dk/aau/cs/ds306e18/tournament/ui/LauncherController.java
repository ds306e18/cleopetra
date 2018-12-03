package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import dk.aau.cs.ds306e18.tournament.utility.FileOperations;
import dk.aau.cs.ds306e18.tournament.utility.SaveLoad;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class LauncherController {
    @FXML private AnchorPane baseAnchorpane;

    private Stage createSystemStage (){
        Stage systemStage = new Stage();

        // Set min- width and height and title.
        systemStage.setMinWidth(800);
        systemStage.setMinHeight(650);
        systemStage.setTitle("DatTournament Runner");

        // Set the correct scene for the system stage.
        try {
            AnchorPane systemRoot = FXMLLoader.load(LauncherController.class.getResource("layout/MainLayout.fxml"));
            Alerts.window = systemRoot;
            systemStage.setScene(new Scene(systemRoot));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Create and load the exit popup when the user tries to close the system.
        Stage exitStage = new Stage();
        exitStage.initStyle(StageStyle.TRANSPARENT);
        exitStage.initModality(Modality.APPLICATION_MODAL);

        systemStage.setOnCloseRequest(e -> {
            e.consume();

            // Load exit program FXML file
            try {
                AnchorPane exitRoot = FXMLLoader.load(LauncherController.class.getResource("layout/Exit.fxml"));

                exitStage.setScene(new Scene(exitRoot));
                exitStage.show();
            } catch (IOException error) {
                error.printStackTrace();
            }
        });

        return systemStage;
    }

    private Stage getLauncherStage (){
        return (Stage) baseAnchorpane.getScene().getWindow();
    }

    @FXML
    void createNewTournament(ActionEvent event) {
        Stage systemStage = createSystemStage();

        // Hide launcher stage and then show the system stage.
        getLauncherStage().hide();
        systemStage.show();
    }

    @FXML
    void openLocalTournament(ActionEvent event) {
        // Set file extension.
        String extension = "rlts";

        // Create Filechooser and add extensionfilter and set initial directory to users documents folder.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open tournament file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tournament format (*." + extension + ")", "*." + extension));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + (File.separatorChar + "Documents")));

        File file = fileChooser.showOpenDialog(getLauncherStage());

        // Deserialize and set the loaded tournament. Then show the main stage.
        if (file != null){
            Tournament.get().setTournament(FileOperations.readTournamentFromFilesystem(file));
            SaveLoad.lastSavedDirectory = file.getParentFile();
            Stage systemStage = createSystemStage();
            getLauncherStage().hide();
            systemStage.show();
        }

    }
}
