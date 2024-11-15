package pm3.model;


public class CharacterInfo {
    protected int characterID;                 
    protected String firstName;      
    protected String lastName;
    protected int maxHP;
    protected Players player;
    
    public CharacterInfo(int characterID, String firstName, String lastName, int maxHP, Players player) {
    	this.characterID = characterID;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.maxHP = maxHP;
    	this.player = player;
    }
    
	public CharacterInfo(int characterID) {
		this.characterID = characterID;
	}
    
    public CharacterInfo(String firstName, String lastName, int maxHP, Players player) {
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.maxHP = maxHP;
    	this.player = player;
    }

	public int getCharacterID() {
		return characterID;
	}

	public void setCharacterID(int characterID) {
		this.characterID = characterID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public Players getPlayer() {
		return player;
	}

	public void setPlayer(Players player) {
		this.player = player;
	}
    
}
