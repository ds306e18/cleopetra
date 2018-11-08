package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.GroupFormat;
import dk.aau.cs.ds306e18.tournament.model.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchListener;
import dk.aau.cs.ds306e18.tournament.ui.tabs.BracketOverview;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoundRobinFormat extends GroupFormat implements MatchListener {


    private ArrayList<Match> matches;
    private static final Team DUMMY_TEAM = new Team("Dummy", new ArrayList<Bot>(), 0, "");
    private ArrayList<RoundRobinGroup> groups = new ArrayList<>();
    //should be set before start() is called, to determine number of groups that should be created
    private int numberOfGroups = 1;

    /**
     * Constructor that automatically creates an arraylist of matches made on the principles of berger tables.
     *
     * @param seededTeams arraylist of all the teams in the bracket.
     */
    public void start(List<Team> seededTeams) {
        teams = new ArrayList<>(seededTeams);
        ArrayList<Team> teams = new ArrayList<>(seededTeams);

        if (seededTeams.size() == 0) {
            matches = new ArrayList<>();
            status = StageStatus.CONCLUDED;
            //if the number of groups is within the allowed range, create groups
        } else if (numberOfGroupsAllowed()) {
            createGroupsWithMatches(teams);
            matches = extractMatchesFromGroups();
            status = StageStatus.RUNNING;
            //else set the number of groups to the maximum allowed (2 teams per group) and create groups
        } else {
            setNumberOfGroups(maxNumberOfGroups());
            createGroupsWithMatches(teams);
            matches = extractMatchesFromGroups();
            status = StageStatus.RUNNING;
        }
    }

    private int maxNumberOfGroups() {
        return teams.size() / 2;
    }

    private boolean numberOfGroupsAllowed() {
        if (teams.size() / numberOfGroups >= 2) {
            return true;
        } else return false;
    }

    private void createGroupsWithMatches(ArrayList<Team> teams) {
        int leftoverTeams = teams.size() % numberOfGroups;


        //sorts the teams so that the lowest seeds comes first
        teams.sort(new SortBySeed());

        for (int i = 0; i < numberOfGroups; i++) {
            ArrayList<Team> splitArray = splitArrayForEachGroup(teams, i);

            //if there must be groups with more teams than others, add an extra team to the first groups, from the highest
            //index of the teams array
            if (leftoverTeams != 0) {
                splitArray.add(teams.get(teams.size() - leftoverTeams));
                leftoverTeams--;
            }

            //if there is an uneven amount of teams, add a dummy team and later remove matches that include the dummy team
            if (splitArray.size() % 2 != 0) {
                splitArray.add(DUMMY_TEAM);
            }

            RoundRobinGroup roundRobinGroup = new RoundRobinGroup(splitArray);
            roundRobinGroup.setMatches(generateMatches(roundRobinGroup.getTeams()));
            groups.add(roundRobinGroup);
        }
    }


    private ArrayList<Team> splitArrayForEachGroup(ArrayList<Team> teams, int startingPoint) {
        ArrayList<Team> splitArray = new ArrayList<>();

        for (int i = 0; i < teams.size() / numberOfGroups; i++)
            splitArray.add(teams.get(startingPoint + numberOfGroups * i));
        return splitArray;
    }

    /**
     * Creates a list of matches, with each team changing color between each of their matches.
     *
     * @param teams arraylist of all teams in the bracket.
     * @return returns a complete arraylist of matches.
     */
    private ArrayList<Match> generateMatches(ArrayList<Team> teams) {
        int nextBlue, nextOrange;
        Match[][] tempMatches = new Match[teams.size() - 1][teams.size() / 2];

        HashMap<Team, Integer> map = createIdHashMap(teams);

        //number of rounds needed
        for (int round = 0; round < teams.size() - 1; round++) {
            //number of matches per round
            for (int match = 0; match < teams.size() / 2; match++) {
                //create first round, which is not based on previous rounds
                if (round == 0) {
                    tempMatches[round][match] = createNewMatch(teams.get(match), teams.get(teams.size() - (match + 1)));
                }
                //hardcoding team with highest id (numberOfTeams - 1 ) to first match each round
                //the other team is found by berger tables rules (findIdOfNextPlayer) on the id of the team in the same match,
                //but previous round
                else if (match == 0) {
                    //else become orange team
                    if ((round % 2) == 0) {
                        nextBlue = findIdOfNextPlayer(map.get(tempMatches[round - 1][match].getOrangeTeam()), teams.size());
                        tempMatches[round][match] = createNewMatch(teams.get(nextBlue - 1), (teams.get(teams.size() - 1)));
                        //if uneven round, player with highest id becomes blue player
                    } else {
                        nextOrange = findIdOfNextPlayer(map.get(tempMatches[round - 1][match].getBlueTeam()), teams.size());
                        tempMatches[round][match] = createNewMatch((teams.get(teams.size() - 1)), teams.get(nextOrange - 1));
                    }
                } else {
                    //if not the first round, or first match, find both players by findIdOfNextPlayer according to berger tables,
                    //on previous teams
                    nextBlue = findIdOfNextPlayer(map.get(tempMatches[round - 1][match].getBlueTeam()), teams.size());
                    nextOrange = findIdOfNextPlayer(map.get(tempMatches[round - 1][match].getOrangeTeam()), teams.size());
                    tempMatches[round][match] = createNewMatch(teams.get(nextBlue - 1), (teams.get(nextOrange - 1)));
                }
            }
        }

        return removeDummyMatches(tempMatches);
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
        for (int m = 1; m <= teams.size(); m++) {
            map.put(teams.get(m - 1), m);
        }
        return map;
    }

    /**
     * @return a new match from the given parameters. And adds listener.
     */
    private Match createNewMatch(Team team1, Team team2) {

        Match match = new Match(team1, team2);
        match.registerListener(this);
        return match;
    }

    /**
     * Find new team, by adding n/2 to the team in the same place in previous round, if this exceeds n-1,
     * instead subtract n/2 - 1.
     *
     * @param id of the team in the match in the previous round.
     * @return the id of the team that should be in this match, according to last.
     */
    public int findIdOfNextPlayer(int id, int limit) {
        if ((id + (limit / 2)) > (limit - 1)) {
            return id - ((limit / 2) - 1);
        } else return id + (limit / 2);
    }

    /**
     * @return the given array with dummy teams removed.
     */
    private ArrayList<Match> removeDummyMatches(Match[][] tempMatches) {

        ArrayList<Match> matches = new ArrayList<>();

        for (int i = 0; i < tempMatches.length; i++) {
            for (int j = 0; j < tempMatches[i].length; j++) {
                if (tempMatches[i][j].getOrangeTeam().equals(DUMMY_TEAM) ||
                        tempMatches[i][j].getBlueTeam().equals(DUMMY_TEAM)) {
                    continue;
                } else {
                    matches.add(tempMatches[i][j]);
                }
            }
        }
        return matches;
    }

    private ArrayList<Match> extractMatchesFromGroups() {
        ArrayList<Match> listOfAllMatches = new ArrayList<>();
        for (RoundRobinGroup group : groups) {
            listOfAllMatches.addAll(group.getMatches());
        }
        return listOfAllMatches;
    }

    @Override
    public List<Match> getAllMatches() {
        return matches;
    }

    @Override
    public void onMatchPlayed(Match match) {
        //Evaluate: has last possible match been played?
        if (getUpcomingMatches().size() == 0)
            status = StageStatus.CONCLUDED;
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

    public void setNumberOfGroups(int numberOfGroups) {
        if (status == StageStatus.RUNNING) throw new IllegalStateException("The matches are already generated.");
        if (numberOfGroups >= 1) {
            this.numberOfGroups = numberOfGroups;
        }
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
    public Node getJavaFxNode(BracketOverview bracketOverview) {
        return null; //TODO
    }
}