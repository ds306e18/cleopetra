package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import org.junit.Test;

import static dk.aau.cs.ds306e18.tournament.TestUtilities.generateTournament;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileWriterTest {

    @Test
    public void writeToFilesystem() {
        Tournament tournament = generateTournament();
        assertTrue(FileWriter.writeToFilesystem("", tournament));

    }

    @Test
    public void readFromFilesystem() {
        Tournament tournament = generateTournament();
        assertTrue(FileWriter.writeToFilesystem("", tournament));
        Tournament reserializedTournament = FileWriter.readFromFilesystem("");
        assertEquals(reserializedTournament, tournament);
    }
}