package dk.aau.cs.ds306e18.tournament.model.stats;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import dk.aau.cs.ds306e18.tournament.model.Stage;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.format.SingleEliminationFormat;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class StatsTest {

    // Test class contains tests for both Stats, StatsTracker, StatsChangeListener,
    // and StatsManager as those classes are very tightly connected

    @Test
    public void stats01() {
        Team teamA = new Team("A", null, 1, "a");
        Team teamB = new Team("B", null, 2, "b");

        Stats statsA = teamA.getStatsManager().getGlobalStats(); // Same Stats instance all way through

        assertEquals(0, statsA.getWins());
        assertEquals(0, statsA.getLoses());
        assertEquals(0, statsA.getGoals());
        assertEquals(0, statsA.getGoalsConceded());

        Match match = new Match(teamA, teamB);
        teamA.getStatsManager().trackMatch(null, match);
        match.setScores(3, 2, true);

        // Stats are added now that a match is tracked
        assertEquals(1, statsA.getWins());
        assertEquals(0, statsA.getLoses());
        assertEquals(3, statsA.getGoals());
        assertEquals(2, statsA.getGoalsConceded());
    }

    @Test
    public void stats02() {
        Team teamA = new Team("A", null, 0, "a");
        Team teamB = new Team("B", null, 0, "b");
        Match match = new Match(teamA, teamB);
        teamA.getStatsManager().trackMatch(null, match);
        teamB.getStatsManager().trackMatch(null, match);

        match.setScores(1, 3, false); // Match not over

        assertStats(teamA, null, 0, 0, 1, 3);
        assertStats(teamB, null, 0, 0, 3, 1);
    }

    @Test
    public void stats03() {
        Team teamA = new Team("A", null, 0, "a");
        Team teamB = new Team("B", null, 0, "b");
        Match match = new Match(teamA, teamB);
        teamA.getStatsManager().trackMatch(null, match);
        teamB.getStatsManager().trackMatch(null, match);

        match.setScores(5, 2, true); // Match is over

        assertStats(teamA, null, 1, 0, 5, 2);
        assertStats(teamB, null, 0, 1, 2, 5);
    }

    @Test
    public void stats04() {
        Team teamA = new Team("A", null, 0, "a");
        Team teamB = new Team("B", null, 0, "b");
        Team teamC = new Team("C", null, 0, "c");

        Match matchOne = new Match(teamA, teamB);
        Match matchTwo = new Match().setTeamOne(teamC).setTeamTwoToWinnerOf(matchOne);
        matchOne.setScores(2, 1, true);
        matchTwo.setScores(4, 3, true);

        for (Team team : new Team[]{teamA, teamB, teamC}) {
            team.getStatsManager().trackMatch(null, matchOne);
            team.getStatsManager().trackMatch(null, matchTwo);
        }

        assertStats(teamA, null, 1, 1, 5, 5);
        assertStats(teamB, null, 0, 1, 1, 2);
        assertStats(teamC, null, 1, 0, 4, 3);
    }

    public static void assertStats(Team team, Format format, int wins, int loses, int goals, int goalsConceded) {
        Stats stats = format == null ?
                team.getStatsManager().getGlobalStats() :
                team.getStatsManager().getStats(format);
        assertEquals(wins, stats.getWins());
        assertEquals(loses, stats.getLoses());
        assertEquals(goals, stats.getGoals());
        assertEquals(goalsConceded, stats.getGoalsConceded());
    }
}