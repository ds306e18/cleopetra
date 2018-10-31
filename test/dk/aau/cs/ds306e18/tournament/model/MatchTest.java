package dk.aau.cs.ds306e18.tournament.model;

import org.junit.Test;

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
}