package dk.aau.cs.ds306e18.tournament.rlbot;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotFromConfig;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfig;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static dk.aau.cs.ds306e18.tournament.CleoPetraSettings.getPathToMatchConfig;
import static dk.aau.cs.ds306e18.tournament.CleoPetraSettings.getPathToRunPy;

public class MatchRunner {

    // The %s will be replaced with the directory of the rlbot.cfg
    private static final String COMMAND_FORMAT = "cmd.exe /c start cmd.exe /c \"cd %s & python run.py\"";

    /** Starts the given match in Rocket League. */
    public static boolean startMatch(MatchConfig matchConfig, Match match) {

        // Checks settings and modifies rlbot.cfg file if everything is okay
        boolean ready = prepareMatch(matchConfig, match);
        if (!ready) {
            return false;
        }

        try {
            Path pathToDirectory = getPathToRunPy().getParent();
            String command = String.format(COMMAND_FORMAT, pathToDirectory);
            System.out.println("Running command: " + command);
            Runtime.getRuntime().exec(command);
            System.out.println("Started RLBot framework");
            return true;

        } catch (Exception err) {
            err.printStackTrace();
        }

        return false;
    }

    /** Returns true if the given match can be started. */
    public static boolean canStartMatch(Match match) {
        try {
            checkMatch(match);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Check the requirements for starting the match. Throws an IllegalStateException if anything is wrong. Does nothing
     * if everything is okay.
     */
    private static void checkMatch(Match match) {
        // These methods throws IllegalStageException if anything is wrong
        checkBotConfigsInMatch(match);
    }

    /**
     * Throws an IllegalStateException if anything is wrong with the bots' config files for this match. Does nothing if
     * all the bots on both teams have a valid config file in a given match.
     */
    private static void checkBotConfigsInMatch(Match match) {
        // Check if there are two teams
        if (match.getBlueTeam() == null) throw new IllegalStateException("There is no blue team for this match.");
        if (match.getOrangeTeam() == null) throw new IllegalStateException("There is no orange team for this match.");

        ArrayList<Bot> bots = new ArrayList<>();
        bots.addAll(match.getBlueTeam().getBots());
        bots.addAll(match.getOrangeTeam().getBots());

        for (Bot bot : bots) {
            String path = bot.getConfigPath();

            // Check if BotFromConfig bots are loaded correctly
            if (bot instanceof BotFromConfig && !((BotFromConfig) bot).loadedCorrectly())
                throw new IllegalStateException("The bot could not load from config: " + bot.getConfigPath());

            // Check if bot cfg is set
            if (path == null || path.isEmpty())
                throw new IllegalStateException("The bot '" + bot.getName() + "' has no config file.");

            // Check if bot cfg exists
            File file = new File(path);
            if (!file.exists() || !file.isFile() || file.isDirectory())
                throw new IllegalStateException("The config file of the bot named '" + bot.getName()
                        + "' is not found (path: '" + path + ")'");
        }
    }

    /**
     * A pre-process for starting a match. This method will check if all config files are available and then modify the
     * rlbot.cfg to start the given match when rlbot is run. Returns false and shows an alert if something went wrong
     * during preparation.
     */
    public static boolean prepareMatch(MatchConfig matchConfig, Match match) {
        try {
            // Check settings and config files
            checkMatch(match);
            matchConfig.write(getPathToMatchConfig().toFile());

            return true;
        } catch (IllegalStateException e) {
            Alerts.errorNotification("Error occurred while configuring match", e.getMessage());
        } catch (IOException e) {
            Alerts.errorNotification("IO error occurred while configuring match", e.getMessage());
        }
        return false;
    }
}
