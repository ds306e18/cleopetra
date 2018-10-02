package dk.aau.cs.ds306e18.tournament.participants;

import java.nio.file.Path;
import java.util.ArrayList;

public interface Participant {

    String getName();
    int getWins();
    int getLosses();
    int getMatchesPlayed();
    double getRanking();
    ArrayList<Path> getConfigPath();

}
