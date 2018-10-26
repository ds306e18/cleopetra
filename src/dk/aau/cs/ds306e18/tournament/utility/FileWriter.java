package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Tournament;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static dk.aau.cs.ds306e18.tournament.utility.Serializer.deserialise;
import static dk.aau.cs.ds306e18.tournament.utility.Serializer.serialize;

public class FileWriter {

    private final static String filename = "tournamentState.obj";

    /**
     * Writes serialised Tournament object to .obj-file with JSON-data
     *
     * @param fqn        the Fully Qualified Name for the directory
     * @param tournament the given tournament to serialize and write to disk
     * @return boolean of success
     */
    public static boolean writeToFilesystem(String fqn, Tournament tournament) {

        //TODO; create logic for checking the fully qualified name and adding trailing forward slash

        Path path = Paths.get(fqn + filename);

        try {
            // call serialize and getBytes from returned string, writing with no options to given path, thus
            // creating, writing and closing if no exception is caught or overwriting with same procedure if object exists
            Files.write(path, serialize(tournament).getBytes());
            return true;
        } catch (IOException e) {
            System.out.println("ERROR: Caught IOException when writing to " + fqn + filename + ". " + e.getMessage());
        }
        return false;
    }

    /**
     * Reads serialized Tournament object from filesystem, deserializes it, and returns it
     *
     * @param fqn the Fully Qualified Name for the directory
     * @return a Tournament object if successful, else null
     */
    public static Tournament readFromFilesystem(String fqn) {

        //TODO; create logic for checking the fully qualified name and adding trailing forward slash

        Path path = Paths.get(fqn + filename);

        try {
            // read all bytes from path and create string from input,
            // passing to deserialize and returning newly created Tournament
            return deserialise(new String(Files.readAllBytes(path)));
        } catch (IOException e) {
            System.out.println("ERROR: Caught IOException when reading from " + fqn + filename + ". " + e.getMessage());
        }
        //else return null, as something must've gone wrong
        return null;
    }
}
