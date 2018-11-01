package dk.aau.cs.ds306e18.tournament.model.match;

import dk.aau.cs.ds306e18.tournament.model.match.Match;

public interface MatchListener {

    void onMatchPlayed(Match match);
}
