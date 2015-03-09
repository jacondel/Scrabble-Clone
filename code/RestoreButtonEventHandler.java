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
public class RestoreButtonEventHandler implements ActionListener {

	private GUI _gui;
	private DataModel _dm;

	public RestoreButtonEventHandler(DataModel dm, GUI gui) {
		_gui = gui;
		_dm = dm;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (_dm.restoreValidate() == false) {
			_dm.restoreGame(null);
			_gui.generate();
			_gui.windowSize();
		}
	}

}
