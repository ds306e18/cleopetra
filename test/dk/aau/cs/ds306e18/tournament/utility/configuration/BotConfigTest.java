package dk.aau.cs.ds306e18.tournament.utility.configuration;

import org.junit.Test;

import static dk.aau.cs.ds306e18.tournament.utility.configuration.ConfigFileEditorTest.testDir;
import static org.junit.Assert.assertEquals;

public class BotConfigTest {

    private final static String testBotConfigFilename = "test_bot.cfg";

    private BotConfig setupConfig() {
        return new BotConfig(testDir + testBotConfigFilename);
    }

    @Test
    public void writeConfig() {
    }

    @Test
    public void getAppearanceConfigPath() {
        BotConfig botConfig = setupConfig();
        assertEquals("./appearance.cfg", botConfig.getAppearanceConfigPath());
    }

    @Test
    public void getPythonFile() {
        BotConfig botConfig = setupConfig();
        assertEquals("bot.py", botConfig.getPythonFile());
    }

    @Test
    public void getName() {
        BotConfig botConfig = setupConfig();
        assertEquals("Bot Name", botConfig.getName());
    }

    @Test
    public void getDeveloper() {
        BotConfig botConfig = setupConfig();
        assertEquals("Bot Developer", botConfig.getDeveloper());
    }

    @Test
    public void getDescription() {
        BotConfig botConfig = setupConfig();
        assertEquals("This is a short description of the bot", botConfig.getDescription());
    }

    @Test
    public void getFunFact() {
        BotConfig botConfig = setupConfig();
        assertEquals("This is a fun fact about the bot", botConfig.getFunFact());
    }

    @Test
    public void getGithub() {
        BotConfig botConfig = setupConfig();
        assertEquals("https://github.com/developer/repository", botConfig.getGithub());
    }

    @Test
    public void getLanguage() {
        BotConfig botConfig = setupConfig();
        assertEquals("programminglanguage", botConfig.getLanguage());
    }
}