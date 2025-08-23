package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.Tournament;
import dk.aau.cs.ds306e18.tournament.rlbot.MatchControl;
import dk.aau.cs.ds306e18.tournament.rlbot.RLBotSettings;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfig;
import dk.aau.cs.ds306e18.tournament.rlbot.configuration.MatchConfigOptions.*;
import dk.aau.cs.ds306e18.tournament.settings.CleoPetraSettings;
import dk.aau.cs.ds306e18.tournament.settings.LatestPaths;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.function.BiConsumer;

public class RLBotSettingsTabController {

    public static RLBotSettingsTabController instance;
    public Button resetAllButton;
    public ChoiceBox<GameMap> gameMapChoiceBox;
    public ChoiceBox<GameMode> gameModeChoiceBox;
    public CheckBox skipReplaysCheckbox;
    public CheckBox instantStartCheckbox;
    public CheckBox writeOverlayDataCheckbox;
    public TextField overlayPathTextField;
    public Button chooseOverlayPathButton;
    public CheckBox useRLBotGUIPythonCheckbox;
    public Button rlbotRunnerOpenButton;
    public Button rlbotRunnerCloseButton;
    public Button rlbotRunnerStopMatchButton;
    public ChoiceBox<MatchLength> matchLengthChoiceBox;
    public ChoiceBox<MaxScore> maxScoreChoiceBox;
    public ChoiceBox<Overtime> overtimeChoiceBox;
    public ChoiceBox<GameSpeed> gameSpeedChoiceBox;
    public ChoiceBox<BallMaxSpeed> ballMaxSpeedChoiceBox;
    public ChoiceBox<BallType> ballTypeChoiceBox;
    public ChoiceBox<BallWeight> ballWeightChoiceBox;
    public ChoiceBox<BallSize> ballSizeChoiceBox;
    public ChoiceBox<BallBounciness> ballBouncinessChoiceBox;
    public ChoiceBox<BoostAmount> boostAmountChoiceBox;
    public ChoiceBox<BoostStrength> boostStrengthChoiceBox;
    public ChoiceBox<RumblePowers> rumblePowersChoiceBox;
    public ChoiceBox<Gravity> gravityChoiceBox;
    public ChoiceBox<Demolish> demolishChoiceBox;
    public ChoiceBox<RespawnTime> respawnTimeChoiceBox;
    public CheckBox renderingCheckbox;
    public CheckBox stateSettingCheckbox;
    public CheckBox autoSaveReplaysCheckbox;

    @FXML private HBox tabRoot;

    @FXML
    private void initialize() {
        instance = this;

        RLBotSettings settings = Tournament.get().getRlBotSettings();
        MatchConfig matchConfig = settings.getMatchConfig();

        // General match settings
        setupChoiceBox(gameMapChoiceBox, GameMap.values(), matchConfig.getGameMap(), MatchConfig::setGameMap);
        setupChoiceBox(gameModeChoiceBox, GameMode.values(), matchConfig.getGameMode(), MatchConfig::setGameMode);
        setupCheckBox(skipReplaysCheckbox, matchConfig.doSkipReplays(), MatchConfig::setSkipReplays);
        setupCheckBox(instantStartCheckbox, matchConfig.isInstantStart(), MatchConfig::setInstantStart);
        setupCheckBox(renderingCheckbox, matchConfig.isRenderingEnabled(), MatchConfig::setRenderingEnabled);
        setupCheckBox(stateSettingCheckbox, matchConfig.isStateSettingEnabled(), MatchConfig::setStateSettingEnabled);
        setupCheckBox(autoSaveReplaysCheckbox, matchConfig.doAutoSaveReplays(), MatchConfig::setAutoSaveReplays);

        // Mutators
        setupChoiceBox(matchLengthChoiceBox, MatchLength.values(), matchConfig.getMatchLength(), MatchConfig::setMatchLength);
        setupChoiceBox(maxScoreChoiceBox, MaxScore.values(), matchConfig.getMaxScore(), MatchConfig::setMaxScore);
        setupChoiceBox(overtimeChoiceBox, Overtime.values(), matchConfig.getOvertime(), MatchConfig::setOvertime);
        setupChoiceBox(gameSpeedChoiceBox, GameSpeed.values(), matchConfig.getGameSpeed(), MatchConfig::setGameSpeed);
        setupChoiceBox(ballMaxSpeedChoiceBox, BallMaxSpeed.values(), matchConfig.getBallMaxSpeed(), MatchConfig::setBallMaxSpeed);
        setupChoiceBox(ballTypeChoiceBox, BallType.values(), matchConfig.getBallType(), MatchConfig::setBallType);
        setupChoiceBox(ballWeightChoiceBox, BallWeight.values(), matchConfig.getBallWeight(), MatchConfig::setBallWeight);
        setupChoiceBox(ballSizeChoiceBox, BallSize.values(), matchConfig.getBallSize(), MatchConfig::setBallSize);
        setupChoiceBox(ballBouncinessChoiceBox, BallBounciness.values(), matchConfig.getBallBounciness(), MatchConfig::setBallBounciness);
        setupChoiceBox(boostAmountChoiceBox, BoostAmount.values(), matchConfig.getBoostAmount(), MatchConfig::setBoostAmount);
        setupChoiceBox(boostStrengthChoiceBox, BoostStrength.values(), matchConfig.getBoostStrength(), MatchConfig::setBoostStrength);
        setupChoiceBox(rumblePowersChoiceBox, RumblePowers.values(), matchConfig.getRumblePowers(), MatchConfig::setRumblePowers);
        setupChoiceBox(gravityChoiceBox, Gravity.values(), matchConfig.getGravity(), MatchConfig::setGravity);
        setupChoiceBox(demolishChoiceBox, Demolish.values(), matchConfig.getDemolish(), MatchConfig::setDemolish);
        setupChoiceBox(respawnTimeChoiceBox, RespawnTime.values(), matchConfig.getRespawnTime(), MatchConfig::setRespawnTime);

        // Other settings
        boolean writeOverlay = settings.writeOverlayDataEnabled();
        writeOverlayDataCheckbox.setSelected(writeOverlay);
        writeOverlayDataCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().getRlBotSettings().setWriteOverlayData(newValue);
            overlayPathTextField.setDisable(!newValue);
            chooseOverlayPathButton.setDisable(!newValue);
        });
        chooseOverlayPathButton.setDisable(!writeOverlay);
        overlayPathTextField.setDisable(!writeOverlay);
        overlayPathTextField.setText(settings.getOverlayPath());
        overlayPathTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String path = overlayPathTextField.textProperty().get();
                settings.setOverlayPath(path);
            }
        });

        // TODO: Remove button - meaningless option in v5
        useRLBotGUIPythonCheckbox.setSelected(settings.useRLBotGUIPythonIfAvailable());
        useRLBotGUIPythonCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().getRlBotSettings().setUseRLBotGUIPythonIfAvailable(newValue);
        });

        update();
    }

    /**
     * Set up a choice box for match config options.
     */
    private <T> void setupChoiceBox(ChoiceBox<T> choiceBox, T[] values, T current, BiConsumer<MatchConfig, T> onSelect) {
        choiceBox.setItems(FXCollections.observableArrayList(values));
        choiceBox.getSelectionModel().select(current);
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            onSelect.accept(Tournament.get().getRlBotSettings().getMatchConfig(), newValue);
        });
    }

    /**
     * Set up a checkbox for match config options.
     */
    private void setupCheckBox(CheckBox checkBox, boolean current, BiConsumer<MatchConfig, Boolean> onSelect) {
        checkBox.setSelected(current);
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            onSelect.accept(Tournament.get().getRlBotSettings().getMatchConfig(), checkBox.isSelected());
        });
    }

    public void onActionResetAllButton(ActionEvent actionEvent) {
        Tournament.get().getRlBotSettings().setMatchConfig(new MatchConfig());
        update();
    }

    /** Updates all ui elements */
    public void update() {

        RLBotSettings settings = Tournament.get().getRlBotSettings();
        MatchConfig matchConfig = settings.getMatchConfig();

        // General match settings
        gameMapChoiceBox.getSelectionModel().select(matchConfig.getGameMap());
        gameModeChoiceBox.getSelectionModel().select(matchConfig.getGameMode());
        skipReplaysCheckbox.setSelected(matchConfig.doSkipReplays());
        instantStartCheckbox.setSelected(matchConfig.isInstantStart());

        // Mutators
        matchLengthChoiceBox.getSelectionModel().select(matchConfig.getMatchLength());
        maxScoreChoiceBox.getSelectionModel().select(matchConfig.getMaxScore());
        overtimeChoiceBox.getSelectionModel().select(matchConfig.getOvertime());
        gameSpeedChoiceBox.getSelectionModel().select(matchConfig.getGameSpeed());
        ballMaxSpeedChoiceBox.getSelectionModel().select(matchConfig.getBallMaxSpeed());
        ballTypeChoiceBox.getSelectionModel().select(matchConfig.getBallType());
        ballWeightChoiceBox.getSelectionModel().select(matchConfig.getBallWeight());
        ballSizeChoiceBox.getSelectionModel().select(matchConfig.getBallSize());
        ballBouncinessChoiceBox.getSelectionModel().select(matchConfig.getBallBounciness());
        boostAmountChoiceBox.getSelectionModel().select(matchConfig.getBoostAmount());
        boostStrengthChoiceBox.getSelectionModel().select(matchConfig.getBoostStrength());
        rumblePowersChoiceBox.getSelectionModel().select(matchConfig.getRumblePowers());
        gravityChoiceBox.getSelectionModel().select(matchConfig.getGravity());
        demolishChoiceBox.getSelectionModel().select(matchConfig.getDemolish());
        respawnTimeChoiceBox.getSelectionModel().select(matchConfig.getRespawnTime());

        // Other settings
        writeOverlayDataCheckbox.setSelected(settings.writeOverlayDataEnabled());
    }

    public void onActionRLBotRunnerOpen(ActionEvent actionEvent) {
        MatchControl.get().launchConnectAndRunRLBotIfNeeded();
    }

    public void onActionRLBotRunnerClose(ActionEvent actionEvent) {
        // TODO: Remove button
    }

    public void onActionRLBotRunnerStopMatch(ActionEvent actionEvent) {
        MatchControl.get().stopMatch();
    }

    public void onActionChooseOverlayPath(ActionEvent actionEvent) {
        LatestPaths latestPaths = CleoPetraSettings.getLatestPaths();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose overlay folder");
        dirChooser.setInitialDirectory(latestPaths.getOverlayDirectory());
        Window window = chooseOverlayPathButton.getScene().getWindow();
        File folder = dirChooser.showDialog(window);

        if (folder != null) {
            String path = folder.toString();
            overlayPathTextField.setText(path);
            Tournament.get().getRlBotSettings().setOverlayPath(path);
            latestPaths.setOverlayDirectory(folder);
        }
    }
}
