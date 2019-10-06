package dk.aau.cs.ds306e18.tournament.rlbot;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotFromConfig;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfig;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.ParticipantInfo;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.TeamColor;
import dk.aau.cs.ds306e18.tournament.settings.SettingsDirectory;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import dk.aau.cs.ds306e18.tournament.utility.OverlayData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class MatchRunner {

    // The first %s will be replaced with the directory of the rlbot.cfg. The second %s will be the drive 'C:' to change drive.
    private static final String COMMAND_FORMAT = "cmd.exe /c start cmd /c \"cd %s & %s & python run.py\"";

    /** Starts the given match in Rocket League. */
    public static boolean startMatch(MatchConfig matchConfig, Match match) {

        // Checks settings and modifies rlbot.cfg file if everything is okay
        boolean ready = prepareMatch(matchConfig, match);
        if (!ready) {
            return false;
        }

        try {
            Path pathToDirectory = SettingsDirectory.RUN_PY.getParent();
            String command = String.format(COMMAND_FORMAT, pathToDirectory, pathToDirectory.toString().substring(0, 2));
            System.out.println("Starting RLBot framework with command: " + command);
            Runtime.getRuntime().exec(command);
            if (Tournament.get().getRlBotSettings().writeOverlayData()) {
                try {
                    OverlayData.write(match);
                } catch (IOException e) {
                    Alerts.errorNotification("Could not write overlay data", "Failed to write overlay data to " + OverlayData.CURRENT_MATCH_PATH);
                    e.printStackTrace();
                }
            }

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

        ArrayList<Bot> blueBots = match.getBlueTeam().getBots();
        ArrayList<Bot> orangeBots = match.getOrangeTeam().getBots();

        if (blueBots.size() == 0) throw new IllegalStateException("There are no bots on the blue team.");
        if (orangeBots.size() == 0) throw new IllegalStateException("There are no bots on the orange team.");

        ArrayList<Bot> bots = new ArrayList<>();
        bots.addAll(blueBots);
        bots.addAll(orangeBots);

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
            insertParticipants(matchConfig, match);
            matchConfig.write(SettingsDirectory.MATCH_CONFIG.toFile());

            return true;
        } catch (IllegalStateException e) {
            Alerts.errorNotification("Error occurred while configuring match", e.getMessage());
        } catch (IOException e) {
            Alerts.errorNotification("IO error occurred while configuring match", e.getMessage());
        }
        return false;
    }

    /**
     * Inserts the participants in a match into the MatchConfig. Any existing participants in the MatchConfig will be
     * removed.
     */
    private static void insertParticipants(MatchConfig config, Match match) {
        config.clearParticipants();

        insertParticipantsFromTeam(config, match.getBlueTeam().getBots(), TeamColor.BLUE);
        insertParticipantsFromTeam(config, match.getOrangeTeam().getBots(), TeamColor.ORANGE);
    }

    /**
     * Inserts the participant info about each bot into the MatchConfig. The Bots in the array must be BotFromConfigs.
     */
    private static void insertParticipantsFromTeam(MatchConfig config, ArrayList<Bot> bots, TeamColor color) {
        for (Bot bot : bots) {
            ParticipantInfo participant = new ParticipantInfo(
                    bot.getBotSkill(),
                    bot.getBotType(),
                    color,
                    ((BotFromConfig) bot).getConfig()
            );
            config.addParticipant(participant);
        }
    }
}
