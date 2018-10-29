package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import org.junit.Test;

import static dk.aau.cs.ds306e18.tournament.TestUtilities.*;
import static org.junit.Assert.*;


public class RoundRobinStageTest {

    @Test
    public void testRoundRobinBracket01(){

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams, teamSize));

        assertEquals(bracket.getStatus(),StageStatus.RUNNING);

    }

    @Test
    public void testRoundRobinBracket02(){

        int numberOfTeams = 3;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams,teamSize));

        assertEquals(bracket.getStatus(),StageStatus.RUNNING);
    }

    @Test
    public void testRoundRobinBracket03(){

        int numberOfTeams = 1234;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams,teamSize));

        assertEquals(bracket.getStatus(),StageStatus.RUNNING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRoundRobinBracket05(){

        int numberOfTeams = -12;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams,teamSize));

    }

    @Test
    public void testFindIdOfNextPlayer(){

        int numberOfTeams = 20;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams, teamSize));

        assertEquals((bracket.findIdOfNextPlayer(3)), 13);
        assertEquals((bracket.findIdOfNextPlayer(1)), 11);

        for (int i = 1; i <= numberOfTeams; i++) {
            assertTrue(bracket.findIdOfNextPlayer(i) < numberOfTeams);
            assertTrue(bracket.findIdOfNextPlayer(i) > 0);
            if (i >= (numberOfTeams/2)) {
                assertTrue(bracket.findIdOfNextPlayer(i) < i);
            } else assertTrue(bracket.findIdOfNextPlayer(i) > i);
        }
    }

    @Test
    public void getUpcomingMatches01(){ //even 4

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams, teamSize));

        assertEquals(TestUtilities.factorial(numberOfTeams-1), bracket.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches02(){ //even 4

        int numberOfTeams = 6;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams, teamSize));

        assertEquals(TestUtilities.factorial(numberOfTeams -1), bracket.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches03(){ //odd 5

        int numberOfTeams = 5;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams, teamSize));

        assertEquals(TestUtilities.factorial(numberOfTeams -1), bracket.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches04(){ // 0 teams

        int numberOfTeams = 0;
        int teamSize = 0;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams, teamSize));

        assertEquals(0, bracket.getUpcomingMatches().size());
    }

    @Test
    public void getCompletedMatches01(){ //non has been played

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams, teamSize));

        assertEquals(0, bracket.getCompletedMatches().size());
    }

    @Test
    public void getCompletedMatches02(){ //all has been played

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams, teamSize));

        TestUtilities.setAllMatchesPlayed(bracket);

        assertEquals(factorial(numberOfTeams-1), bracket.getCompletedMatches().size());
    }

    @Test //more than 0 matches
    public void getAllMatches01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(factorial(numberOfTeams-1), bracket.getAllMatches().size());
    }

    @Test //0 matches
    public void getAllMatches02(){

        int numberOfTeams = 0;
        int teamSize = 2;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(0, bracket.getAllMatches().size());
    }

    @Test
    public void getPendingMatches01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(0, bracket.getPendingMatches().size());
    }
}