package dk.aau.cs.ds306e18.tournament;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * CleoPetraSettings is used to control the settings of CleoPetra between sessions and generally administrating files
 * in CleoPetra's settings folder.
 */
public class CleoPetraSettings {

    public static final String PSYONIX_BOTS_FOLDER = "psyonix_bots/";
    public static final String PSYONIX_ALLSTAR_FILE_NAME = "psyonix_allstar.cfg";
    public static final String PSYONIX_PRO_FILE_NAME = "psyonix_pro.cfg";
    public static final String PSYONIX_ROOKIE_FILE_NAME = "psyonix_rookie.cfg";
    public static final String PSYONIX_APPEARANCE_FILE_NAME = "psyonix_appearance.cfg";

    /**
     * @return The folder containing CleoPetra's settings
     */
    public static Path getPathToSettingsFolder() {
        return Paths.get(System.getProperty("user.home")).resolve(".cleopetra/");
    }

    /**
     * @return The folder of bots called the RLBotPack. It is downloaded with the RLBotGUI. It is not guaranteed to
     * exist and can be null if APPDATA is not an environment variable.
     */
    public static Path getPathToRLBotPack() {
        try {
            return Paths.get(System.getenv("APPDATA")).getParent().resolve("Local\\RLBotGUI\\RLBotPack");
        } catch (Exception e) {
            // Failed. Maybe we are on a Linux system
            return null;
        }
    }

    /**
     * Copies the Psyonix bot config files to the CleoPetra settings folder so they can be read by the RLBot framework.
     * If the files already exists, they won't be replaced. This allows the user to change the config files
     * (e.g. change the appearance) and customize their Psyonix bots.
     *
     * @throws IOException thrown if something goes wrong during copying the files or if URI path is wrong.
     */
    public static void copyPsyonixBotsToSettingsFolder() throws IOException {
        try {
            Path psyonixFolder = getPathToSettingsFolder().resolve(PSYONIX_BOTS_FOLDER);
            Files.createDirectories(psyonixFolder); // make sure it exists

            // All-Star
            Path allStar = psyonixFolder.resolve(PSYONIX_ALLSTAR_FILE_NAME);
            if (!Files.exists(allStar)) {
                Path allStarInternal = Paths.get(Main.class.getResource("rlbot/psyonix_allstar.cfg").toURI());
                Files.copy(allStarInternal, allStar);
            }

            // Pro
            Path pro = psyonixFolder.resolve(PSYONIX_PRO_FILE_NAME);
            if (!Files.exists(pro)) {
                Path proInternal = Paths.get(Main.class.getResource("rlbot/psyonix_pro.cfg").toURI());
                Files.copy(proInternal, pro);
            }

            // Rookie
            Path rookie = psyonixFolder.resolve(PSYONIX_ROOKIE_FILE_NAME);
            if (!Files.exists(rookie)) {
                Path rookieInternal = Paths.get(Main.class.getResource("rlbot/psyonix_rookie.cfg").toURI());
                Files.copy(rookieInternal, rookie);
            }

            // Appearance
            Path appearance = psyonixFolder.resolve(PSYONIX_APPEARANCE_FILE_NAME);
            if (!Files.exists(appearance)) {
                Path appearanceInternal = Paths.get(Main.class.getResource("rlbot/psyonix_appearance.cfg").toURI());
                Files.copy(appearanceInternal, appearance);
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IOException("Failed to copy Psyonix bots due to URISyntaxException.");
        }
    }
}
