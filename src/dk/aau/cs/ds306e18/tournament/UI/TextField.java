package dk.aau.cs.ds306e18.tournament.UI;

public class TextField extends javafx.scene.control.TextField {

    private double maxWidth = 250;
    private double maxHeight = 20;

    TextField(){
        this.setMaxSize(maxWidth,maxHeight);
    }
}