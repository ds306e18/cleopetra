package dk.aau.cs.ds306e18.tournament.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CustomBotTest {

    @Test
    public void clone01() {
        CustomBot bot = new CustomBot("name", "dev", "path", "desc", true);
        assertEquals(bot, bot.clone());
    }
}