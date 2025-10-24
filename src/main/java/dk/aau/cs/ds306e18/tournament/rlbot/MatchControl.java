package dk.aau.cs.ds306e18.tournament.rlbot;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.LoadoutConfigs;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfig;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfigOptions;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.TeamColor;
import dk.aau.cs.ds306e18.tournament.utility.OverlayData;
import rlbot.commons.protocol.RLBotInterface;
import rlbot.commons.protocol.RLBotListenerAdapter;
import rlbot.flat.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MatchControl extends RLBotListenerAdapter {

    private final static MatchControl INSTANCE = new MatchControl();

    private final static String SHOW_MERCY_FILE = "show_mercy.json";
    private final static String SHOW_VS_FILE = "show_vs.json";
    private final static int MERCY_RULE = 6; // TODO: Make into tournament option

    private int prevMatchPhase = MatchPhase.Inactive;
    private boolean hasLatestScore = false;
    private int latestBlueScore = 0;
    private int latestOrangeScore = 0;

    private final RLBotInterface rlbot;

    public MatchControl() {
        rlbot = new RLBotInterface(3);
        rlbot.addListener(this);
    }

    public static MatchControl get() {
        return INSTANCE;
    }

    public void startMatch(MatchConfig matchConfig, Series series) {
        hasLatestScore = false;
        var match = createRLBotMatch(matchConfig, series);
        launchConnectAndRunRLBotIfNeeded();
        if (Tournament.get().getRlBotSettings().writeOverlayDataEnabled()) {
            try {
                var data = new OverlayData(series);
                data.write();
                writeToVsFile(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        rlbot.stopMatch(false);
        prevMatchPhase = MatchPhase.Inactive;
//        try {
//            Thread.sleep(8000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        rlbot.startMatch(match);
    }

    public void stopMatch() {
        launchConnectAndRunRLBotIfNeeded();
        rlbot.stopMatch(false);
    }

    public void launchConnectAndRunRLBotIfNeeded() {
        rlbot.tryLaunchRLBotServer();
        rlbot.connectAsMatchHost();
        if (!rlbot.isRunning()) {
            rlbot.runInBackground();
        }
    }

    private MatchConfigurationT createRLBotMatch(MatchConfig matchConfig, Series series) {
        var match = new MatchConfigurationT();
        // RLBot settings
        match.setLauncher(Launcher.Steam);
        match.setLauncherArg("");
        match.setAutoStartAgents(true);
        match.setWaitForAgents(true);
        match.setGameMode(matchConfig.getGameMode().value);

        // Match details
        var map = matchConfig.getGameMap() == MatchConfigOptions.GameMap.RANDOM_STANDARD ? MatchConfigOptions.GameMap.getRandomStandardMap() : matchConfig.getGameMap();
        match.setGameMapUpk(map.configName);
        match.setSkipReplays(matchConfig.doSkipReplays());
        match.setInstantStart(matchConfig.isInstantStart());
        match.setExistingMatchBehavior(ExistingMatchBehavior.Restart);
        match.setEnableRendering(matchConfig.isRenderingEnabled() ? DebugRendering.OnByDefault : DebugRendering.AlwaysOff);
        match.setEnableStateSetting(matchConfig.isStateSettingEnabled());
        match.setAutoSaveReplay(matchConfig.doAutoSaveReplays());
        match.setFreeplay(false);

        // Players
        var players = new ArrayList<PlayerConfigurationT>();
        insertPlayersFromTeam(players, series.getBlueTeam().getBots(), TeamColor.BLUE);
        insertPlayersFromTeam(players, series.getOrangeTeam().getBots(), TeamColor.ORANGE);
        match.setPlayerConfigurations(players.toArray(new PlayerConfigurationT[0]));

        // Scripts
        match.setScriptConfigurations(new rlbot.flat.ScriptConfigurationT[0]);

        // Mutators
        match.setMutators(new MutatorSettingsT());

        return match;
    }

    private void insertPlayersFromTeam(List<PlayerConfigurationT> players, List<Bot> bots, TeamColor team) {
        for (Bot bot : bots) {
            var player = new PlayerConfigurationT();
            player.setPlayerId(0);
            player.setTeam(team.getConfigValue());
            var variety = new PlayerClassUnion();
            switch (bot.getBotType()) {
                case HUMAN:
                    variety.setType(PlayerClass.Human);
                    variety.setValue(new HumanT());
                    break;
                case RLBOT:
                    var custom = new CustomBotT();
                    custom.setAgentId(bot.getAgentId());
                    custom.setName(bot.getName());
                    custom.setRootDir(bot.getRootDir());
                    custom.setRunCommand(bot.getRunCommand());
                    custom.setHivemind(bot.isHivemind());
                    if (!bot.getLoadoutFile().isBlank()) {
                        var file = Paths.get(bot.getRootDir(), bot.getLoadoutFile());
                        try {
                            custom.setLoadout(LoadoutConfigs.tryLoad(file.toFile(), team));
                        } catch (IOException e) {
                            Main.LOGGER.log(System.Logger.Level.ERROR, "Failed to load loadout file for '" + bot.getName() + "': " + file.toAbsolutePath(), e);
                        }
                    }
                    variety.setType(PlayerClass.CustomBot);
                    variety.setValue(custom);
                    break;
                case PSYONIX:
                    var psyonix = new PsyonixBotT();
                    psyonix.setName(bot.getName());
                    psyonix.setBotSkill(bot.getBotSkill().getValue());
                    if (!bot.getLoadoutFile().isBlank()) {
                        var file = Paths.get(bot.getRootDir(), bot.getLoadoutFile());
                        try {
                            psyonix.setLoadout(LoadoutConfigs.tryLoad(file.toFile(), team));
                        } catch (IOException e) {
                            Main.LOGGER.log(System.Logger.Level.ERROR, "Failed to load loadout file for '" + bot.getName() + "': " + file.toAbsolutePath(), e);
                        }
                    }
                    variety.setType(PlayerClass.PsyonixBot);
                    variety.setValue(psyonix);
                    break;
            }
            player.setVariety(variety);
            players.add(player);
        }
    }

    /// Write a boolean to the mercy file. The value can be read by an overlay to
    /// display that a mercy-victory has happened.
    private void writeToMercyFile(boolean showMercy) {
        if (Tournament.get().getRlBotSettings().writeOverlayDataEnabled()) {
            var folder = Tournament.get().getRlBotSettings().getOverlayPath();
            Path path = new File(folder, SHOW_MERCY_FILE).toPath();
            try {
                Main.LOGGER.log(System.Logger.Level.INFO, "Updating mercy file (value: " + showMercy + ")");
                Files.write(path, String.valueOf(showMercy).getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /// Write a boolean to the VS file. The value can be read by an overlay to
    /// display detailed information from the match info.
    private void writeToVsFile(boolean showVs) {
        if (Tournament.get().getRlBotSettings().writeOverlayDataEnabled()) {
            var folder = Tournament.get().getRlBotSettings().getOverlayPath();
            Path path = new File(folder, SHOW_VS_FILE).toPath();
            try {
                Main.LOGGER.log(System.Logger.Level.INFO, "Updating VS file (value: " + showVs + ")");
                Files.write(path, String.valueOf(showVs).getBytes());
            } catch (IOException e) {}
        }
    }

    // Called whenever we receive a packet from rlbot
    @Override
    public void onGamePacket(GamePacketT packet) {
        hasLatestScore = packet.getMatchInfo().getSecondsElapsed() >= 1;
        int newBlueScore = (int) packet.getTeams()[0].getScore();
        int newOrangeScore = (int) packet.getTeams()[1].getScore();
        boolean goalScored = newBlueScore != latestBlueScore || newOrangeScore != latestOrangeScore;
        latestBlueScore = newBlueScore;
        latestOrangeScore = newOrangeScore;
        if (goalScored) {
            boolean mercy = Math.abs(latestBlueScore - latestOrangeScore) >= MERCY_RULE;
            writeToMercyFile(mercy);
        }
        int matchPhase = packet.getMatchInfo().getMatchPhase();
        if (matchPhase != prevMatchPhase && (matchPhase == MatchPhase.Countdown || matchPhase == MatchPhase.Active)) {
            writeToVsFile(false);
        }
        prevMatchPhase = matchPhase;
    }

    public int getLatestBlueScore() {
        return latestBlueScore;
    }

    public int getLatestOrangeScore() {
        return latestOrangeScore;
    }

    public boolean hasLatestScore() {
        return hasLatestScore;
    }
}
