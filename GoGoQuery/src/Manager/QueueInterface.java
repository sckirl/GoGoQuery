package Manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Database.Connect;
import Database.ManipulateData;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

public class QueueInterface extends ManagerNavBar implements Builder<Region> {
    private Connect connect;
    
    public QueueInterface(Runnable addSwapper, 
			Runnable manageSwapper, 
			Runnable loginSwapper) {
    	
		super(addSwapper, manageSwapper, loginSwapper);
        this.connect = Connect.getInstance();

	}

    public ObservableList<String[]> populateTable() {
        List<String[]> transactions = new ArrayList<>();
        
        try {
            // Modified SQL query to join transactionheader with msuser to get email
            String query = "SELECT " +
                "th.TransactionID, " +
                "th.UserID, " +
                "mu.UserEmail, " +
                "th.DateCreated, " +
                "(SELECT SUM(mi.ItemPrice * td.Quantity) FROM transactiondetail td " +
                " JOIN msitem mi ON td.ItemID = mi.ItemID " +
                " WHERE td.TransactionID = th.TransactionID) AS Total, " +
                "th.Status " +
                "FROM transactionheader th " +
                "JOIN msuser mu ON th.UserID = mu.UserID";
            
            ResultSet rs = connect.execQuery(query);
            
            while (rs.next()) {
                String[] transaction = new String[6];
                transaction[0] = rs.getString("TransactionID");
                transaction[1] = rs.getString("UserID");
                transaction[2] = rs.getString("UserEmail");
                transaction[3] = rs.getString("DateCreated");
                transaction[4] = rs.getString("Total");
                transaction[5] = rs.getString("Status");
                
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Error handling
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to retrieve transaction data");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        
        return FXCollections.observableArrayList(transactions);
    }

    

    private void processSelectedTransaction(TableView<String[]> table) {
        String[] selectedTransaction = table.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            String transactionId = selectedTransaction[0];
            // Implement your transaction processing logic here
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sending Package");
            alert.setHeaderText("Sending Package");
            alert.setContentText("Sending Package ID: " + transactionId);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Transaction Selected");
            alert.setContentText("Please select a transaction to process.");
            alert.showAndWait();
        }
    }

    private TableColumn<String[], String> createTableColumn(String columnName, int index) {
        TableColumn<String[], String> column = new TableColumn<>(columnName);
        column.setCellValueFactory(cellData -> {
            // Safely get the value, returning empty string if index is out of bounds
            String value = index < cellData.getValue().length ? cellData.getValue()[index] : "";
            return new SimpleStringProperty(value);
        });
        column.setPrefWidth(100);
        return column;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Region build() {
        // Root layout
        BorderPane layout = new BorderPane();
        BorderPane navBar = this.createNavBar();
        layout.setStyle("-fx-background-color: #e0e0e0;");

        // Title
        Label title = new Label("Transaction Queue Manager");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10;");
        StackPane titlePane = new StackPane(title);
        titlePane.setStyle("-fx-background-color: #2d8bc0; -fx-padding: 10;");
        layout.setTop(titlePane);

        // TableView
        TableView<String[]> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);

        // Create columns
        TableColumn<String[], String> colTransactionID = createTableColumn("Transaction ID", 0);
        TableColumn<String[], String> colUserID = createTableColumn("Customer ID", 1);
        TableColumn<String[], String> colUserEmail = createTableColumn("Customer Email", 2);
        TableColumn<String[], String> colDate = createTableColumn("Date", 3);
        TableColumn<String[], String> colTotal = createTableColumn("Total", 4);
        TableColumn<String[], String> colStatus = createTableColumn("Status", 5);

        // Add columns to table
        table.getColumns().addAll(
            colTransactionID, colUserID, colUserEmail, 
            colDate, colTotal, colStatus
        );

        // Populate table with data
        ObservableList<String[]> data = populateTable();
        table.setItems(data);

        layout.setTop(navBar);
        layout.setCenter(table);

        // Bottom Button
        Button sendButton = new Button("Send Package");
        sendButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white; -fx-font-size: 16px;");
        sendButton.setOnAction(e -> processSelectedTransaction(table));
        
        StackPane buttonPane = new StackPane(sendButton);
        buttonPane.setStyle("-fx-padding: 10;");
        layout.setBottom(buttonPane);

        return layout;
    }

	@Override
	protected Region createContent() {
		// TODO Auto-generated method stub
		return null;
	}
}