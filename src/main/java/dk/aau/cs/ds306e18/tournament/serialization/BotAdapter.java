package dk.aau.cs.ds306e18.tournament.serialization;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotFromConfig;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.BotSkill;
import dk.aau.cs.ds306e18.tournament.model.PsyonixBotFromConfig;
import dk.aau.cs.ds306e18.tournament.rlbot.BotCollection;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import javafx.application.Platform;

import java.io.IOException;

public class BotAdapter extends TypeAdapter<Bot> {

    @Override
    public void write(JsonWriter out, Bot value) throws IOException {

        out.beginObject();

        // Bot is an interface, so we have to save the class in the json file
        out.name("class").value(value.getClass().getSimpleName());

        // If the bot is based on a config file, just save the path to the config file
        if (value instanceof BotFromConfig) {
            BotFromConfig botFromConfig = (BotFromConfig) value;
            out.name("config").value(botFromConfig.getConfigPath());

            if (value instanceof PsyonixBotFromConfig) {
                // For psyonix bots we also need to store the skill level
                PsyonixBotFromConfig psyonixBot = (PsyonixBotFromConfig) value;
                out.name("skill").value(psyonixBot.getBotSkill().getConfigValue());
            }
        }

        out.endObject();
    }

    @Override
    public Bot read(JsonReader in) throws IOException {

        in.beginObject();
        in.nextName(); // This consumes the value's name, i.e. "class" in "class: BotFromConfig"
        String clazz = in.nextString();

        if (PsyonixBotFromConfig.class.getSimpleName().equals(clazz)) {

            // Construct psyonix bot
            in.nextName();
            String config = in.nextString();
            in.nextName();
            double skill = in.nextDouble();
            in.endObject();
            try {
                PsyonixBotFromConfig bot = new PsyonixBotFromConfig(config, BotSkill.getSkillFromNumber(skill));
                BotCollection.global.add(bot);
                return bot;

            } catch (RuntimeException e) {
                Main.LOGGER.log(System.Logger.Level.ERROR, "Failed to load bot from config. " + e.getCause().getMessage());
                Platform.runLater(() -> Alerts.errorNotification("Failed to load bot: " + config, e.getCause().getMessage()));
                return null;
            }

        } else if (BotFromConfig.class.getSimpleName().equals(clazz)) {

            // Construct bot from config
            in.nextName();
            String config = in.nextString();
            in.endObject();
            try {
                BotFromConfig bot = new BotFromConfig(config);
                BotCollection.global.add(bot);
                return bot;

            } catch (RuntimeException e) {
                Main.LOGGER.log(System.Logger.Level.ERROR, "Failed to load bot from config. " + e.getCause().getMessage());
                Platform.runLater(() -> Alerts.errorNotification("Failed to load bot: " + config, e.getCause().getMessage()));
                return null;
            }
        }

        in.endObject();
        return null; // Unknown bot type
    }
}
