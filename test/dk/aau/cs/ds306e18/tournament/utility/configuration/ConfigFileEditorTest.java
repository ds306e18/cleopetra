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

    /** Sets a simple testConfig for passing ConfigFileEditor for test-cases */
    private void setTestConfig() {
        testConfig.add("[Participant Configuration]");
        testConfig.add("participant_team_0 = 0");
        testConfig.add("participant_team_1 = 1");
    }

    /** Sets up a RLBotConfig object. This is necessary, as CFE is abstract and cannot be instantiated */
    private ConfigFileEditor setupRLBotCFE() {
        // loads test_rlbot.cfg from disk, but overwrites with setConfig()-call
        ConfigFileEditor configFileEditor = new RLBotConfig(testDir + testRLBotConfigFilename);
        setTestConfig();
        configFileEditor.setConfig(testConfig);
        return configFileEditor;
    }

    /** Tests whether a newly written, and read config is identical to the written config */
    @Test
    public void readWriteConcurrencyCFETest() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        configFileEditor.write(testDir + testRLBotConfigTargetFilename);
        configFileEditor.read(testDir + testRLBotConfigTargetFilename);
        assertEquals(testConfig, configFileEditor.getConfig());
    }

    /** Tests if editLine applies the desired change to the config */
    @Test
    public void editLineTest() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        configFileEditor.editLine("participant_team_", 0, "42");
        assertEquals("participant_team_0 = 42", configFileEditor.getConfig().get(1));
    }

    /** Tests if getLine by parameter returns correct line in config */
    @Test
    public void getLineTest2() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("participant_team_0 = 0", configFileEditor.getLine("participant_team_0"));
    }

    /** Tests if getLine by parameter returns correct line in config */
    @Test
    public void getLineTest3() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("participant_team_1 = 1", configFileEditor.getLine("participant_team_1"));
    }

    /** Tests if getLine by index returns correct line in config */
    @Test
    public void getLineTest4() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("[Participant Configuration]", configFileEditor.getLine(0));
    }

    /** Tests if getLine returns null if no parameter is found in an empty String-array */
    @Test
    public void getLineTest5() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        configFileEditor.setConfig(new ArrayList<>());
        assertNull(configFileEditor.getLine("NAN"));
    }

    /** Tests if getValueOfLine returns correct value */
    @Test
    public void getValueOfLineTest1() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("1", configFileEditor.getValueOfLine("participant_team_1"));
    }

    /** Tests if getValueOfLine returns correct value */
    @Test
    public void getValueOfLineTest2() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("0", configFileEditor.getValueOfLine("participant_team_0"));
    }

    /** Tests if getValueOfLine returns correct output on partial match of parameter */
    @Test
    public void getValueOfLineTest3() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("[Participant Configuration]", configFileEditor.getValueOfLine("[Particip"));
    }

    /** Tests if getValueOfLine returns null if no parameter is found in config */
    @Test
    public void getValueOfLineTest4() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertNull(configFileEditor.getValueOfLine("NAN"));
    }

    /** Tests if validateConfigSyntax returns true on a valid config */
    @Test
    public void validateConfigSyntax1() {
        ConfigFileEditor configFileEditor = new RLBotConfig(testDir + testRLBotConfigFilename);
        assertTrue(configFileEditor.isValid());
    }

    /** Tests if validateConfigSyntax returns false on a config with mismatched header delimiters */
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

    /** Tests if validateConfigSyntax returns false on a config with mismatched parameter */
    @Test
    public void validateConfigSyntax3() {
        ConfigFileEditor configFileEditor = new RLBotConfig(testDir + testRLBotConfigFilename);
        ArrayList<String> config = configFileEditor.getConfig();
        // remove trailing, closing square bracket from first header
        config.set(3, "parameter_without_value");
        configFileEditor.setConfig(config);
        configFileEditor.validateConfigSyntax();
        assertFalse(configFileEditor.isValid());
    }
}