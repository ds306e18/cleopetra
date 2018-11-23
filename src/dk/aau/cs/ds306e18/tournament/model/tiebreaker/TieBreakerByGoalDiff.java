package dk.aau.cs.ds306e18.tournament.model.tiebreaker;

import dk.aau.cs.ds306e18.tournament.model.Team;

import java.util.Objects;
import java.util.Random;

public class TieBreakerByGoalDiff extends TieBreaker {
    private String name = "TieBreaker by goal difference";

    @Override
    public String toString() { return "goal difference"; }

    @Override
    public boolean compare(Team teamA, Team teamB) {
        Random rand = new Random();
        int comparison = Integer.compare(teamA.getGoalDiff(), teamB.getGoalDiff());
        if(comparison == 0) return rand.nextBoolean();
        return comparison > 0;
    }

    public String getName() { return this.name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TieBreakerByGoalDiff that = (TieBreakerByGoalDiff) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
