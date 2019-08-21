package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BotConfigTest {

    public final static String TEST_DIR = "test/";
    public final static String TEST_BOT_CONFIG_FILENAME = "test_bot.cfg";
    public final static String TEST_BOT_CONFIG_FILENAME_2 = "test_bot_2.cfg";
    public final static String TEST_BOT_CONFIG_FILENAME_3 = "test_bot_3.cfg";
    public final static String[] TEST_BOT_CONFIG_FILENAMES = {
            TEST_BOT_CONFIG_FILENAME,
            TEST_BOT_CONFIG_FILENAME_2,
            TEST_BOT_CONFIG_FILENAME_3
    };

    @Test
    public void constructor01() throws IOException {
        BotConfig bot = new BotConfig(new File(TEST_DIR, TEST_BOT_CONFIG_FILENAME));

        assertEquals(new File(TEST_DIR, TEST_BOT_CONFIG_FILENAME), bot.getConfigFile());

        assertEquals("Bot Name", bot.getName());
        assertEquals(new File("bot.py"), bot.getPythonFile());
        assertEquals(new File("./appearance.cfg"), bot.getLooksConfig());

        assertEquals("Bot Developer", bot.getDeveloper());
        assertEquals("This is a short description of the bot", bot.getDescription());
        assertEquals("This is a fun fact about the bot", bot.getFunFact());
        assertEquals("https://github.com/developer/repository", bot.getGithub());
        assertEquals("programminglanguage", bot.getLanguage());
    }
}