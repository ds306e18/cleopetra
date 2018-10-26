package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import org.junit.Test;

import static dk.aau.cs.ds306e18.tournament.TestUtilities.generateTournament;
import static org.junit.Assert.assertEquals;

public class SerializeManagerTest {

    /**
     * Test-class for serialising a randomly generated Tournament object, serializes it to a JSON-string, deserializes
     * it to a Tournament object, and checks equality between the original object and the resuscitated object
     */
    @Test
    public void serializingConcurrencyTest() {
        Tournament tournament = generateTournament();
        String jsonObject = SerializeManager.serialize(tournament);
        Tournament deserializedTournament = SerializeManager.deserialise(jsonObject);

        assertEquals(tournament, deserializedTournament);
    }
}