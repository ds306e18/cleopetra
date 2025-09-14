package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.*;
import dk.aau.cs.ds306e18.tournament.settings.CleoPetraSettings;
import dk.aau.cs.ds306e18.tournament.settings.LatestPaths;
import dk.aau.cs.ds306e18.tournament.utility.Alerts;
import dk.aau.cs.ds306e18.tournament.rlbot.BotCollection;
import dk.aau.cs.ds306e18.tournament.utility.AutoNaming;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class ParticipantSettingsTabController {

    public static ParticipantSettingsTabController instance;

    @FXML private HBox participantSettingsTab;
    @FXML private ChoiceBox<SeedingOption> seedingChoicebox;
    @FXML private TextField teamNameTextField;
    @FXML private Button autoNameTeamButton;
    @FXML private Spinner<Integer> seedSpinner;
    @FXML private Button addTeamBtn;
    @FXML private Button removeTeamBtn;
    @FXML private ListView<Team> teamsListView;
    @FXML private VBox teamSettingsColumnVbox;
    @FXML private VBox botCollectionColumnVBox;
    @FXML private Button swapUpTeam;
    @FXML private Button swapDownTeam;
    @FXML private ListView<Bot> rosterListView;
    @FXML private Button loadConfigButton;
    @FXML private Button loadFolderButton;
    @FXML private Button loadBotPack;
    @FXML private ListView<Bot> botCollectionListView;
    @FXML private Button createTeamWithEachBotButton;

    private FileChooser botConfigFileChooser;
    private DirectoryChooser botFolderChooser;

    @FXML
    private void initialize() {
        instance = this;

        // Seeding Option
        seedingChoicebox.setItems(FXCollections.observableArrayList(SeedingOption.values()));
        seedingChoicebox.getSelectionModel().select(Tournament.get().getSeedingOption());
        seedingChoicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Tournament.get().setSeedingOption(newValue);
            updateParticipantFields();
            updateSeedSpinner();
            teamsListView.refresh();
        });

        setUpTeamsListView();

        // Seed spinner behaviour
        seedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
        seedSpinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> {
            // Seed spinner should only be enabled when seeding option is manual. When the value change
            // we want to update the order in the team list view and the teams label in the list
            Team selectedTeam = getSelectedTeam();
            if (selectedTeam != null && Tournament.get().getSeedingOption() == SeedingOption.MANUALLY) {
                selectedTeam.setInitialSeedValue(newValue);
                Tournament.get().sortTeamsBySeed();
                teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
                teamsListView.refresh();

                // Select text to make it easy to edit
                seedSpinner.getEditor().selectAll();
            }
        });
        seedSpinner.setEditable(true);
        seedSpinner.getEditor().textProperty().addListener((observable, oldText, newText) -> {
            // We allow empty strings and all positive numbers. If the string is empty, the text goes
            // back to saved seed value when focus is lost
            if (newText.equals("") || newText.matches("^([1-9][0-9]*)$")) {
                if (!newText.equals("")) {
                    seedSpinner.getValueFactory().setValue(Integer.parseInt(newText));
                }
            } else {
                seedSpinner.getEditor().setText(oldText);
            }
        });
        seedSpinner.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            // Select all text, because that is user friendly for this case
            if (isNowFocused) {
                Platform.runLater(seedSpinner.getEditor()::selectAll);
            }
            // Focus lost and editor is currently empty, so set the text to the saved seed value
            if (wasFocused && seedSpinner.getEditor().getText().equals("")) {
                seedSpinner.getEditor().setText("" + getSelectedTeam().getInitialSeedValue());
            }
        });

        // Team roster list setup
        rosterListView.setCellFactory(listView -> new TeamRosterCell(this));

        // Bot collection list setup
        BotCollection.global.addPsyonixBots();
        botCollectionListView.setCellFactory(listView -> new BotCollectionCell(this));
        botCollectionListView.setItems(FXCollections.observableArrayList(BotCollection.global));

        // Setup file chooser
        botConfigFileChooser = new FileChooser();
        botConfigFileChooser.setTitle("Choose a bot config file");
        botConfigFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TOML (*.toml)", "*bot.toml"));
        botFolderChooser = new DirectoryChooser();
        botFolderChooser.setTitle("Choose a folder with bots");

        // Things are now setup
        // Update everything
        update();
    }

    /** Updates all ui elements */
    public void update() {
        teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
        teamsListView.refresh();
        rosterListView.refresh();
        botCollectionListView.refresh();
        updateParticipantFields();
        updateTeamFields();
    }

    /** Sets up the listview for teams. Setting items,
     * adding listener and changing what is displayed. */
    private void setUpTeamsListView(){

        //Assign teams to the list in case of the tournament being loaded
        teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));

        //Adds selectionsListener to team ListView
        teamsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateParticipantFields();
            updateTeamFields();
        });

        //Formatting what is displayed in the listView: id + name.
        teamsListView.setCellFactory(lv -> new ListCell<Team>() {
            public void updateItem(Team team, boolean empty) {
                super.updateItem(team, empty);
                if (empty) {
                    setText(null);
                } else {
                    SeedingOption seedingOption = Tournament.get().getSeedingOption();

                    switch (seedingOption) {
                        case SEED_BY_ORDER:
                            int index = teamsListView.getItems().indexOf(team) + 1;
                            setText(index + ".    " + team.getTeamName());
                            break;

                        case MANUALLY:
                            setText("Seed " + team.getInitialSeedValue() + ":    " + team.getTeamName());
                            break;

                        case NO_SEEDING:
                        case RANDOM_SEEDING:
                            setText(team.getTeamName());
                            break;
                    }

                    teamsListView.refresh();
                }
            }
        });
    }

    /**
     * Updates tournament values from fields when key released.
     */
    @FXML
    void teamNameTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().getTeams().get(getSelectedTeamIndex()).setTeamName(teamNameTextField.getText());
        teamsListView.refresh();
    }

    @FXML
    void onActionAddTeam(ActionEvent actionEvent) {

        //Create a team with a bot and add the team to the tournament
        Team team = new Team("Unnamed Team", new ArrayList<>(), teamsListView.getItems().size() + 1, "");
        AutoNaming.autoName(team, Tournament.get().getTeams());
        Tournament.get().addTeam(team);

        teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
        teamsListView.refresh();
        teamsListView.getSelectionModel().selectLast();

        rosterListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams().get(getSelectedTeamIndex()).getBots()));
        rosterListView.refresh();
    }

    @FXML
    void onActionRemoveTeam(ActionEvent actionEvent) {
        rosterListView.getSelectionModel().clearSelection();
        rosterListView.setItems(null);
        rosterListView.refresh();

        if (getSelectedTeamIndex() != -1) {
            Tournament.get().removeTeam(getSelectedTeamIndex());

            teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
            teamsListView.refresh();
            teamsListView.getSelectionModel().selectLast();
        }
    }

    /**
     * Update all fields in the first column.
     */
    private void updateParticipantFields() {

        boolean started = Tournament.get().hasStarted();

        SeedingOption seedingOption = Tournament.get().getSeedingOption();
        seedingChoicebox.setDisable(started);

        teamsListView.refresh();

        int selectedIndex = getSelectedTeamIndex();

        // Handle team order button disabling / enabling
        swapUpTeam.setDisable(seedingOption != SeedingOption.SEED_BY_ORDER || selectedIndex <= 0 || started);
        swapDownTeam.setDisable(seedingOption != SeedingOption.SEED_BY_ORDER || selectedIndex == teamsListView.getItems().size() - 1 || started);

        removeTeamBtn.setDisable(selectedIndex == -1 || started);
        addTeamBtn.setDisable(started);
    }

    /**
     * Updates the textfields with the values from the selected team.
     */
    private void updateTeamFields() {

        teamSettingsColumnVbox.setVisible(teamsListView.getItems().size() != 0);
        if (getSelectedTeamIndex() != -1) {

            Team selectedTeam = Tournament.get().getTeams().get(getSelectedTeamIndex());

            teamNameTextField.setText(selectedTeam.getTeamName());
            updateSeedSpinner();

            rosterListView.getSelectionModel().clearSelection();
            rosterListView.setItems(FXCollections.observableArrayList(selectedTeam.getBots()));
            rosterListView.refresh();
        }

        //Check for empty names
        checkForEmptyTeamName();
    }

    /**
     * Updates the value displayed in the seed spinner field for the team and also disables/enables it.
     */
    private void updateSeedSpinner() {
        Team selectedTeam = getSelectedTeam();
        if (selectedTeam != null) {
            int selectedTeamIndex = getSelectedTeamIndex();
            SeedingOption seedingOption = Tournament.get().getSeedingOption();

            seedSpinner.setDisable(seedingOption != SeedingOption.MANUALLY);

            int displayedValue = selectedTeam.getInitialSeedValue();

            switch (seedingOption) {

                case SEED_BY_ORDER:
                    displayedValue = selectedTeamIndex + 1;
                    break;

                case MANUALLY:
                    break;

                case NO_SEEDING:
                case RANDOM_SEEDING:
                    displayedValue = 0;
                    break;
            }

            seedSpinner.getValueFactory().valueProperty().setValue(displayedValue);
        }
    }

    /**
     * All teams with empty names with be renamed to "Team ?"
     */
    private void checkForEmptyTeamName() {
        for (Team team : Tournament.get().getTeams()) {
            String nameCheck = team.getTeamName();
            nameCheck = nameCheck.replaceAll("\\s+", "");
            if (nameCheck.compareTo("") == 0) {

                team.setTeamName("Team ?");
            }
        }
    }

    /**
     * Add a bot to the selected team roster and update the rosterListView
     */
    public void addBotToSelectedTeamRoster(Bot bot) {
        Team selectedTeam = getSelectedTeam();
        if (selectedTeam != null) {
            selectedTeam.addBot(bot);
            rosterListView.setItems(FXCollections.observableArrayList(selectedTeam.getBots()));
            rosterListView.refresh();
        }
    }

    /**
     * Remove a bot to the selected team roster and update the rosterListView
     */
    public void removeBotFromSelectedTeamRoster(int index) {
        Team selectedTeam = getSelectedTeam();
        if (selectedTeam != null) {
            selectedTeam.removeBot(index);
            rosterListView.setItems(FXCollections.observableArrayList(selectedTeam.getBots()));
            rosterListView.refresh();
        }
    }

    /**
     * Remove a bot from the bot collection and update the bot collection list view
     */
    public void removeBotFromBotCollection(Bot bot) {
        BotCollection.global.remove(bot);
        botCollectionListView.setItems(FXCollections.observableArrayList(BotCollection.global));
        botCollectionListView.refresh();
    }

    /**
     * Swaps a team upwards in the list of teams. Used to allow ordering of Team and thereby their seed.
     */
    @FXML
    private void swapTeamUpwards() {
        Tournament.get().swapTeams(getSelectedTeamIndex(), getSelectedTeamIndex() - 1);
        teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
        teamsListView.refresh();
    }

    /**
     * Swaps a team downwards in the list of teams. Used to allow ordering of Team and thereby their seed.
     */
    @FXML
    private void swapTeamDownwards() {
        Tournament.get().swapTeams(getSelectedTeamIndex(), getSelectedTeamIndex() + 1);
        teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
        teamsListView.refresh();
    }

    public Team getSelectedTeam() {
        return teamsListView.getSelectionModel().getSelectedItem();
    }

    public int getSelectedTeamIndex() {
        return teamsListView.getSelectionModel().getSelectedIndex();
    }

    @FXML
    public void onActionLoadConfig(ActionEvent actionEvent) {
        // Open file chooser
        LatestPaths latestPaths = CleoPetraSettings.getLatestPaths();
        botConfigFileChooser.setInitialDirectory(latestPaths.getBotConfigDirectory());
        Window window = loadConfigButton.getScene().getWindow();
        List<File> files = botConfigFileChooser.showOpenMultipleDialog(window);

        if (files != null) {
            // Add all selected bots to bot collection
            BotCollection.global.addAll(files.stream()
                    .map(file -> {
                        try {
                            return new BotFromConfig(file.toString());
                        } catch (Exception e) {
                            Alerts.errorNotification("Loading failed", "Failed to load bot: " + file);
                            Main.LOGGER.log(System.Logger.Level.ERROR, "Failed to load bot: " + file, e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            botCollectionListView.setItems(FXCollections.observableArrayList(BotCollection.global));
            botCollectionListView.refresh();

            latestPaths.setBotConfigDirectory(files.get(0).getParentFile());
        }
    }

    @FXML
    public void onActionLoadFolder(ActionEvent actionEvent) {
        // Open directory chooser
        LatestPaths latestPaths = CleoPetraSettings.getLatestPaths();
        botFolderChooser.setInitialDirectory(latestPaths.getBotConfigDirectory());
        Window window = loadFolderButton.getScene().getWindow();
        File folder = botFolderChooser.showDialog(window);

        if (folder != null) {
            // Find all bots in the folder and add them to bot collection
            var count = BotCollection.global.addAllBotsFromFolder(folder, 10);
            Alerts.infoNotification("Bot folder loaded", "Successfully loaded " + count + " bot(s) from the folder.");
            botCollectionListView.setItems(FXCollections.observableArrayList(BotCollection.global));
            botCollectionListView.refresh();

            latestPaths.setBotConfigDirectory(folder);
        }
    }

    @FXML
    public void onActionLoadBotPack(ActionEvent actionEvent) {
        var optionalCount = BotCollection.global.addRLBotPackIfPresent();
        botCollectionListView.setItems(FXCollections.observableArrayList(BotCollection.global));
        botCollectionListView.refresh();
        if (optionalCount.isPresent()) {
            Alerts.infoNotification("Bot pack loaded", "Successfully loaded " + optionalCount.get() + " bot(s) from the RLBotGUI bot pack.");
        } else {
            Alerts.errorNotification("Failed to load bot pack", "Unable to locate RLBotGUI's bot pack.");
        }
    }

    @FXML
    public void onActionAutoNameTeam(ActionEvent actionEvent) {
        Team team = getSelectedTeam();
        AutoNaming.autoName(team, Tournament.get().getTeams());
        updateTeamFields();
        teamsListView.refresh();
    }

    public void onActionCreateTeamWithEachBot(ActionEvent actionEvent) {
        try {
            javafx.stage.Stage createTeamsStage = new javafx.stage.Stage();
            createTeamsStage.initStyle(StageStyle.TRANSPARENT);
            createTeamsStage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(ParticipantSettingsTabController.class.getResource("layout/CreateTeamsWithEachBot.fxml"));
            AnchorPane createTeamsStageRoot = loader.load();
            createTeamsStage.setScene(new Scene(createTeamsStageRoot));

            // Calculate the center position of the main window.
            javafx.stage.Stage mainWindow = (Stage) participantSettingsTab.getScene().getWindow();
            double centerXPosition = mainWindow.getX() + mainWindow.getWidth()/2d;
            double centerYPosition = mainWindow.getY() + mainWindow.getHeight()/2d;

            // Assign popup window to the center of the main window.
            createTeamsStage.setOnShown(ev -> {
                createTeamsStage.setX(centerXPosition - createTeamsStage.getWidth()/2d);
                createTeamsStage.setY(centerYPosition - createTeamsStage.getHeight()/2d);

                createTeamsStage.show();
            });

            createTeamsStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
