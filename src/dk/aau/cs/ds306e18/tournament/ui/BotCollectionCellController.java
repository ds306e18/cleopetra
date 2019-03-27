package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class BotCollectionCellController extends ListCell<Bot> {

    @FXML public HBox hbox;
    @FXML public Button addToTeamButton;
    @FXML public Label botNameLabel;
    @FXML public Button infoButton;
    @FXML public Button removeBotButton;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(Bot bot, boolean empty) {
        super.updateItem(bot, empty);

        // This text is not used by our custom cell
        setText(null);

        if (empty || bot == null) {

            // Nothing to display
            setGraphic(null);

        } else {
            // If we haven't loaded the layout yet, not is the time. We load the layout of the cell with the fxml loader
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(BotCollectionCellController.class.getResource("layout/BotCollectionCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Display the bot name
            botNameLabel.setText(bot.getName());

            setGraphic(hbox);
        }
    }

    public void onActionAddToTeam(ActionEvent actionEvent) {

    }

    public void onActionInfo(ActionEvent actionEvent) {

    }

    public void onActionRemoveButton(ActionEvent actionEvent) {

    }
}
