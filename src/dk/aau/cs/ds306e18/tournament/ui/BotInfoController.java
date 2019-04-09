package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.BotFromConfig;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class BotInfoController extends DraggablePopupWindow {

    @FXML public Text botNameText;
    @FXML public Text botConfigPathText;
    @FXML public Text developersText;
    @FXML public Text descriptionText;
    @FXML public Text funFactText;
    @FXML public Text githubText;
    @FXML public Text languageText;
    @FXML public Text botTypeText;
    @FXML public Button showFilesButton;
    @FXML public Button closeButton;
    @FXML public Button reloadBot;

    private Bot bot;

    /**
     * Set which bot is shown. This is update all the information displayed.
     */
    public void setBot(Bot bot) {
        this.bot = bot;

        botNameText.setText(bot.getName());
        botConfigPathText.setText(bot.getConfigPath());
        developersText.setText(bot.getDeveloper());
        descriptionText.setText(bot.getDescription());
        funFactText.setText(bot.getFunFact());
        githubText.setText(bot.getGitHub());
        languageText.setText(bot.getLanguage());
        botTypeText.setText(bot.getBotType().toString());
    }

    /**
     * Returns the displayed bot.
     */
    public Bot getBot() {
        return bot;
    }

    @FXML
    public void onActionClose(ActionEvent actionEvent) {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    @FXML
    public void onActionShowFiles(ActionEvent actionEvent) {
        // Check OS. We can only open File Explorer on Windows.
        if (System.getProperty("os.name").contains("Windows")) {

            // Try to show file explorer showing the bot's config file
            File file = new File(bot.getConfigPath());
            boolean showFailed = true;
            if (file.exists()) {
                try {
                    Runtime.getRuntime().exec("explorer.exe /select," + file.toString());
                    showFailed = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // If something failed, let the user know
            if (showFailed) {
                Alerts.errorNotification("Could not show files.", "Could not open File Explorer showing the config file. Does the file exist?");
            }
        } else {
            Alerts.errorNotification("Could not show files.", "Could not open File Explorer since OS is not Windows.");
        }
    }

    @FXML
    public void windowDragged(MouseEvent mouseEvent) {
        super.windowDragged(mouseEvent);
    }

    @FXML
    public void windowPressed(MouseEvent mouseEvent) {
        super.windowPressed(mouseEvent);
    }

    /**
     * Creates a popup window where information about a given bot is shown.
     * @param bot the bot
     * @param parent the parent window
     */
    public static void showInfoForBot(Bot bot, Window parent) {
        try {
            Stage infoStage = new Stage();
            infoStage.initStyle(StageStyle.TRANSPARENT);
            infoStage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(BotInfoController.class.getResource("layout/BotInfo.fxml"));
            AnchorPane infoPane = loader.load();
            infoStage.setScene(new Scene(infoPane));

            BotInfoController controller = loader.getController();
            controller.setBot(bot);

            // Calculate the center position of the parent window.
            double centerXPosition = parent.getX() + parent.getWidth()/2d;
            double centerYPosition = parent.getY() + parent.getHeight()/2d;

            // Show info window at the center of the parent window.
            infoStage.setOnShown(ev -> {
                infoStage.setX(centerXPosition - infoStage.getWidth()/2d);
                infoStage.setY(centerYPosition - infoStage.getHeight()/2d);
            });

            infoStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onActionReloadBot(ActionEvent actionEvent) {
        if (bot instanceof BotFromConfig) {
            ((BotFromConfig) bot).reload();
            setBot(bot); // Updates all fields
            ParticipantSettingsTabController.instance.update();
        }
    }
}
