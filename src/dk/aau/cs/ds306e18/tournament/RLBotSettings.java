package dk.aau.cs.ds306e18.tournament;

import java.io.File;

public class RLBotSettings {

    private static String configPath = "";
    private static boolean configValid = false;

    public static String getConfigPath() {
        return configPath;
    }

    /** Set the path to the config file of the RLBot framework and checks if it is a valid config file. */
    public static void setConfigPath(String path) {
        configPath = path == null ? "" : path;
        configValid = isValidConfigFile(path);
    }

    /** Returns true if the saved config file is valid */
    public static boolean isConfigValid() {
        return configValid;
    }

    /** Returns true of the given path is a valid config file */
    public static boolean isValidConfigFile(String path) {
        File file = new File(path);
        if (!file.isFile())
            return false;

        if (!file.getPath().endsWith(".cfg"))
            return false;

        // TODO Other checks

        return true;
    }
}
