package pm3.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pm3.model.*;

public class PlayersDao {
	protected ConnectionManager connectionManager;
	
	private static PlayersDao instance = null;
	protected PlayersDao() {
		connectionManager = new ConnectionManager();
	}
	public static PlayersDao getInstance() {
		if(instance == null) {
			instance = new PlayersDao();
		}
		return instance;
	}
	
	//create the player
	public Players create(Players player) throws SQLException{
		String insertPlayer = "INSERT INTO PLAYERS(userName, email) VALUES(?, ?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertPlayer,
					Statement.RETURN_GENERATED_KEYS);
			insertStmt.setString(1, player.getUserName());
			insertStmt.setString(2, player.getEmail());
			insertStmt.executeUpdate();
			
			resultKey = insertStmt.getGeneratedKeys();
			int playerID = -1;
			if(resultKey.next()) {
				playerID = resultKey.getInt(1);
			}else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			player.setPlayerID(playerID);
			return player;
		}catch(SQLException e) {
			e.printStackTrace();
			throw e;
		}finally {
			if(connection != null) {
				connection.close();
			}
			if(insertStmt != null) {
				insertStmt.close();
			}
			if(resultKey != null) {
				resultKey.close();
			}
		}
	}
	
	//update the player's user name
	public Players updatePlayerUserName(Players player, String newUserName) throws SQLException {
		String updatePlayer = "UPDATE Players SET userName=? WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updatePlayer);
			updateStmt.setString(1, newUserName);
			updateStmt.setString(2, player.getUserName());
			updateStmt.executeUpdate();
			
			player.setUserName(newUserName);
			return player;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(updateStmt != null) {
				updateStmt.close();
			}
		}
	}
	
	//delete the player
	public Players delete(Players player) throws SQLException{
		String deletePlayer = "DELETE FROM Players WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deletePlayer);
			deleteStmt.setString(1, player.getUserName());
			deleteStmt.executeUpdate();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(deleteStmt != null) {
				deleteStmt.close();
			}
		}
	}
	
	//retrieve the player by user name
	public Players getPlayerFromUserName(String userName) throws SQLException {
		String selectPlayer = "SELECT UserName, Email FROM Players WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectPlayer);
			selectStmt.setString(1, userName);
			results = selectStmt.executeQuery();
			if(results.next()) {
				String resultUserName = results.getString("userName");
				String email = results.getString("email");
				Players player = new Players(resultUserName,email);
				return player;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(selectStmt != null) {
				selectStmt.close();
			}
			if(results != null) {
				results.close();
			}
		}
		return null;
	}
	
	//retrieve the player based on the primary key: playerID
	public Players getPlayerByID(int playerID) throws SQLException{
		String selectPlayer =
				"SELECT playerID, userName, email FROM Players WHERE playerID=?;";
			Connection connection = null;
			PreparedStatement selectStmt = null;
			ResultSet results = null;
			try {
				connection = connectionManager.getConnection();
				selectStmt = connection.prepareStatement(selectPlayer);
				selectStmt.setInt(1, playerID);
				results = selectStmt.executeQuery();
				if(results.next()) {
					int resultPlayerId = results.getInt("playerID");
					String userName = results.getString("userName");
					String email = results.getString("email");
		
					Players player = new Players(resultPlayerId, userName, email);
					return player;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				if(connection != null) {
					connection.close();
				}
				if(selectStmt != null) {
					selectStmt.close();
				}
				if(results != null) {
					results.close();
				}
			}
			return null;
	}
	
}
