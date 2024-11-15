package pm3.tools;

import pm3.dal.CharacterEquipmentsDao;
import pm3.dal.CharacterInfoDao;
import pm3.dal.EquipmentSlotsDao;
import pm3.dal.EquippableItemsDao;
import pm3.dal.PlayersDao;
import pm3.model.CharacterEquipments;
import pm3.model.CharacterInfo;
import pm3.model.EquippableItems;
import pm3.model.EquipmentSlots;
import pm3.model.Players;

import java.sql.SQLException;
import java.util.List;

public class Inserter {

    public static void main(String[] args) throws SQLException {
    	testPlayersDao();
    }
    
    private static void testPlayersDao() throws SQLException {
    	// DAO instances
        PlayersDao playersDao = PlayersDao.getInstance();
        CharacterInfoDao characterInfoDao = CharacterInfoDao.getInstance();
        CharacterEquipmentsDao characterEquipmentsDao = CharacterEquipmentsDao.getInstance();
        EquipmentSlotsDao equipmentSlotsDao = EquipmentSlotsDao.getInstance();
        EquippableItemsDao equippableItemsDao = EquippableItemsDao.getInstance();

        // TEST CASES - Creating unique records based on existing data

        // 1. CREATE - Insert a new player with a unique username and email
        Players newPlayer = new Players("uniquePlayerTest2", "uniquePlayerTest2@example.com");
        newPlayer = playersDao.create(newPlayer);
        System.out.println("Inserted Player: " + newPlayer);

        // 2. CREATE - Insert a new character for the unique player
        CharacterInfo newCharacter = new CharacterInfo("TestFirstName2", "TestLastName2", 250, newPlayer);
        newCharacter = characterInfoDao.create(newCharacter);
        System.out.println("Inserted Character: " + newCharacter);

        // 3. Use an existing slot in EquipmentSlots (e.g., "Main Hand" with slotID 5)
        EquipmentSlots mainHandSlot = equipmentSlotsDao.getSlotById(5); // Assuming slotID 5 exists for "Main Hand"
        
        if (mainHandSlot == null) {
            System.out.println("Main Hand slot not found in EquipmentSlots.");
            return;
        }

        // 4. Use an existing item in EquippableItems (e.g., itemID 1)
        EquippableItems existingItem = equippableItemsDao.getById(1); // Assuming itemID 1 exists in EquippableItems
        
        if (existingItem == null) {
            System.out.println("Equippable item with itemID 1 not found.");
            return;
        }

        // 5. CREATE - Insert equipment for the character using the existing slotID and itemID
        CharacterEquipments newEquipment = new CharacterEquipments(newCharacter, mainHandSlot.getSlotID(), existingItem);
        newEquipment = characterEquipmentsDao.create(newEquipment);
        System.out.println("Inserted Equipment: " + newEquipment);

        // 6. READ - Retrieve player by username
        Players retrievedPlayer = playersDao.getPlayerFromUserName("uniquePlayerTest2");
        System.out.println("Retrieved Player: " + retrievedPlayer);

        // 7. READ - Retrieve character by characterID
        CharacterInfo retrievedCharacter = characterInfoDao.getCharactersByCharacterID(newCharacter.getCharacterID());
        System.out.println("Retrieved Character: " + retrievedCharacter);

        // 8. READ - Retrieve equipment by character and slotID
        CharacterEquipments retrievedEquipment = characterEquipmentsDao.getCharacterEquipmentByCharacterAndSlot(
            newCharacter, mainHandSlot.getSlotID()
        );
        System.out.println("Retrieved Equipment: " + retrievedEquipment);

        // 9. READ - Retrieve all characters for the player by playerID
        List<CharacterInfo> characterList = characterInfoDao.getCharactersByPlayerID(newPlayer.getPlayerID());
        System.out.println("Retrieved Characters for Player ID " + newPlayer.getPlayerID() + ":");
        for (CharacterInfo character : characterList) {
            System.out.println(character);
        }

        // 10. UPDATE - Update player's username to a new unique value
        playersDao.updatePlayerUserName(newPlayer, "updatedUniquePlayerTest2");
        System.out.println("Updated Player Username: " + newPlayer);

        // 11. UPDATE - Update character's first name to a new unique value
        characterInfoDao.updateCharacterFirstName(newCharacter, "UpdatedTestFirstName2");
        System.out.println("Updated Character First Name: " + newCharacter);

        // 12. Use another existing item in EquippableItems (e.g., itemID 2) for updating equipment
        EquippableItems updatedItem = equippableItemsDao.getById(2); // Assuming itemID 2 exists in EquippableItems

        if (updatedItem != null) {
            characterEquipmentsDao.updateItem(newEquipment, updatedItem);
            System.out.println("Updated Equipment Item: " + newEquipment);
        } else {
            System.out.println("Equippable item with itemID 2 not found for updating.");
        }

        // 13. DELETE - Delete equipment
        characterEquipmentsDao.delete(newEquipment);
        System.out.println("Deleted Equipment: " + newEquipment);

        // 14. DELETE - Delete character
        characterInfoDao.delete(newCharacter);
        System.out.println("Deleted Character: " + newCharacter);

        // 15. DELETE - Delete player
        playersDao.delete(newPlayer);
        System.out.println("Deleted Player: " + newPlayer);
    
    }
}
