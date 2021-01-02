package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotType;
import javafx.scene.image.Image;

public class BotIcons {

    private static Image rlbotIcon = new Image(BotCollectionCell.class.getResourceAsStream("layout/images/rlbot small square logo.png"));
    private static Image psyonixIcon = new Image(BotCollectionCell.class.getResourceAsStream("layout/images/psyonix small square logo.png"));

    /**
     * Returns an Image representing the bot.
     */
    public static Image getIconForBot(Bot bot) {
        if (bot.getBotType() == BotType.PSYONIX) return psyonixIcon;
        return rlbotIcon;
    }
}
