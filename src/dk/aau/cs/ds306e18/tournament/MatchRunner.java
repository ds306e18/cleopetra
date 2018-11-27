package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.utility.ConfigFileEditor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MatchRunner {

    // both %s will be replaced with the directory of the rlbot.cfg
    private static final String COMMAND_FORMAT = "cmd.exe /c start cmd.exe /c \"cd %s & python \"%s\\run.py\"\"";

    /** Starts the given match in Rocket League. */
    public static boolean startMatch(RLBotSettings settings, Match match) {
        if (!settings.isConfigValid())
            return false;

        try {
            // Set up config file
            ConfigFileEditor.readConfig(settings.getConfigPath());
            ConfigFileEditor.configureMatch(match);
            ConfigFileEditor.writeConfig(settings.getConfigPath());

            Thread.sleep(300);

            // We assume that the runner is in the same folder as the rlbot.cfg
            Path path = Paths.get(settings.getConfigPath()).getParent();
            String command = String.format(COMMAND_FORMAT, path, path);
            System.out.println(command);
            Runtime.getRuntime().exec(command);
            return true;

        } catch (Exception err) {
            err.printStackTrace();
        }

        return false;
    }
}
