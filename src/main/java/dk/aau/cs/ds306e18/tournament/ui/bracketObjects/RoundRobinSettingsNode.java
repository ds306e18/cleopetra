package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.RoundRobinFormat;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class RoundRobinSettingsNode extends BorderPane {

    private final RoundRobinFormat roundRobin;

    public RoundRobinSettingsNode(RoundRobinFormat roundRobin) {
        this.roundRobin = roundRobin;

        // Groups label
        Label groupsLabel = new Label("Groups:");
        groupsLabel.setFont(new Font("System", 16));
        setLeft(groupsLabel);

        // Groups spinner
        Spinner<Integer> groupsSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
        groupsSpinner.setEditable(true);
        groupsSpinner.getValueFactory().setValue(roundRobin.getNumberOfGroups());
        groupsSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                int value = Integer.valueOf(newValue); //This will throw the exception if the value not only contains numbers
                roundRobin.setNumberOfGroups(value);

            } catch (NumberFormatException e) {
                groupsSpinner.getEditor().setText("1"); //Setting default value
            }
        });

        setRight(groupsSpinner);
    }
}
