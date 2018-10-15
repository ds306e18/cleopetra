package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SwissBracketTest {



    //Even number of teams
    @Test
    public void calculateMaxRounds01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(numberOfTeams - 1, bracket.getMAX_ROUNDS());
    }

    //Odd number of teams
    @Test
    public void calculateMaxRounds02(){

        int numberOfTeams = 5;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(numberOfTeams, bracket.getMAX_ROUNDS());
    }

    //No teams
    @Test
    public void calculateMaxRounds03(){

        int numberOfTeams = 0;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(numberOfTeams, bracket.getMAX_ROUNDS());
    }

    //more than 0 matches
    @Test
    public void getAllMatches01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        ArrayList<Match> allMatches = bracket.getAllMatches();

        assertEquals(numberOfTeams/2, allMatches.size());
    }

    //0 matches
    @Test
    public void getAllMatches02(){

        int numberOfTeams = 0;
        int teamSize = 0;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        ArrayList<Match> allMatches = bracket.getAllMatches();

        assertEquals(0, allMatches.size());
    }

    @Test
    public void getUnplayableMatches01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        ArrayList<Match> unplayedMatches = bracket.getUnplayableMatches();

        assertEquals(0, unplayedMatches.size());
    }

    //0 matches
    @Test
    public void getUnplayableMatches02(){

        int numberOfTeams = 0;
        int teamSize = 0;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        ArrayList<Match> unplayedMatches = bracket.getUnplayableMatches();

        assertEquals(0, unplayedMatches.size());
    }

    @Test
    public void getUnplayableMatches03(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        //All has to be played
        ArrayList<Match> matches = bracket.getUnplayableMatches();

        for(Match match : matches)
            match.setHasBeenPlayed(true);

        assertEquals(0 , bracket.getUnplayableMatches().size());
    }

    @Test
    public void getUpcommingMatches01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(numberOfTeams/2 , bracket.getUpcommingMatches().size());
    }

    @Test
    public void getUpcommingMatches02(){

        int numberOfTeams = 16;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(numberOfTeams/2 , bracket.getUpcommingMatches().size());
    }

    @Test
    public void getUpcommingMatches03(){

        int numberOfTeams = 1;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(0 , bracket.getUpcommingMatches().size());
    }

    @Test
    public void getCompletedMatches01(){
        int numberOfTeams = 4;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertEquals(0 , bracket.getCompletedMatches().size());
    }

    @Test
    public void getCompletedMatches02(){
        int numberOfTeams = 4;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

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

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        //All has to be played
        ArrayList<Match> matches = bracket.getUpcommingMatches();

        for(Match match : matches)
            match.setHasBeenPlayed(true);

        assertTrue(bracket.createNewRound());
    }

    //Create round is illegal: max rounds has been created.
    @Test
    public void createNewRound02(){

        int numberOfTeams = 2;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        //All has to be played
        ArrayList<Match> matches = bracket.getUnplayableMatches();
        for(Match match : matches)
            match.setHasBeenPlayed(true);

        assertFalse(bracket.createNewRound());
    }

    //Create new round is illegal: no matches has been played
    @Test
    public void createNewRound03(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertFalse(bracket.createNewRound());
    }

    //No team can play each other more than once //TODO Create this one where it fails
    @Test
    public void createNewRound04(){

        int numberOfTeams = 16;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        do{
            ArrayList<Match> matches = bracket.getUpcommingMatches();
            for(Match match : matches)
                match.setHasBeenPlayed(true);

            bracket.createNewRound();
        }while(!bracket.hasMaxNumberOfRounds());

        //Check all teams has only played each other once
        ArrayList<Match> allMatches = bracket.getAllMatches();

        for(int i = 0; i < allMatches.size(); i++){
            for(int j = i+1; j < allMatches.size(); j++){

                Team blue = allMatches.get(i).getBlueTeam();
                Team oragne = allMatches.get(j).getOrangeTeam();
                if(blue == oragne)
                    assert false;

            }

        }

        assert true;
    }

    @Test
    public void hasMaxNumberOfRounds01(){

        int numberOfTeams = 2;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertTrue(bracket.hasMaxNumberOfRounds());
    }

    @Test
    public void hasMaxNumberOfRounds02(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissBracket bracket = new SwissBracket(TestUtilities.generateTeams(numberOfTeams, teamSize));

        assertFalse(bracket.hasMaxNumberOfRounds());
    }


    //TODO Below should be reworked

    @Test
    public void checkAndGenerateMatches01(){

        ArrayList<Team> premadeTeams = TestUtilities.generateTeams(3,2);
        SwissBracket bracket = new SwissBracket(premadeTeams);

        SwissBracket.RecursionHelper recursionHelper = new SwissBracket.RecursionHelper();

        recursionHelper.teams = new ArrayList<>(new ArrayList<>(premadeTeams));

        recursionHelper = bracket.checkAndGenerateMatches(recursionHelper);

        System.out.println("Teams");
        int i = 0;
        for(Team team : premadeTeams){
            //System.out.println("Bot1: " + team.getBots().get(0).getName() + " Bot2:" + team.getBots().get(1).getName());
            System.out.println("Team " + i + ": " + team.getTeamName());
        }

        /*
        System.out.println("Matches:");
        for(Team team : recursionHelper.teams){
            System.out.println("Bot1: " + team.getBots().get(0).getName() + " Bot2:" + team.getBots().get(1).getName() );
        }*/

        System.out.println("Created matches:");
        i = 0;
        for(Match match : recursionHelper.createdMatches){
            System.out.println("Match " + i + ": teamBlue: " + match.getBlueTeam().getTeamName() +
                    " teamOrange: " + match.getOrangeTeam().getTeamName());
        }
    }

    @Test
    public void checkAndGenerateMatches02(){

        ArrayList<Team> premadeTeams = TestUtilities.generateTeams(4,2);
        SwissBracket bracket = new SwissBracket(premadeTeams);

        SwissBracket.RecursionHelper recursionHelper = new SwissBracket.RecursionHelper();

        recursionHelper.teams = new ArrayList<>(new ArrayList<>(premadeTeams));

        recursionHelper = bracket.checkAndGenerateMatches(recursionHelper);

        System.out.println("Teams");
        int i = 0;
        for(Team team : premadeTeams){
            //System.out.println("Bot1: " + team.getBots().get(0).getName() + " Bot2:" + team.getBots().get(1).getName());
            System.out.println("Team " + i + ": " + team.getTeamName());
        }

        /*
        System.out.println("Matches:");
        for(Team team : recursionHelper.teams){
            System.out.println("Bot1: " + team.getBots().get(0).getName() + " Bot2:" + team.getBots().get(1).getName() );
        }*/

        System.out.println("Created matches:");
        i = 0;
        for(Match match : recursionHelper.createdMatches){
            System.out.println("Match " + i + ": teamBlue: " + match.getBlueTeam().getTeamName() +
                    " teamOrange: " + match.getOrangeTeam().getTeamName());
        }
    }

}