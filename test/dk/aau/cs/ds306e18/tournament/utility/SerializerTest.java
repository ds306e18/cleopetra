package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Stage;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.format.DoubleEliminationFormat;
import dk.aau.cs.ds306e18.tournament.model.format.RoundRobinFormat;
import dk.aau.cs.ds306e18.tournament.model.format.SingleEliminationFormat;
import dk.aau.cs.ds306e18.tournament.model.format.SwissFormat;
import dk.aau.cs.ds306e18.tournament.serialization.Serializer;
import org.junit.Test;

import static dk.aau.cs.ds306e18.tournament.TestUtilities.*;
import static org.junit.Assert.assertEquals;

public class SerializerTest {

    /**
     * Test for serialising a Tournament object, serializes it to a JSON-string, deserializes it to
     * a Tournament object, and checks equality between the original object and the resuscitated object
     */
    @Test
    public void serializingPrimitiveTournamentConcurrencyTest() {
        Tournament tournament = generateTournamentWithTeams(4, 1);
        String jsonObject = Serializer.serialize(tournament);
        Tournament deserializedTournament = Serializer.deserialize(jsonObject);

        assertEquals(tournament, deserializedTournament);
    }

    /**
     * Test for serialising a Round Robin Tournament object, serializes it to a JSON-string, deserializes it to
     * a Tournament object, and checks equality between the original object and the resuscitated object
     */
    @Test
    public void serializingRoundRobinConcurrencyTest() {
        Tournament tournament = generateTournamentWithTeams(4, 1);
        tournament.addStage(new Stage("Round Robin Stage", new RoundRobinFormat()));
        tournament.start();

        String jsonObject = Serializer.serialize(tournament);
        Tournament deserializedTournament = Serializer.deserialize(jsonObject);

        assertEquals(tournament, deserializedTournament);
    }

    /**
     * Test for serialising a Single Elimination Tournament object, serializes it to a JSON-string, deserializes it
     * to a Tournament object, and checks equality between the original object and the resuscitated object
     */
    @Test
    public void serializingSingleEliminationConcurrencyTest() {
        Tournament tournament = generateTournamentWithTeams(4, 1);
        tournament.addStage(new Stage("Single Elimination Stage", new SingleEliminationFormat()));
        tournament.start();

        String jsonObject = Serializer.serialize(tournament);
        Tournament deserializedTournament = Serializer.deserialize(jsonObject);

        assertEquals(tournament, deserializedTournament);
    }

    /**
     * Test for serialising a Swiss Tournament object, serializes it to a JSON-string, deserializes it to
     * a Tournament object, and checks equality between the original object and the resuscitated object
     */
    @Test
    public void serializingSwissConcurrencyTest() {
        Tournament tournament = generateTournamentWithTeams(4, 1);
        tournament.addStage(new Stage("Swiss Stage", new SwissFormat()));
        tournament.start();

        String jsonObject = Serializer.serialize(tournament);
        Tournament deserializedTournament = Serializer.deserialize(jsonObject);

        assertEquals(tournament, deserializedTournament);
    }

    /**
     * Test for serialising a Double Elimination Tournament object, serializes it to a JSON-string, deserializes it to
     * a Tournament object, and checks equality between the original object and the resuscitated object
     */
    @Test
    public void serializingDoubleEliminationConcurrencyTest() {
        Tournament tournament = generateTournamentWithTeams(4, 1);
        tournament.addStage(new Stage("Double Elimination Stage", new DoubleEliminationFormat()));
        tournament.start();

        String jsonObject = Serializer.serialize(tournament);
        Tournament deserializedTournament = Serializer.deserialize(jsonObject);

        assertEquals(tournament, deserializedTournament);
    }
}