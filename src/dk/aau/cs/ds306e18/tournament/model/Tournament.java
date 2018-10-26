package dk.aau.cs.ds306e18.tournament.model;

import java.util.ArrayList;
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

    private String name = "Unnamed Tournament";
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<PendingStage> pendingStages = new ArrayList<>();
    private ArrayList<Stage> startedStages = new ArrayList<>();
    private TieBreaker tieBreaker = new TieBreakerBySeed();
    private boolean started = false;

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

    public void addStage(PendingStage stage) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        pendingStages.add(stage);
    }

    public void removeStage(PendingStage stage) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        pendingStages.remove(stage);
    }

    public void removeStage(int index) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        pendingStages.remove(index);
    }

    public List<PendingStage> getPendingStages() {
        return new ArrayList<>(pendingStages);
    }

    public boolean hasMorePendingStages() {
        return pendingStages.size() == 0;
    }

    public Stage getCurrentStage() {
        if (startedStages.isEmpty()) return null;
        return startedStages.get(startedStages.size() - 1);
    }

    public void startNextStage() {
        if (pendingStages.isEmpty())
            throw new IllegalStateException("There are no more pending stages.");

        if (!startedStages.isEmpty() && getCurrentStage().getStatus() != StageStatus.CONCLUDED)
            throw new IllegalStateException("Previous stage has not been concluded.");

        // State is ok

        // Find best teams
        List<Team> bestTeams;
        if (startedStages.isEmpty()) {
            sortTeamsAfterInitialSeed();
            bestTeams = new ArrayList<>(teams);
        } else {
            int wantedTeamCount = pendingStages.get(0).getNumberOfTeamsWanted();
            bestTeams = getCurrentStage().getTopTeams(wantedTeamCount, tieBreaker);
        }

        // Proceed to next stage
        Stage newStage = pendingStages.get(0).start(bestTeams);
        pendingStages.remove(0);
        startedStages.add(newStage);
    }

    public boolean hasStarted() {
        return started;
    }

    public boolean canStart() {
        return teams.size() < 2 && !pendingStages.isEmpty();
    }

    public void start() {
        if (started) throw new IllegalStateException("Tournament has already started.");
        if (teams.size() < 2) throw new IllegalStateException("There must be at least two teams in the tournament.");
        if (pendingStages.isEmpty()) throw new IllegalStateException("There must be at least one stage in the tournament.");
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
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getTeams(), that.getTeams()) &&
                Objects.equals(getPendingStages(), that.getPendingStages()) &&
                Objects.equals(startedStages, that.startedStages) &&
                Objects.equals(getTieBreaker(), that.getTieBreaker());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getTeams(), getPendingStages(), startedStages, getTieBreaker(), started);
    }
}
