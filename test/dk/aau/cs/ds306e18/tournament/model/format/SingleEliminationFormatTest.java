package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SingleEliminationFormatTest {

    //there should be a correct amount of matches
    @Test
    public void amountOfMatchesTest01(){
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8,1), true);
        assertEquals(7, bracket.getAllMatches().size());
    }

    //there should be a correct amount of matches
    @Test
    public void amountOfMatchesTest02() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(16,1), true);
        assertEquals(15, bracket.getAllMatches().size());
    }

    //final match should depend on all matches
    @Test
    public void dependsOnFinalMatch01() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8,1), true);
        List<Match> matches = bracket.getAllMatches();
        for(int n = 1; n < matches.size(); n++) {
            assertTrue(matches.get(0).dependsOn(matches.get(n)));
        }
    }

    //final match should depend on all matches
    @Test
    public void dependsOnFinalMatch02() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(16,1), true);
        List<Match> matches = bracket.getAllMatches();
        for(int n = 1; n < matches.size(); n++) {
            assertTrue(matches.get(0).dependsOn(matches.get(n)));
        }
    }

    //final match should depend on all matches
    @Test
    public void dependsOnFinalMatch03() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(10,1), true);
        List<Match> matches = bracket.getAllMatches();
        for(int n = 1; n < matches.size(); n++) {
            assertTrue(matches.get(0).dependsOn(matches.get(n)));
        }
    }

    //The teams should be seeded correctly in first round
    @Test
    public void seedTest01(){
        List<Team> teamList = TestUtilities.getTestTeams(8,1);
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(teamList, true);
        assertEquals(1, bracket.getAllMatches().get(bracket.getAllMatches().size()-1).getTeamOne().getInitialSeedValue());
        assertEquals(8, bracket.getAllMatches().get(bracket.getAllMatches().size()-1).getTeamTwo().getInitialSeedValue());
        assertEquals(4, bracket.getAllMatches().get(bracket.getAllMatches().size()-2).getTeamOne().getInitialSeedValue());
        assertEquals(5, bracket.getAllMatches().get(bracket.getAllMatches().size()-2).getTeamTwo().getInitialSeedValue());
        assertEquals(2, bracket.getAllMatches().get(bracket.getAllMatches().size()-3).getTeamOne().getInitialSeedValue());
        assertEquals(7, bracket.getAllMatches().get(bracket.getAllMatches().size()-3).getTeamTwo().getInitialSeedValue());
        assertEquals(3, bracket.getAllMatches().get(bracket.getAllMatches().size()-4).getTeamOne().getInitialSeedValue());
        assertEquals(6, bracket.getAllMatches().get(bracket.getAllMatches().size()-4).getTeamTwo().getInitialSeedValue());
    }

    //first match should be null, and best seeded team should be placed in next round
    @Test
    public void seedTest02(){
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(7,1), true);
        assertNull(bracket.getMatchesAsArray()[6]);
        assertEquals(1,bracket.getMatchesAsArray()[2].getTeamOne().getInitialSeedValue());
    }

    //match 3 should be null and snd seed should be placed in next round
    @Test
    public void seedTest03(){
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(6,1), true);
        assertNull(bracket.getMatchesAsArray()[4]);
        assertEquals(2,bracket.getMatchesAsArray()[1].getTeamOne().getInitialSeedValue());
    }

    //There should only be one match in first around, this should be the worst seeded teams.
    //The winner of this match should meet seed 1
    @Test
    public void seedTest04(){
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(5,1), true);
        assertNull(bracket.getMatchesAsArray()[6]);
        assertNull(bracket.getMatchesAsArray()[4]);
        assertNull(bracket.getMatchesAsArray()[3]);
        assertEquals(4,bracket.getMatchesAsArray()[5].getTeamOne().getInitialSeedValue());
        assertEquals(5,bracket.getMatchesAsArray()[5].getTeamTwo().getInitialSeedValue());
    }

    //Should return the correct amount of playable matches
    @Test
    public void upcomingMatchesTest01(){
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8,1), true);
        assertEquals(4, bracket.getUpcomingMatches().size());
        bracket.getUpcomingMatches().get(0).setScores(1, 0, true);
        assertEquals(3, bracket.getUpcomingMatches().size());
    }

    //Should return the correct amount of playable matches
    @Test
    public void upcomingMatchesTest02(){
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(6,1), true);
        assertEquals(2, bracket.getUpcomingMatches().size());
    }

    //Should return the correct amount of not-playable upcoming matches
    @Test
    public void pendingMatchesTest01(){
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8,1), true);
        assertEquals(3, bracket.getPendingMatches().size());
        bracket.getAllMatches().get(bracket.getAllMatches().size()-1).setScores(1, 2, true);
        bracket.getAllMatches().get(bracket.getAllMatches().size()-2).setScores(1, 2, true);
        assertEquals(2, bracket.getPendingMatches().size());
    }

    //Should return the correct amount of played matches
    @Test
    public void completedMatchesTest01(){
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8,1), true);
        assertEquals(0, bracket.getCompletedMatches().size());
        bracket.getAllMatches().get(bracket.getAllMatches().size()-1).setScores(1, 0, true);
        bracket.getAllMatches().get(bracket.getAllMatches().size()-2).setScores(1, 0, true);
        assertEquals(2, bracket.getCompletedMatches().size());
    }

    //Gets top 4 teams
    @Test
    public void getTopTeamsTest01(){
        SingleEliminationFormat bracket = generateBracketsAndWins(8);
        List <Team> teamList = new ArrayList<>(bracket.getTopTeams(4, TieBreaker.SEED));
        int seedValue = 1;
        for(int i = 0; i < 4; i++) {
            assertEquals(seedValue, teamList.get(i).getInitialSeedValue());
            seedValue++;
        }
    }

    //Gets top 6 teams
    @Test
    public void getTopTeamTest02() {
        SingleEliminationFormat bracket = generateBracketsAndWins(10);
        List<Team> teamList = new ArrayList<Team>(bracket.getTopTeams(6, TieBreaker.SEED));
        int seedValue = 1;
        for(int i = 0; i < 6; i++) {
            assertEquals(seedValue, teamList.get(i).getInitialSeedValue());
            seedValue++;
        }
    }

    //Tries to get top 10 teams in a 4 team stage, should only return 4 teams
    @Test
    public void getTopTeamTest03() {
        SingleEliminationFormat bracket = generateBracketsAndWins(5);
        List<Team> teamList = new ArrayList<Team>(bracket.getTopTeams(10, TieBreaker.SEED));
        int seedValue = 1;
        for(int i = 0; i < 4; i++) {
            assertEquals(seedValue, teamList.get(i).getInitialSeedValue());
            seedValue++;
        }
    }

    //StageStatus test  pending-running-concluded
    @Test
    public void stageStatusTest01() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        assertEquals(StageStatus.PENDING, bracket.getStatus());
        bracket.start(TestUtilities.getTestTeams(4, 1), true);
        assertEquals(StageStatus.RUNNING, bracket.getStatus());
        bracket =  generateBracketsAndWins(4);
        assertEquals(StageStatus.CONCLUDED, bracket.getStatus());
    }

    //StageStatus should still be running if some matches has been played
    @Test
    public void stageStatusTest02() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8, 1), true);
        List<Match> arrayList = bracket.getAllMatches();
        arrayList.get(arrayList.size()-1).setTeamOneScore(1);
        arrayList.get(arrayList.size()-1).setHasBeenPlayed(true);
        arrayList.get(arrayList.size()-2).setTeamOneScore(1);
        arrayList.get(arrayList.size()-2).setHasBeenPlayed(true);
        assertEquals(StageStatus.RUNNING, bracket.getStatus());
    }

    //Should throw exception if the match is not playable
    @Test(expected = IllegalStateException.class)
    public void unPlayableMatch() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8, 1), true);
        bracket.getAllMatches().get(0).setTeamOneScore(1);
    }

    //Should throw exception if the match is not playable
    @Test(expected = IllegalStateException.class)
    public void unPlayableMatch02() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(8, 1), true);
        bracket.getAllMatches().get(2).setHasBeenPlayed(true);
    }

    @Test
    public void withSeeding01() {

        int numberOfTeams = 4;
        List<Team> teams = TestUtilities.getTestTeams(numberOfTeams, 1);

        SingleEliminationFormat singleelimination = new SingleEliminationFormat();
        singleelimination.start(teams, true);

        Match matchOne = singleelimination.getAllMatches().get(2);
        assertSame(teams.get(0), matchOne.getTeamOne());
        assertSame(teams.get(3), matchOne.getTeamTwo());

        Match matchTwo = singleelimination.getAllMatches().get(1);
        assertSame(teams.get(1), matchTwo.getTeamOne());
        assertSame(teams.get(2), matchTwo.getTeamTwo());
    }

    @Test
    public void withSeeding02() {

        int numberOfTeams = 8;
        List<Team> teams = TestUtilities.getTestTeams(numberOfTeams, 1);

        SingleEliminationFormat singleelimination = new SingleEliminationFormat();
        singleelimination.start(teams, true);

        Match matchOne = singleelimination.getAllMatches().get(6);
        assertSame(teams.get(0), matchOne.getTeamOne());
        assertSame(teams.get(7), matchOne.getTeamTwo());

        Match matchTwo = singleelimination.getAllMatches().get(5);
        assertSame(teams.get(3), matchTwo.getTeamOne());
        assertSame(teams.get(4), matchTwo.getTeamTwo());

        Match matchThree = singleelimination.getAllMatches().get(4);
        assertSame(teams.get(1), matchThree.getTeamOne());
        assertSame(teams.get(6), matchThree.getTeamTwo());

        Match matchFour = singleelimination.getAllMatches().get(3);
        assertSame(teams.get(2), matchFour.getTeamOne());
        assertSame(teams.get(5), matchFour.getTeamTwo());
    }

    @Test
    public void withoutSeeding01() {
        int numberOfTeams = 4;
        List<Team> teams = TestUtilities.getTestTeams(numberOfTeams, 1);

        SingleEliminationFormat singleelimination = new SingleEliminationFormat();
        singleelimination.start(teams, false);

        Match matchOne = singleelimination.getAllMatches().get(2);
        assertSame(teams.get(0), matchOne.getTeamOne());
        assertSame(teams.get(1), matchOne.getTeamTwo());

        Match matchTwo = singleelimination.getAllMatches().get(1);
        assertSame(teams.get(2), matchTwo.getTeamOne());
        assertSame(teams.get(3), matchTwo.getTeamTwo());
    }

    @Test
    public void withoutSeeding02() {

        int numberOfTeams = 8;
        List<Team> teams = TestUtilities.getTestTeams(numberOfTeams, 1);

        SingleEliminationFormat singleelimination = new SingleEliminationFormat();
        singleelimination.start(teams, false);

        Match matchOne = singleelimination.getAllMatches().get(6);
        assertSame(teams.get(0), matchOne.getTeamOne());
        assertSame(teams.get(1), matchOne.getTeamTwo());

        Match matchTwo = singleelimination.getAllMatches().get(5);
        assertSame(teams.get(2), matchTwo.getTeamOne());
        assertSame(teams.get(3), matchTwo.getTeamTwo());

        Match matchThree = singleelimination.getAllMatches().get(4);
        assertSame(teams.get(4), matchThree.getTeamOne());
        assertSame(teams.get(5), matchThree.getTeamTwo());

        Match matchFour = singleelimination.getAllMatches().get(3);
        assertSame(teams.get(6), matchFour.getTeamOne());
        assertSame(teams.get(7), matchFour.getTeamTwo());
    }

    @Test
    public void matchIdentifiers01() {
        List<Team> teams = TestUtilities.getTestTeams(4, 1);
        SingleEliminationFormat singleElimination = new SingleEliminationFormat();
        singleElimination.start(teams, true);

        List<Match> allMatches = singleElimination.getAllMatches();

        assertEquals(3, allMatches.get(0).getIdentifier());
        assertEquals(2, allMatches.get(1).getIdentifier());
        assertEquals(1, allMatches.get(2).getIdentifier());
    }

    @Test
    public void matchIdentifiers02() {
        List<Team> teams = TestUtilities.getTestTeams(6, 1);
        SingleEliminationFormat singleElimination = new SingleEliminationFormat();
        singleElimination.start(teams, true);

        List<Match> allMatches = singleElimination.getAllMatches();

        assertEquals(5, allMatches.get(0).getIdentifier());
        assertEquals(4, allMatches.get(1).getIdentifier());
        assertEquals(3, allMatches.get(2).getIdentifier());
        assertEquals(2, allMatches.get(3).getIdentifier());
        assertEquals(1, allMatches.get(4).getIdentifier());
    }

    /** Generates a bracket and sets wins according to the best seed
     * @param amountOfTeams the amount of teams */
    private SingleEliminationFormat generateBracketsAndWins(int amountOfTeams) {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        bracket.start(TestUtilities.getTestTeams(amountOfTeams,1), true);

        for(int matchIndex = bracket.getAllMatches().size()-1 ; matchIndex >= 0; matchIndex--){
            if(bracket.getAllMatches().get(matchIndex).getTeamOne().getInitialSeedValue() < bracket.getAllMatches().get(matchIndex).getTeamTwo().getInitialSeedValue()) {
                bracket.getAllMatches().get(matchIndex).setTeamOneScore(1);
                bracket.getAllMatches().get(matchIndex).setHasBeenPlayed(true);
            }
            else{
                bracket.getAllMatches().get(matchIndex).setTeamTwoScore(1);
                bracket.getAllMatches().get(matchIndex).setHasBeenPlayed(true);
            }
        }
        return bracket;
    }
}