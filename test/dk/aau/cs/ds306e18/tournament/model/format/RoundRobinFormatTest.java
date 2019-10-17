package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.stats.StatsTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static dk.aau.cs.ds306e18.tournament.TestUtilities.*;
import static org.junit.Assert.*;


public class RoundRobinFormatTest {

    static int numberOfMatchesInRoundRobin(int x) {
        return x * (x - 1) / 2;
    }

    @Test
    public void start01() {

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(rr.getStatus(), StageStatus.RUNNING);
    }

    @Test
    public void start02() {

        int numberOfTeams = 32;
        int teamSize = 1;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(rr.getStatus(), StageStatus.RUNNING);
    }

    @Test
    public void findIdOfNextPlayer01() {

        int numberOfTeams = 20;
        int teamSize = 1;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals((rr.findIdOfNextTeam(3,numberOfTeams)), 13);
        assertEquals((rr.findIdOfNextTeam(1,numberOfTeams)), 11);

        for (int i = 1; i <= numberOfTeams; i++) {
            assertTrue(rr.findIdOfNextTeam(i,numberOfTeams) < numberOfTeams);
            assertTrue(rr.findIdOfNextTeam(i,numberOfTeams) > 0);
            if (i >= (numberOfTeams / 2)) {
                assertTrue(rr.findIdOfNextTeam(i, numberOfTeams) < i);
            } else assertTrue(rr.findIdOfNextTeam(i, numberOfTeams) > i);
        }
    }

    @Test
    public void getUpcomingMatches01() { //even 4

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(numberOfMatchesInRoundRobin(numberOfTeams), rr.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches02() { //even 6

        int numberOfTeams = 6;
        int teamSize = 1;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(numberOfMatchesInRoundRobin(numberOfTeams), rr.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches03() { //odd 5

        int numberOfTeams = 5;
        int teamSize = 1;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(numberOfMatchesInRoundRobin(numberOfTeams), rr.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches04() { // 0 teams

        int numberOfTeams = 0;
        int teamSize = 0;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(0, rr.getUpcomingMatches().size());
    }

    @Test
    public void getCompletedMatches01() { //non has been played

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(0, rr.getCompletedMatches().size());
    }

    @Test
    public void getCompletedMatches02() { //all has been played

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        setAllMatchesToPlayed(rr.getUpcomingMatches());

        assertEquals(numberOfMatchesInRoundRobin(numberOfTeams), rr.getCompletedMatches().size());
    }

    @Test //more than 0 matches
    public void getAllMatches01() {

        int numberOfTeams = 4;
        int teamSize = 2;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(numberOfMatchesInRoundRobin(numberOfTeams), rr.getAllMatches().size());
    }

    @Test //0 matches
    public void getAllMatches02() {

        int numberOfTeams = 0;
        int teamSize = 2;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(0, rr.getAllMatches().size());
    }

    @Test
    public void getPendingMatches01() {

        int numberOfTeams = 4;
        int teamSize = 2;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(0, rr.getPendingMatches().size());
    }

    @Test
    public void getTopTeams01() { //No teams

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(new ArrayList<>(), true);

        assertEquals(0, rr.getTopTeams(0, TieBreaker.SEED).size());
    }

    @Test
    public void getTopTeams02() {

        RoundRobinFormat rr = new RoundRobinFormat();
        List<Team> teams = getTestTeams(7, 1);
        rr.start(teams, true);

        ArrayList<Team> top4Teams = new ArrayList<>(rr.getTopTeams(4, TieBreaker.SEED));

        // Should match seeds when all have the same amount of points
        assertEquals(teams.get(0), top4Teams.get(0));
        assertEquals(teams.get(1), top4Teams.get(1));
        assertEquals(teams.get(2), top4Teams.get(2));
        assertEquals(teams.get(3), top4Teams.get(3));
    }

    @Test
    public void getTopTeams03() {

        RoundRobinFormat rr = new RoundRobinFormat();
        List<Team> teams = getTestTeams(8, 1);
        rr.setNumberOfGroups(3); // group sizes: 2, 3, 3
        rr.start(teams, true);

        // Assign goals equal to their seed, i.e. expected winner loses
        for (Match m : rr.getUpcomingMatches()) {
            int teamOneSeed = m.getTeamOne().getInitialSeedValue();
            int teamTwoSeed = m.getTeamTwo().getInitialSeedValue();
            m.setScores(teamOneSeed, teamTwoSeed, true);
        }

        ArrayList<Team> top7Teams = new ArrayList<>(rr.getTopTeams(7, TieBreaker.SEED));

        // We expect:
        // 2 from group 0
        // 3 from group 1
        // 2 grom group 2
        // And team 3 won't proceed as it is the loser of the last group
        assertEquals(teams.get(3), top7Teams.get(0));
        assertEquals(teams.get(6), top7Teams.get(1));
        assertEquals(teams.get(7), top7Teams.get(2));
        assertEquals(teams.get(0), top7Teams.get(3));
        assertEquals(teams.get(4), top7Teams.get(4));
        assertEquals(teams.get(5), top7Teams.get(5));
        assertEquals(teams.get(1), top7Teams.get(6));
    }

    @Test
    public void getStatus01() { //Pending

        RoundRobinFormat rr = new RoundRobinFormat();

        assertEquals(StageStatus.PENDING, rr.getStatus());
    }

    @Test
    public void getStatus02() { //Running

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.start(getTestTeams(4, 2), true);

        assertEquals(StageStatus.RUNNING, rr.getStatus());
    }

    @Test
    public void getStatus03() { //Concluded // max number of rounds and all played

        RoundRobinFormat rr = new RoundRobinFormat();

        rr.start(getTestTeams(6, 2), true);

        setAllMatchesToPlayed(rr.getUpcomingMatches());

        assertEquals(StageStatus.CONCLUDED, rr.getStatus());
    }

    @Test
    public void groupSizes01() { //even group size, even number of teams in each group

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.setNumberOfGroups(2);
        rr.start(getTestTeams(12,2), true);


        assertEquals(2,rr.getGroups().size());
        for (RoundRobinGroup group: rr.getGroups()) {
            assertEquals(6,group.getTeams().size());
            assertEquals(15,group.getMatches().size());
        }

        for (Match match : rr.getAllMatches()) {
            assertTrue(match.getTeamOne() != RoundRobinFormat.getDummyTeam() &&
                    match.getTeamTwo() != RoundRobinFormat.getDummyTeam());
        }

        assertEquals(30,rr.getAllMatches().size());
        assertNotSame(rr.getGroups().get(0), rr.getGroups().get(1));

    }

    @Test
    public void groupSizes02() { //even group size, uneven number of teams in each group

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.setNumberOfGroups(2);
        rr.start(getTestTeams(10,2), true);

        assertEquals(2,rr.getGroups().size());
        for (RoundRobinGroup group: rr.getGroups()) {
            assertEquals(5, group.getTeams().size());
            assertEquals(10,group.getMatches().size());
        }

        for (Match match : rr.getAllMatches()) {
            assertTrue(match.getTeamOne() != RoundRobinFormat.getDummyTeam() &&
                    match.getTeamTwo() != RoundRobinFormat.getDummyTeam());
        }

        assertEquals(20,rr.getAllMatches().size());
        assertNotSame(rr.getGroups().get(0), rr.getGroups().get(1));
    }

    @Test
    public void groupSizes03() { //uneven group size, even number of teams in each group

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.setNumberOfGroups(3);
        rr.start(getTestTeams(18,2), true);

        assertEquals(3,rr.getGroups().size());
        for (RoundRobinGroup group: rr.getGroups()) {
            assertEquals(6,group.getTeams().size());
            assertEquals(15,group.getMatches().size());
        }
        for (Match match : rr.getAllMatches()) {
            assertTrue(match.getTeamOne() != RoundRobinFormat.getDummyTeam() &&
                    match.getTeamTwo() != RoundRobinFormat.getDummyTeam());
        }

        assertEquals(45,rr.getAllMatches().size());
        assertNotSame(rr.getGroups().get(0), rr.getGroups().get(1));
        assertNotSame(rr.getGroups().get(0), rr.getGroups().get(2));
    }

    @Test
    public void groupSizes04() { //uneven group size, even number of teams in each group

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.setNumberOfGroups(3);
        rr.start(getTestTeams(10,2), true);

        assertEquals(3,rr.getGroups().size());
        for (RoundRobinGroup group: rr.getGroups()) {

            if (group.getTeams().size() == 3) {
                assertEquals(3,group.getMatches().size());
            } else if (group.getTeams().size() == 4) {
                assertEquals(6,group.getMatches().size());
            }
        }

        for (Match match : rr.getAllMatches()) {
            assertTrue(match.getTeamOne() != RoundRobinFormat.getDummyTeam() &&
                    match.getTeamTwo() != RoundRobinFormat.getDummyTeam());
        }

        assertEquals(12,rr.getAllMatches().size());
        assertNotSame(rr.getGroups().get(0), rr.getGroups().get(1));
        assertNotSame(rr.getGroups().get(0), rr.getGroups().get(2));
    }

    @Test //more than 0 matches
    public void tooManyGroups() {

        int numberOfTeams = 10;
        int teamSize = 2;


        RoundRobinFormat rr = new RoundRobinFormat();
        rr.setNumberOfGroups(20);
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(5, rr.getGroups().size());
    }

    @Test //more than 0 matches
    public void tooFewGroups() {

        int numberOfTeams = 10;
        int teamSize = 2;


        RoundRobinFormat rr = new RoundRobinFormat();
        rr.setNumberOfGroups(0);
        rr.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(1, rr.getGroups().size());
    }

    @Test
    public void roundSizes01() {

        int numberOfTeams = 12;
        int numberOfGroups = 3;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.setNumberOfGroups(numberOfGroups);
        rr.start(getTestTeams(numberOfTeams, 2), true);

        ArrayList<RoundRobinGroup> groups = rr.getGroups();

        for (RoundRobinGroup group : groups) {
            assertEquals(3, group.getRounds().size());

            for (ArrayList<Match> round : group.getRounds()) {
                assertEquals(2, round.size());
            }
        }
    }

    @Test
    public void roundSizes02() {

        int numberOfTeams = 12;
        int numberOfGroups = 2;

        RoundRobinFormat rr = new RoundRobinFormat();
        rr.setNumberOfGroups(numberOfGroups);
        rr.start(getTestTeams(numberOfTeams, 2), true);

        ArrayList<RoundRobinGroup> groups = rr.getGroups();

        for (RoundRobinGroup group : groups) {
            assertEquals(5, group.getRounds().size());

            for (ArrayList<Match> round : group.getRounds()) {
                assertEquals(3, round.size());
            }
        }
    }

    @Test
    public void withSeeding01() {
        int numberOfTeams = 6;
        List<Team> teams = getTestTeams(numberOfTeams, 1);

        RoundRobinFormat roundrobin = new RoundRobinFormat();
        roundrobin.setNumberOfGroups(2);
        roundrobin.start(teams, true);

        assertSame(teams.get(0), roundrobin.getGroups().get(0).getTeams().get(0));
        assertSame(teams.get(2), roundrobin.getGroups().get(0).getTeams().get(1));
        assertSame(teams.get(4), roundrobin.getGroups().get(0).getTeams().get(2));
        assertSame(teams.get(1), roundrobin.getGroups().get(1).getTeams().get(0));
        assertSame(teams.get(3), roundrobin.getGroups().get(1).getTeams().get(1));
        assertSame(teams.get(5), roundrobin.getGroups().get(1).getTeams().get(2));
    }

    @Test
    public void withSeeding02() {
        // with seeding and number of teams is not divisible by group count

        int numberOfTeams = 8;
        List<Team> teams = getTestTeams(numberOfTeams, 1);

        RoundRobinFormat roundrobin = new RoundRobinFormat();
        roundrobin.setNumberOfGroups(3);
        roundrobin.start(teams, true);

        // Group 1 is seed 1 and 4
        assertSame(teams.get(0), roundrobin.getGroups().get(0).getTeams().get(0));
        assertSame(teams.get(3), roundrobin.getGroups().get(0).getTeams().get(1));
        // Group 2 is seed 2, 5, and 7
        assertSame(teams.get(1), roundrobin.getGroups().get(1).getTeams().get(0));
        assertSame(teams.get(4), roundrobin.getGroups().get(1).getTeams().get(1));
        assertSame(teams.get(6), roundrobin.getGroups().get(1).getTeams().get(2));
        // Group 3 is seed 3, 6, and 8
        assertSame(teams.get(2), roundrobin.getGroups().get(2).getTeams().get(0));
        assertSame(teams.get(5), roundrobin.getGroups().get(2).getTeams().get(1));
        assertSame(teams.get(7), roundrobin.getGroups().get(2).getTeams().get(2));
    }

    @Test
    public void withoutSeeding01() {
        int numberOfTeams = 6;
        List<Team> teams = getTestTeams(numberOfTeams, 1);

        RoundRobinFormat roundrobin = new RoundRobinFormat();
        roundrobin.setNumberOfGroups(2);
        roundrobin.start(teams, false);

        assertSame(teams.get(0), roundrobin.getGroups().get(0).getTeams().get(0));
        assertSame(teams.get(1), roundrobin.getGroups().get(0).getTeams().get(1));
        assertSame(teams.get(2), roundrobin.getGroups().get(0).getTeams().get(2));
        assertSame(teams.get(3), roundrobin.getGroups().get(1).getTeams().get(0));
        assertSame(teams.get(4), roundrobin.getGroups().get(1).getTeams().get(1));
        assertSame(teams.get(5), roundrobin.getGroups().get(1).getTeams().get(2));
    }

    @Test
    public void withoutSeeding02() {
        // without seeding and number of teams is not divisible by group count

        int numberOfTeams = 8;
        List<Team> teams = getTestTeams(numberOfTeams, 1);

        RoundRobinFormat roundrobin = new RoundRobinFormat();
        roundrobin.setNumberOfGroups(3);
        roundrobin.start(teams, false);

        assertSame(teams.get(0), roundrobin.getGroups().get(0).getTeams().get(0));
        assertSame(teams.get(1), roundrobin.getGroups().get(0).getTeams().get(1));

        assertSame(teams.get(2), roundrobin.getGroups().get(1).getTeams().get(0));
        assertSame(teams.get(3), roundrobin.getGroups().get(1).getTeams().get(1));
        assertSame(teams.get(4), roundrobin.getGroups().get(1).getTeams().get(2));

        assertSame(teams.get(5), roundrobin.getGroups().get(2).getTeams().get(0));
        assertSame(teams.get(6), roundrobin.getGroups().get(2).getTeams().get(1));
        assertSame(teams.get(7), roundrobin.getGroups().get(2).getTeams().get(2));
    }

    @Test
    public void stats01() {
        RoundRobinFormat rr = new RoundRobinFormat();
        List<Team> teams = TestUtilities.getTestTeams(4, 1);
        rr.start(teams, true);

        // Play all matches. The highest seeded team wins 1-0
        for (Match match : rr.getAllMatches()) {
            if (match.getTeamOne().getInitialSeedValue() < match.getTeamTwo().getInitialSeedValue()) {
                match.setScores(1, 0, true);
            } else {
                match.setScores(0, 1, true);
            }
        }

        // Check if stats are as expected
        StatsTest.assertStats(teams.get(0), rr, 3, 0, 3, 0);
        StatsTest.assertStats(teams.get(1), rr, 2, 1, 2, 1);
        StatsTest.assertStats(teams.get(2), rr, 1, 2, 1, 2);
        StatsTest.assertStats(teams.get(3), rr, 0, 3, 0, 3);
    }
}