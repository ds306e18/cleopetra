package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.model.format.RoundRobinFormat;
import dk.aau.cs.ds306e18.tournament.model.format.RoundRobinGroup;
import dk.aau.cs.ds306e18.tournament.model.format.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreakerBySeed;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static dk.aau.cs.ds306e18.tournament.TestUtilities.*;
import static org.junit.Assert.*;


public class RoundRobinFormatTest {

    @Test
    public void testRoundRobinBracket01() {

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(bracket.getStatus(), StageStatus.RUNNING);
    }

    @Test
    public void testRoundRobinBracket02() {

        int numberOfTeams = 3;
        int teamSize = 1;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(bracket.getStatus(), StageStatus.RUNNING);
    }

    @Test
    public void testRoundRobinBracket03() {

        int numberOfTeams = 1234;
        int teamSize = 1;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(bracket.getStatus(), StageStatus.RUNNING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRoundRobinBracket05() {

        int numberOfTeams = -12;
        int teamSize = 1;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);
    }

    @Test
    public void testFindIdOfNextPlayer() {

        int numberOfTeams = 20;
        int teamSize = 1;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals((bracket.findIdOfNextPlayer(3,numberOfTeams)), 13);
        assertEquals((bracket.findIdOfNextPlayer(1,numberOfTeams)), 11);

        for (int i = 1; i <= numberOfTeams; i++) {
            assertTrue(bracket.findIdOfNextPlayer(i,numberOfTeams) < numberOfTeams);
            assertTrue(bracket.findIdOfNextPlayer(i,numberOfTeams) > 0);
            if (i >= (numberOfTeams / 2)) {
                assertTrue(bracket.findIdOfNextPlayer(i, numberOfTeams) < i);
            } else assertTrue(bracket.findIdOfNextPlayer(i, numberOfTeams) > i);
        }
    }

    @Test
    public void getUpcomingMatches01() { //even 4

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(numberOfMatchesInRoundRobin(numberOfTeams), bracket.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches02() { //even 6

        int numberOfTeams = 6;
        int teamSize = 1;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(numberOfMatchesInRoundRobin(numberOfTeams), bracket.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches03() { //odd 5

        int numberOfTeams = 5;
        int teamSize = 1;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(numberOfMatchesInRoundRobin(numberOfTeams), bracket.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches04() { // 0 teams

        int numberOfTeams = 0;
        int teamSize = 0;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(0, bracket.getUpcomingMatches().size());
    }

    @Test
    public void getCompletedMatches01() { //non has been played

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(0, bracket.getCompletedMatches().size());
    }

    @Test
    public void getCompletedMatches02() { //all has been played

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        setAllUpcommingMatchesPlayed(bracket);

        assertEquals(numberOfMatchesInRoundRobin(numberOfTeams), bracket.getCompletedMatches().size());
    }

    @Test //more than 0 matches
    public void getAllMatches01() {

        int numberOfTeams = 4;
        int teamSize = 2;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(numberOfMatchesInRoundRobin(numberOfTeams), bracket.getAllMatches().size());
    }

    @Test //0 matches
    public void getAllMatches02() {

        int numberOfTeams = 0;
        int teamSize = 2;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(0, bracket.getAllMatches().size());
    }

    @Test
    public void getPendingMatches01() {

        int numberOfTeams = 4;
        int teamSize = 2;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(0, bracket.getPendingMatches().size());
    }

    @Test
    public void getTopTeams01() { //No teams

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(new ArrayList<Team>(), seeding);

        assertEquals(0, bracket.getTopTeams(10, new TieBreakerBySeed()).size());
    }

    @Test
    public void getTopTeams02() {

        RoundRobinFormat bracket = new RoundRobinFormat();
        ArrayList<Team> inputTeams = generateSeededTeams(4, 2);
        bracket.start(inputTeams, seeding);

        setAllUpcommingMatchesPlayed(bracket);
        //All teams now have the same amount of points.

        ArrayList<Team> top3Teams = new ArrayList<>(bracket.getTopTeams(3, new TieBreakerBySeed()));

        //Get the team not in the top3Teams list
        ArrayList<Team> teamInputCopy = new ArrayList<>(inputTeams);
        Team notInTopTeam;
        for (Team top3Team : top3Teams) {
            teamInputCopy.remove(top3Team);
        }
        notInTopTeam = teamInputCopy.get(0);

        //
        HashMap<Team, Integer> teamPoints = bracket.getTeamPointsMap();
        Team top3Team = top3Teams.get(2);
        Integer top3TeamPoints = teamPoints.get(top3Team);
        Integer notInTopTeamPoints = teamPoints.get(notInTopTeam);
        assertTrue(top3TeamPoints >= notInTopTeamPoints &&
                top3Team.getInitialSeedValue() <= notInTopTeam.getInitialSeedValue());
    }

    @Test
    public void getStatus01() { //Pending

        RoundRobinFormat bracket = new RoundRobinFormat();

        assertEquals(StageStatus.PENDING, bracket.getStatus());
    }

    @Test
    public void getStatus02() { //Running

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.start(generateTeams(4, 2), seeding);

        assertEquals(StageStatus.RUNNING, bracket.getStatus());
    }

    @Test
    public void getStatus03() { //Concluded // max number of rounds and all played

        RoundRobinFormat bracket = new RoundRobinFormat();

        bracket.start(generateTeams(2, 2), seeding);

        //Set all matches to played
        setAllUpcommingMatchesPlayed(bracket);

        assertEquals(StageStatus.CONCLUDED, bracket.getStatus());
    }

    @Test
    public void groupSizes01() { //even group size, even number of teams in each group

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.setNumberOfGroups(2);
        bracket.start(generateTeams(12,2), seeding);


        assertEquals(2,bracket.getGroups().size());
        for (RoundRobinGroup group: bracket.getGroups()) {
            assertEquals(6, group.getTeamsWithoutDummy().size());
            assertEquals(6,group.getTeams().size());
            assertEquals(15,group.getMatches().size());
        }

        for (Match match : bracket.getAllMatches()) {
            assertTrue(match.getBlueTeam() != RoundRobinFormat.getDummyTeam() &&
                    match.getOrangeTeam() != RoundRobinFormat.getDummyTeam());
        }

        assertEquals(30,bracket.getAllMatches().size());
        assertNotSame(bracket.getGroups().get(0), bracket.getGroups().get(1));

    }

    @Test
    public void groupSizes02() { //even group size, uneven number of teams in each group

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.setNumberOfGroups(2);
        bracket.start(generateTeams(10,2), seeding);

        assertEquals(2,bracket.getGroups().size());
        for (RoundRobinGroup group: bracket.getGroups()) {
            assertEquals(5, group.getTeamsWithoutDummy().size());
            assertEquals(6, group.getTeams().size());
            assertEquals(10,group.getMatches().size());
        }

        for (Match match : bracket.getAllMatches()) {
            assertTrue(match.getBlueTeam() != RoundRobinFormat.getDummyTeam() &&
                    match.getOrangeTeam() != RoundRobinFormat.getDummyTeam());
        }

        assertEquals(20,bracket.getAllMatches().size());
        assertNotSame(bracket.getGroups().get(0), bracket.getGroups().get(1));
    }

    @Test
    public void groupSizes03() { //uneven group size, even number of teams in each group

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.setNumberOfGroups(3);
        bracket.start(generateTeams(18,2), seeding);

        assertEquals(3,bracket.getGroups().size());
        for (RoundRobinGroup group: bracket.getGroups()) {
            assertEquals(6, group.getTeamsWithoutDummy().size());
            assertEquals(6,group.getTeams().size());
            assertEquals(15,group.getMatches().size());
        }
        for (Match match : bracket.getAllMatches()) {
            assertTrue(match.getBlueTeam() != RoundRobinFormat.getDummyTeam() &&
                    match.getOrangeTeam() != RoundRobinFormat.getDummyTeam());
        }

        assertEquals(45,bracket.getAllMatches().size());
        assertNotSame(bracket.getGroups().get(0), bracket.getGroups().get(1));
        assertNotSame(bracket.getGroups().get(0), bracket.getGroups().get(2));
    }

    @Test
    public void groupSizes04() { //uneven group size, even number of teams in each group

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.setNumberOfGroups(3);
        bracket.start(generateTeams(10,2), seeding);

        assertEquals(3,bracket.getGroups().size());
        for (RoundRobinGroup group: bracket.getGroups()) {

            if (group.getTeamsWithoutDummy().size() == 3) {
                assertEquals(4,group.getTeams().size());
                assertEquals(3,group.getMatches().size());
            } else if (group.getTeamsWithoutDummy().size() == 4) {
                assertEquals(4,group.getTeams().size());
                assertEquals(6,group.getMatches().size());
            }
        }

        for (Match match : bracket.getAllMatches()) {
            assertTrue(match.getBlueTeam() != RoundRobinFormat.getDummyTeam() &&
                    match.getOrangeTeam() != RoundRobinFormat.getDummyTeam());
        }

        assertEquals(12,bracket.getAllMatches().size());
        assertNotSame(bracket.getGroups().get(0), bracket.getGroups().get(1));
        assertNotSame(bracket.getGroups().get(0), bracket.getGroups().get(2));
    }

    @Test //more than 0 matches
    public void tooManyGroups() {

        int numberOfTeams = 10;
        int teamSize = 2;


        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.setNumberOfGroups(20);
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(5, bracket.getGroups().size());
    }

    @Test //more than 0 matches
    public void tooFewGroups() {

        int numberOfTeams = 10;
        int teamSize = 2;


        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.setNumberOfGroups(0);
        bracket.start(generateTeams(numberOfTeams, teamSize), seeding);

        assertEquals(1, bracket.getGroups().size());
    }

    @Test
    public void testRounds01(){

        int numberOfTeams = 12;
        int numberOfGroups = 3;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.setNumberOfGroups(numberOfGroups);
        bracket.start(generateTeams(numberOfTeams, 2), seeding);

        ArrayList<RoundRobinGroup> groups = bracket.getGroups();

        for (RoundRobinGroup group : groups) {
            assertEquals(3, group.getRounds().size());

            for (ArrayList<Match> round : group.getRounds()) {
                assertEquals(2, round.size());
            }
        }
    }

    @Test
    public void testRounds02(){

        int numberOfTeams = 12;
        int numberOfGroups = 2;

        RoundRobinFormat bracket = new RoundRobinFormat();
        bracket.setNumberOfGroups(numberOfGroups);
        bracket.start(generateTeams(numberOfTeams, 2), seeding);

        ArrayList<RoundRobinGroup> groups = bracket.getGroups();

        for (RoundRobinGroup group : groups) {
            assertEquals(5, group.getRounds().size());

            for (ArrayList<Match> round : group.getRounds()) {
                assertEquals(3, round.size());
            }
        }
    }

    /**
     * sets all upcoming matches in the given format to have been played. The best seeded team wins.
     */
    private void setAllUpcommingMatchesPlayed(GroupFormat format) {

        //Set all matches to played
        List<Match> matches = format.getUpcomingMatches();
        for (Match match : matches) {
            Team blue = match.getBlueTeam();
            Team orange = match.getOrangeTeam();
            if (blue.getInitialSeedValue() < orange.getInitialSeedValue()) {
                match.setScores(1, 0, true);
            } else {
                match.setScores(0, 1, true);
            }
        }
    }
}