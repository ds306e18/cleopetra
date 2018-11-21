package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.Bot;
import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ParticipantSettingsTabController {

    @FXML private GridPane participantSettingsTab;
    @FXML private TextField teamNameTextField;
    @FXML private TextField botNameTextField;
    @FXML private TextField developerTextField;
    @FXML private TextArea botDescription;
    @FXML private Button configPathBtn;
    @FXML private Button addTeamBtn;
    @FXML private Button addBotBtn;
    @FXML private Button removeTeamBtn;
    @FXML private Button removeBotBtn;
    @FXML private ListView<Bot> botsListView;
    @FXML private ListView<Team> teamsListView;
    @FXML private VBox teamSettingsVbox;
    @FXML private VBox botSettingsVbox;
    @FXML private TextField configPathTextField;
    @FXML private Button swapUpTeam;
    @FXML private Button swapDownTeam;
    final private FileChooser fileChooser = new FileChooser();

    @FXML
    private void initialize() {

        //Sets the VBox for team and bot as false, hiding them
        botSettingsVbox.setVisible(false);
        teamSettingsVbox.setVisible(false);
        configPathTextField.setEditable(false);

        //By default the remove stage and bot button is disabled
        removeTeamBtn.setDisable(true);
        removeBotBtn.setDisable(true);

        //Adds selectionslisteners to bot and team listviews
        botsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateBotFields();
        });
        teamsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateTeamFields();
        });
    }

    @FXML
    void configPathBtnOnAction(ActionEvent actionEvent) {

        File file = fileChooser.showOpenDialog((Stage) participantSettingsTab.getScene().getWindow());
        if (file != null) {
            List<File> files = Arrays.asList(file);
            setConfigPathText(files);
        }
    }

    /** Updates tournament values from fields when key released */
    @FXML
    void botDesscriptionTextAreaOnKeyReleased(KeyEvent event) {
        Tournament.get().getTeams().get(getSelectedTeamIndex()).getBots().get(botsListView.getSelectionModel().getSelectedIndex())
                .setDescription(botDescription.getText());
    }

    /** Updates tournament values from fields when key released. */
    @FXML
    void developerTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().getTeams().get(getSelectedTeamIndex()).getBots().get(botsListView.getSelectionModel().getSelectedIndex())
                .setDeveloper(developerTextField.getText());
    }

    /** Updates tournament values from fields when key released. */
    @FXML
    void botNameTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().getTeams().get(getSelectedTeamIndex()).getBots().get(botsListView.getSelectionModel().getSelectedIndex())
                .setName(botNameTextField.getText());
        botsListView.refresh();
    }

    /** Updates tournament values from fields when key released. */
    @FXML
    void teamNameTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().getTeams().get(getSelectedTeamIndex()).setTeamName(teamNameTextField.getText());
        teamsListView.refresh();
    }

    /** Updates the lists on button press action. */
    @FXML
    void addBotBtnOnAction(ActionEvent actionEvent) {
        if (teamsListView.getSelectionModel().getSelectedIndex() != -1) {
            Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .addBot(new Bot("Bot " + (botsListView.getItems().size() + 1), "Dev 1", ""));
            botsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams().get(getSelectedTeamIndex())
                    .getBots()));
            botsListView.refresh();
            botsListView.getSelectionModel().selectLast();
        }
    }

    /** Updates the lists on button press action */
    @FXML
    void removeBotBtnOnAction(ActionEvent actionEvent) {
        int selectedIndex = botsListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1 && botsListView.getItems().size() != 1) {
            Tournament.get().getTeams().get(getSelectedTeamIndex())
                    .removeBot(selectedIndex);
            botsListView.setItems(FXCollections.observableArrayList(Tournament.get()
                    .getTeams().get(getSelectedTeamIndex()).getBots()));
            botsListView.refresh();
            botsListView.getSelectionModel().selectLast();
        }
    }

    /** Updates the lists on button press action */
    @FXML
    void addTeamBtnOnAction(ActionEvent actionEvent) {

        //Create a team with a bot and add the team to the tournament
        Team team = new Team("Team " + (Tournament.get().getTeams().size() + 1), new ArrayList<Bot>(), teamsListView.getItems().size()+1, "");
        team.addBot(new Bot("Bot 1", "Dev 1", ""));
        Tournament.get().addTeam(team);

        teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
        teamsListView.refresh();
        teamsListView.getSelectionModel().selectLast();

        botsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams().get(getSelectedTeamIndex()).getBots()));
        botsListView.refresh();
    }

    /** Updates the lists on button press action */
    @FXML
    void removeTeamBtnOnAction(ActionEvent actionEvent) {
        botsListView.getSelectionModel().clearSelection();
        botsListView.setItems(null);
        botsListView.refresh();

        if (getSelectedTeamIndex() != -1) {
            Tournament.get().removeTeam(getSelectedTeamIndex());

            teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
            teamsListView.refresh();
            teamsListView.getSelectionModel().selectLast();
        }
    }

    /** Updates the textfields with the values from the selected bot. */
    void updateBotFields() {

        if (botsListView.getSelectionModel().getSelectedIndex() != -1) {

            //Disable addBotButton if there is 5 teams in list.
            addBotBtn.setDisable(botsListView.getItems().size() >= 5);

            botSettingsVbox.setVisible(true);
            Bot selectedBot = Tournament.get().getTeams().get(getSelectedTeamIndex()).getBots().get(botsListView.getSelectionModel().getSelectedIndex());
            botNameTextField.setText(selectedBot.getName());
            developerTextField.setText(selectedBot.getDeveloper());
            botDescription.setText(selectedBot.getDescription());
            configPathTextField.setText(selectedBot.getConfigPath());

            //If the botListView has more then 1 items, then enable remove button
            if(botsListView.getItems().size() > 1)
                removeBotBtn.setDisable(false);

            //if no bot is selected clear the fields and hide the botsettgins box.
        } else {
            removeBotBtn.setDisable(true);
            clearBotFields();
        }
        //Check for empty names
        Tournament.get().getTeams().forEach(this::checkForEmptyBotName);
    }

    /** Clears bot fields and hides bot box. */
    private void clearBotFields() {
        configPathTextField.setText("");
        botDescription.setText("");
        developerTextField.setText("");
        botNameTextField.setText("");
        botSettingsVbox.setVisible(false);
    }

    /** Updates the textfields with the values from the selected team. */
    void updateTeamFields() {

        teamSettingsVbox.setVisible(teamsListView.getItems().size() != 0);

        if (getSelectedTeamIndex() != -1) {

            // Handle team order button disabling / enabling
            int selectedIndex = getSelectedTeamIndex();
            swapUpTeam.setDisable(selectedIndex == 0);
            swapDownTeam.setDisable(selectedIndex == teamsListView.getItems().size() - 1 && selectedIndex != - 1);

            Team selectedTeam = Tournament.get().getTeams().get(getSelectedTeamIndex());

            botsListView.getSelectionModel().clearSelection();
            teamNameTextField.setText(selectedTeam.getTeamName());
            botsListView.setItems(FXCollections.observableArrayList(selectedTeam.getBots()));
            botsListView.refresh();

            //If the teamListView has items, then the enable remove button
            if(teamsListView.getItems().size() != 0)
                removeTeamBtn.setDisable(false);

            //if no bot is selected clear the fields and hide the teamsettings box and disable remove team button
        } else {
            removeTeamBtn.setDisable(true);
            clearTeamFields();
        }

        //Check for empty names
        checkForEmptyTeamName();
    }

    /** Clears bot fields and hides bot box. */
    private void clearTeamFields() {
        teamSettingsVbox.setVisible(false);
        teamNameTextField.setText("");
        botsListView.setItems(null);
        botsListView.refresh();
    }

    void checkForEmptyTeamName() {
        for (Team team : Tournament.get().getTeams()) {
            String nameCheck = team.getTeamName();
            nameCheck = nameCheck.replaceAll("\\s+", "");
            if (nameCheck.compareTo("") == 0) {
                team.setTeamName("Team ?");
            }

        }
    }

    void checkForEmptyBotName(Team team) {
        for (Bot bot : team.getBots()) {
            String nameCheck = bot.getName();
            nameCheck = nameCheck.replaceAll("\\s+", "");
            if (nameCheck.compareTo("") == 0) {
                bot.setName("Bot ?");
            }
        }
    }

    /** Sets the configPath text and changes the path for the selected bot */
    private void setConfigPathText(List<File> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (File file : files) {
            //Format path to be shown
            String path = file.getAbsolutePath().replace("\\", "/");
            int lastSlashIndex = path.lastIndexOf("/");
            int secondLastSlashIndex = path.lastIndexOf("/", lastSlashIndex - 1);

            configPathTextField.setText("." + path.substring(secondLastSlashIndex));
            botsListView.getSelectionModel().getSelectedItem().setConfigPath(file.getAbsolutePath());
        }
    }

    /** Swaps a team upwards in the list of teams. Used to allow ordering of Team and thereby their seed. */
    @FXML
    private void swapTeamUpwards() {
        swapTeamSeeds(Tournament.get().getTeams().get(getSelectedTeamIndex()),
                Tournament.get().getTeams().get(getSelectedTeamIndex() - 1));
    }

    /** Swaps a team downwards in the list of teams. Used to allow ordering of Team and thereby their seed. */
    @FXML
    private void swapTeamDownwards() {
        swapTeamSeeds(Tournament.get().getTeams().get(getSelectedTeamIndex()),
                Tournament.get().getTeams().get(getSelectedTeamIndex() + 1));
    }

    /** Swaps seeds of the firsts given team with the second team. */
    private void swapTeamSeeds(Team first, Team second){

        int firstSeed = first.getInitialSeedValue();
        first.setInitialSeedValue(second.getInitialSeedValue());
        second.setInitialSeedValue(firstSeed);

        Tournament.get().sortTeamsAfterInitialSeed();

        teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
        teamsListView.refresh();
    }

    private Team getSelectedTeam(){
        return teamsListView.getSelectionModel().getSelectedItem();
    }

    private int getSelectedTeamIndex() {
        return teamsListView.getSelectionModel().getSelectedIndex();
    }
}
