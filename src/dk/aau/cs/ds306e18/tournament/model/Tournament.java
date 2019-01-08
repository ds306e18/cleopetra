package dk.aau.cs.ds306e18.tournament.model;

import com.google.gson.annotations.JsonAdapter;
import dk.aau.cs.ds306e18.tournament.model.format.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreakerByGoalDiff;
import dk.aau.cs.ds306e18.tournament.rlbot.RLBotSettings;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreaker;
import dk.aau.cs.ds306e18.tournament.serialization.TrueTeamListAdapter;

import java.util.*;

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
    @JsonAdapter(TrueTeamListAdapter.class) // Teams in this list are serialized as actual teams, other instances of teams will be their index in this list
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Stage> stages = new ArrayList<>();
    private TieBreaker tieBreaker = new TieBreakerByGoalDiff();
    private SeedingOption seedingOption = SeedingOption.SEED_BY_ORDER;
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
    }

    public void removeTeam(Team team) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        teams.remove(team);
    }

    public void removeTeam(int index) {
        if (started) throw new IllegalStateException("Tournament has already started.");
        teams.remove(index);
    }

    public void swapTeams(Team a, Team b) {
        swapTeams(teams.indexOf(a), teams.indexOf(b));
    }

    public void swapTeams(int a, int b) {
        Collections.swap(teams, a, b);
    }

    public void sortTeamsBySeed() {
        teams.sort(Comparator.comparingInt(Team::getInitialSeedValue));
    }

    public List<Team> getTeams() {
        return new ArrayList<>(teams);
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

        // Find teams to transfer
        List<Team> transferedTeams;
        boolean seeding = seedingOption != SeedingOption.NO_SEEDING;
        if (currentStageIndex == -1) {

            // This is the first stage, so all teams are transferred
            transferedTeams = new ArrayList<>(teams);

            if (seedingOption == SeedingOption.SEED_BY_ORDER) {
                // Assign seeds based on order
                for (int i = 0; i < teams.size(); i++) {
                    teams.get(i).setInitialSeedValue(i + 1);
                }
                transferedTeams.sort((a, b) -> Integer.compare(b.getInitialSeedValue(), a.getInitialSeedValue()));

            } else if (seedingOption == SeedingOption.MANUALLY) {
                // Seeds was assigned ny user. If some teams have the same seed value, shuffle those
                final Random random = new Random();
                transferedTeams.sort((a, b) -> {
                    if (a == b) return 0;
                    int comparison = Integer.compare(b.getInitialSeedValue(), a.getInitialSeedValue());
                    if (comparison == 0) {
                        return random.nextBoolean() ? 1 : -1;
                    }
                    return comparison;
                });

            } else if (seedingOption == SeedingOption.RANDOM_SEEDING) {
                // Random seeding. We give everyone a seed value of 0 and shuffle the order
                for (Team team : teams) {
                    team.setInitialSeedValue(0);
                }
                Collections.shuffle(transferedTeams);

            } else if (seedingOption == SeedingOption.NO_SEEDING) {
                // Give everyone a seed value of zero, but no change to the order
                for (Team team : teams) {
                    team.setInitialSeedValue(0);
                }
            }

        } else {

            // Not the first stage, so we find the best teams of previous stage and use their order as seeding regardless of seeding option
            int wantedTeamCount = stages.get(currentStageIndex + 1).getNumberOfTeamsWanted();
            transferedTeams = getCurrentStage().getFormat().getTopTeams(wantedTeamCount, tieBreaker);
            seeding = true;
        }

        // Proceed to next stage
        currentStageIndex++;
        getCurrentStage().getFormat().start(transferedTeams, seeding);
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

    public SeedingOption getSeedingOption() {
        return seedingOption;
    }

    public void setSeedingOption(SeedingOption seedingOption) {
        this.seedingOption = seedingOption;
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
