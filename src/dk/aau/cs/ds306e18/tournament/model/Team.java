package dk.aau.cs.ds306e18.tournament.model;

import java.util.ArrayList;
import java.util.Objects;

public class Team {

    public static final int MAX_SIZE = 32;

    private String teamName;
    private ArrayList<Bot> bots;
    private int initialSeedValue;
    private String description;
    private int goalsScored;
    private int goalsConceded; //This is goals scored on this team by opponent teams

    public Team(String teamName, ArrayList<Bot> bots, int initialSeedValue, String description) {
        this.teamName = teamName;
        this.bots = bots;
        this.initialSeedValue = initialSeedValue;
        this.description = description;
        this.goalsScored = 0;
        this.goalsConceded = 0;
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

    /**
     * Adds both points to the goals scored count and the goals conceded count.
     *
     * @param pointsToAddConceded the number of points to be added to the current conceded score.
     * @param pointsToAddScored   the number of points to be added to the current score.
     */
    public void addGoalsScoredAndConceded(int pointsToAddScored, int pointsToAddConceded) {
        addGoalsScored(pointsToAddScored);
        addGoalsConceded(pointsToAddConceded);
    }

    /**
     * Adds the given points to the goals scored count.
     *
     * @param pointsToAdd the number of points to be added to the current score.
     */
    public void addGoalsScored(int pointsToAdd) {
        this.goalsScored += pointsToAdd;
    }

    /**
     * Adds the given points to the goals concede count.
     *
     * @param pointsToAdd the number of points to be added to the current conceded score.
     */
    public void addGoalsConceded(int pointsToAdd) {
        this.goalsConceded += pointsToAdd;
    }

    /**
     * @return the number of goals this team has scored.
     */
    public int getGoalsScored() {
        return goalsScored;
    }

    /**
     * @return the number of goals that opponents has scored against this team.
     */
    public int getGoalsConceded() {
        return goalsConceded;
    }

    /**
     * @return the difference between goals scored and goals conceded.
     */
    public int getGoalDiff() {
        return goalsScored - goalsConceded;
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