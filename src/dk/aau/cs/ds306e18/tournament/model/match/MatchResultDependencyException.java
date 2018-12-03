package dk.aau.cs.ds306e18.tournament.model.match;

/** Thrown to indicate that a match result cannot change because a subsequent match depends on it. */
public class MatchResultDependencyException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Could not change result. A subsequent match depends on current outcome.";
    }
}
