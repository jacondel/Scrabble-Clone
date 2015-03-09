package code;

/**
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class Player{
	//each player must have a name, score, and tile rack.
	private String _playerName;
	private int _playerScore;
	private Tile[] _playerRack;

	/**The constructor initializes all instance variables. Giving the player a name rack and score.
	 * @param A string representation of the players name.
	 */
	public Player(String n){
		set_playerName(n);
		set_playerScore(0);
		set_playerRack(new Tile[12]);
	}


	/**When called on a player this method generates 12 tiles and assigns them to the players tilerack 
	 * array.
	 * @param tp refers to the games generated TilePool.
	 * @return The randomly generated tilerack drawn from the tilepool.
	 */
	public Tile[] generateRack(TilePool tp){
		for(int i=0; i<12;i++){
			get_playerRack()[i] = new Tile(tp.getRandomLetterFromPool());
		}

		return get_playerRack();

	}

	/**This method can be used after every turn to re-populate players tile racks
	 * @param tp the games tilepool
	 * @return the randomly generated tile.*/
	public Tile replaceUsedTile(TilePool tp){
		Tile t = new Tile(tp.getRandomLetterFromPool());
		return t;
	}


	/**This method checks a rack to find a specific tile
	 * @param t the tile being searched for
	 * @return true if the rack contains the tile, false otherwise.*/
	public boolean rackContainsTile(Tile t){
		for(int i=0; i<get_playerRack().length;i++){
			if(get_playerRack()[i].equals(t)){
				return true;
			}
		}
		return false;


	}


	/**
	 * @param _playerScore the _playerScore to set
	 */
	public void set_playerScore(int _playerScore) {
		this._playerScore = _playerScore;
	}


	/**
	 * @return the _playerScore
	 */
	public int get_playerScore() {
		return _playerScore;
	}


	/**
	 * @param _playerName the _playerName to set
	 */
	public void set_playerName(String _playerName) {
		this._playerName = _playerName;
	}


	/**
	 * @return the _playerName
	 */
	public String get_playerName() {
		return _playerName;
	}


	/**
	 * @param _playerRack the _playerRack to set
	 */
	public void set_playerRack(Tile[] _playerRack) {
		this._playerRack = _playerRack;
	}


	/**
	 * @return the _playerRack
	 */
	public Tile[] get_playerRack() {
		return _playerRack;
	}
}
