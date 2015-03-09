package code;

/**
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class Tile {
	//each tile must have a character designation and a point value
		private Character _character;
		
		
	/**Constructor creates a new tile with a point value and a character.
	 * @param the character associated with the tile
	 */	
	public Tile(Character c) {
		c=Character.toUpperCase(c);
		set_character(c);
	}
	
	/**Access method for character value.
	 * @return character value of tile.
	 */
	public Character getCharacter(){
		return get_character();
	}


	/**
	 * @param _character the _character to set
	 */
	public void set_character(Character _character) {
		this._character = _character;
	}


	/**
	 * @return the _character
	 */
	public Character get_character() {
		return _character;
	}


	public static int getScore(char c) {
		if(c=='A'){return 3;}
		if(c=='B'){return 7;}
		if(c=='C'){return 6;}
		if(c=='D'){return 5;}
		if(c=='E'){return 1;}
		if(c=='F'){return 7;}
		if(c=='G'){return 7;}
		if(c=='H'){return 4;}
		if(c=='I'){return 4;}
		if(c=='J'){return 8;}
		if(c=='K'){return 7;}
		if(c=='L'){return 5;}
		if(c=='M'){return 6;}
		if(c=='N'){return 4;}
		if(c=='O'){return 3;}
		if(c=='P'){return 7;}
		if(c=='Q'){return 8;}
		if(c=='R'){return 4;}
		if(c=='S'){return 4;}
		if(c=='T'){return 2;}
		if(c=='U'){return 6;}
		if(c=='V'){return 7;}
		if(c=='W'){return 6;}
		if(c=='X'){return 8;}
		if(c=='Y'){return 7;}
		if(c=='Z'){return 8;}
		return 0;
	}
}
