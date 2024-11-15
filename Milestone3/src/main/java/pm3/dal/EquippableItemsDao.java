package pm3.dal;

import pm3.model.EquippableItems;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquippableItemsDao {
    protected ConnectionManager connectionManager;
    private static EquippableItemsDao instance = null;

    protected EquippableItemsDao() {
        connectionManager = new ConnectionManager();
    }

    public static EquippableItemsDao getInstance() {
        if (instance == null) {
            instance = new EquippableItemsDao();
        }
        return instance;
    }
    
    public EquippableItems create(EquippableItems equippableItem) throws SQLException {
        String insertItemsQuery = "INSERT INTO Items(itemName, maxStackSize, marketAllowed, vendorPrice) " +
            "VALUES(?,?,?,?);";
        String insertEquippableQuery = "INSERT INTO EquippableItems(itemID, itemLevel, slotID, requiredLevel) " +
            "VALUES(?,?,?,?);";
        
        Connection connection = null;
        PreparedStatement insertItems = null;
        PreparedStatement insertEquippable = null;
        ResultSet resultKey = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);

            // Create Items record first
            insertItems = connection.prepareStatement(insertItemsQuery, Statement.RETURN_GENERATED_KEYS);
            insertItems.setString(1, equippableItem.getItemName());
            insertItems.setInt(2, equippableItem.getMaxStackSize());
            insertItems.setBoolean(3, equippableItem.isMarketAllowed());
            insertItems.setInt(4, equippableItem.getVendorPrice());
            insertItems.executeUpdate();

            // Get the auto-generated itemID
            resultKey = insertItems.getGeneratedKeys();
            int itemId;
            if (resultKey.next()) {
                itemId = resultKey.getInt(1);
            } else {
                throw new SQLException("Unable to retrieve auto-generated key.");
            }

            // Create EquippableItems record
            insertEquippable = connection.prepareStatement(insertEquippableQuery);
            insertEquippable.setInt(1, itemId);
            insertEquippable.setInt(2, equippableItem.getItemLevel());
            insertEquippable.setInt(3, equippableItem.getSlotID());
            insertEquippable.setInt(4, equippableItem.getRequiredLevel());
            insertEquippable.executeUpdate();

            connection.commit();
            
            // Set the generated ID in the returned object
            equippableItem = new EquippableItems(itemId, equippableItem.getItemName(), 
                equippableItem.getMaxStackSize(), equippableItem.isMarketAllowed(),
                equippableItem.getVendorPrice(), equippableItem.getItemLevel(),
                equippableItem.getSlotID(), equippableItem.getRequiredLevel());
            
            return equippableItem;
            
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw ex;
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
            if (resultKey != null) {
                resultKey.close();
            }
            if (insertItems != null) {
                insertItems.close();
            }
            if (insertEquippable != null) {
                insertEquippable.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
//    public EquippableItems create(EquippableItems equippableItem) throws SQLException {
//        String insertEquippableItem = "INSERT INTO EquippableItems(ItemID, ItemLevel, SlotID, RequiredLevel) VALUES(?,?,?,?);";
//        Connection connection = null;
//        PreparedStatement insertStmt = null;
//
//        try {
//            connection = connectionManager.getConnection();
//            insertStmt = connection.prepareStatement(insertEquippableItem);
//            insertStmt.setInt(1, equippableItem.getItemID());
//            insertStmt.setInt(2, equippableItem.getItemLevel());
//            insertStmt.setInt(3, equippableItem.getSlotID());
//            insertStmt.setInt(4, equippableItem.getRequiredLevel());
//            insertStmt.executeUpdate();
//            
//            // Since we're using an existing ItemID, just return the same object
//            return new EquippableItems(
//                equippableItem.getItemID(),
//                equippableItem.getItemName(),
//                equippableItem.getMaxStackSize(),
//                equippableItem.isMarketAllowed(),
//                equippableItem.getVendorPrice(),
//                equippableItem.getItemLevel(),
//                equippableItem.getSlotID(),
//                equippableItem.getRequiredLevel()
//            );
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//            if (connection != null) {
//                connection.close();
//            }
//            if (insertStmt != null) {
//                insertStmt.close();
//            }
//        }
//    }


    // Read by ItemID
    public EquippableItems getById(int itemID) throws SQLException {
        String selectEquippableItem = "SELECT * FROM EquippableItems INNER JOIN Items ON EquippableItems.ItemID = Items.ItemID " +
                                      "WHERE EquippableItems.ItemID = ?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;

        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectEquippableItem);
            selectStmt.setInt(1, itemID);
            results = selectStmt.executeQuery();

            if (results.next()) {
                return new EquippableItems(
                    results.getInt("ItemID"),
                    results.getString("ItemName"),
                    results.getInt("MaxStackSize"),
                    results.getBoolean("MarketAllowed"),
                    results.getInt("VendorPrice"),
                    results.getInt("ItemLevel"),
                    results.getInt("SlotID"),
                    results.getInt("RequiredLevel")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (selectStmt != null) selectStmt.close();
            if (results != null) results.close();
        }
        return null;
    }

    // Get all EquippableItems
    public List<EquippableItems> getAll() throws SQLException {
        List<EquippableItems> equippableItemsList = new ArrayList<>();
        String selectAllEquippableItems = "SELECT * FROM EquippableItems INNER JOIN Items ON EquippableItems.ItemID = Items.ItemID;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;

        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectAllEquippableItems);
            results = selectStmt.executeQuery();

            while (results.next()) {
                EquippableItems item = new EquippableItems(
                    results.getInt("ItemID"),
                    results.getString("ItemName"),
                    results.getInt("MaxStackSize"),
                    results.getBoolean("MarketAllowed"),
                    results.getInt("VendorPrice"),
                    results.getInt("ItemLevel"),
                    results.getInt("SlotID"),
                    results.getInt("RequiredLevel")
                );
                equippableItemsList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (selectStmt != null) selectStmt.close();
            if (results != null) results.close();
        }
        return equippableItemsList;
    }

 // Delete
    public void delete(EquippableItems item) throws SQLException {
        String deleteEquippableItem = "DELETE FROM EquippableItems WHERE ItemID = ?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;

        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteEquippableItem);
            deleteStmt.setInt(1, item.getItemID());
            deleteStmt.executeUpdate();

            // Delete the base item from Items table
            ItemsDao.getInstance().delete(item.getItemID());
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (deleteStmt != null) deleteStmt.close();
        }
    }
}
