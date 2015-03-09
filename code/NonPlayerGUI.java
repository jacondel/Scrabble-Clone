package code;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.List;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
/**
 * 
 * @author jacondel
 * @author brandanj
 * @author djviggia
 * @author nrwheele
 */
public class NonPlayerGUI {
	private DataModel _dm;
	private ArrayList<JFrame> _nonPlayerWindows;
	private ArrayList<Player> _order;
	
	public NonPlayerGUI(DataModel dm) {
		_dm = dm;
		int[] temp = { -1, -1 };
		_dm.movePass(temp);
		
		_order = new ArrayList<Player>();
		for(int i=0;i<_dm.get_g().get_gamePlayers().size();i++){
			_order.add(_dm.get_g().get_gamePlayers().get(i));
		}
		
		_nonPlayerWindows = new ArrayList<JFrame>();
		for(int i=0;i<_dm.get_g().get_gamePlayers().size();i++){
			_nonPlayerWindows.add(new JFrame());		
			}
		


	}

	public void generate(){
		//board panel
		dispose();

		
		Board board = _dm.getTempBoard();
		_order = new ArrayList<Player>();
		for(int i=0;i<_dm.get_g().get_gamePlayers().size();i++){
			_order.add(_dm.get_g().get_gamePlayers().get(i));
		}
		_order.remove(_dm.get_g().get_gamePlayers().indexOf(_dm.get_g().get_currentPlayer()));
		for(int i=0;i<_order.size();i++){
			JFrame p = new JFrame();
			_nonPlayerWindows.add(p);
			JPanel bPanel = new JPanel();
			bPanel.setBorder(BorderFactory.createTitledBorder("Board"));
			bPanel.setLayout(new GridLayout(20,20));
			for (int row = 0; row < 20; row++) {
				for (int col = 0; col < 20; col++) {
						board.get(row, col);
					if (!(board.get(row, col).get_character() == ' ')) {
						if (row == board.get_hsRow() && col == board.get_hsCol()) {
							JPanel tilePanel = new JPanel();
							JLabel l = new JLabel( " "+board.get(row, col).getCharacter()+" " );
							l.setName(""+row+","+col);
							if (_dm.restoreValidate() == false) {
								Border HSborder = BorderFactory.createLineBorder(Color.ORANGE, 2);
								l.setBorder(HSborder);
								tilePanel.add(l);
								bPanel.add(tilePanel);
							}

						} else if (_dm.getTilesFromMovePass().contains(board.get(row, col))) {
							JPanel tilePanel = new JPanel();
							JLabel l = new JLabel( " "+board.get(row, col).getCharacter()+" " );
							Border border = BorderFactory.createLineBorder(Color.BLUE, 1);
							l.setBorder(border);
							l.setName(""+row+","+col);
							tilePanel.add(l);
							bPanel.add(tilePanel);
						} else if(board.get(row, col).get_character()!='_'){
							JPanel tilePanel = new JPanel();
							JLabel l = new JLabel( " "+board.get(row, col).getCharacter()+" " );
							Border border = BorderFactory.createLineBorder(Color.black, 1);
							l.setBorder(border);
							l.setName(""+row+","+col);
							tilePanel.add(l);
							bPanel.add(tilePanel);
						}
						else{ 
							JPanel tilePanel = new JPanel();
							JLabel l = new JLabel( " "+board.get(row, col).getCharacter()+" " );
							Border border = BorderFactory.createLineBorder(Color.gray, 1);
							l.setBorder(border);
							l.setName(""+row+","+col);
							tilePanel.add(l);
							bPanel.add(tilePanel);
						}
					} else {
						JPanel tilePanel = new JPanel();
						JLabel l = new JLabel( " "+" "+" " );
						tilePanel.add(l);
						bPanel.add(tilePanel);
					}
				}

				//player panel
				JPanel pPanel = new JPanel();
				pPanel.setBorder(BorderFactory.createTitledBorder(_order.get(i).get_playerName()));
				for(int j=0; j<12;j++){
					Tile[] t = _order.get(i).get_playerRack();
					JButton b = new JButton(" "+t[j].get_character()+" ");
					b.setName(""+i);
					pPanel.add(b);
				}
				p.add(pPanel, BorderLayout.PAGE_START);

				JPanel sPanel = new JPanel();
				sPanel.setBorder(BorderFactory.createTitledBorder("Score Board"));
				for(int k=0; k<_dm.get_g().get_gamePlayers().size();k++){			
					JLabel s = new JLabel(_dm.get_g().get_gamePlayers().get(k).get_playerName()+"--"+_dm.get_g().get_gamePlayers().get(k).get_playerScore());
					sPanel.add(s);
				}
				p.add(sPanel, BorderLayout.PAGE_END);
			}

			p.add(bPanel, BorderLayout.CENTER);
			p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			p.setSize(700,700);
			p.pack();
			p.setVisible(true);

		}
	}

	public void dispose(){
		for(int i=0;i<_order.size();i++){
		_nonPlayerWindows.get(i).dispose();
		
		}
		_nonPlayerWindows.clear();
	}





}