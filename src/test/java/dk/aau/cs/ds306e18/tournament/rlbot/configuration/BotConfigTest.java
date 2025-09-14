package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class BotConfigTest {

    private static final String TEST_BOT_CONFIG_FILE = "src/test/bots/alpha.cfg";

    @Test
    public void constructor01() throws IOException {
        BotConfig bot = new BotConfig(new File(TEST_BOT_CONFIG_FILE));

        assertEquals(new File(TEST_BOT_CONFIG_FILE), bot.getConfigFile());

        assertEquals("Alpha Bot", bot.getName());
        assertEquals(new File("bot.py"), bot.getPythonFile());
        assertEquals(new File("./appearance.cfg"), bot.getLoadoutFile());

        assertEquals("Alpha Developer", bot.getDeveloper());
        assertEquals("This is a short description of the Alpha Bot", bot.getDescription());
        assertEquals("This is a fun fact about the Alpha Bot", bot.getFunFact());
        assertEquals("https://github.com/developer/Alpha-repo", bot.getSourceLink());
        assertEquals("Alpha language", bot.getLanguage());
    }
}
