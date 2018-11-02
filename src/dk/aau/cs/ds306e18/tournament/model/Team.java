package dk.aau.cs.ds306e18.tournament.model;

import java.nio.file.Path;
import java.util.ArrayList;

public class Team {

    public static final int MAX_SIZE = 5;

    private String teamName;
    private ArrayList<Bot> bots;
    private int initialSeedValue;
    private String description;

    public Team(String teamName, ArrayList<Bot> bots, int initialSeedValue, String description) {
        this.teamName = teamName;
        this.bots = bots;
        this.initialSeedValue = initialSeedValue;
        this.description = description;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getInitialSeedValue() {
        return initialSeedValue;
    }

    public void setInitialSeedValue(int initialSeedValue) {
        this.initialSeedValue = initialSeedValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int size() {
        return bots.size();
    }

    public boolean addBot(Bot bot) {
        if (bots.size() < MAX_SIZE) {
            return bots.add(bot);
        }
        return false;
    }

    public boolean removeBot(Bot bot) {
        if (bots.size() > 1) {
            return bots.remove(bot);
        }
        throw new IllegalStateException("Can't remove the last bot from a team.");
    }

    public Bot removeBot(int index) {
        if (bots.size() > 1) {
            return bots.remove(index);
        }
        throw new IllegalStateException("Can't remove the last bot from a team.");
    }

    public ArrayList<Bot> getBots() {
        return new ArrayList<>(bots);
    }

    public ArrayList<Path> getConfigPaths() {
        ArrayList<Path> paths = new ArrayList<>();
        for (Bot bot : bots) {
            paths.add(bot.getConfigPath());
        }
        return paths;
    }
}