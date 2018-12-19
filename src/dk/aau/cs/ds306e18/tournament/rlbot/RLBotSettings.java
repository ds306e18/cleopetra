package dk.aau.cs.ds306e18.tournament.rlbot;

import java.io.File;
import java.util.Objects;

public class RLBotSettings {

    private String configPath = "";
    private boolean configValid = false;

    public String getConfigPath() {
        return configPath;
    }

    /** Set the path to the config file of the RLBot framework and checks if it is a valid config file. */
    public void setConfigPath(String path) {
        configPath = path == null ? "" : path;
        configValid = isValidConfigFile(path);
    }

    /** Returns true if the saved config file is valid */
    public boolean isConfigValid() {
        return configValid;
    }

    /** Returns true of the given path is a valid config file */
    public static boolean isValidConfigFile(String path) {
        File file = new File(path);
        if (!file.isFile())
            return false;

        if (!file.getPath().endsWith(".cfg"))
            return false;


        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RLBotSettings that = (RLBotSettings) o;
        return configValid == that.configValid &&
                Objects.equals(configPath, that.configPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configPath, configValid);
    }
}
