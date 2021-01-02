package dk.aau.cs.ds306e18.tournament.model.stats;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This class tracks the stats (wins, loses, goals, etc) for a single team over a set of series. The class uses the
 * MatchChangeListener interface to get updates about new stats.
 */
class StatsTracker implements MatchChangeListener {

    private Team team;
    private Stats stats;
    private Set<Series> trackedSeries = new HashSet<>();

    /**
     * Create a Stats object to track stats for the given team.
     * @param team the team to track stats for.
     */
    public StatsTracker(Team team) {
        this.team = team;
        stats = new Stats(team);
    }

    /**
     * Recalculates the stats by check each tracked series. StatsChangeListeners are notified afterwards.
     */
    public void recalculate() {
        int wins = 0;
        int loses = 0;
        int goals = 0;
        int goalsConceded = 0;
        for (Series series : trackedSeries) {
            // If the team is in the series, add the relevant stats
            if (series.getTeamOne() == team) {
                goals += series.getTeamOneScores().stream().mapToInt(x -> x.orElse(0)).sum();
                goalsConceded += series.getTeamTwoScores().stream().mapToInt(x -> x.orElse(0)).sum();
                if (series.hasBeenPlayed()) {
                    if (series.getWinner() == team) {
                        wins++;
                    } else {
                        loses++;
                    }
                }
            } else if (series.getTeamTwo() == team) {
                goals += series.getTeamTwoScores().stream().mapToInt(x -> x.orElse(0)).sum();
                goalsConceded += series.getTeamOneScores().stream().mapToInt(x -> x.orElse(0)).sum();
                if (series.hasBeenPlayed()) {
                    if (series.getWinner() == team) {
                        wins++;
                    } else {
                        loses++;
                    }
                }
            }
        }
        stats.set(wins, loses, goals, goalsConceded);
    }

    /**
     * Returns a set of all the tracked series. Changes to the set does not affect the Stats.
     */
    public Set<Series> getTrackedSeries() {
        return new HashSet<>(trackedSeries);
    }

    /**
     * Remove all tracked matches and essentially resets the stats. StatsChangeListeners are notified.
     */
    public void clearTrackedMatches() {
        trackedSeries.clear();
        recalculate();
    }

    /**
     * Starts tracking stats from the given series. The associated team does not need to be a participant of the
     * given series. StatsChangeListeners are notified.
     * @param series a series to track stats from
     */
    public void trackSeries(Series series) {
        trackedSeries.add(series);
        series.registerMatchChangeListener(this);
        recalculate();
    }
    /**
     * Stops tracking stats from the given series. StatsChangeListeners are notified.
     * @param series a series to stop tracking stats from
     */
    public void untrackSeries(Series series) {
        trackedSeries.remove(series);
        series.unregisterMatchChangeListener(this);
        recalculate();
    }

    /**
     * Starts tracking stats from the given series. The associated team does not need to be a participant of the
     * given matches. StatsChangeListeners are notified.
     * @param matches a collection of series to track stats from.
     */
    public void trackAllSeries(Collection<? extends Series> matches) {
        for (Series series : matches) {
            trackedSeries.add(series);
            series.registerMatchChangeListener(this);
        }
        recalculate();
    }

    /**
     * Stops tracking stats from the given series. StatsChangeListeners are notified.
     * @param matches a collection of series to stop tracking stats from.
     */
    public void untrackAllSeries(Collection<? extends Series> matches) {
        for (Series series : matches) {
            trackedSeries.remove(series);
            series.unregisterMatchChangeListener(this);
        }
        recalculate();
    }

    @Override
    public void onMatchChanged(Series series) {
        recalculate();
    }

    public Team getTeam() {
        return team;
    }

    public Stats getStats() {
        return stats;
    }
}
