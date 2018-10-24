package dk.aau.cs.ds306e18.tournament.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tournament {

    private String name = "Unnamed Tournament";
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<PendingStage> pendingStages = new ArrayList<>();
    private ArrayList<Stage> startedStages = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTeam(Team team) {
        teams.add(team);
        sortTeamsAfterInitialSeed();
    }

    public void removeTeam(Team team) {
        teams.remove(team);
    }

    public void removeTeam(int index) {
        teams.remove(index);
    }

    public List<Team> getTeams() {
        return new ArrayList<>(teams);
    }

    public void sortTeamsAfterInitialSeed() {
        // Sort teams by comparator and not data structure, since seed value is not final
        teams.sort(Comparator.comparingInt(Team::getInitialSeedValue));
    }

    public void addStage(PendingStage stage) {
        pendingStages.add(stage);
    }

    public void removeStage(PendingStage stage) {
        pendingStages.remove(stage);
    }

    public void removeStage(int index) {
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
            bestTeams = getCurrentStage().getTopTeams(wantedTeamCount);
        }

        // Proceed to next stage
        Stage newStage = pendingStages.get(0).start(bestTeams);
        pendingStages.remove(0);
        startedStages.add(newStage);
    }
}
