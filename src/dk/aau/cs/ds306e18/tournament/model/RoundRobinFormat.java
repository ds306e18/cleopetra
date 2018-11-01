package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.ui.tabs.BracketOverview;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoundRobinFormat implements Format, MatchListener {

    private static final Team DUMMY_TEAM = new Team("Dummy", null, 0, null);

    private StageStatus status = StageStatus.PENDING;
    private int numberOfTeams;
    private ArrayList<Match> matches;

    /**
     * constructor that automatically creates an arraylist of matches made on the principles of berger tables
     *
     * @param seededTeams arraylist of all the teams in the bracket
     */
    public void start(List<Team> seededTeams) {
        ArrayList<Team> teams = new ArrayList<>(seededTeams); // TODO: Add support for multiple groups
        //if there is an uneven amount of teams, add a dummy team and later remove matches that include the dummy team
        if (teams.size() % 2 != 0) {
            teams.add(DUMMY_TEAM);
        }
        this.numberOfTeams = teams.size();
        this.matches = generateMatches(teams);
        status = StageStatus.RUNNING;
    }

    @Override
    public StageStatus getStatus() {
        return status; // TODO: Determine when the stage is over
    }

    /**
     * generates a hashMap to be able to give id to each team for use in berger tables
     */
    private HashMap<Team, Integer> createIdHashMap(ArrayList<Team> teams) {
        HashMap<Team, Integer> map = new HashMap<>();
        for (int m = 1; m < teams.size() + 1; m++) {
            map.put(teams.get(m - 1), m);
        }
        return map;
    }

    /**
     * creates a list of matches, with each team changing color between each of their matches
     *
     * @param teams arraylist of all teams in the bracket
     * @return returns a complete arraylist of matches
     */
    private ArrayList<Match> generateMatches(ArrayList<Team> teams) {
        int nextBlue, nextOrange;
        Match[][] tempMatches = new Match[numberOfTeams - 1][numberOfTeams / 2];

        HashMap<Team, Integer> map = createIdHashMap(teams);

        //number of rounds needed
        for (int round = 0; round < numberOfTeams - 1; round++) {
            //number of matches per round
            for (int match = 0; match < numberOfTeams / 2; match++) {
                //create first round, which is not based on previous rounds
                if (round == 0) {
                    tempMatches[round][match] = new Match(teams.get(match), teams.get(numberOfTeams - (match + 1)));
                }
                //hardcoding team with highest id (numberOfTeams - 1 ) to first match each round
                //the other team is found by berger tables rules (runCheck) on the id of the team in the same match,
                //but previous round
                else if (match == 0) {
                    //if uneven round, player with highest id becomes blue player
                    if ((round % 2) != 0) {
                        nextOrange = runCheck(map.get(tempMatches[round - 1][match].getBlueTeam()));
                        tempMatches[round][match] = new Match((teams.get(numberOfTeams - 1)), teams.get(nextOrange - 1));
                        //else become orange team
                    } else {
                        nextBlue = runCheck(map.get(tempMatches[round - 1][match].getOrangeTeam()));
                        tempMatches[round][match] = new Match(teams.get(nextBlue - 1), (teams.get(numberOfTeams - 1)));
                    }
                } else {
                    //if not the first round, or first match, find both players by runCheck according to berger tables,
                    //on previous teams
                    nextBlue = runCheck(map.get(tempMatches[round - 1][match].getBlueTeam()));
                    nextOrange = runCheck(map.get(tempMatches[round - 1][match].getOrangeTeam()));
                    tempMatches[round][match] = new Match(teams.get(nextBlue - 1), (teams.get(nextOrange - 1)));
                }
            }
        }
        return removeDummyMatches(tempMatches);
    }

    /** TODO: Find a better name for method
     * Find new team, by adding n/2 to the team in the same place in previous round, if this exceeds n-1,
     * instead subtract n/2 - 1.
     *
     * @param id of the teamin the match in the previous round
     * @return the id of the team that should be in this match, according to last
     */
    public int runCheck(int id) {
        if ((id + (numberOfTeams / 2)) > (numberOfTeams - 1)) {
            return id - ((numberOfTeams / 2) - 1);
        } else return id + (numberOfTeams / 2);
    }


    /**
     * Dummy teams are removed from the array of matches
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

    @Override
    public ArrayList<Match> getUpcomingMatches() {

        ArrayList<Match> allMatches = getAllMatches();
        ArrayList<Match> upComingMatches = new ArrayList<>();

        for (Match match : allMatches)
            if (!match.hasBeenPlayed())
                upComingMatches.add(match);

        return upComingMatches;
    }

    @Override
    public ArrayList<Match> getCompletedMatches() {

        ArrayList<Match> allMatches = getAllMatches();
        ArrayList<Match> playedMatches = new ArrayList<>();

        for (Match match : allMatches)
            if (match.hasBeenPlayed())
                playedMatches.add(match);

        return playedMatches;
    }

    @Override
    public ArrayList<Match> getPendingMatches() {
        return null;
    }

    @Override
    public ArrayList<Match> getAllMatches() {
        return matches;
    }

    @Override
    public void onMatchPlayed(Match match) {
        // TODO: Register stage as listener to all relevant matches
        // TODO: Evaluate if last match, if it is then status = CONCLUDED. Also add tests
    }

    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {
        return null; // TODO: Returns a list of the teams that performed best this stage. They should be sorted after performance, with best team first.
    }

    @Override
    public Node getJavaFxNode(BracketOverview bracketOverview) {
        return null; //TODO
    }
}