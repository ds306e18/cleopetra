package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import dk.aau.cs.ds306e18.tournament.model.BotSkill;
import dk.aau.cs.ds306e18.tournament.model.BotType;
import dk.aau.cs.ds306e18.tournament.rlbot.TeamColor;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfigOptions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class from interfacing with a match config (usually called rlbot.cfg).
 */
public class MatchConfig {

    public final static String MATCH_CONFIGURATION_HEADER = "Match Configuration";
    public final static String PARTICIPANT_COUNT_KEY = "num_participants";
    public final static String GAME_MODE = "game_mode";
    public final static String GAME_MAP = "game_map";
    public final static String SKIP_REPLAYS = "skip_replays";
    public final static String INSTANT_START = "start_without_countdown";
    public final static String EXISTING_MATCH_BEHAVIOR = "existing_match_behavior";

    public final static String PARTICIPANTS_CONFIGURATION_HEADER = "Participant Configuration";
    public final static String PARTICIPANT_CONFIG_INDEXED = "participant_config_";
    public final static String PARTICIPANT_TEAM_INDEXD = "participant_team_";
    public final static String PARTICIPANT_TYPE_INDEXD = "participant_type_";
    public final static String PARTICIPANT_SKILL_INDEXD = "participant_bot_skill_";

    public final static String MUTATOR_CONFIGURATION_HEADER = "Mutator Configuration";
    public final static String MUTATOR_MATCH_LENGTH = "Match Length";
    public final static String MUTATOR_MAX_SCORE = "Max Score";
    public final static String MUTATOR_OVERTIME = "Overtime";
    public final static String MUTATOR_SERIES_LENGTH = "Series Length";
    public final static String MUTATOR_GAME_SPEED = "Game Speed";
    public final static String MUTATOR_BALL_MAX_SPEED = "Ball Max Speed";
    public final static String MUTATOR_BALL_TYPE = "Ball Type";
    public final static String MUTATOR_BALL_WEIGHT = "Ball Weight";
    public final static String MUTATOR_BALL_SIZE = "Ball Size";
    public final static String MUTATOR_BALL_BOUNCINESS = "Ball Bounciness";
    public final static String MUTATOR_BOOST_AMOUNT = "Boost Amount";
    public final static String MUTATOR_RUMBLE = "Rumble";
    public final static String MUTATOR_BOOST_STRENGTH = "Boost Strength";
    public final static String MUTATOR_GRAVITY = "Gravity";
    public final static String MUTATOR_DEMOLISH = "Demolish";
    public final static String MUTATOR_RESPAWN_TIME = "Respawn Time";

    private File configFile;
    private GameMap gameMap = GameMap.CHAMPIONS_FIELD;
    private GameMode gameMode = GameMode.SOCCER;
    private final List<ParticipantInfo> participants = new ArrayList<>();
    // TODO Mutators

    public MatchConfig() {

    }

    public MatchConfig(File configFile) throws IOException {
        this.configFile = configFile;
        ConfigFile config = new ConfigFile(configFile);

        // Load match settings
        gameMap = GameMap.get(config.getString(MATCH_CONFIGURATION_HEADER, GAME_MAP, gameMap.configName));
        gameMode = GameMode.get(config.getString(MATCH_CONFIGURATION_HEADER, GAME_MODE, gameMode.configName));
        int numParticipants = config.getInt(MATCH_CONFIGURATION_HEADER, PARTICIPANT_COUNT_KEY, 2);

        // Load participants
        for (int i = 0; i < numParticipants; i++) {
            // Load the bot config file. It something fails, skip this bot.
            BotConfig botConfig;
            try {
                File botConfigFile = new File(config.getString(PARTICIPANTS_CONFIGURATION_HEADER, PARTICIPANT_CONFIG_INDEXED, ""));
                if (!botConfigFile.isAbsolute()) {
                    botConfigFile = new File(configFile.getParentFile(), botConfigFile.toString());
                }
                botConfig = new BotConfig(botConfigFile);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            // Load other participant info
            BotSkill skill = BotSkill.getSkillFromNumber(config.getDouble(PARTICIPANTS_CONFIGURATION_HEADER, PARTICIPANT_SKILL_INDEXD, 1.0));
            BotType type = BotType.getTypeFromConfigValue(config.getString(PARTICIPANTS_CONFIGURATION_HEADER, PARTICIPANT_TYPE_INDEXD, "rlbot"));
            TeamColor color = TeamColor.getFromInt(config.getInt(PARTICIPANTS_CONFIGURATION_HEADER, PARTICIPANT_TEAM_INDEXD, 0));

            ParticipantInfo participant = new ParticipantInfo(skill, type, color, botConfig);
            addParticipant(participant);
        }
    }

    /**
     * Write this MatchConfig to the given file.
     * @param file
     * @throws IOException
     */
    public void write(File file) throws IOException {
        this.configFile = file;
        ConfigFile config = new ConfigFile();

        config.hasSection(MATCH_CONFIGURATION_HEADER);
        config.set(MATCH_CONFIGURATION_HEADER, GAME_MAP, gameMap.configName);
        config.set(MATCH_CONFIGURATION_HEADER, GAME_MODE, gameMode.configName);
        config.set(MATCH_CONFIGURATION_HEADER, PARTICIPANT_COUNT_KEY, participants.size());

        config.createSection(PARTICIPANTS_CONFIGURATION_HEADER);
        for (int i = 0; i < participants.size(); i++) {
            ParticipantInfo participant = participants.get(i);
            config.set(PARTICIPANTS_CONFIGURATION_HEADER, PARTICIPANT_CONFIG_INDEXED + i, participant.getConfig().getConfigFile().toString());
            config.set(PARTICIPANTS_CONFIGURATION_HEADER, PARTICIPANT_TYPE_INDEXD + i, participant.getType().getConfigValue());
            config.set(PARTICIPANTS_CONFIGURATION_HEADER, PARTICIPANT_SKILL_INDEXD + i, participant.getSkill().getConfigValue());
            config.set(PARTICIPANTS_CONFIGURATION_HEADER, PARTICIPANT_TEAM_INDEXD + i, participant.getTeam().getConfigValue());
        }

        config.write(file);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public List<ParticipantInfo> getParticipants() {
        return participants;
    }

    public void addParticipant(ParticipantInfo participant) {
        participants.add(participant);
    }

    public void clearParticipants() {
        participants.clear();
    }

    public File getConfigFile() {
        return configFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchConfig that = (MatchConfig) o;
        return Objects.equals(gameMap, that.gameMap) &&
                Objects.equals(gameMode, that.gameMode) &&
                Objects.equals(participants, that.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameMap, gameMode, participants);
    }
}
