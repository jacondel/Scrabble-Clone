package code;

import java.util.HashMap;
import java.util.Random;
/**
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class TilePool {
	private HashMap<Character, Integer> _pool;

	/**Constructor creates a tilepool to track the 400 required characters and separates them into 3 different 
	 * hashmaps for scoring.
	 * */
	public TilePool(){

		_pool = new HashMap<Character, Integer>();
		PoolPopulator('A',41);
		PoolPopulator('B',7);
		PoolPopulator('C',14);
		PoolPopulator('D',21);
		PoolPopulator('E',56);
		PoolPopulator('F',11);
		PoolPopulator('G',10);
		PoolPopulator('H',30);
		PoolPopulator('I',35);
		PoolPopulator('J',2);
		PoolPopulator('K',4);
		PoolPopulator('L',20);
		PoolPopulator('M',12);
		PoolPopulator('N',33);
		PoolPopulator('O',37);
		PoolPopulator('P',10);
		PoolPopulator('Q',2);
		PoolPopulator('R',30);
		PoolPopulator('S',31);
		PoolPopulator('T',45);
		PoolPopulator('U',14);
		PoolPopulator('V',5);
		PoolPopulator('W',12);
		PoolPopulator('X',2);
		PoolPopulator('Y',10);
		PoolPopulator('Z',2);





	}

	/**This helper method populates hashmap with characters and their corresponding values .
	 * @param a The character to populate.
	 * @param amount the amount of characters to populate
	 * */
	private void PoolPopulator(Character a, int amount){
		for(int i=0; i<amount; i++){
			if(_pool.containsKey(a)){
				_pool.put(a, _pool.get(a)+1);
			}
			else{
				_pool.put(a, 1);
			}
		}
	}



	/**This method grabs a random character from those in the pool and adjusts the count of that available character
	in the pool.if no more of that character are available removes that character from the pool.
	if no characters are available it returns '~'.
	@return a random letter drawn from the tilepool.*/
	public Character getRandomLetterFromPool(){
		int index = 0;
		int total=0;
		char c;
		
		for(int j=0;j<26;j++){
			c=(char)('A'+j);
			total+=_pool.get(c);
		}                                            //count how many tiles are left
		if(total==0){                                //if there are none, return ~  
			return '~';
		}
		
		index = 1+(int)(Math.random()*total);       //get a random index from 1-total 
		c=(char)('A'-1);                            //start at A-1
		
		do{
			c=(char)(c+1);                         //go to the next letter  
			index-= _pool.get(c);	 			   //subtract how many were there
		}
		while(index>0);                            //if the index is zero of lower, then c is the letter range that holds index
		_pool.put(c,_pool.get(c)-1);
		return c;

	}
	
	public void tilePoolRemove(char c){
		_pool.put(c, _pool.get(c)-1);
	}
	public int numberOfTiles(char c){
		return _pool.get(c);
	}

}


