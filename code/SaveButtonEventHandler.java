package code;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
/**
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class SaveButtonEventHandler implements ActionListener{

	private GUI _gui;
	private DataModel _dm;
	
	public SaveButtonEventHandler(DataModel dm, GUI gui) {
		_gui=gui;
		_dm=dm;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		_dm.saveGame();
		
	}

}