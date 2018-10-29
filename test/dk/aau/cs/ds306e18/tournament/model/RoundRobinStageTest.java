package dk.aau.cs.ds306e18.tournament.model;

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

    @Test(expected = NegativeArraySizeException.class)
    public void testRoundRobinBracket04(){

        int numberOfTeams = 0;
        int teamSize = 1;

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(generateTeams(numberOfTeams,teamSize));

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
}