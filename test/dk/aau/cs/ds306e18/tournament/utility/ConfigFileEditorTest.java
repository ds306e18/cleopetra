package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class ConfigFileEditorTest {

    private ArrayList<String> testConfig = new ArrayList<>();

    /**
     * Sets a simple testConfig for passing ConfigFileEditor for test-cases
     */
    private void setTestConfig() {
        testConfig.add("[Participant Configuration]");
        testConfig.add("participant_team_0 = 0");
        testConfig.add("participant_team_1 = 1");
    }

    /**
     * Creates a simple Match with three bots on each team
     *
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
    public void readWriteConcurrencyTest() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        ConfigFileEditor.writeConfig("dummyconfig.cfg");

        ConfigFileEditor.readConfig("dummyconfig.cfg");
        assertEquals(testConfig, ConfigFileEditor.getConfig());
    }

    @Test
    public void editLineTest() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);

        ConfigFileEditor.editLine("participant_team_", 0, "42");
        assertEquals("participant_team_0 = 42", ConfigFileEditor.getConfig().get(1));
    }

    @Test
    public void getLineTest1() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        assertEquals(null, ConfigFileEditor.getLine("NAN"));
    }

    @Test
    public void getLineTest2() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        assertEquals("participant_team_0 = 0", ConfigFileEditor.getLine("participant_team_0"));
    }

    @Test
    public void getLineTest3() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        assertEquals("participant_team_1 = 1", ConfigFileEditor.getLine("participant_team_1"));
    }

    @Test
    public void getLineTest4() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        assertEquals("[Participant Configuration]", ConfigFileEditor.getLine(0));
    }

    @Test
    public void getValueTest1() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        assertEquals("1", ConfigFileEditor.getValueOfLine("participant_team_1"));
    }

    @Test
    public void getValueTest2() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        assertEquals("0", ConfigFileEditor.getValueOfLine("participant_team_0"));
    }

    @Test
    public void getValueTest3() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        assertEquals("[Participant Configuration]", ConfigFileEditor.getValueOfLine("[Particip"));
    }

    @Test
    public void configureMatchTest1() {
        ConfigFileEditor.readConfig("testconfig.cfg");
        Match match = createTestMatch();
        ConfigFileEditor.configureMatch(match);


        assertEquals("6", ConfigFileEditor.getValueOfLine("num_participant"));

        // checking correctly set paths
        assertEquals(match.getBlueTeam().getBots().get(0).getConfigPath(),
                ConfigFileEditor.getValueOfLine("participant_config_0"));
        assertEquals(match.getBlueTeam().getBots().get(1).getConfigPath(),
                ConfigFileEditor.getValueOfLine("participant_config_1"));
        assertEquals(match.getBlueTeam().getBots().get(2).getConfigPath(),
                ConfigFileEditor.getValueOfLine("participant_config_2"));

        assertEquals(match.getOrangeTeam().getBots().get(0).getConfigPath(),
                ConfigFileEditor.getValueOfLine("participant_config_3"));
        assertEquals(match.getOrangeTeam().getBots().get(1).getConfigPath(),
                ConfigFileEditor.getValueOfLine("participant_config_4"));
        assertEquals(match.getOrangeTeam().getBots().get(2).getConfigPath(),
                ConfigFileEditor.getValueOfLine("participant_config_5"));

        // checking correctly set team positions
        for (int i = 0; i < match.getBlueTeam().size(); i++) {
            assertEquals("0", ConfigFileEditor.getValueOfLine("participant_team_" + i));
        }

        for (int i = match.getBlueTeam().size(); i < match.getOrangeTeam().size(); i++) {
            assertEquals("1", ConfigFileEditor.getValueOfLine("participant_team_" + i));
        }
    }
}