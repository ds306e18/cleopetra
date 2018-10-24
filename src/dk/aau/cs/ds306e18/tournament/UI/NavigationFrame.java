package dk.aau.cs.ds306e18.tournament.UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public abstract class NavigationFrame  {

    private TabPane navigationTabs;

    public void startWindow(Stage primaryStage) throws Exception {

        navigationTabs = new TabPane();
        navigationTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        addTabsToPane(); //Calls the addContent and fills in tabs.

        VBox mainContent = new VBox();
        HBox bottomNav = bottomNav();

        mainContent.getChildren().addAll(navigationTabs, bottomNav);

        primaryStage.setScene(new Scene(mainContent));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void startWindow(Stage primaryStage, ArrayList<Tab> tabs) throws Exception {

        navigationTabs = new TabPane();
        navigationTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        primaryStage.setScene(new Scene(navigationTabs));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    /** Calls addContent, and adds all the given tabs to the navigation bar. */
    public void addTabsToPane(){

        ArrayList<Tab> tabs = addContent();

        for(Tab tab : tabs){
            this.navigationTabs.getTabs().add(tab);
        }
    }

    private HBox bottomNav(){
        HBox bottomNavigation = new HBox();

        Button prevBtn = new Button("< Prev");
        Button nextBtn = new Button("Next >");

        bottomNavigation.setAlignment(Pos.BASELINE_RIGHT);
        bottomNavigation.setSpacing(15);
        bottomNavigation.setPadding(new Insets(5));
        bottomNavigation.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                null, null, null)));

        bottomNavigation.getChildren().addAll(prevBtn, nextBtn);

        return bottomNavigation;
    }

    /** This is where the content of the window goes.
     * Create tabs with the desired content and return them in an arrayList
     * @return an arrayList of tabs with content for the window. */
    abstract ArrayList<Tab> addContent();

}
