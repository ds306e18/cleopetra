package dk.aau.cs.ds306e18.tournament.settings;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Properties;

/**
 * A helper class to handle paths to where tournament files, bots, and overlays are stored. Main usage is
 * making file chooser start from a reasonable directory.
 */
public class LatestPaths {

    // If we have no info about where things are stored we will use the current working directory
    private static final File FALLBACK_DIR = new File("").getAbsoluteFile();

    private static final String KEY_TOURNAMENT_SAVE_DIR = "lastTournamentSaveDir";
    private static final String KEY_BOT_CONFIG_DIR = "lastBotConfigDir";
    private static final String KEY_OVERLAY_DIR = "lastOverlayDir";

    private final Properties properties;

    /**
     * Create a new LatestPaths based on the given properties file.
     */
    public LatestPaths(Properties properties) {
        this.properties = properties;
    }

    /**
     * Returns the directory where the latest tournament file was saved or a reasonable fallback directory.
     */
    public File getTournamentSaveDirectory() {
        String tournamentSaveDirectory = properties.getProperty(KEY_TOURNAMENT_SAVE_DIR);
        if (tournamentSaveDirectory != null) {
            File dir = new File(tournamentSaveDirectory);
            // Dir might not exist anymore. In that case we will forget about it
            if (dir.exists()) return dir;
            else properties.remove(KEY_TOURNAMENT_SAVE_DIR);
        }
        return FALLBACK_DIR;
    }

    /**
     * Update latest tournament save directory.
     */
    public void setTournamentSaveDirectory(File dir) {
        if (dir.exists()) {
            properties.setProperty(KEY_TOURNAMENT_SAVE_DIR, dir.getAbsolutePath());
            saveProperties();
        }
    }

    /**
     * Returns the directory where the latest bot(s) was loaded from or a reasonable fallback directory.
     */
    public File getBotConfigDirectory() {
        String botConfigDirectory = properties.getProperty(KEY_BOT_CONFIG_DIR);
        if (botConfigDirectory != null) {
            File dir = new File(botConfigDirectory);
            // Dir might not exist anymore. In that case we will forget about it
            if (dir.exists()) return dir;
            else properties.remove(KEY_BOT_CONFIG_DIR);
        }
        // Fallback to tournament save dir
        return getTournamentSaveDirectory();
    }

    /**
     * Update latest bot config directory.
     */
    public void setBotConfigDirectory(File dir) {
        if (dir.exists()) {
            properties.setProperty(KEY_BOT_CONFIG_DIR, dir.getAbsolutePath());
            saveProperties();
        }
    }

    /**
     * Returns the directory where the latest overlay was stored or a reasonable fallback directory.
     */
    public File getOverlayDirectory() {
        String overlayDirectory = properties.getProperty(KEY_OVERLAY_DIR);
        if (overlayDirectory != null) {
            File dir = new File(overlayDirectory);
            // Dir might not exist anymore. In that case we will forget about it
            if (dir.exists()) return dir;
            else properties.remove(KEY_TOURNAMENT_SAVE_DIR);
        }
        // Fallback to tournament save dir
        return getTournamentSaveDirectory();
    }

    /**
     * Update latest overlay directory.
     */
    public void setOverlayDirectory(File dir) {
        if (dir.exists()) {
            properties.setProperty(KEY_OVERLAY_DIR, dir.getAbsolutePath());
            saveProperties();
        }
    }

    private void saveProperties() {
        try (OutputStream fs = Files.newOutputStream(SettingsDirectory.PROPERTIES)) {
            properties.store(fs, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
