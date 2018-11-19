package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class ConfigFileEditorTest {

    private ArrayList<String> testConfig = new ArrayList<>();

    private void setTestConfig() {
        testConfig.add("[Participant Configuration]");
        testConfig.add("participant_team_0 = 0");
        testConfig.add("participant_team_1 = 1");
    }

    @Test
    public void readWriteConcurrencyTest() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        ConfigFileEditor.writeConfig("testconfig1.cfg");

        ConfigFileEditor.readConfig("testconfig1.cfg");
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
    public void getValueTest1() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        assertEquals("1", ConfigFileEditor.getValue(ConfigFileEditor.getLine("participant_team_1")));
    }

    @Test
    public void getValueTest2() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        assertEquals("0", ConfigFileEditor.getValue(ConfigFileEditor.getLine("participant_team_0")));
    }

    @Test
    public void getValueTest3() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        assertEquals("[Participant Configuration]", ConfigFileEditor.getValue(ConfigFileEditor.getLine("[Participant ")));
    }
}