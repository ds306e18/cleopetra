package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.UI.Tabs.BracketOverview;
import dk.aau.cs.ds306e18.tournament.UI.bracketObjects.SwissNode;
import javafx.scene.Node;

import java.util.*;

public class SwissFormat implements Format, MatchListener {

    private StageStatus status = StageStatus.PENDING;
    private ArrayList<ArrayList<Match>> rounds;
    private int maxRounds;
    private ArrayList<Team> teams;
    private HashMap<Team, Integer> teamPoints;

    @Override
    public void start(List<Team> teams) {

        rounds = new ArrayList<>();
        maxRounds = calculateMaxRounds(teams.size());
        teamPoints = createPointsHashMap(teams);
        this.teams = new ArrayList<>(teams);
        status = StageStatus.RUNNING;

        createNewRound(); //generate the first round
    }

    /** Creates the hashMap that stores the "swiss" points for the teams. */
    private HashMap<Team, Integer> createPointsHashMap(List<Team> teams) {

        HashMap<Team, Integer> pointsMap = new HashMap<>();

        for(Team team : teams)
            pointsMap.put(team, 0);

        return pointsMap;
    }

    /** Calculates the max number of rounds that can be played with the given number of teams.
     * @param numberOfTeams the number of teams participating.
     * @return max number of rounds possible with the given number of teams.*/
    public static int calculateMaxRounds(int numberOfTeams) {

        if (numberOfTeams == 0)
            return 0;
        else
            return (numberOfTeams % 2 == 0) ? numberOfTeams - 1 : numberOfTeams;
    }

    /** Creates a new round of swiss, with the current teams.
     * @return true if a round was generated and false if a new round could not be generated. */
    public boolean createNewRound() {

        if(status == StageStatus.PENDING){
            return false;
        } else if(status == StageStatus.CONCLUDED){
            return false;
        } else if(rounds.size() == maxRounds){ //Is it legal to create another round?
            return false;
        } else if(getUpcomingMatches().size() != 0) //Has all matches been played?
            return false;
        else if(rounds.size() != 0) { //Assign points for played matches
            assignPoints();
            createRound();
            return true;
        }else{
            createRound();
            return true;
        }
    }

    /** Used when a round has been played to assign points to the teams based on the played matches.
     * Teams will get 2 points for winning and -2 for loosing. */
    private void assignPoints() {

        ArrayList<Match> finishedRoundMatches = rounds.get(rounds.size() - 1);

        for (Match match : finishedRoundMatches){
            Team winnerTeam = match.getWinner();
            Team loserTeam = match.getLoser();

            teamPoints.put(winnerTeam, teamPoints.get(winnerTeam) + 2);
            teamPoints.put(loserTeam, teamPoints.get(loserTeam) - 2);
        }
    }

    /** Takes a list of teams and sorts it based on their points in the given hashMap.
     * @param teamList a list of teams.
     * @param teamPoints a hashMap containing teams as key and their points as value.
     * @return the given list sorted based on the given points. */
    private ArrayList<Team> orderTeamsListFromPoints(ArrayList<Team> teamList, HashMap<Team, Integer> teamPoints) {
        ArrayList<Team> tempTeams = new ArrayList<>(teamList);
        ArrayList<Team> orderedTeams = new ArrayList<>();

        while(tempTeams.size() != 0){
            int teamCount = tempTeams.size();
            Team teamWithMostPoints = tempTeams.get(0);

            //Find the team with the most points.
            for(int i = 1; i < teamCount; i++){

                if(teamPoints.get(tempTeams.get(i)) < teamPoints.get(teamWithMostPoints))
                    teamWithMostPoints = tempTeams.get(i);
            }

            //Remove that team from the tempList and add it to the ordered list.
            tempTeams.remove(teamWithMostPoints);
            orderedTeams.add(teamWithMostPoints);
        }

        return orderedTeams;
    }

    /** Creates the matches for the next round. This is done so that no team will play the same opponents twice,
     * and with the teams with the closest amount of points.
     * Credit for algorithm: Amanda.*/
    private void createRound() {

        // Create ordered list of team, based on points.
        ArrayList<Team> orderedTeamList = orderTeamsListFromPoints(teams, teamPoints);

        ArrayList<Match> createdMatches = new ArrayList<>();

        // Create matches while there is more than 1 team in the list.
        while(orderedTeamList.size() > 1){

            Team team1 = orderedTeamList.get(0);
            Team team2 = null;

            //Find the next team that has not played team1 yet.
            for(int i = 1; i < orderedTeamList.size(); i++){

                team2 = orderedTeamList.get(i);

                //Has the two selected teams played each other before?
                if(!hasTheseTeamsPlayedBefore(team1, team2)){
                    Match match = new Match(new StarterSlot(team1), new StarterSlot(team2));
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

    /** Checks if the two given teams has played a match against each other.
     * Returns a boolean based on that comparison.
     * @param team1 one of the two teams for the check.
     * @param team2 one of the two teams for the check.
     * @return true if the two given teams has played before, and false if that is not the case. */
    private boolean hasTheseTeamsPlayedBefore(Team team1, Team team2) {

        List<Match> matches = getCompletedMatches();

        for(Match match : matches){

            Team blueTeam = match.getBlueTeam();
            Team orangeTeam = match.getOrangeTeam();

            //Compare the current match's teams with the two given teams.
            if(team1 == blueTeam && team2 == orangeTeam || team1 == orangeTeam && team2 == blueTeam)
                return true;
        }

        return false;
    }

    @Override
    public List<Match> getAllMatches() {

        ArrayList<Match> matches = new ArrayList<>();

        for(ArrayList<Match> list : rounds){
            for(Match match : list){
                matches.add(match);
            }
        }

        return matches;
    }

    @Override
    public List<Match> getUpcomingMatches() {

        List<Match> allMatches = getAllMatches();
        ArrayList<Match> upComingMatches = new ArrayList<>();

        for(Match match : allMatches)
            if(!match.hasBeenPlayed())
                upComingMatches.add(match);

        return upComingMatches;
    }

    /** All created matches can be played in swiss.
     * @return an empty ArrayList<Match>*/
    @Override
    public List<Match> getPendingMatches() {

        return new ArrayList<>();
    }

    @Override
    public List<Match> getCompletedMatches() {

        List<Match> allMatches = getAllMatches();
        ArrayList<Match> playedMatches = new ArrayList<>();

        for(Match match : allMatches)
            if(match.hasBeenPlayed())
                playedMatches.add(match);

        return playedMatches;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public boolean hasMaxNumberOfRounds() {

        return rounds.size() == getMaxRounds();
    }

    @Override
    public StageStatus getStatus() {
        return status;
    }

    /** Used for tests. This should not be used to anything else. */
    public ArrayList<Match> getRawMatches(){
        return rounds.get(rounds.size() - 1);
    }

    @Override
    public void onMatchPlayed(Match match) {
        //Evaluate: has last possible match been played?
        if(hasMaxNumberOfRounds() && getUpcomingMatches().size() == 0){
            status = StageStatus.CONCLUDED;
        }
    }

    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {

        //Create points ordered team list
        ArrayList<Team> teamPointsOrderList = new ArrayList<>();
        ArrayList<Team> tempTeamsList = new ArrayList<>(teams);
        while(tempTeamsList.size() != 0){
            Team teamWithMostPoints = tempTeamsList.get(0);

            //Find the team with the most points
            for(Team team : tempTeamsList){
                if(teamPoints.get(team) > teamPoints.get(teamWithMostPoints))
                    teamWithMostPoints = team;
            }

            teamPointsOrderList.add(teamWithMostPoints);
            tempTeamsList.remove(teamWithMostPoints);
        }

        if(teams.size() <= count) //Is the requested count larger then the count of teams?
            return new ArrayList<>(teamPointsOrderList);
        else {
            if (teamPoints.get(teamPointsOrderList.get(count - 1)).equals(teamPoints.get(teamPointsOrderList.get(count)))) {

                //TIE BREAKING!

                ArrayList<Team> topTeamsList = new ArrayList<>();

                //Find the teams that are tied
                ArrayList<Team> tiedTeams = new ArrayList<>();
                for(Team team : teamPointsOrderList)
                    if(teamPoints.get(team) == teamPoints.get(teamPointsOrderList.get(count-1))) //Does the current team has the same points as the for sure tied one.
                        tiedTeams.add(team);

                //Create list with the topteams down untill and without the tied teams
                for(Team team : teamPointsOrderList){

                    if(teamPoints.get(team) == teamPoints.get(teamPointsOrderList.get(count-1)))
                        break;
                    else
                        topTeamsList.add(team);
                }

                //Get list of tie broken teams
                ArrayList<Team> tieBrokenTeams = new ArrayList<>(tieBreaker.compareAll(tiedTeams, tiedTeams.size()));

                //Fill the topteamsList with the remaining needed count of teams from the tie broken teams
                while(topTeamsList.size() < count){
                    topTeamsList.add(tieBrokenTeams.get(0));
                    tieBrokenTeams.remove(0);
                }

                return topTeamsList;
            }
            //Get the desired number of teams
            ArrayList<Team> desiredNumberOfTeam = new ArrayList<>();
            for (int i = 0; i < count; i++)
                desiredNumberOfTeam.add(teamPointsOrderList.get(i));

            return desiredNumberOfTeam;
        }
    }

    @Override
    public Node getJavaFxNode(BracketOverview bracketOverview) {

        return new SwissNode(this, bracketOverview);
    }

    /** @return an arraylist of the current created rounds. */
    public ArrayList<ArrayList<Match>> getRounds(){
        return new ArrayList<>(this.rounds);
    }
}
