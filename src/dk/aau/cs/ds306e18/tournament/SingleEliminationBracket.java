package dk.aau.cs.ds306e18.tournament;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.ds306e18.tournament.participants.Participant;

public class SingleEliminationBracket implements Bracket {

    private Match finalMatch;

    public SingleEliminationBracket(List<Participant> participants) {
        int rounds = (int) Math.ceil(Math.log(participants.size())/Math.log(2));
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
