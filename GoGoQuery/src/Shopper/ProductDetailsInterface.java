package Shopper;

import java.util.ArrayList;
import java.util.HashMap;

import Database.ManipulateData;
import Tables.Cart;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Builder;

public class ProductDetailsInterface implements Builder<Region>, EventHandler<MouseEvent>{
	private final Runnable homeSwapper;
	private String currentItemId;
	
	ManipulateData item = new ManipulateData("MsItem");
	
	// Logo 
	Image image = new Image("file:./assets/image.png", 100, 100, true, true);
	ImageView logo = new ImageView(image);
	
	Label productPrice = new Label();
 	Label productCategory = new Label();
 	Label itemDetailLabel = new Label();
 	Hyperlink productTitle = new Hyperlink();
 	
 	Spinner<Integer> quantitySpinner = new Spinner<>(1, 100, 1);
 	
	public ProductDetailsInterface(Runnable cartSwapper, String itemId) {
		this.homeSwapper = cartSwapper;
		this.currentItemId = itemId;
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
	
	private VBox createProductDetail() {
		// TODO ACTUALLY DISPLAY ALL OF THIS ITEM DETAILS
		int idx = 0;
		ArrayList<String> itemIDs = item.getData("ItemID");
		
		for (int i = 0; i < itemIDs.size(); i++) {
			if(itemIDs.get(i).equals(this.currentItemId)) {
				idx = i;
				break;
			}
		}
		
		productTitle = new Hyperlink(item.getData("ItemName").get(idx));
		productPrice.setText(item.getData("ItemPrice").get(idx));
		productCategory.setText(item.getData("ItemCategory").get(idx));
		itemDetailLabel.setText(item.getData("ItemDesc").get(idx));
		
		
        // Styles (just to make everything clean)
		productTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        productTitle.setTextFill(Color.WHITE);
       
        productPrice.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        productPrice.setTextFill(Color.ORANGE);

        productCategory.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        productCategory.setTextFill(Color.LIGHTGRAY);

        itemDetailLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        itemDetailLabel.setTextFill(Color.WHITE);

        VBox productDetailBox = new VBox(
            productTitle,
            productPrice,
            productCategory,
            itemDetailLabel
        );
        
        productDetailBox.setAlignment(Pos.TOP_LEFT);
        productDetailBox.setSpacing(10);
        productDetailBox.setPadding(new Insets(20));
        productDetailBox.setStyle("-fx-background-color: #1e1f29;");
        
        return productDetailBox;
	}

    private HBox createNavBar() {
        // Left Section - Search Bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search Items in GoGoQuery Store");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-background-color: #282a36; -fx-text-fill: white; -fx-prompt-text-fill: #aaa;");
        
        Button searchButton = new Button("Search");
        searchButton.setPrefHeight(35);
        searchButton.setStyle("-fx-background-color: #b19cd9; -fx-text-fill: white; -fx-font-weight: bold; ");

        HBox searchBox = new HBox(searchField, searchButton);
        searchBox.setSpacing(10);

        // Right Section - Welcome and Log Out
        Label welcomeLabel = new Label("Welcome, user");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        welcomeLabel.setTextFill(Color.ORANGE);

        Button logOutButton = new Button("Log Out");
        logOutButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox userBox = new HBox(welcomeLabel, logOutButton);
        userBox.setSpacing(20);
        userBox.setAlignment(Pos.CENTER_RIGHT);

        // Combine Both Sections
        HBox navBar = new HBox(logo, searchBox, userBox);
        navBar.setSpacing(50);
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color: #1e1f29;");
        HBox.setHgrow(userBox, Priority.ALWAYS);

        return navBar;
    }

    private BorderPane createCenterContent() {
    	
        // ==== Left Section: Product Image ====
        Label productImagePlaceholder = new Label();
        productImagePlaceholder.setPrefSize(250, 250);
        productImagePlaceholder.setStyle("-fx-background-color: gray; "
        								+ "-fx-border-color: black;");
        productImagePlaceholder.setAlignment(Pos.CENTER);

        VBox productImageBox = new VBox(productImagePlaceholder);
        productImageBox.setStyle("-fx-background-color: #1e1f29; "
        						+ "-fx-border-color: black;");
        productImageBox.setAlignment(Pos.CENTER_LEFT);
        productImageBox.setPadding(new Insets(20));
        productImageBox.setSpacing(15);
    	
        
        // ==== Right Section: Buy Now ====
        Label selectQuantityLabel = new Label("Select Quantity:");
        selectQuantityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        selectQuantityLabel.setTextFill(Color.WHITE);

        
        quantitySpinner.setStyle("-fx-background-color: white;");

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setPrefWidth(120);
        addToCartButton.setStyle("-fx-background-color: #ff9500; "
        						+ "-fx-text-fill: white; "
        						+ "-fx-font-weight: bold;");
        
        
        VBox buyNowBox = new VBox(
            selectQuantityLabel,
            quantitySpinner,
            addToCartButton
        );
        buyNowBox.setAlignment(Pos.TOP_CENTER);
        buyNowBox.setSpacing(10);
        buyNowBox.setPadding(new Insets(20));
        buyNowBox.setStyle("-fx-background-color: #282a36; "
        				+ "-fx-border-color: #444; "
        				+ "-fx-border-width: 2;");
        
        
        // Event Handler 
        // most of the components here are local because they change so much
        // this is for reliability reasons
        addToCartButton.setOnAction(evt -> validate());
        
        // Combine All Sections
        BorderPane contentPane = new BorderPane();
        contentPane.setLeft(productImageBox);
        contentPane.setCenter(this.createProductDetail());
        contentPane.setRight(buyNowBox);
        
        logo.setOnMouseClicked(evt -> this.homeSwapper.run());
        return contentPane;
    }
    
    private int getCurrentStock() {
        int idx = 0;
        ArrayList<String> itemIDs = item.getData("ItemID");
        
        for (int i = 0; i < itemIDs.size(); i++) {
            if(itemIDs.get(i).equals(this.currentItemId)) {
                idx = i;
                break;
            }
        }
        
        // Retrieve current stock from the database
        String stockStr = item.getData("ItemStock").get(idx);
        return Integer.parseInt(stockStr);
    }

    
    public boolean validate() {
    	// TODO actually make this work
    	int currentStock = this.getCurrentStock();
        int spinnerValue = this.quantitySpinner.getValue();
        
        if (spinnerValue <= 0) {
            alert("Error", "Quantity must be a positive integer.");
            return false;
        }

        if (spinnerValue > currentStock) {
            alert("Error", "Selected quantity exceeds available stock.");
            return false;
        }


        sendToCart();
        this.success("Success", "Item added to cart!");
        return true;
    }
    
    private void sendToCart() {
    	ManipulateData msCart = new ManipulateData("mscart");
    	// TODO Set the actual UserID
		Cart itemToAdd = new Cart(1, 
							Integer.parseInt(currentItemId), 
							this.quantitySpinner.getValue());
		
		itemToAdd.sendData();
	}
    
	@Override
	public Region build() {

        // ==== Top Navigation Bar ====
        HBox navBar = createNavBar();

        // ==== Center Content (Product Detail) ====
        BorderPane centerContent = createCenterContent();
        
        
        // ==== Main Layout ====
        BorderPane layout = new BorderPane();
        layout.setTop(navBar);
        layout.setCenter(centerContent);

        return layout;
	}

	@Override
	public void handle(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

}

