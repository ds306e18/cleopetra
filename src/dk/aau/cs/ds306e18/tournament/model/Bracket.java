package dk.aau.cs.ds306e18.tournament.model;

import java.util.ArrayList;

public interface Bracket {

    ArrayList<Match> getAllMatches();
    ArrayList<Match> getUpcomingMatches();
    ArrayList<Match> getPendingMatches();
    ArrayList<Match> getCompletedMatches();
}
