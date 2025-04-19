package UserInterface;

import Manager.AddItemInterface;
import Manager.ManagerHome;
import Manager.QueueInterface;
import Shopper.CartInterface;
import Shopper.ProductDetailsInterface;
import Shopper.ShopperHomeInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class Main extends Application {
	
	// set ABSOLUTE / FINAL values in the application
	
	final int WIDTH = 1080;
	final int HEIGHT = 720;
	final Scene SCENE = new Scene(new VBox(), 
								this.WIDTH, 
								this.HEIGHT);
	
	// make every single Region/scene there are in this whole app
 	private Region registration;
	private Region login;
	private Region role;
	
	// windows in Shopper
	ShopperHomeInterface shopperHomeInterface;
	private Region shopperHome;
	private Region productDetail;
	private Region cart;
	
	
	// windows in Manager
	ManagerHome managerHomeInterface;
	private Region managerHome;
	private Region queue;
	private Region addItem;
    
    @Override
    public void start(Stage primaryStage) {
    	// Set the scene as final, it will never be changed. 
    	// but everything inside of the scene will be changed
    	// this is changing the root of the scene without treating the scene like a component, 
    	// rather a immovable javafx object
    	// this is way better in implementation because its 
    	// only swapping layout component, without making changes in
    	// crucial javafx parts; making everything generic. HUGE Credit to:
    	// https://www.pragmaticcoding.ca/javafx/swap-scenes#swapping-out-a-layout-component
        
    	// All windows with possible branches of other links (2 or more buttons/links)
    	// Load everything in the starting interface.
    	
 
    	// Starting Interfaces
    	login = new loginInterface(
    	        () -> SCENE.setRoot(registration),  // Register swapper
    	        () -> SCENE.setRoot(shopperHome),    // Login to role swapper
    	        () -> SCENE.setRoot(managerHome)
    		).build();
    	
        registration = new registrationInterface(
        		() -> SCENE.setRoot(role), // Roleswapper 
        		() -> SCENE.setRoot(login), // loginswapper
        		() -> SCENE.setRoot(role)
        		).build();
        
        role = new roleInterface(
                () -> SCENE.setRoot(shopperHome),   // Shopper swapper
                () -> SCENE.setRoot(managerHome)    // Manager swapper
            ).build();
        
        // Shopper Interfaces
        cart = new CartInterface(
        		() -> SCENE.setRoot(shopperHome)).build(); // Home Swapper
        
         shopperHomeInterface = new ShopperHomeInterface(
                () -> {
                    // Create ProductDetailsInterface ONLY BY using the 
                	// selected item ID from ShopperHomeInterface
                    productDetail = new ProductDetailsInterface(
                        () -> SCENE.setRoot(shopperHome), 
                        shopperHomeInterface.getSelectedItemId()
                    ).build();
                    SCENE.setRoot(productDetail);
                },
                
                () -> SCENE.setRoot(cart), 	// Cart swapper
                () -> SCENE.setRoot(login), // go back to login from button press
                () -> SCENE.setRoot(shopperHome) // home, when clicking logo
                
            );
        
        shopperHome = shopperHomeInterface.build();

        // Manager Interfaces
        managerHomeInterface = new ManagerHome(
        		() -> SCENE.setRoot(addItem), 
        		() -> SCENE.setRoot(queue),
        		() -> SCENE.setRoot(login));
        
        managerHome = managerHomeInterface.build();//
        
        queue = new QueueInterface(
        		() -> SCENE.setRoot(addItem), 
        		() -> SCENE.setRoot(queue),
        		() -> SCENE.setRoot(login)).build();// TODO MAKE THIS LESS SLOW LMAO
        											// THIS  ACTUALLY TAKES FOREVER
        
        addItem = new AddItemInterface(
        		() -> SCENE.setRoot(addItem), 
        		() -> SCENE.setRoot(queue),
        		() -> SCENE.setRoot(login)).build();
        
        // start from login -> registration -> role
        
        SCENE.setRoot(login);
        
        primaryStage.setTitle("Go Go Query");
        primaryStage.setScene(SCENE);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
