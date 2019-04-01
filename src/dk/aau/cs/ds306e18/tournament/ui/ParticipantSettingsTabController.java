package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.Main;
import dk.aau.cs.ds306e18.tournament.model.*;
import dk.aau.cs.ds306e18.tournament.utility.BotCollection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class ParticipantSettingsTabController {

    public static ParticipantSettingsTabController instance;

    @FXML private HBox participantSettingsTab;
    @FXML private ChoiceBox<SeedingOption> seedingChoicebox;
    @FXML private TextField teamNameTextField;
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
    @FXML private ListView<Bot> botCollectionListView;

    public BotCollection botCollection;
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
        botCollection = new BotCollection();
        botCollectionListView.setCellFactory(listView -> new BotCollectionCell(this));
        botCollectionListView.setItems(FXCollections.observableArrayList(botCollection));

        // Setup file chooser
        botConfigFileChooser = new FileChooser();
        botConfigFileChooser.setTitle("Choose a bot config file");
        botConfigFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Config (*.cfg)", "*.cfg"));
        botFolderChooser = new DirectoryChooser();
        botFolderChooser.setTitle("Choose a folder with bots");

        // Things are now setup
        // Update everything
        update();
    }

    /** Updates all ui elements */
    public void update() {
        teamsListView.refresh();
        rosterListView.refresh();
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
        Team team = new Team("Team " + (Tournament.get().getTeams().size() + 1), new ArrayList<>(), teamsListView.getItems().size() + 1, "");
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
        botCollection.remove(bot);
        botCollectionListView.setItems(FXCollections.observableArrayList(botCollection));
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
        botConfigFileChooser.setInitialDirectory(Main.lastSavedDirectory);
        Window window = loadConfigButton.getScene().getWindow();
        List<File> files = botConfigFileChooser.showOpenMultipleDialog(window);

        if (files != null) {
            // Add all selected bots to bot collection
            botCollection.addAll(files.stream().map(file -> new BotFromConfig(file.toString())).collect(Collectors.toList()));
            botCollectionListView.setItems(FXCollections.observableArrayList(botCollection));
            botCollectionListView.refresh();

            Main.lastSavedDirectory = files.get(0).getParentFile();
        }
    }

    @FXML
    public void onActionLoadFolder(ActionEvent actionEvent) {
        // Open directory chooser
        botFolderChooser.setInitialDirectory(Main.lastSavedDirectory);
        Window window = loadFolderButton.getScene().getWindow();
        File folder = botFolderChooser.showDialog(window);

        if (folder != null) {
            // Find all bots in the folder and add them to bot collection
            botCollection.addAllBotsFromFolder(folder, 10);
            botCollectionListView.setItems(FXCollections.observableArrayList(botCollection));
            botCollectionListView.refresh();

            Main.lastSavedDirectory = folder;
        }
    }
}
