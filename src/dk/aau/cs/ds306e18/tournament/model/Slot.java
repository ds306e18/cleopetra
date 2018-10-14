package dk.aau.cs.ds306e18.tournament.model;

/** Matches has two Slots. A Slot contains the Team for the Match. The Team in a Slot is not always known
 * from the start, e.g. when the Team is the winner of another Match.
 * The method {@code isReady()} will mark when the Team is known. */
public interface Slot {

    /** Returns true when are required Matches has been player. */
    boolean isReady();
    /** Get the Team contained in this Slot. Can be null if required Matches has not been played. */
    Team getTeam();
    /** The match, that the Team of the Slot is dependent on */
    Match getRequiredMatch();
}
