package dk.aau.cs.ds306e18.tournament.utility.configuration;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertFalse;

public class ConfigFileEditorTest {

    final static String testDir = "test/";
    final static String testRLBotConfigFilename = "test_rlbot.cfg";
    private final static String testRLBotConfigTargetFilename = "dummy_rlbot.cfg";
    private ArrayList<String> testConfig = new ArrayList<>();

    /**
     * Sets a simple testConfig for passing ConfigFileEditor for test-cases
     */
    private void setTestConfig() {
        testConfig.add("[Participant Configuration]");
        testConfig.add("participant_team_0 = 0");
        testConfig.add("participant_team_1 = 1");
    }

    private ConfigFileEditor setupRLBotCFE() {
        ConfigFileEditor configFileEditor = new RLBotConfig();
        setTestConfig();
        configFileEditor.setConfig(testConfig);
        return configFileEditor;
    }

    @Test
    public void readWriteConcurrencyTest() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        configFileEditor.write(testDir + testRLBotConfigTargetFilename);

        configFileEditor.read(testDir + testRLBotConfigTargetFilename);
        assertEquals(testConfig, configFileEditor.getConfig());
    }


    @Test
    public void editLineTest() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();

        configFileEditor.editLine("participant_team_", 0, "42");
        assertEquals("participant_team_0 = 42", configFileEditor.getConfig().get(1));
    }

    @Test
    public void getLineTest1() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertNull(configFileEditor.getValueOfLine("NAN"));
    }

    @Test
    public void getLineTest2() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("participant_team_0 = 0", configFileEditor.getLine("participant_team_0"));
    }

    @Test
    public void getLineTest3() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("participant_team_1 = 1", configFileEditor.getLine("participant_team_1"));
    }

    @Test
    public void getLineTest4() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("[Participant Configuration]", configFileEditor.getLine(0));
    }

    @Test
    public void getValueTest1() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("1", configFileEditor.getValueOfLine("participant_team_1"));
    }

    @Test
    public void getValueTest2() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("0", configFileEditor.getValueOfLine("participant_team_0"));
    }

    @Test
    public void getValueTest3() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("[Participant Configuration]", configFileEditor.getValueOfLine("[Particip"));
    }

    @Test
    public void getValueOfLineTest1() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertNull(configFileEditor.getValueOfLine("notavailable"));
    }

    @Test
    public void validateConfigSyntax1() {
        ConfigFileEditor configFileEditor = new RLBotConfig(testDir + testRLBotConfigFilename);
        assertTrue(configFileEditor.isValid());
    }

    @Test
    public void validateConfigSyntax2() {
        ConfigFileEditor configFileEditor = new RLBotConfig(testDir + testRLBotConfigFilename);
        ArrayList<String> config = configFileEditor.getConfig();
        // remove trailing, closing square bracket from first header
        config.set(0, config.get(0).split("]")[0]);
        configFileEditor.setConfig(config);
        configFileEditor.validateConfigSyntax();
        assertFalse(configFileEditor.isValid());
    }

}