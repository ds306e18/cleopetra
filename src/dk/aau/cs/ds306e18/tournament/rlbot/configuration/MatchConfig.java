package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private String gameMap = "ChampionsField";
    private String gameMode = "Soccer";
    private final List<ParticipantInfo> participants = new ArrayList<>();
    // TODO Mutators

    public void write(File file) throws IOException {
        ConfigFile config = new ConfigFile();

        config.containsSection(MATCH_CONFIGURATION_HEADER);
        config.set(MATCH_CONFIGURATION_HEADER, GAME_MAP, gameMap);
        config.set(MATCH_CONFIGURATION_HEADER, GAME_MODE, gameMode);
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

    public String getGameMap() {
        return gameMap;
    }

    public void setGameMap(String gameMap) {
        this.gameMap = gameMap;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
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
}
