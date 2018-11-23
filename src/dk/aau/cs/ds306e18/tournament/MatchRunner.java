package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.utility.ConfigFileEditor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class MatchRunner {

    private boolean matchIsRunning = false;
    private Match runningMatch = null;

    public static boolean startMatch(Match match) {
        if (!RLBotSettings.isConfigValid())
            return false;

        try {
            // Set up config file
            ConfigFileEditor.readConfig(RLBotSettings.getConfigPath());
            ConfigFileEditor.configureMatch(match);
            ConfigFileEditor.writeConfig(RLBotSettings.getConfigPath());

            // We assume that the runner is in the same folder as the config
            Path path = Paths.get(RLBotSettings.getConfigPath()).getParent();
            String command = "python \"" + path.toString() + "/run.py\"";
            System.out.println("command: " + command);
            Process p = Runtime.getRuntime().exec(command);

            try(BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;

                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (Exception err) {
            err.printStackTrace();
        }

        return true;
    }
}
