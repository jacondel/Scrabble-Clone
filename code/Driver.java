package code;
/**
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class Driver {

	public static void main(String[] args){
		commandLineStartUp(args);

	}


	public static void commandLineStartUp(String[] args){
		DataModel dm = new DataModel();
		int playerCount=0;
		if(args.length==1){
			if(dm.restoreValidate()==false){
				dm.restoreGame(args[0]);
				GUI gui = new GUI(dm);
				gui.generate();
				gui.windowSize();
			}
		}
		for(int i=0;i<args.length-1;i++){
			if(args[i].equals("-p")){
				Player p = new Player(args[i+1]);
				p.generateRack(dm.get_tp());
				dm.get_g().register(p);
				playerCount++;
			}
			if(args[i].equals("-d")){
				dm.set_dictionaryPath(args[i+1]);
			}
		}
		if(playerCount>1){
			dm.get_g().start();
			GUI gui = new GUI(dm);
			gui.generate();
		}
	}
	public static void inputWindowStartUp(){
		DataModel dm = new DataModel();
		StartUpInquiry sui= new StartUpInquiry();
		sui.playerInquiry(dm);
	}











}
