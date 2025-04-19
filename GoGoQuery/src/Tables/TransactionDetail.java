package Tables;

import java.util.HashMap;

import Database.ManipulateData;

public class TransactionDetail extends ManipulateData{
	private int transactionID;
	private int itemID;
	private int quantity;
	
	
	public TransactionDetail(int transactionID, int itemID, int quantity) {
		super("transactiondetail");
		this.transactionID = transactionID;
		this.itemID = itemID;
		this.quantity = quantity;
	}
	
	@Override
	public void sendData() {
		HashMap<String, String> res = new HashMap<>();
		res.put("TransactionID", Integer.toString(transactionID));
		res.put("ItemID", Integer.toString(itemID));
		res.put("Quantity", Integer.toString(quantity));

		this.insertData(res);
	}

	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
