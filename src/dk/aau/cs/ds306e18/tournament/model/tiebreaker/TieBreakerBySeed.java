package dk.aau.cs.ds306e18.tournament.model.tiebreaker;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreaker;

import java.util.Random;

public class TieBreakerBySeed extends TieBreaker {

    private Random rand = new Random();

    @Override
    public boolean compare(Team teamA, Team teamB) {
        int comparison = Integer.compare(teamA.getInitialSeedValue(), teamB.getInitialSeedValue());
        if (comparison == 0) return rand.nextBoolean();
        return comparison < 0;
    }

    @Override
    public String toString() {
        return "seed";
    }
}
