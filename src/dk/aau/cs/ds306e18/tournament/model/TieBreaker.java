package dk.aau.cs.ds306e18.tournament.model;

public interface TieBreaker {

    String toString();

    /** Returns true if teamA wins over teamB in a tie breaker. */
    boolean compare(Team teamA, Team teamB);
}
