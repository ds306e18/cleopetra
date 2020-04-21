package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.model.match.Series;
import dk.aau.cs.ds306e18.tournament.model.match.MatchResultDependencyException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SeriesTest {

    @Test
    public void isReadyToPlay01() {
        Series series = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        assertTrue(series.isReadyToPlay());
    }

    @Test
    public void isReadyToPlay02() {
        Series firstSeries = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Series secondSeries = new Series().setTeamOne(new Team("C", null, 0, "c")).setTeamTwoToWinnerOf(firstSeries);
        secondSeries.setTeamTwoToWinnerOf(firstSeries);
        assertFalse(secondSeries.isReadyToPlay());

        firstSeries.setScores(4, 2, 0);
        firstSeries.setHasBeenPlayed(true);
        assertTrue(secondSeries.isReadyToPlay());
    }

    @Test
    public void getWinnerAndLoser01() {
        // team one wins
        Team expectedWinner = new Team("A", null, 0, "a");
        Team expectedLoser = new Team("B", null, 0, "b");
        Series series = new Series(expectedWinner, expectedLoser);
        series.setScores(3, 2, 0);
        series.setHasBeenPlayed(true);
        assertSame(series.getWinner(), expectedWinner);
        assertSame(series.getLoser(), expectedLoser);
    }

    @Test
    public void getWinnerAndLoser02() {
        // team two wins
        Team expectedWinner = new Team("A", null, 0, "a");
        Team expectedLoser = new Team("B", null, 0, "b");
        Series series = new Series(expectedLoser, expectedWinner);
        series.setScores(3, 5, 0);
        series.setHasBeenPlayed(true);
        assertSame(series.getWinner(), expectedWinner);
        assertSame(series.getLoser(), expectedLoser);
    }

    @Test(expected = IllegalStateException.class)
    public void getWinnerAndLoser03() {
        Series series = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        series.setScores(3, 5, 0); // note: match is not finished
        series.getWinner();
    }

    @Test(expected = IllegalStateException.class)
    public void getWinnerAndLoser04() {
        Series series = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        series.setScores(3, 5, 0); // note: match is not finished
        series.getLoser();
    }

    @Test
    public void getStatus01() {
        Series firstSeries = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Series secondSeries = new Series().setTeamOne(new Team("C", null, 0, "c")).setTeamTwoToWinnerOf(firstSeries);
        assertSame(secondSeries.getStatus(), Series.Status.NOT_PLAYABLE);
    }

    @Test
    public void getStatus02() {
        Series firstSeries = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Series secondSeries = new Series().setTeamOne(new Team("C", null, 0, "c")).setTeamTwoToWinnerOf(firstSeries);
        firstSeries.setScores(0, 2, 0);
        firstSeries.setHasBeenPlayed(true);
        assertSame(secondSeries.getStatus(), Series.Status.READY_TO_BE_PLAYED);
    }

    @Test
    public void getStatus03() {
        Series series = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        series.setScores(0, 0, 0);
        series.setHasBeenPlayed(true);
        assertSame(series.getOutcome(), Series.Outcome.DRAW);
    }


    @Test
    public void getStatus04() {
        Series series = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        series.setScores(2, 0, 0);
        series.setHasBeenPlayed(true);
        assertSame(series.getOutcome(), Series.Outcome.TEAM_ONE_WINS);
    }

    @Test
    public void getStatus05() {
        Series series = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        series.setScores(0, 2, 0);
        series.setHasBeenPlayed(true);
        assertSame(series.getOutcome(), Series.Outcome.TEAM_TWO_WINS);
    }

    @Test
    public void dependsOn01() {
        Series firstSeries = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Series secondSeries = new Series().setTeamOne(new Team("C", null, 0, "c")).setTeamTwoToWinnerOf(firstSeries);
        assertTrue(secondSeries.dependsOn(firstSeries));
    }

    @Test
    public void dependsOn02() {
        Series firstSeries = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Series secondSeries = new Series().setTeamOne(new Team("C", null, 0, "c")).setTeamTwoToLoserOf(firstSeries);
        assertTrue(secondSeries.dependsOn(firstSeries));
    }

    @Test
    public void dependsOn03() {
        Series firstSeries = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Series secondSeries = new Series().setTeamOne(new Team("C", null, 0, "c")).setTeamTwoToWinnerOf(firstSeries);
        assertFalse(firstSeries.dependsOn(secondSeries));
    }

    @Test
    public void dependsOn04() {
        Series firstSeries = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Series secondSeries = new Series().setTeamOne(new Team("C", null, 0, "c")).setTeamTwoToLoserOf(firstSeries);
        assertFalse(firstSeries.dependsOn(secondSeries));
    }

    @Test
    public void dependsOn05() {
        Series firstSeries = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Series secondSeries = new Series(new Team("C", null, 0, "c"), new Team("D", null, 0, "d"));
        Series thirdSeries = new Series().setTeamOneToWinnerOf(firstSeries).setTeamTwoToLoserOf(secondSeries);

        assertFalse(firstSeries.dependsOn(secondSeries));
        assertFalse(secondSeries.dependsOn(firstSeries));

        assertTrue(thirdSeries.dependsOn(firstSeries));
        assertTrue(thirdSeries.dependsOn(secondSeries));
    }

    @Test
    public void getTreeAsListBFS01() {
        Series firstSeries = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Series secondSeries = new Series(new Team("C", null, 0, "c"), new Team("D", null, 0, "d"));
        Series thirdSeries = new Series(new Team("E", null, 0, "e"), new Team("F", null, 0, "f"));
        Series fourthSeries = new Series(new Team("G", null, 0, "g"), new Team("H", null, 0, "h"));
        Series firstSemiFinal = new Series().setTeamOneToWinnerOf(firstSeries).setTeamTwoToWinnerOf(secondSeries);
        Series secondSemiFinal = new Series().setTeamOneToWinnerOf(thirdSeries).setTeamTwoToWinnerOf(fourthSeries);
        Series finalSeries = new Series().setTeamOneToWinnerOf(firstSemiFinal).setTeamTwoToWinnerOf(secondSemiFinal);

        List<Series> bfs = finalSeries.getTreeAsListBFS();

        assertSame(finalSeries, bfs.get(0));
        assertSame(secondSemiFinal, bfs.get(1));
        assertSame(firstSemiFinal, bfs.get(2));
        assertSame(fourthSeries, bfs.get(3));
        assertSame(thirdSeries, bfs.get(4));
        assertSame(secondSeries, bfs.get(5));
        assertSame(firstSeries, bfs.get(6));
    }

    @Test
    public void getTreeAsListBFS02() {
        Series firstSeries = new Series(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Series secondSeries = new Series().setTeamOneToWinnerOf(firstSeries).setTeamTwo(new Team("C", null, 0, "c"));
        Series thirdSeries = new Series().setTeamOneToWinnerOf(secondSeries).setTeamTwo(new Team("D", null, 0, "d"));

        List<Series> bfs = thirdSeries.getTreeAsListBFS();

        assertSame(thirdSeries, bfs.get(0));
        assertSame(secondSeries, bfs.get(1));
        assertSame(firstSeries, bfs.get(2));
        assertEquals(3, bfs.size());
    }

    @Test
    public void reconnect01() {
        Team expectedWinner = new Team("A", null, 0, "a");
        Series seriesOne = new Series(expectedWinner, new Team("B", null, 0, "b"));
        Series seriesTwo = new Series().setTeamOne(new Team("C", null, 0, "c")).setTeamTwoToWinnerOf(seriesOne);
        seriesOne.setScores(4, 2, 0);
        seriesOne.setHasBeenPlayed(true);

        assertSame(expectedWinner, seriesTwo.getTeamTwo());

        // change match one winner now goes to third match instead
        Series seriesThree = new Series().setTeamOne(new Team("D", null, 0, "d")).setTeamTwoToWinnerOf(seriesOne);

        assertFalse(seriesTwo.dependsOn(seriesOne));
        assertNull(seriesTwo.getTeamTwo());
        assertSame(expectedWinner, seriesThree.getTeamTwo());
    }

    @Test
    public void reconnect02() {
        Team expectedWinner = new Team("A", null, 0, "a");
        Series seriesOne = new Series(expectedWinner, new Team("B", null, 0, "b"));
        Series seriesTwo = new Series().setTeamOne(new Team("C", null, 0, "c")).setTeamTwoToWinnerOf(seriesOne);
        seriesOne.setScores(4, 2, 0);
        seriesOne.setHasBeenPlayed(true);

        assertSame(expectedWinner, seriesTwo.getTeamTwo());

        // change match two's team two to be winner of match three
        Series seriesThree = new Series(new Team("D", null, 0, "d"), new Team("E", null, 0, "e"));
        seriesTwo.setTeamTwoToWinnerOf(seriesThree);

        assertFalse(seriesTwo.dependsOn(seriesOne));
        assertTrue(seriesTwo.dependsOn(seriesThree));
        assertNull(seriesTwo.getTeamTwo());
    }

    @Test
    public void reconnect03() {
        Team expectedLoser = new Team("A", null, 0, "a");
        Series seriesOne = new Series(expectedLoser, new Team("B", null, 0, "b"));
        Series seriesTwo = new Series().setTeamOne(new Team("C", null, 0, "c")).setTeamTwoToLoserOf(seriesOne);
        seriesOne.setScores(0, 3, 0);
        seriesOne.setHasBeenPlayed(true);

        assertSame(expectedLoser, seriesTwo.getTeamTwo());

        // change match one loser now goes to third match instead
        Series seriesThree = new Series().setTeamOne(new Team("D", null, 0, "d")).setTeamTwoToLoserOf(seriesOne);

        assertFalse(seriesTwo.dependsOn(seriesOne));
        assertNull(seriesTwo.getTeamTwo());
        assertSame(expectedLoser, seriesThree.getTeamTwo());
    }

    @Test
    public void reconnect04() {
        Team expectedLoser = new Team("A", null, 0, "a");
        Series seriesOne = new Series(expectedLoser, new Team("B", null, 0, "b"));
        Series seriesTwo = new Series().setTeamOne(new Team("C", null, 0, "c")).setTeamTwoToLoserOf(seriesOne);
        seriesOne.setScores(0, 3, 0);
        seriesOne.setHasBeenPlayed(true);

        assertSame(expectedLoser, seriesTwo.getTeamTwo());

        // change match two's team two to be loser of match three
        Series seriesThree = new Series(new Team("D", null, 0, "d"), new Team("E", null, 0, "e"));
        seriesTwo.setTeamTwoToLoserOf(seriesThree);

        assertFalse(seriesTwo.dependsOn(seriesOne));
        assertTrue(seriesTwo.dependsOn(seriesThree));
        assertNull(seriesTwo.getTeamTwo());
    }

    @Test
    public void setScores01(){

        Team teamOne = new Team("A", null, 1, null);
        Team teamTwo = new Team("B", null, 1, null);
        Series series = new Series(teamOne, teamTwo);

        int teamOneScore = 5;
        int teamTwoScore = 2;
        series.setScores(teamOneScore, teamTwoScore, 0);
        series.setHasBeenPlayed(true);

        assertEquals(teamOneScore, (int) series.getTeamOneScore(0).get());
        assertEquals(teamTwoScore, (int) series.getTeamTwoScore(0).get());
    }

    @Test
    public void setScores02(){

        Team teamOne = new Team(null, null, 1, null);
        Team teamTwo = new Team(null, null, 1, null);
        Series series = new Series(teamOne, teamTwo);

        int teamOneScore1 = 5;
        int teamTwoScore1 = 2;
        int teamOneScore2 = 2;
        int teamTwoScore2 = 2;
        series.setScores(teamOneScore1, teamTwoScore1, 0);
        series.setHasBeenPlayed(true);
        series.setScores(teamOneScore2, teamTwoScore2, 0);
        series.setHasBeenPlayed(true);

        assertEquals(teamOneScore2, (int) series.getTeamOneScore(0).get());
        assertEquals(teamTwoScore2, (int) series.getTeamTwoScore(0).get());
    }

    @Test
    public void reconnectAfterPlay01() {
        Team tA = new Team("A", null, 0, "a");
        Team tE = new Team("E", null, 0, "e");
        Series seriesOne = new Series(tA, new Team("B", null, 0, "b"));
        Series seriesTwo = new Series().setTeamOne(new Team("C", null, 0, "c")).setTeamTwoToWinnerOf(seriesOne);
        Series seriesThree = new Series(tE, new Team("D", null, 0, "d"));

        // Team A and E wins
        seriesOne.setScores(1, 0, 0);
        seriesOne.setHasBeenPlayed(true);
        seriesThree.setScores(1, 0, 0);
        seriesThree.setHasBeenPlayed(true);

        // Match two's team two should be A
        assertSame(tA, seriesTwo.getTeamTwo());

        // Match two's team two should be E, if we change it to be winner of match three
        seriesTwo.setTeamTwoToWinnerOf(seriesThree);
        assertSame(tE, seriesTwo.getTeamTwo());
    }

    @Test
    public void reset01() {
        Team teamA = new Team("A", null, 0, "a");
        Team teamB = new Team("B", null, 0, "b");
        Team teamC = new Team("C", null, 0, "c");

        Series seriesOne = new Series(teamA, teamB);
        Series seriesTwo = new Series().setTeamOne(teamC).setTeamTwoToWinnerOf(seriesOne);
        seriesOne.setScores(2, 1, 0);
        seriesOne.setHasBeenPlayed(true);
        seriesTwo.setScores(4, 3, 0);

        assertEquals(2, (int) seriesOne.getTeamOneScore(0).get());
        assertEquals(1, (int) seriesOne.getTeamTwoScore(0).get());
        assertEquals(4, (int) seriesTwo.getTeamOneScore(0).get());
        assertEquals(3, (int) seriesTwo.getTeamTwoScore(0).get());

        // This forced reset should remove all scores. Also in match two that has not been played
        seriesOne.forceReset();

        assertFalse(seriesOne.getTeamOneScore(0).isPresent());
        assertFalse(seriesOne.getTeamTwoScore(0).isPresent());
        assertFalse(seriesTwo.getTeamOneScore(0).isPresent());
        assertFalse(seriesTwo.getTeamTwoScore(0).isPresent());
    }

    @Test(expected = MatchResultDependencyException.class)
    public void reset02() {
        Team teamA = new Team("A", null, 0, "a");
        Team teamB = new Team("B", null, 0, "b");
        Team teamC = new Team("C", null, 0, "c");

        Series seriesOne = new Series(teamA, teamB);
        Series seriesTwo = new Series().setTeamOne(teamC).setTeamTwoToWinnerOf(seriesOne);
        seriesOne.setScores(2, 1, 0);
        seriesOne.setHasBeenPlayed(true);
        seriesTwo.setScores(4, 3, 0);

        assertEquals(2, (int) seriesOne.getTeamOneScore(0).get());
        assertEquals(1, (int) seriesOne.getTeamTwoScore(0).get());
        assertEquals(4, (int) seriesTwo.getTeamOneScore(0).get());
        assertEquals(3, (int) seriesTwo.getTeamTwoScore(0).get());

        // This reset should not be legal as match two has entered scores
        seriesOne.softReset();
    }

    @Test(expected = MatchResultDependencyException.class)
    public void reset03() {
        Team teamA = new Team("A", null, 0, "a");
        Team teamB = new Team("B", null, 0, "b");
        Team teamC = new Team("C", null, 0, "c");

        Series seriesOne = new Series(teamA, teamB);
        Series seriesTwo = new Series().setTeamOne(teamC).setTeamTwoToWinnerOf(seriesOne);
        seriesOne.setScores(2, 1, 0);
        seriesOne.setHasBeenPlayed(true);
        seriesTwo.setScores(4, 3, 0);
        seriesTwo.setHasBeenPlayed(true);

        assertEquals(2, (int) seriesOne.getTeamOneScore(0).get());
        assertEquals(1, (int) seriesOne.getTeamTwoScore(0).get());
        assertEquals(4, (int) seriesTwo.getTeamOneScore(0).get());
        assertEquals(3, (int) seriesTwo.getTeamTwoScore(0).get());

        // This reset should not be legal as match two has been played
        seriesOne.softReset();
    }

    @Test
    public void reset04() {
        Team teamA = new Team("A", null, 0, "a");
        Team teamB = new Team("B", null, 0, "b");
        Team teamC = new Team("C", null, 0, "c");

        Series seriesOne = new Series(teamA, teamB);
        Series seriesTwo = new Series().setTeamOne(teamC).setTeamTwoToWinnerOf(seriesOne);
        seriesOne.setScores(2, 1, 0);
        seriesOne.setHasBeenPlayed(true);
        seriesTwo.setScores(4, 3, 0);

        assertEquals(2, (int) seriesOne.getTeamOneScore(0).get());
        assertEquals(1, (int) seriesOne.getTeamTwoScore(0).get());
        assertEquals(4, (int) seriesTwo.getTeamOneScore(0).get());
        assertEquals(3, (int) seriesTwo.getTeamTwoScore(0).get());

        // This should be legal as it doesn't change the outcome of match one
        seriesOne.setScores(4, 2, 0);

        assertEquals(4, (int) seriesOne.getTeamOneScore(0).get());
        assertEquals(2, (int) seriesOne.getTeamTwoScore(0).get());
        assertEquals(4, (int) seriesTwo.getTeamOneScore(0).get());
        assertEquals(3, (int) seriesTwo.getTeamTwoScore(0).get());
    }

    @Test
    public void reset05() {
        Team teamA = new Team("A", null, 0, "a");
        Team teamB = new Team("B", null, 0, "b");
        Team teamC = new Team("C", null, 0, "c");

        Series seriesOne = new Series(teamA, teamB);
        Series seriesTwo = new Series().setTeamOne(teamC).setTeamTwoToWinnerOf(seriesOne);
        seriesOne.setScores(2, 1, 0);
        seriesOne.setHasBeenPlayed(true);
        seriesTwo.setScores(4, 3, 0);
        seriesTwo.setHasBeenPlayed(true);

        assertEquals(2, (int) seriesOne.getTeamOneScore(0).get());
        assertEquals(1, (int) seriesOne.getTeamTwoScore(0).get());
        assertEquals(4, (int) seriesTwo.getTeamOneScore(0).get());
        assertEquals(3, (int) seriesTwo.getTeamTwoScore(0).get());

        // This reset should be legal as it doesn't change the outcome of match one, even when match two has been played
        seriesOne.setScores(4, 2, 0);

        assertEquals(4, (int) seriesOne.getTeamOneScore(0).get());
        assertEquals(2, (int) seriesOne.getTeamTwoScore(0).get());
        assertEquals(4, (int) seriesTwo.getTeamOneScore(0).get());
        assertEquals(3, (int) seriesTwo.getTeamTwoScore(0).get());
    }

    @Test
    public void identifier01() {
        Team teamA = new Team("A", null, 0, "a");
        Team teamB = new Team("B", null, 0, "b");
        Team teamC = new Team("C", null, 0, "c");
        Team teamD = new Team("D", null, 0, "d");

        Series seriesOne = new Series(teamA, teamB);
        Series seriesTwo = new Series().setTeamOne(teamC).setTeamTwoToWinnerOf(seriesOne);
        Series seriesThree = new Series().setTeamTwo(teamD).setTeamOneToLoserOf(seriesOne);

        seriesOne.setIdentifier(1);
        seriesTwo.setIdentifier(2);
        seriesThree.setIdentifier(3);

        assertEquals("Winner of 1", seriesTwo.getTeamTwoAsString());
        assertEquals("C", seriesTwo.getTeamOneAsString());
        assertEquals("Loser of 1", seriesThree.getTeamOneAsString());
        assertEquals("D", seriesThree.getTeamTwoAsString());
    }

    @Test
    public void switchingColors01() {
        Team teamA = new Team("A", null, 0, "a");
        Team teamB = new Team("B", null, 0, "b");

        Series series = new Series(teamA, teamB);

        assertSame(teamA, series.getTeamOne());
        assertSame(teamB, series.getTeamTwo());
        assertSame(teamA, series.getBlueTeam());
        assertSame(teamB, series.getOrangeTeam());

        series.setTeamOneToBlue(false);

        assertSame(teamA, series.getTeamOne());
        assertSame(teamB, series.getTeamTwo());
        assertSame(teamB, series.getBlueTeam());
        assertSame(teamA, series.getOrangeTeam());
    }

    @Test
    public void switchingColors02() {
        Team teamA = new Team("A", null, 0, "a");
        Team teamB = new Team("B", null, 0, "b");

        Series series = new Series(teamA, teamB);
        series.setScores(2, 0, 0);
        series.setHasBeenPlayed(true);

        assertEquals(2, (int) series.getTeamOneScore(0).get());
        assertEquals(0, (int) series.getTeamTwoScore(0).get());
        assertEquals(2, (int) series.getBlueScore(0).get());
        assertEquals(0, (int) series.getOrangeScore(0).get());

        series.setTeamOneToBlue(false);

        assertEquals(2, (int) series.getTeamOneScore(0).get());
        assertEquals(0, (int) series.getTeamTwoScore(0).get());
        assertEquals(0, (int) series.getBlueScore(0).get());
        assertEquals(2, (int) series.getOrangeScore(0).get());
    }

    @Test
    public void switchingColors03() {
        Team teamA = new Team("A", null, 0, "a");
        Team teamB = new Team("B", null, 0, "b");

        Series series = new Series(teamA, teamB);
        series.setScores(2, 0, 0);
        series.setHasBeenPlayed(true);

        assertEquals("A", series.getTeamOneAsString());
        assertEquals("B", series.getTeamTwoAsString());
        assertEquals("A", series.getBlueTeamAsString());
        assertEquals("B", series.getOrangeTeamAsString());

        series.setTeamOneToBlue(false);

        assertEquals("A", series.getTeamOneAsString());
        assertEquals("B", series.getTeamTwoAsString());
        assertEquals("B", series.getBlueTeamAsString());
        assertEquals("A", series.getOrangeTeamAsString());
    }
}