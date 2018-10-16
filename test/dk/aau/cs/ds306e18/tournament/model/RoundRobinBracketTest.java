package dk.aau.cs.ds306e18.tournament.model;

import org.junit.Test;

import static dk.aau.cs.ds306e18.tournament.TestUtilities.*;
import static org.junit.Assert.*;


public class RoundRobinBracketTest {


    @Test
    public void testRoundRobinBracket(){

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinBracket bracket = new RoundRobinBracket(generateTeams(numberOfTeams, teamSize));
    }


    /*@Test
    public void testrunCheck(int cutoff){

        int numberOfTeams = 4;
        int teamSize = 1;

        RoundRobinBracket bracket = new RoundRobinBracket(generateTeams(numberOfTeams, teamSize));

        assertEquals(RoundRobinBracket.runCheck(5), 12);
    }*/


}