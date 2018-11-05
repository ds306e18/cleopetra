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
        } else {
            createGroupsWithMatches(teams);
            matches = extractMatchesFromGroups();
            status = StageStatus.RUNNING;
        }
    }

    private void createGroupsWithMatches(ArrayList<Team> teams) {

        for (int i = 0; i < numberOfGroups; i++) {
            ArrayList<Team> splitArray = splitArrayForEachGroup((i) * teams.size());

            RoundRobinGroup roundRobinGroup = new RoundRobinGroup(splitArray);
            roundRobinGroup.setMatches(generateMatches(roundRobinGroup.getTeams()));
            groups.add(roundRobinGroup);
        }
    }

    private ArrayList<Team> splitArrayForEachGroup(int startingPoint) {
        ArrayList<Team> splitArray = new ArrayList<>();

        for (int i = 0; i < teams.size() / numberOfGroups; i++) {
            splitArray.add(teams.get(startingPoint + i));
        }
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
                    //if uneven round, player with highest id becomes blue player
                    if ((round % 2) != 0) {
                        nextOrange = findIdOfNextPlayer(map.get(tempMatches[round - 1][match].getBlueTeam()));
                        tempMatches[round][match] = createNewMatch((teams.get(teams.size() - 1)), teams.get(nextOrange - 1));
                        //else become orange team
                    } else {
                        nextBlue = findIdOfNextPlayer(map.get(tempMatches[round - 1][match].getOrangeTeam()));
                        tempMatches[round][match] = createNewMatch(teams.get(nextBlue - 1), (teams.get(teams.size() - 1)));
                    }
                } else {
                    //if not the first round, or first match, find both players by findIdOfNextPlayer according to berger tables,
                    //on previous teams
                    nextBlue = findIdOfNextPlayer(map.get(tempMatches[round - 1][match].getBlueTeam()));
                    nextOrange = findIdOfNextPlayer(map.get(tempMatches[round - 1][match].getOrangeTeam()));
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
        for (int m = 1; m < teams.size() + 1; m++) {
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
    public int findIdOfNextPlayer(int id) {
        if ((id + (teams.size() / 2)) > (teams.size() - 1)) {
            return id - ((teams.size() / 2) - 1);
        } else return id + (teams.size() / 2);
    }

    /**
     * @return the given array with dummy teams removed.
     */
    private ArrayList<Match> removeDummyMatches(Match[][] tempMatches) {

        ArrayList<Match> matches = new ArrayList<>();

        for (int i = 0; i < tempMatches.length; i++) {
            for (int j = 0; j < tempMatches[i].length; j++) {
                if (tempMatches[i][j].getOrangeTeam().getTeamName().equals("Dummy") ||
                        tempMatches[i][j].getBlueTeam().getTeamName().equals("Dummy")) {
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
        this.numberOfGroups = numberOfGroups;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    @Override
    public Node getJavaFxNode(BracketOverview bracketOverview) {
        return null; //TODO
    }
}