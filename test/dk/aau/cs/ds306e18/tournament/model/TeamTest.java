package dk.aau.cs.ds306e18.tournament.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TeamTest {

    @Test
    public void addGoalsScored01(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsScored = 5;
        team.addGoalsScored(goalsScored);

        assertEquals(goalsScored, team.getGoalsScored());
    }

    @Test
    public void addGoalsScored02(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsScored = -5;
        team.addGoalsScored(goalsScored);

        assertEquals(goalsScored, team.getGoalsScored());
    }

    @Test
    public void addGoalsScored03(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsScored = 0;
        team.addGoalsScored(goalsScored);

        assertEquals(goalsScored, team.getGoalsScored());
    }

    @Test
    public void addGoalsConceded01(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsConceded = 5;
        team.addGoalsConceded(goalsConceded);

        assertEquals(goalsConceded, team.getGoalsConceded());
    }

    @Test
    public void addGoalsConceded02(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsConceded = -5;
        team.addGoalsConceded(goalsConceded);

        assertEquals(goalsConceded, team.getGoalsConceded());
    }

    @Test
    public void addGoalsConceded03(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsConceded = 0;
        team.addGoalsConceded(goalsConceded);

        assertEquals(goalsConceded, team.getGoalsConceded());
    }

    @Test
    public void addGoalsScoredAndConceded01(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsScored = 5;
        int goalsConceded = 5;
        team.addGoalsScoredAndConceded(goalsScored, goalsConceded);

        assertEquals(goalsScored, team.getGoalsScored());
        assertEquals(goalsConceded, team.getGoalsConceded());
    }

    @Test
    public void subtractGoalsScored01(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsScored = 5;
        team.addGoalsScored(-goalsScored);

        assertEquals(-goalsScored, team.getGoalsScored());
    }

    @Test
    public void subtractGoalsScored02(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsScored = 0;
        team.addGoalsScored(-goalsScored);

        assertEquals(-goalsScored, team.getGoalsScored());
    }

    @Test
    public void subtractGoalsScored03(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsScored = -5;
        team.addGoalsScored(-goalsScored);

        assertEquals(-1*goalsScored, team.getGoalsScored());
    }

    @Test
    public void subtractGoalsConceded01(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsConceded = 5;
        team.addGoalsConceded(-goalsConceded);

        assertEquals(-goalsConceded, team.getGoalsConceded());
    }

    @Test
    public void subtractGoalsConceded02(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsConceded = 0;
        team.addGoalsConceded(-goalsConceded);

        assertEquals(-goalsConceded, team.getGoalsConceded());
    }

    @Test
    public void subtractGoalsConceded03(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsConceded = -5;
        team.addGoalsConceded(-goalsConceded);

        assertEquals(-1*goalsConceded, team.getGoalsConceded());
    }

    @Test
    public void subtractGoalsScoredAndConceded01(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsScored = 5;
        int goalsConceded = 5;
        team.addGoalsScoredAndConceded(-goalsScored, -goalsConceded);

        assertEquals(-goalsScored, team.getGoalsScored());
        assertEquals(-goalsConceded, team.getGoalsConceded());
    }

    @Test
    public void getGoalDiff01(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsScored = 5;
        int goalsConceded = 5;

        team.addGoalsScoredAndConceded(goalsScored, goalsConceded);

        assertEquals(goalsScored - goalsConceded, team.getGoalDiff());
    }

    @Test
    public void getGoalDiff02(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsScored = 0;
        int goalsConceded = 5;

        team.addGoalsScoredAndConceded(goalsScored, goalsConceded);

        assertEquals(goalsScored - goalsConceded, team.getGoalDiff());
    }

    @Test
    public void getGoalDiff03(){
        Team team = new Team("Team Pleb", null, 0, null);

        int goalsScored = 1000;
        int goalsConceded = 5;

        team.addGoalsScoredAndConceded(goalsScored, goalsConceded);

        assertEquals(goalsScored - goalsConceded, team.getGoalDiff());
    }
}
