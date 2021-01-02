package dk.aau.cs.ds306e18.tournament.utility;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class FileOperationsTest {

    @Test
    public void checkFilename01() {
        assertEquals("txt", FileOperations.getFileExtension(new File("filename.txt")));
        assertEquals("obj", FileOperations.getFileExtension(new File("filename.obj")));
    }

    @Test
    public void checkFilename02() {
        assertEquals("", FileOperations.getFileExtension(new File("folder/no-extension-file")));
    }

    @Test
    public void checkFilename03() {
        assertEquals("", FileOperations.getFileExtension(new File("fol.der/no-extension-file")));
    }
}