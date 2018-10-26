package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SwissStageTest {

    //Even number of teams
    @Test
    public void calculateMaxRounds01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(numberOfTeams - 1, bracket.getMaxRounds());
    }

    //Odd number of teams
    @Test
    public void calculateMaxRounds02(){

        int numberOfTeams = 5;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(numberOfTeams, bracket.getMaxRounds());
    }

    //No teams
    @Test
    public void calculateMaxRounds03(){

        int numberOfTeams = 0;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(numberOfTeams, bracket.getMaxRounds());
    }

    //more than 0 matches // one round
    @Test
    public void getAllMatches01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        ArrayList<Match> allMatches = bracket.getAllMatches();

        assertEquals(numberOfTeams/2, allMatches.size());
    }

    //0 matches // one round
    @Test
    public void getAllMatches02(){

        int numberOfTeams = 0;
        int teamSize = 0;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        ArrayList<Match> allMatches = bracket.getAllMatches();

        assertEquals(0, allMatches.size());
    }

    //more than 0 matches // more round
    @Test
    public void getAllMatches03(){

        int numberOfTeams = 12;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        //The first round.
        assertEquals(numberOfTeams/2, bracket.getAllMatches().size());

        //Fill in scores
        ArrayList<Match> matches = bracket.getRawMatches();
        for(Match match : matches){
            match.setScores(5, 2, true);
        }

        bracket.createNewRound();

        assertEquals((numberOfTeams/2) * 2, bracket.getAllMatches().size());
    }

    @Test
    public void getPendingMatches01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        ArrayList<Match> unplayedMatches = bracket.getPendingMatches();

        assertEquals(0, unplayedMatches.size());
    }

    //0 matches
    @Test
    public void getPendingMatches02(){

        int numberOfTeams = 0;
        int teamSize = 0;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        ArrayList<Match> unplayedMatches = bracket.getPendingMatches();

        assertEquals(0, unplayedMatches.size());
    }

    @Test
    public void getPendingMatches03(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        //All has to be played
        ArrayList<Match> matches = bracket.getPendingMatches();

        for(Match match : matches)
            match.setHasBeenPlayed(true);

        assertEquals(0 , bracket.getPendingMatches().size());
    }

    @Test
    public void getUpcomingMatches01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(numberOfTeams/2 , bracket.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches02(){

        int numberOfTeams = 16;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(numberOfTeams/2 , bracket.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches03(){

        int numberOfTeams = 1;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(0 , bracket.getUpcomingMatches().size());
    }

    @Test
    public void getCompletedMatches01(){
        int numberOfTeams = 4;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(0 , bracket.getCompletedMatches().size());
    }

    @Test
    public void getCompletedMatches02(){
        int numberOfTeams = 4;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        ArrayList<Match> allMatches = bracket.getAllMatches();

        for(Match match : allMatches)
            match.setHasBeenPlayed(true);

        assertEquals(bracket.getAllMatches().size() , bracket.getCompletedMatches().size());
    }

    //Create round is legal
    @Test
    public void createNewRound01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        //All has to be played
        ArrayList<Match> matches = bracket.getUpcomingMatches();

        for(Match match : matches)
            match.setHasBeenPlayed(true);

        assertTrue(bracket.createNewRound());
    }

    //Create round is illegal: max rounds has been created.
    @Test
    public void createNewRound02(){

        int numberOfTeams = 2;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        //All has to be played
        ArrayList<Match> matches = bracket.getPendingMatches();
        for(Match match : matches)
            match.setHasBeenPlayed(true);

        assertFalse(bracket.createNewRound());
    }

    //Create new round is illegal: no matches has been played
    @Test
    public void createNewRound03(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertFalse(bracket.createNewRound());
    }

    //No team can play each other more than once
    @Test
    public void createNewRound04(){

        //Create a list of unique teams.
        ArrayList<Team> teams = new ArrayList<>();
        for(int i = 0; i < 7; i++)
            teams.add(new Team(String.valueOf(i), TestUtilities.generateBots(2), 0, "Hello"));

        //Create the bracket with the teams
        SwissStage bracket = new SwissStage();
        bracket.start(teams);

        //Generate all rounds and fill result
        do{
            ArrayList<Match> matches = bracket.getUpcomingMatches();
            for(Match match : matches)
                match.setHasBeenPlayed(true);

            bracket.createNewRound();
        }while(!bracket.hasMaxNumberOfRounds());

        ArrayList<Match> allMatches = bracket.getAllMatches();

        //Check if no teams has played each other more than once
        for(int i = 0; i < allMatches.size(); i++){

            for(int j = i + 1; j < allMatches.size(); j++){

                Match match1 = allMatches.get(i);
                Match match2 = allMatches.get(j);

                //System.out.println("Match Comp 1: Match1B: " + match1.getBlueTeam().getTeamName() + " Match1O " + match1.getOrangeTeam().getTeamName()
                // + " Match2B " + match2.getBlueTeam().getTeamName() + " Match2O " + match2.getOrangeTeam().getTeamName());

                assertFalse(match1.getBlueTeam().getTeamName().equals(match2.getBlueTeam().getTeamName()) &&
                        match1.getOrangeTeam().getTeamName().equals(match2.getOrangeTeam().getTeamName()));
                assertFalse(match1.getBlueTeam().getTeamName().equals(match2.getOrangeTeam().getTeamName()) &&
                        match1.getOrangeTeam().getTeamName().equals(match2.getBlueTeam().getTeamName()));
            }
        }
    }

    //Create round is illegal: only one team.
    @Test
    public void createNewRound05(){

        int numberOfTeams = 1;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        //All has to be played
        ArrayList<Match> matches = bracket.getPendingMatches();
        for(Match match : matches)
            match.setHasBeenPlayed(true);

        assertFalse(bracket.createNewRound());
    }

    @Test
    public void hasMaxNumberOfRounds01(){

        int numberOfTeams = 2;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertTrue(bracket.hasMaxNumberOfRounds());
    }

    @Test
    public void hasMaxNumberOfRounds02(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertFalse(bracket.hasMaxNumberOfRounds());
    }

    @Test
    public void getStatus01(){ //Pending

        SwissStage bracket = new SwissStage();

        assertEquals(StageStatus.PENDING, bracket.getStatus());
    }

    @Test
    public void getStatus02(){ //Running

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(4, 2));
        bracket.createNewRound();

        assertEquals(StageStatus.RUNNING, bracket.getStatus());
    }

    @Test
    public void getStatus03(){ //Concluded // max number of rounds and all played

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(2,2));
        bracket.createNewRound();

        //Set all matches to played
        ArrayList<Match> matches = bracket.getUpcomingMatches();
        for(Match match : matches){
            match.setHasBeenPlayed(true);
        }

        assertEquals(StageStatus.CONCLUDED, bracket.getStatus());
    }

    @Test
    public void getStatus04(){ //Concluded //max number of round but not played

        SwissStage bracket = new SwissStage();
        bracket.start(TestUtilities.generateTeams(2,2));
        bracket.createNewRound();

        assertNotEquals(StageStatus.CONCLUDED, bracket.getStatus());
    }
}