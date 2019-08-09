package dk.aau.cs.ds306e18.tournament.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class RLBotSettingsTabController {

    public static RLBotSettingsTabController instance;

    @FXML private VBox tabRoot;

    @FXML
    private void initialize() {
        instance = this;
        update();
    }

    /** Updates all ui elements */
    public void update() {

    }
}
