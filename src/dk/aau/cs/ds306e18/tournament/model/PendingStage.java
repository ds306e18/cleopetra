package dk.aau.cs.ds306e18.tournament.model;

import java.util.List;

public interface PendingStage {

    int getNumberOfTeamsWanted();
    Stage start(List<Team> seededTeams);
}
