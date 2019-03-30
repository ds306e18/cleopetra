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
                new CustomBot("AllStar", "Psyonix", null, "The AllStar bot", BotType.PSYONIX)
        ));
    }
}
