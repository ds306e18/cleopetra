package dk.aau.cs.ds306e18.tournament.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum TieBreaker {
    // Goal diff -> goals scored -> seed
    GOAL_DIFF("Goal difference", (A, B) -> {
        int comparison = Integer.compare(A.getGoalDiff(), B.getGoalDiff());
        if (comparison == 0) {
            // Goal diff is same, use goals scored
            comparison = Integer.compare(A.getGoalsScored(), B.getGoalsScored());
            if (comparison == 0) {
                // Goals scores is same, use seed
                comparison = Integer.compare(B.getInitialSeedValue(), A.getInitialSeedValue());
            }
        }
        return comparison;
    }),
    // Goals scored -> seed
    GOALS_SCORED("Goals scored", (A, B) -> {
        int comparison = Integer.compare(A.getGoalsScored(), B.getGoalsScored());
        if (comparison == 0) {
            // Goals scores is same, use seed
            comparison = Integer.compare(B.getInitialSeedValue(), A.getInitialSeedValue());
        }
        return comparison;
    }),
    SEED("By seed", (A, B) -> {
        return Integer.compare(B.getInitialSeedValue(), A.getInitialSeedValue());
    })
    ;

    private interface TieBreakerFunction {
        /** Should return 1 if team A wins, -1 if team B wins, and preferable never 0. */
        int compare(Team A, Team B);
    }

    private final String uiName;
    private final TieBreakerFunction compareFunction;

    TieBreaker(String uiName, TieBreakerFunction compareFunction) {
        this.uiName = uiName;
        this.compareFunction = compareFunction;
    }

    public List<Team> compareAll(List<Team> teams, int count) {
        ArrayList<Team> sorted = new ArrayList<>(teams);
        sorted.sort((a, b) -> a == b ? 0 : compare(a, b) ? -1 : 1);
        return sorted.subList(0, count);
    }

    /** Returns a list of teams sorted by their points, and if two teams' points are equal, this tiebreaker has been used
     * to determine the better team of the two. */
    public List<Team> compareWithPoints(List<Team> teams, int count, Map<Team, Integer> pointsMap) {
        List<Team> sorted = new ArrayList<>(teams);
        sorted.sort((a, b) -> {
            if (a == b) return 0;
            int comparison = Integer.compare(pointsMap.get(b), pointsMap.get(a));
            if (comparison == 0) {
                return compare(a, b) ? -1 : 1;
            }
            return comparison;
        });

        count = Math.min(count, teams.size());
        return sorted.subList(0, count);
    }

    @Override
    public String toString() {
        return uiName;
    }

    /** Returns true if teamA wins over teamB in the tie breaker. */
    public boolean compare(Team teamA, Team teamB) {
        int comparison = compareFunction.compare(teamA, teamB);
        if (comparison == 0) {
            // Tie breaking was somehow not enough, use hash as last option
            comparison = Integer.compare(teamA.hashCode(), teamB.hashCode());
        }
        return comparison > 0;
    }
}
