package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Test
    public void getTopTeams01(){ //No teams

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(new ArrayList<Team>());

        assertEquals(0, bracket.getTopTeams(10, new TieBreakerBySeed()).size());
    }

    @Test
    public void getTopTeams02(){

        RoundRobinStage bracket = new RoundRobinStage();
        ArrayList<Team> inputTeams = TestUtilities.generateSeededTeams(4,2);
        bracket.start(inputTeams);

        setAllMatchesPlayed(bracket);
        //All teams now have the same amount of points.

        ArrayList<Team> top3Teams = new ArrayList<>(bracket.getTopTeams(3, new TieBreakerBySeed()));

        //Get the team not in the top3Teams list
        ArrayList<Team> teamInputCopy = new ArrayList<>(inputTeams);
        Team notInTopTeam;
        for(Team top3Team : top3Teams){
            teamInputCopy.remove(top3Team);
        }
        notInTopTeam = teamInputCopy.get(0);

        //
        HashMap<Team, Integer> teamPoints = bracket.getTeamPointsMap();
        Team top3Team = top3Teams.get(2);
        Integer top3TeamPoints = teamPoints.get(top3Team);
        Integer notInTopTeamPoints = teamPoints.get(notInTopTeam);
        assertTrue(top3TeamPoints >= notInTopTeamPoints &&
                top3Team.getInitialSeedValue() < notInTopTeam.getInitialSeedValue());
    }

    @Test
    public void getStatus01(){ //Pending

        RoundRobinStage bracket = new RoundRobinStage();

        assertEquals(StageStatus.PENDING, bracket.getStatus());
    }

    @Test
    public void getStatus02(){ //Running

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(TestUtilities.generateTeams(4, 2));

        assertEquals(StageStatus.RUNNING, bracket.getStatus());
    }

    @Test
    public void getStatus03(){ //Concluded // max number of rounds and all played

        RoundRobinStage bracket = new RoundRobinStage();
        bracket.start(TestUtilities.generateTeams(2,2));

        //Set all matches to played
        setAllMatchesPlayed(bracket);

        assertEquals(StageStatus.CONCLUDED, bracket.getStatus());
    }

    /** sets all upcoming matches in the given format to have been played.*/
    private void setAllMatchesPlayed(GroupFormat format){

        //Set all matches to played
        List<Match> matches = format.getUpcomingMatches();
        for(Match match : matches){
            match.setHasBeenPlayed(true);
        }
    }
}