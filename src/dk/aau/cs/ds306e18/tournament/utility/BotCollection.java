package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotType;
import dk.aau.cs.ds306e18.tournament.model.CustomBot;

import java.util.Arrays;
import java.util.TreeSet;

public class BotCollection extends TreeSet<Bot> {

    public BotCollection() {
        super((a, b) -> {
            if (a == b) return 0;
            // Sort by bot type
            int diff = a.getBotType().ordinal() - b.getBotType().ordinal();
            if (diff == 0) {
                // If type is the same, sort by bot name
                return a.getName().compareTo(b.getName());
            }
            return diff;
        });

        // Bots starting in the bot collection
        addAll(Arrays.asList(
                new CustomBot("Psyonix All-Star", "Psyonix", null, "All-Star is the only difficulty level where the bots will seek out boost.", BotType.PSYONIX),
                new CustomBot("Psyonix Pro", "Psyonix", null, "The bots wiggle their tires during the countdown just to add some personality. Unfortunately we didn't have time to give them different personalities.", BotType.PSYONIX),
                new CustomBot("Psyonix Rookie", "Psyonix", null, "Rocket League's bots are implemented with a single behavior tree.", BotType.PSYONIX)
        ));
    }
}
