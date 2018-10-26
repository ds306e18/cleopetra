package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import org.junit.Test;

import static dk.aau.cs.ds306e18.tournament.TestUtilities.generateTournament;
import static org.junit.Assert.assertEquals;

public class SerializerTest {

    /**
     * Test-class for serialising a randomly generated Tournament object, serializes it to a JSON-string, deserializes
     * it to a Tournament object, and checks equality between the original object and the resuscitated object
     */
    @Test
    public void serializingConcurrencyTest() {
        Tournament tournament = generateTournament();
        String jsonObject = Serializer.serialize(tournament);
        Tournament deserializedTournament = Serializer.deserialise(jsonObject);

        assertEquals(tournament, deserializedTournament);
    }
}