package code;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
/**
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class RemoveButtonEventHandler implements ActionListener{






	private GUI _gui;
	private DataModel _dm;

	public RemoveButtonEventHandler(DataModel dm, GUI gui) {
		_gui=gui;
		_dm = dm;
	}





	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("remove button clicked");
		int[] coords = _gui.getLastClickedBoardLabelCoordinates();
		//if the tile at these coordinates is in tiles from move pass-- get the index of it in 
		if(_dm.getTilesFromMovePass().contains(_dm.getTempBoard().get(coords[0], coords[1]))){
			System.out.println("remove button if statemend entered");
			System.out.println("coords: "+ coords[0]+","+coords[1]);
			int index=_dm.getTilesFromMovePass().indexOf(_dm.getTempBoard().get(coords[0], coords[1]));
			System.out.println("index: " +index);
			int[] temp = {-1,index};
			_dm.movePass(temp);
			_gui.generate();
		}




	}


}
