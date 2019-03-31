package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Bot;
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

import java.io.IOException;

public class BotInfoController {


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

    private Bot bot;

    public void setBot(Bot bot) {
        this.bot = bot;

        botNameText.setText(bot.getName());
        developersText.setText(bot.getDeveloper());
        descriptionText.setText(bot.getDescription());
        funFactText.setText(bot.getFunFact());
        githubText.setText(bot.getGitHub());
        languageText.setText(bot.getLanguage());
        botTypeText.setText(bot.getBotType().toString());
    }

    public Bot getBot() {
        return bot;
    }

    @FXML
    public void onActionClose(ActionEvent actionEvent) {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    @FXML
    public void onActionShowFiles(ActionEvent actionEvent) {

    }

    @FXML
    public void windowDragged(MouseEvent mouseEvent) {

    }

    @FXML
    public void windowPressed(MouseEvent mouseEvent) {
        
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
}
