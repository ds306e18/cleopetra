package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class DoubleEliminationFormatTest {

    @Test
    public void amountOfMatches01() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.generateTeams(8, 1), true);
        assertEquals(15, de.getAllMatches().size());
    }

    @Test
    public void amountOfMatches02() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.generateTeams(30, 1), true);
        assertEquals(59, de.getAllMatches().size());
    }

    @Test
    public void amountOfMatches03() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.generateTeams(12, 1), true);
        assertEquals(23, de.getAllMatches().size());
    }

    @Test
    public void amountOfMatches04() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.generateTeams(19, 1), true);
        assertEquals(37, de.getAllMatches().size());
    }

    @Test
    public void matchDependencies01() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.generateTeams(8, 1), true);
        Match extra = de.getExtraMatch();
        List<Match> matches = de.getAllMatches();
        for (Match match : matches) {
            if (match != extra) {
                assertTrue(extra.dependsOn(match));
            }
        }
    }

    @Test
    public void matchDependencies02() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.generateTeams(17, 1), true);
        Match extra = de.getExtraMatch();
        List<Match> matches = de.getAllMatches();
        for (Match match : matches) {
            if (match != extra) {
                assertTrue(extra.dependsOn(match));
            }
        }
    }

    @Test
    public void seedPlacingTest01() {
        ArrayList<Team> teams = TestUtilities.generateSeededTeams(8, 1);
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(teams, true);
        Match[] upperBracket = de.getUpperBracket();
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
        ArrayList<Team> teams = TestUtilities.generateSeededTeams(5, 1);
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(teams, true);
        Match[] upperBracket = de.getUpperBracket();
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
        de.start(TestUtilities.generateTeams(2, 1), true);
        assertEquals(3, de.getAllMatches().size());
        assertEquals(0, de.getLowerBracket().length);
    }

    @Test
    public void isExtraMatchNeeded01() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.generateTeams(4, 1), true);

        assertFalse(de.isExtraMatchNeeded());

        // Play all matches except the last (the extra match)
        List<Match> matches = de.getAllMatches();
        for (int i = matches.size() - 1; i > 0; i--) {
            matches.get(i).setScores(1, 0, true);
        }

        assertFalse(de.isExtraMatchNeeded());
    }

    @Test
    public void isExtraMatchNeeded02() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.generateTeams(4, 1), true);

        // Play all matches except the last (the extra match)
        List<Match> matches = de.getAllMatches();
        for (int i = matches.size() - 1; i > 0; i--) {
            matches.get(i).setScores(1, 0, true);
        }

        assertFalse(de.isExtraMatchNeeded());

        // Change result of final match so lower bracket winner wins
        matches.get(1).setScores(0, 1, true);

        assertTrue(de.isExtraMatchNeeded());
    }

    @Test
    public void upcomingAndPendingMatches01() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.generateTeams(4, 1), true);
        assertEquals(2, de.getUpcomingMatches().size());
        assertEquals(4, de.getPendingMatches().size());
    }

    @Test
    public void upcomingAndPendingMatches02() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.generateTeams(7, 1), true);
        assertEquals(3, de.getUpcomingMatches().size());
        assertEquals(9, de.getPendingMatches().size());
    }

    @Test
    public void upcomingAndPendingMatches03() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.generateTeams(13, 1), true);
        assertEquals(5, de.getUpcomingMatches().size());
        assertEquals(19, de.getPendingMatches().size());
    }

    @Test
    public void losesMap01() {
        ArrayList<Team> teams = TestUtilities.generateSeededTeams(14, 1);
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(teams, true);

        // Play all matches except the last (the extra match)
        List<Match> matches = de.getAllMatches();
        for (int i = matches.size() - 1; i > 0; i--) {
            matches.get(i).setScores(1, 0, true);
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
        ArrayList<Team> teams = TestUtilities.generateSeededTeams(4, 1);
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(teams, true);

        // Play all matches except the last (the extra match)
        List<Match> matches = de.getAllMatches();
        for (int i = matches.size() - 1; i > 0; i--) {
            Match match = matches.get(i);
            if (match.getTeamOne().getInitialSeedValue() < match.getTeamTwo().getInitialSeedValue()) {
                match.setScores(1, 0, true);
            } else {
                match.setScores(0, 1, true);
            }
        }

        List<Team> topTeams = de.getTopTeams(4, TieBreaker.GOALS_SCORED);
        for (int i = 0; i < topTeams.size(); i++) {
            assertSame(teams.get(i), topTeams.get(i));
        }
    }

    @Test
    public void topTeams02() {
        ArrayList<Team> teams = TestUtilities.generateSeededTeams(32, 1);
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(teams, true);

        // Play all matches except the last (the extra match)
        List<Match> matches = de.getAllMatches();
        for (int i = matches.size() - 1; i > 0; i--) {
            Match match = matches.get(i);
            if (match.getTeamOne().getInitialSeedValue() < match.getTeamTwo().getInitialSeedValue()) {
                match.setScores(1, 0, true);
            } else {
                match.setScores(0, 1, true);
            }
        }

        List<Team> topTeams = de.getTopTeams(8, TieBreaker.GOALS_SCORED);
        for (int i = 0; i < topTeams.size(); i++) {
            assertSame(teams.get(i), topTeams.get(i));
        }
    }
}