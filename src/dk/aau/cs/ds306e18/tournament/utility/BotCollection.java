package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Bot;

import java.util.Collection;
import java.util.HashSet;

public class BotCollection {

    private HashSet<Bot> bots = new HashSet<>();

    public BotCollection() {

    }

    public void addBot(Bot bot) {
        this.bots.add(bot);
    }

    public void addBots(Collection<Bot> bots) {
        this.bots.addAll(bots);
    }
}
