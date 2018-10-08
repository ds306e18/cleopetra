package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.participants.Team;

import java.util.ArrayList;
import java.util.List;

public class SingleEliminationBracket implements Bracket {

    private Match finalMatch;

    public SingleEliminationBracket(List<Team> teams) {
        int rounds = (int) Math.ceil(Math.log(teams.size())/Math.log(2));
    }

    @Override
    public ArrayList<Match> getAllMatches() {
        return null;
    }

    @Override
    public ArrayList<Match> getUpcommingMatches() {
        return null;
    }

    @Override
    public ArrayList<Match> getUnplayableMatches() {
        return null;
    }

    @Override
    public ArrayList<Match> getCompletedMatches() {
        return null;
    }
}
