package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.TestUtilities;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.model.stats.StatsTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static dk.aau.cs.ds306e18.tournament.TestUtilities.*;
import static org.junit.Assert.*;

public class SwissFormatTest {

    //Even number of teams
    @Test
    public void calculateMaxRounds01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(numberOfTeams - 1, sw.getMaxRoundsPossible());
    }

    //Odd number of teams
    @Test
    public void calculateMaxRounds02(){

        int numberOfTeams = 5;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(numberOfTeams, sw.getMaxRoundsPossible());
    }

    //No teams
    @Test
    public void calculateMaxRounds03(){

        int numberOfTeams = 0;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(numberOfTeams, sw.getMaxRoundsPossible());
    }

    //more than 0 matches // one round
    @Test
    public void getAllMatches01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        List<Series> allSeries = sw.getAllSeries();

        assertEquals(numberOfTeams/2, allSeries.size());
    }

    //0 matches // one round
    @Test
    public void getAllMatches02(){

        int numberOfTeams = 0;
        int teamSize = 0;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        List<Series> allSeries = sw.getAllSeries();

        assertEquals(0, allSeries.size());
    }

    //more than 0 matches // more round
    @Test
    public void getAllMatches03(){

        int numberOfTeams = 12;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        //The first round.
        assertEquals(numberOfTeams/2, sw.getAllSeries().size());

        //Fill in scores
        List<Series> series = sw.getLatestRound();
        for(Series serie : series){
            serie.setScores(5, 2, 0);
            serie.setHasBeenPlayed(true);
        }

        sw.startNextRound();

        assertEquals((numberOfTeams/2) * 2, sw.getAllSeries().size());
    }

    @Test
    public void getPendingMatches01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        List<Series> unplayedSeries = sw.getPendingMatches();

        assertEquals(0, unplayedSeries.size());
    }

    //0 matches
    @Test
    public void getPendingMatches02(){

        int numberOfTeams = 0;
        int teamSize = 0;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        List<Series> unplayedSeries = sw.getPendingMatches();

        assertEquals(0, unplayedSeries.size());
    }

    @Test
    public void getPendingMatches03(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        //All has to be played
        setAllMatchesToPlayed(sw.getUpcomingMatches());

        assertEquals(0 , sw.getPendingMatches().size());
    }

    @Test
    public void getUpcomingMatches01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(numberOfTeams/2 , sw.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches02(){

        int numberOfTeams = 16;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(numberOfTeams/2 , sw.getUpcomingMatches().size());
    }

    @Test
    public void getUpcomingMatches03(){

        int numberOfTeams = 1;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(0 , sw.getUpcomingMatches().size());
    }

    @Test
    public void getCompletedMatches01(){
        int numberOfTeams = 4;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertEquals(0 , sw.getCompletedMatches().size());
    }

    @Test
    public void getCompletedMatches02(){
        int numberOfTeams = 4;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        setAllMatchesToPlayed(sw.getUpcomingMatches());

        assertEquals(sw.getAllSeries().size() , sw.getCompletedMatches().size());
    }

    //Create round is legal
    @Test
    public void createNewRound01(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        //All has to be played
        setAllMatchesToPlayed(sw.getUpcomingMatches());

        assertTrue(sw.startNextRound());
    }

    //Create round is illegal: max rounds has been created.
    @Test
    public void createNewRound02(){

        int numberOfTeams = 2;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        //All has to be played
        setAllMatchesToPlayed(sw.getUpcomingMatches());

        assertFalse(sw.startNextRound());
    }

    //Create new round is illegal: no matches has been played
    @Test
    public void createNewRound03(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertFalse(sw.startNextRound());
    }

    //No team can play each other more than once
    @Test
    public void createNewRound04(){

        //Create a list of unique teams.
        List<Team> teams = getTestTeams(7, 1);

        //Create the sw with the teams
        SwissFormat sw = new SwissFormat();
        sw.start(teams, true);

        //Generate all rounds and fill result
        do{
            List<Series> series = sw.getUpcomingMatches();
            for(Series serie : series) {
                serie.setScores(1, 0, 0);
                serie.setHasBeenPlayed(true);
            }

            sw.startNextRound();
        }while(!sw.hasUnstartedRounds());

        List<Series> allSeries = sw.getAllSeries();

        //Check if no teams has played each other more than once
        for(int i = 0; i < allSeries.size(); i++){

            for(int j = i + 1; j < allSeries.size(); j++){

                Series series1 = allSeries.get(i);
                Series series2 = allSeries.get(j);

                //System.out.println("Match Comp 1: Match1B: " + match1.getTeamOne().getTeamName() + " Match1O " + match1.getTeamTwo().getTeamName()
                // + " Match2B " + match2.getTeamOne().getTeamName() + " Match2O " + match2.getTeamTwo().getTeamName());

                assertFalse(series1.getTeamOne().getTeamName().equals(series2.getTeamOne().getTeamName()) &&
                        series1.getTeamTwo().getTeamName().equals(series2.getTeamTwo().getTeamName()));
                assertFalse(series1.getTeamOne().getTeamName().equals(series2.getTeamTwo().getTeamName()) &&
                        series1.getTeamTwo().getTeamName().equals(series2.getTeamOne().getTeamName()));
            }
        }
    }

    //Create round is illegal: only one team.
    @Test
    public void createNewRound05(){

        int numberOfTeams = 1;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        //All has to be played
        setAllMatchesToPlayed(sw.getUpcomingMatches());

        assertFalse(sw.startNextRound());
    }

    @Test
    public void hasUnstartedRounds01(){

        int numberOfTeams = 2;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertFalse(sw.hasUnstartedRounds());
    }

    @Test
    public void hasUnstartedRounds02(){

        int numberOfTeams = 4;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertTrue(sw.hasUnstartedRounds());
    }

    @Test
    public void hasUnstartedRounds03(){

        int numberOfTeams = 12;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.setRoundCount(1);
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertFalse(sw.hasUnstartedRounds());
    }

    @Test
    public void setRoundCount01() {
        int numberOfTeams = 12;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.setRoundCount(3);
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertTrue(sw.hasUnstartedRounds());
        assertEquals(3, sw.getRoundCount());
    }

    @Test
    public void setRoundCount02() {
        int numberOfTeams = 8;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.setRoundCount(44444);
        sw.start(getTestTeams(numberOfTeams, teamSize), true);

        assertTrue(sw.hasUnstartedRounds());
        assertEquals(7, sw.getRoundCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setRoundCount03() {
        SwissFormat sw = new SwissFormat();
        sw.setRoundCount(-2);
    }

    @Test(expected = IllegalStateException.class)
    public void setRoundCount04() {
        int numberOfTeams = 8;
        int teamSize = 2;

        SwissFormat sw = new SwissFormat();
        sw.setRoundCount(3);
        sw.start(getTestTeams(numberOfTeams, teamSize), true);
        sw.setRoundCount(2);
    }

    @Test
    public void getStatus01(){ //Pending

        SwissFormat sw = new SwissFormat();

        assertEquals(StageStatus.PENDING, sw.getStatus());
    }

    @Test
    public void getStatus02(){ //Running

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(4, 2), true);
        sw.startNextRound();

        assertEquals(StageStatus.RUNNING, sw.getStatus());
    }

    @Test
    public void getStatus03(){ //Concluded // max number of rounds and all played

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(2,2), true);
        sw.startNextRound();

        //Set all matches to played
        setAllMatchesToPlayed(sw.getUpcomingMatches());

        assertEquals(StageStatus.CONCLUDED, sw.getStatus());
    }

    @Test
    public void getStatus04(){ //Concluded //max number of round but not played

        SwissFormat sw = new SwissFormat();
        sw.start(getTestTeams(2,2), true);
        sw.startNextRound();

        assertNotEquals(StageStatus.CONCLUDED, sw.getStatus());
    }

    @Test
    public void getTopTeams01(){ //No teams

        SwissFormat sw = new SwissFormat();
        sw.start(new ArrayList<>(), true);

        assertEquals(0, sw.getTopTeams(0, TieBreaker.SEED).size());
    }

    @Test
    public void getTopTeams02(){

        SwissFormat sw = new SwissFormat();
        List<Team> inputTeams = getTestTeams(4, 2);
        sw.start(inputTeams, true);

        setAllMatchesToPlayed(sw.getUpcomingMatches());
        //All teams now have the same amount of points.

        ArrayList<Team> top3Teams = new ArrayList<>(sw.getTopTeams(3, TieBreaker.SEED));

        //The top teams should be the ones with the lowest seeds

        //Sort the input teams by seed
        Team teamWithWorstSeed = inputTeams.get(0);

        //Find team with highest seed
        for(Team team : inputTeams){
            if(team.getInitialSeedValue() > teamWithWorstSeed.getInitialSeedValue()){
                teamWithWorstSeed = team;
            }
        }

        //Make sure that that team is not a part of the top 3
        for(Team team : top3Teams)
            assertNotSame(team, teamWithWorstSeed);
    }

    @Test
    public void stats01() {
        SwissFormat sw = new SwissFormat();
        List<Team> teams = TestUtilities.getTestTeams(4, 1);
        sw.start(teams, true);

        // Play all matches. The highest seeded team wins 1-0
        while (sw.getStatus() == StageStatus.RUNNING) {
            List<Series> latestRound = sw.getLatestRound();
            for (Series series : latestRound) {
                if (series.getTeamOne().getInitialSeedValue() < series.getTeamTwo().getInitialSeedValue()) {
                    series.setScores(1, 0, 0);
                } else {
                    series.setScores(0, 1, 0);
                }
                series.setHasBeenPlayed(true);
            }
            sw.startNextRound();
        }

        // Check if stats are as expected
        StatsTest.assertStats(teams.get(0), sw, 3, 0, 3, 0);
        StatsTest.assertStats(teams.get(1), sw, 2, 1, 2, 1);
        StatsTest.assertStats(teams.get(2), sw, 1, 2, 1, 2);
        StatsTest.assertStats(teams.get(3), sw, 0, 3, 0, 3);
    }
}