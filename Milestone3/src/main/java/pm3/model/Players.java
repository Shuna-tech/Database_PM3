package pm3.model;

public class Players {
	protected int playerID;
	protected String userName;
	protected String email;
	
	 // Constructors
    public Players(int playerID, String userName, String email) {
        this.playerID = playerID;
        this.userName = userName;
        this.email = email;
    }

    public Players(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    // Getters and setters
    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
	
}
