package pm3.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pm3.model.CharacterInfo;
import pm3.model.Players;


public class CharacterInfoDao {
	protected ConnectionManager connectionManager;
	private static CharacterInfoDao instance = null;
	protected CharacterInfoDao() {
		connectionManager = new ConnectionManager();
	}
	public static CharacterInfoDao getInstance() {
		if(instance == null) {
			instance = new CharacterInfoDao();
		}
		return instance;
	}
	
	//create a new character
	public CharacterInfo create(CharacterInfo character) throws SQLException{
		String insertCharacter = "INSERT INTO CHARACTERINFO(playerID, firstName, lastName, maxHP) VALUES(?, ?, ?, ?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertCharacter,
					Statement.RETURN_GENERATED_KEYS);
			insertStmt.setInt(1, character.getPlayer().getPlayerID());
			insertStmt.setString(2, character.getFirstName());
			insertStmt.setString(3, character.getLastName());
			insertStmt.setInt(4, character.getMaxHP());
			insertStmt.executeUpdate();
			
			resultKey = insertStmt.getGeneratedKeys();
			int characterID = -1;
			if(resultKey.next()) {
				characterID = resultKey.getInt(1);
			}else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			character.setCharacterID(characterID);
			return character;
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
	
	
	//update the firstName of the Character instance
	public CharacterInfo updateCharacterFirstName(CharacterInfo character, String newFirstName) throws SQLException {
		String updateCharacter = "UPDATE CharacterInfo SET firstName=? WHERE firstName=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateCharacter);
			updateStmt.setString(1, newFirstName);
			updateStmt.setString(2, character.getFirstName());
			updateStmt.executeUpdate();
			
			character.setFirstName(newFirstName);
			return character;
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
	
	//retrieve the character based on the primary key: characterID
	public CharacterInfo getCharactersByCharacterID(int characterID) throws SQLException{
		String selectCharacter =
				"SELECT characterID, playerID, firstName, lastName, maxHP FROM CharacterInfo WHERE characterID=?;";
			Connection connection = null;
			PreparedStatement selectStmt = null;
			ResultSet results = null;
			try {
				connection = connectionManager.getConnection();
				selectStmt = connection.prepareStatement(selectCharacter);
				selectStmt.setInt(1, characterID);
				results = selectStmt.executeQuery();
				PlayersDao playersDao = PlayersDao.getInstance();
				if(results.next()) {
					int resultCharacterID = results.getInt("characterID");
					int playerID = results.getInt("playerID");
					String firstName = results.getString("firstName");
					String lastName = results.getString("lastName");
					int maxHP = results.getInt("maxHP");
					
					Players player = playersDao.getPlayerByID(playerID);
					CharacterInfo character = new CharacterInfo(resultCharacterID, firstName, lastName, maxHP, player);
					return character;
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
	
	//retrieve all characters for a specific playerID
	public List<CharacterInfo> getCharactersByPlayerID(int playerID) throws SQLException{
		List<CharacterInfo> characters = new ArrayList<>();
		String selectCharacters =
				"SELECT characterID, playerID, firstName, lastName, maxHP FROM CharacterInfo WHERE playerID=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectCharacters);
			selectStmt.setInt(1, playerID);
			results = selectStmt.executeQuery();
			PlayersDao playersDao = PlayersDao.getInstance();
			while(results.next()) {
				int characterID = results.getInt("characterID");
				String firstName = results.getString("firstName");
				String lastName = results.getString("lastName");
				int maxHP = results.getInt("maxHP");
				
				Players player = playersDao.getPlayerByID(playerID);
				CharacterInfo character = new CharacterInfo(characterID, firstName, lastName, maxHP, player);
				characters.add(character);
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
		return characters;
	}
	
	//delete a character by characterID
	public CharacterInfo delete(CharacterInfo character) throws SQLException{
		String deleteCharacter = "DELETE FROM CharacterInfo WHERE characterID=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteCharacter);
			deleteStmt.setInt(1, character.getCharacterID());
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
}
