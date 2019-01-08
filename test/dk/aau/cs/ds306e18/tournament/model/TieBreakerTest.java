package dk.aau.cs.ds306e18.tournament.model;
import dk.aau.cs.ds306e18.tournament.model.format.SingleEliminationFormat;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TieBreakerTest {

    //
    @Test
    public void goalDiff01() {
        SingleEliminationFormat bracket = new SingleEliminationFormat();
        ArrayList<Team> teamList = new ArrayList();
        ArrayList<Bot> botList = new ArrayList();
        for (int i = 0; i < 8; i++) {
            botList.add(new Bot("bot" + Integer.toString(i), "botty" + Integer.toString(i)));
            teamList.add(new Team("Team" + Integer.toString(i), botList, i+1, "team" + Integer.toString(i)));
            botList.clear();
        }
        bracket.start(teamList, true);
        bracket.getAllMatches().get(6).setScores(2, 0, true);
        bracket.getAllMatches().get(5).setScores(5, 0, true);
        bracket.getAllMatches().get(4).setScores(3, 2, true);
        bracket.getAllMatches().get(3).setScores(4, 2, true);
        bracket.getAllMatches().get(2).setScores(1, 0, true);
        bracket.getAllMatches().get(1).setScores(2, 4, true);
        bracket.getAllMatches().get(0).setScores(2, 0, true);
        List topTeams = bracket.getTopTeams(8, TieBreaker.GOAL_DIFF);
        assertTrue(teamList.get(3).getGoalDiff() >= teamList.get(1).getGoalDiff());
        assertTrue(teamList.get(6).getGoalDiff() >= teamList.get(5).getGoalDiff() && teamList.get(7).getGoalDiff() >= teamList.get(4).getGoalDiff());
    }
}