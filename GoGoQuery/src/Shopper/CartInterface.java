package Shopper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import Database.ManipulateData;
import Tables.TransactionDetail;
import Tables.TransactionHeader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Builder;

public class CartInterface implements Builder<Region> {
	private final Runnable homeSwapper;
	
	// Logo 
	Image image = new Image("file:./assets/image.png", 100, 100, true, true);
	ImageView logo = new ImageView(image);

	private TextField searchField = new TextField();
	private Button searchButton = new Button("Search");
	private Label welcomeLabel = new Label();
	private Button logOutButton = new Button("Log out");;
	private Button checkoutButton = new Button("Checkout Items");
	private String username = "Alvin";
	
	private ManipulateData msCart = new ManipulateData("mscart");
	private ManipulateData msItem = new ManipulateData("msitem");
	
	public CartInterface(Runnable sceneSwapper) {
		this.homeSwapper = sceneSwapper;
	}
	
	private ScrollPane createProductList() {
		
		// Product list
		ScrollPane root =  new ScrollPane();
        VBox productList = new VBox(15);
        
		ArrayList<String> cartList = msCart.getData("ItemID"); // id in all carts
		ArrayList<String> quantList = msCart.getData("Quantity");
		ArrayList<String> idList = msItem.getData("ItemID"); // id in all list of items
	
		if (cartList.isEmpty()) {
	        alert("Empty Cart", "Your cart is empty. Please add items before checkout.");
	        productList.getChildren().add(new Label("No Item in Cart"));
	        return root;
	    }
		
		// IM SO SORRY FOR 3D ARRAY
		ArrayList<ArrayList<ArrayList<String>>> res = new ArrayList<ArrayList<ArrayList<String>>> ();
		
		// Just get the intersect of msItem and msCart,
		// the proper way should be using SQL commands, but the work is too much
		// and will take long to modify connect.java
		
		// . . . did it anyway
		for (int i = 0; i < cartList.size(); i++) {
			// over-engineered intersection in java sql 
			// im not proud
			res.add(msItem.getIntersect("ItemID", cartList.get(i)));
		}
		
		for (int i = 0; i < res.size(); i++) {
			for (ArrayList<String> filteredItem : res.get(i)) {
				VBox productItem = this.createProductItem( 
						filteredItem.get(1),// get the name
						filteredItem.get(2), // get the price
						quantList.get(i) // get the Quantity
				 );

				productList.getChildren().add(productItem);	
		}

			
		}
		
        productList.setPadding(new Insets(20, 0, 20, 0));
        productList.setStyle("-fx-background-color: #1e1f29;");
        root.setContent(productList);
        
        return root;
	}

	private HBox createNavBar() {
		
    	// Left Section - Search Bar
		HBox searchBox = new HBox();
        searchField.setPromptText("Search Items in GoGoQuery Store");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #282a36; "
        		+ "-fx-text-fill: white; "
        		+ "-fx-prompt-text-fill: #aaa; "
        		+ "-fx-background-radius: 15;");
       
        searchButton.setStyle("-fx-background-color: #b19cd9; "
        		+ "-fx-text-fill: white; "
        		+ "-fx-font-weight: bold; ");
        searchBox.setSpacing(10);
        searchBox.getChildren().addAll(searchField, searchButton);

        // Welcome and Log Out
        welcomeLabel.setText("Welcome " + username);
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        welcomeLabel.setTextFill(Color.ORANGE);

        logOutButton.setStyle("-fx-background-color: #ff4444; "
        		+ "-fx-text-fill: white; "
        		+ "-fx-font-weight: bold;");
        HBox userBox = new HBox(welcomeLabel, logOutButton);
        userBox.setSpacing(20);
        userBox.setAlignment(Pos.CENTER_RIGHT);

        // Combine
        HBox navBar = new HBox(logo, searchBox, userBox);
       
        navBar.setSpacing(50);
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color: #1e1f29;");
        HBox.setHgrow(navBar, Priority.ALWAYS);
        
        return navBar;
    }
	

    private VBox createProductItem(String productName, String price, String stock) {
    	
    	// Steal the product item from shopper home idk idc yes yes
        VBox productList = new VBox(10);
        productList.setPadding(new Insets(10));
        productList.setStyle("-fx-background-color: #3e3e4e; "
        					+ "-fx-border-color: #555555;");
        
        VBox itemDetails = new VBox(10);
        HBox itemBox = new HBox(10);
        itemBox.setPadding(new Insets(10));
        itemBox.setStyle("-fx-background-color: #2e2e3e; -fx-border-color: #555555; -fx-border-radius: 5px;");

        ImageView itemImage = new ImageView(); // Placeholder for product image
        itemImage.setFitWidth(50);
        itemImage.setFitHeight(50);
        itemImage.setStyle("-fx-background-color: gray;");

        Label itemName = new Label(productName);
        itemName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        itemName.setTextFill(Color.WHITE);

        Label itemPrice = new Label(price);
        itemPrice.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        itemPrice.setTextFill(Color.GOLD);

        Label productStock = new Label(stock);
        productStock.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        productStock.setTextFill(Color.RED);

        itemDetails.getChildren().addAll(itemName, itemPrice, productStock);
        itemBox.getChildren().addAll(itemImage, itemDetails);
     
        productList.getChildren().add(itemBox);
       
        return productList;
        
//        // old ones
//    	  this is ugly lemme change the thing
//        GridPane productWindow = new GridPane();
//        
//        
//        productLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//        productLabel.setTextFill(Color.WHITE);
//        productLabel.setStyle("-fx-background-color: #282a36; -fx-padding: 1; -fx-background-radius: 20;");
//        stockLabel.setStyle("-fx-background-color: RED; -fx-padding: 1; -fx-background-radius: 20;");
//        
//        productWindow.addRow(0, productLabel);
//        productWindow.add(priceLabel, 1, 1);
//        productWindow.add(stockLabel, 2, 1);
//
//        productWindow.setPadding(new Insets(15));
//        productWindow.setStyle("-fx-background-color: #1e1f29; -fx-background-radius: 10;");

    }

    private double calculateTotalPrice() {
        ArrayList<String> cartList = msCart.getData("ItemID"); // id in all carts
        ArrayList<String> quantList = msCart.getData("Quantity");
        double totalPrice = 0.0;

        // Iterate through the cart items
        for (int i = 0; i < cartList.size(); i++) {
            // Get the intersect of cart items with item details
            ArrayList<ArrayList<String>> itemDetails = msItem.getIntersect("ItemID", cartList.get(i));
            
            // Each intersect should return a list with item details
            if (!itemDetails.isEmpty()) {
                ArrayList<String> item = itemDetails.get(0);
                
                // get the price, just like the above methods 
                double itemPrice = Double.parseDouble(item.get(2));
                
                // get the quantity
                int quantity = Integer.parseInt(quantList.get(i));
                
                // Calculate total for this item
                totalPrice += itemPrice * quantity;
            }
        }
        
        return totalPrice;
    }

    private VBox createBillingSummary() {
        double totalPrice = calculateTotalPrice();
        
        Label totalLabel = new Label(String.format("Total: $%.2f", totalPrice));
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

       
        checkoutButton.setStyle("-fx-background-color: #ff9900; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-background-radius: 10;");

        VBox billingSummary = new VBox(20, totalLabel, checkoutButton);
        billingSummary.setPadding(new Insets(20));
        billingSummary.setAlignment(Pos.TOP_CENTER);
        billingSummary.setStyle("-fx-background-color: #2e2e3e; -fx-border-color: #ff9900; -fx-border-radius: 10; -fx-background-radius: 10;");

        return billingSummary;
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

	 public void eventHandler() {
		 logo.setOnMouseClicked(evt -> this.homeSwapper.run());
		 checkoutButton.setOnMouseClicked(evt -> validate());
	 }
	 
	 public int createID() {
			ManipulateData data = new ManipulateData("TransactionHeader");
			int latestID = 0;
			for (String id : data.getData("ItemID")) {
				
				latestID = Math.max(latestID, Integer.valueOf(id));
			}
			
			return latestID + 1;
		}

	 private void validate() {
		    // Check if cart is empty
		    ArrayList<String> cartList = msCart.getData("ItemID");
		    if (cartList.isEmpty()) {
		        alert("Empty Cart", "Your cart is empty. Cannot proceed with checkout.");
		        return;
		    }

		    // Confirm checkout
		    Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
		    confirmationAlert.setTitle("Checkout Confirmation");
		    confirmationAlert.setHeaderText("Confirm Cart Checkout");
		    confirmationAlert.setContentText("Are you sure you want to checkout these items?");

		    Optional<ButtonType> result = confirmationAlert.showAndWait();
		    if (result.isPresent() && result.get() == ButtonType.OK) {
		        try {
		            int transactionID = createID();


		            LocalDate currentDate = LocalDate.now();
		            String dateCreated = currentDate.toString();

		            TransactionHeader transactionHeader = new TransactionHeader(
		                transactionID, 
		                999, 
		                dateCreated, 
		                "In Queue"
		            );
		            transactionHeader.sendData(); 

		            // get each cart items
		            ArrayList<String> quantityList = msCart.getData("Quantity");
		            for (int i = 0; i < cartList.size(); i++) {
		                int itemID = Integer.parseInt(cartList.get(i));
		                int quantity = Integer.parseInt(quantityList.get(i));

		                // get Transaction Detail
		                TransactionDetail transactionDetail = new TransactionDetail(
		                    transactionID, 
		                    itemID, 
		                    quantity
		                );
		                
		                transactionDetail.sendData(); // send data to database

		            }

		            // show alert, somehow giving idk error TODO
		            success("Transaction Successful", "Your order is now in queue");

		        } catch (Exception e) {
		            alert("Transaction Error", "An error occurred during checkout: " + e.getMessage());
		        }
		    }
		}


	@Override
	public Region build() {

        // Cart title
        Label cartTitle = new Label(this.username+"'s Cart");
        cartTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #ff9900;");

        // Subheader
        Label subheader = new Label("Showing "+ msCart.getData("ItemID").size()+" products in cart");
        subheader.setStyle("-fx-font-size: 14px; -fx-text-fill: #bbbbbb;");

        ScrollPane productList =  this.createProductList();
        // Billing summary
        VBox billingSummary = createBillingSummary();

        // Main layout
        HBox mainContent = new HBox(30, productList, billingSummary);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(20));

        eventHandler();
        VBox layout = new VBox(20, createNavBar(), 
					        		cartTitle, 
					        		subheader, 
					        		mainContent);
        
        
        layout.setStyle("-fx-background-color: #1e1e2e;");

        return layout;
	}
}
