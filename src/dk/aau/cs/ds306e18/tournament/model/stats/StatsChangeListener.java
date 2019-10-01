package dk.aau.cs.ds306e18.tournament.model.stats;

/**
 * StatsChangeListener can be notified when a Stats object changes, for instance when a tracked match changes or
 * when a new match is tracked or when a match is no longer tracked, thus potentially changing the stats.
 */
public interface StatsChangeListener {

    void statsChanged(Stats stats);
}
