package paw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import core.Game;
import core.SpringUtility;

public class AdminPlayPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private PAWgui internalgui;
	private static Game currentGame;
	private JPanel gridPanel;
	private JPanel answerPanel;
	private JPanel titlePanel;
	private JPanel wordListPanel;
	private JPanel buttonPanel;
	private JPanel buttPanel;
	private Font font;
	private int clickCount = 0;
	private List<GridTile> gridTiles = new ArrayList<>();
	private List<AnswerTile> answerTiles = new ArrayList<>();
	private String[] guessWord;
	private GameTracker tracker;
	private JEditorPane numWords;
	private JEditorPane words;

	ArrayList<String> foundWordList = new ArrayList<String>();
	
	public AdminPlayPanel(Color color, PAWgui paw){
		
		this.internalgui = paw;
		font = internalgui.getFont();
		setMinimumSize(new Dimension(1000,550));
		setBackground(color);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout());
		initialize();
	}
	
	public void initialize(){

		//if newGame == null the panels will generate empty 
		generateWordListPanel();
		generateGridPanel();
		generateButtonPanel();
		
		setVisible(true);
	}
	/**
	 * generates the panel to display words that have been guessed correctly
	 */
	public void generateWordListPanel(){
		wordListPanel = new JPanel();
		wordListPanel.setPreferredSize(new Dimension(200, 300));
		wordListPanel.setLayout(new BorderLayout());

		numWords = new JEditorPane();
		numWords.setFont(font);
		numWords.setBackground(Color.yellow);
		if(currentGame != null){
			numWords.setText("Number of Words = " + String.valueOf(currentGame.getNumberWords())
				+ "\n You Have Found = " + foundWordList.size());
		}else{
			numWords.setText("Number of Words = 0 \n You Have Found = 0");
		}
		wordListPanel.add(numWords, BorderLayout.NORTH);

		words = new JEditorPane();
		words.setFont(font);
		words.setBackground(Color.yellow);
		String foundList = "";
		if(foundWordList != null){
			for(int i = 0; i < foundWordList.size(); i++){
				foundList += foundWordList.get(i) + "\n";
			}
			words.setText(foundList);
		}
		wordListPanel.add(words, BorderLayout.CENTER);
		
		add(wordListPanel, BorderLayout.WEST);
	}
	/**
	 * Creates the panel to display the columns
	 * includes the answer row
	 */
	public void generateGridPanel(){
		gridPanel =  new JPanel();
		gridPanel.setLayout(new BorderLayout());
		gridPanel.setBorder(new LineBorder(Color.black, 2));
		
		titlePanel = new JPanel();
		if (currentGame != null) {
			guessWord = new String[currentGame.getWordLength()];
			tracker = new GameTracker(currentGame);
			JLabel titleLabel = new JLabel(currentGame.getTitle()
					+ " - (Duplicates = " + currentGame.getDuplicate() + ")"
					+ " - (In Order = " + currentGame.getCharOrder() + ")");
			titleLabel.setFont(font);
			titlePanel.add(titleLabel);
		}else{
			JLabel titleLabel = new JLabel(" No Game Selected ");
			titleLabel.setFont(font);
			titlePanel.add(titleLabel);
		}
		gridPanel.add(titlePanel, BorderLayout.NORTH);
		
		JPanel columnPanel = new JPanel();
		columnPanel.setLayout(new GridLayout(1, 0, 5, 0));
		columnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));

		ArrayList<ArrayList<String>> columnData = new ArrayList<ArrayList<String>>();
		if(currentGame!= null){
			columnData = currentGame.getColumnData();
		
			for(int i = 0; i < columnData.size(); i++){
				ArrayList<String> characters = columnData.get(i);
				JPanel column = new JPanel(new GridLayout(characters.size(), 1, 0, 5));
				for (int j = 0; j < characters.size(); j++) {
					GridTile newTile = new GridTile(characters.get(j), j);
					newTile.columnNum = i;
					if (newTile.getText().equals(" ")) {
						newTile.setVisible(false);
					}
					if (currentGame.getCharOrder()) {
						newTile.addMouseListener(new DropClick());
					}
					column.add(newTile);
					gridTiles.add(newTile);
					// TODO add in the dragndrop features to each tile...
				}
				columnPanel.add(column);
			}
		}
		gridPanel.add(columnPanel, BorderLayout.CENTER);

		JPanel instructPanel = new JPanel();
		JLabel ansLabel = new JLabel("Click tile to quess word.");
		ansLabel.setFont(font);
		instructPanel.add(ansLabel);

		answerPanel = new JPanel(new BorderLayout());
		answerPanel.add(instructPanel, BorderLayout.NORTH);
		if (currentGame != null) {
			JPanel guessPanel = new JPanel(new GridLayout(1, 0, 5, 0));
			guessPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			for (int i = 0; i < columnData.size(); i++) {
				AnswerTile newTile = new AnswerTile(" ", i);
				if (currentGame.getCharOrder()) {
					newTile.addMouseListener(new DropClick());
				}
				newTile.setVisible(true);
				guessPanel.add(newTile);
				answerTiles.add(newTile);
			}
			answerPanel.add(guessPanel, BorderLayout.CENTER);
		}
		
//		JButton guessBtn = new JButton("Guess");
//		guessBtn.setFont(font);
//		guessBtn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					checkGuess();
//					generateWordListPanel();
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}
//		});
//		answerPanel.add(guessBtn, BorderLayout.SOUTH);
		
		gridPanel.add(answerPanel, BorderLayout.SOUTH);
		add(gridPanel, BorderLayout.CENTER);
	}
	

	/**
	 * gather tiles and form a word
	 * check the word against newGame.getWordList()
	 * if correct add the word to foundWordList and remove from original word list
	 * unclear how the main play grid will react...??
	 */
	public void checkGuess(){
		//TODO write guess button functionality
		
	}
		

	/**
	 * generates a panel to hold action buttons
	 */
	public void generateButtonPanel(){
		buttPanel = new JPanel();
		buttPanel.setBackground(Color.yellow);
		buttonPanel = new JPanel(new SpringLayout());
		buttonPanel.setBackground(Color.yellow);
		
		// generates the HTML
		JButton createHTMLBtn = new JButton("Create\nHTML");
		createHTMLBtn.setFont(font);
		createHTMLBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
//					CreateHTML ch = new CreateHTML(currentGame);
					//TODO ?? add a popup to confirm the file was written
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(createHTMLBtn);
		
		//calls gameSaver and displays saved message
		JButton saveGameBtn = new JButton("Save\nGame");
		saveGameBtn.setFont(font);
		saveGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					GameSaver gs = new GameSaver(currentGame);
					//TODO ?? add a popup to confirm the file was written
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(saveGameBtn);
		SpringUtility.makeGrid(buttonPanel, 2, 1, 25, 15, 15, 15);
		buttPanel.add(buttonPanel);
		add(buttPanel, BorderLayout.EAST);
		
	}
	
	/**
	 * sets the newGame for the admin in Play Panel
	 * @param GameGenerator
	 */
	public static void setCurrentGame(Game newGame){
		currentGame = newGame;
	}

	/**
	 * This defines the basic tile used to hold the logical characters in the game
	 *
	 */
	class Tile extends JButton {
		private static final long serialVersionUID = 1L;
		int clickedPosition = -1;
		int tileId = -1;
		
		Tile(){
			setFont(font);
		}

	}
	
	/**
	 * extends tile to further define the tiles in the game
	 * sets text to the tile and colors
	 *
	 */
	class GridTile extends Tile {
		private static final long serialVersionUID = 1L;
		int clickedPosition = -1;
		int tileId = -1;
		int columnNum;
		Color pressedColor = Color.WHITE;
		String character;

		GridTile(String character, int iD) {
			super();
			this.character = character;
			setText(character);
			setBackground(Color.yellow);
			tileId = iD;
			setMargin(new Insets(5, 5, 5, 5));
		}

	}

	class AnswerTile extends Tile {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/** The tile id. */
		int tileId = -1;

		/** The character. */
		String character = "";

		/** The color. */
		Color color = Color.YELLOW;

		/**
		 * Instantiates a new answer tile.
		 * @param character, the character
		 * @param iD, the i d
		 */
		AnswerTile(String character, int iD) {
			super();
			setText(character);
			tileId = iD;
			setBackground(color);
			setVisible(false);
		}

	}

	class DropClick implements MouseListener {
		long timer = 0;

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			timer = System.currentTimeMillis();

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			if (System.currentTimeMillis() - timer < 150) {
				if (arg0.getSource() instanceof GridTile) {
					GridTile pressedButton = (GridTile) arg0.getSource();

					for (GridTile gridTile : gridTiles) {
						if (gridTile.columnNum == pressedButton.columnNum
								&& gridTile.clickedPosition > -1
								&& gridTile.tileId != pressedButton.tileId) {
							return;
						}
					}
					if (clickCount == currentGame.getWordLength()) {
						if (pressedButton.clickedPosition < 0) {
							return;
						}
					}

					if (pressedButton.clickedPosition == -1) {
						pressedButton.clickedPosition = clickCount++;
						AnswerTile at = answerTiles
								.get(pressedButton.columnNum);
						at.character = pressedButton.character;
						at.setText(pressedButton.character);
						at.setVisible(true);
						at.getParent().revalidate();
						pressedButton.setBackground(Color.WHITE);
						pressedButton.getParent().revalidate();
						guessWord[pressedButton.columnNum] = pressedButton.character;
					} else {
						AnswerTile nt = answerTiles
								.get(pressedButton.columnNum);
						if (nt.getBackground().equals(Color.RED)) {
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.YELLOW);
							}
						}
						nt.setText(" ");
						nt.getParent().revalidate();
						pressedButton.setBackground(Color.YELLOW);
						pressedButton.setSelected(false);
						pressedButton.getParent().revalidate();
						clickCount--;
						guessWord[pressedButton.columnNum] = "";
						for (GridTile gridTile : gridTiles) {
							if (gridTile.clickedPosition > pressedButton.clickedPosition) {
								gridTile.clickedPosition -= 1;
							}
						}
						pressedButton.clickedPosition = -1;
					}
					if (clickCount == currentGame.getWordLength()) {
						if (tracker.isWordInTheList(guessWord)) {
							String foundWord = "";
							for (String string : guessWord) {
								foundWord += string;
							}
							foundWordList.add(foundWord);
							numWords.setText("Number of Words = "
									+ String.valueOf(currentGame.getNumberWords())
									+ "\n You Have Found = "
									+ foundWordList.size());
							String foundList = "";
							for (String string : foundWordList) {
								foundList += string + "\n";
							}
							words.setText(foundList);
							wordListPanel.revalidate();
							Timer timer = new Timer();
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.GREEN);
							}
							long pause = 0;
							for (GridTile gridTile : gridTiles) {
								
								if (gridTile.clickedPosition > -1) {
									pause += 500;
									timer.schedule(new TimerTask() {

										@Override
										public void run() {
											gridTile.setVisible(false);
											gridTile.clickedPosition = -1;
										}
									}, pause);

								}
							}

							clickCount = 0;
							guessWord = new String[currentGame.getWordLength()];
							for (AnswerTile answerTile : answerTiles) {
								timer.schedule(new TimerTask() {

									@Override
									public void run() {
										answerTile.setText(" ");
										answerTile.setBackground(Color.YELLOW);
									}
								}, pause);
								pause -= 500;
									
							}
						} else {
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.RED);
							}
						}

					}
				}
				if (arg0.getSource() instanceof AnswerTile) {
					AnswerTile pressedButton = (AnswerTile) arg0.getSource();
					if (pressedButton.getText().equals(" ")) {
						return;
					}
					if (pressedButton.getBackground().equals(Color.RED)) {
						for (AnswerTile answerTile : answerTiles) {
							answerTile.setBackground(Color.YELLOW);
						}
					}
					guessWord[pressedButton.tileId] = "";
					pressedButton.setText(" ");
					pressedButton.getParent().revalidate();
					clickCount--;
					for (GridTile gridTile : gridTiles) {
						if (gridTile.columnNum == pressedButton.tileId) {
							gridTile.clickedPosition = -1;
							gridTile.setBackground(Color.YELLOW);
							gridTile.setSelected(false);
							gridTile.getParent().revalidate();
						}
					}

				}
			}
		}

	}
}
