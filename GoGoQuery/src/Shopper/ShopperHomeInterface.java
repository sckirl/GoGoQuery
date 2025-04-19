package Shopper;

import java.util.ArrayList;

import Database.Connect;
import Database.ManipulateData;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Builder;

public class ShopperHomeInterface implements Builder<Region>  {
	private final Runnable detailSwapper;
	private final Runnable cartSwapper;
	private final Runnable logoutSwapper;
	private final Runnable refreshWindow;
	
	GridPane contentPane = new GridPane();
	
	// Logo 
	Image image = new Image("file:./assets/image.png", 150, 150, true, true);
	ImageView logo = new ImageView(image);
	
	// navbar initalize
		TextField searchField = new TextField();
	 Button searchButton = new Button("Search");
	 HBox searchBox = new HBox(searchField, searchButton);
	 
	 Label welcomeLabel = new Label("Welcome, alvin");
	 Button logOutButton = new Button("Log Out");
	 Button myCart = new Button("My Cart");
	 
	
	 HBox userBox = new HBox(welcomeLabel, myCart, logOutButton);
	 
	 
	 Label filterLabel = new Label("Filter");
	 Label categoryLabel = new Label("Category");
	 Label productLabel =  new Label();
	
	 // filter box initialize
	MenuButton categoryField = new MenuButton("Select a Category");
	VBox mainPane;
		
	 
	 ArrayList<String> itemList = new ArrayList<>();
	 ArrayList<String> priceList = new ArrayList<>();
	 ArrayList<String> stockList = new ArrayList<>();
	 

	 public ShopperHomeInterface(Runnable sceneSwapper, 
    		 Runnable cartSwapper, Runnable logoutSwapper,
			Runnable refreshWindow) {
    	 this.detailSwapper = sceneSwapper;
	  		this.cartSwapper = cartSwapper;
	  		this.logoutSwapper = logoutSwapper;
	  		this.refreshWindow = refreshWindow;
	}
	 
	private ManipulateData data;
	private String selectedItemId;

	
	// Add a method to update the product details scene with a specific item ID
    private void showProductDetails(String itemId) {
        // Store the selected item ID
        this.selectedItemId = itemId;
        
        // Run the detail swapper
        this.detailSwapper.run();
    }
    
    // Add a getter for the selected item ID
    public String getSelectedItemId() {
        return this.selectedItemId;
    }
	

	public void refreshProductList() {
    	    getAllData();
    	    
    	    ScrollPane updatedProductBox = createProductBox("all");
    	    
    	    // Update the existing product box in the scene
    	    contentPane.getChildren().set(1, updatedProductBox);
    	}
	
	private void updateProductLabel(String filter, int size) {
	    String title = String.format("Showing %d products from %s", 
	    	size, 
	        filter);
	    
	    productLabel.setText(title);
	}


	private void getAllData() {
    	data = new ManipulateData("MsItem");
    	itemList = data.getData("ItemName");
    	priceList = data.getData("ItemPrice");
    	stockList = data.getData("ItemStock");
    }
    
	private ScrollPane filters(String filterName) {
	    ArrayList<String> filteredNames = new ArrayList<>();
	    ArrayList<String> filteredPrices = new ArrayList<>();
	    
	    
	    for (int i = 0; i < this.itemList.size(); i++) {
	        String name = itemList.get(i);
	        // Improve filtering to be more flexible
	        if (name.toLowerCase().contains(filterName.toLowerCase())) {
	            filteredNames.add(name);
	            filteredPrices.add(priceList.get(i));
	        }
	    }
	    
	    updateProductLabel(filterName, filteredNames.size());
	    
	    // Update the product box with filtered results
	    return createProductBox(filterName);
	}
	
	private void refreshProductBox() {

    	int col = 1;
    	int row = 2;
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            // Create a new product box with filtered results
            ScrollPane filteredProductBox = filters(searchText);

            // Replace the existing product box in the GridPane
            contentPane.getChildren().removeIf( node -> GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row);
            GridPane.setColumnIndex(filteredProductBox, col);
            GridPane.setRowIndex(filteredProductBox, row);
            contentPane.getChildren().add(filteredProductBox);
        } else {
            // If search is empty, show all products
            ScrollPane allProductBox = createProductBox("all");
            
            // Replace the existing product box in the GridPane
            contentPane.getChildren().remove(allProductBox);
            GridPane.setColumnIndex(allProductBox, col);
            GridPane.setRowIndex(allProductBox, row);
            contentPane.getChildren().add(allProductBox);
        }
	}

    private HBox createNavBar() {
        
        // Left Section - Search Bar
        searchField.setPromptText("Search Items in GoGoQuery Store");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #282a36; -fx-text-fill: white; -fx-prompt-text-fill: #aaa; -fx-background-radius: 15;");
        
        searchButton.setStyle("-fx-background-color: #b19cd9; -fx-text-fill: white; -fx-font-weight: bold; ");
       
    	System.out.println(searchField.getText());
    	
        searchBox.setSpacing(10);

        // Right Section - Welcome and Log Out
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        welcomeLabel.setTextFill(Color.ORANGE);

        myCart.setStyle("-fx-background-color: transparent; "
        				+ "-fx-text-fill: white; "
        				+ "-fx-font-weight: bold;");
        logOutButton.setStyle("-fx-background-color: #ff4444; "
        					+ "-fx-text-fill: white; "
        					+ "-fx-font-weight: bold;");

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

    private VBox createFilterBox() {
    	VBox filterBox = new VBox(10);
        filterBox.setPadding(new Insets(10));
        filterBox.setAlignment(Pos.TOP_LEFT);

        Label filterLabel = new Label("Filter");
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        filterLabel.setTextFill(Color.LIGHTGRAY);

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(data.getData("ItemCategory"));
        categoryComboBox.setPromptText("Select a category");

        Button applyFilterButton = new Button("Apply");
        applyFilterButton.setOnAction(
        		// TODO actually change the filter for this thing
        		
        		evt -> this.createFilteredProductBox(categoryComboBox.getValue())
        		);

        filterBox.getChildren().addAll(filterLabel, categoryComboBox, applyFilterButton);
        return filterBox;
    }
    
    private ScrollPane createFilteredProductBox(String category) {
        ScrollPane root = new ScrollPane();
        VBox productList = new VBox(10);
        productList.setPadding(new Insets(10));
        productList.setStyle("-fx-background-color: #3e3e4e; "
                            + "-fx-border-color: #555555;");
        
        ArrayList<String> categoryData = data.getData("ItemCategory");
        
        for (int i = 0; i < this.itemList.size(); i++) {
            if (categoryData.get(i).equalsIgnoreCase(category)) {
                HBox itemBox = createProductItem(i);
                productList.getChildren().add(itemBox);
            }
        }
        
        updateProductLabel(category, this.itemList.size());
        root.setContent(productList);
        return root;
    }
    
    private HBox createProductItem(int idx) {
    	HBox itemBox = new HBox(10);
        itemBox.setPadding(new Insets(10));
        itemBox.setStyle("-fx-background-color: #2e2e3e; -fx-border-color: #555555; -fx-border-radius: 5px;");

        ImageView itemImage = new ImageView(); // Placeholder for product image
        itemImage.setFitWidth(50);
        itemImage.setFitHeight(50);
        itemImage.setStyle("-fx-background-color: gray;");

        VBox itemDetails = new VBox(5);
        Hyperlink itemName = new Hyperlink(this.itemList.get(idx));
        itemName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        itemName.setTextFill(Color.WHITE);
        
        // this is important for showing productDetails.
        String itemId = data.getData("ItemID").get(idx);

        Label itemPrice = new Label(this.priceList.get(idx));
        itemPrice.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        itemPrice.setTextFill(Color.GOLD);

        Label productStock = new Label(this.stockList.get(idx));
        productStock.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        productStock.setTextFill(Color.RED);
        
        // this is where to use ItemID above, sending the data to main and then to productDetails
        // Add click event to switch to product details
        itemName.setOnAction(evt -> showProductDetails(itemId));


        itemDetails.getChildren().addAll(itemName, itemPrice, productStock);
        itemBox.getChildren().addAll(itemImage, itemDetails);
        
        return itemBox;
    }
    
    private ScrollPane createProductBox(String filter) {
    	// Right Side Product List
    	// product box initialize
    	 // Product listing
    	ScrollPane root = new ScrollPane();
        VBox productList = new VBox(10);
        productList.setPadding(new Insets(10));
        productList.setStyle("-fx-background-color: #3e3e4e; "
        					+ "-fx-border-color: #555555;");
        
        this.updateProductLabel(filter, this.itemList.size());
        // Filter logic
        for (int i = 0; i < this.itemList.size(); i++) {
            // Apply filter conditions
            if (filter.equals("all") || 
                this.itemList.get(i).toLowerCase().contains(filter.toLowerCase())) {
                
                // Create and add product item
                HBox itemBox = createProductItem(i);
                productList.getChildren().add(itemBox);
            }
        }
            
        root.setContent(productList);
        return root;
    }
    
    public void set() {
    	filterLabel.setStyle(" -fx-text-fill: GRAY; -fx-font-weight: bold;");
    	productLabel.setStyle(" -fx-text-fill: GRAY; -fx-font-weight: bold;");
    	
		categoryField.setGraphicTextGap(0);
		categoryField.getItems().addAll(new MenuItem("Add Item"), 
										new MenuItem("Manage Queue"),
										new MenuItem("Log Out"));
		
	
	};
	
	public void eventHandler() {
		 searchButton.setOnAction(
				evt -> refreshProductBox()
	    );

	    myCart.setOnAction(evt -> this.cartSwapper.run());
	    this.logOutButton.setOnAction(evt -> this.logoutSwapper.run());
	}
    

	@Override
	public Region build() {
		getAllData();
		set();
    	
		// navigation, filter box and product box 
        HBox navBar = createNavBar();
        VBox filterBox =  createFilterBox();
        ScrollPane productBox = createProductBox("all");
        
        // combine filter and product boc
        contentPane.addRow(0, welcomeLabel);
        contentPane.addRow(1, filterLabel, productLabel);
        contentPane.addRow(2, filterBox, productBox);
        contentPane.setPadding(new Insets(20));
        contentPane.setHgap(20); 
        contentPane.setVgap(20); 
        
        eventHandler();
        VBox layout = new VBox(navBar, contentPane);
        layout.setStyle("-fx-background-color: #282a36;");
        
        return layout;
	}

}
