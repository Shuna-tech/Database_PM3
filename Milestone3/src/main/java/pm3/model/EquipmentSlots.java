package pm3.model;

public class EquipmentSlots {
    private int slotID;       // Primary key
    private String slotName;   // Name of the equipment slot (e.g., "Headgear", "Main Hand")

    // Constructor for creating a new slot with an auto-generated ID
    public EquipmentSlots(String slotName) {
        this.slotName = slotName;
    }

    // Constructor for retrieving an existing slot by slotID
    public EquipmentSlots(int slotID, String slotName) {
        this.slotID = slotID;
        this.slotName = slotName;
    }

    // Getters and Setters
    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }
}
