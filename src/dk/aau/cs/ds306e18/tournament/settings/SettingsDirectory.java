package dk.aau.cs.ds306e18.tournament.settings;

import dk.aau.cs.ds306e18.tournament.utility.FileOperations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static dk.aau.cs.ds306e18.tournament.utility.FileOperations.internalPath;

/**
 * This class contains paths to all the files in the settings directory, which makes it easy to refer to the any given
 * file. The setup method should be called on start up to ensure that the require directories and files exists.
 */
public class SettingsDirectory {

    public static final Path BASE = Paths.get(System.getProperty("user.home")).resolve(".cleopetra/");
    public static final Path PSYONIX_BOTS = BASE.resolve("psyonix_bots");
    public static final Path PSYONIX_APPEARANCE = PSYONIX_BOTS.resolve("psyonix_appearance.cfg");
    public static final Path PSYONIX_ALLSTAR = PSYONIX_BOTS.resolve("psyonix_allstar.cfg");
    public static final Path PSYONIX_PRO = PSYONIX_BOTS.resolve("psyonix_pro.cfg");
    public static final Path PSYONIX_ROOKIE = PSYONIX_BOTS.resolve("psyonix_rookie.cfg");
    public static final Path MATCH_FILES = BASE.resolve("rlbot");
    public static final Path MATCH_CONFIG = MATCH_FILES.resolve("rlbot.cfg");
    public static final Path RUN_PY = MATCH_FILES.resolve("run.py");

    /**
     * Setup the settings directory, 'user.home/.cleopetra/', and makes sure the required files are present. Should
     * always be called on start up.
     */
    public static void setup() {
        try {

            // Files for starting matches. 'rlbot.cfg' is created right before match start.
            Files.createDirectories(MATCH_FILES);
            Files.copy(internalPath("settings/files/run.py"), RUN_PY, StandardCopyOption.REPLACE_EXISTING);

            setupPsyonixBots();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copies the Psyonix bot config files to the CleoPetra settings folder so they can be read by the RLBot framework.
     * If the files already exists, they won't be replaced. This allows the user to change the config files
     * (e.g. change the appearance) and customize their Psyonix bots.
     *
     * @throws IOException thrown if something goes wrong during copying the files or if URI path is wrong.
     */
    private static void setupPsyonixBots() throws IOException {
        Files.createDirectories(PSYONIX_BOTS);
        FileOperations.copyIfMissing(internalPath("settings/files/psyonix_appearance.cfg"), PSYONIX_APPEARANCE);
        FileOperations.copyIfMissing(internalPath("settings/files/psyonix_allstar.cfg"), PSYONIX_ALLSTAR);
        FileOperations.copyIfMissing(internalPath("settings/files/psyonix_pro.cfg"), PSYONIX_PRO);
        FileOperations.copyIfMissing(internalPath("settings/files/psyonix_rookie.cfg"), PSYONIX_ROOKIE);
    }
}
