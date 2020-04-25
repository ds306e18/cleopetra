package dk.aau.cs.ds306e18.tournament.ui;

import javafx.scene.control.SpinnerValueFactory;

/**
 * A SpinnerValueFactory for scores. It accepts integers in a given range as well as empty strings and "-".
 */
public class ScoreSpinnerValueFactory extends SpinnerValueFactory<String> {

    private final int min;
    private final int max;

    public ScoreSpinnerValueFactory(int min, int max) {
        assert min <= max;
        this.min = min;
        this.max = max;
    }

    @Override
    public void decrement(int steps) {
        int value = getValueAsInt();
        setValue(String.valueOf(Math.max(value - 1, min)));
    }

    @Override
    public void increment(int steps) {
        int value = getValueAsInt();
        setValue(String.valueOf(Math.min(value + 1, max)));
    }

    private int getValueAsInt() {
        String s = getValue();
        if (s == null || "".equals(s) || "-".equals(s)) return 0;
        else return Integer.parseInt(s);
    }
}
