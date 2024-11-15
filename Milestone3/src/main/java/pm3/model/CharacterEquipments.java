package pm3.model;

public class CharacterEquipments {
    protected CharacterInfo character;
    protected int slotID; // Changed from EquipmentSlots enum to int to align with database definition
    protected EquippableItems item;

    // Constructor with item
    public CharacterEquipments(CharacterInfo character, int slotID, EquippableItems item) {
        this.character = character;
        this.slotID = slotID;
        this.item = item;
    }

    // Constructor without item (useful when a slot is empty)
    public CharacterEquipments(CharacterInfo character, int slotID) {
        this.character = character;
        this.slotID = slotID;
        this.item = null;
    }

    // Getters and Setters
    public CharacterInfo getCharacter() {
        return character;
    }

    public void setCharacter(CharacterInfo character) {
        this.character = character;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public EquippableItems getItem() {
        return item;
    }

    public void setItem(EquippableItems item) {
        this.item = item;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "CharacterEquipments{" +
                "characterID=" + character.getCharacterID() +
                ", slotID=" + slotID +
                ", itemID=" + (item != null ? item.getItemID() : "null") +
                '}';
    }
}
