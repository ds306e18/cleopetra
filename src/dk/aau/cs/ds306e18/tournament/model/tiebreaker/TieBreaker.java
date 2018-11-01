package dk.aau.cs.ds306e18.tournament.model.tiebreaker;

import dk.aau.cs.ds306e18.tournament.model.Team;

import java.util.ArrayList;
import java.util.List;

public abstract class TieBreaker {

    public List<Team> compareAll(List<Team> teams, int count) {
        ArrayList<Team> sorted = new ArrayList<>(teams);
        sorted.sort((a, b) -> a == b ? 0 : compare(a, b) ? -1 : 1);
        return sorted.subList(0, count);
    }

    public abstract String toString();

    /** Returns true if teamA wins over teamB in a tie breaker. */
    public abstract boolean compare(Team teamA, Team teamB);
}
