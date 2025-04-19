package UserInterface;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.jar.Attributes.Name;

import Database.ManipulateData;
import Tables.User;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Builder;

public class registrationInterface implements Builder<Region> {
	private final Runnable regisSwapper;
	private final Runnable loginSwapper;
	private final Runnable roleSwapper;
	
	// logo
	Image image = new Image("file:./assets/image.png", 500, 500, true, true);
	ImageView logo = new ImageView(image);
	
	Label registerLabel = new Label("Register");
    TextField emailField = new TextField();
    PasswordField passwordField = new PasswordField();
    PasswordField confirmPasswordField = new PasswordField();
    DatePicker datePicker = new DatePicker();
    
    ToggleGroup genderGroup = new ToggleGroup();
    RadioButton maleButton = new RadioButton("Male");
    RadioButton femaleButton = new RadioButton("Female");
    HBox genderBox = new HBox(10, maleButton, femaleButton);
    
    CheckBox termsCheckBox = new CheckBox("I accept the Terms and Conditions");
    Button registerButton = new Button("Register");
    Label signInLabel = new Label("Already have an account? ");
    Hyperlink signInLink = new Hyperlink("Sign in Here!");
    
    ManipulateData data = new ManipulateData("msuser");
    
    interface validate {
        boolean verify();
    }
    
	public registrationInterface(Runnable regisSwapper, Runnable loginSwapper, Runnable roleSwapper) {
		this.regisSwapper = regisSwapper;
		this.loginSwapper = loginSwapper;
		this.roleSwapper = roleSwapper;
	}
	
    static void alert() {
    	// TODO lots of validation
    	
    	System.out.println("alert");
    	Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("DevLaunch Dialog");
        alert.setHeaderText("An error has been encountered");
        alert.show();
    }
    
    public void alert(String context) {
    	// TODO lots of validation
    	
    	Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(context);
        alert.setHeaderText("Register error!");
        alert.show();
    }
 
 public void success(String context) {
    	// TODO lots of validation
    	
    	Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(context);
        alert.setHeaderText("Register Success!");
        alert.show();
    }
    
    private void validate() {
    	// being fancy n using lambda and interface shiee, this is cool tho
    	// idk about performace or other thing in this one, but looks sick
    	
    	/*
    	 * Email, Password, Confirm Password, Date of Birth, Gender, and Terms and Condition Checkbox cannot be empty. 

			Email must be in email format, such as: 
			
			Email must ends with ‘@gomail.com’ 
			
			Email must not contain special characters, except for ‘@’, ‘_’, or ‘.’. 
			
			Email must be unique (not used by other user). 
			
			Password must be alphanumeric. 
			
			Password and Confirm Password must match. 
			
			Date of Birth must be older than 17 years old 
    	 */
    	String email = this.emailField.getText();
    	String password = this.passwordField.getText();
    	LocalDate dateofBirth = this.datePicker.getValue();
    	String gender = this.maleButton.isSelected() ? "Male" : 
    					this.femaleButton.isSelected() ? "Female" : null;;
    	
    	validate emailValidation = () ->
    		 {
    			 
    			  if (email == null || email.isEmpty()) {
    				  this.alert("Email cannot be empty.");
    				  return false;
    		        }

    		        if (!email.endsWith("@gomail.com")) {
    		        	this.alert("Email must ends with ‘@gomail.com’ ");
    		            return false;
    		        }

    		        if (!email.matches("[a-zA-Z0-9._@]+")) {
    		        	this.alert("Email must not contain special characters, except for ‘@’, ‘_’, or ‘.’. ");
    		            return false;
    		        }

    		        ArrayList<String> registeredEmails = data.getData("UserEmail");
    		        for (String registeredEmail : registeredEmails) {
    		            if (email.equals(registeredEmail)) {
    		            	this.alert("Email must be unique (not used by other user).");
    	    				  return false;
    		            }
    		        }

    		        return true;

            };
            
        validate passwordValidation = () -> {
        	
        	 if (password == null || password.isEmpty()) {
        		 this.alert("Password cannot be empty.");
                 return false;
             }

             if (!password.matches("[a-zA-Z0-9]+")) {
            	 this.alert("Password must be alphanumeric. ");
                 return false;
             }
             
             if (!password.equals(this.confirmPasswordField.getText())) {
            	 this.alert("Password and Confirm Password must match. ");
                 return false;
             }
             
             return true;
        };
        
        validate validateDOB = () ->
        {
        
            if(LocalDate.now().getYear() - dateofBirth.getYear()  <= 17) {
            	this.alert("Date of Birth must be older than 17 years old  ");
            	
            	return false;
            }
            return true;

        };
        
        try {
        	gender = this.genderGroup.getSelectedToggle().toString();
		} catch (Exception e) {
			this.alert("Gender cannot be empty  ");
			return;
		}
            
            
        if(emailValidation.verify() && 
        		passwordValidation.verify() &&
        		validateDOB.verify() && 
        		gender.endsWith("ale\'") ) {
        	
        	gender = gender.endsWith("\'Female\'") ? "female" : "male";
        	// TODO change the role accordingly
        	User user = new User(createID(), dateofBirth.toString(), email, password, gender, "shopper");
        	
        	user.sendData();
        	
        	this.success("Please log in with your newly created account");
        	this.roleSwapper.run();
        }

	}
    
    public int createID() {
		
		int latestID = 0;
		for (String id : data.getData("UserID")) {
			
			latestID = Math.max(latestID, Integer.valueOf(id));
		}
		
		return latestID + 1;
	}
    
    public void eventHandler() {
    	registerButton.setOnAction(
        		// evt -> sceneSwapper.run()
        		evt -> validate()
        		);
    	
    	this.signInLink.setOnAction(
    			evt -> this.loginSwapper.run()
    			);
    	
    }

    
	@Override
	public Region build() {

        // Register Label
        registerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        emailField.setPromptText("Email");
        passwordField.setPromptText("Password");
        confirmPasswordField.setPromptText("Confirm Password");

        // Date of Birth Field
        datePicker.setPromptText("Date of Birth");

        // Gender Radio Buttons
        maleButton.setToggleGroup(genderGroup);
        femaleButton.setToggleGroup(genderGroup);
        genderBox.setAlignment(Pos.CENTER);

        // Terms and Conditions Checkbox
        termsCheckBox.setTextFill(Color.WHITE);

        // Register Button

        registerButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px;");

        // Sign-in Link
        signInLabel.setTextFill(Color.WHITE);;
        signInLink.setTextFill(Color.ORANGE);
        signInLink.setStyle("-fx-underline: true;");
        HBox signInBox = new HBox(signInLabel, signInLink);
        signInBox.setAlignment(Pos.CENTER);

        // Layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #2c2f33;");

        // Styling Components
        registerLabel.setTextFill(Color.WHITE);
        emailField.setStyle("-fx-padding: 8px; -fx-font-size: 14px;");
        passwordField.setStyle("-fx-padding: 8px; -fx-font-size: 14px;");
        confirmPasswordField.setStyle("-fx-padding: 8px; -fx-font-size: 14px;");
        datePicker.setStyle("-fx-padding: 8px; -fx-font-size: 14px;");
        maleButton.setTextFill(Color.WHITE);
        femaleButton.setTextFill(Color.WHITE);
        
        eventHandler();
        
        
        // Adding Components to Layout
        layout.getChildren().addAll(
        		logo, registerLabel, emailField, passwordField, confirmPasswordField, datePicker, genderBox,
                termsCheckBox, registerButton, signInBox
        );
        
		return layout;
	}
	
 
 }
