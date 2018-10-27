package dk.aau.cs.ds306e18.tournament.UI.Tabs.general;

import dk.aau.cs.ds306e18.tournament.UI.FixedTextField;
import dk.aau.cs.ds306e18.tournament.model.Format;
import dk.aau.cs.ds306e18.tournament.model.TieBreaker;
import dk.aau.cs.ds306e18.tournament.model.TieBreakerBySeed;
import dk.aau.cs.ds306e18.tournament.model.Tournament;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class StageSettings extends VBox {

    private final TextField nameTextField;
    private final ChoiceBox<Format> formatChoiceBox;
    private Insets standardPaddingInsets = new Insets(8, 8, 8, 8);

    public StageSettings() {
        setMinWidth(260);

        // Header
        VBox header = new VBox();
        Label headerText = new Label("Stage settings");
        headerText.setFont(new Font("Arial", 20));
        header.getChildren().addAll(headerText);
        header.setPadding(standardPaddingInsets);

        // Name
        VBox nameBox = new VBox();
        Label nameLabel = new Label("Stage name:");
        nameTextField = new TextField();
        nameBox.getChildren().addAll(nameLabel, nameTextField);
        nameBox.setPadding(standardPaddingInsets);

        // Format
        BorderPane formatLayout = new BorderPane();
        Label formatLabel = new Label("Format:");
        formatChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList());
        formatChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {});
        formatChoiceBox.getSelectionModel().select(0);
        formatLayout.setPadding(standardPaddingInsets);
        formatLayout.setLeft(formatLabel);
        formatLayout.setRight(formatChoiceBox);


        getChildren().addAll(header, nameBox, formatLayout);
    }
}
