package code;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
/**
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class StartUpInquiry {

	private JFrame _playerInquiryWindow;
	private JSpinner _spinner;
	private  playerInquiryEventHandler _playerInquirybeh;
	
	
	
	/**
	 * A Temporary testing method that creates a 
	 * 
	 * **/
	
	
	public  void playerInquiry(DataModel dm){

		set_playerInquiryWindow(new JFrame("How many Players?"));
		JPanel inqPanel = new JPanel();
		get_playerInquiryWindow().add(inqPanel);
		get_playerInquiryWindow().setLayout(new FlowLayout());

		SpinnerModel model = new SpinnerNumberModel(2, 2, 100, 1);
		_spinner = new JSpinner(model);
		_spinner.setEditor(new JSpinner.NumberEditor(_spinner));	         
		inqPanel.add(_spinner);
		get_playerInquiryWindow().setDefaultCloseOperation(get_playerInquiryWindow().EXIT_ON_CLOSE);
		get_playerInquiryWindow().setSize(400, 400);
		get_playerInquiryWindow().setVisible(true);

		JPanel submitPanel = new JPanel();
		get_playerInquiryWindow().add(submitPanel);
		JButton submit = new JButton("Submit");
		_playerInquirybeh = new playerInquiryEventHandler(dm, this);
		submit.addActionListener(_playerInquirybeh);
		submitPanel.add(submit);
		get_playerInquiryWindow().pack();


	}
	
	public int amountOfPlayers(){
		int value = (Integer) _spinner.getValue();
		return value;
	}

	/**
	 * @param _playerInquiryWindow the _playerInquiryWindow to set
	 */
	public void set_playerInquiryWindow(JFrame _playerInquiryWindow) {
		this._playerInquiryWindow = _playerInquiryWindow;
	}

	/**
	 * @return the _playerInquiryWindow
	 */
	public JFrame get_playerInquiryWindow() {
		return _playerInquiryWindow;
	}

}
