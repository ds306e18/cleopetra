package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import org.junit.Test;

import java.util.ArrayList;
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
        assertSame(1, upperBracket[len-1].getBlueTeam().getInitialSeedValue());
        assertSame(8, upperBracket[len-1].getOrangeTeam().getInitialSeedValue());
        assertSame(4, upperBracket[len-2].getBlueTeam().getInitialSeedValue());
        assertSame(5, upperBracket[len-2].getOrangeTeam().getInitialSeedValue());
        assertSame(2, upperBracket[len-3].getBlueTeam().getInitialSeedValue());
        assertSame(7, upperBracket[len-3].getOrangeTeam().getInitialSeedValue());
        assertSame(3, upperBracket[len-4].getBlueTeam().getInitialSeedValue());
        assertSame(6, upperBracket[len-4].getOrangeTeam().getInitialSeedValue());
    }

    @Test
    public void seedPlacingTest02() {
        ArrayList<Team> teams = TestUtilities.generateSeededTeams(5, 1);
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(teams, true);
        Match[] upperBracket = de.getUpperBracket();
        int len = upperBracket.length;
        assertSame(1, upperBracket[len-5].getBlueTeam().getInitialSeedValue());
        assertSame(2, upperBracket[len-6].getBlueTeam().getInitialSeedValue());
        assertSame(3, upperBracket[len-6].getOrangeTeam().getInitialSeedValue());
        assertSame(4, upperBracket[len-2].getBlueTeam().getInitialSeedValue());
        assertSame(5, upperBracket[len-2].getOrangeTeam().getInitialSeedValue());
    }

    @Test
    public void onlyTwoTeams() {
        DoubleEliminationFormat de = new DoubleEliminationFormat();
        de.start(TestUtilities.generateTeams(2, 1), true);
        assertEquals(3, de.getAllMatches().size());
        assertEquals(0, de.getLowerBracket().length);
    }
}