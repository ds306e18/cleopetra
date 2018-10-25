package dk.aau.cs.ds306e18.tournament.UI;

public class TextField extends javafx.scene.control.TextField {

    double width = 250;
    double height = 20;

    TextField(){
        this.setMaxSize(width,height);
    }
}