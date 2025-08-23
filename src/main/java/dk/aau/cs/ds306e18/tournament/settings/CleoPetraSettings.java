package dk.aau.cs.ds306e18.tournament.settings;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.utility.FileOperations;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class CleoPetraSettings {

    private static Properties properties;
    private static LatestPaths latestPaths;

    /**
     * Setup/load the settings for CleoPetra, located in 'user.home/.cleopetra/', and makes sure the required
     * files are present. Should always be called on start up.
     */
    public static void setup() {
        try {

            Files.createDirectories(SettingsDirectory.BASE);

            // CleoPetra properties
            if (Files.notExists(SettingsDirectory.PROPERTIES)) {
                Files.createFile(SettingsDirectory.PROPERTIES);
            }
            try (InputStream fs = Files.newInputStream(SettingsDirectory.PROPERTIES)) {
                properties = new Properties();
                properties.load(fs);
            }

            latestPaths = new LatestPaths(properties);

            // Files for starting matches. 'rlbot.cfg' is created right before match start.
            Files.createDirectories(SettingsDirectory.MATCH_FILES);
            Files.copy(Main.class.getResourceAsStream("settings/files/run.py"), SettingsDirectory.RUN_PY, StandardCopyOption.REPLACE_EXISTING);

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
        Files.createDirectories(SettingsDirectory.PSYONIX_BOTS);
        FileOperations.copyIfMissing(Main.class.getResourceAsStream("settings/files/psyonix_loadout.toml"), SettingsDirectory.PSYONIX_APPEARANCE);
        FileOperations.copyIfMissing(Main.class.getResourceAsStream("settings/files/psyonix_allstar.bot.toml"), SettingsDirectory.PSYONIX_ALLSTAR);
        FileOperations.copyIfMissing(Main.class.getResourceAsStream("settings/files/psyonix_pro.bot.toml"), SettingsDirectory.PSYONIX_PRO);
        FileOperations.copyIfMissing(Main.class.getResourceAsStream("settings/files/psyonix_rookie.bot.toml"), SettingsDirectory.PSYONIX_ROOKIE);
    }

    public static LatestPaths getLatestPaths() {
        return latestPaths;
    }
}
