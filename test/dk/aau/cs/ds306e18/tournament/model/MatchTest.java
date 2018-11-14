package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchStatus;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MatchTest {

    @Test
    public void isReadyToPlay01() {
        Match match = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        assertTrue(match.isReadyToPlay());
    }

    @Test
    public void isReadyToPlay02() {
        Match firstMatch = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Match secondMatch = new Match().setBlue(new Team("C", null, 0, "c")).setOrangeToWinnerOf(firstMatch);
        secondMatch.setOrangeToWinnerOf(firstMatch);
        assertFalse(secondMatch.isReadyToPlay());

        firstMatch.setScores(4, 2, true);
        assertTrue(secondMatch.isReadyToPlay());
    }

    @Test
    public void getWinnerAndLoser01() {
        // team blue wins
        Team expectedWinner = new Team("A", null, 0, "a");
        Team expectedLoser = new Team("B", null, 0, "b");
        Match match = new Match(expectedWinner, expectedLoser);
        match.setScores(3, 2, true);
        assertSame(match.getWinner(), expectedWinner);
        assertSame(match.getLoser(), expectedLoser);
    }

    @Test
    public void getWinnerAndLoser02() {
        // team orange wins
        Team expectedWinner = new Team("A", null, 0, "a");
        Team expectedLoser = new Team("B", null, 0, "b");
        Match match = new Match(expectedLoser, expectedWinner);
        match.setScores(3, 5, true);
        assertSame(match.getWinner(), expectedWinner);
        assertSame(match.getLoser(), expectedLoser);
    }

    @Test(expected = IllegalStateException.class)
    public void getWinnerAndLoser03() {
        Match match = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        match.setScores(3, 5); // note: match is not finished
        match.getWinner();
    }

    @Test(expected = IllegalStateException.class)
    public void getWinnerAndLoser04() {
        Match match = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        match.setScores(3, 5); // note: match is not finished
        match.getLoser();
    }

    @Test
    public void getStatus01() {
        Match firstMatch = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Match secondMatch = new Match().setBlue(new Team("C", null, 0, "c")).setOrangeToWinnerOf(firstMatch);
        assertSame(secondMatch.getStatus(), MatchStatus.NOT_PLAYABLE);
    }

    @Test
    public void getStatus02() {
        Match firstMatch = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Match secondMatch = new Match().setBlue(new Team("C", null, 0, "c")).setOrangeToWinnerOf(firstMatch);
        firstMatch.setScores(0, 2, true);
        assertSame(secondMatch.getStatus(), MatchStatus.READY_TO_BE_PLAYED);
    }

    @Test
    public void getStatus03() {
        Match match = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        match.setScores(0, 0, true);
        assertSame(match.getStatus(), MatchStatus.DRAW);
    }


    @Test
    public void getStatus04() {
        Match match = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        match.setScores(2, 0, true);
        assertSame(match.getStatus(), MatchStatus.BLUE_WINS);
    }

    @Test
    public void getStatus05() {
        Match match = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        match.setScores(0, 2, true);
        assertSame(match.getStatus(), MatchStatus.ORANGE_WINS);
    }

    @Test
    public void dependsOn01() {
        Match firstMatch = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Match secondMatch = new Match().setBlue(new Team("C", null, 0, "c")).setOrangeToWinnerOf(firstMatch);
        assertTrue(secondMatch.dependsOn(firstMatch));
    }

    @Test
    public void dependsOn02() {
        Match firstMatch = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Match secondMatch = new Match().setBlue(new Team("C", null, 0, "c")).setOrangeToLoserOf(firstMatch);
        assertTrue(secondMatch.dependsOn(firstMatch));
    }

    @Test
    public void dependsOn03() {
        Match firstMatch = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Match secondMatch = new Match().setBlue(new Team("C", null, 0, "c")).setOrangeToWinnerOf(firstMatch);
        assertFalse(firstMatch.dependsOn(secondMatch));
    }

    @Test
    public void dependsOn04() {
        Match firstMatch = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Match secondMatch = new Match().setBlue(new Team("C", null, 0, "c")).setOrangeToLoserOf(firstMatch);
        assertFalse(firstMatch.dependsOn(secondMatch));
    }

    @Test
    public void dependsOn05() {
        Match firstMatch = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Match secondMatch = new Match(new Team("C", null, 0, "c"), new Team("D", null, 0, "d"));
        Match thirdMatch = new Match().setBlueToWinnerOf(firstMatch).setOrangeToLoserOf(secondMatch);

        assertFalse(firstMatch.dependsOn(secondMatch));
        assertFalse(secondMatch.dependsOn(firstMatch));

        assertTrue(thirdMatch.dependsOn(firstMatch));
        assertTrue(thirdMatch.dependsOn(secondMatch));
    }

    @Test
    public void getTreeAsListBFS01() {
        Match firstMatch = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Match secondMatch = new Match(new Team("C", null, 0, "c"), new Team("D", null, 0, "d"));
        Match thirdMatch = new Match(new Team("E", null, 0, "e"), new Team("F", null, 0, "f"));
        Match fourthMatch = new Match(new Team("G", null, 0, "g"), new Team("H", null, 0, "h"));
        Match firstSemiFinal = new Match().setBlueToWinnerOf(firstMatch).setOrangeToWinnerOf(secondMatch);
        Match secondSemiFinal = new Match().setBlueToWinnerOf(thirdMatch).setOrangeToWinnerOf(fourthMatch);
        Match finalMatch = new Match().setBlueToWinnerOf(firstSemiFinal).setOrangeToWinnerOf(secondSemiFinal);

        List<Match> bfs = finalMatch.getTreeAsListBFS();

        assertSame(finalMatch, bfs.get(0));
        assertSame(secondSemiFinal, bfs.get(1));
        assertSame(firstSemiFinal, bfs.get(2));
        assertSame(fourthMatch, bfs.get(3));
        assertSame(thirdMatch, bfs.get(4));
        assertSame(secondMatch, bfs.get(5));
        assertSame(firstMatch, bfs.get(6));
    }

    @Test
    public void getTreeAsListBFS02() {
        Match firstMatch = new Match(new Team("A", null, 0, "a"), new Team("B", null, 0, "b"));
        Match secondMatch = new Match().setBlueToWinnerOf(firstMatch).setOrange(new Team("C", null, 0, "c"));
        Match thirdMatch = new Match().setBlueToWinnerOf(secondMatch).setOrange(new Team("D", null, 0, "d"));

        List<Match> bfs = thirdMatch.getTreeAsListBFS();

        assertSame(thirdMatch, bfs.get(0));
        assertSame(secondMatch, bfs.get(1));
        assertSame(firstMatch, bfs.get(2));
        assertEquals(3, bfs.size());
    }

    @Test
    public void reconnect01() {
        Team expectedWinner = new Team("A", null, 0, "a");
        Match matchOne = new Match(expectedWinner, new Team("B", null, 0, "b"));
        Match matchTwo = new Match().setBlue(new Team("C", null, 0, "c")).setOrangeToWinnerOf(matchOne);
        matchOne.setScores(4, 2, true);

        assertSame(expectedWinner, matchTwo.getOrangeTeam());

        // change match one winner now goes to third match instead
        Match matchThree = new Match().setBlue(new Team("D", null, 0, "d")).setOrangeToWinnerOf(matchOne);

        assertFalse(matchTwo.dependsOn(matchOne));
        assertNull(matchTwo.getOrangeTeam());
        assertSame(expectedWinner, matchThree.getOrangeTeam());
    }

    @Test
    public void reconnect02() {
        Team expectedWinner = new Team("A", null, 0, "a");
        Match matchOne = new Match(expectedWinner, new Team("B", null, 0, "b"));
        Match matchTwo = new Match().setBlue(new Team("C", null, 0, "c")).setOrangeToWinnerOf(matchOne);
        matchOne.setScores(4, 2, true);

        assertSame(expectedWinner, matchTwo.getOrangeTeam());

        // change match two's orange to be winner of match three
        Match matchThree = new Match(new Team("D", null, 0, "d"), new Team("E", null, 0, "e"));
        matchTwo.setOrangeToWinnerOf(matchThree);

        assertFalse(matchTwo.dependsOn(matchOne));
        assertTrue(matchTwo.dependsOn(matchThree));
        assertNull(matchTwo.getOrangeTeam());
    }

    @Test
    public void reconnect03() {
        Team expectedLoser = new Team("A", null, 0, "a");
        Match matchOne = new Match(expectedLoser, new Team("B", null, 0, "b"));
        Match matchTwo = new Match().setBlue(new Team("C", null, 0, "c")).setOrangeToLoserOf(matchOne);
        matchOne.setScores(0, 3, true);

        assertSame(expectedLoser, matchTwo.getOrangeTeam());

        // change match one loser now goes to third match instead
        Match matchThree = new Match().setBlue(new Team("D", null, 0, "d")).setOrangeToLoserOf(matchOne);

        assertFalse(matchTwo.dependsOn(matchOne));
        assertNull(matchTwo.getOrangeTeam());
        assertSame(expectedLoser, matchThree.getOrangeTeam());
    }

    @Test
    public void reconnect04() {
        Team expectedLoser = new Team("A", null, 0, "a");
        Match matchOne = new Match(expectedLoser, new Team("B", null, 0, "b"));
        Match matchTwo = new Match().setBlue(new Team("C", null, 0, "c")).setOrangeToLoserOf(matchOne);
        matchOne.setScores(0, 3, true);

        assertSame(expectedLoser, matchTwo.getOrangeTeam());

        // change match two's orange to be loser of match three
        Match matchThree = new Match(new Team("D", null, 0, "d"), new Team("E", null, 0, "e"));
        matchTwo.setOrangeToLoserOf(matchThree);

        assertFalse(matchTwo.dependsOn(matchOne));
        assertTrue(matchTwo.dependsOn(matchThree));
        assertNull(matchTwo.getOrangeTeam());
    }

    @Test
    public void setScores01(){

        Team teamBlue = new Team(null, null, 1, null);
        Team teamOrange = new Team(null, null, 1, null);
        Match match = new Match(teamBlue, teamOrange);

        int blueScore = 5;
        int orangeScore = 2;
        match.setScores(blueScore, orangeScore, true);

        assertEquals(blueScore, teamBlue.getGoalsScored());
        assertEquals(orangeScore, teamBlue.getGoalsConceded());
        assertEquals(orangeScore, teamOrange.getGoalsScored());
        assertEquals(blueScore, teamOrange.getGoalsConceded());
    }

    @Test
    public void setScores02(){

        Team teamBlue = new Team(null, null, 1, null);
        Team teamOrange = new Team(null, null, 1, null);
        Match match = new Match(teamBlue, teamOrange);

        int blueScore1 = 5;
        int orangeScore1 = 2;
        int blueScore2 = 2;
        int orangeScore2 = 2;
        match.setScores(blueScore1, orangeScore1, true);
        match.setScores(blueScore2, orangeScore2, true);

        assertEquals(blueScore2, teamBlue.getGoalsScored());
        assertEquals(orangeScore2, teamOrange.getGoalsScored());
        assertEquals(orangeScore2, teamBlue.getGoalsConceded());
        assertEquals(blueScore2, teamOrange.getGoalsConceded());
    }

    @Test
    public void setScores03(){ //Multiple matches

        Team teamBlue = new Team(null, null, 1, null);
        Team teamOrange = new Team(null, null, 1, null);
        Match match1 = new Match(teamBlue, teamOrange);
        Match match2 = new Match(teamBlue, teamOrange);

        int blueScore = 5;
        int orangeScore = 2;
        match1.setScores(blueScore, orangeScore, true);
        match2.setScores(blueScore, orangeScore, true);

        assertEquals(blueScore*2, teamBlue.getGoalsScored());
        assertEquals(orangeScore*2, teamBlue.getGoalsConceded());
        assertEquals(orangeScore*2, teamOrange.getGoalsScored());
        assertEquals(blueScore*2, teamOrange.getGoalsConceded());
    }

    @Test
    public void reconnectAfterPlay01() {
        Team tA = new Team("A", null, 0, "a");
        Match matchOne = new Match(tA, new Team("B", null, 0, "b"));
        Match matchTwo = new Match().setBlue(new Team("C", null, 0, "c")).setOrangeToWinnerOf(matchOne);
        Match matchThree = new Match(new Team("D", null, 0, "d"), new Team("E", null, 0, "e"));

        matchOne.setScores(0, 0, true);

        matchTwo.setOrangeToWinnerOf(matchThree);

        System.out.println(matchTwo.getOrangeTeam());
    }
}