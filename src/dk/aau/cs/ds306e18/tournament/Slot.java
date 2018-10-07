package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.participants.Participant;

/** Matches has two Slots. A Slot contains the Participant for the Match. The Participant in a Slot is not always known
 * from the start, e.g. when the Participant is the winner of another Match.
 * The method {@code isReady()} will mark when the Participant is known. */
public interface Slot {

    boolean isReady();
    Participant getTeam();
}
