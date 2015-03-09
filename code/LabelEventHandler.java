package code;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
/**
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class LabelEventHandler implements MouseListener {

	private DataModel _dm;
	private GUI _gui;
	public LabelEventHandler(DataModel dm, GUI gui){
		_dm=dm;
		_gui=gui;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		JLabel _lastClicked=(JLabel)e.getSource();
		String str=_lastClicked.getName();
		//work around the comma
		int comma=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)==','){
				comma=i;
			}
		}
		String row=str.substring(0,comma);
		String col=str.substring(comma+1, str.length());
		int second=Integer.parseInt(row);
		int third=Integer.parseInt(col);
		int[] input = {second,third};
		_gui.setLastClickedBorderLabelCoordinates(input);
		//
		int first=_gui.getlastClickedRackIndex();
		int[] temp = {first, second,third};
		_dm.movePass(temp);
		_gui.generate();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e){}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	

	
}

