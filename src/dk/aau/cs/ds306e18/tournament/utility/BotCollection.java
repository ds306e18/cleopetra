package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotFromConfig;
import dk.aau.cs.ds306e18.tournament.model.BotSkill;
import dk.aau.cs.ds306e18.tournament.model.PsyonixBot;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
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

        try {
            // Bots starting in the bot collection
            URL allstarURL = Main.class.getResource("rlbot/psyonix_allstar.cfg");
            PsyonixBot allstar = new PsyonixBot(Paths.get(allstarURL.toURI()).toString(), BotSkill.ALLSTAR);
            URL proURL = Main.class.getResource("rlbot/psyonix_pro.cfg");
            PsyonixBot pro = new PsyonixBot(Paths.get(proURL.toURI()).toString(), BotSkill.PRO);
            URL rookieURL = Main.class.getResource("rlbot/psyonix_rookie.cfg");
            PsyonixBot rookie = new PsyonixBot(Paths.get(rookieURL.toURI()).toString(), BotSkill.ROOKIE);

            addAll(Arrays.asList(
                    allstar, pro, rookie
            ));

        } catch (Exception e) {
            // Something went wrong. Report it, but continue
            System.err.println("Could not load default bots.");
        }
    }
}
