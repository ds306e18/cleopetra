package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class TieBreakerTest {

    @Test
    public void goalDiff01() {

        List<Team> teams = TestUtilities.getTestTeams(6, 1);
        Series seriesOne = new Series(teams.get(0), teams.get(1));
        Series seriesTwo = new Series(teams.get(2), teams.get(3));
        Series seriesThree = new Series(teams.get(4), teams.get(5));
        List<Series> series = Arrays.asList(seriesOne, seriesTwo, seriesThree);

        seriesOne.setScores(9, 5, 0);
        seriesOne.setHasBeenPlayed(true);
        seriesTwo.setScores(4, 0, 0);
        seriesTwo.setHasBeenPlayed(true);
        seriesThree.setScores(4, 3, 0);
        seriesThree.setHasBeenPlayed(true);

        for (Team team : teams) {
            team.getStatsManager().trackAllSeries(null, series);
        }

        List<Team> ordered = TieBreaker.GOAL_DIFF.compareAll(teams, null);

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
        Series seriesOne = new Series(teams.get(0), teams.get(1));
        Series seriesTwo = new Series(teams.get(2), teams.get(3));
        List<Series> series = Arrays.asList(seriesOne, seriesTwo);

        seriesOne.setScores(5, 1, 0);
        seriesOne.setHasBeenPlayed(true);
        seriesTwo.setScores(4, 2, 0);
        seriesTwo.setHasBeenPlayed(true);

        for (Team team : teams) {
            team.getStatsManager().trackAllSeries(null, series);
        }

        List<Team> ordered = TieBreaker.GOALS_SCORED.compareAll(teams, null);

        assertSame(ordered.get(0), teams.get(0)); // goals scored of 5
        assertSame(ordered.get(1), teams.get(2)); // goals scored of 4
        assertSame(ordered.get(2), teams.get(3)); // goals scored of 2
        assertSame(ordered.get(3), teams.get(1)); // goals scored of 1
    }

    @Test
    public void bySeed01() {
        List<Team> teams = TestUtilities.getTestTeams(4, 1);
        Series seriesOne = new Series(teams.get(0), teams.get(1));
        Series seriesTwo = new Series(teams.get(2), teams.get(3));
        List<Series> series = Arrays.asList(seriesOne, seriesTwo);

        // Scores shouldn't matter
        seriesOne.setScores(2, 3, 0);
        seriesOne.setHasBeenPlayed(true);
        seriesTwo.setScores(4, 1, 0);
        seriesTwo.setHasBeenPlayed(true);

        for (Team team : teams) {
            team.getStatsManager().trackAllSeries(null, series);
        }

        List<Team> ordered = TieBreaker.SEED.compareAll(teams, null);

        assertSame(ordered.get(0), teams.get(0)); // seed 1
        assertSame(ordered.get(1), teams.get(1)); // seed 2
        assertSame(ordered.get(2), teams.get(2)); // seed 3
        assertSame(ordered.get(3), teams.get(3)); // seed 4
    }

    @Test
    public void compareWithPoints01() {
        List<Team> teams = TestUtilities.getTestTeams(6, 1);
        Series seriesOne = new Series(teams.get(0), teams.get(1));
        Series seriesTwo = new Series(teams.get(2), teams.get(3));
        Series seriesThree = new Series(teams.get(4), teams.get(5));
        List<Series> series = Arrays.asList(seriesOne, seriesTwo, seriesThree);

        seriesOne.setScores(2, 3, 0);
        seriesOne.setHasBeenPlayed(true);
        seriesTwo.setScores(4, 2, 0);
        seriesTwo.setHasBeenPlayed(true);
        seriesThree.setScores(3, 4, 0);
        seriesThree.setHasBeenPlayed(true);

        for (Team team : teams) {
            team.getStatsManager().trackAllSeries(null, series);
        }

        // Team 0 and team 3 both have 2 goals, but team 0 has one point
        // Team 1 and team 4 both have 3 goals, but team 4 has one point
        // Team 2 and team 5 both have 4 goals, but team 2 has one point
        HashMap<Team, Integer> pointsMap = new HashMap<>();
        pointsMap.put(teams.get(0), 1);
        pointsMap.put(teams.get(1), 0);
        pointsMap.put(teams.get(2), 1);
        pointsMap.put(teams.get(3), 0);
        pointsMap.put(teams.get(4), 1);
        pointsMap.put(teams.get(5), 0);

        List<Team> best = TieBreaker.GOALS_SCORED.compareWithPoints(teams, pointsMap, null);

        assertSame(teams.get(2), best.get(0));
        assertSame(teams.get(4), best.get(1));
        assertSame(teams.get(0), best.get(2));
        assertSame(teams.get(5), best.get(3));
        assertSame(teams.get(1), best.get(4));
        assertSame(teams.get(3), best.get(5));

    }

    @Test
    public void compareWithPoints02() {
        List<Team> teams = TestUtilities.getTestTeams(6, 1);
        Series seriesOne = new Series(teams.get(0), teams.get(1));
        Series seriesTwo = new Series(teams.get(2), teams.get(3));
        Series seriesThree = new Series(teams.get(4), teams.get(5));
        List<Series> series = Arrays.asList(seriesOne, seriesTwo, seriesThree);

        seriesOne.setScores(1, 3, 0);
        seriesOne.setHasBeenPlayed(true);
        seriesTwo.setScores(2, 5, 0);
        seriesTwo.setHasBeenPlayed(true);
        seriesThree.setScores(4, 0, 0);
        seriesThree.setHasBeenPlayed(true);

        for (Team team : teams) {
            team.getStatsManager().trackAllSeries(null, series);
        }

        // Teams with points should be ahead, but their goals decide the order next
        HashMap<Team, Integer> pointsMap = new HashMap<>();
        pointsMap.put(teams.get(0), 0);
        pointsMap.put(teams.get(1), 0);
        pointsMap.put(teams.get(2), 1);
        pointsMap.put(teams.get(3), 0);
        pointsMap.put(teams.get(4), 1);
        pointsMap.put(teams.get(5), 1);

        List<Team> best = TieBreaker.GOALS_SCORED.compareWithPoints(teams, pointsMap, null);

        assertSame(teams.get(4), best.get(0));
        assertSame(teams.get(2), best.get(1));
        assertSame(teams.get(5), best.get(2));
        assertSame(teams.get(3), best.get(3));
        assertSame(teams.get(1), best.get(4));
        assertSame(teams.get(0), best.get(5));
    }
}