package dk.aau.cs.ds306e18.tournament.model;

import dk.aau.cs.ds306e18.tournament.model.format.Format;
import dk.aau.cs.ds306e18.tournament.model.format.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.match.Match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GroupFormat implements Format {

    protected ArrayList<Team> teams;
    protected StageStatus status = StageStatus.PENDING;

    /** @return a hashMap containing the teams and their points. */
    public abstract HashMap<Team, Integer> getTeamPointsMap();

    @Override
    public List<Team> getTopTeams(int count, TieBreaker tieBreaker) {

        //Get list of teamPoints
        HashMap<Team, Integer> teamPoints = getTeamPointsMap();

        //Created ordered team list based on wins
        ArrayList<Team> orderedTeamList = getTeamsOrderedByPoints(teamPoints);

        //Get the requested count of top teams from that list
        return getTopTeams(count, tieBreaker, orderedTeamList, teamPoints);
    }

    /** Get a requested about of top players.
     * @param count the top number of players you request.
     * @param tieBreaker the tiebreaker to be used by method.
     * @param teamPointsOrderList a list of the teams.
     * @param pointsMap hashMap containing the given teams and their points.
     * @return a list containing the requested amount of top players. */
    private List<Team> getTopTeams(int count, TieBreaker tieBreaker, ArrayList<Team> teamPointsOrderList, HashMap<Team, Integer> pointsMap){

        if(teams.size() <= count) //Is the requested count larger then the count of teams?
            return new ArrayList<>(teamPointsOrderList);
        if (!pointsMap.get(teamPointsOrderList.get(count - 1)).equals(pointsMap.get(teamPointsOrderList.get(count))))
            return teamPointsOrderList.subList(0, count);

        // TIE BREAKING!

        int tiedTeamsPoints = pointsMap.get(teamPointsOrderList.get(count - 1));

        // Create list with the teams with more points and guaranteed to be in top teams
        List<Team> topTeams = teams.stream().filter(t -> pointsMap.get(t) > tiedTeamsPoints).collect(Collectors.toList());

        // Find the teams that are tied
        List<Team> tiedTeams = teams.stream().filter(t -> pointsMap.get(t) == tiedTeamsPoints).collect(Collectors.toList());

        // Order the list of tied teams, and get the best
        tiedTeams = tieBreaker.compareAll(tiedTeams, count - topTeams.size());
        topTeams.addAll(tiedTeams);

        return topTeams;
    }

    /** Creates an ordered list of the teams based on the points.
     * @param teamPoints a HashMap containing the points of the teams.
     * @return an ordered list of the current teams. */
    private ArrayList<Team> getTeamsOrderedByPoints(HashMap<Team, Integer> teamPoints){
        ArrayList<Team> sortedTeams = new ArrayList<>(teams);
        sortedTeams.sort((a, b) -> teamPoints.get(b) - teamPoints.get(a));
        return sortedTeams;
    }

    public abstract List<Match> getAllMatches();

    @Override
    public List<Match> getCompletedMatches() {

        List<Match> allMatches = new ArrayList<>(getAllMatches());
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
