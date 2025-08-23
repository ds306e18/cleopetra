package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfigOptions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MatchConfigTest {

    private static final String MATCH_CONFIG_FILE = "src/test/rlbot.cfg";
    private static final String MATCH_CONFIG_FILE_OUT = "src/test/_out_rlbot.cfg";

    @Test
    public void read01() throws IOException {
        MatchConfig config = new MatchConfig(new File(MATCH_CONFIG_FILE));

        // Match settings
        assertEquals(GameMap.MANNFIELD, config.getGameMap());
        assertEquals(GameMode.SOCCER, config.getGameMode());
        assertFalse(config.doSkipReplays());
        assertFalse(config.isInstantStart());

        // Mutators
        assertEquals(MatchLength.UNLIMITED, config.getMatchLength());
        assertEquals(MaxScore.UNLIMITED, config.getMaxScore());
        assertEquals(Overtime.UNLIMITED, config.getOvertime());
        assertEquals(BallMaxSpeed.DEFAULT, config.getBallMaxSpeed());
        assertEquals(GameSpeed.DEFAULT, config.getGameSpeed());
        assertEquals(BallType.DEFAULT, config.getBallType());
        assertEquals(BallWeight.DEFAULT, config.getBallWeight());
        assertEquals(BallSize.DEFAULT, config.getBallSize());
        assertEquals(BallBounciness.DEFAULT, config.getBallBounciness());
        assertEquals(BoostAmount.DEFAULT, config.getBoostAmount());
        assertEquals(BoostStrength.TIMES_ONE, config.getBoostStrength());
        assertEquals(RumblePowers.NONE, config.getRumblePowers());
        assertEquals(Gravity.DEFAULT, config.getGravity());
        assertEquals(Demolish.DEFAULT, config.getDemolish());
        assertEquals(RespawnTime.THREE_SECONDS, config.getRespawnTime());

        // Participants
        // We won't test the BotConfigs here, only the other stuff in ParticipantInfo
        List<ParticipantInfo> participants = config.getParticipants();
        assertEquals(2, participants.size());
        assertEquals(BotSkill.ALLSTAR, participants.get(0).getSkill());
        assertEquals(BotType.RLBOT, participants.get(0).getType());
        assertEquals(TeamColor.BLUE, participants.get(0).getTeam());
        assertEquals(BotSkill.ALLSTAR, participants.get(1).getSkill());
        assertEquals(BotType.RLBOT, participants.get(1).getType());
        assertEquals(TeamColor.BLUE, participants.get(1).getTeam());
    }

    @Test
    public void readWrite01() throws IOException {
        MatchConfig config = new MatchConfig(new File(MATCH_CONFIG_FILE));
        config.clearParticipants(); // Clear since the bot configs are irrelevant
        config.write(new File(MATCH_CONFIG_FILE_OUT));

        // Load same file again and compare before and after
        MatchConfig config2 = new MatchConfig(new File(MATCH_CONFIG_FILE_OUT));
        assertEquals(config, config2);
    }
}
