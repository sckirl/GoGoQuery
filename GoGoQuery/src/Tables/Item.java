package Tables;

import java.util.HashMap;

import Database.ManipulateData;

public class Item extends ManipulateData{
	int itemID;
	String itemName;
	Double itemPrice;
	String itemDesc;
	int itemStock;
	String itemCategory;
	
	public Item(int itemID, String itemName, Double itemPrice, String itemDesc, int itemStock, String itemCategory) {
		super("msitem");
		this.itemID = itemID;
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.itemDesc = itemDesc;
		this.itemStock = itemStock;
		this.itemCategory = itemCategory;
	}

	@Override
	public void sendData() {
		HashMap<String, String> res = new HashMap<>();
		res.put("ItemID", Integer.toString(this.itemID));
		res.put("ItemName", this.itemName);
		res.put("ItemPrice", Double.toString(this.itemPrice));
		res.put("ItemDesc", itemDesc);
		res.put("ItemStock", Integer.toString(this.itemStock));
		res.put("ItemCategory", this.itemCategory);
		
		System.out.println(res);
		this.insertData(res);
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public int getItemStock() {
		return itemStock;
	}

	public void setItemStock(int itemStock) {
		this.itemStock = itemStock;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	
}
