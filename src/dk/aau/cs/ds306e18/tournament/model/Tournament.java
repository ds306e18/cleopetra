package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreakerBySeed;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tournament {

    private static Tournament instance;
    public static Tournament get() {
        if (instance == null)
            instance = new Tournament();
        return instance;
    }

    private String name = "Unnamed Tournament";
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Stage> stages = new ArrayList<>();
    private TieBreaker tieBreaker = new TieBreakerBySeed();
    private boolean started = false;
    private int currentStageIndex = -1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTeam(Team team) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        teams.add(team);
        sortTeamsAfterInitialSeed();
    }

    public void removeTeam(Team team) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        teams.remove(team);
    }

    public void removeTeam(int index) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        teams.remove(index);
    }

    public List<Team> getTeams() {
        return new ArrayList<>(teams);
    }

    public void sortTeamsAfterInitialSeed() {
        if (started) throw new IllegalStateException("Tournament has already started.");
        // Sort teams by comparator and not data structure, since seed value is not final
        teams.sort(Comparator.comparingInt(Team::getInitialSeedValue));
    }

    public void addStage(Stage stage) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        stages.add(stage);
    }

    public void removeStage(Stage stage) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        stages.remove(stage);
    }

    public void removeStage(int index) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        stages.remove(index);
    }

    public List<Stage> getStages() {
        return new ArrayList<>(stages);
    }

    /** Returns the number of stages that has not started yet. */
    public int getUpcomingStagesCount() {
        return stages.size() - currentStageIndex - 1;
    }

    /** Returns true there stages that has not started yet. */
    public boolean hasUpcomingStages() {
        return getUpcomingStagesCount() > 0;
    }

    public Stage getCurrentStage() {
        if (stages.isEmpty() || currentStageIndex == -1) return null;
        return stages.get(currentStageIndex);
    }

    public void startNextStage() {
        if (!started)
            throw new IllegalStateException("The first stage should be started through the start() method.");

        if (!hasUpcomingStages())
            throw new IllegalStateException("There are no more pending stages.");

        if (currentStageIndex != -1 && getCurrentStage().getFormat().getStatus() != StageStatus.CONCLUDED)
            throw new IllegalStateException("Previous stage has not been concluded.");

        // State is ok

        // Find best teams
        List<Team> bestTeams;
        if (currentStageIndex == -1) {
            sortTeamsAfterInitialSeed();
            bestTeams = new ArrayList<>(teams);
        } else {
            int wantedTeamCount = stages.get(currentStageIndex + 1).getNumberOfTeamsWanted();
            bestTeams = getCurrentStage().getFormat().getTopTeams(wantedTeamCount, tieBreaker);
        }

        // Proceed to next stage
        currentStageIndex++;
        getCurrentStage().getFormat().start(bestTeams);
    }

    public boolean hasStarted() {
        return started;
    }

    public boolean canStart() {
        return teams.size() < 2 && !stages.isEmpty();
    }

    public void start() {
        if (started) throw new IllegalStateException("Tournament has already started.");
        if (teams.size() < 2) throw new IllegalStateException("There must be at least two teams in the tournament.");
        if (stages.isEmpty()) throw new IllegalStateException("There must be at least one stage in the tournament.");
        started = true;
        startNextStage();
    }

    public TieBreaker getTieBreaker() {
        return tieBreaker;
    }

    public void setTieBreaker(TieBreaker tieBreaker) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        this.tieBreaker = tieBreaker;
    }
}
