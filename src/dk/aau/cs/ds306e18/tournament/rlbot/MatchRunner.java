package dk.aau.cs.ds306e18.tournament.rlbot;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import dk.aau.cs.ds306e18.tournament.utility.configuration.RLBotConfig;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MatchRunner {

    // The %s will be replaced with the directory of the rlbot.cfg
    private static final String COMMAND_FORMAT = "cmd.exe /c start cmd.exe /c \"cd %s & python run.py\"";

    /** Starts the given match in Rocket League. */
    public static boolean startMatch(RLBotSettings settings, Match match) {

        // Checks settings and modifies rlbot.cfg file if everything is okay
        boolean ready = prepareMatch(settings, match);
        if (!ready) {
            return false;
        }

        try {
            // We assume that the runner is in the same folder as the rlbot.cfg
            Path pathToDirectory = Paths.get(settings.getConfigPath()).getParent();
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
    public static boolean canStartMatch(RLBotSettings settings, Match match) {
        try {
            checkMatch(settings, match);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Check the requirements for starting the match. Throws an IllegalStateException if anything is wrong. Does nothing
     * if everything is okay.
     */
    private static void checkMatch(RLBotSettings settings, Match match) {
        // These methods throws IllegalStageException if anything is wrong
        checkRLBotSettings(settings);
        checkBotConfigsInMatch(match);
    }

    /** Checks rlbot settings. Throws an IllegalStateException if anything is wrong. Does nothing if everything is okay. */
    private static void checkRLBotSettings(RLBotSettings settings) {
        // Check if rlbot.cfg set
        String configPath = settings.getConfigPath();
        if (configPath == null || configPath.isEmpty())
            throw new IllegalStateException("The RLBot config file (rlbot.cfg) is not set in RLBot Settings.");

        // Check if rlbot.cfg exists
        File cfg = new File(configPath);
        if (!cfg.exists() || !cfg.isFile() || cfg.isDirectory())
            throw new IllegalStateException("Could not find RLBot config file (\"" + configPath + "\")");

        // Check if run.py exists in same directory
        File runner = new File(cfg.getParent(), "run.py");
        if (!runner.exists()) {
            throw new IllegalStateException("Could not find run.py next to the RLBot config file (\"" + runner.getAbsolutePath() + "\")");
        }
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
    public static boolean prepareMatch(RLBotSettings settings, Match match) {
        try {
            // Check settings and config files
            checkMatch(settings, match);

            // Set up rlbot config file
            RLBotConfig.readConfig(settings.getConfigPath());
            RLBotConfig.setupMatch(match);
            RLBotConfig.writeConfig(settings.getConfigPath());
            return true;

        } catch (IllegalStateException e) {
            Alerts.errorNotification("Error occurred while configuring match", e.getMessage());
            return false;
        }
    }
}
