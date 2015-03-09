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
public class EndTurnEventHandler implements ActionListener {
	private DataModel _dm;
	private GUI _gui;
	
	public EndTurnEventHandler(DataModel dm, GUI gui){
		_dm=dm;
		_gui=gui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		_dm.endTurn();
		int[] temp = {-1,-1};
		_dm.movePass(temp);
		_gui.generate();
		
	}
	
}
