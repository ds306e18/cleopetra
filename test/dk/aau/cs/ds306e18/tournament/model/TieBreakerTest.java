package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class TieBreakerTest {

    @Test
    public void goalDiff01() {

        List<Team> teams = TestUtilities.getTestTeams(6, 1);
        Match matchOne = new Match(teams.get(0), teams.get(1));
        Match matchTwo = new Match(teams.get(2), teams.get(3));
        Match matchThree = new Match(teams.get(4), teams.get(5));

        matchOne.setScores(9, 5, true);
        matchTwo.setScores(4, 0, true);
        matchThree.setScores(4, 3, true);

        List<Team> ordered = TieBreaker.GOAL_DIFF.compareAll(teams, 6);

        assertSame(ordered.get(0), teams.get(0)); // goal diff of 4, goals scored of 9
        assertSame(ordered.get(1), teams.get(2)); // goal diff of 4, goals scored of 4
        assertSame(ordered.get(2), teams.get(4)); // goal diff of 1, goals scored of 4
        assertSame(ordered.get(3), teams.get(5)); // goal diff of -1, goals scored of 3
        assertSame(ordered.get(4), teams.get(1)); // goal diff of -4, goals scored of 5
        assertSame(ordered.get(5), teams.get(3)); // goal diff of -4, goals scored of 0
    }

    @Test
    public void goalScored01() {
        List<Team> teams = TestUtilities.getTestTeams(4, 1);
        Match matchOne = new Match(teams.get(0), teams.get(1));
        Match matchTwo = new Match(teams.get(2), teams.get(3));

        matchOne.setScores(5, 1, true);
        matchTwo.setScores(4, 2, true);

        List<Team> ordered = TieBreaker.GOALS_SCORED.compareAll(teams, 4);

        assertSame(ordered.get(0), teams.get(0)); // goals scored of 5
        assertSame(ordered.get(1), teams.get(2)); // goals scored of 4
        assertSame(ordered.get(2), teams.get(3)); // goals scored of 2
        assertSame(ordered.get(3), teams.get(1)); // goals scored of 1
    }

    @Test
    public void bySeed01() {
        List<Team> teams = TestUtilities.getTestTeams(4, 1);
        for (int i = 0; i < teams.size(); i++) {
            teams.get(i).setInitialSeedValue(i + 1);
        }

        Match matchOne = new Match(teams.get(0), teams.get(1));
        Match matchTwo = new Match(teams.get(2), teams.get(3));

        // Scores shouldn't matter
        matchOne.setScores(2, 3, true);
        matchTwo.setScores(4, 1, true);

        List<Team> ordered = TieBreaker.SEED.compareAll(teams, 4);

        assertSame(ordered.get(0), teams.get(0)); // seed 1
        assertSame(ordered.get(1), teams.get(1)); // seed 2
        assertSame(ordered.get(2), teams.get(2)); // seed 3
        assertSame(ordered.get(3), teams.get(3)); // seed 4
    }

    @Test
    public void compareWithPoints01() {
        List<Team> teams = TestUtilities.getTestTeams(6, 1);
        teams.get(0).addGoalsScored(2);
        teams.get(1).addGoalsScored(3);
        teams.get(2).addGoalsScored(4);
        teams.get(3).addGoalsScored(2);
        teams.get(4).addGoalsScored(3);
        teams.get(5).addGoalsScored(4);

        HashMap<Team, Integer> pointsMap = new HashMap<>();
        pointsMap.put(teams.get(0), 1);
        pointsMap.put(teams.get(1), 0);
        pointsMap.put(teams.get(2), 1);
        pointsMap.put(teams.get(3), 0);
        pointsMap.put(teams.get(4), 1);
        pointsMap.put(teams.get(5), 0);

        List<Team> best = TieBreaker.GOALS_SCORED.compareWithPoints(teams, 5, pointsMap);

        assertSame(teams.get(2), best.get(0));
        assertSame(teams.get(4), best.get(1));
        assertSame(teams.get(0), best.get(2));
        assertSame(teams.get(5), best.get(3));
        assertSame(teams.get(1), best.get(4));
        assertEquals(5, best.size());
    }

    @Test
    public void compareWithPoints02() {
        List<Team> teams = TestUtilities.getTestTeams(6, 1);
        teams.get(0).addGoalsScored(1);
        teams.get(1).addGoalsScored(3);
        teams.get(2).addGoalsScored(2);
        teams.get(3).addGoalsScored(5);
        teams.get(4).addGoalsScored(4);
        teams.get(5).addGoalsScored(0);

        HashMap<Team, Integer> pointsMap = new HashMap<>();
        pointsMap.put(teams.get(0), 0);
        pointsMap.put(teams.get(1), 0);
        pointsMap.put(teams.get(2), 1);
        pointsMap.put(teams.get(3), 0);
        pointsMap.put(teams.get(4), 1);
        pointsMap.put(teams.get(5), 1);

        List<Team> best = TieBreaker.GOALS_SCORED.compareWithPoints(teams, 5, pointsMap);

        assertSame(teams.get(4), best.get(0));
        assertSame(teams.get(2), best.get(1));
        assertSame(teams.get(5), best.get(2));
        assertSame(teams.get(3), best.get(3));
        assertSame(teams.get(1), best.get(4));
        assertEquals(5, best.size());
    }
}