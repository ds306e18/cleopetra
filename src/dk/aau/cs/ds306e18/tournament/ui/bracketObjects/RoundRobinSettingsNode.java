package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.RoundRobinFormat;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;

public class RoundRobinSettingsNode extends BorderPane {

    private final RoundRobinFormat roundRobin;

    public RoundRobinSettingsNode(RoundRobinFormat roundRobin) {
        this.roundRobin = roundRobin;

        // Rounds label
        Label roundsLabel = new Label("Groups:");
        setLeft(roundsLabel);

        // Rounds spinner
        Spinner<Integer> roundsSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
        roundsSpinner.setEditable(true);
        roundsSpinner.getValueFactory().setValue(roundRobin.getNumberOfGroups());
        roundsSpinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> {
            roundRobin.setNumberOfGroups(newValue);
        });
        setRight(roundsSpinner);
    }
}
