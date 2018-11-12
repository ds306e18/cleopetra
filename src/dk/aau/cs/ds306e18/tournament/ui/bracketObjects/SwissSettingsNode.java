package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.SwissFormat;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;

public class SwissSettingsNode extends BorderPane {

    private final SwissFormat swiss;

    public SwissSettingsNode(SwissFormat swiss) {
        this.swiss = swiss;

        // Rounds label
        Label roundsLabel = new Label("Rounds:");
        setLeft(roundsLabel);

        // Rounds spinner
        Spinner<Integer> roundsSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
        roundsSpinner.setEditable(true);
        roundsSpinner.getValueFactory().setValue(swiss.getRoundCount());
        roundsSpinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> {
            swiss.setRoundCount(newValue);
        });
        setRight(roundsSpinner);
    }
}
