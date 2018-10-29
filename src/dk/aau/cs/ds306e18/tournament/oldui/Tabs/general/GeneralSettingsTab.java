package dk.aau.cs.ds306e18.tournament.oldui.Tabs.general;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class GeneralSettingsTab extends Tab {

    private Insets standardPaddingInsets = new Insets(8, 8, 8, 8);

    /** GeneralSettingsTab extends Tab and creates a tab with a specific layout*/
    public GeneralSettingsTab() {

        this.setText("Tournament Settings");
        HBox content = new HBox();

        GeneralSettings generalSettings = new GeneralSettings();
        StageSettings stageSettings = new StageSettings(null);
        generalSettings.setOnStageSelected(stage -> stageSettings.setStage(stage));
        VBox filler = fillerBox();

        content.getChildren().addAll(generalSettings, filler, stageSettings);
        this.setContent(content);
    }

    /** Creates an empty box */
    private VBox fillerBox(){
        VBox content = new VBox();
        content.setMinWidth(100);

        return content;
    }
}
