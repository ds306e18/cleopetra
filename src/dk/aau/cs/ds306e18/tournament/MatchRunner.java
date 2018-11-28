package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.utility.ConfigFileEditor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MatchRunner {

    // both %s will be replaced with the directory of the rlbot.cfg
    private static final String COMMAND_FORMAT = "cmd.exe /c start cmd.exe /c \"cd %s & python \"%s\\run.py\"\"";

    /** Starts the given match in Rocket League. */
    public static boolean startMatch(RLBotSettings settings, Match match) {
        try {
            checkMatch(settings, match);
        } catch (IllegalStateException e) {
            // TODO Show error notification
            System.err.println(e.getMessage());
            return false;
        }

        try {
            // Set up config file
            ConfigFileEditor.readConfig(settings.getConfigPath());
            ConfigFileEditor.configureMatch(match);
            ConfigFileEditor.writeConfig(settings.getConfigPath());

            Thread.sleep(100);

            // We assume that the runner is in the same folder as the rlbot.cfg
            Path pathToDirectory = Paths.get(settings.getConfigPath()).getParent();
            String command = String.format(COMMAND_FORMAT, pathToDirectory, pathToDirectory);
            System.out.println(command);
            Runtime.getRuntime().exec(command);
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

    /** Check the requirements for starting the match. Throws an IllegalStateException if anything is wrong.
     * Does nothing if everything is okay. */
    private static void checkMatch(RLBotSettings settings, Match match) {
        if (!settings.isConfigValid()) {
            throw new IllegalStateException("Error with RLBot config file.");
        }

        checkBotConfigsInMatch(match); // throws IllegalStageException too

        Path pathToDirectory = Paths.get(settings.getConfigPath()).getParent();
        File runner = new File(pathToDirectory.toFile(), "run.py");
        if (!runner.exists()) {
            throw new IllegalStateException("Could not find: " + runner.getAbsolutePath());
        }
    }

    /** Throws an IllegalStateException if anything is wrong with the bots' config files for this match.
     * Does nothing if all the bots on both teams have a valid config file in a given match. */
    private static void checkBotConfigsInMatch(Match match) {
        if (match.getBlueTeam() == null) throw new IllegalStateException("There is no blue team for this match.");
        if (match.getOrangeTeam() == null) throw new IllegalStateException("There is no orange team for this match.");

        ArrayList<Bot> bots = new ArrayList<>();
        bots.addAll(match.getBlueTeam().getBots());
        bots.addAll(match.getOrangeTeam().getBots());

        for (Bot bot : bots) {
            String path = bot.getConfigPath();

            if (path == null || path.isEmpty())
                throw new IllegalStateException("The bot '" + bot.getName() + "' has no config file.");

            File file = new File(path);

            if (!file.exists() || !file.isFile() || file.isDirectory())
                throw new IllegalStateException("The config file of the bot named '" + bot.getName()
                        + "' is not found (path: '" + path + ")'");
        }
    }
}
