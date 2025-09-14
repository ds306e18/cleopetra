package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfigOptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Internal version of the rlbot match configuration.
 */
public class MatchConfig {

    private GameMap gameMap = GameMap.RANDOM_STANDARD;
    private GameMode gameMode = GameMode.SOCCER;
    private boolean skipReplays = false;
    private boolean instantStart = false;
    private boolean renderingEnabled = false;
    private boolean stateSettingEnabled = false;
    private boolean autoSaveReplays = false;
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

    public boolean doSkipReplays() {
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

    public boolean isRenderingEnabled() {
        return renderingEnabled;
    }

    public void setRenderingEnabled(boolean renderingEnabled) {
        this.renderingEnabled = renderingEnabled;
    }

    public boolean isStateSettingEnabled() {
        return stateSettingEnabled;
    }

    public void setStateSettingEnabled(boolean stateSettingEnabled) {
        this.stateSettingEnabled = stateSettingEnabled;
    }

    public boolean doAutoSaveReplays() {
        return autoSaveReplays;
    }

    public void setAutoSaveReplays(boolean autoSaveReplays) {
        this.autoSaveReplays = autoSaveReplays;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchConfig that = (MatchConfig) o;
        return skipReplays == that.skipReplays &&
                instantStart == that.instantStart &&
                gameMap == that.gameMap &&
                gameMode == that.gameMode &&
                Objects.equals(participants, that.participants) &&
                matchLength == that.matchLength &&
                maxScore == that.maxScore &&
                overtime == that.overtime &&
                gameSpeed == that.gameSpeed &&
                ballMaxSpeed == that.ballMaxSpeed &&
                ballType == that.ballType &&
                ballWeight == that.ballWeight &&
                ballSize == that.ballSize &&
                ballBounciness == that.ballBounciness &&
                boostAmount == that.boostAmount &&
                boostStrength == that.boostStrength &&
                rumblePowers == that.rumblePowers &&
                gravity == that.gravity &&
                demolish == that.demolish &&
                respawnTime == that.respawnTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameMap, gameMode, skipReplays, instantStart, participants, matchLength,
                maxScore, overtime, gameSpeed, ballMaxSpeed, ballType, ballWeight, ballSize, ballBounciness,
                boostAmount, boostStrength, rumblePowers, gravity, demolish, respawnTime);
    }
}
