package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.model.stats.StatsTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class DoubleEliminationFormatTest {

    @Test
    public void amountOfMatches01() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.getTestTeams(8, 1), true);
        assertEquals(15, de.getAllSeries().size());
    }

    @Test
    public void amountOfMatches02() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.getTestTeams(30, 1), true);
        assertEquals(59, de.getAllSeries().size());
    }

    @Test
    public void amountOfMatches03() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.getTestTeams(12, 1), true);
        assertEquals(23, de.getAllSeries().size());
    }

    @Test
    public void amountOfMatches04() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.getTestTeams(19, 1), true);
        assertEquals(37, de.getAllSeries().size());
    }

    @Test
    public void matchDependencies01() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.getTestTeams(8, 1), true);
        Series extra = de.getExtraSeries();
        List<Series> series = de.getAllSeries();
        for (Series serie : series) {
            if (serie != extra) {
                assertTrue(extra.dependsOn(serie));
            }
        }
    }

    @Test
    public void matchDependencies02() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.getTestTeams(17, 1), true);
        Series extra = de.getExtraSeries();
        List<Series> series = de.getAllSeries();
        for (Series serie : series) {
            if (serie != extra) {
                assertTrue(extra.dependsOn(serie));
            }
        }
    }

    @Test
    public void seedPlacingTest01() {
        List<Team> teams = TestUtilities.getTestTeams(8, 1);
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(teams, true);
        Series[] upperBracket = de.getUpperBracket();
        int len = upperBracket.length;
        assertSame(1, upperBracket[len-1].getTeamOne().getInitialSeedValue());
        assertSame(8, upperBracket[len-1].getTeamTwo().getInitialSeedValue());
        assertSame(4, upperBracket[len-2].getTeamOne().getInitialSeedValue());
        assertSame(5, upperBracket[len-2].getTeamTwo().getInitialSeedValue());
        assertSame(2, upperBracket[len-3].getTeamOne().getInitialSeedValue());
        assertSame(7, upperBracket[len-3].getTeamTwo().getInitialSeedValue());
        assertSame(3, upperBracket[len-4].getTeamOne().getInitialSeedValue());
        assertSame(6, upperBracket[len-4].getTeamTwo().getInitialSeedValue());
    }

    @Test
    public void seedPlacingTest02() {
        List<Team> teams = TestUtilities.getTestTeams(5, 1);
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(teams, true);
        Series[] upperBracket = de.getUpperBracket();
        int len = upperBracket.length;
        assertSame(1, upperBracket[len-5].getTeamOne().getInitialSeedValue());
        assertSame(2, upperBracket[len-6].getTeamOne().getInitialSeedValue());
        assertSame(3, upperBracket[len-6].getTeamTwo().getInitialSeedValue());
        assertSame(4, upperBracket[len-2].getTeamOne().getInitialSeedValue());
        assertSame(5, upperBracket[len-2].getTeamTwo().getInitialSeedValue());
    }

    @Test
    public void onlyTwoTeams() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.getTestTeams(2, 1), true);
        assertEquals(3, de.getAllSeries().size());
        assertEquals(0, de.getLowerBracket().length);
    }

    @Test
    public void isExtraMatchNeeded01() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.getTestTeams(4, 1), true);

        assertFalse(de.isExtraMatchNeeded());

        // Play all matches except the last (the extra match)
        List<Series> series = de.getAllSeries();
        for (int i = series.size() - 1; i > 0; i--) {
            series.get(i).setScores(1, 0, 0);
            series.get(i).setHasBeenPlayed(true);
        }

        assertFalse(de.isExtraMatchNeeded());
    }

    @Test
    public void isExtraMatchNeeded02() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.getTestTeams(4, 1), true);

        // Play all matches except the last (the extra match)
        List<Series> series = de.getAllSeries();
        for (int i = series.size() - 1; i > 0; i--) {
            series.get(i).setScores(1, 0, 0);
            series.get(i).setHasBeenPlayed(true);
        }

        assertFalse(de.isExtraMatchNeeded());

        // Change result of final match so lower bracket winner wins
        series.get(1).setScores(0, 1, 0);
        series.get(1).setHasBeenPlayed(true);

        assertTrue(de.isExtraMatchNeeded());
    }

    @Test
    public void upcomingAndPendingMatches01() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.getTestTeams(4, 1), true);
        assertEquals(2, de.getUpcomingMatches().size());
        assertEquals(4, de.getPendingMatches().size());
    }

    @Test
    public void upcomingAndPendingMatches02() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.getTestTeams(7, 1), true);
        assertEquals(3, de.getUpcomingMatches().size());
        assertEquals(9, de.getPendingMatches().size());
    }

    @Test
    public void upcomingAndPendingMatches03() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.getTestTeams(13, 1), true);
        assertEquals(5, de.getUpcomingMatches().size());
        assertEquals(19, de.getPendingMatches().size());
    }

    @Test
    public void losesMap01() {
        List<Team> teams = TestUtilities.getTestTeams(14, 1);
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(teams, true);

        // Play all matches except the last (the extra match)
        List<Series> series = de.getAllSeries();
        for (int i = series.size() - 1; i > 0; i--) {
            series.get(i).setScores(1, 0, 0);
            series.get(i).setHasBeenPlayed(true);
        }

        // All matches have now been played, so all teams has 2 loses, except the winner
        HashMap<Team, Integer> losesMap = de.getLosesMap();
        for (Team team : losesMap.keySet()) {
            if (team.getInitialSeedValue() == 1) {
                assertEquals(0, (int)losesMap.get(team));
            } else {
                assertEquals(2, (int)losesMap.get(team));
            }
        }
    }

    @Test
    public void topTeams01() {
        List<Team> teams = TestUtilities.getTestTeams(4, 1);
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(teams, true);

        // Play all matches except the last (the extra match)
        List<Series> series = de.getAllSeries();
        for (int i = series.size() - 1; i > 0; i--) {
            Series serie = series.get(i);
            if (serie.getTeamOne().getInitialSeedValue() < serie.getTeamTwo().getInitialSeedValue()) {
                serie.setScores(1, 0, 0);
            } else {
                serie.setScores(0, 1, 0);
            }
            serie.setHasBeenPlayed(true);
        }

        List<Team> topTeams = de.getTopTeams(4, TieBreaker.GOALS_SCORED);
        for (int i = 0; i < topTeams.size(); i++) {
            assertSame(teams.get(i), topTeams.get(i));
        }
    }

    @Test
    public void topTeams02() {
        List<Team> teams = TestUtilities.getTestTeams(32, 1);
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(teams, true);

        // Play all matches except the last (the extra match)
        List<Series> series = de.getAllSeries();
        for (int i = series.size() - 1; i > 0; i--) {
            Series serie = series.get(i);
            if (serie.getTeamOne().getInitialSeedValue() < serie.getTeamTwo().getInitialSeedValue()) {
                serie.setScores(1, 0, 0);
            } else {
                serie.setScores(0, 1, 0);
            }
            serie.setHasBeenPlayed(true);
        }

        List<Team> topTeams = de.getTopTeams(8, TieBreaker.GOALS_SCORED);
        for (int i = 0; i < topTeams.size(); i++) {
            assertSame(teams.get(i), topTeams.get(i));
        }
    }

    @Test
    public void stats01() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        List<Team> teams = TestUtilities.getTestTeams(4, 1);
        de.start(teams, true);

        // Play all matches. The highest seeded team wins 1-0
        List<Series> allSeries = de.getAllSeries();
        for (int matchIndex = allSeries.size() - 1; matchIndex > 0; matchIndex--) {
            Series series = allSeries.get(matchIndex);
            if (series.getTeamOne().getInitialSeedValue() < series.getTeamTwo().getInitialSeedValue()) {
                series.setScores(1, 0, 0);
            } else {
                series.setScores(0, 1, 0);
            }
            series.setHasBeenPlayed(true);
        }

        // Check if stats are as expected
        StatsTest.assertStats(teams.get(0), de, 3, 0, 3, 0);
        StatsTest.assertStats(teams.get(1), de, 2, 2, 2, 2);
        StatsTest.assertStats(teams.get(2), de, 1, 2, 1, 2);
        StatsTest.assertStats(teams.get(3), de, 0, 2, 0, 2);
    }
}