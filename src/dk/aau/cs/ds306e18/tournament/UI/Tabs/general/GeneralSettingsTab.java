package dk.aau.cs.ds306e18.tournament.UI.Tabs.general;

import dk.aau.cs.ds306e18.tournament.UI.FixedTextField;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class GeneralSettingsTab extends Tab {

    private Insets standardPaddingInsets = new Insets(8, 8, 8, 8);

    /** GeneralSettingsTab extends Tab and creates a tab with a specific layout*/
    public GeneralSettingsTab() {

        this.setText("Tournament Settings");
        HBox content = new HBox();

        GeneralSettings generalSettings = new GeneralSettings();
        VBox filler = fillerBox();
        VBox secondContent = stageSettings();

        content.getChildren().addAll(generalSettings, filler, secondContent);
        this.setContent(content);
    }

    /** Creates an empty box */
    private VBox fillerBox(){
        VBox content = new VBox();
        content.setMinWidth(100);

        return content;
    }

    /**
     * Creates a StageSettings VBox, that contains a header, textfield, checkbox and menufield.
     * @return a specific VBOx with a header, textfield, checkbox
     */
    private VBox stageSettings (){
        VBox content = new VBox();
        content.setMinWidth(300);

        // Header
        VBox header = new VBox();
        Label headerText = new Label("Stage settings");
        headerText.setFont(new Font("Arial", 20));
        header.getChildren().addAll(headerText);
        header.setPadding(standardPaddingInsets);

        // Stage settings
        VBox settings = new VBox();
        Label stageName = new Label("Stage name:");
        FixedTextField stageNameTf = new FixedTextField();
        stageNameTf.setMaxWidth(250);
        Label format = new Label("Format:");
        MenuButton formatMenu = new MenuButton("Choose format");
        Label seedingMethod = new Label("Seeding method:");
        MenuButton seedingMenu = new MenuButton("Choose method");

        Label participants = new Label("Participants");
        participants.setFont(new Font("Arial", 15));
        participants.setPadding(new Insets(25, 0 ,5, 0));

        CheckBox checkBox = new CheckBox("Use top 3 participants from previous stage?");

        HBox buttons = new HBox();
        Button addBtn = new Button("Apply changes");
        Button deleteBtn = new Button("Revert");
        buttons.setSpacing(10);

        buttons.setPadding(new Insets(25, 0, 0 , 0));
        buttons.getChildren().addAll(addBtn, deleteBtn);

        settings.setPadding(standardPaddingInsets);
        settings.getChildren().addAll(stageName, stageNameTf, format, formatMenu,seedingMethod, seedingMenu,participants, checkBox, buttons);


        content.getChildren().addAll(header, settings);

        return content;
    }
}
