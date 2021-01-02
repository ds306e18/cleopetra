package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.stats.Stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum TieBreaker {
    // Goal diff -> goals scored -> seed
    GOAL_DIFF("Goal difference", (A, B, format) -> {
        Stats stataA = format == null ?
                A.getStatsManager().getGlobalStats() :
                A.getStatsManager().getStats(format);
        Stats statsB = format == null ?
                B.getStatsManager().getGlobalStats() :
                B.getStatsManager().getStats(format);

        int comparison = Integer.compare(stataA.getGoalDifference(), statsB.getGoalDifference());
        if (comparison == 0) {
            // Goal diff is same, use goals scored
            comparison = Integer.compare(stataA.getGoals(), statsB.getGoals());
            if (comparison == 0) {
                // Goals scores is same, use seed
                comparison = Integer.compare(B.getInitialSeedValue(), A.getInitialSeedValue());
            }
        }
        return comparison;
    }),
    // Goals scored -> seed
    GOALS_SCORED("Goals scored", (A, B, format) -> {
        Stats stataA = format == null ?
                A.getStatsManager().getGlobalStats() :
                A.getStatsManager().getStats(format);
        Stats statsB = format == null ?
                B.getStatsManager().getGlobalStats() :
                B.getStatsManager().getStats(format);

        int comparison = Integer.compare(stataA.getGoals(), statsB.getGoals());
        if (comparison == 0) {
            // Goals scores is same, use seed
            comparison = Integer.compare(B.getInitialSeedValue(), A.getInitialSeedValue());
        }
        return comparison;
    }),
    SEED("By seed", (A, B, format) -> {
        return Integer.compare(B.getInitialSeedValue(), A.getInitialSeedValue());
    })
    ;

    private interface TieBreakerFunction {
        /** Should return 1 if team A wins, -1 if team B wins, and preferable never 0. The comparison will use stats
         * from the given stage/format. If format is null, global stats are used. */
        int compare(Team A, Team B, Format format);
    }

    private final String uiName;
    private final TieBreakerFunction compareFunction;

    TieBreaker(String uiName, TieBreakerFunction compareFunction) {
        this.uiName = uiName;
        this.compareFunction = compareFunction;
    }

    /**
     * Compares the teams in a list and returns a new list where the teams has been sorted based on the tiebreaker.
     * Stats are taken from the given format. If format is null, global stats are used instead.
     */
    public List<Team> compareAll(List<Team> teams, Format format) {
        ArrayList<Team> sorted = new ArrayList<>(teams);
        sorted.sort((a, b) -> a == b ? 0 : compare(a, b, format) ? -1 : 1);
        return sorted;
    }

    /**
     * Returns a list of teams sorted by their points, and if two teams' points are equal, this tiebreaker has been used
     * to determine the better team of the two. Stats are taken from the given format. If format is null,
     * global stats are used instead.
     */
    public List<Team> compareWithPoints(List<Team> teams, Map<Team, Integer> pointsMap, Format format) {
        List<Team> sorted = new ArrayList<>(teams);
        sorted.sort((a, b) -> {
            if (a == b) return 0;
            int comparison = Integer.compare(pointsMap.get(b), pointsMap.get(a));
            if (comparison == 0) {
                return compare(a, b, format) ? -1 : 1;
            }
            return comparison;
        });
        return sorted;
    }

    @Override
    public String toString() {
        return uiName;
    }

    /**
     * Returns true if teamA wins over teamB in the tie breaker. Stats are taken from the given format. If format is
     * null, global stats are used instead.
     */
    public boolean compare(Team teamA, Team teamB, Format format) {
        int comparison = compareFunction.compare(teamA, teamB, format);
        if (comparison == 0) {
            // Tie breaking was somehow not enough, use hash as last option
            comparison = Integer.compare(teamA.hashCode(), teamB.hashCode());
        }
        return comparison > 0;
    }
}
