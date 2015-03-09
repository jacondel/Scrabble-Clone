package code;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class playerInquiryEventHandler implements ActionListener{

	
	
	
	

	private DataModel _dm;
	private StartUpInquiry _numOfPlayers;
	
	
	public playerInquiryEventHandler(DataModel dm, StartUpInquiry startUpInquiry) {
		_numOfPlayers=startUpInquiry;
		_dm=dm;
	}

	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int numOfPlayers = _numOfPlayers.amountOfPlayers();
		_dm.startUp(numOfPlayers);	
		GUI gui = new GUI(_dm);
		gui.generate();
		_numOfPlayers.get_playerInquiryWindow().dispose();

	}
}
