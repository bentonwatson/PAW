package paw;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import core.Game;
import core.GameCollection;
import core.SpringUtility;

public class UserPlayPanel extends JPanel {
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
	
	ArrayList<String> foundWordList = new ArrayList<String>();
	
	public UserPlayPanel(Color color, PAWgui paw){
		this.internalgui = paw;
		font = internalgui.getFont();
		gameCollection = internalgui.getGameCollection();
		
//		currentGame = Config.defaultGameGenerator.getNewGame();
//		userGameLevel = currentGame.getLevel();
		userGameLevel = internalgui.getUserGameLevel();
		
		
		setMinimumSize(new Dimension(1000,550));
		setBackground(color);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout());
		
		initialize();
	}
	public void initialize(){
		
		//if currentGame == null the panels will generate empty 
		generateWordListPanel();
		generateGridPanel();
		generateButtonPanel();
		
		setVisible(true);
		time.restart();
	}
	
	/**
	 * generates the panel to display words that have been guessed correctly
	 */
	public void generateWordListPanel(){
		wordListPanel = new JPanel();
		wordListPanel.setPreferredSize(new Dimension(200, 300));
		wordListPanel.setLayout(new BorderLayout());
		
		JEditorPane numWords = new JEditorPane();
		numWords.setFont(font);
		numWords.setBackground(Color.yellow);
		
		if(currentGame != null){
			numWords.setText("Number of Words = " + String.valueOf(currentGame.getNumberWords())
					+ "\n You Have Found = " + foundWordList.size());
		}else{
			numWords.setText("Number of Words = 0 \n You Have Found = 0");
		}
		wordListPanel.add(numWords, BorderLayout.NORTH);
		
		JEditorPane words = new JEditorPane();
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
		if(currentGame != null){
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
		JScrollPane sp = new JScrollPane(columnPanel);
		
		columnPanel.setLayout(new SpringLayout());
		
		ArrayList<ArrayList<String>> columnData = new ArrayList<ArrayList<String>>();
		if(currentGame!= null){
			columnData = currentGame.getColumnData();
			
			for(int i = 0; i < columnData.size(); i++){
				ArrayList<String> characters = columnData.get(i);
				JPanel column = new JPanel(new GridLayout(currentGame.getNumberWords(), 1));
				for(int j = 0; j < characters.size(); j++){
					GridTile newTile = new GridTile(characters.get(j));
					column.add(newTile);
					//TODO add in the dragndrop features to each tile...
				}
				
				columnPanel.add(column);
			}
			SpringUtility.makeGrid(columnPanel, 1, columnData.size(), 5, 5, 5, 5);
			
		}
		
		gridPanel.add(sp, BorderLayout.CENTER);
		
		JPanel instructPanel = new JPanel();
		JLabel ansLabel = new JLabel("Drag tile to quess word.");
		ansLabel.setFont(font);
		instructPanel.add(ansLabel);

		answerPanel = new JPanel(new BorderLayout());
		answerPanel.add(instructPanel, BorderLayout.NORTH);
		if(currentGame!= null){
			for(int i = 0; i < columnData.size(); i++){
				JPanel ansRow = new JPanel(new GridLayout(1, columnData.size()));
				for(int j = 0; j < columnData.size(); j++){
					GridTile newTile = new GridTile("_");
					ansRow.add(newTile);
					//TODO add in the dragndrop features to each tile...
				}
				answerPanel.add(ansRow, BorderLayout.CENTER);
			}
		}
		JButton guessBtn = new JButton("Guess");
		guessBtn.setFont(font);
		guessBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					checkGuess();
					//TODO if last word stop timer, record and call progress tracker
					generateWordListPanel();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		answerPanel.add(guessBtn, BorderLayout.SOUTH);
		
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
	
	public void setUserGameLevel(int level){
		userGameLevel = level;
	}
	
	/**
	 * generates a panel to hold action buttons
	 */
	public void generateButtonPanel(){
		buttPanel = new JPanel();
		buttPanel.setBackground(Color.yellow);
		buttonPanel = new JPanel(new SpringLayout());
		buttonPanel.setBackground(Color.yellow);
		
		// Level
		levelComboLabel = new JLabel("Level");
		levelComboLabel.setFont(font);
		levelComboBox = new JComboBox<String>();
		String [] levelByName = {"Easy", "Medium", "Hard", "Impossible"};
		for(int i = 0; i < 4; i++){
			levelComboBox.addItem(levelByName[i]);
		}
		levelComboBox.setFont(font);
//		if(currentGame != null){
//			userGameLevel = currentGame.getLevel();
//			userGameLevelByName = levelByName[currentGame.getLevel() - 1];
//		}else{
//			userGameLevel = Integer.valueOf(internalgui.tmpConfigSettings.get(1));
			userGameLevelByName = levelByName[userGameLevel - 1];
//		}
		levelComboBox.setSelectedItem(userGameLevelByName);
		levelComboBox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				userGameLevelByName = String.valueOf(levelComboBox.getSelectedItem());
				if(userGameLevelByName.equals("Easy")){
					setUserGameLevel(1);
					System.out.println("change to 1");
				}
				if(userGameLevelByName.equals("Medium")){
					setUserGameLevel(2);
					System.out.println("change to 2");
				}
				if(userGameLevelByName.equals("Hard")){
					setUserGameLevel(3);
					System.out.println("change to 3");
				}
				if(userGameLevelByName.equals("Impossible")){
					setUserGameLevel(4);
					System.out.println("change to 4");
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
//					ProgressTracker pt = new ProgressTracker();
					System.out.println(userGameLevel);
					System.out.println(userGameLevelByName);
					time.stop();
					currentGame = null;
					currentGame = gameCollection.getGameByLevel(userGameLevel);
					System.out.println(userGameLevel);
					System.out.println(userGameLevelByName);
					initialize();
					//TODO ?? add function to save completed game then get new game
					// may be passing in the game level to get a particular game set
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
		
		SpringUtility.makeGrid(buttonPanel, 4, 1, 25, 15, 15, 15);
		buttPanel.add(buttonPanel);
		add(buttPanel, BorderLayout.EAST);
	}
	
	/**
	 * sets the currentGame for the User in Play Panel
	 * @param Game
	 */
	public static void setCurrentGame(Game game){
		currentGame = game;
	}
	
	/**
	 * This defines the basic tile used to hold the logical characters in the game
	 *
	 */
	class Tile extends JToggleButton {
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

		GridTile(String character){
			super();
			setText(character);			
			setBackground(Color.yellow);
			if(character.equals(" ")){
				setVisible(false);
			}
		}

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
			if (seconds == N) {
				seconds = 00;
				minutes++;
			}

			if (minutes == N) {
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

	public void componentResized(ComponentEvent arg0)
	{

//		Dimension sidePanelSize = sidePanel.getSize();
//		wordsPanel.setBounds(0, 50, sidePanelSize.width,
//				sidePanelSize.height - 50 - 70);
//		Dimension wordsPanelSize = wordsPanel.getSize();
//		timerPanel.setBounds(0, wordsPanelSize.height + 50,
//				sidePanelSize.width, 70);
	}

	public static void errorMessage(String a_string)
	{

		JOptionPane.showMessageDialog(null, a_string, "Error",
				JOptionPane.ERROR_MESSAGE);
		System.exit(0);

	}

	public void componentHidden(ComponentEvent arg0)
	{
	}

	public void componentMoved(ComponentEvent arg0)
	{
	}

	public void componentShown(ComponentEvent arg0)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
	}
}
