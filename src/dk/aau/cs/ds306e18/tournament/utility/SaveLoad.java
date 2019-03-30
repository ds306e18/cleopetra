package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class SaveLoad {

    public static boolean saveTournament (Stage stage){
        boolean saveStatus = false;

        String extension = "rlts";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file name and save destination");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tournament format (*." + extension + ")", "*." + extension));
        fileChooser.setInitialFileName(Tournament.get().getName() + "." + extension);
        fileChooser.setInitialDirectory(Main.lastSavedDirectory);

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                saveStatus = FileOperations.writeTournamentToFilesystem(file.getParent(), file.getName(), extension, Tournament.get());
            } catch (IOException e) {
                System.out.println("ERROR: Caught IOException when writing to " + file.getAbsolutePath() + ". " + e.getMessage());
            }
        }

        if (saveStatus){
            Main.lastSavedDirectory = file.getParentFile();
        }

        return saveStatus;
    }

}
