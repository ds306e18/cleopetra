package dk.aau.cs.ds306e18.tournament.UI;

import javafx.scene.control.Tab;
import java.util.ArrayList;

public class NavigationFrameTest extends NavigationFrame {


    @Override
    ArrayList<Tab> addContent() {

        Tab tab1 = new Tab();
        Tab tab2 = new Tab();

        tab1.setText("Test 1");
        tab2.setText("Test 2");

        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.add(tab1);
        tabs.add(tab2);

        return tabs;
    }
}
