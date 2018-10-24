package dk.aau.cs.ds306e18.tournament.model;

import java.util.*;

public class SwissStage implements Stage, MatchListener {

    private String name = "Swiss";
    private StageStatus status = StageStatus.PENDING;
    private ArrayList<ArrayList<Match>> rounds;
    private int MAX_ROUNDS;
    private ArrayList<Team> teams;
    private HashMap<Team, Integer> teamPoints;

    @Override
    public void start(List<Team> teams) {

        rounds = new ArrayList<>();
        MAX_ROUNDS = calculateMaxRounds(teams.size()); // TODO: Add the ability to end Swiss after X rounds
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

        if(rounds.size() == MAX_ROUNDS) //Is it legal to create another round?
            return false;
        else if(getUpcomingMatches().size() != 0) //Has all matches been played?
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
                    createdMatches.add(new Match(new StarterSlot(team1), new StarterSlot(team2)));
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

        ArrayList<Match> matches = getCompletedMatches();

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
    public ArrayList<Match> getAllMatches() {

        ArrayList<Match> matches = new ArrayList<>();

        for(ArrayList<Match> list : rounds){
            for(Match match : list){
                matches.add(match);
            }
        }

        return matches;
    }

    @Override
    public ArrayList<Match> getUpcomingMatches() {

        ArrayList<Match> allMatches = getAllMatches();
        ArrayList<Match> upComingMatches = new ArrayList<>();

        for(Match match : allMatches)
            if(!match.hasBeenPlayed())
                upComingMatches.add(match);

        return upComingMatches;
    }

    /** All created matches can be played in swiss.
     * @return an empty ArrayList<Match>*/
    @Override
    public ArrayList<Match> getPendingMatches() {

        return new ArrayList<Match>();
    }

    @Override
    public ArrayList<Match> getCompletedMatches() {

        ArrayList<Match> allMatches = getAllMatches();
        ArrayList<Match> playedMatches = new ArrayList<>();

        for(Match match : allMatches)
            if(match.hasBeenPlayed())
                playedMatches.add(match);

        return playedMatches;
    }

    public int getMAX_ROUNDS() {
        return MAX_ROUNDS;
    }

    public boolean hasMaxNumberOfRounds() {

        return rounds.size() == getMAX_ROUNDS();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public StageStatus getStatus() {
        return status; // TODO: Determine when the stage is over
    }

    //TODO DELETE currently used for testing as a workaround.
    public ArrayList<Match> getRawMatches(){
        return rounds.get(rounds.size() - 1);
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
}
