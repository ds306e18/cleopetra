package dk.aau.cs.ds306e18.tournament.rlbot;

import com.google.flatbuffers.FlatBufferBuilder;
import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotFromConfig;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import dk.aau.cs.ds306e18.tournament.utility.configuration.RLBotConfig;
import rlbot.cppinterop.RLBotDll;
import rlbot.flat.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MatchRunner {

    private static boolean dllInitialized = false;

    /**
     * Setup and prepare communication with the RLBotDll.
     */
    public static void setupRLBotFramework() {
        if (!dllInitialized) {
            try {
                // The DllPathFetcher.py script prints the location of the RLBot interface dll
                // A python process runs the script, and the result is read from the associated input stream
                String dllFetcher = Paths.get(Main.class.getResource("rlbot/DllPathFetcher.py").toURI()).toString();
                ProcessBuilder pb = new ProcessBuilder("python", dllFetcher);
                Process process = pb.start();
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String pathToDll = in.readLine();

                // Init the RLBot interface
                RLBotDll.initialize(pathToDll);

                dllInitialized = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Starts the given match in Rocket League. */
    public static boolean startMatch(RLBotSettings settings, Match match) {

        // Checks settings and modifies rlbot.cfg file if everything is okay
        boolean ready = true;//prepareMatch(settings, match);
        if (!ready) {
            return false;
        }

        try {
            setupRLBotFramework();

            // Build a flatbuffer packet with the info needed
            FlatBufferBuilder builder = new FlatBufferBuilder();
            int bluePlayers = match.getBlueTeam().size();
            int orangePlayers = match.getOrangeTeam().size();
            int playerCount = bluePlayers + orangePlayers;
            int[] players = new int[playerCount];
            for (int i = 0; i < playerCount; i++) {
                boolean isBlue = i < bluePlayers;
                Bot bot = isBlue ?
                        match.getBlueTeam().getBots().get(i) :
                        match.getOrangeTeam().getBots().get(i - bluePlayers);

                // TODO Loadout and paint will just be all 0's for now. Should be config from appearance.cfg
                int loadoutPaintOffset = LoadoutPaint.createLoadoutPaint(builder,
                        0, 0, 0, 0, 0, 0, 0, 0);
                int loadoutOffset = PlayerLoadout.createPlayerLoadout(builder,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,loadoutPaintOffset);

                players[i] = PlayerConfiguration.createPlayerConfiguration(
                        builder,
                        bot.getBotType().getFlatBufferBotType(),
                        bot.getFlatBufferBotInfo(builder),
                        builder.createString(bot.getName()),
                        isBlue ? 1 : 0,
                        loadoutOffset
                );
            }

            // Default mutators
            int mutatorsOffset = MutatorSettings.createMutatorSettings(
                    builder,
                    MatchLength.Five_Minutes,
                    MaxScore.Unlimited,
                    OvertimeOption.Unlimited,
                    SeriesLengthOption.Unlimited,
                    GameSpeedOption.Default,
                    BallMaxSpeedOption.Default,
                    BallTypeOption.Default,
                    BallWeightOption.Default,
                    BallSizeOption.Default,
                    BallBouncinessOption.Default,
                    BoostOption.Normal_Boost,
                    RumbleOption.No_Rumble,
                    BoostStrengthOption.One,
                    GravityOption.Default,
                    DemolishOption.Default,
                    RespawnTimeOption.Three_Seconds);

            int root = MatchSettings.createMatchSettings(builder,
                    MatchSettings.createPlayerConfigurationsVector(builder, players),
                    GameMode.Soccer,
                    GameMap.DFHStadium,
                    false,
                    false,
                    mutatorsOffset,
                    ExistingMatchBehavior.Restart);

            builder.finish(root);
            RLBotDll.startMatch(builder);
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
        // Settings are always okay right now
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
    public static boolean prepareMatch(RLBotSettings settings, Match match) {
        try {
            // Check settings and config files
            checkMatch(settings, match);

            // Set up rlbot config file
            RLBotConfig rlBotConfig = new RLBotConfig(settings.getConfigPath());
            // Utilise return of setupMatch for syntax validation
            if (!rlBotConfig.setupMatch(match)) {
                Alerts.errorNotification("Error occurred while configuring match", "Syntax of config file is not valid!");
                return false;
            }
            rlBotConfig.writeConfig(settings.getConfigPath());
            return true;

        } catch (IllegalStateException e) {
            Alerts.errorNotification("Error occurred while configuring match", e.getMessage());
            return false;
        }
    }
}
