package dk.aau.cs.ds306e18.tournament.utility.configuration;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertFalse;

public class ConfigFileEditorTest {

    final static String testDir = "test/";
    final static String testRLBotConfigFilename = "test_rlbot.cfg";
    final static String testRLBotConfigTargetFilename = "dummy_rlbot.cfg";
    private ArrayList<String> testConfig = new ArrayList<>();

    /**
     * Sets a simple testConfig for passing ConfigFileEditor for test-cases
     */
    private void setTestConfig() {
        testConfig.add("[Participant Configuration]");
        testConfig.add("participant_team_0 = 0");
        testConfig.add("participant_team_1 = 1");
    }

    @Test
    public void readWriteConcurrencyTest() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        ConfigFileEditor.write(testDir + testRLBotConfigTargetFilename);

        ConfigFileEditor.read(testDir + testRLBotConfigTargetFilename);
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
        assertNull(ConfigFileEditor.getLine("NAN"));
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
    public void getValueOfLineTest1() {
        setTestConfig();
        ConfigFileEditor.setConfig(testConfig);
        assertNull(ConfigFileEditor.getValueOfLine("notavailable"));
    }

    @Test
    public void validateConfigSyntax1() {
        ConfigFileEditor.read(testDir + testRLBotConfigFilename);
        assertTrue(ConfigFileEditor.isValid());
    }

    @Test
    public void validateConfigSyntax2() {
        ConfigFileEditor.read(testDir + testRLBotConfigFilename);
        ArrayList<String> config = ConfigFileEditor.getConfig();
        // remove trailing, closing square bracket from first header
        config.set(0, config.get(0).split("]")[0]);
        ConfigFileEditor.setConfig(config);
        ConfigFileEditor.validateConfigSyntax();
        assertFalse(ConfigFileEditor.isValid());
    }
}