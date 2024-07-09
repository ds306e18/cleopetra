package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.model.stats.StatsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Team {

    public static final int MAX_SIZE = 32;

    private String teamName;
    private ArrayList<Bot> bots;
    private int initialSeedValue;
    private String description;

    transient private StatsManager statsManager;

    public Team(String teamName, List<Bot> bots, int initialSeedValue, String description) {
        this.teamName = teamName;
        this.bots = bots == null ? new ArrayList<>() : new ArrayList<>(bots); // List can't be null to avoid NullPointerExceptions
        this.initialSeedValue = initialSeedValue;
        this.description = description;
        statsManager = new StatsManager(this);
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
        return bots.remove(bot);
    }

    public Bot removeBot(int index) {
        if (0 <= index && index < bots.size()) {
            return bots.remove(index);
        }
        return null;
    }

    public ArrayList<Bot> getBots() {
        return new ArrayList<>(bots);
    }

    public ArrayList<String> getConfigPaths() {
        ArrayList<String> paths = new ArrayList<>();
        for (Bot bot : bots) {
            paths.add(bot.getConfigPath());
        }
        return paths;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    /**
     * Repairs properties that cannot be deserialized.
     */
    public void postDeserializationRepair() {
        bots.removeAll(Collections.singletonList(null));
        statsManager = new StatsManager(this);
    }

    @Override
    public String toString() {
        return teamName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return getInitialSeedValue() == team.getInitialSeedValue() &&
                Objects.equals(getTeamName(), team.getTeamName()) &&
                Objects.equals(getBots(), team.getBots()) &&
                Objects.equals(getDescription(), team.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTeamName(), getBots(), getInitialSeedValue(), getDescription());
    }
}