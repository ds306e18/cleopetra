package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.participants.Team;

/** Matches has two Slots. A Slot contains the Team for the Match. The Team in a Slot is not always known
 * from the start, e.g. when the Team is the winner of another Match.
 * The method {@code isReady()} will mark when the Team is known. */
public interface Slot {

    boolean isReady();
    Team getTeam();
}
