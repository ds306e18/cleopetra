package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.model.stats.StatsTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SingleEliminationFormatTest {

    //there should be a correct amount of matches
    @Test
    public void amountOfMatchesTest01() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8, 1), true);
        assertEquals(7, bracket.getAllSeries().size());
    }

    //there should be a correct amount of matches
    @Test
    public void amountOfMatchesTest02() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(16, 1), true);
        assertEquals(15, bracket.getAllSeries().size());
    }

    //final match should depend on all matches
    @Test
    public void dependsOnFinalMatch01() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8, 1), true);
        List<Series> series = bracket.getAllSeries();
        for (int n = 1; n < series.size(); n++) {
            assertTrue(series.get(0).dependsOn(series.get(n)));
        }
    }

    //final match should depend on all matches
    @Test
    public void dependsOnFinalMatch02() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(16, 1), true);
        List<Series> series = bracket.getAllSeries();
        for (int n = 1; n < series.size(); n++) {
            assertTrue(series.get(0).dependsOn(series.get(n)));
        }
    }

    //final match should depend on all matches
    @Test
    public void dependsOnFinalMatch03() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(10, 1), true);
        List<Series> series = bracket.getAllSeries();
        for (int n = 1; n < series.size(); n++) {
            assertTrue(series.get(0).dependsOn(series.get(n)));
        }
    }

    //The teams should be seeded correctly in first round
    @Test
    public void seedTest01() {
        List<Team> teamList = TestUtilities.getTestTeams(8, 1);
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(teamList, true);
        assertEquals(1, bracket.getAllSeries().get(bracket.getAllSeries().size() - 1).getTeamOne().getInitialSeedValue());
        assertEquals(8, bracket.getAllSeries().get(bracket.getAllSeries().size() - 1).getTeamTwo().getInitialSeedValue());
        assertEquals(4, bracket.getAllSeries().get(bracket.getAllSeries().size() - 2).getTeamOne().getInitialSeedValue());
        assertEquals(5, bracket.getAllSeries().get(bracket.getAllSeries().size() - 2).getTeamTwo().getInitialSeedValue());
        assertEquals(2, bracket.getAllSeries().get(bracket.getAllSeries().size() - 3).getTeamOne().getInitialSeedValue());
        assertEquals(7, bracket.getAllSeries().get(bracket.getAllSeries().size() - 3).getTeamTwo().getInitialSeedValue());
        assertEquals(3, bracket.getAllSeries().get(bracket.getAllSeries().size() - 4).getTeamOne().getInitialSeedValue());
        assertEquals(6, bracket.getAllSeries().get(bracket.getAllSeries().size() - 4).getTeamTwo().getInitialSeedValue());
    }

    //first match should be null, and best seeded team should be placed in next round
    @Test
    public void seedTest02() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(7, 1), true);
        assertNull(bracket.getMatchesAsArray()[6]);
        assertEquals(1, bracket.getMatchesAsArray()[2].getTeamOne().getInitialSeedValue());
    }

    //match 3 should be null and snd seed should be placed in next round
    @Test
    public void seedTest03() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(6, 1), true);
        assertNull(bracket.getMatchesAsArray()[4]);
        assertEquals(2, bracket.getMatchesAsArray()[1].getTeamOne().getInitialSeedValue());
    }

    //There should only be one match in first around, this should be the worst seeded teams.
    //The winner of this match should meet seed 1
    @Test
    public void seedTest04() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(5, 1), true);
        assertNull(bracket.getMatchesAsArray()[6]);
        assertNull(bracket.getMatchesAsArray()[4]);
        assertNull(bracket.getMatchesAsArray()[3]);
        assertEquals(4, bracket.getMatchesAsArray()[5].getTeamOne().getInitialSeedValue());
        assertEquals(5, bracket.getMatchesAsArray()[5].getTeamTwo().getInitialSeedValue());
    }

    //Should return the correct amount of playable matches
    @Test
    public void upcomingMatchesTest01() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8, 1), true);
        assertEquals(4, bracket.getUpcomingMatches().size());
        Series series = bracket.getUpcomingMatches().get(0);
        series.setScores(1, 0, 0);
        series.setHasBeenPlayed(true);
        assertEquals(3, bracket.getUpcomingMatches().size());
    }

    //Should return the correct amount of playable matches
    @Test
    public void upcomingMatchesTest02() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(6, 1), true);
        assertEquals(2, bracket.getUpcomingMatches().size());
    }

    //Should return the correct amount of not-playable upcoming matches
    @Test
    public void pendingMatchesTest01() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8, 1), true);
        assertEquals(3, bracket.getPendingMatches().size());
        List<Series> allSeries = bracket.getAllSeries();
        int seriesCount = allSeries.size();
        allSeries.get(seriesCount - 1).setScores(1, 2, 0);
        allSeries.get(seriesCount - 1).setHasBeenPlayed(true);
        allSeries.get(seriesCount - 2).setScores(1, 2, 0);
        allSeries.get(seriesCount - 2).setHasBeenPlayed(true);
        assertEquals(2, bracket.getPendingMatches().size());
    }

    //Should return the correct amount of played matches
    @Test
    public void completedMatchesTest01() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8, 1), true);
        assertEquals(0, bracket.getCompletedMatches().size());
        List<Series> allSeries = bracket.getAllSeries();
        int seriesCount = allSeries.size();
        allSeries.get(seriesCount - 1).setScores(1, 2, 0);
        allSeries.get(seriesCount - 1).setHasBeenPlayed(true);
        allSeries.get(seriesCount - 2).setScores(1, 2, 0);
        allSeries.get(seriesCount - 2).setHasBeenPlayed(true);
        assertEquals(2, bracket.getCompletedMatches().size());
    }

    //Gets top 4 teams
    @Test
    public void getTopTeamsTest01() {
        SingleEliminationFormat bracket = generateBracketsAndWins(8);
        List<Team> teamList = new ArrayList<>(bracket.getTopTeams(4, TieBreaker.SEED));
        int seedValue = 1;
        for (int i = 0; i < 4; i++) {
            assertEquals(seedValue, teamList.get(i).getInitialSeedValue());
            seedValue++;
        }
    }

    //Gets top 6 teams
    @Test
    public void getTopTeamTest02() {
        SingleEliminationFormat bracket = generateBracketsAndWins(10);
        List<Team> teamList = new ArrayList<Team>(bracket.getTopTeams(6, TieBreaker.SEED));
        int seedValue = 1;
        for (int i = 0; i < 6; i++) {
            assertEquals(seedValue, teamList.get(i).getInitialSeedValue());
            seedValue++;
        }
    }

    //Tries to get top 10 teams in a 4 team stage, should only return 4 teams
    @Test
    public void getTopTeamTest03() {
        SingleEliminationFormat bracket = generateBracketsAndWins(5);
        List<Team> teamList = new ArrayList<Team>(bracket.getTopTeams(10, TieBreaker.SEED));
        int seedValue = 1;
        for (int i = 0; i < 4; i++) {
            assertEquals(seedValue, teamList.get(i).getInitialSeedValue());
            seedValue++;
        }
    }

    //StageStatus test  pending-running-concluded
    @Test
    public void stageStatusTest01() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        assertEquals(StageStatus.PENDING, bracket.getStatus());
        bracket.start(TestUtilities.getTestTeams(4, 1), true);
        assertEquals(StageStatus.RUNNING, bracket.getStatus());

        bracket = generateBracketsAndWins(4);
        assertEquals(StageStatus.CONCLUDED, bracket.getStatus());
    }

    //StageStatus should still be running if some matches has been played
    @Test
    public void stageStatusTest02() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8, 1), true);
        List<Series> arrayList = bracket.getAllSeries();
        arrayList.get(arrayList.size() - 1).setScores(1, 0, 0);
        arrayList.get(arrayList.size() - 1).setHasBeenPlayed(true);
        arrayList.get(arrayList.size() - 2).setScores(1, 0, 0);
        arrayList.get(arrayList.size() - 2).setHasBeenPlayed(true);
        assertEquals(StageStatus.RUNNING, bracket.getStatus());
    }

    //Should throw exception if the match is not playable
    @Test(expected = IllegalStateException.class)
    public void unPlayableMatch() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8, 1), true);
        bracket.getAllSeries().get(0).setScores(1, 0, 0);
    }

    //Should throw exception if the match is not playable
    @Test(expected = IllegalStateException.class)
    public void unPlayableMatch02() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8, 1), true);
        bracket.getAllSeries().get(2).setHasBeenPlayed(true);
    }

    @Test
    public void withSeeding01() {

        int numberOfTeams = 4;
        List<Team> teams = TestUtilities.getTestTeams(numberOfTeams, 1);

        SingleEliminationFormat singleelimination = new SingleEliminationFormat();
        singleelimination.start(teams, true);

        Series seriesOne = singleelimination.getAllSeries().get(2);
        assertSame(teams.get(0), seriesOne.getTeamOne());
        assertSame(teams.get(3), seriesOne.getTeamTwo());

        Series seriesTwo = singleelimination.getAllSeries().get(1);
        assertSame(teams.get(1), seriesTwo.getTeamOne());
        assertSame(teams.get(2), seriesTwo.getTeamTwo());
    }

    @Test
    public void withSeeding02() {

        int numberOfTeams = 8;
        List<Team> teams = TestUtilities.getTestTeams(numberOfTeams, 1);

        SingleEliminationFormat singleelimination = new SingleEliminationFormat();
        singleelimination.start(teams, true);

        Series seriesOne = singleelimination.getAllSeries().get(6);
        assertSame(teams.get(0), seriesOne.getTeamOne());
        assertSame(teams.get(7), seriesOne.getTeamTwo());

        Series seriesTwo = singleelimination.getAllSeries().get(5);
        assertSame(teams.get(3), seriesTwo.getTeamOne());
        assertSame(teams.get(4), seriesTwo.getTeamTwo());

        Series seriesThree = singleelimination.getAllSeries().get(4);
        assertSame(teams.get(1), seriesThree.getTeamOne());
        assertSame(teams.get(6), seriesThree.getTeamTwo());

        Series seriesFour = singleelimination.getAllSeries().get(3);
        assertSame(teams.get(2), seriesFour.getTeamOne());
        assertSame(teams.get(5), seriesFour.getTeamTwo());
    }

    @Test
    public void withoutSeeding01() {
        int numberOfTeams = 4;
        List<Team> teams = TestUtilities.getTestTeams(numberOfTeams, 1);

        SingleEliminationFormat singleelimination = new SingleEliminationFormat();
        singleelimination.start(teams, false);

        Series seriesOne = singleelimination.getAllSeries().get(2);
        assertSame(teams.get(0), seriesOne.getTeamOne());
        assertSame(teams.get(1), seriesOne.getTeamTwo());

        Series seriesTwo = singleelimination.getAllSeries().get(1);
        assertSame(teams.get(2), seriesTwo.getTeamOne());
        assertSame(teams.get(3), seriesTwo.getTeamTwo());
    }

    @Test
    public void withoutSeeding02() {

        int numberOfTeams = 8;
        List<Team> teams = TestUtilities.getTestTeams(numberOfTeams, 1);

        SingleEliminationFormat singleelimination = new SingleEliminationFormat();
        singleelimination.start(teams, false);

        Series seriesOne = singleelimination.getAllSeries().get(6);
        assertSame(teams.get(0), seriesOne.getTeamOne());
        assertSame(teams.get(1), seriesOne.getTeamTwo());

        Series seriesTwo = singleelimination.getAllSeries().get(5);
        assertSame(teams.get(2), seriesTwo.getTeamOne());
        assertSame(teams.get(3), seriesTwo.getTeamTwo());

        Series seriesThree = singleelimination.getAllSeries().get(4);
        assertSame(teams.get(4), seriesThree.getTeamOne());
        assertSame(teams.get(5), seriesThree.getTeamTwo());

        Series seriesFour = singleelimination.getAllSeries().get(3);
        assertSame(teams.get(6), seriesFour.getTeamOne());
        assertSame(teams.get(7), seriesFour.getTeamTwo());
    }

    @Test
    public void matchIdentifiers01() {
        List<Team> teams = TestUtilities.getTestTeams(4, 1);
        SingleEliminationFormat singleElimination = new SingleEliminationFormat();
        singleElimination.start(teams, true);

        List<Series> allSeries = singleElimination.getAllSeries();

        assertEquals(3, allSeries.get(0).getIdentifier());
        assertEquals(2, allSeries.get(1).getIdentifier());
        assertEquals(1, allSeries.get(2).getIdentifier());
    }

    @Test
    public void matchIdentifiers02() {
        List<Team> teams = TestUtilities.getTestTeams(6, 1);
        SingleEliminationFormat singleElimination = new SingleEliminationFormat();
        singleElimination.start(teams, true);

        List<Series> allSeries = singleElimination.getAllSeries();

        assertEquals(5, allSeries.get(0).getIdentifier());
        assertEquals(4, allSeries.get(1).getIdentifier());
        assertEquals(3, allSeries.get(2).getIdentifier());
        assertEquals(2, allSeries.get(3).getIdentifier());
        assertEquals(1, allSeries.get(4).getIdentifier());
    }

    @Test
    public void stats01() {
        SingleEliminationFormat se = new SingleEliminationFormat();
        List<Team> teams = TestUtilities.getTestTeams(4, 1);
        se.start(teams, true);

        // Play all matches. The highest seeded team wins 1-0
        List<Series> allSeries = se.getAllSeries();
        for (int matchIndex = allSeries.size() - 1; matchIndex >= 0; matchIndex--) {
            Series series = allSeries.get(matchIndex);
            if (series.getTeamOne().getInitialSeedValue() < series.getTeamTwo().getInitialSeedValue()) {
                series.setScores(1, 0, 0);
            } else {
                series.setScores(0, 1, 0);
            }
            series.setHasBeenPlayed(true);
        }

        // Check if stats are as expected
        StatsTest.assertStats(teams.get(0), se, 2, 0, 2, 0);
        StatsTest.assertStats(teams.get(1), se, 1, 1, 1, 1);
        StatsTest.assertStats(teams.get(2), se, 0, 1, 0, 1);
        StatsTest.assertStats(teams.get(3), se, 0, 1, 0, 1);
    }

    /**
     * Generates a bracket and sets wins according to the best seed
     * @param amountOfTeams the amount of teams
     */
    public static SingleEliminationFormat generateBracketsAndWins(int amountOfTeams) {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(amountOfTeams, 1), true);

        List<Series> allSeries = bracket.getAllSeries();
        for (int matchIndex = allSeries.size() - 1; matchIndex >= 0; matchIndex--) {
            Series series = allSeries.get(matchIndex);
            if (series.getTeamOne().getInitialSeedValue() < series.getTeamTwo().getInitialSeedValue()) {
                series.setScores(1, 0, 0);
            } else {
                series.setScores(0, 1, 0);
            }
            series.setHasBeenPlayed(true);
        }
        return bracket;
    }
}