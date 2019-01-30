package dk.aau.cs.ds306e18.tournament.utility.configuration;

import org.junit.Test;

import static dk.aau.cs.ds306e18.tournament.utility.configuration.ConfigFileEditorTest.deleteConfig;
import static dk.aau.cs.ds306e18.tournament.utility.configuration.ConfigFileEditorTest.testDir;
import static org.junit.Assert.assertEquals;

public class BotConfigTest {

    private final static String testBotConfigFilename = "test_bot.cfg";
    private final static String testBotConfigTargetFilename = "dummy_bot.cfg";

    /** Sets up BotConfig object from reading test_bot.cfg */
    private BotConfig setupConfig() {
        return new BotConfig(testDir + testBotConfigFilename);
    }

    /** Reads in test_bot.cfg, edits one parameter, and returns it for easy testing */
    private static BotConfig setupEditedBotConfig() {
        BotConfig botConfig = new BotConfig(testDir + testBotConfigFilename);
        botConfig.editLine("name", "Testing bot");
        return botConfig;
    }

    /**
     * Tests whether a newly written, and read config is identical to the written config. Redundant but tests call to
     * CFE write-method. Does not show up as coverage though.
     */
    @Test
    public void readWriteConcurrencyBotConfigTest() {
        // get and write edited BotConfig to filesystem
        BotConfig botConfig = setupEditedBotConfig();
        botConfig.writeConfig(testDir + testBotConfigTargetFilename);

        // assert that expected, edited config is equal to read config from filesystem
        assertEquals(setupEditedBotConfig().getConfig(), new BotConfig(testDir + testBotConfigTargetFilename).getConfig());

        deleteConfig(testDir + testBotConfigTargetFilename);
    }

    /** Tests if getters return expected value from test_bot.cfg */
    @Test
    public void getAppearanceConfigPath() {
        BotConfig botConfig = setupConfig();
        assertEquals("./appearance.cfg", botConfig.getAppearanceConfigPath());
    }

    /** Tests if getters return expected value from test_bot.cfg */
    @Test
    public void getPythonFile() {
        BotConfig botConfig = setupConfig();
        assertEquals("bot.py", botConfig.getPythonFile());
    }

    /** Tests if getters return expected value from test_bot.cfg */
    @Test
    public void getName() {
        BotConfig botConfig = setupConfig();
        assertEquals("Bot Name", botConfig.getName());
    }

    /** Tests if getters return expected value from test_bot.cfg */
    @Test
    public void getDeveloper() {
        BotConfig botConfig = setupConfig();
        assertEquals("Bot Developer", botConfig.getDeveloper());
    }

    /** Tests if getters return expected value from test_bot.cfg */
    @Test
    public void getDescription() {
        BotConfig botConfig = setupConfig();
        assertEquals("This is a short description of the bot", botConfig.getDescription());
    }

    /** Tests if getters return expected value from test_bot.cfg */
    @Test
    public void getFunFact() {
        BotConfig botConfig = setupConfig();
        assertEquals("This is a fun fact about the bot", botConfig.getFunFact());
    }

    /** Tests if getters return expected value from test_bot.cfg */
    @Test
    public void getGithub() {
        BotConfig botConfig = setupConfig();
        assertEquals("https://github.com/developer/repository", botConfig.getGithub());
    }

    /** Tests if getters return expected value from test_bot.cfg */
    @Test
    public void getLanguage() {
        BotConfig botConfig = setupConfig();
        assertEquals("programminglanguage", botConfig.getLanguage());
    }

    /** Tests if CFE supports extended charset */
    @Test
    public void characterTest() {
        BotConfig botConfig = setupConfig();
        botConfig.editLine("name", "øæåâèî");
        assertEquals("øæåâèî", botConfig.getName());
    }
}