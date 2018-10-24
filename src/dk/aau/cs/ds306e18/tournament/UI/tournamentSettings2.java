package dk.aau.cs.ds306e18.tournament.UI;

import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class tournamentSettings2 extends NavigationFrame {

    @Override
    ArrayList<Tab> addContent() {

        Tab tournamentSettings = new Tab();

        HBox contentAll = null;

        tournamentSettings.setContent(contentAll);

        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.add(tournamentSettings);

        return tabs;
    }
}
