package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchChangeListener;
import dk.aau.cs.ds306e18.tournament.model.match.MatchPlayedListener;
import dk.aau.cs.ds306e18.tournament.ui.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.SwissNode;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.SwissSettingsNode;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import javafx.scene.Node;

import java.util.*;
import java.util.stream.Collectors;

public class SwissFormat implements Format, MatchChangeListener, MatchPlayedListener {

    private StageStatus status = StageStatus.PENDING;
    private ArrayList<Team> teams;
    private ArrayList<ArrayList<Match>> rounds = new ArrayList<>();
    private int maxRoundsPossible;
    private int roundCount = 4;
    private boolean doSeeding;
    transient private IdentityHashMap<Team, Integer> teamPoints;

    transient private List<StageStatusChangeListener> statusChangeListeners = new LinkedList<>();

    @Override
    public void start(List<Team> teams, boolean doSeeding) {
        rounds = new ArrayList<>();
        maxRoundsPossible = getMaxRoundsPossible(teams.size());
        roundCount = maxRoundsPossible < roundCount ? maxRoundsPossible : roundCount;
        teamPoints = createPointsMap(teams);
        this.teams = new ArrayList<>(teams);
        this.doSeeding = doSeeding;
        status = StageStatus.RUNNING;

        startNextRound(); // Generate the first round
    }

    @Override
    public StageStatus getStatus() {
        return status;
    }

    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {
        return tieBreaker.compareWithPoints(teams, getTeamPointsMap(), this).subList(0, count);
    }

    /**
     * Creates the hashMap that stores the "swiss" points for the teams.
     */
    private IdentityHashMap<Team, Integer> createPointsMap(List<Team> teams) {
        IdentityHashMap<Team, Integer> pointsMap = new IdentityHashMap<>();
        for (Team team : teams) pointsMap.put(team, 0);
        return pointsMap;
    }

    /**
     * Creates a new round of swiss, with the current teams.
     *
     * @return true if a round was generated and false if a new round could not be generated.
     */
    public boolean startNextRound() {
        if (!canStartNextRound())
            return false;
        createNextRound();
        return true;
    }

    /**
     * @return true if it is allowed to generate a new round.
     */
    public boolean canStartNextRound() {
        if (status == StageStatus.PENDING) {
            return false;
        } else if (status == StageStatus.CONCLUDED) {
            return false;
        } else if (!hasUnstartedRounds()) { //Is it legal to create another round?
            return false;
        } else if (getUpcomingMatches().size() != 0) { //Has all matches been played?
            return false;
        }
        return true;
    }

    /**
     * Takes a list of teams and sorts it based on their points in the given hashMap.
     *
     * @param teamList   a list of teams.
     * @param teamPoints a hashMap containing teams as key and their points as value.
     * @return the given list sorted based on the given points.
     */
    private List<Team> getOrderedTeamsListFromPoints(ArrayList<Team> teamList, Map<Team, Integer> teamPoints) {
        if (rounds.size() == 0) return new ArrayList<>(teamList);
        else return Tournament.get().getTieBreaker().compareWithPoints(teamList, teamPoints, this);
    }

    /**
     * Creates the matches for the next round. This is done so that no team will play the same opponents twice,
     * and with the teams with the closest amount of points.
     * Credit for algorithm: Amanda.
     */
    private void createNextRound() {

        List<Team> orderedTeamList;
        // Use seeding for first round.
        if (doSeeding && rounds.size() == 0) {
            // Best seed should play against worst seed,
            // second best against second worst, etc.
            // Create seeded list fitting the algorithm below
            orderedTeamList = Seeding.simplePairwiseSeedList(teams);
        } else {
            // Create ordered list of team, based on points.
            orderedTeamList = getOrderedTeamsListFromPoints(teams, teamPoints);
        }

        ArrayList<Match> createdMatches;
        int badTries = 0;

        while (true) {
            createdMatches = new ArrayList<>();
            List<Team> remaingTeams = new ArrayList<>(orderedTeamList);
            int acceptedRematches = badTries;

            // Create matches while there is more than 1 team in the list.
            while (remaingTeams.size() > 1) {

                Team team1 = remaingTeams.get(0);
                Team team2 = null;

                // Find the next team that has not played team1 yet.
                for (int i = 1; i < remaingTeams.size(); i++) {

                    team2 = remaingTeams.get(i);

                    // Has the two selected teams played each other before?
                    // Due to previous bad tries we might accept rematches. Note: short circuiting
                    if (!hasTheseTeamsPlayedBefore(team1, team2) || 0 < acceptedRematches--) {
                        Match match = new Match(team1, team2);
                        createdMatches.add(match);
                        break; // Two valid teams has been found, and match has been created.
                    }
                }

                remaingTeams.remove(team1);
                remaingTeams.remove(team2);
            }

            if (createdMatches.size() == orderedTeamList.size() / 2) {
                break; // Round was fully constructed
            } else {
                badTries++;
            }
        }

        // Alert users about rematches. TODO Fix the algorithm. Replace it with Blossom algorithm, good luck!
        if (badTries > 0) {
            Alerts.errorNotification("Bad round", "Accepted " + badTries + " rematches due to a bad algorithm.");
        }

        // Register self as listener
        for (Match m : createdMatches) {
            m.registerMatchPlayedListener(this);
            m.registerMatchChangeListener(this);
        }

        // Track stats from the new matches
        for (Team team : teams) {
            team.getStatsManager().trackMatches(this, createdMatches);
        }

        rounds.add(createdMatches);
    }

    /**
     * Checks if the two given teams has played a match against each other.
     * Returns a boolean based on that comparison.
     *
     * @param team1 one of the two teams for the check.
     * @param team2 one of the two teams for the check.
     * @return true if the two given teams has played before, and false if that is not the case.
     */
    private boolean hasTheseTeamsPlayedBefore(Team team1, Team team2) {

        List<Match> matches = getCompletedMatches();

        for (Match match : matches) {

            Team teamOne = match.getTeamOne();
            Team teamTwo = match.getTeamTwo();

            //Compare the current match's teams with the two given teams.
            if (team1 == teamOne && team2 == teamTwo || team1 == teamTwo && team2 == teamOne)
                return true;
        }

        return false;
    }

    @Override
    public List<Match> getAllMatches() {

        ArrayList<Match> matches = new ArrayList<>();

        for (ArrayList<Match> list : rounds) {
            matches.addAll(list);
        }

        return matches;
    }

    @Override
    public List<Match> getUpcomingMatches() {
        return getAllMatches().stream().filter(match -> !match.hasBeenPlayed()).collect(Collectors.toList());
    }

    @Override
    public List<Match> getPendingMatches() {
        return getAllMatches().stream().filter(match -> match.getStatus() == Match.Status.NOT_PLAYABLE).collect(Collectors.toList());
    }

    @Override
    public List<Match> getCompletedMatches() {
        return getAllMatches().stream().filter(Match::hasBeenPlayed).collect(Collectors.toList());
    }

    public int getMaxRoundsPossible() {
        return maxRoundsPossible;
    }

    /**
     * Returns the max number of rounds that can be played with the given number of teams.
     */
    public static int getMaxRoundsPossible(int numberOfTeams) {
        if (numberOfTeams == 0)
            return 0;
        return (numberOfTeams % 2 == 0) ? numberOfTeams - 1 : numberOfTeams;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        if (status != StageStatus.PENDING)
            throw new IllegalStateException("Swiss stage has already started.");
        if (roundCount < 1)
            throw new IllegalArgumentException("There must be at least one round.");
        this.roundCount = roundCount;
    }

    public boolean hasUnstartedRounds() {
        return rounds.size() < roundCount;
    }

    /**
     * @Return the latest generated round. Or null if there are no rounds generated.
     */
    public List<Match> getLatestRound() {
        if (rounds.size() == 0) return null;
        return new ArrayList<>(rounds.get(rounds.size() - 1));
    }

    /**
     * @return a hashMap containing the teams and their points.
     */
    public IdentityHashMap<Team, Integer> getTeamPointsMap() {
        return teamPoints;
    }

    @Override
    public SwissNode getBracketFXNode(BracketOverviewTabController boc) {
        return new SwissNode(this, boc);
    }

    @Override
    public Node getSettingsFXNode() {
        return new SwissSettingsNode(this);
    }

    /**
     * @return an arraylist of the current created rounds.
     */
    public ArrayList<ArrayList<Match>> getRounds() {
        ArrayList<ArrayList<Match>> roundsCopy = new ArrayList<>(rounds.size());
        for (ArrayList<Match> r : rounds) {
            roundsCopy.add(new ArrayList<>(r));
        }
        return roundsCopy;
    }

    /**
     * Checks a given team for every completed match if they have been a loser or winner. Then assigning their points
     * based upon their win/loss ratio. A win gives 1 point.
     * By going through all completed matches we can assure proper point giving due to recalculations.
     *
     * @param team The given team to check, calculate and then assign point for.
     */
    private void calculateAndAssignTeamPoints(Team team) {
        int points = 0;

        for (Match match : getCompletedMatches()) {
            if (match.getWinner().equals(team)) {
                points += 1;
            }
        }

        teamPoints.put(team, points);
    }

    public List<Team> getTeams() {
        return teams;
    }

    @Override
    public void onMatchPlayed(Match match) {
        // Has last possible match been played?
        StageStatus oldStatus = status;
        if (!hasUnstartedRounds() && getUpcomingMatches().size() == 0) {
            status = StageStatus.CONCLUDED;
        } else {
            status = StageStatus.RUNNING;
        }

        // Notify listeners if status changed
        if (oldStatus != status) {
            nofityStatusListeners(status, oldStatus);
        }
    }

    @Override
    public void onMatchChanged(Match match) {
        // Calculate and assign points for each team in the match.
        calculateAndAssignTeamPoints(match.getTeamOne());
        calculateAndAssignTeamPoints(match.getTeamTwo());
    }

    @Override
    public void registerStatusChangedListener(StageStatusChangeListener listener) {
        statusChangeListeners.add(listener);
    }

    @Override
    public void unregisterStatusChangedListener(StageStatusChangeListener listener) {
        statusChangeListeners.remove(listener);
    }

    /** Let listeners know, that the status has changed */
    private void nofityStatusListeners(StageStatus oldStatus, StageStatus newStatus) {
        for (StageStatusChangeListener listener : statusChangeListeners) {
            listener.onStageStatusChanged(this, oldStatus, newStatus);
        }
    }

    @Override
    public void postDeserializationRepair() {
        for (Match match : getAllMatches()) {
            match.registerMatchPlayedListener(this);
            match.registerMatchChangeListener(this);
        }
        teamPoints = createPointsMap(teams);
        for (Team team : teams) {
            calculateAndAssignTeamPoints(team);
            team.getStatsManager().trackMatches(this, getAllMatches());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwissFormat that = (SwissFormat) o;
        return getMaxRoundsPossible() == that.getMaxRoundsPossible() &&
                Objects.equals(getRounds(), that.getRounds()) &&
                Objects.equals(teams, that.teams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRounds(), getMaxRoundsPossible(), teamPoints);
    }
}
