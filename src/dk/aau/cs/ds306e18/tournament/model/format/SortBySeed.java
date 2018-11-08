package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.Team;

import java.util.Comparator;

class SortBySeed implements Comparator<Team> {
    // Used for sorting in ascending order based on initialSeedValue
    public int compare(Team a, Team b) {
        return a.getInitialSeedValue() - b.getInitialSeedValue();
    }
}
