package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchPlayedListener;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.RoundRobinNode;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.RoundRobinSettingsNode;
import dk.aau.cs.ds306e18.tournament.ui.BracketOverviewTabController;
import javafx.scene.Node;

import java.util.*;
import java.util.stream.Collectors;

public class RoundRobinFormat implements Format, MatchPlayedListener {

    private static final Team DUMMY_TEAM = new Team("Dummy", new ArrayList<>(), 0, "Dummy team description");

    private StageStatus status = StageStatus.PENDING;
    private ArrayList<Team> teams;
    private ArrayList<Match> matches;
    private ArrayList<RoundRobinGroup> groups;
    private int numberOfGroups = 1;

    transient private List<StageStatusChangeListener> statusChangeListeners = new LinkedList<>();

    /** Start the round robin format.
     * @param seededTeams arraylist of all the teams in the round robin.
     * @param doSeeding whether or not to group teams based on their seed. */
    public void start(List<Team> seededTeams, boolean doSeeding) {
        teams = new ArrayList<>(seededTeams);
        ArrayList<Team> teams = new ArrayList<>(seededTeams);

        if (seededTeams.size() <= 1) {
            matches = new ArrayList<>();
            groups = new ArrayList<>();
            status = StageStatus.CONCLUDED;

        } else {

            // Clamp the number of groups to something that is okay (at least 2 teams per group)
            if (numberOfGroups < 1) numberOfGroups = 1;
            else if (numberOfGroups > teams.size() / 2) numberOfGroups = teams.size() / 2;

            status = StageStatus.RUNNING;
            createGroupsAndMatches(teams, doSeeding);
            matches = extractMatchesFromGroups();
            setupStatsTracking();
        }
    }

    @Override
    public StageStatus getStatus() {
        return status;
    }

    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {

        /*
        Sort the teams of each group by their performance. Then pick the top X teams as follows:
        1st from group 0,
        1st from group 1,
        ...
        1st from group N-1,
        2nd from group 0,
        2nd from group 1,
        ...
        Until X teams has been picked or every team was picked.

        However, the first groups might be smaller if the number of teams is not divisible by the number groups.
        Therefore we skip those when they are empty.
         */

        HashMap<Team, Integer> pointsMap = getTeamPointsMap();
        List<List<Team>> sortedGroups = groups.stream()
                .map((group) -> tieBreaker.compareWithPoints(group.getTeams(), pointsMap, this))
                .collect(Collectors.toList());

        List<Team> topTeams = new ArrayList<>();
        int left = Math.min(count, teams.size());
        int nextGroupToTakeFrom = 0;
        int indexToTake = 0;

        while (left > 0) {
            List<Team> group = sortedGroups.get(nextGroupToTakeFrom);
            // Are there more teams in this group?
            if (group.size() > indexToTake) {
                topTeams.add(group.get(indexToTake));
                left--;
            }
            nextGroupToTakeFrom++;
            if (nextGroupToTakeFrom == groups.size()) {
                // Wrap around at last group and pick next best team from the groups
                nextGroupToTakeFrom = 0;
                indexToTake++;
            }
        }

        return topTeams;
    }

    /** This function populates the groups list and generates the matches for each group. */
    private void createGroupsAndMatches(ArrayList<Team> teams, boolean doSeeding) {
        groups = new ArrayList<>();

        int groupSize = teams.size() / numberOfGroups;
        int leftoverTeams = teams.size() % numberOfGroups;

        if (doSeeding) {

            // With seeding
            // Pick teams evenly
            for (int i = 0; i < numberOfGroups; i++) {

                // Pick every numberOfGroups'th team
                ArrayList<Team> teamsForGroup = new ArrayList<>();
                for (int j = 0; j < groupSize; j++) {
                    teamsForGroup.add(teams.get(i + j * numberOfGroups));
                }

                // When team count is not divisible by group count, the leftover teams are added to the lowest seeded groups
                if (i >= numberOfGroups - leftoverTeams) {
                    teamsForGroup.add(teams.get(teams.size() - numberOfGroups + i));
                }

                RoundRobinGroup group = new RoundRobinGroup(teamsForGroup);
                group.setRounds(generateMatches(group.getTeams()));
                groups.add(group);
            }

        } else {

            // Without seeding
            // Pick clusters of teams
            int t = 0;
            for (int i = 0; i < numberOfGroups; i++) {

                // Some groups may be bigger if team count is not divisible by number of groups
                // The first group(s) will be the small ones
                int size = groupSize;
                if (i >= numberOfGroups - leftoverTeams) {
                    size++;
                }

                RoundRobinGroup group = new RoundRobinGroup(teams.subList(t, t + size));
                group.setRounds(generateMatches(group.getTeams()));
                groups.add(group);

                t += size;
            }
        }
    }

    /**
     * Creates all matches with each team changing color between each of their matches, algorithm based on berger tables.
     * @return an list of list of matches with the given teams. Each nested list is a round of matches.
     */
    private ArrayList<ArrayList<Match>> generateMatches(ArrayList<Team> t) {
        // Add dummy team if team count is odd - berger tables only work for an even number.
        // The matches with dummy teams are removed later
        ArrayList<Team> teams = new ArrayList<>(t);
        if (t.size() % 2 != 0) {
            teams.add(DUMMY_TEAM);
        }

        int nextTeamOneIndex, nextTeamTwoIndex;
        Match[][] table = new Match[teams.size() - 1][teams.size() / 2];

        HashMap<Team, Integer> map = createIdHashMap(teams);

        //number of rounds needed
        for (int round = 0; round < teams.size() - 1; round++) {
            //number of matches per round
            for (int match = 0; match < teams.size() / 2; match++) {
                //create first round, which is not based on previous rounds
                if (round == 0) {
                    table[round][match] = createNewMatch(teams.get(match), teams.get(teams.size() - (match + 1)));
                }
                //hardcoding team with highest id (numberOfTeams - 1 ) to first match each round
                //the other team is found by berger tables rules (findIdOfNextTeam) on the id of the team in the same match,
                //but previous round
                else if (match == 0) {
                    //else become team two
                    if ((round % 2) == 0) {
                        nextTeamOneIndex = findIdOfNextTeam(map.get(table[round - 1][match].getTeamTwo()), teams.size());
                        table[round][match] = createNewMatch(teams.get(nextTeamOneIndex - 1), (teams.get(teams.size() - 1)));
                        //if uneven round, team with highest id becomes team one
                    } else {
                        nextTeamTwoIndex = findIdOfNextTeam(map.get(table[round - 1][match].getTeamOne()), teams.size());
                        table[round][match] = createNewMatch((teams.get(teams.size() - 1)), teams.get(nextTeamTwoIndex - 1));
                    }
                } else {
                    //if not the first round, or first match, find both teams by findIdOfNextTeam according to berger tables,
                    //on previous teams
                    nextTeamOneIndex = findIdOfNextTeam(map.get(table[round - 1][match].getTeamOne()), teams.size());
                    nextTeamTwoIndex = findIdOfNextTeam(map.get(table[round - 1][match].getTeamTwo()), teams.size());
                    table[round][match] = createNewMatch(teams.get(nextTeamOneIndex - 1), (teams.get(nextTeamTwoIndex - 1)));
                }
            }
        }

        // Find matches that does not contain the dummy team
        ArrayList<ArrayList<Match>> matches = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            matches.add(new ArrayList<>());
            for (int j = 0; j < table[i].length; j++) {
                if (!table[i][j].getTeamTwo().equals(DUMMY_TEAM) &&
                        !table[i][j].getTeamOne().equals(DUMMY_TEAM)) {
                    matches.get(i).add(table[i][j]);
                }
            }
        }

        return matches;
    }

    /**
     * Generates a hashMap containing the given teams and an unique integer(id).
     * This will be used in the berger tables.
     *
     * @param teams a list of all teams in the bracket.
     * @return a hashMap containing the teams an a unique id.
     */
    private HashMap<Team, Integer> createIdHashMap(ArrayList<Team> teams) {
        HashMap<Team, Integer> map = new HashMap<>();
        for (int m = 0; m < teams.size(); m++) {
            map.put(teams.get(m), m + 1);
        }
        return map;
    }

    /**
     * @return a new match from the given parameters. And adds listener.
     */
    private Match createNewMatch(Team team1, Team team2) {

        Match match = new Match(team1, team2);
        match.registerMatchPlayedListener(this);
        return match;
    }

    /**
     * Find new team, by adding n/2 to the team in the same place in previous round, if this exceeds n-1,
     * instead subtract n/2 - 1.
     *
     * @param id of the team in the match in the previous round.
     * @return the id of the team that should be in this match, according to last.
     */
    public int findIdOfNextTeam(int id, int limit) {
        if ((id + (limit / 2)) > (limit - 1)) {
            return id - ((limit / 2) - 1);
        } else return id + (limit / 2);
    }

    /**
     * @return a list of all matches contained in the Round Robin Groups
     */
    private ArrayList<Match> extractMatchesFromGroups() {
        ArrayList<Match> listOfAllMatches = new ArrayList<>();
        for (RoundRobinGroup group : groups) {
            listOfAllMatches.addAll(group.getMatches());
        }
        return listOfAllMatches;
    }

    private void setupStatsTracking() {
        List<Match> allMatches = getAllMatches();
        for (Team team : teams) {
            team.getStatsManager().trackMatches(this, allMatches);
        }
    }

    @Override
    public List<Match> getAllMatches() {
        return matches;
    }

    @Override
    public List<Match> getUpcomingMatches() {
        return getAllMatches().stream().filter(match -> !match.hasBeenPlayed()).collect(Collectors.toList());
    }

    @Override
    public List<Match> getPendingMatches() {
        return new ArrayList<>(); // Round robin never has pending matches
    }

    @Override
    public List<Match> getCompletedMatches() {
        return getAllMatches().stream().filter(Match::hasBeenPlayed).collect(Collectors.toList());
    }

    @Override
    public void onMatchPlayed(Match match) {
        // Has last possible match been played?
        StageStatus oldStatus = status;
        if (getUpcomingMatches().size() == 0)
            status = StageStatus.CONCLUDED;
        else
            status = StageStatus.RUNNING;

        // Notify listeners if status changed
        if (oldStatus != status) {
            nofityStatusListeners(status, oldStatus);
        }
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

    /**
     * @return a hashMap containing the teams and their points.
     */
    public HashMap<Team, Integer> getTeamPointsMap() {

        //Get list of all completed matches
        List<Match> completedMatches = getCompletedMatches();

        //Calculate team wins
        HashMap<Team, Integer> teamPoints = new HashMap<>();
        for (Team team : teams)
            teamPoints.put(team, 0);

        //Run through all matches and give points for win.
        for (Match match : completedMatches) {
            Team winningTeam = match.getWinner();

            //+1 for winning //TODO Should we do more?
            teamPoints.put(winningTeam, teamPoints.get(winningTeam) + 1);
        }

        return teamPoints;
    }

    /**
     * @param numberOfGroups sets the number of groups in the format. Sanity check for at least 1 group
     */
    public void setNumberOfGroups(int numberOfGroups) {
        if (status != StageStatus.PENDING) throw new IllegalStateException("The matches are already generated.");
        this.numberOfGroups = Math.max(numberOfGroups, 1);
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    public ArrayList<RoundRobinGroup> getGroups() {
        return groups;
    }

    public static Team getDummyTeam() {
        return DUMMY_TEAM;
    }

    @Override
    public RoundRobinNode getBracketFXNode(BracketOverviewTabController boc) {
        return new RoundRobinNode(this, boc);
    }

    @Override
    public Node getSettingsFXNode() {
        return new RoundRobinSettingsNode(this);
    }

    @Override
    public void postDeserializationRepair() {
        for (Match match : this.getAllMatches()) match.registerMatchPlayedListener(this);
        setupStatsTracking();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoundRobinFormat that = (RoundRobinFormat) o;
        return Objects.equals(matches, that.matches);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matches);
    }
}