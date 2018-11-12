package dk.aau.cs.ds306e18.tournament.ui.bracketObjects;

import dk.aau.cs.ds306e18.tournament.model.format.SwissFormat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class SwissOptionsNode extends BorderPane {

    private final SwissFormat swiss;

    public SwissOptionsNode(SwissFormat swiss) {
        this.swiss = swiss;

        TextField roundsTextField = new TextField("Rounds:");
        setLeft(roundsTextField);

        Spinner<Integer> roundsSpinner = new Spinner<>(1, Integer.MAX_VALUE, 3);
        roundsSpinner.valueFactoryProperty().addListener(new ChangeListener<SpinnerValueFactory<Integer>>() {
            @Override
            public void changed(ObservableValue<? extends SpinnerValueFactory<Integer>> observable, SpinnerValueFactory<Integer> oldValue, SpinnerValueFactory<Integer> newValue) {
                swiss
            }
        });
    }
}
