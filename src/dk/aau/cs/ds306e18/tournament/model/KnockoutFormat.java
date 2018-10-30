package dk.aau.cs.ds306e18.tournament.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.spi.AbstractResourceBundleProvider;

public abstract class KnockoutFormat implements Format {

    public ArrayList<Team> teams;
    public StageStatus status = StageStatus.PENDING;

    /** @return a hashMap containing the teams and their points. */
    abstract HashMap<Team, Integer> getTeamPointsMap();

    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {

        //Get list of teamPoints
        HashMap<Team, Integer> teamPoints = getTeamPointsMap();

        //Created ordered team list based on wins
        ArrayList<Team> orderedTeamList = getOrderedTeamList(teamPoints);

        //Get the requested count of top teams from that list
        return getTopTeamsFromOrderedList(count, tieBreaker, orderedTeamList, teamPoints);
    }

    /** Get a requested about of top players.
     * @param count the top number of players you request.
     * @param tieBreaker the tiebreaker to be used by method.
     * @param teamPointsOrderList a list of the teams.
     * @param teamPoints hashMap containing the given teams and their points.
     * @return a list containing the requested amount of top players. */
    private ArrayList<Team> getTopTeamsFromOrderedList(int count, TieBreaker tieBreaker, ArrayList<Team> teamPointsOrderList, HashMap<Team, Integer> teamPoints){

        if(teams.size() <= count) //Is the requested count larger then the count of teams?
            return new ArrayList<>(teamPointsOrderList);
        else {
            if (teamPoints.get(teamPointsOrderList.get(count - 1)).equals(teamPoints.get(teamPointsOrderList.get(count)))) {

                //TIE BREAKING!

                ArrayList<Team> topTeamsList = new ArrayList<>();

                //Find the teams that are tied
                ArrayList<Team> tiedTeams = new ArrayList<>();
                for (Team team : teamPointsOrderList)
                    if (teamPoints.get(team) == teamPoints.get(teamPointsOrderList.get(count - 1))) //Does the current team has the same points as the for sure tied one.
                        tiedTeams.add(team);

                //Create list with the topteams down untill and without the tied teams
                for (Team team : teamPointsOrderList) {

                    if (teamPoints.get(team) == teamPoints.get(teamPointsOrderList.get(count - 1)))
                        break;
                    else
                        topTeamsList.add(team);
                }

                //Get list of tie broken teams
                ArrayList<Team> tieBrokenTeams = new ArrayList<>(tieBreaker.compareAll(tiedTeams, tiedTeams.size()));

                //Fill the topteamsList with the remaining needed count of teams from the tie broken teams
                while (topTeamsList.size() < count) {
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

    /** Creates an ordered list of the teams based on the points.
     * @param teamPoints
     * @return an ordered list of the current teams*/
    private ArrayList<Team> getOrderedTeamList(HashMap<Team, Integer> teamPoints){

        ArrayList<Team> teamPointsOrderedList = new ArrayList<>();
        ArrayList<Team> tempTeamsList = new ArrayList<>(teams);

        while(tempTeamsList.size() != 0){
            Team teamWithMostPoints = tempTeamsList.get(0);

            //Find the team with the most points
            for(Team team : tempTeamsList){
                if(teamPoints.get(team) > teamPoints.get(teamWithMostPoints))
                    teamWithMostPoints = team;
            }

            teamPointsOrderedList.add(teamWithMostPoints);
            tempTeamsList.remove(teamWithMostPoints);
        }

        return teamPointsOrderedList;
    }

    public abstract List<Match> getAllMatches();

    @Override
    public ArrayList<Match> getCompletedMatches() {

        ArrayList<Match> allMatches = new ArrayList<>(getAllMatches());
        ArrayList<Match> playedMatches = new ArrayList<>();

        for (Match match : allMatches)
            if (match.hasBeenPlayed())
                playedMatches.add(match);

        return playedMatches;
    }

    /** All created matches can be played in swiss.
     * @return an empty ArrayList<Match>*/
    @Override
    public List<Match> getPendingMatches() {
        return new ArrayList<>();
    }

    @Override
    public List<Match> getUpcomingMatches() {

        List<Match> allMatches = getAllMatches();
        ArrayList<Match> upComingMatches = new ArrayList<>();

        for (Match match : allMatches)
            if (!match.hasBeenPlayed())
                upComingMatches.add(match);

        return upComingMatches;
    }

    @Override
    public StageStatus getStatus() {
        return status;
    }
}
