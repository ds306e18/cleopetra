package dk.aau.cs.ds306e18.tournament.rlbot.configuration;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigFileTest {

    private final static String TEST_DIR = "test/";
    private final static String TEST_CONFIG = "config_format_test.cfg";
    private final static String TEST_CONFIG_OUT = "_out_config_format_test.cfg";

    @Test
    public void setAndGetString01() {
        ConfigFile config = new ConfigFile();
        config.set("Section", "key", "value");
        assertEquals("value", config.getString("Section", "key", null));
    }

    @Test
    public void setAndGetString02() {
        ConfigFile config = new ConfigFile();
        config.set("Some Section", "Some key", "some value");
        assertEquals("some value", config.getString("Some Section", "Some key", null));
    }

    @Test
    public void setAndGetString03() {
        ConfigFile config = new ConfigFile();
        config.set("Section", "key", "value");
        // Section does not exist
        assertEquals("default value", config.getString("Some other Section", "key", "default value"));
    }

    @Test
    public void setAndGetString04() {
        ConfigFile config = new ConfigFile();
        config.set("Section", "key", "value");
        // Key does not exist
        assertEquals("default value", config.getString("Section", "some other key", "default value"));
    }

    @Test
    public void setAndGetInt01() {
        ConfigFile config = new ConfigFile();
        config.set("Section", "key", 42);
        assertEquals(42, config.getInt("Section", "key", -1));
    }

    @Test
    public void setAndGetFloat01() {
        ConfigFile config = new ConfigFile();
        config.set("Section", "key", 0.5f);
        assertEquals(0.5f, config.getFloat("Section", "key", -1f), 1e-10f);
    }

    @Test
    public void setAndGetDouble01() {
        ConfigFile config = new ConfigFile();
        config.set("Section", "key", 1.23);
        assertEquals(1.23, config.getDouble("Section", "key", 0.0), 1e-10);
    }

    @Test
    public void setAndGetBoolean01() {
        ConfigFile config = new ConfigFile();
        config.set("Section", "key", true);
        assertTrue(config.getBoolean("Section", "key", false));
    }

    @Test
    public void load01() throws IOException {
        ConfigFile testConfig = new ConfigFile(new File(TEST_DIR, TEST_CONFIG));

        assertTrue(testConfig.hasSection("Apples"));
        assertEquals("red", testConfig.getString("Apples", "color", null));
        assertEquals(20, testConfig.getInt("Apples", "count", -1));

        assertTrue(testConfig.hasSection("Bananas"));
        assertEquals("they taste pretty good", testConfig.getString("Bananas", "multiline property", null));
        assertEquals("", testConfig.getString("Bananas", "no value", null));
        assertTrue(testConfig.getBoolean("Bananas", "other delimiter", false));

        assertTrue(testConfig.hasSection("Empty"));
    }

    @Test
    public void write01() throws IOException {
        ConfigFile testConfig = new ConfigFile(new File(TEST_DIR, TEST_CONFIG));
        testConfig.write(new File(TEST_DIR, TEST_CONFIG_OUT));

        // Load again to make sure output was readable and didn't modify any values
        testConfig = new ConfigFile(new File(TEST_DIR, TEST_CONFIG));

        assertTrue(testConfig.hasSection("Apples"));
        assertEquals("red", testConfig.getString("Apples", "color", null));
        assertEquals(20, testConfig.getInt("Apples", "count", -1));

        assertTrue(testConfig.hasSection("Bananas"));
        assertEquals("they taste pretty good", testConfig.getString("Bananas", "multiline property", null));
        assertEquals("", testConfig.getString("Bananas", "no value", null));
        assertTrue(testConfig.getBoolean("Bananas", "other delimiter", false));

        assertTrue(testConfig.hasSection("Empty"));
    }
}