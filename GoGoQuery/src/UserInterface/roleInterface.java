package UserInterface;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Builder;

public class roleInterface implements Builder<Region> {
	
	private Runnable shopperSwapper;
	private Runnable managerSwapper;
	
	// ==== Components of each profil

    
    // logo
 	Image image = new Image("file:./assets/image.png", 500, 500, true, true);
 	ImageView logo = new ImageView(image);
	
	public roleInterface(Runnable shopperSwapper, 
						Runnable managerSwapper) {
		this.shopperSwapper = shopperSwapper;
		this.managerSwapper = managerSwapper;
	}
	
	private VBox createRoleBox(String avatarText, 
								String role, 
								String description, 
								String buttonText,
								Runnable swapper) {
		
		// Avatar Circle
	    Label avatarLabel = new Label();
	    
	    // Online Indicator
	    Circle onlineIndicator = new Circle(8, Paint.valueOf("#00ff00"));
	    Label descriptionLabel = new Label();
		// Top avatar
		avatarLabel.setText(avatarText);
        avatarLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        avatarLabel.setTextFill(Color.WHITE);
        Circle avatar = new Circle(50, Paint.valueOf("#3e3e4e"));
        StackPane avatarPane = new StackPane(avatar, avatarLabel);

        StackPane.setAlignment(onlineIndicator, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(onlineIndicator, new Insets(0, 10, 10, 0));
        
        avatarPane.getChildren().addAll(onlineIndicator);

        // Role Label
        Label roleLabel = new Label(role);
        roleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        roleLabel.setTextFill(Color.WHITE);

        // Description Label
        descriptionLabel.setText(description);;
        descriptionLabel.setFont(Font.font("Arial", 14));
        descriptionLabel.setTextFill(Color.LIGHTGRAY);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(200);
        descriptionLabel.setAlignment(Pos.CENTER);

        // Register Button
        Button roleButton = new Button(buttonText);
        roleButton.setStyle("-fx-background-color: #ff9900; "
        						+ "-fx-text-fill: white; "
        						+ "-fx-font-size: 14px; "
        						+ "-fx-font-weight: bold; "
        						+ "-fx-padding: 10px 20px; "
        						+ "-fx-background-radius: 20;");
        
        // Set on mouse click on appropriate swapper that's inputed before
        roleButton.setOnAction(evt -> swapper.run());

        // Layout for Role Box
        VBox roleBox = new VBox(10, avatarPane, roleLabel, descriptionLabel, roleButton);
        roleBox.setAlignment(Pos.CENTER);
        roleBox.setPadding(new Insets(20));
        roleBox.setStyle("-fx-background-color: #2e2e3e; -fx-border-color: #ff9900; -fx-border-radius: 15; -fx-background-radius: 15;");

        return roleBox;
    }
	
	@Override
    public Region build() {
		// Explanation in WrapperLayoutBuilder

        // Manager Section
        VBox managerBox = createRoleBox("M", 
						        		"Manager", 
						        		"Manage products and deliveries, run the store!", 
						        		"Register as Manager",
						        		this.managerSwapper);

        // Shopper Section
        VBox shopperBox = createRoleBox("S", 
        								"Shopper", 
        								"Search products, manage your cart, go shopping!", 
        								"Register as Shopper",
        								this.shopperSwapper);

        // Layout for Manager and Shopper
        HBox rolesBox = new HBox(20, managerBox, shopperBox);
        rolesBox.setAlignment(Pos.CENTER);

        // Main layout
        VBox layout = new VBox(30, logo, rolesBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1e1e2e;");

        return layout;
    }

    public boolean validate() {
		
    	// TODO display error when account is not in database
    	// TODO display error when email or password is empty
    	return false;
    }

    
}
