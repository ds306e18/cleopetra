package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

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

    public final static String PARTICIPANTS_CONFIGURATION_HEADER = "Participant Configuration";
    public final static String PARTICIPANT_CONFIG_INDEXED = "participant_config_";
    public final static String PARTICIPANT_TEAM_INDEXD = "participant_team_";
    public final static String PARTICIPANT_TYPE_INDEXD = "participant_type_";
    public final static String PARTICIPANT_SKILL_INDEXD = "participant_bot_skill_";

    public final static String MUTATOR_CONFIGURATION_HEADER = "Mutator Configuration";
    public final static String MUTATOR_MATCH_LENGTH = "Match Length";
    public final static String MUTATOR_MAX_SCORE = "Max Score";
    public final static String MUTATOR_OVERTIME = "Overtime";
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
    private boolean skipReplays = false;
    private boolean instantStart = false;
    private final List<ParticipantInfo> participants = new ArrayList<>();

    private MatchLength matchLength = MatchLength.FIVE_MINUTES;
    private MaxScore maxScore = MaxScore.UNLIMITED;
    private Overtime overtime = Overtime.UNLIMITED;
    private GameSpeed gameSpeed = GameSpeed.DEFAULT;
    private BallMaxSpeed ballMaxSpeed = BallMaxSpeed.DEFAULT;
    private BallType ballType = BallType.DEFAULT;
    private BallWeight ballWeight = BallWeight.DEFAULT;
    private BallSize ballSize = BallSize.DEFAULT;
    private BallBounciness ballBounciness = BallBounciness.DEFAULT;
    private BoostAmount boostAmount = BoostAmount.DEFAULT;
    private BoostStrength boostStrength = BoostStrength.TIMES_ONE;
    private RumblePowers rumblePowers = RumblePowers.NONE;
    private Gravity gravity = Gravity.DEFAULT;
    private Demolish demolish = Demolish.DEFAULT;
    private RespawnTime respawnTime = RespawnTime.THREE_SECONDS;

    public MatchConfig() {

    }

    public MatchConfig(File configFile) throws IOException {
        this.configFile = configFile;
        ConfigFile config = new ConfigFile(configFile);

        // Load match settings
        gameMap = GameMap.get(config.getString(MATCH_CONFIGURATION_HEADER, GAME_MAP, gameMap.configName));
        gameMode = GameMode.get(config.getString(MATCH_CONFIGURATION_HEADER, GAME_MODE, gameMode.configName));
        skipReplays = config.getBoolean(MUTATOR_CONFIGURATION_HEADER, SKIP_REPLAYS, skipReplays);
        instantStart = config.getBoolean(MUTATOR_CONFIGURATION_HEADER, INSTANT_START, instantStart);

        // Load mutators
        matchLength = MatchLength.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_MATCH_LENGTH, matchLength.configName));
        maxScore = MaxScore.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_MAX_SCORE, maxScore.configName));
        overtime = Overtime.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_OVERTIME, overtime.configName));
        ballMaxSpeed = BallMaxSpeed.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BALL_MAX_SPEED, ballMaxSpeed.configName));
        gameSpeed = GameSpeed.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_GAME_SPEED, gameSpeed.configName));
        ballType = BallType.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BALL_TYPE, ballType.configName));
        ballWeight = BallWeight.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BALL_WEIGHT, ballWeight.configName));
        ballSize = BallSize.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BALL_SIZE, ballSize.configName));
        ballBounciness = BallBounciness.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BALL_BOUNCINESS, ballBounciness.configName));
        boostAmount = BoostAmount.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BOOST_AMOUNT, boostAmount.configName));
        boostStrength = BoostStrength.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BOOST_STRENGTH, boostStrength.configName));
        rumblePowers = RumblePowers.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_RUMBLE, rumblePowers.configName));
        gravity = Gravity.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_GRAVITY, gravity.configName));
        demolish = Demolish.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_DEMOLISH, demolish.configName));
        respawnTime = RespawnTime.get(config.getString(MUTATOR_CONFIGURATION_HEADER, MUTATOR_RESPAWN_TIME, respawnTime.configName));

        // Load participants
        int numParticipants = config.getInt(MATCH_CONFIGURATION_HEADER, PARTICIPANT_COUNT_KEY, 2);
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

        // Match settings
        config.createSection(MATCH_CONFIGURATION_HEADER);
        config.set(MATCH_CONFIGURATION_HEADER, GAME_MAP, gameMap.configName);
        config.set(MATCH_CONFIGURATION_HEADER, GAME_MODE, gameMode.configName);
        config.set(MATCH_CONFIGURATION_HEADER, SKIP_REPLAYS, skipReplays);
        config.set(MATCH_CONFIGURATION_HEADER, INSTANT_START, instantStart);
        config.set(MATCH_CONFIGURATION_HEADER, PARTICIPANT_COUNT_KEY, participants.size());

        // Mutators
        config.createSection(MUTATOR_CONFIGURATION_HEADER);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_MATCH_LENGTH, matchLength.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_MAX_SCORE, maxScore.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_OVERTIME, overtime.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_GAME_SPEED, gameSpeed.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BALL_MAX_SPEED, ballMaxSpeed.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BALL_TYPE, ballType.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BALL_WEIGHT, ballWeight.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BALL_SIZE, ballSize.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BALL_BOUNCINESS, ballBounciness.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BOOST_AMOUNT, boostAmount.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_BOOST_STRENGTH, boostStrength.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_RUMBLE, rumblePowers.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_GRAVITY, gravity.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_DEMOLISH, demolish.configName);
        config.set(MUTATOR_CONFIGURATION_HEADER, MUTATOR_RESPAWN_TIME, respawnTime.configName);
        
        // Participants
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

    public boolean isSkipReplays() {
        return skipReplays;
    }

    public void setSkipReplays(boolean skipReplays) {
        this.skipReplays = skipReplays;
    }

    public boolean isInstantStart() {
        return instantStart;
    }

    public void setInstantStart(boolean instantStart) {
        this.instantStart = instantStart;
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

    public MatchLength getMatchLength() {
        return matchLength;
    }

    public void setMatchLength(MatchLength matchLength) {
        this.matchLength = matchLength;
    }

    public MaxScore getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(MaxScore maxScore) {
        this.maxScore = maxScore;
    }

    public Overtime getOvertime() {
        return overtime;
    }

    public void setOvertime(Overtime overtime) {
        this.overtime = overtime;
    }

    public GameSpeed getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(GameSpeed gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    public BallMaxSpeed getBallMaxSpeed() {
        return ballMaxSpeed;
    }

    public void setBallMaxSpeed(BallMaxSpeed ballMaxSpeed) {
        this.ballMaxSpeed = ballMaxSpeed;
    }

    public BallType getBallType() {
        return ballType;
    }

    public void setBallType(BallType ballType) {
        this.ballType = ballType;
    }

    public BallWeight getBallWeight() {
        return ballWeight;
    }

    public void setBallWeight(BallWeight ballWeight) {
        this.ballWeight = ballWeight;
    }

    public BallSize getBallSize() {
        return ballSize;
    }

    public void setBallSize(BallSize ballSize) {
        this.ballSize = ballSize;
    }

    public BallBounciness getBallBounciness() {
        return ballBounciness;
    }

    public void setBallBounciness(BallBounciness ballBounciness) {
        this.ballBounciness = ballBounciness;
    }

    public BoostAmount getBoostAmount() {
        return boostAmount;
    }

    public void setBoostAmount(BoostAmount boostAmount) {
        this.boostAmount = boostAmount;
    }

    public BoostStrength getBoostStrength() {
        return boostStrength;
    }

    public void setBoostStrength(BoostStrength boostStrength) {
        this.boostStrength = boostStrength;
    }

    public RumblePowers getRumblePowers() {
        return rumblePowers;
    }

    public void setRumblePowers(RumblePowers rumblePowers) {
        this.rumblePowers = rumblePowers;
    }

    public Gravity getGravity() {
        return gravity;
    }

    public void setGravity(Gravity gravity) {
        this.gravity = gravity;
    }

    public Demolish getDemolish() {
        return demolish;
    }

    public void setDemolish(Demolish demolish) {
        this.demolish = demolish;
    }

    public RespawnTime getRespawnTime() {
        return respawnTime;
    }

    public void setRespawnTime(RespawnTime respawnTime) {
        this.respawnTime = respawnTime;
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
