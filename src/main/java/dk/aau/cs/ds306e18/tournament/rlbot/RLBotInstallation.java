package dk.aau.cs.ds306e18.tournament.rlbot;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RLBotInstallation {

    /**
     * @return The folder of bots called the RLBotPack. It is downloaded with the RLBotGUI. It is not guaranteed to
     * exist and can be null if APPDATA is not an environment variable.
     */
    public static Path getPathToRLBotPack() {
        try {
            return Paths.get(System.getenv("APPDATA")).getParent().resolve("Local\\RLBotGUIX\\RLBotPackDeletable");
        } catch (Exception e) {
            // Failed. Maybe we are on a Linux system
            return null;
        }
    }

    /**
     * @return The path to the Python installation that the RLBotGUI uses
     * (the new installation in %APPDATA%/Local/RLBotGUIX/...). It is not guaranteed to
     * exist and can be null if APPDATA is not an environment variable.
     */
    public static Path getPathToPython() {
        try {
            return Paths.get(System.getenv("APPDATA")).getParent().resolve("Local\\RLBotGUIX\\Python311\\python.exe");
        } catch (Exception e) {
            // Failed. Maybe we are on a Linux system
            return null;
        }
    }
}
