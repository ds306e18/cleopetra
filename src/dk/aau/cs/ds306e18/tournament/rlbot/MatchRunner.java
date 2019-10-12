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

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This class maintains a RLBot runner process for starting matches. The runner process is the run.py running in
 * a separate command prompt. Communication between CleoPetra and the RLBot runner happens through a socket and
 * consists of simple commands like STOP, STOP, EXIT, which are defined in the subclass Command. If the RLBot runner
 * is not running when a command is issued, a new instance of the RLBot runner is started.
 */
public class MatchRunner {

    // The first %s will be replaced with the directory of the rlbot.cfg. The second %s will be the drive 'C:' to change drive.
    private static final String COMMAND_FORMAT = "cmd.exe /c start cmd /c \"cd %s & %s & python run.py\"";
    private static final String ADDR = "127.0.0.1";
    private static final int PORT = 35353; // TODO Make user able to change the port in a settings file

    private enum Command {
        START("START"), // Start the match described by the rlbot.cfg
        STOP("STOP"), // Stop the current match and all bot pids
        EXIT("EXIT"); // Close the run.py process

        private final String cmd;

        Command(String cmd) {
            this.cmd = cmd;
        }

        @Override
        public String toString() {
            return cmd;
        }
    }

    /**
     * Starts the given match in Rocket League.
     */
    public static boolean startMatch(MatchConfig matchConfig, Match match) {

        // Checks settings and modifies rlbot.cfg file if everything is okay
        boolean ready = prepareMatch(matchConfig, match);
        if (!ready) {
            return false;
        }

        // Write overlay data to current_match.json
        if (Tournament.get().getRlBotSettings().writeOverlayData()) {
            try {
                OverlayData.write(match);
            } catch (IOException e) {
                Alerts.errorNotification("Could not write overlay data", "Failed to write overlay data to " + OverlayData.CURRENT_MATCH_PATH);
                e.printStackTrace();
            }
        }

        return sendCommandToRLBot(Command.START, true);
    }

    /**
     * Start the RLBot runner in the run.py and will be a separate process in a separate cmd.
     * The runner might not be ready to accept commands immediately. This method returns true on success.
     */
    private static boolean startRLBotRunner() {
        try {
            Alerts.infoNotification("Starting RLBot runner", "Attempting to start new instance of run.py for running matches.");
            Path pathToDirectory = SettingsDirectory.RUN_PY.getParent();
            String cmd = String.format(COMMAND_FORMAT, pathToDirectory, pathToDirectory.toString().substring(0, 2));
            Runtime.getRuntime().exec(cmd);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Alerts.errorNotification("Could not start RLBot runner", "Something went wrong starting the run.py.");
            return false;
        }
    }

    /**
     * Sends the given command to the RLBot runner. If the runner does not respond, this method will optionally
     * start a new RLBot runner instance and retry sending the command. If we believe the command was send
     * and received, this method returns true, otherwise false.
     */
    private static boolean sendCommandToRLBot(Command cmd, boolean startRLBotIfMissingAndRetry) {
        try (Socket sock = new Socket(ADDR, PORT);
             PrintWriter writer = new PrintWriter(sock.getOutputStream(), true)) {
            writer.print(cmd.toString());
            writer.flush();
            return true;
        } catch (ConnectException e) {
            // The run.py did not respond. Starting a new instance if allowed
            if (startRLBotIfMissingAndRetry) {
                try {
                    startRLBotRunner();
                    Thread.sleep(200);
                    // Retry
                    return sendCommandToRLBot(cmd, false);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alerts.errorNotification("IO Exception", "Failed to open socket and send message to run.py");
        }
        return false;
    }

    /**
     * Returns true if the given match can be started.
     */
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
