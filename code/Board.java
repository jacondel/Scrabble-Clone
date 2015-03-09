package code;

import java.util.Random;

import code.interfaces.IBoard;
/**
 * This class is responsible for opperations regarding adding and removing tiles to the 2D array.
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class Board implements IBoard{
	/**Representation of the Board.*/
	private Tile[][] _b;
	/**Home Square location coordinates.*/
	private int _hsCol;
	private int _hsRow;
	
	/**Constructor creates a representation of the Board in a 2D array. With the # of arrays being the 
	 * Rows and the contents of the arrays being the Columns.  It then populates the board with 
	 * tiles of no point value containing spaces for unused spaces and '_' for places where tiles will
	 * be placed.  The home square is then randomly selected and assigned 0 point value and '@' as its
	 * character value.
	*/
	public Board(){
		_b = new Tile[20][20];
		
		int randomi=(int)(Math.random()*5)+3;
		int randomj=(int)(Math.random()*5)+3;
		
		for(int i=0;i<20;i++){
			for(int j=0; j<20;j++){
				if((i<randomi && j>19-randomj) || (i>19-randomi && j<randomj)){
					_b[i][j]=new Tile(' ');
				}
				else{
					_b[i][j] = new Tile('_');
				}
			}
		}
		Random rand = new Random();
		do{
			set_hsCol(rand.nextInt(20));
			set_hsRow(rand.nextInt(20));
			if((!(get_hsRow()<randomi && get_hsCol()>19-randomj)) && (!(get_hsRow()>19-randomi && get_hsCol()<randomj))){
			_b[get_hsRow()][get_hsCol()] = new Tile('_');
			}
		}while((get_hsRow()<randomi && get_hsCol()>19-randomj) || (get_hsRow()>19-randomi && get_hsCol()<randomj));
	}	
	 
	
	/**This method tells the location of the home square in reference to the boards arrays.
	 *@return an integer representing the index of the home Square row on the board.
	*/
	@Override
	public int homeSquareRow() {

		return get_hsRow();
	}
	
	
	/**This method identifies the location of the home square in reference to the contents of the boards 
	 * arrays.
	 * @return an int representing the index of the home Square column on the board.
	*/
	@Override
	public int homeSquareCol() {

		return get_hsCol();
	}
	
	
	/**This method accesses the underlying Array of the Board 
	 * @return _b which representing the Board
	*/
	public Tile[][] getBoard(){
		return _b;
	}
	
	
	/**This method mutates the underlying Array of the Board 
	 * @param  Tile[][] b representing the Board
	*/
	public void setBoard(Tile[][] b){
		_b=b;
	}
	
	/**	 This method accesses the tile in the board 2d array at the intersection
	 * of the inputed row and column.
	 * @param row; An int representing the index of desired array.
	 * @param col; An int that represents the index of the content of desired array.
	 * @return Tile the tile located on the board at the intersection of the inputed row and column.
	 * */
	@Override
	public Tile get(int row, int col) {

		return _b[row][col];
	}

	/**This method attempts to place the inputed tile at the intersection of the inputed row and column.
	 * @param tile  The tile being place on the board.
	 * @param row index of desired array.
	 * @param col index of the content of desired array
	 * @return true if successfully placed tile in board 2d array and False if not.
	*/
	@Override
	public boolean place(Tile tile, int row, int col) {

		if(isEmpty(row,col) && row >=0 && row<20 && col >=0 && col<20 ){
			_b[row][col]= tile;
			return true;
		}
		else{
			return false;
		}
	}
	
	/**This method attempts to retract a placed tile from the board.
	 * @param tile The tile to be removed from the board.
	 * @return true if the desired tile was removed and false otherwise.*/
	@Override
	public boolean retract(Tile tile) {
		for(int i=0;i<20;i++){
			for(int j=0;j<20;j++){
				if(_b[i][j]==tile){
					_b[i][j]=new Tile('_');
					return true;
				}
			}
		}
		return false;
	}
	
	/**This method checks the tile in the designated space on the board to verify another tile has not been 
	 * placed there.	 
	 * @param row an int of the index of desired array.
	 * @param col an int of the index of the content of desired array
	 * @return true if a tile can be placed at the intersection of the inputed row and column.  False if not.
	 * */
	@Override
	public boolean isEmpty(int row, int col) {
		if(!(row >=0 && row<20 && col >=0 && col<20)){// if it is not within the 20x20 board return true
			return true;
		}
		Tile t = _b[row][col];
		if(t.getCharacter() =='_' || t.getCharacter()=='@'||t.getCharacter()==' '){
			return true;
		}
		else{
			return false;
		}
	}

	/**This method converts the 2D array of the board into a String representation where empty spaces 
	 * are '_' characters and ' ' designates an unusable space the arrays are separated by a newline 
	 * character.
	 * @return a String representation of the Board
	 * */
	@Override
	public String boardConfiguration() {
		String board="";
		for(int i =0;i<20;i++){
			for(int j=0;j<20;j++){
					board = board + _b[i][j].get_character();
		}
			board=board+'\n';
		}
		
		return board;
	}
	
	/**
	 * @param _hsCol the _hsCol to set
	 */
	public void set_hsCol(int _hsCol) {
		this._hsCol = _hsCol;
	}


	/**
	 * @return the _hsCol
	 */
	public int get_hsCol() {
		return _hsCol;
	}


	/**
	 * @param _hsRow the _hsRow to set
	 */
	public void set_hsRow(int _hsRow) {
		this._hsRow = _hsRow;
	}


	/**
	 * @return the _hsRow
	 */
	public int get_hsRow() {
		return _hsRow;
	}


	/**
	 * This method finds the value of the row that the tile being searched for is placed in
	 * @param Tile
	 * @returns an integer which represents the row that the tile being searched for was placed in
	 **/
	public int rowOf(Tile tile) {
		for(int i=0;i<20;i++){
			for(int j=0; j<20;j++){
				if(tile==_b[i][j])
				return i;
			}
		}
		return -1;
	}


	
	/**
	 * This is a method that finds the column location of the tile that is passed in the 
	 * argument list
	 * @param	Tile that your looking for
	 * @returns  Column location of the tile that is being passed in
	 **/
	public int colOf(Tile tile) {
		for(int i=0;i<20;i++){
			for(int j=0; j<20;j++){
				if(tile==_b[i][j])
				return j;
			}
		}
		return -1;
	}
	
	

}



//saving players score 

//if turn is valid 
//add score to current score and keep tiles on the board 
//or else do not let the player make that move 
//or skip 


// public boolean scoreUpdate {
// if (curentplays currentturn is valid than get point value and add to current score)
// if not valid than 











