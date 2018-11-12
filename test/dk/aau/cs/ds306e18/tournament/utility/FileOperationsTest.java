package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import org.junit.Test;

import java.io.IOException;

import static dk.aau.cs.ds306e18.tournament.TestUtilities.generateTournamentOnlyTeams;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileOperationsTest {

    @Test
    public void writeToFilesystem() {
        Tournament tournament = generateTournamentOnlyTeams();
        try {
            assertTrue(FileOperations.writeTournamentToFilesystem("", tournament));
        } catch (IOException e) {
            assertFalse(true);
        }

    }

    @Test
    public void readFromFilesystem1() {
        Tournament tournament = generateTournamentOnlyTeams();
        try {
            assertTrue(FileOperations.writeTournamentToFilesystem("", tournament));
        } catch (IOException e) {
            assertFalse(true);
        }
        Tournament reserializedTournament = FileOperations.readTournamentFromFilesystem("");
        assertEquals(reserializedTournament, tournament);
    }

    @Test
    public void readFromFilesystem2() {
        Tournament tournament = generateTournamentOnlyTeams();
        try {
            assertTrue(FileOperations.writeTournamentToFilesystem("", "filename", tournament));
        } catch (IOException e) {
            assertFalse(true);
        }
        Tournament reserializedTournament = FileOperations.readTournamentFromFilesystem("", "filename");
        assertEquals(reserializedTournament, tournament);
    }

    @Test
    public void readFromFilesystem3() {
        Tournament tournament = generateTournamentOnlyTeams();
        try {
            assertTrue(FileOperations.writeTournamentToFilesystem("", "filename", "object", tournament));
        } catch (IOException e) {
            assertFalse(true);
        }
        Tournament reserializedTournament = FileOperations.readTournamentFromFilesystem("", "filename", "object");
        assertEquals(tournament, reserializedTournament);
    }

    @Test
    public void checkTrailingSlash1() {
        assertEquals("", FileOperations.checkTrailingSlash(""));
    }

    @Test
    public void checkTrailingSlash2() {
        assertEquals("/home/user/location/dir/", FileOperations.checkTrailingSlash("/home/user/location/dir/"));
    }

    @Test
    public void checkTrailingSlash3() {
        assertEquals("/home/user/location/dir/", FileOperations.checkTrailingSlash("/home/user/location/dir"));
    }

    @Test
    public void checkFilename1() {
        assertEquals("filename.obj", FileOperations.checkFilename("filename.obj"));
        assertEquals("filename.obj", FileOperations.checkFilename("filename"));
        assertEquals("filename.txt.obj", FileOperations.checkFilename("filename.txt"));
    }

    @Test
    public void checkFilename2() {
        assertEquals("filename.txt", FileOperations.checkFilename("filename", "txt"));
        assertEquals("filename.object", FileOperations.checkFilename("filename", "object"));
        assertEquals("filename.object", FileOperations.checkFilename("filename.object", "object"));
    }

    @Test
    public void checkFilename3() {
        assertEquals("filename.obj", FileOperations.checkFilename("filename.obj.obj", "obj"));
        assertEquals("filename.obj", FileOperations.checkFilename("filename.obj.obj.obj.obj", "obj"));
    }
}