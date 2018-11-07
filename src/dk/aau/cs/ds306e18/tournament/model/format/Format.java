package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.StageStatus;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreaker;
import dk.aau.cs.ds306e18.tournament.ui.controllers.BracketOverviewTabController;
import javafx.scene.Node;

import java.util.List;

public interface Format {

    /** Starts the stage with the given list of teams. The teams should seeded after the order in the list. */
    void start(List<Team> seededTeams);

    /** Returns the status of the format. */
    StageStatus getStatus();

    /** Returns a list of the teams that performed best this stage. They are sorted after performance, with best team first. */
    List<Team> getTopTeams(int count, TieBreaker tieBreaker);

    /** Returns a list of all the matches in this stage. */
    List<Match> getAllMatches();

    /** Returns a list of all the matches that are ready to be played, but haven't played yet. */
    List<Match> getUpcomingMatches();

    /** Returns a list of all planned matches, that can't be played yet. */
    List<Match> getPendingMatches();

    /** Returns a list of all the matches that have been played. */
    List<Match> getCompletedMatches();

    /** Returns a Node of the stage. This node contains a reference to it self and other functionality to display the stage.*/
    Node getJavaFxNode(BracketOverviewTabController bracketOverview);
}
