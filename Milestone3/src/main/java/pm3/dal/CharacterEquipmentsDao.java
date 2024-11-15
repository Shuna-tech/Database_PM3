package pm3.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import pm3.model.CharacterEquipments;
import pm3.model.CharacterInfo;
import pm3.model.EquippableItems;

public class CharacterEquipmentsDao {
    protected ConnectionManager connectionManager;

    private static CharacterEquipmentsDao instance = null;

    protected CharacterEquipmentsDao() {
        connectionManager = new ConnectionManager();
    }

    public static CharacterEquipmentsDao getInstance() {
        if (instance == null) {
            instance = new CharacterEquipmentsDao();
        }
        return instance;
    }

    // Create a new character equipment
    public CharacterEquipments create(CharacterEquipments characterEquipment) throws SQLException {
        String insertCharacterEquipment =
                "INSERT INTO CharacterEquipments (characterID, slotID, itemID) VALUES (?, ?, ?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertCharacterEquipment);
            insertStmt.setInt(1, characterEquipment.getCharacter().getCharacterID());
            insertStmt.setInt(2, characterEquipment.getSlotID());
            if (characterEquipment.getItem() != null) {
                insertStmt.setInt(3, characterEquipment.getItem().getItemID());
            } else {
                insertStmt.setNull(3, Types.INTEGER);
            }

            insertStmt.executeUpdate();
            return characterEquipment;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (insertStmt != null) {
                insertStmt.close();
            }
        }
    }

    // Update the item in a character's equipment
    public CharacterEquipments updateItem(CharacterEquipments characterEquipment, EquippableItems newItem) throws SQLException {
        String updateCharacterEquipment =
                "UPDATE CharacterEquipments SET itemID = ? WHERE characterID = ? AND slotID = ?;";
        Connection connection = null;
        PreparedStatement updateStmt = null;
        try {
            connection = connectionManager.getConnection();
            updateStmt = connection.prepareStatement(updateCharacterEquipment);

            if (newItem != null) {
                updateStmt.setInt(1, newItem.getItemID());
            } else {
                updateStmt.setNull(1, Types.INTEGER);
            }

            updateStmt.setInt(2, characterEquipment.getCharacter().getCharacterID());
            updateStmt.setInt(3, characterEquipment.getSlotID());

            updateStmt.executeUpdate();

            // Update the item in the object before returning
            characterEquipment.setItem(newItem);
            return characterEquipment;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (updateStmt != null) updateStmt.close();
        }
    }

    // Get character's equipment by characterID and slotID
    public CharacterEquipments getCharacterEquipmentByCharacterAndSlot(CharacterInfo character, int slotID) throws SQLException {
        String selectCharacterEquipment =
                "SELECT characterID, slotID, itemID FROM CharacterEquipments WHERE characterID = ? AND slotID = ?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectCharacterEquipment);
            selectStmt.setInt(1, character.getCharacterID());
            selectStmt.setInt(2, slotID);

            results = selectStmt.executeQuery();

            if (results.next()) {
                int itemID = results.getInt("itemID");
                EquippableItems item = null;
                if (!results.wasNull()) {
                    EquippableItemsDao itemsDao = EquippableItemsDao.getInstance();
                    item = itemsDao.getById(itemID);
                }
                return new CharacterEquipments(character, slotID, item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (results != null) results.close();
            if (selectStmt != null) selectStmt.close();
            if (connection != null) connection.close();
        }
        return null;
    }

    // Delete the specified character's equipment
    public CharacterEquipments delete(CharacterEquipments characterEquipment) throws SQLException {
        String deleteCharacterEquipment =
                "DELETE FROM CharacterEquipments WHERE characterID = ? AND slotID = ?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteCharacterEquipment);

            deleteStmt.setInt(1, characterEquipment.getCharacter().getCharacterID());
            deleteStmt.setInt(2, characterEquipment.getSlotID());

            deleteStmt.executeUpdate();

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (deleteStmt != null) deleteStmt.close();
            if (connection != null) connection.close();
        }
    }
}
