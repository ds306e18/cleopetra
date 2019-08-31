package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static dk.aau.cs.ds306e18.tournament.serialization.Serializer.deserialize;
import static dk.aau.cs.ds306e18.tournament.serialization.Serializer.serialize;

public class SaveLoad {

    public static final String EXTENSION = "rlts";

    /**
     * Opens a FileChooser and saves the Tournament singleton to the selected location, or does nothing if no
     * file is was selected.
     * @param fxstage the JavaFX Stage in control of the FileChooser.
     * @throws IOException thrown if something goes wrong during saving.
     */
    public static void saveTournamentWithFileChooser(Stage fxstage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file name and save destination");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tournament format (*." + EXTENSION + ")", "*." + EXTENSION));
        fileChooser.setInitialFileName(Tournament.get().getName() + "." + EXTENSION);
        fileChooser.setInitialDirectory(Main.lastSavedDirectory);

        File file = fileChooser.showSaveDialog(fxstage);

        if (file != null) {
            saveTournament(Tournament.get(), file);
            Main.lastSavedDirectory = file.getParentFile();
        }
    }

    /**
     * Saves the given tournament to the given location.
     * @throws IOException thrown if something goes wrong during loading.
     */
    public static void saveTournament(Tournament tournament, File file) throws IOException {
        Files.write(file.toPath(), serialize(tournament).getBytes());
        Main.lastSavedDirectory = file.getParentFile();
    }

    /**
     * Loads a tournament from a given file.
     * @return The loaded Tournament.
     * @throws IOException thrown if something goes wrong during loading.
     */
    public static Tournament loadTournament(File file) throws IOException {
        Main.lastSavedDirectory = file.getParentFile();
        return deserialize(new String(Files.readAllBytes(file.toPath())));
    }
}
