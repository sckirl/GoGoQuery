module GoGoQuery {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires java.sql;
	
	opens UserInterface to javafx.graphics, javafx.fxml;
	exports UserInterface;
	exports Shopper;
	exports Manager;
	exports Database;
}