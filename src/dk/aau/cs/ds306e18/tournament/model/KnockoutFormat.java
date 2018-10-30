package dk.aau.cs.ds306e18.tournament.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class KnockoutFormat implements Format {

    public ArrayList<Team> teams;

    abstract HashMap<Team, Integer> getTeamPointsMap();

    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {

        //Get list of teamPoints
        HashMap<Team, Integer> teamPoints = getTeamPointsMap();

        //Created ordered team list based on wins
        ArrayList<Team> orderedTeamList = getOrderedTeamList(teams, teamPoints);

        //Get the requested count of top teams from that list
        return getTopTeamsFromOrderedList(count, tieBreaker, orderedTeamList, teamPoints);

    }

    public ArrayList<Team> getTopTeamsFromOrderedList(int count, TieBreaker tieBreaker, ArrayList<Team> teamPointsOrderList, HashMap<Team, Integer> teamPoints){

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

    public ArrayList<Team> getOrderedTeamList(ArrayList<Team> teams, HashMap<Team, Integer> teamPoints){

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
}
