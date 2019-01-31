package dk.aau.cs.ds306e18.tournament.utility.configuration;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import org.junit.Test;

import java.util.ArrayList;

import static dk.aau.cs.ds306e18.tournament.utility.configuration.ConfigFileEditorTest.*;
import static junit.framework.TestCase.assertEquals;

public class RLBotConfigTest {

    /** Reads in test_rlbot.cfg, edits one parameter, and returns it for easy testing */
    private static RLBotConfig setupEditedRLBotConfig() {
        RLBotConfig rlBotConfig = new RLBotConfig(testDir + testRLBotConfigFilename);
        rlBotConfig.editLine("num_participants", "42");
        return rlBotConfig;
    }

    /**
     * Creates a simple Match with three bots on each team
     * @return created match
     */
    private static Match createTestMatch() {
        Bot blueBot1 = new Bot("Blue1", "BlueDev1", "path/blue/1");
        Bot blueBot2 = new Bot("Blue2", "BlueDev2", "path/blue/2");
        Bot blueBot3 = new Bot("Blue3", "BlueDev3", "path/blue/3");

        ArrayList<Bot> blueBots = new ArrayList<>();
        blueBots.add(blueBot1);
        blueBots.add(blueBot2);
        blueBots.add(blueBot3);

        Team blueTeam = new Team("BlueTeam", blueBots, 0, "BlueDescription");

        Bot orangeBot1 = new Bot("Orange1", "OrangeDev1", "path/orange/1");
        Bot orangeBot2 = new Bot("Orange2", "OrangeDev2", "path/orange/2");
        Bot orangeBot3 = new Bot("Orange3", "OrangeDev3", "path/orange/3");

        ArrayList<Bot> orangeBots = new ArrayList<>();
        orangeBots.add(orangeBot1);
        orangeBots.add(orangeBot2);
        orangeBots.add(orangeBot3);

        Team orangeTeam = new Team("OrangeTeam", orangeBots, 1, "OrangeDescription");

        return new Match(blueTeam, orangeTeam);
    }

    @Test
    public void setupMatchTest() {
        RLBotConfig rlBotConfig = new RLBotConfig(testDir + testRLBotConfigFilename);
        Match match = createTestMatch();
        rlBotConfig.setupMatch(match);

        assertEquals("6", rlBotConfig.getValueOfLine("num_participant"));

        // checking correctly set paths
        assertEquals(match.getBlueTeam().getBots().get(0).getConfigPath(),
                rlBotConfig.getValueOfLine("participant_config_0"));
        assertEquals(match.getBlueTeam().getBots().get(1).getConfigPath(),
                rlBotConfig.getValueOfLine("participant_config_1"));
        assertEquals(match.getBlueTeam().getBots().get(2).getConfigPath(),
                rlBotConfig.getValueOfLine("participant_config_2"));

        assertEquals(match.getOrangeTeam().getBots().get(0).getConfigPath(),
                rlBotConfig.getValueOfLine("participant_config_3"));
        assertEquals(match.getOrangeTeam().getBots().get(1).getConfigPath(),
                rlBotConfig.getValueOfLine("participant_config_4"));
        assertEquals(match.getOrangeTeam().getBots().get(2).getConfigPath(),
                rlBotConfig.getValueOfLine("participant_config_5"));

        // checking correctly set team positions
        for (int i = 0; i < match.getBlueTeam().size(); i++) {
            assertEquals("0", rlBotConfig.getValueOfLine("participant_team_" + i));
        }

        for (int i = match.getBlueTeam().size(); i < match.getOrangeTeam().size(); i++) {
            assertEquals("1", rlBotConfig.getValueOfLine("participant_team_" + i));
        }
    }

    /**
     * Tests whether a newly written, and read config is identical to the written config. Redundant but tests call to
     * CFE write-method. Does not show up as coverage though.
     */
    @Test
    public void readWriteConcurrencyRLBotConfigTest() {
        // get and write edited RLBotConfig to filesystem
        RLBotConfig rlBotConfig = setupEditedRLBotConfig();
        rlBotConfig.writeConfig(testDir + testRLBotConfigTargetFilename);

        // assert that expected, edited config is equal to read config from filesystem
        assertEquals(setupEditedRLBotConfig().getConfig(), new RLBotConfig(testDir + testRLBotConfigTargetFilename).getConfig());

        deleteConfig(testDir + testRLBotConfigTargetFilename);
    }
}