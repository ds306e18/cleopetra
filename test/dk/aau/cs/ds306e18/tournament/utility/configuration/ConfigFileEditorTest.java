package dk.aau.cs.ds306e18.tournament.utility.configuration;

import org.ini4j.Config;
import org.ini4j.Wini;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertFalse;

public class ConfigFileEditorTest {

    final static String testDir = "test/";
    final static String testRLBotConfigFilename = "test_rlbot.cfg";
    final static String testRLBotConfigTargetFilename = "dummy_rlbot.cfg";
    private Wini testConfig = new Wini();

    @BeforeClass
    public static void init() {
        Config.getGlobal().setLineSeparator("\n");
    }

    /**
     * Removes file at given path. Useful for cleaning up after testing.
     *
     * @param path to file to be deleted
     */
    static void deleteConfig(String path) {
        try {
            Files.delete(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up a RLBotConfig object. This is necessary, as CFE is abstract and cannot be instantiated
     */
    private RLBotConfig setupRLBotCFE() {
        // loads test_rlbot.cfg from disk, but overwrites with setConfig()-call
        return new RLBotConfig(testDir + testRLBotConfigFilename);
    }

    /**
     * Tests whether a newly written, and read config is identical to the written config
     */
    @Ignore
    @Test
    public void readWriteConcurrencyCFETest() {
        RLBotConfig configFileEditor = setupRLBotCFE();
        configFileEditor.write(testDir + testRLBotConfigTargetFilename);
        configFileEditor.read(testDir + testRLBotConfigTargetFilename);
        assertEquals(testConfig, configFileEditor.getConfig());
        deleteConfig(testDir + testRLBotConfigTargetFilename);
    }

    /**
     * Tests if editLine applies the desired change to the config
     */
    @Test
    public void editLineTest() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        configFileEditor.editLine("Participant Configuration", "participant_team_0", "42");
        assertEquals("42", configFileEditor.getValueOfLine("Participant Configuration", "participant_team_0"));
    }

    /**
     * Tests if getValueOfLine returns correct value
     */
    @Test
    public void getValueOfLineTest1() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("1", configFileEditor.getValueOfLine("Participant Configuration", "participant_team_1"));
    }

    /**
     * Tests if getValueOfLine returns correct value
     */
    @Test
    public void getValueOfLineTest2() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertEquals("0", configFileEditor.getValueOfLine("Participant Configuration", "participant_team_0"));
    }

    /**
     * Tests if getValueOfLine returns correct output on partial match of parameter
     */
    @Test
    public void getValueOfLineTest3() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertNull(configFileEditor.getValueOfLine("Participant Configuration", "[Particip"));
    }

    /**
     * Tests if getValueOfLine returns null if no parameter is found in config
     */
    @Test
    public void getValueOfLineTest4() {
        ConfigFileEditor configFileEditor = setupRLBotCFE();
        assertNull(configFileEditor.getValueOfLine("Participant Configuration", "NAN"));
    }

    /**
     * Tests if validateConfigSyntax returns true on a valid config
     */
    @Test
    public void validateConfigSyntax1() {
        ConfigFileEditor configFileEditor = new RLBotConfig(testDir + testRLBotConfigFilename);
        assertTrue(configFileEditor.isValid());
    }

    /**
     * Tests if validateConfigSyntax returns false on a config with mismatched header delimiters
     */
    @Test
    public void validateConfigSyntax2() {
        ConfigFileEditor configFileEditor = new RLBotConfig(testDir + testRLBotConfigFilename);
        configFileEditor.getConfig().remove("Participant Configuration");
        configFileEditor.validateConfigSyntax();
        assertFalse(configFileEditor.isValid());
    }

    /**
     * Tests if validateConfigSyntax returns false on a config with mismatched parameter
     */
    @Test
    public void validateConfigSyntax3() {
        ConfigFileEditor configFileEditor = new RLBotConfig(testDir + testRLBotConfigFilename);
        configFileEditor.getConfig().remove("Match Configuration");
        configFileEditor.validateConfigSyntax();
        assertFalse(configFileEditor.isValid());
    }
}