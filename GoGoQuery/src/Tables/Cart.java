package Tables;

import java.util.HashMap;

import Database.ManipulateData;

public class Cart extends ManipulateData{
	private int UserID, ItemID, Quantity;

	public Cart(int userID, int itemID, int quantity) {
		super("mscart");
		UserID = userID;
		ItemID = itemID;
		Quantity = quantity;
	}
	
	@Override
	public void sendData() {
		HashMap<String, String> res = new HashMap<>();
		res.put("UserID", Integer.toString(UserID));
		res.put("ItemID", Integer.toString(ItemID));
		res.put("Quantity", Integer.toString(Quantity));
		
		this.insertData(res);
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	public int getQuantity() {
		return Quantity;
	}

	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	
	
}
