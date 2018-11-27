package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.utility.ConfigFileEditor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MatchRunner {

    // The %s will be replaced with the directory of the rlbot.cfg
    private static final String COMMAND_FORMAT = "cmd.exe /c start cmd.exe /c \"python \"%s\\run.py\"\"";

    /** Starts the given match in Rocket League. */
    public static boolean startMatch(Match match) {
        if (!RLBotSettings.isConfigValid())
            return false;

        try {
            // Set up config file
            ConfigFileEditor.readConfig(RLBotSettings.getConfigPath());
            ConfigFileEditor.configureMatch(match);
            ConfigFileEditor.writeConfig(RLBotSettings.getConfigPath());

            Thread.sleep(300);

            // We assume that the runner is in the same folder as the rlbot.cfg
            Path path = Paths.get(RLBotSettings.getConfigPath()).getParent();
            String command = String.format(COMMAND_FORMAT, path);
            System.out.println(command);
            Runtime.getRuntime().exec(command);
            return true;

        } catch (Exception err) {
            err.printStackTrace();
        }

        return false;
    }
}
