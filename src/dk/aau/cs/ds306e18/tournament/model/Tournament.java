package dk.aau.cs.ds306e18.tournament.model;

import com.google.gson.annotations.JsonAdapter;
import dk.aau.cs.ds306e18.tournament.model.format.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreakerByGoalDiff;
import dk.aau.cs.ds306e18.tournament.rlbot.RLBotSettings;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Tournament {

    private static Tournament instance;
    public static Tournament get() {
        if (instance == null)
            instance = new Tournament();
        return instance;
    }

    public static final int START_REQUIREMENT_TEAMS = 2;
    public static final int START_REQUIREMENT_STAGES = 1;

    private RLBotSettings rlBotSettings = new RLBotSettings();
    private String name = "Unnamed Tournament";
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Stage> stages = new ArrayList<>();
    private TieBreaker tieBreaker = new TieBreakerByGoalDiff();
    private boolean started = false;
    private int currentStageIndex = -1;

    public Tournament() {
        instance = this;
    }

    public RLBotSettings getRlBotSettings() {
        return rlBotSettings;
    }

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
        //TODO if (started) throw new IllegalStateException("Tournament has already started.");
        // Sort teams by comparator and not data structure, since seed value is not final
        teams.sort(Comparator.comparingInt(Team::getInitialSeedValue));
    }

    public void addStage(Stage stage) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        stages.add(stage);
        stage.setStageNumber(stages.size());
    }

    public void removeStage(Stage stage) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        stages.remove(stage);
        recalculateStageNumbers();
    }

    public void removeStage(int index) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        stages.remove(index);
        recalculateStageNumbers();
    }

    private void recalculateStageNumbers() {
        for (int i = 0; i < stages.size(); i++) {
            stages.get(i).setStageNumber(i + 1);
        }
    }

    public List<Stage> getStages() {
        return new ArrayList<>(stages);
    }

    public void swapStages(Stage primaryStage, Stage secondaryStage) {
        Collections.swap(stages, stages.indexOf(primaryStage), stages.indexOf(secondaryStage));
        recalculateStageNumbers();
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

    public int getCurrentStageIndex() {
        return currentStageIndex;
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
        return teams.size() >= START_REQUIREMENT_TEAMS && stages.size() >= START_REQUIREMENT_STAGES;
    }

    public void start() {
        if (started) throw new IllegalStateException("Tournament has already started.");
        if (teams.size() < START_REQUIREMENT_TEAMS) throw new IllegalStateException("There must be at least two teams in the tournament.");
        if (stages.size() < START_REQUIREMENT_STAGES) throw new IllegalStateException("There must be at least one stage in the tournament.");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return started == that.started &&
                currentStageIndex == that.currentStageIndex &&
                Objects.equals(getRlBotSettings(), that.getRlBotSettings()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getTeams(), that.getTeams()) &&
                Objects.equals(getStages(), that.getStages()) &&
                Objects.equals(getTieBreaker(), that.getTieBreaker());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRlBotSettings(), getName(), getTeams(), getStages(), getTieBreaker(), started, currentStageIndex);
    }

    public void setTournament (Tournament newTournament) {
        instance = newTournament;
    }
}
