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
        Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                .addBot(new Bot("Bot 1 ", "Dev 1"));
        botsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .getBots()));
        botsListView.refresh();

        System.out.println("Test Add Bot");
    }

    @FXML void removeBotBtnOnAction(ActionEvent actionEvent){
        int selectedIndex = botsListView.getSelectionModel().getSelectedIndex();
        System.out.println("Test Remove Bot");

        if (selectedIndex != -1){
            botsListView.setItems(FXCollections.observableArrayList(Tournament.get()
                    .getTeams().get(teamsListView.getSelectionModel().getSelectedIndex())
                    .removeBot(selectedIndex)));
            botsListView.refresh();
        }
    }
    @FXML void addTeamBtnOnAction(ActionEvent actionEvent){
        System.out.println("Test Add Team");
        Tournament.get().addTeam(new Team("Team 1",new ArrayList<Bot>(),0,""));
        teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
        teamsListView.refresh();
    }

    @FXML void removeTeamBtnOnAction(ActionEvent actionEvent){
        int selectedIndex = teamsListView.getSelectionModel().getSelectedIndex();
        System.out.println("Test Remove team");
        if (selectedIndex != -1){
            Tournament.get().removeTeam(selectedIndex);
            teamsListView.setItems(FXCollections.observableArrayList(Tournament.get().getTeams()));
            teamsListView.refresh();
        }
    }

    @FXML void configPathBtnOnAction(ActionEvent actionEvent){
    }
}
