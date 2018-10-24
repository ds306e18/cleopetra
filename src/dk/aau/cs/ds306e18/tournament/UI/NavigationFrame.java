package dk.aau.cs.ds306e18.tournament.UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public abstract class NavigationFrame  {

    private TabPane navigationTabs;

    public void startWindow(Stage primaryStage) throws Exception {

        navigationTabs = new TabPane();
        navigationTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        addTabsToPane(); //Calls the addContent and fills in tabs.

        primaryStage.setScene(new Scene(navigationTabs));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /** Calls addContent, and adds all the given tabs to the navigation bar. */
    private void addTabsToPane(){

        ArrayList<Tab> tabs = addContent();

        for(Tab tab : tabs){
            this.navigationTabs.getTabs().add(tab);
        }
    }

    /** This is where the content of the window goes.
     * Create tabs with the desired content and return them in an arrayList
     * @return an arrayList of tabs with content for the window. */
    abstract ArrayList<Tab> addContent();

}
