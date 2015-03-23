package paw;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

//import paw.Config.languages;






public class PAWguiLL {
	private JFrame frame;
	private PAWgame newGame;
	private JProgressBar progressBar;
	private JComboBox<String> languageComboBox;
	private JSpinner lengthOfWordsSpinner;
	private JSpinner numOfWordsSpinner;
	private ArrayList<String> guessWord = new ArrayList<>();
	private static PAWguiLL window;
	private int clickCount = 0;
	private ArrayList<AnswerTile> answerTiles = new ArrayList<>();
	private ArrayList<GridTile> gridTiles = new ArrayList<>(); 
	private JPanel guessPanel;
	private JLabel lblWordsFound;
	
	class TileBoard extends JPanel {
		private int columns;


		/**
		 * Create the panel.
		 */
		public TileBoard(ArrayList<String> characters) {			
			columns = newGame.getWordLength();
			this.setLayout(new GridLayout(0, columns));
			gridTiles = new ArrayList<>(); 
			for (int i = 0; i < characters.size(); i++) {			
				GridTile newTile = new GridTile(characters.get(i),i);
				gridTiles.add(newTile);
				add(newTile);
				revalidate();
			}
		}

	}
	class Tile extends JToggleButton {
		int clickedPosition = -1;
		int tileId = -1;

		Tile(){
			setFont(new Font("Arial Unicode MS", Font.PLAIN, 24));
		}

	}
	class GridTile extends Tile {
		int clickedPosition = -1;
		int tileId = -1;
		int columnNum;
		String character = "";
		Color randomColor = new Color((int)(Math.random()*255)+1,(int)(Math.random()*255)+1,(int)(Math.random()*255)+1);
		Color pressedColor = Color.WHITE;

		GridTile(String character, int iD){
			super();
			setText(character);			
			tileId = iD;			
			setBackground(randomColor);
			columnNum = iD % newGame.getWordLength();
			addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					for (GridTile gridTile : gridTiles) {
						if(gridTile.columnNum == columnNum &&
								gridTile.clickedPosition > -1 && gridTile.tileId != tileId){
							return;
						}
					}
					if (clickCount == newGame.getWordLength()){
						if(clickedPosition < 0){
							return;
						}
					}

					if (clickedPosition == -1){
						clickedPosition = clickCount++;
						AnswerTile at = answerTiles.get(columnNum);
						at.character = character;
						at.setText(character);
						at.setVisible(true);
						at.getParent().revalidate();
						setBackground(pressedColor);
						getParent().revalidate();
						guessWord.add(clickedPosition, character);
					} else {
						AnswerTile nt = answerTiles.get(columnNum);
						nt.setVisible(false);
						nt.getParent().revalidate();
						setBackground(randomColor);
						getParent().revalidate();
						clickCount--;
						guessWord.remove(clickedPosition);
						for (GridTile gridTile : gridTiles) {
							if(gridTile.clickedPosition > clickedPosition){
								gridTile.clickedPosition -= 1;
							}
						}
						clickedPosition = -1;
					}
					if (clickCount == newGame.getWordLength()){
						if(newGame.isCorrectWord(guessWord)){
							lblWordsFound.setText("Words Found: ("+newGame.getNumberOfWordsFound() +"/" + newGame.getNumberOfWords() + ")");
							lblWordsFound.getParent().revalidate();
							progressBar.setValue(newGame.getNumberOfWordsFound());
							Timer timer = new Timer();
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.GREEN);
							} 

							for (GridTile gridTile : gridTiles) {
								if (gridTile.clickedPosition >-1){
									timer.schedule(new TimerTask() {

										@Override
										public void run() {
											gridTile.setVisible(false);
											gridTile.clickedPosition = -1;


										}
									}, 1000);

								}									
							}
							
							clickCount = 0;
							guessWord.clear();
							for (AnswerTile answerTile : answerTiles) {
								timer.schedule(new TimerTask() {

									@Override
									public void run() {
										answerTile.setVisible(false);



									}
								}, 1000);
							}
						} 
						else {
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.RED);
							} 
						}

					}						
				}

			});
		}

	}
	class AnswerTile extends Tile {
		int tileId = -1;
		String character = "";
		Color color = Color.WHITE;

		AnswerTile(String character, int iD){
			super();
			setText(character);			
			tileId = iD;			
			setBackground(color);
			setVisible(false);
		}

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new PAWguiLL();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PAWguiLL() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		newGame = new PAWgame();
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(1000,800));
		frame.setBounds(100, 100, 634, 492);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);

		JPanel settingsPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, settingsPanel, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, settingsPanel, 0, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(settingsPanel);
		SpringLayout sl_settingsPanel = new SpringLayout();
		settingsPanel.setLayout(sl_settingsPanel);

		JPanel tileBoardPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, settingsPanel, 0, SpringLayout.SOUTH, tileBoardPanel);
		springLayout.putConstraint(SpringLayout.EAST, settingsPanel, -6, SpringLayout.WEST, tileBoardPanel);
		springLayout.putConstraint(SpringLayout.NORTH, tileBoardPanel, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, tileBoardPanel, -80, SpringLayout.SOUTH, frame.getContentPane());
		tileBoardPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		springLayout.putConstraint(SpringLayout.WEST, tileBoardPanel, 235, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, tileBoardPanel, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(tileBoardPanel);
		tileBoardPanel.setLayout(new GridLayout(1, 0, 0, 0));
		tileBoardPanel.add(new TileBoard(newGame.newGameCharacters()));

		guessPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, guessPanel, -80, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, guessPanel, 0, SpringLayout.WEST, tileBoardPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, guessPanel, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, guessPanel, 0, SpringLayout.EAST, tileBoardPanel);
		frame.getContentPane().add(guessPanel);
		addAnswerBlocks(guessPanel);
		JButton btnExitGame = new JButton("Exit Game");
		springLayout.putConstraint(SpringLayout.WEST, btnExitGame, 118, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnExitGame, -22, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnExitGame, -27, SpringLayout.WEST, guessPanel);
		guessPanel.setLayout(new GridLayout(1, 0, 0, 0));
		btnExitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		frame.getContentPane().add(btnExitGame);

		JButton btnStartNewGame = new JButton("New Game");
		springLayout.putConstraint(SpringLayout.NORTH, btnStartNewGame, 0, SpringLayout.NORTH, btnExitGame);
		springLayout.putConstraint(SpringLayout.EAST, btnStartNewGame, -6, SpringLayout.WEST, btnExitGame);
		btnStartNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newGame = new PAWgame(languageComboBox.getModel().getSelectedItem().toString(),
						(int)lengthOfWordsSpinner.getValue(), (int)numOfWordsSpinner.getValue());
				TileBoard newGamePanel = new TileBoard(newGame.newGameCharacters());
				tileBoardPanel.removeAll();
				tileBoardPanel.add(newGamePanel);
				tileBoardPanel.revalidate();
				guessPanel.removeAll();
				addAnswerBlocks(guessPanel);
				guessPanel.revalidate();
			}
		});

		lengthOfWordsSpinner = new JSpinner();
		lengthOfWordsSpinner.setModel(new SpinnerNumberModel(5, 4, 6, 1));
		settingsPanel.add(lengthOfWordsSpinner);

		JLabel lblSelectLengthOf = new JLabel("Select Length of Words");
		sl_settingsPanel.putConstraint(SpringLayout.NORTH, lengthOfWordsSpinner, -6, SpringLayout.NORTH, lblSelectLengthOf);
		sl_settingsPanel.putConstraint(SpringLayout.NORTH, lblSelectLengthOf, 26, SpringLayout.NORTH, settingsPanel);
		sl_settingsPanel.putConstraint(SpringLayout.WEST, lblSelectLengthOf, 10, SpringLayout.WEST, settingsPanel);
		settingsPanel.add(lblSelectLengthOf);

		JLabel lblSelectNumberOf = new JLabel("Select Number of Words");
		sl_settingsPanel.putConstraint(SpringLayout.NORTH, lblSelectNumberOf, 26, SpringLayout.SOUTH, lblSelectLengthOf);
		sl_settingsPanel.putConstraint(SpringLayout.WEST, lblSelectNumberOf, 0, SpringLayout.WEST, lblSelectLengthOf);
		settingsPanel.add(lblSelectNumberOf);

		numOfWordsSpinner = new JSpinner();
		sl_settingsPanel.putConstraint(SpringLayout.EAST, lengthOfWordsSpinner, 0, SpringLayout.EAST, numOfWordsSpinner);
		sl_settingsPanel.putConstraint(SpringLayout.NORTH, numOfWordsSpinner, -6, SpringLayout.NORTH, lblSelectNumberOf);
		sl_settingsPanel.putConstraint(SpringLayout.WEST, numOfWordsSpinner, 4, SpringLayout.EAST, lblSelectNumberOf);
		numOfWordsSpinner.setModel(new SpinnerNumberModel(10, 5, 15, 1));
		settingsPanel.add(numOfWordsSpinner);

		JLabel lblSelectLanguage = new JLabel("Select Language:");
		sl_settingsPanel.putConstraint(SpringLayout.NORTH, lblSelectLanguage, 32, SpringLayout.SOUTH, lblSelectNumberOf);
		sl_settingsPanel.putConstraint(SpringLayout.WEST, lblSelectLanguage, 10, SpringLayout.WEST, settingsPanel);
		settingsPanel.add(lblSelectLanguage);

		languageComboBox = new JComboBox<String>();
		sl_settingsPanel.putConstraint(SpringLayout.NORTH, languageComboBox, -5, SpringLayout.NORTH, lblSelectLanguage);
		sl_settingsPanel.putConstraint(SpringLayout.EAST, languageComboBox, 0, SpringLayout.EAST, lengthOfWordsSpinner);
		languageComboBox.setModel(new DefaultComboBoxModel(Config.languages.values()));
		settingsPanel.add(languageComboBox);

		progressBar = new JProgressBar();
		progressBar.setMaximum((int)numOfWordsSpinner.getValue());
		sl_settingsPanel.putConstraint(SpringLayout.WEST, progressBar, 39, SpringLayout.WEST, settingsPanel);
		settingsPanel.add(progressBar);

		lblWordsFound = new JLabel("Words Found: (0/" + numOfWordsSpinner.getValue() + ")");
		sl_settingsPanel.putConstraint(SpringLayout.SOUTH, lblWordsFound, -35, SpringLayout.SOUTH, settingsPanel);
		sl_settingsPanel.putConstraint(SpringLayout.NORTH, progressBar, 6, SpringLayout.SOUTH, lblWordsFound);
		sl_settingsPanel.putConstraint(SpringLayout.WEST, lblWordsFound, 51, SpringLayout.WEST, settingsPanel);
		settingsPanel.add(lblWordsFound);
		frame.getContentPane().add(btnStartNewGame);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JCheckBoxMenuItem chckbxmntmUserMode = new JCheckBoxMenuItem("User Mode");
		mnFile.add(chckbxmntmUserMode);

		JCheckBoxMenuItem chckbxmntmAdminMode = new JCheckBoxMenuItem("Admin Mode");
		mnFile.add(chckbxmntmAdminMode);

		JMenuItem mntmNewGame = new JMenuItem("New Game");
		mnFile.add(mntmNewGame);

		JMenuItem mntmExitGame = new JMenuItem("Exit Game");
		mnFile.add(mntmExitGame);



	}
	public void addAnswerBlocks(JPanel answerPanel){
		answerTiles = new ArrayList<>();
		for (int i = 0; i < newGame.getWordLength(); i++) {
			AnswerTile answerTile = new AnswerTile("",i);
			answerTiles.add(answerTile);
			answerPanel.add(answerTile);
		}
	}

}