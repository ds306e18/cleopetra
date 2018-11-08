package dk.aau.cs.ds306e18.tournament.utility.instanceCreators;

import com.google.gson.InstanceCreator;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.tiebreaker.TieBreakerBySeed;

import java.lang.reflect.Type;

/**
 * InstanceCreator for TieBreaker, naïvely returns the only implemented class of TieBreakers
 */
public class TieBreakerInstanceCreator implements InstanceCreator<TieBreaker> {

    public TieBreaker createInstance(Type type) {
        return new TieBreakerBySeed();
    }
}
