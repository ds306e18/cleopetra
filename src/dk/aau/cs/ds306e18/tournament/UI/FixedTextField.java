package dk.aau.cs.ds306e18.tournament.UI;

public class FixedTextField extends javafx.scene.control.TextField {

    private final double maxWidth = 250;
    private final double maxHeight = 20;
    private final double minWidth = 100;
    private final double minHeight = 20;

    /** A simple class with specific settings, replaces all other textField objects*/
    public FixedTextField(){
        this.setMaxSize(maxWidth,maxHeight);
        this.setMinSize(minWidth,minHeight);
    }
}