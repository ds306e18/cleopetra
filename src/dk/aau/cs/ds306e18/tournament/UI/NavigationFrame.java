package dk.aau.cs.ds306e18.tournament.UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public abstract class NavigationFrame extends Application {

    private TabPane navigationTabs;

    @Override
    public void start(Stage primaryStage) throws Exception {

        navigationTabs = new TabPane();
        navigationTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        addTabsToPane(); //Calls the addContent and fills in tabs.

        primaryStage.setScene(new Scene(navigationTabs));
        primaryStage.show();
    }

    private void addTabsToPane(){

        ArrayList<Tab> tabs = addContent();

        for(Tab tab : tabs){
            this.navigationTabs.getTabs().add(tab);
        }
    }

    /***/
    abstract ArrayList<Tab> addContent();

}
