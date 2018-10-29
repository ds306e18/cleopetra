package dk.aau.cs.ds306e18.tournament.model;

import com.google.common.math.BigIntegerMath;
import org.junit.Test;

import static dk.aau.cs.ds306e18.tournament.TestUtilities.*;
import static org.junit.Assert.*;


public class RoundRobinStageTest {


    @Test
    public void testRoundRobinBracket(){

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams, teamSize));
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

        assertEquals(factorial(numberOfTeams-1), bracket.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches02(){ //even 4

        int numberOfTeams = 6;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams, teamSize));

        assertEquals(factorial(numberOfTeams -1), bracket.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches03(){ //odd 5

        int numberOfTeams = 5;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams, teamSize));

        assertEquals(factorial(numberOfTeams -1), bracket.getUpcomingMatches().size());
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

        assertEquals(0, bracket.getCompletedMatches());
    }


    /** @return the given x factored. x! */
    private int factorial(int x){

        int result = 0;

        for(int i = x; i > 0; i--)
            result += i;

        return result;
    }
}