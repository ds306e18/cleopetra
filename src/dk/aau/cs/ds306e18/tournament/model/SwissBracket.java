package dk.aau.cs.ds306e18.tournament.model;

import java.util.*;

public class SwissBracket implements Bracket{

    /** TAKE CARE OF UNEVEN NUMBER OF TEAMS
     *  tie-breaker for scores.. see wiki
     *
     */

    private ArrayList<ArrayList<Match>> rounds;
    private final int MAX_ROUNDS;
    private final ArrayList<Team> teams;
    private HashMap<Team, Integer> teamPoints;

    public static class RecursionHelper{

        ArrayList<Team> teams = new ArrayList<>();
        ArrayList<Match> createdMatches = new ArrayList<>();
        boolean isStepFailed = false;

    }


    public SwissBracket(ArrayList<Team> teams) {

        rounds = new ArrayList<>();
        MAX_ROUNDS = calculateMaxRounds(teams.size());
        teamPoints = createPointsHashMap(teams);
        this.teams = new ArrayList<>(teams);

        //Should generate the first round
        createNewRound();
    }

    private HashMap<Team, Integer> createPointsHashMap(List<Team> teams){

        HashMap<Team, Integer> pointsMap = new HashMap<>();

        for(Team team : teams)
            pointsMap.put(team, 0);

        return pointsMap;
    }

    private int calculateMaxRounds(int numberOfTeams){

        if(numberOfTeams == 0)
            return 0;
        else
            return (numberOfTeams % 2 == 0) ? numberOfTeams - 1 : numberOfTeams;
    }

    /*** Create a new round of swiss, with the current players.
     *
     * @return */
    public boolean createNewRound(){

        if(teams.size() < 1){
            return false;
        }

        /*
        //SHORTCUT, needed? //First round?
        if(rounds.size() == 0){
            createRound();
            return true;
        }*/

        //Is it legal to create another round?
        if(rounds.size() == MAX_ROUNDS)
            return false;

        //Check: is all matches played?
        if(getUpcommingMatches().size() != 0)
            return false;

        createRound();

        return true; //TODO
    }

    private void createRound(){

        //TODO should handle this being called with no rounds played Does it already handle it?

        //At this point: we have played at least one round
        // and all matches has been completed

        //Rules for creating a new match:
            // the new match cannot have played each other before.
            // Aim to match players closest to each other in points.

        // Create ordered list of team, based on points
        ArrayList<Team> orderedTeams = orderTeamsListFromPoints(teams);

        // Then try to create matches from that order and check for "already" played
        ArrayList<Match> newRound = generateRound(orderedTeams);

        rounds.add(newRound);
    }

    /** Takes a list of teams and sorts it based on points.
     * @param teamList a list of teams.
     * @return the given list sorted based on points. */
    private ArrayList<Team> orderTeamsListFromPoints(ArrayList<Team> teamList){
        ArrayList<Team> tempTeams = new ArrayList<>(teamList);
        ArrayList<Team> orderedTeams = new ArrayList<>();

        while(tempTeams.size() != 1){
            int teamCount = tempTeams.size();
            Team teamWithMostPoints = tempTeams.get(0);
            for(int i = 1; i < teamCount; i++){

                if(teamPoints.get(tempTeams.get(i)) < teamPoints.get(teamWithMostPoints))
                    teamWithMostPoints = tempTeams.get(i);
            }

            tempTeams.remove(teamWithMostPoints);
            orderedTeams.add(teamWithMostPoints);
        }

        orderedTeams.add(tempTeams.get(0));

        return orderedTeams;
    }

    /** Takes a list of ordered teams, creates a round of matches with the teams, and return that.
     * @param orderedTeamList an list of teams ordered by points.
     * @return an arrayList of valid matches. */
    private ArrayList<Match> generateRound(ArrayList<Team> orderedTeamList){
        ArrayList<Match> newRound = new ArrayList<>();


        //TODO This is buggy
        while(orderedTeamList.size() > 1){

            int i = 0;
            boolean hasMatchBeenAdded = false;
            while(!hasMatchBeenAdded){

                Team team1 = orderedTeamList.get(i);
                Team team2 = orderedTeamList.get(i+1);

                if(!hasTheseTeamsPlayedBefore(team1, team2)){
                    newRound.add(new Match(new StarterSlot(team1), new StarterSlot(team2)));
                    orderedTeamList.remove(team1);
                    orderedTeamList.remove(team2);

                    hasMatchBeenAdded = true;
                }

                i = i + 2;
            }
        }

        return new ArrayList<>(); //TODO NOT THIS! FIX RETURN

    }

    /**
    private ArrayList<Match> checkAndGenerateMatches(ArrayList<Team> teamList){

        ArrayList<Match> createdMatches = new ArrayList<>();

        if(teamList.size() == 2){

            Team team1 = teamList.get(0);
            Team team2 = teamList.get(1);

            if(hasTheseTeamsPlayedBefore(team1, team1)){
               throw new IllegalArgumentException(); //TODO Create own?
            }else{
                createdMatches.add(new Match(new StarterSlot(team1), new StarterSlot(team2)));
                return createdMatches;
            }
        }

        if(teamList.size() == 3){
            Team team1 = teamList.get(0);
            Team team2 = teamList.get(1);
            Team team3 = teamList.get(2);

            ArrayList<Team> teams = new ArrayList<>();

            teams.add(team1);
            teams.add(team2);

            try{
                createdMatches.addAll(checkAndGenerateMatches(teams));
                return createdMatches;
            }catch (IllegalArgumentException e){

                try{
                    teams.remove(0);
                    teams.remove(1);
                    teams.add(team1);
                    teams.add(team3);
                    createdMatches.addAll(checkAndGenerateMatches(teams));
                    return createdMatches;
                }catch (IllegalArgumentException d){

                    try{
                        teams.remove(0);
                        teams.remove(1);
                        teams.add(team2);
                        teams.add(team3);
                        createdMatches.addAll(checkAndGenerateMatches(teams));
                        return createdMatches;
                    }catch (IllegalArgumentException f){
                        throw new IllegalArgumentException();
                    }

                }
            }

            //Check for x % 2 = 0
            if(teamList.size() % 2 == 0){

                ArrayList<Team> tempTeamList = new ArrayList<>();

                Team tempTeam1, tempTeam2;

                for(int i = 0; i < teamList.size(); i = i+2){

                    tempTeam1 = teamList.get(i);
                    tempTeam2 = teamList.get(i+1);
                    tempTeamList.add(tempTeam1);
                    tempTeamList.add(tempTeam2);

                    if(!hasTheseTeamsPlayedBefore(tempTeam1, tempTeam2)){
                        try{
                            createdMatches.addAll(checkAndGenerateMatches(tempTeamList));
                        }catch (IllegalArgumentException e){

                            //TODO Check if there is two more elements

                            Team tempTeam3 = teamList.get(i+2);
                            Team tempTeam4 = teamList.get(i+3);
                            i++;

                        }
                    }

                }

            }else{ //Check for x % 2 = 1

            }
        }
    }*/

    public RecursionHelper checkAndGenerateMatches(RecursionHelper recursionHelper){

        if(recursionHelper.teams.size() == 1){
            recursionHelper.isStepFailed = false;
            return recursionHelper;
        }

        if(recursionHelper.teams.size() == 2){

            Team team1 = recursionHelper.teams.get(0);
            Team team2 = recursionHelper.teams.get(1);

            if(hasTheseTeamsPlayedBefore(team1, team1)){
                recursionHelper.isStepFailed = true;
                return recursionHelper;
            }else{
                recursionHelper.isStepFailed = false;
                recursionHelper.createdMatches.add(new Match(new StarterSlot(team1), new StarterSlot(team2)));
                recursionHelper.teams.remove(team1);
                recursionHelper.teams.remove(team2);
                return recursionHelper;
            }
        }

        if(recursionHelper.teams.size() == 3) {
            Team team1 = recursionHelper.teams.get(0);
            Team team2 = recursionHelper.teams.get(1);
            Team team3 = recursionHelper.teams.get(2);

            RecursionHelper recursionHelperTemp = new RecursionHelper();
            recursionHelperTemp.teams.add(team1);
            recursionHelperTemp.teams.add(team2);

            recursionHelperTemp = checkAndGenerateMatches(recursionHelperTemp);
            if (recursionHelperTemp.isStepFailed) {

                //Try with two other teams
                recursionHelperTemp.teams.remove(team1);
                recursionHelperTemp.teams.add(team3);
                recursionHelperTemp = checkAndGenerateMatches(recursionHelperTemp);
                if (recursionHelperTemp.isStepFailed) {

                    //Try with the last combination
                    recursionHelperTemp.teams.remove(team2);
                    recursionHelperTemp.teams.add(team1);
                    recursionHelperTemp = checkAndGenerateMatches(recursionHelperTemp);
                    if (recursionHelperTemp.isStepFailed) {
                        return recursionHelperTemp;
                    }
                }

            }

            recursionHelperTemp.isStepFailed = false;
            return recursionHelperTemp;
        }

        //Check for x % 2 = 0
        if(recursionHelper.teams.size() % 2 == 0){

            while(recursionHelper.teams.size() != 0){
                Team team1, team2;

                team1 = recursionHelper.teams.get(0);
                team2 = recursionHelper.teams.get(0);



            }





        }else{

        }

        return recursionHelper;


        /**
        try{
            createdMatches.addAll(checkAndGenerateMatches(teams));
            return createdMatches;
        }catch (IllegalArgumentException e){

            try{
                teams.remove(0);
                teams.remove(1);
                teams.add(team1);
                teams.add(team3);
                createdMatches.addAll(checkAndGenerateMatches(teams));
                return createdMatches;
            }catch (IllegalArgumentException d){

                try{
                    teams.remove(0);
                    teams.remove(1);
                    teams.add(team2);
                    teams.add(team3);
                    createdMatches.addAll(checkAndGenerateMatches(teams));
                    return createdMatches;
                }catch (IllegalArgumentException f){
                    throw new IllegalArgumentException();
                }

            }
        }

        //Check for x % 2 = 0
        if(teamList.size() % 2 == 0){

            ArrayList<Team> tempTeamList = new ArrayList<>();

            Team tempTeam1, tempTeam2;

            for(int i = 0; i < teamList.size(); i = i+2){

                tempTeam1 = teamList.get(i);
                tempTeam2 = teamList.get(i+1);
                tempTeamList.add(tempTeam1);
                tempTeamList.add(tempTeam2);

                if(!hasTheseTeamsPlayedBefore(tempTeam1, tempTeam2)){
                    try{
                        createdMatches.addAll(checkAndGenerateMatches(tempTeamList));
                    }catch (IllegalArgumentException e){

                        //TODO Check if there is two more elements

                        Team tempTeam3 = teamList.get(i+2);
                        Team tempTeam4 = teamList.get(i+3);
                        i++;

                    }
                }

            }

        }else{ //Check for x % 2 = 1

        }
    }*/

    }


    private boolean hasTheseTeamsPlayedBefore(Team team1, Team team2){

        ArrayList<Match> matches = getCompletedMatches();

        for(Match match : matches){

            Team blueTeam = match.getBlueTeam();
            Team orangeTeam = match.getOrangeTeam();

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
    public ArrayList<Match> getUpcommingMatches() {


        ArrayList<Match> allMatches = getAllMatches();
        ArrayList<Match> upCommingMatches = new ArrayList<>();

        for(Match match : allMatches)
            if(!match.hasBeenPlayed())
                upCommingMatches.add(match);

        return upCommingMatches;
    }

    /** All created matches can be played in swiss. */
    @Override
    public ArrayList<Match> getUnplayableMatches() {

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

    public boolean hasMaxNumberOfRounds(){

        return rounds.size() == getMAX_ROUNDS();
    }
}
