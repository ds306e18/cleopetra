package dk.aau.cs.ds306e18.tournament.UI;

import dk.aau.cs.ds306e18.tournament.UI.Tabs.BracketOverview;
import dk.aau.cs.ds306e18.tournament.UI.Tabs.general.GeneralSettingsTab;
import dk.aau.cs.ds306e18.tournament.UI.Tabs.ParticipantSettings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class NavigationFrame  extends VBox{

    private TabPane navigationTabs;

    /** NavigationFrame extends VBox and is the primary VBox shown in the application
     *  Navigationframe adds the tabs to a tabPane and adds a smaller buttom row on buttons.*/
    NavigationFrame() {

        navigationTabs = new TabPane();
        navigationTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        addTabsToPane(); //Calls the addContent and fills in tabs.

        HBox bottomNav = bottomNav();

        this.getChildren().addAll(navigationTabs, bottomNav);

    }


    /** Calls Creates new tabs and adds them to the pane. */
    public void addTabsToPane(){
        this.navigationTabs.getTabs().addAll(new GeneralSettingsTab(), new ParticipantSettings(), new BracketOverview());
    }

    /** Returns a HBox with two buttons aligned at the bottom of the scene*/
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
}
