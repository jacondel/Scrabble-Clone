package code;

import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * This class holds the underlying data for the game.
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class DataModel {

	private Board _b;
	private Game _g;
	private TilePool _tp;
	private int _passCount;
	private boolean _firstPlayerFirstTurn;
	private Player _winner;
	private Board _tempBoard;
	private Tile[] _tempRack;
	private ArrayList<Tile> _tilesFromMovePass;
	private boolean _newTurn;
	private String _lastWord;
	private Integer _lastScore;
	private int[][] _wordMultipliers;
	private int[][] _letterMultipliers;
	private boolean _ch;
	private boolean _cv;
	private List<Player> _restoreList;
	private boolean _restore;
	private boolean _ranOutOfTiles;
	private String _dictionaryPath;
	private int _playerNum;

	/**
	 * The constructor assigns initial values to instance variables.This class will be used to hold the underlying data of the game.
	 **/
	public DataModel() {
		_dictionaryPath = System.getProperty("user.dir") + "/words";
		_playerNum = 0;
		_restore = false;
		_restoreList = new ArrayList<Player>();
		_firstPlayerFirstTurn = true;
		_b = new Board();
		set_g(new Game());
		set_tp(new TilePool());
		_tempBoard = new Board();
		_tempRack = new Tile[12];
		set_newTurn(true);
		set_tilesFromMovePass(new ArrayList<Tile>());
		set_wordMultipliers(new int[20][20]);
		set_letterMultipliers(new int[20][20]);
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				get_letterMultipliers()[i][j] = lmAssign();
				get_wordMultipliers()[i][j] = wmAssign();
			}
		}
		_ch = false;
		_cv = false;
		set_ranOutOfTiles(false);
	}

	/**
	 * returns an integer based on the probability of letter multipliers in the game
	 * @return int representing multiplier
	 */
	public int lmAssign() {
		double num = Math.random() * 100;
		if (num < 10) {
			return 2;
		}
		if (num < 90) {
			return 1;
		}
		if (num < 95) {
			return 0;
		}
		return -1;
	}

	/**
	 * returns an integer based on the probability of word multipliers in the game 
	 * @return int representing multiplier
	 */
	public int wmAssign() {
		double num = Math.random() * 100;
		if (num < 5) {
			return 3;
		}
		if (num < 20) {
			return 2;
		}
		if (num < 95) {
			return 1;
		}
		return 0;
	}

	/**
	 * This method creates a player; names, generates a rack for them, and sets
	 * initial score of 0.
	 * 
	 * @param s A string representation of the players name.
	 * @return the created Player
	 * */
	public Player createPlayer(String s) {

		Player p = new Player(s);
		p.generateRack(get_tp());
		get_g().register(p);

		return p;

	}

	/**
	 * This method accesses a players rack of tiles.
	 * 
	 * @param p A player in the game
	 * @return a String representation of players current tile rack
	 * */
	public String showRack(Player p) {
		String rack = "";
		for (int i = 0; i < p.get_playerRack().length; i++) {
			rack = rack + p.get_playerRack()[i].get_character();
		}
		return rack;
	}

	/**
	 * This method checks to see if it is a player's turn.
	 * 
	 * @param p The Player to check against the current player.
	 * @return true if it is the players turn, false if not
	 * */
	public boolean isTurn(Player p) {
		if (get_g().get_gamePlayers().indexOf(p) == get_g().get_currentTurn()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method checks to see if a move is made on an empty board space and not on an empty tile and is touching another tile and is a continuous stream of character
	 * starts or continues a sequence. If it is the first move of a turn, then checking to make sure the space is empty and the tile would not be stranded is sufficient.
	 * 
	 * @param tile A tile that has been played
	 * @param row An int representing the row
	 * @param col An int representing the column
	 * @return true if the move in question does not conflict with the rules , false if it does 
	 * */
	public boolean isValidMove(Tile tile, int row, int col) {
		if (!_tempBoard.isEmpty(row, col) || tile.get_character() == '/') {
			return false;
		}
		if (stranded(row, col)) {
			return false;
		}
		if (get_tilesFromMovePass().size() == 0) {
			return true;
		}
		if (!allOccupiedBetween(row, col)) {
			return false;
		}

		return true;
	}

	/**
	 * This method checks to see if all of the spaces between the first played
	 * tile and the current tile attempting to be played are occupied.
	 * 
	 * @param rowToCheck  row of the tile to be checked
	 * @param colToCheck column of the tile to be checked
	 * @return true if all the spaces between two tiles are occupied by other tiles, false otherwise
	 * */
	public boolean allOccupiedBetween(int rowToCheck, int colToCheck) {
		int row1 = _tempBoard.rowOf(get_tilesFromMovePass().get(0));
		int col1 = _tempBoard.colOf(get_tilesFromMovePass().get(0));
		if (row1 != rowToCheck && col1 != colToCheck) {// neither row nor column
			// in common
			return false;
		}
		boolean consistentHorizontally = true;
		boolean consistentVertically = true;

		for (int i = 0; i < get_tilesFromMovePass().size(); i++) {
			if (_tempBoard.rowOf(get_tilesFromMovePass().get(i)) != _tempBoard
					.rowOf(get_tilesFromMovePass().get(0))) {
				consistentHorizontally = false;
			}
		}

		for (int i = 0; i < get_tilesFromMovePass().size(); i++) {
			if (_tempBoard.colOf(get_tilesFromMovePass().get(i)) != _tempBoard
					.colOf(get_tilesFromMovePass().get(0))) {
				consistentVertically = false;
			}
		}
		if ((consistentVertically || consistentHorizontally)
				&& get_tilesFromMovePass().size() == 1) {
			return true;
		}

		if (consistentHorizontally) {// sameRow
			boolean first = false;
			boolean second = false;
			for (int i = col1; i < colToCheck; i++) {
				if (i == col1) {
					first = true;
				}
				if (_tempBoard.isEmpty(row1, i)) {
					first = false;
				}

			}// if first is true, then it is valid
			for (int i = col1; i > colToCheck; i--) {
				if (i == col1) {
					second = true;
				}
				if (_tempBoard.isEmpty(row1, i)) {
					second = false;
				}

			}// if second is true, then it is valid
			return first || second;
		}
		if (consistentVertically) {// sameCol
			boolean first = false;
			boolean second = false;
			for (int i = row1; i < rowToCheck; i++) {
				if (i == row1) {
					first = true;
				}
				if (_tempBoard.isEmpty(i, col1)) {
					first = false;
				}

			}// if first is true, then it is valid
			for (int i = row1; i > rowToCheck; i--) {
				if (i == row1) {
					second = true;
				}
				if (_tempBoard.isEmpty(i, col1)) {
					second = false;
				}

			}// if second is true, then it is valid
			return first || second;

		}
		return false;
	}

	/**
	 * This Method is responsible for actually removing a tile from the players
	 * rack and placing it on the board. This method also forces the user to use
	 * only the tiles located in his/her rack. This method can also remove tiles from
	 * the board according to index of the current players moves and can make a
	 * deep copy of the board and rack if necessary
	 * 
	 * @param an int array whose first three indices represent the index of the tile, row at which to be placed, and column at which to be placed respectively.
	 * */
	public void movePass(int[] moveInput) {
		_firstPlayerFirstTurn = true;
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				if (!_tempBoard.isEmpty(i, j)
						&& _tempBoard.get(i, j).get_character() != ' ') {
					_firstPlayerFirstTurn = false;
				}
			}
		}// if board is completely empty then _firstPlayerFirstTurn is true

		if (is_newTurn()) { // then make deep copy, this can be done by calling this method for the first time of a turn with argument {-1,-1}
			set_newTurn(false);
			set_tilesFromMovePass(new ArrayList<Tile>());
			_tempBoard = new Board();
			_tempRack = new Tile[12];
			Tile[][] tempArray = new Tile[20][20];
			for (int i = 0; i < 20; i++) {
				for (int j = 0; j < 20; j++) {
					tempArray[i][j] = new Tile(
							_b.getBoard()[i][j].getCharacter());
				}
			}
			_tempBoard.setBoard(tempArray);
			_tempBoard.set_hsCol(_b.homeSquareCol());
			_tempBoard.set_hsRow(_b.homeSquareRow());
			for (int i = 0; i < 12; i++) {
				_tempRack[i] = new Tile(
						get_g().get_currentPlayer().get_playerRack()[i]
								.getCharacter());
			}// deep copy
		}// done deep copy

		if (moveInput[0] == -1) {// remove section
			if (moveInput[1] > -1 && moveInput[1] < 12) {
				Tile temp = get_tilesFromMovePass().get(moveInput[1]);
				if (!wouldCreateGap(temp)) {
					if (_tempBoard.rowOf(temp) == _tempBoard.get_hsRow()
							&& _tempBoard.colOf(temp) == _tempBoard.get_hsCol()
							&& !(get_tilesFromMovePass().size() == 1)) {
						// do nothing because you can't remove the homesquare
						// unless it is the only tile remaining
					} else {
						get_tilesFromMovePass().remove(moveInput[1]);
						_tempBoard.retract(temp);
						for (int i = 0; i < _tempRack.length; i++) {
							if (_tempRack[i].get_character() == '/') {
								_tempRack[i] = temp; // replace exactly one tile in the rack
								break;
							}

						}
					}
				}
				return;
			}
		} else { //attempting to place a tile
			Tile t = _tempRack[moveInput[0]];
			int row = moveInput[1];
			int column = moveInput[2];

			if (_firstPlayerFirstTurn) {// if first move of the game, place tile on homesquare
				_tempBoard.place(t, _tempBoard.get_hsRow(),
						_tempBoard.get_hsCol());
				get_tilesFromMovePass().add(t);
				_tempRack[moveInput[0]] = new Tile('/');
				_firstPlayerFirstTurn = false;
				return;
			}

			if (isValidMove(t, row, column)) {// if its is valid, place the tile
				_tempBoard.place(t, row, column);
				get_tilesFromMovePass().add(t);
				_tempRack[moveInput[0]] = new Tile('/');

			} else {

			}
		}

		// done

	}

	/**
	 * This method tests a row and column to see if their is currently a tile
	 * occupying an adjacent space
	 * 
	 * @param row
	 * @param col
	 * @return true if the row and column in question has an adjacent space
	 *         occupied, false if not
	 */
	public boolean stranded(int row, int col) {
		boolean b1 = _tempBoard.isEmpty(row + 1, col);
		boolean b2 = _tempBoard.isEmpty(row - 1, col);
		boolean b3 = _tempBoard.isEmpty(row, col + 1);
		boolean b4 = _tempBoard.isEmpty(row, col - 1);
		return b1 && b2 && b3 && b4;
	}

	private boolean wouldCreateGap(Tile tile) {
		int row = _tempBoard.rowOf(tile);
		int col = _tempBoard.colOf(tile);
		int leftMost = col;
		int rightMost = col;
		int upMost = row;
		int downMost = row;
		do {
			leftMost--;
		} while (!_tempBoard.isEmpty(row, leftMost));
		leftMost++;
		// -------------
		do {
			rightMost++;
		} while (!_tempBoard.isEmpty(row, rightMost));
		rightMost--;
		// -------------
		do {
			upMost--;
		} while (!_tempBoard.isEmpty(upMost, col));
		upMost++;
		// -------------
		do {
			downMost++;
		} while (!_tempBoard.isEmpty(downMost, col));
		downMost--;
		// -------------
		// iff only one direction changed, then it would not create a gap
		int count = 0;
		if (row != upMost) {
			count++;
		}
		if (row != downMost) {
			count++;
		}
		if (col != leftMost) {
			count++;
		}
		if (col != rightMost) {
			count++;
		}
		return count > 1;
	}

	/**
	 * This method validates the string created by the characters on the tiles
	 * as a valid word. It also calculates the value of that word and adds it to
	 * the players score. It then replaces used tiles from the players rack from
	 * the tile pool
	 * 
	 * @return true if word is acceptable false if not.
	 */
	public boolean validation() {
		Player p = get_g().get_currentPlayer();
		int wordScore = 0;
		String word = getWord();
		ArrayList<String> collateralWords = new ArrayList<String>();
		collateralWords = otherWordsFormed();
		boolean allOtherWordsAreWords = true;
		for (int i = 0; i < collateralWords.size(); i++) {
			if (!isWord(collateralWords.get(i))) {
				allOtherWordsAreWords = false;
			}
		}
		if (isWord(word) && allOtherWordsAreWords) {
			wordScore = getScore(collateralWords, word);
			p.set_playerScore(p.get_playerScore() + wordScore);
			updateRack();
			_b = _tempBoard;
			get_g().get_currentPlayer().set_playerRack(_tempRack);
			_lastWord = word;
			_lastScore = wordScore;
			get_g().setLastTurnWordScores(_lastWord, _lastScore);
			_passCount = 0;
			return true;
		}
		return false;
	}

	/**
	 * This method finds all words that were formed as a result of a turn not
	 * including the main word
	 * 
	 * @return a collection of words that were formed as a result of a turn not
	 *         including the main word
	 */

	private ArrayList<String> otherWordsFormed() {
		ArrayList<String> answer = new ArrayList<String>();
		// Step 1: find consistency (vertical or horizontal)
		// Step 2: check for tiles against(perpendicular) consistency (you can
		// use _tilesFromMovePass and
		// _tempBoard.rowOf(_tilesFromMovePass[i])+or-1.isEmpty
		// Step 3: follow them to the bottom/top or to the right/left and
		// reconstruct them
		for (int i = 0; i < _tilesFromMovePass.size(); i++) {
			if (_ch) {// word was verified horizontally
				if (!_tempBoard.isEmpty(
						_tempBoard.rowOf(_tilesFromMovePass.get(i)) + 1,
						_tempBoard.colOf(_tilesFromMovePass.get(i)))) {
					String total = "";
					int row1 = _tempBoard.rowOf(_tilesFromMovePass.get(i));
					int col1 = _tempBoard.colOf(_tilesFromMovePass.get(i));
					int topMost = row1;
					int bottomMost = row1;
					do {
						topMost--;
					} while (!_tempBoard.isEmpty(topMost, col1)
							&& _b.getBoard()[topMost][col1].get_character() != ' ');
					topMost++;
					do {
						bottomMost++;
					} while (!_tempBoard.isEmpty(bottomMost, col1)
							&& _b.getBoard()[bottomMost][col1].get_character() != ' ');
					bottomMost--;
					if (topMost != bottomMost) {
						for (int j = topMost; j <= bottomMost; j++) {
							total += _tempBoard.get(j, col1).get_character();
						}
						answer.add(total);
					}

				} else if (!_tempBoard.isEmpty(
						_tempBoard.rowOf(_tilesFromMovePass.get(i)) - 1,
						_tempBoard.colOf(_tilesFromMovePass.get(i)) - 1)) {// or
																			// to
																			// left
					String total = "";
					int row1 = _tempBoard.rowOf(_tilesFromMovePass.get(i));
					int col1 = _tempBoard.colOf(_tilesFromMovePass.get(i));
					int topMost = row1;
					int bottomMost = row1;
					do {
						topMost--;
					} while (!_tempBoard.isEmpty(topMost, col1)
							&& _b.getBoard()[topMost][col1].get_character() != ' ');
					topMost++;
					do {
						bottomMost++;
					} while (!_tempBoard.isEmpty(bottomMost, col1)
							&& _b.getBoard()[bottomMost][col1].get_character() != ' ');
					bottomMost--;
					if (topMost != bottomMost) {
						for (int j = topMost; j <= bottomMost; j++) {
							total += _tempBoard.get(j, col1).get_character();
						}
						answer.add(total);
					}

				}
			}
			if (_cv) {// word was verified vertically
				if (!_tempBoard.isEmpty(
						_tempBoard.rowOf(_tilesFromMovePass.get(i)),
						_tempBoard.colOf(_tilesFromMovePass.get(i)) + 1)) {
					String total = "";
					int row1 = _tempBoard.rowOf(_tilesFromMovePass.get(i));
					int col1 = _tempBoard.colOf(_tilesFromMovePass.get(i));
					int rightMost = col1;
					int leftMost = col1;
					do {
						leftMost--;
					} while (!_tempBoard.isEmpty(row1, leftMost)
							&& _b.getBoard()[row1][leftMost].get_character() != ' ');
					leftMost++;
					do {
						rightMost++;
					} while (!_tempBoard.isEmpty(row1, rightMost)
							&& _b.getBoard()[row1][rightMost].get_character() != ' ');
					rightMost--;
					if (leftMost != rightMost) {
						for (int j = leftMost; j <= rightMost; j++) {
							total += _tempBoard.get(row1, j).get_character();
						}
						answer.add(total);
					}

				} else if (!_tempBoard.isEmpty(
						_tempBoard.rowOf(_tilesFromMovePass.get(i)),
						_tempBoard.colOf(_tilesFromMovePass.get(i)) - 1)) {
					String total = "";
					int row1 = _tempBoard.rowOf(_tilesFromMovePass.get(i));
					int col1 = _tempBoard.colOf(_tilesFromMovePass.get(i));
					int rightMost = col1;
					int leftMost = col1;
					do {
						leftMost--;
					} while (!_tempBoard.isEmpty(row1, leftMost)
							&& _b.getBoard()[row1][leftMost].get_character() != ' ');
					leftMost++;
					do {
						rightMost++;
					} while (!_tempBoard.isEmpty(row1, rightMost)
							&& _b.getBoard()[row1][rightMost].get_character() != ' ');
					rightMost--;
					if (leftMost != rightMost) {
						for (int j = leftMost; j <= rightMost; j++) {
							total += _tempBoard.get(row1, j).get_character();
						}
						answer.add(total);
					}

				}

			}

		}

		return answer;
	}

	/**
	 * This method searches for a file at both the absolute and relative file
	 * paths then traverses the file to see if it contains that word
	 * 
	 * @param word
	 * @return true if the word is in the dictionary file passed in
	 */
	public boolean isWord(String word) {
		word = word.toLowerCase();
		Scanner scan;
		try {
			
			scan = new Scanner(new File(_dictionaryPath)); // looks for
															// dictionary at
															// absolute path
			while (scan.hasNext() == true) {
				String dictionary = scan.nextLine();
				dictionary.toUpperCase();
				if (word.equals(dictionary)) {
					scan.close();
					return true;
				}
			}
			scan.close();
			return false;
		} catch (FileNotFoundException e) {
			try {
				System.out.println(System.getProperty("user.dir")
						+"\\" +_dictionaryPath);
				scan = new Scanner(new File(System.getProperty("user.dir")
						+"\\"+ _dictionaryPath)); // looks for dictionary at relative
												// path
				while (scan.hasNext() == true) {
					String dictionary = scan.nextLine();
					dictionary.toUpperCase();
					if (word.equals(dictionary)) {
						scan.close();
						return true;
					}
				}
			} catch (FileNotFoundException e1) {
			}
		}
		return false;
	}

	/**
	 * This method uses the tiles played to find the main word that was formed
	 * as a result of a turn
	 * 
	 * @return the main word that was formed as a result of a turn
	 */
	public String getWord() {
		if (get_g().get_currentTurn() == 0 && get_tilesFromMovePass().size() == 1) {
			return "" + get_tilesFromMovePass().get(0).get_character();
		}
		int row1 = _tempBoard.rowOf(get_tilesFromMovePass().get(0));
		int col1 = _tempBoard.colOf(get_tilesFromMovePass().get(0));
		int row2 = 0;
		int col2 = 0;
		String total = "";
		if (get_tilesFromMovePass().size() == 1) {
			// find a value for row2 and col2
			if (!_tempBoard.isEmpty(row1 + 1, col1)) {
				row2 = row1 + 1;
				col2 = col1;
			}
			if (!_tempBoard.isEmpty(row1 - 1, col1)) {
				row2 = row1 - 1;
				col2 = col1;
			}
			if (!_tempBoard.isEmpty(row1, col1 + 1)) {
				row2 = row1;
				col2 = col1 + 1;
			}
			if (!_tempBoard.isEmpty(row1, col1 - 1)) {
				row2 = row1;
				col2 = col1 - 1;
			}
		} else {
			row2 = _tempBoard.rowOf(get_tilesFromMovePass().get(1));
			col2 = _tempBoard.colOf(get_tilesFromMovePass().get(1));
		}
		int topMost = row1;
		int bottomMost = row1;
		int leftMost = col1;
		int rightMost = col1;
		boolean consistentHorizontally = false;
		boolean consistentVertically = false;
		if (row1 == row2) {
			consistentHorizontally = true;
			_ch = true;
		}
		if (col1 == col2) {
			consistentVertically = true;
			_cv = true;
		}
		if (consistentVertically) {// find the row of the top and bottom most
			// tile
			do {
				topMost--;
			} while (!_tempBoard.isEmpty(topMost, col1)
					&& _b.getBoard()[topMost][col1].get_character() != ' ');
			topMost++;
			do {
				bottomMost++;
			} while (!_tempBoard.isEmpty(bottomMost, col1)
					&& _b.getBoard()[bottomMost][col1].get_character() != ' ');
			bottomMost--;

			for (int i = topMost; i <= bottomMost; i++) {
				total += _tempBoard.get(i, col1).get_character();
			}
			return total;

		}
		if (consistentHorizontally) {// find the row of the right and left most
			// tile
			do {
				leftMost--;
			} while (!_tempBoard.isEmpty(row1, leftMost)
					&& _b.getBoard()[row1][leftMost].get_character() != ' ');
			leftMost++;
			do {
				rightMost++;
			} while (!_tempBoard.isEmpty(row1, rightMost)
					&& _b.getBoard()[row1][rightMost].get_character() != ' ');
			rightMost--;
			for (int i = leftMost; i <= rightMost; i++) {
				total += _tempBoard.get(row1, i).get_character();
			}
			return total;
		}

		return "";
	}

	/**
	 * This method multiplies each character in the main word by the character
	 * multipliers at each tile, then multiplies the entire main word by the
	 * word multipliers at each space it occupies. Any other words created as a
	 * result of the turn are scored with no multipliers.
	 * 
	 * @param collateralWords
	 * @param main
	 * @return the points awarded for the entire turn
	 */
	public int getScore(ArrayList<String> collateralWords, String main) {
		// go through multiplied chars then just add all regular chars
		int score = 0;
		int wordMultiplier = 1;
		for (int i = 0; i < get_tilesFromMovePass().size(); i++) {
			// if in tiles from word pass apply multiplier
			char c = get_tilesFromMovePass().get(i).get_character();
			int row = _tempBoard.rowOf(get_tilesFromMovePass().get(i));
			int col = _tempBoard.colOf(get_tilesFromMovePass().get(i));
			main.replace("" + c, "");
			score += Tile.getScore(c) * get_letterMultipliers()[row][col];
			// System.out.println("multiplier for "+c+" is "+
			// get_letterMultipliers()[row][col]);
			main = main.replace("" + c, "");
		}
		// characters left over in main get scored normally
		int length = main.length();
		// System.out.println("length of letters that don't recieve letter multiplier: "+
		// main.length());
		for (int i = 0; i < length; i++) {
			char c = main.charAt(i);
			score += Tile.getScore(c);
		}
		// now for the word multipliers- cycle through all the newly placed
		// tiles and multiply "wordMultiplier" by that.
		for (int i = 0; i < get_tilesFromMovePass().size(); i++) {
			int row = _tempBoard.rowOf(get_tilesFromMovePass().get(i));
			int col = _tempBoard.colOf(get_tilesFromMovePass().get(i));
			wordMultiplier *= get_wordMultipliers()[row][col];
		}
		// System.out.println("total word multiplier is: "+wordMultiplier);
		score *= wordMultiplier;
		// now score all words formed in collateral words
		for (int i = 0; i < collateralWords.size(); i++) {
			for (int j = 0; j < collateralWords.get(i).length(); j++) {
				score += Tile.getScore(collateralWords.get(i).charAt(j));
			}
		}
		return score;
	}

	/**
	 * This helper method replaces tiles in a players rack used during the
	 * course of the game. Tiles are drawn randomly from the tile pool.
	 * */
	public void updateRack() {
		Player p = get_g().get_currentPlayer();
		Tile[] rack = _tempRack;
		int size = rack.length;
		for (int i = 0; i < size; i++) {
			Tile t = rack[i];
			if (t.get_character() == '/') {
				Tile temp = p.replaceUsedTile(get_tp());
				if (temp.get_character() == '~') {
					endGame();
					set_ranOutOfTiles(true);
				}
				rack[i] = temp;
			}
		}
	}

	/**
	 * This method starts the game with a known number of players,
	 * creating them, generating a rack, and registering them.
	 * 
	 * @param int num number of Players
	 */
	public void startUp(int num) {
		for (int i = 1; i <= num; i++) {
			Player temp = new Player("Player " + i);
			temp.generateRack(get_tp());
			get_g().register(temp);

		}
		get_g().start();// this also shuffles players

	}

	/**
	 * This method gives feedback to the user displaying the current board
	 * representation, the current player, and the current player's rack.
	 */
	public void outPut() {
		System.out.println(_b.boardConfiguration());

		System.out.println(get_g().get_currentPlayer().get_playerName() + "'s Turn");
		Tile[] tileTemp = get_g().get_currentPlayer().get_playerRack();
		String stringTemp = "";
		for (int i = 0; i < tileTemp.length; i++) {
			stringTemp += tileTemp[i].get_character();
		}
		System.out.println("Your Tile Rack:  " + stringTemp);
		System.out.println("Your Score:  "
				+ get_g().get_currentPlayer().get_playerScore());
	}

	/**
	 * This method ends the current turn and attempts to cycle to the next
	 * player's turn. If the player ends their turn and a winner is not
	 * established than the turn is cycled to the next players. If a winner is
	 * established, the game ends
	 */
	public void endTurn() {
		everyonePassed();// will exit the game if true;
		if (_tilesFromMovePass.size() == 0) {// pass
			_passCount++;
			get_g().endTurn();
			_ch = false;
			_cv = false;
			_newTurn = true;
		} else if (validation()) {// pass or valid move
			get_g().endTurn();
			_ch = false;
			_cv = false;
			_newTurn = true;
		} else {// not valid word
			_newTurn = true;
		}
	}

	/**
	 * This method determines if the game has ended due to everyone ending their turn without placing a tile. If the game has ended then
	 * the method calls endGame
	 */
	public boolean everyonePassed() {
		if (_passCount == get_g().get_gamePlayers().size()) { // game ends
			endGame();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method compares the final scores establishes and displays the winner
	 * and his/her score, followed by the losers and their respective scores.
	 */
	public void endGame() {
		set_winner(get_g().get_gamePlayers().get(0));
		for (int i = 1; i < get_g().get_gamePlayers().size() - 1; i++) {
			if (get_winner().get_playerScore() < get_g().get_gamePlayers().get(i)
					.get_playerScore()) {
				set_winner(get_g().get_gamePlayers().get(i));
			}
		}

		// printout winner and score.
		System.out.println("You Win! " + get_winner().get_playerName()
				+ " Congratulations!  " + "Your Score: "
				+ get_winner().get_playerScore());

		// printout all losers and scores.
		get_g().get_gamePlayers().remove(get_winner());
		for (int j = 0; j < get_g().get_gamePlayers().size(); j++) {
			System.out.println("You Lose. "
					+ get_g().get_gamePlayers().get(j).get_playerName()
					+ "  Your Score: "
					+ get_g().get_gamePlayers().get(j).get_playerScore());
		}

		// System.exit(0)
	}

	/**
	 * This method gets the current board 2d array
	 * 
	 * @return Board the Board
	 * */
	public Board getBoard() {
		return _b;
	}

	/**
	 * This method gets the temporary board 2d array
	 * 
	 * @return Board the temporary board
	 * */
	public Board getTempBoard() {
		return _tempBoard;
	}

	/**
	 * This method gets the temporary rack array of Tile
	 * 
	 * @return Tile[] the temporary rack
	 * */
	public Tile[] getTempRack() {
		return _tempRack;
	}

	/**
	 * This method gets the current game
	 * 
	 * @return Game The game
	 * */
	public Game getGame() {
		return get_g();
	}

	public String getLastWord() {
		return _lastWord;
	}

	public Integer getLastScore() {
		return _lastScore;
	}
	/**
	 * This method attempts to save the game as a String
	 * @return true if successful, false otherwise
	 */
	public boolean saveGame() {
		Board saveBoard = _b;
		_playerNum = get_g().get_gamePlayers().size();
		int currentPlayer = get_g().get_gamePlayers()
				.indexOf(get_g().get_currentPlayer());
		String saveString = "[";

		try {
			int numberOfPlayers = get_g().get_gamePlayers().size();
			PrintWriter writer = new PrintWriter("save_game.txt");
			writer.print(_dictionaryPath + " ");

			for (int y = currentPlayer; y < numberOfPlayers; y++) {
				String playerName = get_g().get_gamePlayers().get(y)
						.get_playerName();
				int score = get_g().score(get_g().get_gamePlayers().get(y));
				Tile[] rack = get_g().get_gamePlayers().get(y).get_playerRack();
				String playerRack = "";
				for (int q = 0; q < 12; q++) {
					playerRack = playerRack + rack[q].get_character();
				}
				writer.print(playerName + " ");
				writer.print(playerRack + " ");
				writer.print(score + " ");
			}
			for (int y = 0; y < currentPlayer; y++) {
				String playerName = get_g().get_gamePlayers().get(y)
						.get_playerName();
				int score = get_g().score(get_g().get_gamePlayers().get(y));
				Tile[] rack = get_g().get_gamePlayers().get(y).get_playerRack();
				String playerRack = "";
				for (int q = 0; q < 12; q++) {
					playerRack = playerRack + rack[q].get_character();
				}
				writer.print(playerName + " ");
				writer.print(playerRack + " ");
				writer.print(score + " ");
			}
			for (int i = 0; i < 20; i++) {
				for (int j = 0; j < 20; j++) {
					char c = saveBoard.get(i, j).getCharacter();
					if (c == '_') {
						int multiplier = _letterMultipliers[i][j];
						int wordMult = _wordMultipliers[i][j];
						if (multiplier == -1) {
							multiplier = 3;
						}
						saveString = saveString + '_';
						saveString = saveString + multiplier;
						saveString = saveString + wordMult;
					} else if (c == ' ') {
						saveString = saveString + "#";
					} else {
						saveString = saveString + c;
					}
				}
				saveString = saveString + "]";
				writer.print(saveString);
				saveString = "[";
			}
			writer.close();
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("The save file does not exist");
			return false;
		}
	}
	/**
	 * This method attempts to restore a game from a string file created as a result of the saveGame() method
	 * @param path path of save file
	 * @return true if successful, false otherwise
	 */
	public boolean restoreGame(String path) {
		if (path == null) {
			path = "save_game.txt";
		}
		set_g(new Game());
		_b = new Board();
		Board temp = new Board();
		int x = 0;

		File restore = new File(path);
		try {
			Scanner scan = new Scanner(restore);
			String restoreGame = scan.nextLine();
			char[] c = restoreGame.toCharArray();
			char[] tempBoard = new char[c.length];
			String dictionaryPath = "";
			String score = "";
			String name = "";
			String rack = "";
			int state = 0;
			int arrayIndex = 0;
			int characterNumber = c.length;
			for (int n = 0; n < characterNumber; n++) {
				switch (state) {
				case 0:
					if (c[arrayIndex] == 'w') {
						state = 1;
						arrayIndex++;
					} else {
						arrayIndex++;
					}
					break;
				case 1:
					if (c[arrayIndex] == 'o') {
						state = 2;
						arrayIndex++;
					} else {
						state = 0;
						arrayIndex++;
					}
					break;
				case 2:
					if (c[arrayIndex] == 'r') {
						state = 3;
						arrayIndex++;
					} else {
						state = 0;
						arrayIndex++;
					}
					break;
				case 3:
					if (c[arrayIndex] == 'd') {
						state = 4;
						arrayIndex++;
					} else {
						state = 0;
						arrayIndex++;
					}
					break;
				case 4:
					if (c[arrayIndex] == 's') {
						state = 5;
						arrayIndex++;
					} else {
						state = 0;
						arrayIndex++;
					}
					break;
				case 5:
					if (c[arrayIndex] == ' ') {
						state = 6;
						for (int r = 0; r < arrayIndex; r++) {
							dictionaryPath = dictionaryPath + c[r];
						}
						_dictionaryPath = dictionaryPath;
						arrayIndex++;
					} else {
						state = 0;
						arrayIndex++;
					}
					break;
				case 6:
					if (c[arrayIndex] == '[') {
						arrayIndex--;
						state = 10;
					} else if (c[arrayIndex] == ' ') {
						state = 7;
					} else {
						name = name + c[arrayIndex];
					}

					arrayIndex++;
					break;
				case 7:
					if (c[arrayIndex] == '[') {
						arrayIndex++;
						state = 10;
					} else if (c[arrayIndex] == ' ') {
						state = 8;
						arrayIndex++;
					} else {
						rack = rack + c[arrayIndex];
						get_tp().tilePoolRemove(c[arrayIndex]);
						arrayIndex++;
					}
					break;
				case 8:
					if (c[arrayIndex] == '[') {
						arrayIndex++;
						state = 10;
					} else if (c[arrayIndex] == ' ') {
						state = 9;
					} else {
						score = score + c[arrayIndex];
						arrayIndex++;
					}
					break;
				case 9:
					int playerScore = Integer.parseInt(score);
					get_g().restorePlayer(name, playerScore, rack, this);
					x++;
					state = 6;
					arrayIndex++;
					name = "";
					score = "";
					rack = "";
					break;

				case 10:
					int boardSpot = 0;
					int length = arrayIndex;
					for (x = 0; x < c.length - length; x++) {
						if (c[arrayIndex] == '[' || c[arrayIndex] == ']') {
							arrayIndex++;
							if (arrayIndex >= c.length) {
								arrayIndex--;
								state = 11;
							}
						} else if (c[arrayIndex] == '_') {
							tempBoard[boardSpot] = '_';
							arrayIndex++;
							boardSpot++;
							tempBoard[boardSpot] = c[arrayIndex];
							arrayIndex++;
							boardSpot++;
							tempBoard[boardSpot] = c[arrayIndex];
							arrayIndex++;
							boardSpot++;
							if (arrayIndex >= c.length) {
								state = 11;
							}
						} else {
							tempBoard[boardSpot] = c[arrayIndex];
							boardSpot++;
							arrayIndex++;
							if (arrayIndex >= c.length) {
								state = 11;
							}
						}
					}
					state = 11;
					break;

				case 11:
					boardSpot = 0;
					for (int i = 0; i < 20; i++) {
						for (int j = 0; j < 20; j++) {
							char q = tempBoard[boardSpot];
							if (q == '#') {
								temp.place(new Tile(' '), i, j);
								boardSpot++;
							} else if (q == '_') {
								temp.place(new Tile('_'), i, j);
								boardSpot++;
								_letterMultipliers[i][j] = (int) tempBoard[boardSpot];
								boardSpot++;
								_wordMultipliers[i][j] = (int) tempBoard[boardSpot];
								boardSpot++;
								
							} else {
								temp.place(new Tile(q), i, j);
								get_tp().tilePoolRemove(q);
								boardSpot++;
							}
						}
					}
					scan.close();
					_tempBoard = temp;
					_b = temp;
					get_g().set_gamePlayers(getRestore());
					get_g().set_currentPlayer(get_g().restoreOrder());
					_restore = true;
					state = 13;
					break;
				}
			}
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found (Restore)");
			return false;
		}

	}

	public ArrayList<Tile> getTilesFromMovePass() {
		return get_tilesFromMovePass();
	}

	public void set_letterMultipliers(int[][] _letterMultipliers) {
		this._letterMultipliers = _letterMultipliers;
	}

	public int[][] get_letterMultipliers() {
		return _letterMultipliers;
	}

	public void set_wordMultipliers(int[][] _wordMultipliers) {
		this._wordMultipliers = _wordMultipliers;
	}

	public int[][] get_wordMultipliers() {
		return _wordMultipliers;
	}

	public void set_tilesFromMovePass(ArrayList<Tile> _tilesFromMovePass) {
		this._tilesFromMovePass = _tilesFromMovePass;
	}

	public ArrayList<Tile> get_tilesFromMovePass() {
		return _tilesFromMovePass;
	}

	public void set_newTurn(boolean _newTurn) {
		this._newTurn = _newTurn;
	}

	public boolean is_newTurn() {
		return _newTurn;
	}

	public void setRestore(Player person) {
		_restoreList.add(person);
	}

	public List<Player> getRestore() {
		return _restoreList;

	}

	public boolean restoreValidate() {
		return _restore;
	}

	public boolean get_ranOutOfTiles() {
		return _ranOutOfTiles;
	}

	public void set_ranOutOfTiles(boolean _ranOutOfTiles) {
		this._ranOutOfTiles = _ranOutOfTiles;
	}

	public String get_dictionaryPath() {
		return _dictionaryPath;
	}

	public void set_dictionaryPath(String s) {
		this._dictionaryPath = s;
	}

	/**
	 * @param _tp the _tp to set
	 */
	public void set_tp(TilePool _tp) {
		this._tp = _tp;
	}

	/**
	 * @return the _tp
	 */
	public TilePool get_tp() {
		return _tp;
	}

	/**
	 * @param _g the _g to set
	 */
	public void set_g(Game _g) {
		this._g = _g;
	}

	/**
	 * @return the _g
	 */
	public Game get_g() {
		return _g;
	}

	/**
	 * @param _winner the _winner to set
	 */
	public void set_winner(Player _winner) {
		this._winner = _winner;
	}

	/**
	 * @return the _winner
	 */
	public Player get_winner() {
		return _winner;
	}
}
