package Tables;

import java.sql.Date;
import java.util.HashMap;

import Database.ManipulateData;

public class TransactionHeader extends ManipulateData{
	int transactionID;
	int userID;
	String dateCreated;
	String status;
	
	
	public TransactionHeader(int transactionID, 
			int userID, 
			String dateCreated, 
			String status) {
		super("transactionheader");
		this.transactionID = transactionID;
		this.userID = userID;
		this.dateCreated = dateCreated;
		this.status = status;
	}
	
	@Override
	public void sendData() {
		HashMap<String, String> res = new HashMap<>();
		res.put("TransactionID", Integer.toString(transactionID));
		res.put("UserID", Integer.toString(userID));
		res.put("DateCreated", dateCreated);
		res.put("Status", status);

		this.insertData(res);
	}

	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
