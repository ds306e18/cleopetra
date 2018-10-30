package dk.aau.cs.ds306e18.tournament.model;

import java.util.Objects;
import java.util.Random;

public class TieBreakerBySeed extends TieBreaker {

    private Random rand = new Random();
    private String name = "TieBreaker by seed";

    public String toString() {
        return "seed";
    }

    @Override
    public boolean compare(Team teamA, Team teamB) {
        int comparison = Integer.compare(teamA.getInitialSeedValue(), teamB.getInitialSeedValue());
        if (comparison == 0) return rand.nextBoolean();
        return comparison < 0;
    }

    public String getName() {
        return this.name;
    }

    // equals- and hashCode-methods could be implemented better, however, a simple namecheck suffices
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TieBreakerBySeed that = (TieBreakerBySeed) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
