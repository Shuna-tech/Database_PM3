package pm3.model;

public class EquippableItems extends Items {
    protected int itemLevel;
    protected int slotID;
    protected int requiredLevel;
    
    public EquippableItems(String itemName, int maxStackSize, boolean marketAllowed, int vendorPrice,
                         int itemLevel, int slotID, int requiredLevel) {
        super(itemName, maxStackSize, marketAllowed, vendorPrice);
        this.itemLevel = itemLevel;
        this.slotID = slotID;
        this.requiredLevel = requiredLevel;
    }
    
    public EquippableItems(int itemID, String itemName, int maxStackSize, boolean marketAllowed, int vendorPrice,
                         int itemLevel, int slotID, int requiredLevel) {
        super(itemID, itemName, maxStackSize, marketAllowed, vendorPrice);
        this.itemLevel = itemLevel;
        this.slotID = slotID;
        this.requiredLevel = requiredLevel;
    }
    public int getItemLevel() { return itemLevel; }
    public void setItemLevel(int itemLevel) { this.itemLevel = itemLevel; }
    
    public int getSlotID() { return slotID; }
 
    
    public int getRequiredLevel() { return requiredLevel; }
    public void setRequiredLevel(int requiredLevel) { this.requiredLevel = requiredLevel; }

}
