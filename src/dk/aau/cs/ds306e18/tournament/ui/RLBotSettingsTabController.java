package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfig;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfigOptions.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

public class RLBotSettingsTabController {

    public static RLBotSettingsTabController instance;
    public ChoiceBox<GameMap> gameMapChoicebox;
    public ChoiceBox<GameMode> gameModeChoicebox;

    @FXML private VBox tabRoot;

    @FXML
    private void initialize() {
        instance = this;

        MatchConfig loadedMatchConfig = Tournament.get().getRlBotSettings().getMatchConfig();

        gameMapChoicebox.setItems(FXCollections.observableArrayList(GameMap.values()));
        gameMapChoicebox.getSelectionModel().select(loadedMatchConfig.getGameMap());
        gameMapChoicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().getRlBotSettings().getMatchConfig().setGameMap(newValue);
        });

        gameModeChoicebox.setItems(FXCollections.observableArrayList(GameMode.values()));
        gameModeChoicebox.getSelectionModel().select(loadedMatchConfig.getGameMode());
        gameModeChoicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().getRlBotSettings().getMatchConfig().setGameMode(newValue);
        });

        /*
        seedingChoicebox.setItems(FXCollections.observableArrayList(SeedingOption.values()));
        seedingChoicebox.getSelectionModel().select(Tournament.get().getSeedingOption());
        seedingChoicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().setSeedingOption(newValue);
            updateParticipantFields();
            updateSeedSpinner();
            teamsListView.refresh();
        });
        */

        update();
    }

    /** Updates all ui elements */
    public void update() {

    }
}
