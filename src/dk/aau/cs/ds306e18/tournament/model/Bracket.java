package dk.aau.cs.ds306e18.tournament.model;

import java.util.ArrayList;

public interface Bracket {

    ArrayList<Match> getAllMatches();
    ArrayList<Match> getUpcommingMatches();
    ArrayList<Match> getUnplayableMatches();
    ArrayList<Match> getCompletedMatches();
}
