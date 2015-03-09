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
public class ButtonEventHandler implements ActionListener{

	private GUI _gui;
	
	public ButtonEventHandler(GUI gui) {
		_gui=gui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton _lastClicked =(JButton)e.getSource();
		_gui.setLastClickedRackIndex(Integer.parseInt(_lastClicked.getName()));
		
		
	}

}
