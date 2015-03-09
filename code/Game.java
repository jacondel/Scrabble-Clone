package code;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import code.interfaces.IGame;
/**
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class Game implements IGame {

	private int _currentTurn;
	private Player _currentPlayer;
	private List<Player> _gamePlayers;
	private boolean _gameStarted;
	private LinkedList<SimpleImmutableEntry<String, Integer>> _lastTurnScores;
	private int _currentIndex = 0;
	
	/**The constructor initializes the instance variables.
	 * _gameStarted is true if the game has been started and false if not.
	 * _gamePlayers an arraylist of all the players playing the game
	 * _currentTurn the index of the _gamePlayers list referring to the current player.
	 * */
	public Game(){
		set_currentTurn(0);
		set_gamePlayers(new ArrayList<Player>());
		_gameStarted=false;
//		set_currentPlayer(null);
		_lastTurnScores=new LinkedList<SimpleImmutableEntry<String, Integer>>();
		
	}
	/**This method takes the list of players shuffles the order of the indexes and returns an arrayList of players
	 * in random order.  After calling this at the start of the game the instance variable _gamePlayers
	 * is the order of play 
	 * @return A list of the games players in random order for their turns to be taken.
	 * */
	@Override
	public List<Player> orderOfPlay() {
		Collections.shuffle(get_gamePlayers());
		set_currentPlayer(get_gamePlayers().get(0));
		return get_gamePlayers();
	}

	/**This method gets the score of the desired player.
	 * @param player The player who's score is required.
	 * @return the score of the player passed into the argument.
	 * */
	@Override
	public int score(Player player) {
		return player.get_playerScore();// accessor?
	}

	/**This method adds a player to the list of players playing the game.  
	 * @Return true if successful, false otherwise.
	 * */
	@Override
	public boolean register(Player player) {
		if(!_gameStarted){
		return get_gamePlayers().add(player);
		}
		else{return false;}
	}

	
	/**This method attempts to begin the game if there are at least 2 players and the game has not already 
	 * been started.
	 * @return true if game has been started and order of play established in _gamePlayers, false otherwise.
	 * */
	@Override
	public boolean start() {
		if(!_gameStarted &&  get_gamePlayers().size()>1){
			_gameStarted=true;
			orderOfPlay();
			return true;
		}
		else{
			return false;
			}
	}
	
	/**This method cycles to the next player in the order of play.
	 * @return true if turn was ended.*/
	@Override
	public boolean endTurn() {
		set_currentTurn(get_currentTurn() + 1);
		set_currentPlayer(get_gamePlayers().get(get_currentTurn()%(get_gamePlayers().size())));
		return true;
	}
	/**This method accesses the word and score of the last previous turn.
	 * @return the words and scores of the last turn.*/
	@Override
	public List<SimpleImmutableEntry<String, Integer>> lastTurnWordScores() {
		return _lastTurnScores;
		//_lastTurnScores.put(word, value)
//		ArrayList<TreeMap> temp =new ArrayList<TreeMap>().add(_lastTurnScores);
//		return temp;
	}
	/**Helper method sets up last turn word scores
	 * @param str; A string representation of the last word played
	 * @param value; An int representing the score value of the last word played.*/
	public void setLastTurnWordScores(String str, int value){
		_lastTurnScores.add(new SimpleImmutableEntry<String, Integer>(str, value));
	}
	/**
	  * @param _gamePlayers the _gamePlayers to set
	 */
	public void set_gamePlayers(List<Player> _gamePlayers) {
		this._gamePlayers = _gamePlayers;
	}
	/**
	 * @return the _gamePlayers
	 */
	public List<Player> get_gamePlayers() {
		return _gamePlayers;
	}
	/**
	 * @param _currentTurn the _currentTurn to set
	 */
	public void set_currentTurn(int _currentTurn) {
		this._currentTurn = _currentTurn;
	}
	/**
	 * @return the _currentTurn
	 */
	public int get_currentTurn() {
		return _currentTurn;
	}
	/**
	 * @param _currentPlayer the _currentPlayer to set
	 */
	public void set_currentPlayer(Player _currentPlayer) {
		this._currentPlayer = _currentPlayer;
	}
	/**
	 * @return the _currentPlayer
	 */
	public Player get_currentPlayer() {
		return _currentPlayer;
	}
	public void restorePlayer(String player, int score, String rack, DataModel dm){
		Tile[] restoreRack = new Tile[12];
		char[] newRack = rack.toCharArray();
		Player person = new Player(player);
		dm.setRestore(person);
		person.set_playerScore(score);
		for (int j=0; j<12; j++){
			restoreRack[j] = new Tile(newRack[j]);
		}
		person.set_playerRack(restoreRack);
		this.set_currentPlayer(person);
		_currentIndex++;
	}
	public Player restoreOrder(){
		return _gamePlayers.get(0);
	}
}
