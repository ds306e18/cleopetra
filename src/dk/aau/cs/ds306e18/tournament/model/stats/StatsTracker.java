package dk.aau.cs.ds306e18.tournament.model.stats;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This class tracks the stats (wins, loses, goals, etc) for a single team over a set of matches. The class uses the
 * MatchChangeListener interface to get updates about new stats.
 */
class StatsTracker implements MatchChangeListener {

    private Team team;
    private Stats stats;
    private Set<Match> trackedMatches = new HashSet<>();

    /**
     * Create a Stats object to track stats for the given team.
     * @param team the team to track stats for.
     */
    public StatsTracker(Team team) {
        this.team = team;
        stats = new Stats(team);
    }

    /**
     * Recalculates the stats by check each tracked match. StatsChangeListeners are notified afterwards.
     */
    public void recalculate() {
        int wins = 0;
        int loses = 0;
        int goals = 0;
        int goalsConceded = 0;
        for (Match match : trackedMatches) {
            // If the team is in the match (which it might not be), add the relevant stats
            if (match.getTeamOne() == team) {
                goals += match.getTeamOneScore();
                goalsConceded += match.getTeamTwoScore();
                if (match.hasBeenPlayed()) {
                    if (match.getWinner() == team) {
                        wins++;
                    } else {
                        loses++;
                    }
                }
            } else if (match.getTeamTwo() == team) {
                goals += match.getTeamTwoScore();
                goalsConceded += match.getTeamOneScore();
                if (match.hasBeenPlayed()) {
                    if (match.getWinner() == team) {
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
     * Returns a set of all the tracked matches. Changes to the set does not affect the Stats.
     */
    public Set<Match> getTrackedMatches() {
        return new HashSet<>(trackedMatches);
    }

    /**
     * Remove all tracked matches and essentially resets the stats. StatsChangeListeners are notified.
     */
    public void clearTrackedMatches() {
        trackedMatches.clear();
        recalculate();
    }

    /**
     * Starts tracking stats from the given match. The associated team does not need to be a participant of the
     * given match. StatsChangeListeners are notified.
     * @param match a match to track stats from
     */
    public void trackMatch(Match match) {
        trackedMatches.add(match);
        match.registerMatchChangeListener(this);
        recalculate();
    }
    /**
     * Stops tracking stats from the given match. StatsChangeListeners are notified.
     * @param match a match to stop tracking stats from
     */
    public void untrackMatch(Match match) {
        trackedMatches.remove(match);
        match.unregisterMatchChangeListener(this);
        recalculate();
    }

    /**
     * Starts tracking stats from the given matches. The associated team does not need to be a participant of the
     * given matches. StatsChangeListeners are notified.
     * @param matches a collection of matches to track stats from.
     */
    public void trackMatches(Collection<? extends Match> matches) {
        for (Match match : matches) {
            trackedMatches.add(match);
            match.registerMatchChangeListener(this);
        }
        recalculate();
    }

    /**
     * Stops tracking stats from the given match. StatsChangeListeners are notified.
     * @param matches a collection of matches to stop tracking stats from.
     */
    public void untrackMatches(Collection<? extends Match> matches) {
        for (Match match : matches) {
            trackedMatches.remove(match);
            match.unregisterMatchChangeListener(this);
        }
        recalculate();
    }

    @Override
    public void onMatchChanged(Match match) {
        recalculate();
    }

    public Team getTeam() {
        return team;
    }

    public Stats getStats() {
        return stats;
    }
}
