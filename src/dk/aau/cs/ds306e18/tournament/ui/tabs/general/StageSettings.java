package dk.aau.cs.ds306e18.tournament.ui.tabs.general;

import dk.aau.cs.ds306e18.tournament.model.Stage;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class StageSettings extends VBox {

    private final TextField nameTextField;
    private final ChoiceBox<StageFormatOption> formatChoiceBox;
    private Insets standardPaddingInsets = new Insets(8, 8, 8, 8);

    private Stage stage;

    public StageSettings(Stage stage) {
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
        nameTextField.setOnKeyReleased(e -> this.stage.setName(nameTextField.getText()));
        nameBox.getChildren().addAll(nameLabel, nameTextField);
        nameBox.setPadding(standardPaddingInsets);

        // format
        BorderPane formatLayout = new BorderPane();
        Label formatLabel = new Label("format:");
        formatChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(StageFormatOption.values()));
        formatChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (this.stage != null && StageFormatOption.getOption(this.stage.getFormat()) != newValue) {
                this.stage.setFormat(newValue.getNewInstance());
            }
        });
        formatChoiceBox.getSelectionModel().select(0);
        formatLayout.setPadding(standardPaddingInsets);
        formatLayout.setLeft(formatLabel);
        formatLayout.setRight(formatChoiceBox);


        getChildren().addAll(header, nameBox, formatLayout);

        setStage(stage);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        if (stage == null) {
            setVisible(false);
        } else {
            setVisible(true);
            nameTextField.setText(stage.getName());
            formatChoiceBox.getSelectionModel().select(StageFormatOption.getOption(stage.getFormat()));
        }
    }
}
