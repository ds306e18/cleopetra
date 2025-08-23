package dk.aau.cs.ds306e18.tournament.rlbot;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.LoadoutConfigs;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfig;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfigOptions;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.TeamColor;
import rlbot.commons.protocol.RLBotInterface;
import rlbot.commons.protocol.RLBotListenerAdapter;
import rlbot.flat.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MatchControl extends RLBotListenerAdapter {

    private final static MatchControl INSTANCE = new MatchControl();

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
        var match = createRLBotMatch(matchConfig, series);
        launchConnectAndRunRLBotIfNeeded();
        rlbot.startMatch(match);
        hasLatestScore = false;
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
        // TODO
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

    // Called whenever we receive a packet from rlbot
    @Override
    public void onGamePacket(GamePacketT packet) {
        latestBlueScore = (int) packet.getTeams()[0].getScore();
        latestOrangeScore = (int) packet.getTeams()[1].getScore();
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
