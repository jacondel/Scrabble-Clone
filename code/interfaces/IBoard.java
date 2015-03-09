package code.interfaces;

import code.Tile;

/**
 * An interface for the board class of the stage 1 version of the game.
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
public interface IBoard {

	/**
	 * The board has a square identified as the "home square".  This method
	 * returns the row in which the "home square" is located.  Rows are numbered
	 * starting at one (i.e. the top row of the board is row number one).
	 * 
     * @return an int representing the row in which the "home square" appears
     */
    public int homeSquareRow();
	/**
	 * The board has a square identified as the "home square".  This method
	 * returns the column in which the "home square" is located.  Columns are
	 * numbered starting at one (i.e. the leftmost column of the board is 
	 * column number one).
	 * 
     * @return an int the column  in which the "home square" appears
     */
    public int homeSquareCol();

    /**
     * An accessor method for the Tile on a given square of the board.
     * The square is indicated by its row and column position on the board.
     * The behavior of this method is undefined if the row and column
     * values given do not refer to a position on the board, or if that
     * position does not contain a Tile.
     * 
     * @param row - the row of the square being queried, expressed as an int
     * @param col - the column of the square being queried, expressed as an int
     *
     * @return - the Tile on the square indicated by row and col, if that square
     * exists and has a Tile on it
     */
    public Tile get(int row, int col);
    
    /**
     * Attempt to place a given tile on the square indicated.
     * The tile can be placed only if the square indicated by the row and column
     * positions exists and does not already contain a Tile.
     * 
     * @param tile - the Tile to place
     * @param row - the row in which to place the Tile, expressed as an int
     * @param col - the column in which to place the Tile, expressed as an int
     * 
     * @return true if the placement was successful, false otherwise
     */
    public boolean place(Tile tile, int row, int col);

    /**
     * Attempt to remove a given tile from the board.
     * A tile can be removed from the board only if it is on the board, 
     * and only during the turn that it was placed there.
     * 
     * @param tile - the Tile to be removed
     * 
     * @return true if the tile was successfully removed, false otherwise
     */
    public boolean retract(Tile tile);

    /**
     * Determine whether the square at the indicated row and column location
     * contains a Tile or not.  This method must return false if the given row 
     * and column do not refer to a location on the board.
     * 
     * @param row - the row of the square being queried, expressed as an int
     * @param col - the column of the square being queried, expressed as an int
     * 
     * @return true if the squared indicated by row and col exists and does not
     * contain a Tile, false otherwise (i.e. if the square does not exist, or if
     * it does exist but contains a Tile).
     */
    public boolean isEmpty(int row, int col);

    /**
     * Provides a String representation of the current state of the board.
     * The String representation must represent the rows of the board separated
     * by newline characters.  Within a row, each column is represented by a
     * single character.  An empty square is represented by an underscore, '_',
     * while an occupied square is represented by the letter on its tile, e.g.
     * 'A' for a square with a Tile whose letter is 'A'.  Each row must have the
     * same number of characters in it (so the representation is square).  Since
     * the board itself is not square, any cell in the representation which does 
     * not correspond to a square on the board is represented by space.
     * 
     * @return a String representing the current state of the board.
     */
    public String boardConfiguration();
}
