package pm3.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pm3.model.Items;

public class ItemsDao {
    protected ConnectionManager connectionManager;
    private static ItemsDao instance = null;
    
    protected ItemsDao() {
        connectionManager = new ConnectionManager();
    }
    
    public static ItemsDao getInstance() {
        if (instance == null) {
            instance = new ItemsDao();
        }
        return instance;
    }
    
    public Items create(Items item) throws SQLException {
        String insertItem = "INSERT INTO Items(ItemName, MaxStackSize, MarketAllowed, VendorPrice) VALUES(?,?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        ResultSet resultKey = null;
        
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertItem, PreparedStatement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, item.getItemName());
            insertStmt.setInt(2, item.getMaxStackSize());
            insertStmt.setBoolean(3, item.isMarketAllowed());
            insertStmt.setInt(4, item.getVendorPrice());
            insertStmt.executeUpdate();
            
            resultKey = insertStmt.getGeneratedKeys();
            if (resultKey.next()) {
                int generatedItemId = resultKey.getInt(1);
                return new Items(generatedItemId, item.getItemName(), item.getMaxStackSize(), 
                    item.isMarketAllowed(), item.getVendorPrice());
            } else {
                throw new SQLException("Unable to retrieve auto-generated key.");
            }
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

    public Items getItemById(int itemId) throws SQLException {
        String selectItem = "SELECT ItemID, ItemName, MaxStackSize, MarketAllowed, VendorPrice FROM Items WHERE ItemID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectItem);
            selectStmt.setInt(1, itemId);
            results = selectStmt.executeQuery();
            
            if (results.next()) {
                return new Items(
                    results.getInt("ItemID"),
                    results.getString("ItemName"),
                    results.getInt("MaxStackSize"),
                    results.getBoolean("MarketAllowed"),
                    results.getInt("VendorPrice")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
        return null;
    }

    public boolean delete(int itemID) throws SQLException {
        String deleteItem = "DELETE FROM Items WHERE ItemID=?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteItem);
            deleteStmt.setInt(1, itemID);
            int affectedRows = deleteStmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (deleteStmt != null) {
                deleteStmt.close();
            }
        }
    }

}
