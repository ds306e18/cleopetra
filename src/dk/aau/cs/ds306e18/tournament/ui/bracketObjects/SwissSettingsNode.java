package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.SwissFormat;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class SwissSettingsNode extends BorderPane {

    private final SwissFormat swiss;

    public SwissSettingsNode(SwissFormat swiss) {
        this.swiss = swiss;

        // Rounds label
        Label roundsLabel = new Label("Rounds:");
        roundsLabel.setFont(new Font("System", 16));
        setLeft(roundsLabel);

        // Rounds spinner
        Spinner<Integer> roundsSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
        roundsSpinner.setEditable(true);
        roundsSpinner.getValueFactory().setValue(swiss.getRoundCount());
        roundsSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                int value = Integer.valueOf(newValue); //This will throw the exception if the value not only contains numbers
                swiss.setRoundCount(value);

            } catch (NumberFormatException e) {
                roundsSpinner.getEditor().setText("1"); //Setting default value
            }
        });

        setRight(roundsSpinner);
    }
}
