package dk.aau.cs.ds306e18.tournament;

import com.sun.javafx.robot.FXRobot;
import dk.aau.cs.ds306e18.tournament.model.match.Match;
import dk.aau.cs.ds306e18.tournament.utility.ConfigFileEditor;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MatchRunner {

    private static MatchProcess matchProcess;

    private static boolean matchIsRunning = false;
    private static Match runningMatch = null;

    /** Starts the given match in Rocket League. */
    public static boolean startMatch(Match match) {
        if (matchIsRunning)
            throw new IllegalStateException("A Match is already running.");

        if (!RLBotSettings.isConfigValid())
            return false;

        try {
            // Set up config file
            ConfigFileEditor.readConfig(RLBotSettings.getConfigPath());
            ConfigFileEditor.configureMatch(match);
            ConfigFileEditor.writeConfig(RLBotSettings.getConfigPath());

            matchProcess = new MatchProcess();
            Thread t = new Thread(matchProcess);
            t.start();

            matchIsRunning = true;
            runningMatch = match;
            return true;

        } catch (Exception err) {
            err.printStackTrace();
        }

        return true;
    }

    /** Stops the currently running match. */
    public static boolean stopMatch() {
        if (matchProcess == null)
            return true;

        matchProcess.stop();

        matchIsRunning = false;
        runningMatch = null;
        matchProcess = null;

        System.out.println("Match stopped successfully");
        return true;
    }

    public static boolean isMatchRunning() {
        return matchIsRunning;
    }

    public static Match getRunningMatch() {
        return runningMatch;
    }



    private static class MatchProcess implements Runnable {

        private Process process;

        /** Starts the match process and opens the rlbot framework in that process. */
        @Override
        public void run() {

            try {
                // We assume that the runner is in the same folder as the config
                Path path = Paths.get(RLBotSettings.getConfigPath()).getParent();
                String command = "cmd.exe /c start cmd.exe /c \"python \"" + path.toString() + "\\run.py\"\"";
                System.out.println("command: " + command);
                process = Runtime.getRuntime().exec(command);

                try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;

                    while ((line = input.readLine()) != null) {
                        // Loop is exited when process is killed
                        System.out.println(line);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /** Stops the match process and the rlbot framework. */
        public void stop() {
            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                out.write("q");
                out.write("q");
                out.write("q");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            process.destroy();
            while (process.isAlive()) {
                System.out.println("Waiting for match process to die.");
            }
        }
    }
}
