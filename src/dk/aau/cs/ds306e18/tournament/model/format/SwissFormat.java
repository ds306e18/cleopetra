package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.GroupFormat;
import dk.aau.cs.ds306e18.tournament.model.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.match.MatchListener;
import dk.aau.cs.ds306e18.tournament.ui.controllers.BracketOverviewTabController;
import dk.aau.cs.ds306e18.tournament.ui.bracketObjects.SwissNode;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SwissFormat extends GroupFormat implements MatchListener {

    private ArrayList<ArrayList<Match>> rounds;
    private int maxRoundsPossible, roundCount;
    private HashMap<Team, Integer> teamPoints;

    @Override
    public void start(List<Team> teams) {
        rounds = new ArrayList<>();
        maxRoundsPossible = getMaxRoundsPossible(teams.size());
        teamPoints = createPointsHashMap(teams);
        this.teams = new ArrayList<>(teams);
        status = StageStatus.RUNNING;

        createNewRound(); //generate the first round
    }

    /** Creates the hashMap that stores the "swiss" points for the teams. */
    private HashMap<Team, Integer> createPointsHashMap(List<Team> teams) {

        HashMap<Team, Integer> pointsMap = new HashMap<>();

        for (Team team : teams)
            pointsMap.put(team, 0);

        return pointsMap;
    }

    /**
     * Creates a new round of swiss, with the current teams.
     *
     * @return true if a round was generated and false if a new round could not be generated.
     */
    public boolean createNewRound() {

        if (isGenerateNewRoundAllowed()) {
            if (rounds.size() != 0) //Assign points for played matches
                assignPointsForLatestRound();

            createRound();

            return true;
        } else {
            return false;
        }
    }

    /**
     * Used when a round has been played to assign points to the teams based on the played matches.
     * Teams will get 2 points for winning and -2 for loosing.
     */
    private void assignPointsForLatestRound() {

        ArrayList<Match> finishedRoundMatches = rounds.get(rounds.size() - 1);

        for (Match match : finishedRoundMatches) {
            Team winnerTeam = match.getWinner();
            Team loserTeam = match.getLoser();

            teamPoints.put(winnerTeam, teamPoints.get(winnerTeam) + 2);
            teamPoints.put(loserTeam, teamPoints.get(loserTeam) - 2);
        }
    }

    /**
     * Takes a list of teams and sorts it based on their points in the given hashMap.
     *
     * @param teamList   a list of teams.
     * @param teamPoints a hashMap containing teams as key and their points as value.
     * @return the given list sorted based on the given points.
     */
    private ArrayList<Team> orderTeamsListFromPoints(ArrayList<Team> teamList, HashMap<Team, Integer> teamPoints) {
        ArrayList<Team> tempTeams = new ArrayList<>(teamList);
        ArrayList<Team> orderedTeams = new ArrayList<>();

        while (tempTeams.size() != 0) {
            int teamCount = tempTeams.size();
            Team teamWithMostPoints = tempTeams.get(0);

            //Find the team with the most points.
            for (int i = 1; i < teamCount; i++) {

                if (teamPoints.get(tempTeams.get(i)) < teamPoints.get(teamWithMostPoints))
                    teamWithMostPoints = tempTeams.get(i);
            }

            //Remove that team from the tempList and add it to the ordered list.
            tempTeams.remove(teamWithMostPoints);
            orderedTeams.add(teamWithMostPoints);
        }

        return orderedTeams;
    }

    /**
     * Creates the matches for the next round. This is done so that no team will play the same opponents twice,
     * and with the teams with the closest amount of points.
     * Credit for algorithm: Amanda.
     */
    private void createRound() {

        // Create ordered list of team, based on points.
        ArrayList<Team> orderedTeamList = orderTeamsListFromPoints(teams, teamPoints);

        ArrayList<Match> createdMatches = new ArrayList<>();

        // Create matches while there is more than 1 team in the list.
        while (orderedTeamList.size() > 1) {

            Team team1 = orderedTeamList.get(0);
            Team team2 = null;

            //Find the next team that has not played team1 yet.
            for (int i = 1; i < orderedTeamList.size(); i++) {

                team2 = orderedTeamList.get(i);

                //Has the two selected teams played each other before?
                if (!hasTheseTeamsPlayedBefore(team1, team2)) {
                    Match match = new Match(team1, team2);
                    match.registerListener(this);
                    createdMatches.add(match);
                    break; //Two valid teams has been found, and match has been created. BREAK.
                }
            }

            //Remove the two valid teams.
            orderedTeamList.remove(team1);
            orderedTeamList.remove(team2);
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

            Team blueTeam = match.getBlueTeam();
            Team orangeTeam = match.getOrangeTeam();

            //Compare the current match's teams with the two given teams.
            if (team1 == blueTeam && team2 == orangeTeam || team1 == orangeTeam && team2 == blueTeam)
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

    public int getMaxRoundsPossible() {
        return maxRoundsPossible;
    }

    /** Returns the max number of rounds that can be played with the given number of teams. */
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
        this.roundCount = roundCount;
    }

    public boolean hasUnstartedRounds() {
        return rounds.size() < roundCount;
    }

    /** @Return the latest generated round. Or null if there are no rounds generated. */
    public List<Match> getLatestRound() {
        if (rounds.size() == 0) return null;
        return new ArrayList<>(rounds.get(rounds.size() - 1));
    }

    @Override
    public void onMatchPlayed(Match match) {
        // Has last possible match been played?
        if (!hasUnstartedRounds() && getUpcomingMatches().size() == 0) {
            status = StageStatus.CONCLUDED;
        }
    }

    /** @return a hashMap containing the teams and their points. */
    public HashMap<Team, Integer> getTeamPointsMap() {
        return new HashMap<>(teamPoints);
    }

    @Override
    public Node getBracketFXNode(BracketOverviewTabController boc) {
        return new SwissNode(this, boc);
    }

    @Override
    public Node getOptionFXNode() {
        return null; // TODO
    }

    /** @return an arraylist of the current created rounds. */
    public ArrayList<ArrayList<Match>> getRounds() {
        ArrayList<ArrayList<Match>> roundsCopy = new ArrayList<>(rounds.size());
        for (ArrayList<Match> r : rounds) {
            roundsCopy.add(new ArrayList<>(r));
        }
        return roundsCopy;
    }

    /** As format is Round Robin, nothing is necessary to postDeserializationRepair after deserialization, except listeners */
    @Override
    public void repair() {
        for (Match match : getAllMatches()) match.registerListener(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwissFormat that = (SwissFormat) o;
        return getMaxRoundsPossible() == that.getMaxRoundsPossible() &&
                Objects.equals(getRounds(), that.getRounds()) &&
                Objects.equals(teamPoints, that.teamPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRounds(), getMaxRoundsPossible(), teamPoints);
    }

    /** @return true if it is allowed to generate a new round. */
    public boolean isGenerateNewRoundAllowed() {
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
}
