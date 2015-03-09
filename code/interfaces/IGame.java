package code.interfaces;

import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

import code.Player;

/**
 * An interface for the main game class of the stage 1 version of the game.
 * 
 * Your team must not modify this interface, and must write suitable tests
 * for each of the methods specified.
 * 
 * Your team must implement this interface, defining methods which provide
 * the indicated functionality.  Your implementing class may define methods
 * in addition to the ones given here.
 * 
 * @author Carl Alphonce <alphonce@cse.buffalo.edu>
 *
 */
public interface IGame {
	
	/**
	 * A method which returns a java.util.List object containing Player objects
	 * in the order of their turns.
	 * @return a list of players
	 */
	public List<Player> orderOfPlay();
	
	/**
	 * A method which returns the current score of a given player.
	 * The behavior of this method if Player is not valid is undefined.
	 * @param player - the Player whose score we desire
	 * @return the Player's score
	 */
	public int score(Player player);
	
	/**
	 * Register a Player with the game.
	 * @param player - the Player we wish to register
	 * @return true if the registration succeeded, false otherwise
	 */
	public boolean register(Player player);
	
	/**
	 * Attempt to start the game.  A game can be started only if it
	 * has not already been started, and if two or more Players have
	 * been registered.
	 * @return true if starting the game was successful, false otherwise
	 */
	public boolean start();
	
	/**
	 * Attempt to the end the current Player's turn.  A Player's turn can be
	 * ended only if either no word was placed, or the word placed is valid
	 * according to the rules of the game.
	 * 
	 * For more details on word placement validity, see
	 * http://www.cse.buffalo.edu/faculty/alphonce/Courses/Spring2008/cse116/Project/Stage1/
	 * 
	 * @return true if the current Player's turn was ended, false otherwise
	 */
	public boolean endTurn();

	/**
	 * This method returns the words and corresponding scores which were
	 * placed on the board on the last turn.  The words are represented as Strings,
	 * the scores as Integers.  The words and scores are paired in a SimpleImmutableEntry,
	 * a class whose instances hold two values (a "key" (the word) and a corresponding
	 * "value" (the score)).
	 * @return a list of word-score pairs.
	 */
	public List<SimpleImmutableEntry<String,Integer>> lastTurnWordScores();
}
