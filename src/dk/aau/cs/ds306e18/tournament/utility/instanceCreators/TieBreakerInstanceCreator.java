package dk.aau.cs.ds306e18.tournament.utility.instanceCreators;

import com.google.gson.InstanceCreator;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.TieBreakerBySeed;

import java.lang.reflect.Type;

public class TieBreakerInstanceCreator implements InstanceCreator <TieBreaker> {
    public TieBreaker createInstance(Type type) {
        return new TieBreakerBySeed();
    }
}
