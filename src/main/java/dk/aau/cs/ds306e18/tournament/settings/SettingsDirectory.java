package dk.aau.cs.ds306e18.tournament.settings;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.utility.FileOperations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * This class contains paths to all the files in the settings directory, which makes it easy to refer to the any given
 * file. The setup method should be called on start up to ensure that the require directories and files exists.
 */
public class SettingsDirectory {

    public static final Path BASE = Paths.get(System.getProperty("user.home")).resolve(".cleopetra/");
    public static final Path PROPERTIES = BASE.resolve("cleopetra.properties");
    public static final Path PSYONIX_BOTS = BASE.resolve("psyonix_bots");
    public static final Path PSYONIX_APPEARANCE = PSYONIX_BOTS.resolve("psyonix_loadout.toml");
    public static final Path PSYONIX_ALLSTAR = PSYONIX_BOTS.resolve("psyonix_allstar.bot.toml");
    public static final Path PSYONIX_PRO = PSYONIX_BOTS.resolve("psyonix_pro.bot.toml");
    public static final Path PSYONIX_ROOKIE = PSYONIX_BOTS.resolve("psyonix_rookie.bot.toml");
    public static final Path MATCH_FILES = BASE.resolve("rlbot");
    public static final Path MATCH_CONFIG = MATCH_FILES.resolve("rlbot.cfg");
    public static final Path RUN_PY = MATCH_FILES.resolve("run.py");

}
