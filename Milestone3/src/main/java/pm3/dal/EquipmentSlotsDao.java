package pm3.dal;

import pm3.model.EquipmentSlots;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EquipmentSlotsDao {
    protected ConnectionManager connectionManager;

    private static EquipmentSlotsDao instance = null;

    protected EquipmentSlotsDao() {
        connectionManager = new ConnectionManager();
    }

    public static EquipmentSlotsDao getInstance() {
        if (instance == null) {
            instance = new EquipmentSlotsDao();
        }
        return instance;
    }

    // Create a new equipment slot
    public EquipmentSlots create(EquipmentSlots equipmentSlot) throws SQLException {
        String insertEquipmentSlot = "INSERT INTO EquipmentSlots (slotName) VALUES (?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        ResultSet resultKey = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertEquipmentSlot, Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, equipmentSlot.getSlotName());
            insertStmt.executeUpdate();

            resultKey = insertStmt.getGeneratedKeys();
            int slotID = -1;
            if (resultKey.next()) {
                slotID = resultKey.getInt(1);
                equipmentSlot.setSlotID(slotID); // Set the generated slotID to the model
            } else {
                throw new SQLException("Unable to retrieve auto-generated key.");
            }
            return equipmentSlot;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resultKey != null) resultKey.close();
            if (insertStmt != null) insertStmt.close();
            if (connection != null) connection.close();
        }
    }

    // Retrieve an EquipmentSlot by slotID
    public EquipmentSlots getSlotById(int slotID) throws SQLException {
        String selectSlot = "SELECT slotID, slotName FROM EquipmentSlots WHERE slotID = ?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectSlot);
            selectStmt.setInt(1, slotID);
            results = selectStmt.executeQuery();

            if (results.next()) {
                String slotName = results.getString("slotName");
                return new EquipmentSlots(slotID, slotName);
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

    // Delete an EquipmentSlot by slotID
    public EquipmentSlots delete(EquipmentSlots equipmentSlot) throws SQLException {
        String deleteSlot = "DELETE FROM EquipmentSlots WHERE slotID = ?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteSlot);
            deleteStmt.setInt(1, equipmentSlot.getSlotID());
            deleteStmt.executeUpdate();

            // Return null after successful deletion
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

