package Manager;

import Database.ManipulateData;
import Tables.Item;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;

public class AddItemInterface extends ManagerNavBar implements Builder<Region> {
	private Runnable sceneSwapper;
	 
	 HBox topMenu = new HBox();
	 Label menuLabel = new Label("Menu");
	 Label title = new Label("Add Item");
	 Label nameLabel = new Label("Item Name:");
     TextField nameField = new TextField();

     Label descLabel = new Label("Item Desc:");
     TextArea descField = new TextArea();
     
     Label categoryLabel = new Label("Item Category:");
     TextField categoryField = new TextField();

     Label priceLabel = new Label("Item Price:");
     TextField priceField = new TextField();

     Label quantityLabel = new Label("Quantity:");
     ComboBox<Integer> quantityBox = new ComboBox<>();
     
     Button addButton = new Button("Add Item");

     public AddItemInterface(Runnable addSwapper, Runnable manageSwapper, Runnable loginSwapper) {
 		super(addSwapper, manageSwapper, loginSwapper);
 	}
	
	 public void alert(String header, String context) {
	    	
	    	Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Insert error!");
	        alert.setContentText(context);
	        alert.setHeaderText(header);
	        alert.show();
	    }
	 
	 public void success(String header, String context) {
	    	
	    	Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Insert Success!");
	        alert.setContentText(context);
	        alert.setHeaderText(header);
	        alert.show();
	    }
	
	private void validate() {
		/*
		 * Validations: 

			All fields must be filled out. 
			Item name must be between 5 and 70 characters. 
			Item description must be between 10 and 255 characters.
			Quantity must be a Double and between $0.50 and $900,000. 
			Spinner value must be a positive integer and cannot be more than 300 
		 * 
		 */
		 // First, check for null or empty strings
		String name = nameField.getText();
		String description = descField.getText();
		String price = priceField.getText();
		String category = categoryField.getText();
		Integer spinnerVal = quantityBox.getValue();
		
			if(name.length() == 0 || description.length() == 0 || price.length() == 0
					|| category.length() == 0 ) {
				this.alert("Insert Error!", "All Field must be filled out");
				return;
			}
		
		    if (!(name.length() >= 5 && name.length() <= 70)) {
		    	this.alert("Insert Error!", "Item name must be between 5 and 70 Characters.");
		    	return;
		    }

		    if (!(description.length() >= 10 && description.length() <= 255)) {
		    	this.alert("Insert Error!", "Item description must be between 10 and 255 characters");
		    	return;
		    }
		    
		    Double dprice;
		    try {
		    	dprice = Double.parseDouble(price);
		    }  catch (NumberFormatException e) {
		    	this.alert("Insert Error!", "Item price must be a valid number");
		    	return;
			}
		    if (!(dprice >= 0.50 && dprice <= 900000)) {
		    	this.alert("Insert Error!", "Item price must be between $0.50 and $900,000");
		    	return;
		    }

		    
		    if (!(spinnerVal > 0 && spinnerVal <= 300)) {
		    	this.alert("Insert Error!", "Spinner value must be a positive integer and cannot be more than 300 ");
		    	return;
		    }
		    
		    // Send the message if it goes through all of these validations
		    // TODO make the data to be inputed to the database
		    Item item = new Item(createID(),
		    					name, 
		    					dprice, 
		    					description, 
		    					spinnerVal,
		    					category);
		    item.sendData();
		    this.success("Insert Success!", "Item added to product catalog");
	    	return;
	}
	
	public int createID() {
		ManipulateData data = new ManipulateData("msItem");
		int latestID = 0;
		for (String id : data.getData("ItemID")) {
			
			latestID = Math.max(latestID, Integer.valueOf(id));
		}
		
		return latestID + 1;
	}

	
	public void set() {
// Top menu bar placeholder
        topMenu.setStyle("-fx-background-color: #2e2e3e;");
        topMenu.setPadding(new Insets(10));
        menuLabel.setStyle("-fx-font-size: 16px; "
        					+ "-fx-text-fill: white;");
        topMenu.getChildren().add(menuLabel);

        // Form title
        title.setStyle("-fx-font-size: 35px; "
        		+ "-fx-font-weight: bold; "
        		+ "-fx-text-fill: #1e1e2e;");

        // Form fields
        descField.setPrefRowCount(4);
	}
	
	public void eventHandler() {
		addButton.setOnAction(
				evt -> validate()
		);
	}

	@Override
	public Region build() {
		
		set();
	        for (int i = 1; i <= 300; i++) {
	            quantityBox.getItems().add(i);
	        }
	        quantityBox.getSelectionModel().selectFirst();

	        addButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white; -fx-font-weight: bold;");

        // Layout for form fields
        VBox formLayout = new VBox(10,
                nameLabel, nameField,
                descLabel, descField,
                categoryLabel, categoryField,
                priceLabel, priceField,
                quantityLabel, quantityBox,
                addButton);
        formLayout.setPadding(new Insets(20));
        formLayout.setStyle("-fx-background-color: #a9c4e7; -fx-border-color: #2e2e3e; -fx-border-width: 2px;");
        formLayout.setAlignment(Pos.CENTER);

        // Main layout
        VBox layout = new VBox(createNavBar(), topMenu, title, formLayout);
        layout.setSpacing(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f4f4f4;");
        
        eventHandler();

        return layout;
	}

	@Override
	protected Region createContent() {
		// TODO Auto-generated method stub
		return null;
	}

}
