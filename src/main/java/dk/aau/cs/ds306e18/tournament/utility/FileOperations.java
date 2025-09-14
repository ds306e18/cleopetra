package dk.aau.cs.ds306e18.tournament.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileOperations {

    /**
     * Returns the file extension of a file. E.g. "folder/botname.cfg" returns "cfg". The method should also support
     * folders with dots in their name.
     */
    public static String getFileExtension(File file) {
        String name = file.getName();
        int i = name.lastIndexOf('.');
        int p = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));

        if (i > p) {
            return name.substring(i + 1);
        } else {
            return "";
        }
    }
}

