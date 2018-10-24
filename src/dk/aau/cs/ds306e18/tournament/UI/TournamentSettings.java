package dk.aau.cs.ds306e18.tournament.UI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class TournamentSettings extends NavigationFrame {

    private Insets standardPaddingInsets = new Insets(5, 8, 8, 8);
    private int id = 1;

    @Override
    ArrayList<Tab> addContent() {

        Tab tournamentSettings = new Tab();
        tournamentSettings.setText("Tournament Settings");

        HBox contentAll = new HBox();
        VBox mainContent = mainSettings();
        VBox secondContent = stageSettings();

        contentAll.getChildren().addAll(mainContent, secondContent);

        tournamentSettings.setContent(contentAll);

        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.add(tournamentSettings);

        return tabs;
    }

    private VBox mainSettings(){
        VBox content = new VBox();
        content.setMinWidth(200);

        // Header
        VBox header = new VBox();
        Label headerText = new Label("Tournament settings");
        headerText.setFont(new Font("Arial", 20));
        header.getChildren().addAll(headerText);
        header.setPadding(standardPaddingInsets);

        // Tournament general settings
        VBox settings = new VBox();
        Label tournamentName = new Label("Tournament name:");
        TextField textField = new TextField();

        settings.setPadding(standardPaddingInsets);
        settings.getChildren().addAll(tournamentName, textField);

        // Tiebreaker
        HBox tieBreak = new HBox();
        Label text2 = new Label("Tiebreaker rule: ");
        MenuButton menuButton = new MenuButton("Choose rule");
        menuButton.getItems().addAll(new MenuItem("Setting 1"), new MenuItem("Setting 2"));

        tieBreak.setPadding(standardPaddingInsets);
        tieBreak.getChildren().addAll(text2, menuButton);

        // Stage overview table
        Label stageHeader = new Label("Stages");
        stageHeader.setFont(new Font("Arial", 15));

        TableView table = new TableView();
        table.setEditable(true);

        TableColumn stageIdCol = new TableColumn("ID");
        stageIdCol.setResizable(false);
        stageIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn stageNameCol = new TableColumn("Stage name");
        stageNameCol.setResizable(false);
        stageNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().addAll(stageIdCol, stageNameCol);

        HBox buttons = new HBox();
        Button addBtn = new Button("Add stage");
        Button deleteBtn = new Button("Delete stage");
        buttons.setSpacing(10);

        addBtn.setOnAction(e -> {
            dk.aau.cs.ds306e18.tournament.UI.Stage tempStage = new dk.aau.cs.ds306e18.tournament.UI.Stage(id, "Stage " + id);
            id += 1;
            table.getItems().add(tempStage);
        });

        deleteBtn.setOnAction(e -> {
            if (table.getSelectionModel().getSelectedItem() != null) {
                Object selectedItem = table.getSelectionModel().getSelectedItem();
                table.getItems().remove(selectedItem);
                id -= 1;
            }
        });

        buttons.getChildren().addAll(addBtn, deleteBtn);


        VBox stageBox = new VBox();
        stageBox.setSpacing(5);
        stageBox.setPadding(standardPaddingInsets);
        stageBox.getChildren().addAll(stageHeader, table, buttons);



        content.getChildren().addAll(header, settings, tieBreak, stageBox);

        return content;
    }

    private VBox stageSettings (){
        VBox content = new VBox();
        content.setMinWidth(500);

        // Header
        VBox header = new VBox();
        Label headerText = new Label("Stage settings");
        headerText.setFont(new Font("Arial", 20));
        header.getChildren().addAll(headerText);
        header.setPadding(standardPaddingInsets);

        // Stage settings
        VBox settings = new VBox();
        Label stageName = new Label("Stage name:");
        TextField stageNameTf = new TextField();
        Label format = new Label("Stage name:");

        Label seedingMethod = new Label("Stage name:");

        Label participants = new Label("Participants");
        participants.setFont(new Font("Arial", 15));

        settings.setPadding(standardPaddingInsets);
        settings.getChildren().addAll(stageName);

        content.getChildren().addAll(header, settings);

        return content;
    }
}
