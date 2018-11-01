package dk.aau.cs.ds306e18.tournament.ui.controllers;

import dk.aau.cs.ds306e18.tournament.model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ParticipantSettingsTabController {

    @FXML private BorderPane participantSettingsTab;
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
    @FXML private Spinner<Integer> seedValueSpinner;

    @FXML private void initialize() {


        botsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateBotFields();
        });
        teamsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateTeamFields();
        });

        seedValueSpinner.setEditable(true);
        seedValueSpinner.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

        seedValueSpinner.valueFactoryProperty().addListener(((observable, oldValue, newValue) -> {
            updateSeedValue();

        }));

    }

    private void updateSeedValue() {
        Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex()).setInitialSeedValue(seedValueSpinner.getValue());
    }

    void updateBotFields(){
        if (botsListView.getSelectionModel().getSelectedIndex() !=-1) {
            botNameTextField.setText(Tournament.get()
                    .getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .getBots().get(botsListView.getSelectionModel().getSelectedIndex()).getName());
            developerTextField.setText(Tournament.get()
                    .getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .getBots().get(botsListView.getSelectionModel().getSelectedIndex()).getDeveloper());
            botDescription.setText(Tournament.get()
                    .getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .getBots().get(botsListView.getSelectionModel().getSelectedIndex()).getDescription());
        }else clearBotFields();
    }

    private void clearBotFields() {
        botDescription.setText("");
        developerTextField.setText("");
        botNameTextField.setText("");
    }


    void updateTeamFields(){
        if (teamsListView.getSelectionModel().getSelectedIndex()!=-1) {
            seedValueSpinner.getValueFactory().setValue(Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .getInitialSeedValue());
            botsListView.getSelectionModel().clearSelection();
            teamNameTextField.setText(Tournament.get()
                    .getTeams().get(teamsListView.getSelectionModel().getSelectedIndex()).getTeamName());
            botsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .getBots()));
            botsListView.refresh();
        }else clearTeamFields();
        checkForEmptyName();
    }

    private void clearTeamFields() {
        teamNameTextField.setText("");
        botsListView.setItems(null);
        botsListView.refresh();
    }

    @FXML void botDesscriptionTextAreaOnKeyReleased(KeyEvent event) {
        Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex()).getBots().get(botsListView.getSelectionModel().getSelectedIndex())
                .setDescription(botDescription.getText());
    }


    @FXML void developerTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex()).getBots().get(botsListView.getSelectionModel().getSelectedIndex())
                .setDeveloper(developerTextField.getText());

    }

    @FXML void botNameTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex()).getBots().get(botsListView.getSelectionModel().getSelectedIndex())
                .setName(botNameTextField.getText());
        botsListView.refresh();
    }

    @FXML void teamNameTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                .setTeamName(teamNameTextField.getText());
        teamsListView.refresh();
    }
    void checkForEmptyName(){
        for (Team team: Tournament.get().getTeams()) {
            String nameCheck = team.getTeamName();
            nameCheck = nameCheck.replaceAll("\\s+","");
            if (nameCheck.compareTo("")==0){
                team.setTeamName("Team ?");
            }

        }

    }


    @FXML void addBotBtnOnAction(ActionEvent actionEvent){
        if (teamsListView.getSelectionModel().getSelectedIndex()!=-1) {
            Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .addBot(new Bot("Bot "+ (Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex()).size()+1), "Dev 1"));
            botsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .getBots()));
            botsListView.refresh();
        }
    }

    @FXML void removeBotBtnOnAction(ActionEvent actionEvent){
        int selectedIndex = botsListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1 && botsListView.getItems().size()!=1){
            Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .removeBot(selectedIndex);
            botsListView.setItems(FXCollections.observableArrayList(Tournament.get()
                    .getTeams().get(teamsListView.getSelectionModel().getSelectedIndex()).getBots()));
            botsListView.refresh();
        }
    }
    @FXML void addTeamBtnOnAction(ActionEvent actionEvent){
        Tournament.get().addTeam(new Team("Team "+ (Tournament.get().getTeams().size()+1),new ArrayList<Bot>(),0,""));
        teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
        teamsListView.getSelectionModel().selectLast();
        teamsListView.refresh();
        addBotBtnOnAction(actionEvent);

    }

    @FXML void removeTeamBtnOnAction(ActionEvent actionEvent){
        botsListView.getSelectionModel().clearSelection();
        botsListView.setItems(null);
        botsListView.refresh();
        int selectedIndex = teamsListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1){
            Tournament.get().removeTeam(selectedIndex);

            teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
            teamsListView.refresh();
        }
    }
    @FXML void seedValueSpinnerOnAction(){
        int selectedIndex = teamsListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1){
            Tournament.get().getTeams().get(selectedIndex).setInitialSeedValue(seedValueSpinner.valueProperty().getValue());
        }


    }

    @FXML void configPathBtnOnAction(ActionEvent actionEvent){
    }
}
