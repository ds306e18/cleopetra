package dk.aau.cs.ds306e18.tournament.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MatchTest {

    @Test
    public void isReadyToPlay01() {
        Match match = new Match(new StarterSlot(new Team("A", null, 0, "a")), new StarterSlot(new Team("B", null, 0, "b")));
        assertTrue(match.isReadyToPlay());
    }

    @Test
    public void isReadyToPlay02() {
        Match firstMatch = new Match(new StarterSlot(new Team("A", null, 0, "a")), new StarterSlot(new Team("B", null, 0, "b")));
        Match secondMatch = new Match(new StarterSlot(new Team("C", null, 0, "c")), new WinnerOf(firstMatch));
        assertFalse(secondMatch.isReadyToPlay());

        firstMatch.setScores(4, 2, true);
        assertTrue(secondMatch.isReadyToPlay());
    }

    @Test
    public void getWinnerAndLoser01() {
        // team blue wins
        Team expectedWinner = new Team("A", null, 0, "a");
        Team expectedLoser = new Team("B", null, 0, "b");
        Match match = new Match(new StarterSlot(expectedWinner), new StarterSlot(expectedLoser));
        match.setScores(3, 2, true);
        assertSame(match.getWinner(), expectedWinner);
        assertSame(match.getLoser(), expectedLoser);
    }

    @Test
    public void getWinnerAndLoser02() {
        // team orange wins
        Team expectedWinner = new Team("A", null, 0, "a");
        Team expectedLoser = new Team("B", null, 0, "b");
        Match match = new Match(new StarterSlot(expectedLoser), new StarterSlot(expectedWinner));
        match.setScores(3, 5, true);
        assertSame(match.getWinner(), expectedWinner);
        assertSame(match.getLoser(), expectedLoser);
    }

    // TODO Add test getWinnerAndLoser were match has not been played

    @Test
    public void getStatus01() {
        Match firstMatch = new Match(new StarterSlot(new Team("A", null, 0, "a")), new StarterSlot(new Team("B", null, 0, "b")));
        Match secondMatch = new Match(new StarterSlot(new Team("C", null, 0, "c")), new WinnerOf(firstMatch));
        assertSame(secondMatch.getStatus(), MatchStatus.NOT_PLAYABLE);
    }

    @Test
    public void getStatus02() {
        Match firstMatch = new Match(new StarterSlot(new Team("A", null, 0, "a")), new StarterSlot(new Team("B", null, 0, "b")));
        Match secondMatch = new Match(new StarterSlot(new Team("C", null, 0, "c")), new WinnerOf(firstMatch));
        firstMatch.setScores(0, 2, true);
        assertSame(secondMatch.getStatus(), MatchStatus.READY_TO_BE_PLAYED);
    }

    @Test
    public void getStatus03() {
        Match match = new Match(new StarterSlot(new Team("A", null, 0, "a")), new StarterSlot(new Team("B", null, 0, "b")));
        match.setScores(0, 0, true);
        assertSame(match.getStatus(), MatchStatus.DRAW);
    }

    // TODO Add tests getStatus that expects BLUE_WINS and ORANGE_WINS
}