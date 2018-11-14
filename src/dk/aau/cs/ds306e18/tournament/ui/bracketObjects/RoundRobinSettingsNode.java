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
        Spinner<Integer> GroupsSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
        GroupsSpinner.setEditable(true);
        GroupsSpinner.getValueFactory().setValue(roundRobin.getNumberOfGroups());
        GroupsSpinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> {
            roundRobin.setNumberOfGroups(newValue);
        });
        setRight(GroupsSpinner);
    }
}
