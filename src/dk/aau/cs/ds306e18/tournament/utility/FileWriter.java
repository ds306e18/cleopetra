package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.model.Tournament;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static dk.aau.cs.ds306e18.tournament.utility.Serializer.deserialise;
import static dk.aau.cs.ds306e18.tournament.utility.Serializer.serialize;

public class FileWriter {

    private final static String stateFilename = "tournamentState.obj";
    private final static String filenameExtension = ".obj";

    private final static String matchEndOfLinePattern = "[^/]*$";
    private final static String matchFilenamePrefixPattern = "(/";
    private final static String matchFilenamePostfixPattern = ")";
    private final static String matchExtensionDefaultPattern = ".*(\\.obj)";
    private final static String matchExtensionPrefixPattern = "[.";
    private final static String matchExtensionPostfixPattern = "]*$";
    private final static String matchDotPattern = "[\\.]";
    private final static String matchFilenameWithDotPattern = ".*(\\.).*";
    private final static String matchDotAndExtensionPattern = "\\.(.*)";

    /**
     * Writes serialised Tournament object to .obj-file with JSON-data
     *
     * @param fqn        the Fully Qualified Name for the directory
     * @param filename   the custom filename wished to used
     * @param tournament the given tournament to serialize and write to disk
     * @return boolean of success
     */
    public static boolean writeToFilesystem(String fqn, String filename, String extension, Tournament tournament) {
        String pathFilename = checkFilenameInString(checkTrailingSlash(fqn), checkFilename(filename, extension));
        return writeSerializedTournament(pathFilename, tournament);
    }

    /**
     * Writes serialised Tournament object to .obj-file with JSON-data
     *
     * @param fqn        the Fully Qualified Name for the directory
     * @param filename   the custom filename wished to used
     * @param tournament the given tournament to serialize and write to disk
     * @return boolean of success
     */
    public static boolean writeToFilesystem(String fqn, String filename, Tournament tournament) {
        String pathFilename = checkFilenameInString(checkTrailingSlash(fqn), checkFilename(filename));
        return writeSerializedTournament(pathFilename, tournament);
    }

    /**
     * Writes serialised Tournament object to .obj-file with JSON-data
     *
     * @param fqn        the Fully Qualified Name for the directory
     * @param tournament the given tournament to serialize and write to disk
     * @return boolean of success
     */
    public static boolean writeToFilesystem(String fqn, Tournament tournament) {
        String pathFilename = checkFilenameInString(checkTrailingSlash(fqn), stateFilename);
        return writeSerializedTournament(pathFilename, tournament);

    }

    /**
     * Writes serialized tournament to path given by String and Tournament object passed
     *
     * @param pathFilename the String of the given path to write object to
     * @param tournament   the given Tournament object to serialize
     * @return boolean of success
     */
    private static boolean writeSerializedTournament(String pathFilename, Tournament tournament) {
        Path path = Paths.get(pathFilename);
        try {
            // call serialize and getBytes from returned string, writing with no options to given path, thus
            // creating, writing and closing if no exception is caught or overwriting with same procedure if object exists
            Files.write(path, serialize(tournament).getBytes());
            return true;
        } catch (IOException e) {
            System.out.println("ERROR: Caught IOException when writing to " + pathFilename + ". " + e.getMessage());
        }
        return false;
    }

    /**
     * Reads serialized Tournament object from filesystem, deserializes it, and returns it
     *
     * @param fqn       the Fully Qualified Name for the directory
     * @param filename  the custom filename given
     * @param extension the custom extension given
     * @return a Tournament object if successful, else null
     */
    public static Tournament readFromFilesystem(String fqn, String filename, String extension) {
        return readSerializedTournament(checkFilenameInString(checkTrailingSlash(fqn), checkFilename(filename, extension)));
    }

    /**
     * Reads serialized Tournament object from filesystem, deserializes it, and returns it
     *
     * @param fqn      the Fully Qualified Name for the directory
     * @param filename the custom filename given
     * @return a Tournament object if successful, else null
     */
    public static Tournament readFromFilesystem(String fqn, String filename) {
        return readSerializedTournament(checkFilenameInString(checkTrailingSlash(fqn), checkFilename(filename)));
    }


    /**
     * Reads serialized Tournament object from filesystem, deserializes it, and returns it
     *
     * @param fqn the Fully Qualified Name for the directory
     * @return a Tournament object if successful, else null
     */
    public static Tournament readFromFilesystem(String fqn) {
        return readSerializedTournament(checkFilenameInString(checkTrailingSlash(fqn), stateFilename));
    }

    /**
     * Reads serialized Tournament object from filesystem, deserializes it, and returns it
     *
     * @param pathFilename the Fully Qualified Name for the location
     * @return deserialized Tournament object if successful, or null if error occurred
     */
    public static Tournament readSerializedTournament(String pathFilename) {
        Path path = Paths.get(pathFilename);

        try {
            // read all bytes from path and create string from input,
            // passing to deserialize and returning newly created Tournament
            return deserialise(new String(Files.readAllBytes(path)));
        } catch (IOException e) {
            System.out.println("ERROR: Caught IOException when reading from " + pathFilename + ". " + e.getMessage());
        }
        // else return null, as something must've gone wrong
        return null;
    }

    /**
     * Checks for trailing slash at end of argument string
     *
     * @param fqn the Fully Qualified Name
     * @return returns the string fqn with a trailing slash, if not already in string
     */
    public static String checkTrailingSlash(String fqn) {
        // if no dir supplied, just return given argument
        if (fqn.length() == 0) return fqn;
        if (fqn.charAt(fqn.length() - 1) == '/') return fqn;
        else return fqn + '/';
    }

    /**
     * Checks filename for default extension, and appends it if missing
     *
     * @param filename the given filename
     * @return the filename with correct extension
     */
    public static String checkFilename(String filename) {
        if (filename.matches(matchExtensionDefaultPattern)) return filename;
        return filename + filenameExtension;
    }

    /**
     * Checks filename for correctly formed extension, and appends it if missing. Also removes any extension given in filename.
     *
     * @param filename  the given filename
     * @param extension the given extension
     * @return the filename with a correct extension
     */
    public static String checkFilename(String filename, String extension) {
        // if a dot is found in given filename, remove everything from end and including dot
        if (filename.matches(matchFilenameWithDotPattern))
            filename = filename.replaceAll(matchDotAndExtensionPattern, "");

        //if filename matches ". 'extension' EOL", then simply return
        if (filename.matches(matchExtensionPrefixPattern + extension + matchExtensionPostfixPattern)) return filename;
        // handle dot delimiter
        else if (!(filename.matches(matchDotPattern))) return filename + "." + extension;
        else return filename + extension;
    }

    /**
     * Checks for filename in path, regex matches end of line, slash immediately before given filename, and filename
     *
     * @param fqn      the Fully Qualified Name
     * @param filename the filename of the given object
     * @return the path (fqn + filename) corrected if necessary
     */
    public static String checkFilenameInString(String fqn, String filename) {
        if (fqn.matches(matchFilenamePrefixPattern + filename
                + matchFilenamePostfixPattern + matchEndOfLinePattern)) return fqn;
        else return fqn + filename;
    }
}

