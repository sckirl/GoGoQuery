package UserInterface;

import java.util.ArrayList;

import Database.ManipulateData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Builder;

public class loginInterface implements Builder<Region> {
	private final Runnable sceneSwapper;
	private final Runnable shopperSwapper;
	private final Runnable managerSwapper;
	
	// Logo 
	Image image = new Image("file:./assets/image.png", 500, 500, true, true);
	ImageView logo = new ImageView(image);
	
	 Label loginLabel = new Label("Login");
	 TextField emailField = new TextField();
	 PasswordField passwordField = new PasswordField();
	 Button loginButton = new Button("Login");
	 
	 Label registerLabel = new Label("Are you new? ");
     Hyperlink registerLink = new Hyperlink("Register Here!");
     
     ManipulateData user = new ManipulateData("msuser");
	
	public loginInterface(Runnable registrationSwapper, 
			Runnable shopperSwapper, 
			Runnable managerSwapper) {
		this.sceneSwapper = registrationSwapper;
		this.shopperSwapper = shopperSwapper;
		this.managerSwapper = managerSwapper;
	}
	
	public void alert(String header, String context) {
    	// TODO lots of validation
    	
    	Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Insert error!");
        alert.setContentText(context);
        alert.setHeaderText(header);
        alert.show();
    }
 
 public void success(String header, String context) {
    	// TODO lots of validation
    	
    	Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Insert Success!");
        alert.setContentText(context);
        alert.setHeaderText(header);
        alert.show();
    }
	
	public boolean validate() {
        // Check if email or password fields are empty
        if (emailField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
        	alert("Log in failed", "Email and password cannot be empty");
            return false;
        }

        try {
            // Get the list of users with matching email
            ArrayList<ArrayList<String>> users = user.getIntersect("UserEmail", emailField.getText().trim());

            if (users.isEmpty()) {
            	alert("Wrong Credentials!", "You entered a wrong email or password");
                return false;
            }

            // Check password
            String storedPassword = users.get(0).get(3); // get the password
            if (!storedPassword.equals(passwordField.getText().trim())) {
            	alert("Wrong Credentials!", "You entered a wrong email or password");
                return false;
            }

            // Login successful, change screen to their appropriate screen
            System.out.println(users.get(0).get(5));
            if (users.get(0).get(5).equalsIgnoreCase("Manager")) {
            	this.managerSwapper.run();
            } else {
            	this.shopperSwapper.run();
            }
            return true;

        } catch (Exception e) {
        	alert("Error", "An error occurred during login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
	
	public void init() {
		 // Login Label
        loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        // Email Field
        emailField.setPromptText("Email");

        // Password Field
        passwordField.setPromptText("Password");

        // Login Button
        loginButton.setStyle("-fx-background-color: orange;"
        					+ " -fx-text-fill: white; -fx-font-size: 14px; "
        					+ "-fx-padding: 8px 16px;");

        // Registration Link
        registerLabel.setStyle(" -fx-text-fill: white;");
        registerLink.setTextFill(Color.ORANGE);
        registerLink.setStyle("-fx-underline: true;");
        
        // Styling Components
        loginLabel.setTextFill(Color.WHITE);
        emailField.setStyle("-fx-padding: 8px; -fx-font-size: 14px;");
        passwordField.setStyle("-fx-padding: 8px; -fx-font-size: 14px;");
	}
	
	public VBox put() {
		HBox registerBox = new HBox(registerLabel, registerLink);
        registerBox.setAlignment(Pos.CENTER);

        // Layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #2c2f33;");
        
     // Adding Components to Layout
        layout.getChildren().addAll(logo, 
        					loginLabel, 
        					emailField, 
        					passwordField, 
        					loginButton, registerBox);
        
        return layout;
	}
	
	// Builder method for changing the javafx window, credit to:
	@SuppressWarnings("exports")
	public Region build() {
		init();

		VBox layout = put();
        eventHandler();
        
        return layout;
    }
    public void eventHandler() {
    	// Actions taken when hyperlink is pressed or the loginButton is pressed
        loginButton.setOnAction(evt -> validate());
        registerLink.setOnAction(evt -> sceneSwapper.run());
    }
    
}
