package dk.aau.cs.ds306e18.tournament.rlbot;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotFromConfig;
import dk.aau.cs.ds306e18.tournament.model.BotSkill;
import dk.aau.cs.ds306e18.tournament.model.PsyonixBotFromConfig;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.TreeSet;

import static dk.aau.cs.ds306e18.tournament.CleoPetraSettings.*;

public class BotCollection extends TreeSet<Bot> {

    public static BotCollection global = new BotCollection();

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
    }

    /**
     * Add the default Psyonix bots to the bot collection. The bots will be loaded from the CleoPetra settings folder.
     *
     * @return returns true if succeeded.
     */
    public boolean addPsyonixBots() {
        try {
            copyPsyonixBotsToSettingsFolder();

            // Bots starting in the bot collection
            Path allStarPath = getPathToSettingsFolder().resolve(PSYONIX_BOTS_FOLDER).resolve(PSYONIX_ALLSTAR_FILE_NAME);
            PsyonixBotFromConfig allstar = new PsyonixBotFromConfig(allStarPath.toString(), BotSkill.ALLSTAR);
            Path proPath = getPathToSettingsFolder().resolve(PSYONIX_BOTS_FOLDER).resolve(PSYONIX_PRO_FILE_NAME);
            PsyonixBotFromConfig pro = new PsyonixBotFromConfig(proPath.toString(), BotSkill.PRO);
            Path rookiePath = getPathToSettingsFolder().resolve(PSYONIX_BOTS_FOLDER).resolve(PSYONIX_ROOKIE_FILE_NAME);
            PsyonixBotFromConfig rookie = new PsyonixBotFromConfig(rookiePath.toString(), BotSkill.ROOKIE);

            addAll(Arrays.asList(
                    allstar, pro, rookie
            ));

            return true;

        } catch (Exception e) {
            // Something went wrong. Report it, but continue
            System.err.println("Could not load default bots.");

            return false;
        }
    }

    /**
     * The RLBotGUI is able to download a pack filled with bots. If that pack is present, load it into the
     * bot collection.
     *
     * @return returns true if succeeded.
     */
    public boolean addRLBotPackIfPresent() {
        try {
            // Get path to BotPack and try to load bots
            Path rlbotpackPath = getPathToRLBotPack();
            if (rlbotpackPath != null && Files.exists(rlbotpackPath)) {
                System.out.println("Loading bots from RLBotGUI's BotPack.");
                return addAllBotsFromFolder(rlbotpackPath.toFile(), 10);
            }
        } catch (Exception e) {
            // Something went wrong. Report it, but continue
            System.err.println("Could not load bots for RLBotGUI's BotPack. Something went wrong.");
        }

        return false;
    }

    /**
     * Looks through a folder and all its sub-folders and adds all bots found to the bot collection. The bot must be a
     * bot config file that is a valid BotFromConfig bot. The method also provides a max depth that in can go in
     * sub-folders which ensures the search doesn't take too long.
     * @param folder The folder to be checked.
     * @param maxDepth the maximum depth that method can go in folders.
     *
     * @return returns true if a bot was found and added to the bot collection.
     */
    public boolean addAllBotsFromFolder(File folder, int maxDepth) {
        boolean addedSomething = false;
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory() && maxDepth > 0) {
                        // Check sub-folders using recursion
                        addedSomething = addAllBotsFromFolder(file, maxDepth - 1) || addedSomething;

                    } else if ("cfg".equals(getFileExtension(file))) {
                        try {
                            // Try to read bot
                            BotFromConfig bot = new BotFromConfig(file.getAbsolutePath());
                            if (bot.loadedCorrectly()) {
                                this.add(bot);
                                addedSomething = true;
                            }

                        } catch (Exception e) {
                            // Failed
                            System.out.println("Could not parse " + file.getName() + " as a bot.");
                        }
                    }
                }
            }
        }
        return addedSomething;
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
