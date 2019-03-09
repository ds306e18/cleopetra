package dk.aau.cs.ds306e18.tournament.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BotTest {

    @Test
    public void clone01() {
        Bot bot = new Bot("name", "dev", "path", "desc", true);
        assertEquals(bot, bot.clone());
    }
}