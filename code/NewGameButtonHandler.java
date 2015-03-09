
	package code;

	import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;

/**
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
	public class NewGameButtonHandler  implements ActionListener {


		private GUI _gui;
		private DataModel _dm;
		
		public NewGameButtonHandler(DataModel dm, GUI gui) {
			_gui=gui;
			_dm=dm;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			_gui.get_winnerWindow().dispose();
			DataModel dm = new DataModel();
			StartUpInquiry sui= new StartUpInquiry();
			sui.playerInquiry(dm);

		}

	}


