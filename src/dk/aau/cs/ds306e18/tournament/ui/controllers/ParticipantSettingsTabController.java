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

    @FXML private void initialize() {

        botsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Bots ListView Changed (selected: )"+ + teamsListView.getSelectionModel().getSelectedIndex());
            updateBotFields();
        });
        teamsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Teams Listview changed (selected:  )"+ teamsListView.getSelectionModel().getSelectedIndex());
            updateTeamFields();
        });
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
        }
        }


    void updateTeamFields(){
        if (teamsListView.getSelectionModel().getSelectedIndex()!=-1) {
            botsListView.getSelectionModel().clearSelection();
            teamNameTextField.setText(Tournament.get()
                    .getTeams().get(teamsListView.getSelectionModel().getSelectedIndex()).getTeamName());
            botsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .getBots()));
            botsListView.refresh();
        }
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

    }

    @FXML void teamNameTextFieldOnKeyReleased(KeyEvent event) {
        Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                .setTeamName(teamNameTextField.getText());

    }

    @FXML void addBotBtnOnAction(ActionEvent actionEvent){
        if (teamsListView.getSelectionModel().getSelectedIndex()!=-1) {
            Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .addBot(new Bot("Bot 1 ", "Dev 1"));
            botsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .getBots()));
            botsListView.refresh();
        }
        System.out.println("Test Add Bot");
    }

    @FXML void removeBotBtnOnAction(ActionEvent actionEvent){
        int selectedIndex = botsListView.getSelectionModel().getSelectedIndex();
        System.out.println("Test Remove Bot");

        if (selectedIndex != -1 && botsListView.getItems().size()!=1){
            Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .removeBot(selectedIndex);
            botsListView.setItems(FXCollections.observableArrayList(Tournament.get()
                    .getTeams().get(teamsListView.getSelectionModel().getSelectedIndex()).getBots()));
            botsListView.refresh();
        }
    }
    @FXML void addTeamBtnOnAction(ActionEvent actionEvent){
        System.out.println("Test Add Team");
        Tournament.get().addTeam(new Team("Team "+ Tournament.get().getTeams().size(),new ArrayList<Bot>(),0,""));
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
            System.out.println("Test Remove team");

            teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
            teamsListView.refresh();
        }
    }

    @FXML void configPathBtnOnAction(ActionEvent actionEvent){
    }
}
