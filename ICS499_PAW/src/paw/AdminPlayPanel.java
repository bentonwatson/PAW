package paw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;



import core.Game;
import core.SpringUtility;
import core.WordProcessor;

public class AdminPlayPanel extends JPanel implements MouseListener{
	
	private static final long serialVersionUID = 1L;
	private PAWgui internalgui;
	private Game currentGame;
	private JPanel gridPanel;
	private JPanel columnPanel;
	private JPanel answerPanel;
	private JPanel titlePanel;
	private JPanel wordListPanel;
	private JPanel buttonPanel;
	private JPanel buttPanel;
	private JLabel message;
	private Font font;
	private int clickCount = 0;
	private List<GridTile> gridTiles = new ArrayList<>();
	private List<AnswerTile> answerTiles = new ArrayList<>();
	private String[] guessWord;
	private GameTracker tracker;
	private JEditorPane numWords;
	private JEditorPane words;
	
	ArrayList<String> notFoundWords = new ArrayList<String>();
	ArrayList<String> foundWordList = new ArrayList<String>();
	
	public AdminPlayPanel(Color color, PAWgui paw){
		
		this.internalgui = paw;
		font = internalgui.getFont();
//		gameCollection = internalgui.getGameCollection();
//		userGameLevel = internalgui.getUserGameLevel();
		if(internalgui.getCurrentGame() != null){
			currentGame = internalgui.getCurrentGame();
			tracker = new GameTracker(currentGame);
		}
		setMinimumSize(new Dimension(1000,550));
		setBackground(color);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout());
		
		addMouseListener(this);
		
		generateButtonPanel();

		initialize();
	}
	
	public void initialize(){

		//if newGame == null the panels will generate empty 
		generateWordListPanel();
		generateGridPanel();
		
		setVisible(true);
	}
	/**
	 * generates the panel to display words that have been guessed correctly
	 */
	public void generateWordListPanel(){
		wordListPanel = new JPanel();
		wordListPanel.addMouseListener(this);
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
		
		columnPanel = new JPanel();

		columnPanel.setLayout(new GridLayout(1, 10, 10, 0));
		columnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));

		ArrayList<ArrayList<String>> columnData = new ArrayList<ArrayList<String>>();
		if(currentGame!= null){
			guessWord = new String[currentGame.getNumberColumns()];
			columnData = currentGame.getColumnData();
		
			for(int i = 0; i < columnData.size(); i++){
				ArrayList<String> characters = columnData.get(i);
				JPanel column = new JPanel(new GridLayout(characters.size(), 1, 0, 5));
				for(int j = 0; j < characters.size(); j++){
					if(!currentGame.getDuplicate()){
						String tmp = characters.get(j);
						if(tmp.length() > 1){
							WordProcessor wp = new WordProcessor(tmp);
							ArrayList<String> ch = wp.getLogicalChars();
							int count = Integer.valueOf(ch.get(1));
							GridTile newTile = new GridTile(wp.logicalCharAt(0), j);
							newTile.columnNum = i;
							newTile.repeat = count;
							newTile.addMouseListener(this);
							column.add(newTile);
							gridTiles.add(newTile);
						}else{
							GridTile newTile = new GridTile("", j);
							newTile.columnNum = i;
							newTile.repeat = 1;
							newTile.setVisible(false);
							newTile.addMouseListener(this);
							column.add(newTile);
							gridTiles.add(newTile);
						}
					}else{
						GridTile newTile = new GridTile(characters.get(j), j);
						newTile.columnNum = i;
						newTile.repeat = 1;
						newTile.addMouseListener(this);
						column.add(newTile);
						gridTiles.add(newTile);
					}
				}
				columnPanel.add(column);
			}
			columnPanel.addMouseListener(this);
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
			guessPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
			for (int i = 0; i < columnData.size(); i++) {
				AnswerTile newTile = new AnswerTile(" ", i);
				newTile.addMouseListener(this);
				
				newTile.setVisible(true);
				guessPanel.add(newTile);
				answerTiles.add(newTile);
			}
			answerPanel.add(guessPanel, BorderLayout.CENTER);
		}
		message = new JLabel();
		message.setFont(font);
		message.setText("");
		answerPanel.add(message, BorderLayout.SOUTH);
		gridPanel.add(answerPanel, BorderLayout.SOUTH);
	
		add(gridPanel, BorderLayout.CENTER);
	}
	public void endGame()
	{
		int answer = JOptionPane.showConfirmDialog(null,
				"Game completed, would you like to Save Game?", "Game Complete",
				JOptionPane.OK_OPTION);
		switch (answer)
		{
		case JOptionPane.OK_OPTION:
			try {
				new GameSaver(currentGame);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			break;
		case JOptionPane.NO_OPTION:
			break;
		}// end switch

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
//					new CreateHTML(currentGame);
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
					new GameSaver(currentGame);
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
	public void setCurrentGame(Game game){
		currentGame = game;
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
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
			if (pressedButton.clickedPosition == -1 && currentGame.getCharOrder()) {
				
				pressedButton.clickedPosition += 1;
				clickCount++;
				AnswerTile at = answerTiles.get(pressedButton.columnNum);
				at.character = pressedButton.character;
				at.setText(pressedButton.character);
				at.setVisible(true);
				pressedButton.setBackground(Color.YELLOW);
				guessWord[pressedButton.columnNum] = pressedButton.character;
				
			}else if(pressedButton.clickedPosition == -1 && !currentGame.getCharOrder()){
				
				pressedButton.clickedPosition += 1;
				AnswerTile at = answerTiles.get(clickCount);
				at.character = pressedButton.character;
				at.tileId = pressedButton.columnNum;
				at.clickOrder = clickCount;
				at.setText(pressedButton.character);
				at.setVisible(true);
				pressedButton.setBackground(Color.YELLOW);
				guessWord[clickCount] = pressedButton.character;
				clickCount++;
				
			}else if (!currentGame.getCharOrder()){
				
				for(AnswerTile ans : answerTiles){
					if(ans.tileId == pressedButton.columnNum){
						ans.setText(" ");
						guessWord[ans.clickOrder] = "";
						pressedButton.setVisible(true);
						pressedButton.clickedPosition = -1;
						
						break;
					}
				}
				AnswerTile nt = answerTiles.get(pressedButton.columnNum);
				if (nt.getBackground().equals(Color.RED)) {
					for (AnswerTile answerTile : answerTiles) {
						answerTile.setSelected(false);
						answerTile.setBackground(Color.YELLOW);
					}
				}
				clickCount--;
				for (GridTile gridTile : gridTiles) {
					if (gridTile.clickedPosition > pressedButton.clickedPosition) {
						gridTile.clickedPosition -= 1;
					}
				}
				
			}else{
				AnswerTile nt = answerTiles.get(pressedButton.columnNum);
				if (nt.getBackground().equals(Color.RED)) {
					for (AnswerTile answerTile : answerTiles) {
						answerTile.setSelected(false);
						answerTile.setBackground(Color.YELLOW);
						pressedButton.setSelected(false);
					}
				}
				nt.setText(" ");
//				pressedButton.setVisible(true);
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
					String found = "";
					for(String s: guessWord){
						found += s;
					}
					foundWordList.add(found);
					
					java.util.Timer timer = new java.util.Timer();
					for (AnswerTile answerTile : answerTiles) {
						answerTile.setSelected(false);
						answerTile.setBackground(Color.GREEN);
					}
					long pause = 500;
					for (GridTile gridTile : gridTiles) {
						if (gridTile.clickedPosition > -1) {
							pause += 500; // creates a bug
							timer.schedule(new TimerTask() {
								@Override
								public void run() {
									if (currentGame.getDuplicate()) {
										gridTile.setVisible(false);
										
									} else {
										//i want to know if there are any more of this letter needed
										if(gridTile.repeat > 1){
											gridTile.setSelected(false);
											gridTile.setBackground(Color.YELLOW);
											gridTile.clickedPosition = -1;
											gridTile.setSelected(false);
											gridTile.repeat -= 1;
										}else{
											gridTile.setVisible(false);
										}
									}
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
								answerTile.setSelected(false);
								answerTile.setBackground(Color.YELLOW);
							}
						}, pause);
						pause -= 500; // creates a bug
					}
					generateWordListPanel();
					if(foundWordList.size() == currentGame.getNumberWords()){
						endGame();
					}
				} else {
					for (AnswerTile answerTile : answerTiles) {
						answerTile.setSelected(false);
						answerTile.setBackground(Color.RED);
						message.setText("Incorrect Keep Trying!");
					}
				}

			}
		}
		if (arg0.getSource() instanceof AnswerTile) {
			AnswerTile pressedButton = (AnswerTile) arg0.getSource();
			if (pressedButton.getText().equals(" ")) {
				pressedButton.setSelected(false);
				return;
			}
			if (pressedButton.getBackground().equals(Color.RED)) {
				for (AnswerTile answerTile : answerTiles) {
					answerTile.setSelected(false);
					answerTile.setBackground(Color.YELLOW);
					message.setText("");
				}
			}
			guessWord[pressedButton.tileId] = "";
			pressedButton.setText(" ");
			pressedButton.setSelected(false);
			clickCount--;
			for (GridTile gridTile : gridTiles) {
				if (gridTile.columnNum == pressedButton.tileId) {
					gridTile.clickedPosition = -1;
					gridTile.setSelected(false);
					gridTile.setBackground(Color.YELLOW);
				}
			}

		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	/**
	 * This defines the basic tile used to hold the logical characters in the game
	 *
	 */
	class Tile extends JToggleButton {
		private static final long serialVersionUID = 1L;
		int clickedPosition = -1;
		int tileId = -1;

		Tile() {
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
		int repeat;
		Color pressedColor = Color.WHITE;
		String character;

		GridTile(String character, int iD) {
			super();
			this.character = character;
			setText(character);
			setBackground(Color.yellow);
			tileId = iD;
		}

	}

	class AnswerTile extends Tile {

		private static final long serialVersionUID = 1L;
		int tileId = -1;
		int clickOrder;
		String character = "";
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

	public static void errorMessage(String a_string) {

		JOptionPane.showMessageDialog(null, a_string, "Error",
				JOptionPane.ERROR_MESSAGE);
		System.exit(0);

	}
	
}
