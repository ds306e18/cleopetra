package dk.aau.cs.ds306e18.tournament.UI;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import java.util.ArrayList;

public class NavigationFrameTest extends NavigationFrame {


    @Override
    ArrayList<Tab> addContent() {

        //Create the tabs
        Tab tab1 = new Tab();
        Tab tab2 = new Tab();

        //Set content and attributes of tabs
        tab1.setText("Test 1");
        tab2.setText("Test 2");
        tab1.setContent(new Label("Hello"));

        //Add tabs to arrayList and return it
        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.add(tab1);
        tabs.add(tab2);

        return tabs;
    }
}
