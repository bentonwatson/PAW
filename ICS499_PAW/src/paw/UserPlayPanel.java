package paw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import core.Game;
import core.GameCollection;
import core.SpringUtility;
import core.WordProcessor;

public class UserPlayPanel extends JPanel implements MouseListener{
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
	private Font font;
	private Color bgColor;
	private Color tileColor;
	
	private int userGameLevel;
	private String userGameLevelByName;
	private JComboBox<String> levelComboBox;
	private JLabel levelComboLabel;
	
	private JLabel jlbTime;
	private JPanel timerPanel;
	private final ClockListener cl = new ClockListener();
	private final Timer time = new Timer(1000, cl);
	private final JLabel jlbTimer = new JLabel("00:00:00");
	private GameCollection gameCollection;
	
	private int clickCount = 0;
	private List<GridTile> gridTiles = new ArrayList<>();
	private List<AnswerTile> answerTiles = new ArrayList<>();
	private ArrayList<String> guessWord;
	private GameTracker tracker;
	private JEditorPane numWords;
	private JEditorPane words;
	private GameCollection currentCollection;	
	private ProgressTracker progress;
	private String language;
	
	ArrayList<String> notFoundWords = new ArrayList<String>();
	ArrayList<String> foundWordList = new ArrayList<String>();
	
	public UserPlayPanel(Color color, PAWgui paw){
		bgColor = color;
		tileColor = Config.PLAY_TILE_COLOR;
		this.internalgui = paw;
		font = internalgui.getFont();
		language = Config.DEFAULTLANGUAGE;
		if(language.equals("en")){
			if(new File(Config.EN_GAME_SET).exists() && new File(Config.EN_GAME_SET).isFile()){
				gameCollection = new GameCollection(Config.EN_GAME_SET);
			}else{
				PAWgui.errorMessageClose("A Game Collection does not exist! Contact Administrator!");
			}
		}else{
			if(new File(Config.TE_GAME_SET).exists() && new File(Config.TE_GAME_SET).isFile()){
				gameCollection = new GameCollection(Config.TE_GAME_SET);
			}else{
				PAWgui.errorMessageClose("A Game Collection does not exist! Contact Administrator!");
			}
		}
		
		setUserGameLevel(internalgui.getUserGameLevel());
		
		setMinimumSize(new Dimension(1000,550));
		setBackground(bgColor);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout());
		
		addMouseListener(this);
		progress = new ProgressTracker();
		setVisible(true);
		initialize();
	}
	
	public void initialize(){
		if(internalgui.getPlayRandom()){
			currentGame = gameCollection.getRandomGame();
			tracker = new GameTracker(currentGame);
			generateButtonPanel();
			generateWordListPanel();
			generateGridPanel();
		}else if(!progress.allGameSetPlayed()){
				
			currentCollection = gameCollection.getGameCollectionByLevel(userGameLevel);
			if(currentCollection.size() > 0){
				currentCollection = progress.removeCompletedGamesFromGameCollection(currentCollection);
				 if(currentCollection.size() > 0){
						setCurrentGame(currentCollection.getGame(0));
						tracker = new GameTracker(currentGame);
						generateButtonPanel();
						generateWordListPanel();
						generateGridPanel();
	
						time.start();
				 }else if(progress.levelsNotCompleted().size() > 0){
						int answer = JOptionPane.showConfirmDialog(null,
								"You have played all games for level " + userGameLevel +"! "
										+ "\n Contact Administrator for more games. "
										+ "\n Select YES to play an incomplete level."
										+ "\n Select NO to play a random game from any level.",
										"Movin' On UP 1!",
										JOptionPane.OK_OPTION);
							switch (answer)
							{
							case JOptionPane.OK_OPTION:
									setUserGameLevel(progress.levelsNotCompleted().get(0));
									internalgui.setPlayRandom(false);
									internalgui.initialize();
									internalgui.selectTabbedPaneIndex(1);
									break;
							case JOptionPane.NO_OPTION:
									internalgui.setPlayRandom(true);
									internalgui.initialize();
									internalgui.selectTabbedPaneIndex(1);
									break;
							}// end switch
				}
			}else {
				int answer = JOptionPane.showConfirmDialog(null,
					"You have played all games for level " + userGameLevel +"! "
					+ "\n Contact Administrator for more games. "
					+ "\n Select YES to play an incomplete level."
					+ "\n Select NO to play a random game from any level.",
					"Movin' On UP 2!",
					JOptionPane.OK_OPTION);
					switch (answer)
					{
					case JOptionPane.OK_OPTION:
							setUserGameLevel(progress.levelsNotCompleted().get(0));
							internalgui.setPlayRandom(false);
							internalgui.initialize();
							internalgui.selectTabbedPaneIndex(1);
							break;
					case JOptionPane.NO_OPTION:
							internalgui.setPlayRandom(true);
							internalgui.initialize();
							internalgui.selectTabbedPaneIndex(1);
							break;
					}// end switch
			}
		} else {
			int answer = JOptionPane.showConfirmDialog(null,
					"You have played all games in the set. "
					+ "\nContact your administrator to produce more. "
					+ "\nYES to play random game."
					+ "\nNO to exit game.", "Game Set Completed!",
					JOptionPane.OK_OPTION);
			switch (answer)
			{
			case JOptionPane.OK_OPTION:
					setUserGameLevel(1);
					internalgui.setPlayRandom(true);
					internalgui.initialize();
					internalgui.selectTabbedPaneIndex(1);
					break;
			case JOptionPane.NO_OPTION:
					System.exit(0);
					break;
				}// end switch
		}
		
		
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
		numWords.setEditable(false);
		numWords.setFont(font);
		numWords.setBackground(bgColor);
		
		if(currentGame != null){
			numWords.setText("Number of Words = " + String.valueOf(currentGame.getNumberWords())
					+ "\n You Have Found = " + foundWordList.size());
		}else{
			numWords.setText("Number of Words = 0 \n You Have Found = 0");
		}
		wordListPanel.add(numWords, BorderLayout.NORTH);
		
		words = new JEditorPane();
		words.setEditable(false);
		words.setFont(font);
		words.setBackground(bgColor);
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
			JLabel titleLabel = new JLabel(currentGame.getTitle());
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
//			guessWord = new String[currentGame.getNumberColumns()];
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
		JLabel ansLabel = new JLabel("Click or Drag tile to quess word.");
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
		gridPanel.add(answerPanel, BorderLayout.SOUTH);
	
		add(gridPanel, BorderLayout.CENTER);
	}
	
	public void setUserGameLevel(int level){
		internalgui.setUserGameLevel(level);
		userGameLevel = level;
	}
	
	/**
	 * sets the currentGame for the User in Play Panel
	 * @param Game
	 */
	public void setCurrentGame(Game game){
		currentGame = game;
		
	}
	public void endGame()
	{
		time.stop();
		String optionMsg;
		if(!internalgui.getPlayRandom()){
			progress.setCurrentGameId(currentGame.getId());
			progress.setPlayDate( new Date());
			progress.setTimeElapsed(cl.toString());
			progress.setCompleted(tracker.isGameComplete());
			progress.writeToFile();
			optionMsg = tracker.getGameStatusMsg();
		}else{
			optionMsg = "YES to play another random game?"
					+ "\nNO to exit game.";
		}
		foundWordList.clear();
		int answer = JOptionPane.showConfirmDialog(null,
				optionMsg, "Awesome!!!",
				JOptionPane.YES_NO_OPTION);
		switch (answer)
		{
		case JOptionPane.OK_OPTION:
			internalgui.initialize();
			internalgui.selectTabbedPaneIndex(1);
			break;
		case JOptionPane.NO_OPTION:
			System.exit(0);
			break;
		}// end switch

	}
	
	/**
	 * generates a panel to hold action buttons
	 */
	public void generateButtonPanel(){
		buttPanel = new JPanel();
		buttPanel.setLayout(new BorderLayout());
		buttPanel.setBackground(bgColor);
		buttonPanel = new JPanel(new SpringLayout());
		buttonPanel.setBackground(bgColor);
		
		if(currentGame != null){
			JEditorPane gameDetails = new JEditorPane();
			gameDetails.setEditable(false);
			gameDetails.setBackground(bgColor);
			gameDetails.setFont(font);
			String dup;
			String ord;
			
			if(currentGame.getDuplicate()){
				dup = "Duplicates Allowed";
			}else{
				dup = "No Duplicates";
			}
			if(currentGame.getCharOrder()){
				ord = "In Logical Order";
			}else{
				ord = "Out Logical Order";
			}
			gameDetails.setText("Current Game Details \n" 
					+ "Level - " + currentGame.getLevel() +"\n"
					+ dup +"\n" 
					+ ord);
			
			buttPanel.add(gameDetails, BorderLayout.NORTH);
		}
		
		// Level
		levelComboLabel = new JLabel("Level");
		levelComboLabel.setFont(font);
		levelComboBox = new JComboBox<String>();
		String [] levelByName = {"Easy", "Medium", "Hard", "Impossible"};
		for(int i = 0; i < 4; i++){
			levelComboBox.addItem(levelByName[i]);
		}
		levelComboBox.setFont(font);
		if(currentGame != null){
			userGameLevel = currentGame.getLevel();
			userGameLevelByName = levelByName[currentGame.getLevel() - 1];
		}else{
			userGameLevelByName = levelByName[userGameLevel - 1];
		}
		userGameLevelByName = levelByName[userGameLevel - 1];
		levelComboBox.setSelectedItem(userGameLevelByName);
		levelComboBox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				userGameLevelByName = String.valueOf(levelComboBox.getSelectedItem());
				if(userGameLevelByName.equals("Easy")){
					setUserGameLevel(1);
				}
				if(userGameLevelByName.equals("Medium")){
					setUserGameLevel(2);
				}
				if(userGameLevelByName.equals("Hard")){
					setUserGameLevel(3);
				}
				if(userGameLevelByName.equals("Impossible")){
					setUserGameLevel(4);
				}
			}
		});
		
		buttonPanel.add(levelComboLabel);
		buttonPanel.add(levelComboBox);
		
		// calls for a new game using the userGameLevel variable
		JButton newGameBtn = new JButton("New Game");
		newGameBtn.setFont(font);
		newGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					time.stop();
					internalgui.setPlayRandom(false);
					internalgui.initialize();
					internalgui.selectTabbedPaneIndex(1);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(newGameBtn);
		
		timerPanel = new JPanel();
		jlbTime = new JLabel("time");
		jlbTime.setForeground(Color.black);
		jlbTime.setFont(new Font("Serif", Font.BOLD, 36));
		jlbTimer.setFont(new Font("Serif", Font.BOLD, 36));
		jlbTimer.setForeground(Color.black);
		timerPanel.add(jlbTime);
		timerPanel.add(jlbTimer);
		buttonPanel.add(timerPanel);
		timerPanel.setBounds(0, 330, 300, 70);
		time.start();
		
		//clears the guess tiles
		JButton clearGuessBtn = new JButton("Clear Guess");
		clearGuessBtn.setFont(font);
		clearGuessBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (AnswerTile answerTile : answerTiles) {
					answerTile.setText(" ");
					answerTile.setBackground(tileColor);
				}
				for (GridTile gridTile : gridTiles) {
					gridTile.clickedPosition = -1;
					gridTile.setSelected(false);
					gridTile.setBackground(tileColor);
				}
//				for (int i = 0; i < guessWord.length; i++) {
//					guessWord[i] = "";
//				}
				clickCount = 0;
			}
		});
		buttonPanel.add(clearGuessBtn);
		
		SpringUtility.makeGrid(buttonPanel, 5, 1, 25, 15, 15, 15);
		
		buttPanel.add(buttonPanel, BorderLayout.SOUTH);
		add(buttPanel, BorderLayout.EAST);
	}
	
	private class ClockListener implements ActionListener
	{

		private int hours;
		private int minutes;
		private int seconds;
		private String hour;
		private String minute;
		private String second;
		private static final int N = 60;
		
		//PAW group added method toSring()
		public String toString(){
			return String.valueOf(hour + ":" + minute + ":" + second);
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			NumberFormat formatter = new DecimalFormat("00");
			if (seconds == N)
			{
				seconds = 00;
				minutes++;
			}

			if (minutes == N)
			{
				minutes = 00;
				hours++;
			}
			hour = formatter.format(hours);
			minute = formatter.format(minutes);
			second = formatter.format(seconds);
			jlbTimer.setText(String.valueOf(hour + ":" + minute + ":" + second));
			seconds++;
		}
	}
	long timer = 0;
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
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
				
				if (pressedButton.clickedPosition == -1 && currentGame.getCharOrder()) {
					
					pressedButton.clickedPosition += 1;
					pressedButton.setBackground(Color.WHITE);
					clickCount++;
					AnswerTile at = answerTiles.get(pressedButton.columnNum);
					at.character = pressedButton.character;
					at.setText(pressedButton.character);
					at.setVisible(true);
					
				}else if(pressedButton.clickedPosition == -1 && !currentGame.getCharOrder()){
					
					pressedButton.clickedPosition = clickCount;
					AnswerTile at = answerTiles.get(pressedButton.clickedPosition);
					if(!at.getText().equals(" ")){
						shiftTilesLeft(answerTiles, pressedButton.clickedPosition, gridTiles);
					}
					pressedButton.setBackground(Color.white);
					at.character = pressedButton.character;
					at.clickedPosition = pressedButton.clickedPosition;
					at.setText(pressedButton.character);
					at.setVisible(true);
					clickCount++;
					
				}else if (pressedButton.clickedPosition > -1 && !currentGame.getCharOrder()){
					//set
					for(AnswerTile ans : answerTiles){
						if(ans.getBackground().equals(Color.RED)){
							ans.setBackground(tileColor);
						}
					}
					shiftAnswerTilesLeft(answerTiles, pressedButton.clickedPosition, gridTiles);
					clickCount--;
					
				}else if(pressedButton.clickedPosition > -1 && currentGame.getCharOrder()){
					for(AnswerTile ans : answerTiles){
						if(ans.tileId == pressedButton.columnNum){
							ans.setText(" ");
							break;
						}
					}
					AnswerTile nt = answerTiles.get(pressedButton.columnNum);
					if (nt.getBackground().equals(Color.RED)) {
						for (AnswerTile answerTile : answerTiles) {
							answerTile.setBackground(tileColor);
						}
					}
					nt.setText(" ");
					pressedButton.setVisible(true);
					pressedButton.setBackground(tileColor);
					pressedButton.clickedPosition = -1;
					clickCount--;
				}
				if (clickCount == currentGame.getWordLength()) {
					if (pressedButton.clickedPosition < 0) {
						return;
					}
				}
				if (clickCount == currentGame.getWordLength()) {
					setGuessWord();
					
					String found = "";
					for(String s: guessWord){
						found += s;
					}
					if (tracker.isWordInTheList(guessWord) && !tracker.isCurrentWordAlreadyFound()) {
						tracker.setCurrentWordAsFound();
						foundWordList.add(found);
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
						java.util.Timer timer = new java.util.Timer();
						for (AnswerTile answerTile : answerTiles) {
							answerTile.setSelected(false);
							answerTile.setBackground(Color.GREEN);
						}
						long pause = 0;
						for (GridTile gridTile : gridTiles) {
							if (gridTile.clickedPosition > -1) {
//								pause += 500; // creates a bug
								timer.schedule(new TimerTask() {
									@Override
									public void run() {
										//i want to know if there are any more of this letter needed
										if(gridTile.repeat > 1){
											gridTile.setSelected(false);
											gridTile.setBackground(tileColor);
											gridTile.setSelected(false);
											gridTile.repeat -= 1;
										}else{
											gridTile.setVisible(false);
										}
										gridTile.clickedPosition = -1;
									}
								}, pause);
							}
						}
						clickCount = 0;
//						guessWord = new String[currentGame.getWordLength()];
						for (AnswerTile answerTile : answerTiles) {
							timer.schedule(new TimerTask() {
								@Override
								public void run() {
									answerTile.setText(" ");
									answerTile.setSelected(false);
									answerTile.setBackground(tileColor);
								}
							}, pause);
//							pause -= 500; // creates a bug
						}
						if(foundWordList.size() == currentGame.getNumberWords()){
							endGame();
						}
					} else {
						for (AnswerTile answerTile : answerTiles) {
							answerTile.setSelected(false);
							answerTile.setBackground(Color.RED);
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
						answerTile.setBackground(tileColor);
					}
				}
				if(currentGame.getCharOrder()){
					
					pressedButton.setText(" ");
					pressedButton.setSelected(false);
					for (GridTile gridTile : gridTiles) {
						if (gridTile.columnNum == pressedButton.tileId) {
							gridTile.clickedPosition = -1;
							gridTile.setSelected(false);
							gridTile.setBackground(tileColor);
						}
					}
				} else{
					shiftAnswerTilesLeft(answerTiles, pressedButton.clickedPosition, gridTiles);
				}
				clickCount--;
	
			}
		}
	}

	
	public boolean canShiftRight(List<AnswerTile> atList, int currentLocation) {
		for (int i = currentLocation; i < atList.size(); i++) {
			if (atList.get(i).getText().equals(" ")) {
				return true;
			}
		}
		return false;
	}

	public boolean canShiftLeft(List<AnswerTile> atList, int currentLocation) {
		for (int i = currentLocation; i > -1; i--) {
			if (atList.get(i).getText().equals(" ")) {
				return true;
			}
		}
		return false;
	}

	public int shiftTilesRight(List<AnswerTile> atList, int currentLocation,
			List<GridTile> gtList) {
		AnswerTile currentTile = atList.get(currentLocation);
		String currentChar = currentTile.getText();
		int currentCP = currentTile.clickedPosition;
		currentTile.setText(" ");
		currentTile.clickedPosition = -1;
		while (currentLocation < atList.size() - 1) {
			AnswerTile nextTile = atList.get(++currentLocation);
			String nextChar = nextTile.getText();
			int nextCP = nextTile.clickedPosition;
			nextTile.setText(currentChar);
			nextTile.clickedPosition = currentCP;
			currentCP = nextCP;
			currentChar = nextChar;
			if (currentChar.equals(" ")) {
				return currentLocation;
			}
		}
		return 0;
	}

	public int shiftTilesLeft(List<AnswerTile> atList, int currentLocation,
			List<GridTile> gtList) {
		AnswerTile currentTile = atList.get(currentLocation);
		String currentChar = currentTile.getText();
		int currentCP = currentTile.clickedPosition;
		currentTile.setText(" ");
		currentTile.clickedPosition = -1;
		while (currentLocation > 0) {
			AnswerTile nextTile = atList.get(--currentLocation);
			String nextChar = nextTile.getText();
			int nextCP = nextTile.clickedPosition;
			nextTile.setText(currentChar);
			nextTile.clickedPosition = currentCP;
			currentCP = nextCP;
			currentChar = nextChar;
			if (currentChar.equals(" ")) {
				return currentLocation;
			}
		}
		return 0;
	}

	public void shiftAnswerTilesLeft(List<AnswerTile> atList,
			int currentLocation, List<GridTile> gtList) {
		for (AnswerTile answerTile : atList) {
			if(answerTile.clickedPosition == currentLocation){
				currentLocation = answerTile.tileId;
				break;
			}
		}
		AnswerTile currentTile = atList.get(currentLocation);
		for (GridTile gridTile : gtList) {
			if (gridTile.clickedPosition == currentTile.clickedPosition) {
				gridTile.clickedPosition = -1;
				gridTile.setBackground(tileColor);
				gridTile.setSelected(false);
				gridTile.getParent().revalidate();
			} else if (gridTile.clickedPosition > currentTile.clickedPosition) {
				gridTile.clickedPosition--;
			}
		}
		for (AnswerTile answerTile : atList) {
			if (answerTile.clickedPosition > currentTile.clickedPosition) {
				answerTile.clickedPosition--;
			}
		}
		while (currentLocation < atList.size() - 1) {
			currentTile = atList.get(currentLocation);
			AnswerTile nextTile = atList.get(++currentLocation);
			currentTile.setText(nextTile.getText());
			currentTile.clickedPosition = nextTile.clickedPosition;
		}
		atList.get(currentLocation).setText(" ");
		atList.get(currentLocation).clickedPosition = -1;
	}

	public void shiftAnswerTilesRight(List<AnswerTile> atList,
			int currentLocation) {
		while (currentLocation > 0) {
			atList.get(currentLocation).setText(
					atList.get(--currentLocation).getText());
		}
		atList.get(currentLocation).setText(" ");
	}
	public void setGuessWord(){
		guessWord = new ArrayList<String>();
		for (int i = 0; i < answerTiles.size(); i++) {
			guessWord.add(answerTiles.get(i).getText());
 		}
	}
	
	/**
	 * This defines the basic tile used to hold the logical characters in the game
	 *
	 */
	class Tile extends JButton implements DragGestureListener, 
		DragSourceListener, MouseListener{
		private static final long serialVersionUID = 1L;
		int clickedPosition = -1;
		int tileId = -1;
		DragSource ds = new DragSource();
		long timer = 0;
		
		Tile() {
			setFont(font);
			ds.createDefaultDragGestureRecognizer(this,
					DnDConstants.ACTION_COPY_OR_MOVE, this);
			
		}

		@Override
		public void dragDropEnd(DragSourceDropEvent arg0) {
			if (this instanceof GridTile) {
				GridTile pressedButton = (GridTile) this;
				if (!arg0.getDropSuccess()) {
				} else if (arg0.getDropSuccess()) {
					if (pressedButton.clickedPosition == -1) {
						pressedButton.setBackground(Color.WHITE);
						pressedButton.getParent().revalidate();
						clickCount++;
						pressedButton.clickedPosition = clickCount - 1;
					}
					if (clickCount == currentGame.getWordLength()) {
						setGuessWord();
						String found = "";
						for(String s: guessWord){
							found += s;
						}
						if (tracker.isWordInTheList(guessWord) && !tracker.isCurrentWordAlreadyFound()) {
							tracker.setCurrentWordAsFound();
							foundWordList.add(found);
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
							java.util.Timer timer = new java.util.Timer();
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.GREEN);
							}
							long pause = 0;

							for (GridTile gridTile : gridTiles) {
//								pause = 500; // adds bug
								if (gridTile.clickedPosition > -1) {
//									pause = pause * (gridTile.columnNum + 1);
									timer.schedule(new TimerTask() {

										@Override
										public void run() {
											//i want to know if there are any more of this letter needed
											if(gridTile.repeat > 1){
												gridTile.setSelected(false);
												gridTile.setBackground(tileColor);
												gridTile.repeat -= 1;
											}else{
												gridTile.setVisible(false);
											}
											gridTile.clickedPosition = -1;
										}
									}, pause);

								}
							}
							
							clickCount = 0;
//							guessWord = new String[currentGame.getWordLength()];
							for (AnswerTile answerTile : answerTiles) {
								timer.schedule(new TimerTask() {

									@Override
									public void run() {
										answerTile.setText(" ");
										answerTile.setBackground(Color.YELLOW);
									}
								}, pause);
//								pause -= 500;
							}
							if(foundWordList.size() == currentGame.getNumberWords()){
								endGame();
							}
						} else {
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.RED);
							}
						}

					}
				}
			}
			if (this instanceof AnswerTile) {
				AnswerTile pressedButton = (AnswerTile) this;
				if (!arg0.getDropSuccess()) {
					if (pressedButton.getBackground().equals(Color.RED)) {
						for (AnswerTile answerTile : answerTiles) {
							answerTile.setBackground(Color.YELLOW);
						}
					}
					guessWord.set(pressedButton.clickedPosition, "");
					shiftAnswerTilesLeft(answerTiles, pressedButton.clickedPosition,
							gridTiles);
					pressedButton.getParent().revalidate();
					clickCount--;
				} else if (arg0.getDropSuccess()) {
					
					if (clickCount == currentGame.getWordLength()) {
						setGuessWord();
						String found = "";
						for(String s: guessWord){
							found += s;
						}
						if (tracker.isWordInTheList(guessWord) && !tracker.isCurrentWordAlreadyFound()) {
							tracker.setCurrentWordAsFound();
							foundWordList.add(found);
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
							java.util.Timer timer = new java.util.Timer();
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.GREEN);
							}
							long pause = 0;

							for (GridTile gridTile : gridTiles) {
//								pause = 500; // adds bug
								if (gridTile.clickedPosition > -1) {
//									pause = pause * (gridTile.columnNum + 1);
									timer.schedule(new TimerTask() {

										@Override
										public void run() {
											//i want to know if there are any more of this letter needed
											if(gridTile.repeat > 1){
												gridTile.setSelected(false);
												gridTile.setBackground(tileColor);
												gridTile.repeat -= 1;
											}else{
												gridTile.setVisible(false);
											}
											gridTile.clickedPosition = -1;
										}
									}, pause);

								}
							}

							clickCount = 0;
//							guessWord = new String[currentGame.getWordLength()];
							for (AnswerTile answerTile : answerTiles) {
								timer.schedule(new TimerTask() {

									@Override
									public void run() {
										answerTile.setText(" ");
										answerTile.setBackground(Color.YELLOW);
									}
								}, pause);
//								pause -= 500; // adds bug

							}
							if(foundWordList.size() == currentGame.getNumberWords()){
								endGame();
							}
						} else {
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.RED);
							}
						}
					}
				}
			}
		}

		@Override
		public void dragEnter(DragSourceDragEvent arg0) {
		}

		@Override
		public void dragExit(DragSourceEvent arg0) {
		}

		@Override
		public void dragOver(DragSourceDragEvent arg0) {
		}

		@Override
		public void dropActionChanged(DragSourceDragEvent arg0) {
		}

		@Override
		public void dragGestureRecognized(DragGestureEvent arg0) {
			if (this instanceof GridTile) {
				GridTile pressedButton = (GridTile) this;
				for (GridTile gridTile : gridTiles) {
					if (gridTile.columnNum == pressedButton.columnNum
							&& gridTile.clickedPosition > -1) {
						return;
					}
				}
				if (clickCount == currentGame.getWordLength()) {
					if (pressedButton.clickedPosition < 0) {
						return;
					}
				}
				Transferable transferable;
				if (currentGame.getCharOrder()) {
					transferable = new StringSelection(getText() + " "
							+ pressedButton.columnNum);
				} else {
					transferable = new StringSelection(getText() + " "
							+ clickCount);

				}
				if (System.currentTimeMillis() - timer > 150) {
					ds.startDrag(arg0, DragSource.DefaultCopyDrop,
							transferable, this);
				}
			} else if (this instanceof AnswerTile) {
				if (getText().equals(" ")) {
					return;
				}
				AnswerTile pressedButton = (AnswerTile) this;
				Transferable transferable;
				if (currentGame.getCharOrder()) {
					transferable = new StringSelection(getText() + " "
							+ pressedButton.tileId);
				} else {
					transferable = new StringSelection(getText() + " "
							+ pressedButton.clickedPosition);
					setText(" ");
				}
				if (System.currentTimeMillis() - timer > 150) {
					ds.startDrag(arg0, DragSource.DefaultCopyDrop,
							transferable, this);
				}
			}

		}
		

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			timer = System.currentTimeMillis();

		}

		@Override
		public void mouseReleased(MouseEvent e) {
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
			setBackground(tileColor);
			tileId = iD;
		}

		DropTarget dt = new DropTarget(this, new DropTargetListener() {

			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {
			}

			@Override
			public void drop(DropTargetDropEvent dtde) {
				dtde.rejectDrop();
			}

			@Override
			public void dragOver(DropTargetDragEvent dtde) {
			}

			@Override
			public void dragExit(DropTargetEvent dte) {
			}

			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
			}
		});

	}

	class AnswerTile extends Tile {
		
		private static final long serialVersionUID = 1L;
		private static final int RIGHT_SHIFT = 0;
		private static final int LEFT_SHIFT = 1;
		int tileId = -1;
		int clickedPosition;
		String character = "";
		int tileShiftEnd = -1;
		int lastShift;
		/**
		 * Instantiates a new answer tile.
		 * @param character, the character
		 * @param iD, the i d
		 */
		AnswerTile(String character, int iD) {
			super();
			setText(character);
			tileId = iD;
			setBackground(tileColor);
			setVisible(false);
		}
		
		
		public void undoLastShift(int lastShift, int tileShiftEnd) {
			if (lastShift == RIGHT_SHIFT) {
				shiftTilesLeft(answerTiles, tileShiftEnd, gridTiles);
			} else if (lastShift == LEFT_SHIFT) {
				shiftTilesRight(answerTiles, tileShiftEnd, gridTiles);
			}
		}

		DropTarget dt = new DropTarget(this, new DropTargetListener() {

			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {
			}

			@Override
			public void drop(DropTargetDropEvent dtde) {
				try {

					Transferable transferable = dtde.getTransferable();

					if (transferable
							.isDataFlavorSupported(DataFlavor.stringFlavor)) {

						String dragContents = (String) transferable
								.getTransferData(DataFlavor.stringFlavor);
						String[] text = dragContents.split(" ");
						if (currentGame.getCharOrder()) {
							if (tileId != Integer.parseInt(text[1])) {
								dtde.rejectDrop();
							} else {
								dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
								setText(text[0]);
//								guessWord[tileId] = text[0];
								dtde.getDropTargetContext().dropComplete(true);
							}
						} else {
							dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
							clickedPosition = Integer.parseInt(text[1]);
							setText(text[0]);
//							guessWord[tileId] = text[0];
							dtde.getDropTargetContext().dropComplete(true);
						}

					} else {

						dtde.rejectDrop();

					}

				} catch (IOException e) {

					dtde.rejectDrop();

				} catch (UnsupportedFlavorException e) {

					dtde.rejectDrop();

				}
			}

			@Override
			public void dragOver(DropTargetDragEvent dtde) {

			}

			@Override
			public void dragExit(DropTargetEvent dte) {
				if (lastShift > -1) {
					undoLastShift(lastShift, tileShiftEnd);
				}
			}

			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				if (getText().equals(" ") | currentGame.getCharOrder()) {
					lastShift = -1;
					return;
				}
				if (canShiftRight(answerTiles, tileId)) {
					tileShiftEnd = shiftTilesRight(answerTiles, tileId,
							gridTiles);
					lastShift = RIGHT_SHIFT;
				} else if (canShiftLeft(answerTiles, tileId)) {
					tileShiftEnd = shiftTilesLeft(answerTiles, tileId,
							gridTiles);
					lastShift = LEFT_SHIFT;
				}
			}
		});
	}



}
