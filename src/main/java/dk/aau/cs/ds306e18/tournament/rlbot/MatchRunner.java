package dk.aau.cs.ds306e18.tournament.rlbot;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotFromConfig;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * This class maintains a RLBot runner process for starting matches. The runner process is the run.py running in
 * a separate command prompt. Communication between CleoPetra and the RLBot runner happens through a socket and
 * consists of simple commands like START, STOP, EXIT, which are defined in the subclass Command. If the RLBot runner
 * is not running when a command is issued, a new instance of the RLBot runner is started.
 */
public class MatchRunner {

    // The first %s will be replaced with the directory of the rlbot.cfg.
    // The second %s will be the drive 'C:' to change drive.
    // The third %s is the python installation path.
    private static final String COMMAND_FORMAT = "cmd.exe /c start cmd /c \"cd %s & %s & \"%s\" run.py %s\"";
    private static final String ADDR = "127.0.0.1";
    private static final int PORT = 35353; // TODO Make user able to change the port in a settings file

    public static Optional<Integer> latestBlueScore = Optional.empty();
    public static Optional<Integer> latestOrangeScore = Optional.empty();

    private enum Command {
        START("START"), // Start the match described by the rlbot.cfg
        STOP("STOP"), // Stop the current match and all bot pids
        EXIT("EXIT"), // Close the run.py process
        FETCH("FETCH"); // Fetch the scores of the match

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
    public static boolean startMatch(MatchConfig matchConfig, Series series) {

        // Checks settings and modifies rlbot.cfg file if everything is okay
        boolean ready = prepareMatch(matchConfig, series);
        if (!ready) {
            return false;
        }

        Main.LOGGER.log(System.Logger.Level.INFO, "Starting match in RLBot.");
        return sendCommandToRLBot(Command.START, true);
    }

    /**
     * Starts the RLBot runner, aka. the run.py, as a separate process in a separate cmd.
     * The runner might not be ready to accept commands immediately. This method returns true on success.
     */
    public static boolean startRLBotRunner() {
        try {
            Alerts.infoNotification("Starting RLBot runner", "Attempting to start new instance of run.py for running matches.");
            Main.LOGGER.log(System.Logger.Level.INFO, "Starting RLBot runner. Attempting to start new instance of run.py for running matches.");

            String python = "python";
            if (Tournament.get().getRlBotSettings().useRLBotGUIPythonIfAvailable()) {
                // Find python installation in RLBotGUI
                Path botPackPython = RLBotInstallation.getPathToPython();
                if (botPackPython != null && Files.exists(botPackPython)) {
                    python = botPackPython.toString();
                    Main.LOGGER.log(System.Logger.Level.INFO, "Found RLBotGUI python installation: " + python);
                }
            }

            Path pathToDirectory = SettingsDirectory.RUN_PY.getParent();
            String cmd = String.format(COMMAND_FORMAT, pathToDirectory, pathToDirectory.toString().substring(0, 2), python, Tournament.get().getRlBotSettings().getOverlayPath());
            Main.LOGGER.log(System.Logger.Level.INFO, "Running command: " + cmd);
            Runtime.getRuntime().exec(cmd);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Alerts.errorNotification("Could not start RLBot runner", "Something went wrong starting the run.py.");
            Main.LOGGER.log(System.Logger.Level.ERROR, "Could not start RLBot runner. Something went wrong starting the run.py.");
            return false;
        }
    }

    public static boolean fetchScores() {
        return sendCommandToRLBot(Command.FETCH, false);
    }

    /**
     * Closes the RLBot runner.
     */
    public static void closeRLBotRunner() {
        // If the command fails, the runner is probably not running anyway, so ignore any errors.
        sendCommandToRLBot(Command.EXIT, false);
    }

    /**
     * Stops the current match.
     */
    public static void stopMatch() {
        sendCommandToRLBot(Command.STOP, false);
    }

    /**
     * Sends the given command to the RLBot runner. If the runner does not respond, this method will optionally
     * start a new RLBot runner instance and retry sending the command. If we believe the command was send
     * and received, this method returns true, otherwise false.
     */
    private static boolean sendCommandToRLBot(Command cmd, boolean startRLBotIfMissingAndRetry) {
        Main.LOGGER.log(System.Logger.Level.INFO, "Sending command to RLBot runner: " + cmd.toString());
        latestBlueScore = Optional.empty();
        latestOrangeScore = Optional.empty();
        try (Socket sock = new Socket(ADDR, PORT);
            PrintWriter writer = new PrintWriter(sock.getOutputStream(), true)) {
            writer.print(cmd.toString());
            writer.flush();

            Thread.sleep(80); // Fetching scores may take up to 60 ms

            // Parse answer
            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String answer = reader.readLine();
            if ("OK".equals(answer))
                return true;
            else if ("ERR".equals(answer))
                return false;
            else {
                // We fetched the scoreline then
                try {
                    String[] split = answer.split(",");
                    int blueScore = Integer.parseInt(split[0]);
                    int orangeScore = Integer.parseInt(split[1]);
                    latestBlueScore = Optional.of(blueScore);
                    latestOrangeScore = Optional.of(orangeScore);
                    return true;
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Unable to parse answer from run.py. \"" + answer + "\"", ex);
                }
            }
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
            Main.LOGGER.log(System.Logger.Level.ERROR, "Failed to open socket and send message to run.py");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns true if the given match can be started.
     */
    public static boolean canStartMatch(Series series) {
        try {
            checkMatch(series);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Check the requirements for starting the match. Throws an IllegalStateException if anything is wrong. Does nothing
     * if everything is okay.
     */
    private static void checkMatch(Series series) {
        // These methods throws IllegalStageException if anything is wrong
        checkBotConfigsInMatch(series);
    }

    /**
     * Throws an IllegalStateException if anything is wrong with the bots' config files for this match. Does nothing if
     * all the bots on both teams have a valid config file in a given match.
     */
    private static void checkBotConfigsInMatch(Series series) {
        // Check if there are two teams
        if (series.getBlueTeam() == null) throw new IllegalStateException("There is no blue team for this match.");
        if (series.getOrangeTeam() == null) throw new IllegalStateException("There is no orange team for this match.");

        ArrayList<Bot> blueBots = series.getBlueTeam().getBots();
        ArrayList<Bot> orangeBots = series.getOrangeTeam().getBots();

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
    public static boolean prepareMatch(MatchConfig matchConfig, Series series) {
        try {
            // Check settings and config files
            checkMatch(series);
            insertParticipants(matchConfig, series);
            matchConfig.write(SettingsDirectory.MATCH_CONFIG.toFile());

            // Write overlay data
            RLBotSettings settings = Tournament.get().getRlBotSettings();
            if (settings.writeOverlayDataEnabled()) {
                try {
                    OverlayData overlayData = new OverlayData(series);
                    overlayData.write();
                } catch (IOException e) {
                    Path path = new File(settings.getOverlayPath(), OverlayData.CURRENT_MATCH_FILE_NAME).toPath();
                    Alerts.errorNotification("Could not write overlay data", "Failed to write overlay data to " + path);
                    Main.LOGGER.log(System.Logger.Level.ERROR, "Could not write overlay data to " + path);
                    e.printStackTrace();
                }
                try {
                    Path showVsFile = new File(settings.getOverlayPath(), "show_vs.json").toPath();
                    Files.write(showVsFile, "true".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Main.LOGGER.log(System.Logger.Level.INFO, "Updated match configuration (rlbot.cfg).");
            return true;
        } catch (IllegalStateException e) {
            Alerts.errorNotification("Error occurred while configuring match", e.getMessage());
            Main.LOGGER.log(System.Logger.Level.ERROR, "Error occurred while configuring match", e);
        } catch (IOException e) {
            Alerts.errorNotification("IO error occurred while configuring match", e.getMessage());
            Main.LOGGER.log(System.Logger.Level.ERROR, "IO error occurred while configuring match", e);
        }

        return false;
    }

    /**
     * Inserts the participants in a match into the MatchConfig. Any existing participants in the MatchConfig will be
     * removed.
     */
    private static void insertParticipants(MatchConfig config, Series series) {
        config.clearParticipants();

        boolean[] modes = { true, true, true };

        int set = (int)series.getTeamOneScores().stream().filter(Optional::isPresent).count();
        if (set == 0) {
            // First match of series. Exclude based on what teams played in previous matches
            for (int once = 0; once < 1; once++) {
                // Exclude game mode that team one played in previous match
                Series s = series.getTeamOneFromSeries();
                if (s == null) break;
                int len = (int)s.getTeamOneScores().stream().filter(Optional::isPresent).count();
                int mode = s.gameModes.get(len - 1);
                modes[mode - 1] = false;
                Main.LOGGER.log(System.Logger.Level.INFO, "Team " + series.getTeamOne().getTeamName() + " played " + mode  + "s last match");
            }
            for (int once = 0; once < 1; once++) {
                // Exclude game mode that team two played in previous match
                Series s = series.getTeamTwoFromSeries();
                if (s == null) break;
                int len = (int)s.getTeamTwoScores().stream().filter(Optional::isPresent).count();
                int mode = s.gameModes.get(len - 1);
                modes[mode - 1] = false;
                Main.LOGGER.log(System.Logger.Level.INFO, "Team " + series.getTeamTwo().getTeamName() + " played " + mode  + "s last match");
            }
        } else {
            // Not first match. Exclude based on previous matches in this series
            StringBuilder sb = new StringBuilder("Series has used these game modes so far: ");
            for (int i = 0; i < set; i++) {
                modes[series.gameModes.get(i) - 1] = false;
                if (i != 0) sb.append(" and ");
                sb.append(series.gameModes.get(i));
                sb.append('s');
            }
            Main.LOGGER.log(System.Logger.Level.INFO, sb.toString());
        }

        // Pick from remaining
        Random rng = new Random();
        int gm = ((rng.nextInt() % 3) + 3) % 3;
        while (!modes[gm]) gm = ((rng.nextInt() % 3) + 3) % 3;
        series.gameModes.set(set, gm + 1);
        Main.LOGGER.log(System.Logger.Level.INFO, "Game mode picked: " + (gm + 1) + "s");

        insertParticipantsFromTeam(config, series.getBlueTeam().getBots(), TeamColor.BLUE, gm + 1);
        insertParticipantsFromTeam(config, series.getOrangeTeam().getBots(), TeamColor.ORANGE, gm + 1);
    }

    /**
     * Inserts the participant info about each bot into the MatchConfig. The Bots in the array must be BotFromConfigs.
     */
    private static void insertParticipantsFromTeam(MatchConfig config, ArrayList<Bot> bots, TeamColor color, int n) {
        for (int i = 0; i < n; i++) {
            Bot bot = bots.get(i);
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
