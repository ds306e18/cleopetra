package dk.aau.cs.ds306e18.tournament.rlbot;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotFromConfig;
import dk.aau.cs.ds306e18.tournament.model.PsyonixBotFromConfig;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotSkill;
import dk.aau.cs.ds306e18.tournament.settings.SettingsDirectory;
import dk.aau.cs.ds306e18.tournament.utility.FileOperations;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.TreeSet;

public class BotCollection extends TreeSet<Bot> {

    public static BotCollection global = new BotCollection();

    public BotCollection() {
        super((a, b) -> {
            if (a == b) return 0;
            // Sort by bot type
            int diff = a.getBotType().ordinal() - b.getBotType().ordinal();
            if (diff == 0) {
                // If type is the same, sort by bot name
                diff = a.getName().compareTo(b.getName());
                if (diff == 0) {
                    // If name is the same, sort by path
                    diff = a.getConfigPath().compareTo(b.getConfigPath());
                }
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
            // Bots starting in the bot collection
            PsyonixBotFromConfig allstar = new PsyonixBotFromConfig(SettingsDirectory.PSYONIX_ALLSTAR.toString(), BotSkill.ALLSTAR);
            PsyonixBotFromConfig pro = new PsyonixBotFromConfig(SettingsDirectory.PSYONIX_PRO.toString(), BotSkill.PRO);
            PsyonixBotFromConfig rookie = new PsyonixBotFromConfig(SettingsDirectory.PSYONIX_ROOKIE.toString(), BotSkill.ROOKIE);

            addAll(Arrays.asList(
                    allstar, pro, rookie
            ));

            return true;

        } catch (Exception e) {
            // Something went wrong. Not severe.
            Main.LOGGER.log(System.Logger.Level.ERROR, "Failed to load Psyonix bots.", e);

            return false;
        }
    }

    /**
     * The RLBotGUI is able to download a pack filled with bots. If that pack is present, load it into the
     * bot collection.
     *
     * @return returns the number of bots loaded or empty optional if the bot pack was not found.
     */
    public Optional<Integer> addRLBotPackIfPresent() {
        try {
            // Get path to BotPack and try to load bots
            Path rlbotpackPath = RLBotInstallation.getPathToRLBotPack();
            if (rlbotpackPath != null && Files.exists(rlbotpackPath)) {
                Main.LOGGER.log(System.Logger.Level.INFO, "Loading bots from RLBotGUI's BotPack.");
                return Optional.of(addAllBotsFromFolder(rlbotpackPath.toFile(), 10));
            } else {
                Main.LOGGER.log(System.Logger.Level.INFO, "RLBotGUI's BotPack does not exist.");
            }
        } catch (Exception e) {
            // Something went wrong. Report it, but continue
            Main.LOGGER.log(System.Logger.Level.ERROR, "Could not load bots for RLBotGUI's BotPack. Something went wrong.", e);
        }

        return Optional.empty() ;
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
    public int addAllBotsFromFolder(File folder, int maxDepth) {
        int botsAdded = 0;
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory() && maxDepth > 0) {
                        // Check sub-folders using recursion
                        botsAdded += addAllBotsFromFolder(file, maxDepth - 1);

                    } else if (file.getName().endsWith("bot.toml")) {
                        try {
                            // Try to read bot
                            BotFromConfig bot = new BotFromConfig(file.getAbsolutePath());
                            Main.LOGGER.log(System.Logger.Level.INFO, "Adding '" + file + "' to the bot collection.");
                            this.add(bot);
                            botsAdded += 1;
                        } catch (Exception e) {
                            Main.LOGGER.log(System.Logger.Level.DEBUG, "Could not load '" + file + "' as a bot.", e);
                        }
                    }
                }
            }
        }
        return botsAdded;
    }
}
