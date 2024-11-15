package pm3.model;

public class Items {
	protected int itemID;
	protected String itemName;
	protected int maxStackSize;
	protected boolean marketAllowed;
	protected int vendorPrice;
	
	public Items(String itemName, int maxStackSize, boolean marketAllowed, int vendorPrice) {
        this.itemName = itemName;
        this.maxStackSize = maxStackSize;
        this.marketAllowed = marketAllowed;
        this.vendorPrice = vendorPrice;
    }
    
    public Items(int itemID, String itemName, int maxStackSize, boolean marketAllowed, int vendorPrice) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.maxStackSize = maxStackSize;
        this.marketAllowed = marketAllowed;
        this.vendorPrice = vendorPrice;
    }
    
    public int getItemID() {return itemID;}
	
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public int getMaxStackSize() { return maxStackSize; }
    public void setMaxStackSize(int maxStackSize) { this.maxStackSize = maxStackSize; }
    
    public boolean isMarketAllowed() { return marketAllowed; }
    public void setMarketAllowed(boolean marketAllowed) { this.marketAllowed = marketAllowed; }
    
    public int getVendorPrice() { return vendorPrice; }
    public void setVendorPrice(int vendorPrice) { this.vendorPrice = vendorPrice; }
}