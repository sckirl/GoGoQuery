package Manager;

import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public abstract class ManagerNavBar {
    protected final Runnable addSwapper;
    protected final Runnable manageSwapper;
    protected final Runnable loginSwapper;

    protected BorderPane mainPane = new BorderPane();
    protected MenuButton menuButton = new MenuButton("Menu");
    protected Label welcomeLabel = new Label("Welcome to GoGoQuery Manager");
    protected MenuItem add = new MenuItem("Add Item");
    protected MenuItem manage = new MenuItem("Manage Queue");
    protected MenuItem logout = new MenuItem("Log Out");

    public ManagerNavBar(Runnable addSwapper, 
                         Runnable manageSwapper, 
                         Runnable loginSwapper) {
        this.addSwapper = addSwapper;
        this.manageSwapper = manageSwapper;
        this.loginSwapper = loginSwapper;
    }

    protected BorderPane createNavBar() {
        menuButton.setGraphicTextGap(0);
        menuButton.getItems().addAll(add, manage, logout);
        
        add.setOnAction(evt -> this.addSwapper.run());
        manage.setOnAction(evt -> this.manageSwapper.run());
        logout.setOnAction(evt -> this.loginSwapper.run());
        
        mainPane.setTop(menuButton);
        return mainPane;
    }


    // method for navbar to be added in other screen
    protected abstract Region createContent();

}