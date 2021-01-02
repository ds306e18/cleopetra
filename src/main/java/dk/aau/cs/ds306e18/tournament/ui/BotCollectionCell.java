package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * This is a custom ListCell used to display bots in the bot collection panel in the participant settings tab.
 */
public class BotCollectionCell extends ListCell<Bot> {

    @FXML public HBox hbox;
    @FXML public Button addToTeamButton;
    @FXML public Label botNameLabel;
    @FXML public Button infoButton;
    @FXML public Button removeBotButton;
    @FXML public ImageView botTypeIcon;

    private ParticipantSettingsTabController participantSettingsTabController;

    public BotCollectionCell(ParticipantSettingsTabController participantSettingsTabController) {
        this.participantSettingsTabController = participantSettingsTabController;

        try {

            // Load the layout of the cell from the fxml file. The controller will be this class
            FXMLLoader fxmlLoader = new FXMLLoader(BotCollectionCell.class.getResource("layout/BotCollectionCell.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Bot bot, boolean empty) {
        super.updateItem(bot, empty);

        // This text is not used by our custom cell
        setText(null);

        if (empty || bot == null) {

            // Nothing to display
            setGraphic(null);

        } else {

            // Display the bot name and a fitting icon
            botNameLabel.setText(bot.getName());
            botTypeIcon.setImage(BotIcons.getIconForBot(bot));
            setGraphic(hbox);
        }
    }

    @FXML
    public void onActionAddToTeam(ActionEvent actionEvent) {
        participantSettingsTabController.addBotToSelectedTeamRoster(getItem());
    }

    @FXML
    public void onActionInfo(ActionEvent actionEvent) {
        BotInfoController.showInfoForBot(getItem(), getScene().getWindow());
    }

    @FXML
    public void onActionRemove(ActionEvent actionEvent) {
        participantSettingsTabController.removeBotFromBotCollection(getItem());
    }
}
