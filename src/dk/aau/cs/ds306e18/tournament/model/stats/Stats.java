package dk.aau.cs.ds306e18.tournament.model.stats;

import dk.aau.cs.ds306e18.tournament.model.Team;

import java.util.HashSet;
import java.util.Set;

/**
 * This class holds the stats (wins, loses, goals, etc) for a single team. It provides an observer pattern
 * with StatsChangeListener and notifies listeners whenever there are potential stats changes.
 */
public class Stats {

    private Team team;
    private int wins = 0;
    private int loses = 0;
    private int goals = 0;
    private int goalsConceded = 0;

    private Set<StatsChangeListener> statsChangeListeners = new HashSet<>();

    /**
     * Create a Stats object to track stats for the given team.
     * @param team the team to track stats for.
     */
    public Stats(Team team) {
        this.team = team;
    }

    /**
     * Sets the stats. Should only be called by StatsTracker. StatsChangeListeners are notified.
     */
    void set(int wins, int loses, int goals, int goalsConceded) {
        this.wins = wins;
        this.loses = loses;
        this.goals = goals;
        this.goalsConceded = goalsConceded;
        notifyStatsChangeListeners();
    }

    /**
     * Listeners registered will be notified when the stats changes, for instance when a tracked match changes or
     * when a new match tracked or a match is no longer tracked, thus potentially changing the stats.
     */
    public void registerStatsChangeListener(StatsChangeListener listener) {
        statsChangeListeners.add(listener);
    }

    public void unregisterStatsChangeListener(StatsChangeListener listener) {
        statsChangeListeners.remove(listener);
    }

    private void notifyStatsChangeListeners() {
        for (StatsChangeListener listener : statsChangeListeners) {
            listener.statsChanged(this);
        }
    }

    public Team getTeam() {
        return team;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public int getGoals() {
        return goals;
    }

    public int getGoalsConceded() {
        return goalsConceded;
    }

    public int getGoalDifference() {
        return goals - goalsConceded;
    }
}
