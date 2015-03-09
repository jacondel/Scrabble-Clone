package code;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
/**
 * This class displays the game visually.
 * 
* @author jacondel
 *@author brandanj
 *@author djviggia
 *@author nrwheele
 */
public class GUI {
	private DataModel _dm;
	private JFrame _gameWindow;

	private JFrame _playerInquiryWindow;
	private LabelEventHandler _leh;
	private ButtonEventHandler _beh;
	private SaveButtonEventHandler _sbeh;
	private RestoreButtonEventHandler _rbeh;
	private int _lastClickedRackIndex;
	private EndTurnEventHandler _eteh;
	private RemoveButtonEventHandler _removebeh;
	private NewGameButtonHandler _newGamebeh;

	private JFrame _winnerWindow;
	private int[] _lastClickedBoardLabelCoordinates;
	private NonPlayerGUI _npw;

	public GUI(DataModel dm) {
		_dm = dm;
		int[] temp = { -1, -1 };
		_dm.movePass(temp);
		_leh = new LabelEventHandler(_dm, this);
		_beh = new ButtonEventHandler(this);
		_gameWindow = new JFrame();
		_playerInquiryWindow = new JFrame();
		_eteh = new EndTurnEventHandler(_dm, this);
		_sbeh = new SaveButtonEventHandler(_dm, this);
		_rbeh = new RestoreButtonEventHandler(_dm, this);
		_removebeh = new RemoveButtonEventHandler(_dm, this);
		_newGamebeh = new NewGameButtonHandler(_dm, this);
		_npw = new NonPlayerGUI(_dm);

	}

	/**
	 * This method creates the GUI system the user will play the game through.
	 * IE; the board, player scores, rack of the current player, and a utility
	 * window for saving games restoring saved games and ending turns. This
	 * method replaces all of these elements every move and on the final move
	 * ends the game and declares the winner along with all players scores and
	 * an option to start a new game.
	 * **/
	public void generate() {
		// Generates board.
		_gameWindow.dispose();
		Board board = _dm.getTempBoard();
		_gameWindow = new JFrame("Scrabble");
		_gameWindow.setLayout(new BorderLayout());
		JPanel boardPanel = new JPanel();
		boardPanel.setBorder(BorderFactory.createTitledBorder("Board"));
		boardPanel.setLayout(new GridLayout(20,20));
		for (int row = 0; row < 20; row++) {
			for (int col = 0; col < 20; col++) {
				board.get(row, col);
				if (!(board.get(row, col).get_character() == ' ')) {
					if (row == board.get_hsRow() && col == board.get_hsCol()&& _dm.restoreValidate() == false) {
						JPanel tilePanel = new JPanel();
						JLabel l = new JLabel( " "+board.get(row, col).getCharacter()+" " );
						l.setName(""+row+","+col);
							Border HSborder = BorderFactory.createLineBorder(Color.ORANGE, 2);
							l.setBorder(HSborder);
							l.addMouseListener(_leh);
							tilePanel.add(l);
							boardPanel.add(tilePanel);
						

					} else if (_dm.getTilesFromMovePass().contains(board.get(row, col))) {
						JPanel tilePanel = new JPanel();
						JLabel l = new JLabel( " "+board.get(row, col).getCharacter()+" " );
						Border border = BorderFactory.createLineBorder(Color.BLUE, 1);
						l.setBorder(border);
						l.setName(""+row+","+col);
						l.addMouseListener(_leh);
						tilePanel.add(l);
						boardPanel.add(tilePanel);
					} else if(board.get(row, col).get_character()!='_'){
						JPanel tilePanel = new JPanel();
						JLabel l = new JLabel( " "+board.get(row, col).getCharacter()+" " );
						Border border = BorderFactory.createLineBorder(Color.black, 1);
						l.setBorder(border);
						l.setName(""+row+","+col);
						l.addMouseListener(_leh);
						tilePanel.add(l);
						boardPanel.add(tilePanel);
					}
					else{ 
						JPanel tilePanel = new JPanel();
						JLabel l = new JLabel( " "+board.get(row, col).getCharacter()+" " );
						Border border = BorderFactory.createLineBorder(Color.gray, 1);
						l.setBorder(border);
						l.setName(""+row+","+col);
						l.addMouseListener(_leh);
						tilePanel.add(l);
						int[][] wm = _dm.get_wordMultipliers();
						int[][] lm = _dm.get_letterMultipliers();
						l.setOpaque(true);
						if(wm[row][col]==0){
							tilePanel.setBackground(Color.RED);
						}
						if(wm[row][col]==2){
							tilePanel.setBackground(Color.CYAN);
						}
						if(wm[row][col]==3){
							tilePanel.setBackground(Color.GREEN);
						}
						if(lm[row][col]== -1){
							l.setBackground(new Color(199,21,133));
						}
						if(lm[row][col]== 0){
							l.setBackground(Color.RED);
						}
						if(lm[row][col]== 2){
							l.setBackground(Color.CYAN);
						}
						boardPanel.add(tilePanel);
					}
				} else {
					JPanel tilePanel = new JPanel();
					JLabel l = new JLabel( " "+" "+" " );
					tilePanel.add(l);
					boardPanel.add(tilePanel);
				}
			}

		}
		_gameWindow.add(boardPanel, BorderLayout.CENTER);
		

		JPanel playerPanel = new JPanel();
		playerPanel.setBorder(BorderFactory.createTitledBorder("Your Turn "+_dm.get_g().get_currentPlayer().get_playerName()));
		for(int i=0; i<12;i++){
			Tile[] t = _dm.getTempRack();
			JButton b = new JButton(" "+t[i].get_character()+" ");
			b.setName(""+i);
			b.addActionListener(_beh);
			playerPanel.add(b);
		}
		_gameWindow.add(playerPanel, BorderLayout.PAGE_START);

		JPanel scorePanel = new JPanel();
		scorePanel.setBorder(BorderFactory.createTitledBorder("Score Board"));
		for(int i=0; i<_dm.get_g().get_gamePlayers().size();i++){			
			JLabel s = new JLabel(_dm.get_g().get_gamePlayers().get(i).get_playerName()+"--"+_dm.get_g().get_gamePlayers().get(i).get_playerScore());
			scorePanel.add(s);
		}
		_gameWindow.add(scorePanel, BorderLayout.PAGE_END);


		JPanel utilityPanel = new JPanel();

		utilityPanel.setLayout(new GridLayout(0,1));
		utilityPanel.setBorder(BorderFactory.createTitledBorder("Utilities"));
		JButton end = new JButton("End Turn");
		end.addActionListener(_eteh);
		utilityPanel.add(end);

		JButton remove = new JButton("Remove Tile");
		remove.addActionListener(_removebeh);
		utilityPanel.add(remove);


		JButton save = new JButton("Save Game");
		save.addActionListener(_sbeh);
		utilityPanel.add(save);


		JButton restore = new JButton("Restore Game");
		restore.addActionListener(_rbeh);
		utilityPanel.add(restore);
		_gameWindow.add(utilityPanel, BorderLayout.LINE_END);


		//sets close operation, packs, sizes, and sets visibility
		_gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_gameWindow.setSize(700,700);
		_gameWindow.pack();
		_gameWindow.setVisible(true);
		_gameWindow.setAlwaysOnTop(true);
		
		

		_npw.generate();
		
		
		

		// Winner notification frame.
		if (_dm.everyonePassed()||_dm.get_ranOutOfTiles()) {
			set_winnerWindow(new JFrame("Game Over!"));
			get_winnerWindow().setLayout(new GridLayout(0, 1));
			get_winnerWindow().setLocation(725, 200);

			JPanel winnerPanel = new JPanel();
			get_winnerWindow().add(winnerPanel);
			JLabel w = new JLabel("You Win! " + _dm.get_winner().get_playerName()
					+ " Congratulations!  " + "Your Score: "
					+ _dm.get_winner().get_playerScore());
			get_winnerWindow().add(w);
			_dm.get_g().get_gamePlayers().remove(_dm.get_winner());
			for (int j = 0; j < _dm.get_g().get_gamePlayers().size(); j++) {
				JPanel loserPanel = new JPanel();
				get_winnerWindow().add(loserPanel);
				JLabel l = new JLabel("You Lose. "
						+ _dm.get_g().get_gamePlayers().get(j).get_playerName()
						+ "  Your Score: "
						+ _dm.get_g().get_gamePlayers().get(j).get_playerScore());
				get_winnerWindow().add(l);
			}

			// start new game option button
			JPanel newGame = new JPanel();
			get_winnerWindow().add(newGame);
			JButton startNewGame = new JButton("Start New Game?");
			startNewGame.addActionListener(_newGamebeh);
			get_winnerWindow().add(startNewGame);

			get_winnerWindow().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			get_winnerWindow().pack();
			get_winnerWindow().setVisible(true);
			
			


			// kills ability to play by removing JFrame GUI.

			_gameWindow.dispose();
		}

	}



	/**
	 *This method gets the index of the last location that was 
	 *clicked on the players rack
	 *@returns the index on the rack that was last clicked
	 **/
	public int getlastClickedRackIndex() {
		return _lastClickedRackIndex;
	}



	/**
	 * This method sets the index on the rack of the last time clicked
	 * @param int i which is the last space on the players rack that was clicked**/
	public void setLastClickedRackIndex(int i) {
		_lastClickedRackIndex = i;
	}


	/**
	 * This method sets the window of the winner at the games conclusion
	 * @param JFrame _winnerWindow
	 **/
	public void set_winnerWindow(JFrame _winnerWindow) {
		this._winnerWindow = _winnerWindow;
	}

	/**
	 * This method gets the JFrame window for the winner when the came is ended
	 * @return the window displaying winner
	 **/
	public JFrame get_winnerWindow() {
		return _winnerWindow;
	}

	/**
	 * This method sets coordinates at the last letter played
	 * @param int[] input which is the coordinates of the last clicked space
	 **/
	public void setLastClickedBorderLabelCoordinates(int[] input) {
		_lastClickedBoardLabelCoordinates = input;
	}


	/**
	 * This method gets the coordinates of the last letter played 
	 * @returns coordinates of the last letter played
	 **/
	public int[] getLastClickedBoardLabelCoordinates() {
		return _lastClickedBoardLabelCoordinates;
	}
	public void windowSize(){
		_gameWindow.setSize(700,700);
		_gameWindow.pack();
	}

}
