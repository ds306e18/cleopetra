package dk.aau.cs.ds306e18.tournament.utility;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotFromConfig;
import dk.aau.cs.ds306e18.tournament.model.BotSkill;
import dk.aau.cs.ds306e18.tournament.model.PsyonixBot;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.TreeSet;

public class BotCollection extends TreeSet<Bot> {

    public BotCollection() {
        super((a, b) -> {
            if (a == b) return 0;
            // Sort by bot type
            int diff = a.getBotType().ordinal() - b.getBotType().ordinal();
            if (diff == 0) {
                // If type is the same, sort by bot name
                return a.getName().compareTo(b.getName());
            }
            return diff;
        });

        addPsyonixBots();
        addRLBotPackIfPresent();
    }

    /**
     * Add the default Psyonix bots to the bot collection.
     */
    private void addPsyonixBots() {
        try {
            // Bots starting in the bot collection
            URL allstarURL = Main.class.getResource("rlbot/psyonix_allstar.cfg");
            PsyonixBot allstar = new PsyonixBot(Paths.get(allstarURL.toURI()).toString(), BotSkill.ALLSTAR);
            URL proURL = Main.class.getResource("rlbot/psyonix_pro.cfg");
            PsyonixBot pro = new PsyonixBot(Paths.get(proURL.toURI()).toString(), BotSkill.PRO);
            URL rookieURL = Main.class.getResource("rlbot/psyonix_rookie.cfg");
            PsyonixBot rookie = new PsyonixBot(Paths.get(rookieURL.toURI()).toString(), BotSkill.ROOKIE);

            addAll(Arrays.asList(
                    allstar, pro, rookie
            ));

        } catch (Exception e) {
            // Something went wrong. Report it, but continue
            System.err.println("Could not load default bots.");
        }
    }

    /**
     * The RLBotGUI is able to download a pack filled with bots. If that pack is present, load it into the
     * bot collection.
     */
    private void addRLBotPackIfPresent() {
        try {
            // Get path to BotPack and try to load bots
            File rlbotpack = Paths.get(System.getenv("APPDATA")).getParent().resolve("Local\\RLBotGUI\\RLBotPack").toFile();
            if (rlbotpack.exists()) {
                System.out.println("Loading bots from RLBotGUI's BotPack.");
                addAllBotsFromFolder(rlbotpack, 10);
            }
        } catch (Exception e) {
            // Something went wrong. Report it, but continue
            System.err.println("Could not load bots for RLBotGUI's BotPack.");
        }
    }

    /**
     * Looks through a folder and all its sub-folders and adds all bots found to the bot collection. The bot must be a
     * bot config file that is a valid BotFromConfig bot. The method also provides a max depth that in can go in
     * sub-folders which ensures the search doesn't take too long.
     * @param folder The folder to be checked.
     * @param maxDepth the maximum depth that method can go in folders.
     */
    public void addAllBotsFromFolder(File folder, int maxDepth) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory() && maxDepth > 0) {
                        // Check sub-folders using recursion
                        addAllBotsFromFolder(file, maxDepth - 1);

                    } else if ("cfg".equals(getFileExtension(file))) {
                        try {
                            // Try to read bot
                            BotFromConfig bot = new BotFromConfig(file.getAbsolutePath());
                            if (bot.isValidConfig()) {
                                this.add(bot);
                            }

                        } catch (Exception e) {
                            // Failed
                            System.out.println("Could not parse " + file.getName() + " as a bot.");
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the file extension of a file. E.g. "folder/botname.cfg" returns "cfg". The method should also support
     * folders with dots in their name.
     */
    private String getFileExtension(File file) {
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
