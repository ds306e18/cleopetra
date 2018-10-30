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
    public void readFromFilesystem1() {
        Tournament tournament = generateTournament();
        assertTrue(FileWriter.writeToFilesystem("", tournament));
        Tournament reserializedTournament = FileWriter.readFromFilesystem("");
        assertEquals(reserializedTournament, tournament);
    }

    @Test
    public void readFromFilesystem2() {
        Tournament tournament = generateTournament();
        assertTrue(FileWriter.writeToFilesystem("", "filename", tournament));
        Tournament reserializedTournament = FileWriter.readFromFilesystem("", "filename");
        assertEquals(reserializedTournament, tournament);
    }

    @Test
    public void readFromFilesystem3() {
        Tournament tournament = generateTournament();
        assertTrue(FileWriter.writeToFilesystem("", "filename", "object", tournament));
        Tournament reserializedTournament = FileWriter.readFromFilesystem("", "filename", "object");
        assertEquals(tournament, reserializedTournament);
    }


    @Test
    public void checkTrailingSlash1() {
        assertEquals("", FileWriter.checkTrailingSlash(""));
    }

    @Test
    public void checkTrailingSlash2() {
        assertEquals("/home/user/location/dir/", FileWriter.checkTrailingSlash("/home/user/location/dir/"));
    }

    @Test
    public void checkTrailingSlash3() {
        assertEquals("/home/user/location/dir/", FileWriter.checkTrailingSlash("/home/user/location/dir"));
    }

    @Test
    public void checkFilename() {
        assertEquals("filename.obj", FileWriter.checkFilename("filename.obj"));
        assertEquals("filename.obj", FileWriter.checkFilename("filename"));
        assertEquals("filename.txt.obj", FileWriter.checkFilename("filename.txt"));

    }

    @Test
    public void checkFilename1() {
        assertEquals("filename.txt", FileWriter.checkFilename("filename", "txt"));
        assertEquals("filename.object", FileWriter.checkFilename("filename", "object"));
        assertEquals("filename.object.object", FileWriter.checkFilename("filename.object", "object"));
    }

    @Test
    public void checkFilenameInString() {

    }
}