package paw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
	private static GameGenerator newGame;
	private static Game currentGame;
	private JPanel gridPanel;
	private JPanel answerPanel;
	private JPanel titlePanel;
	private JPanel wordListPanel;
	private JPanel buttonPanel;
	private JPanel buttPanel;
	private Font font;
	
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
		
		JEditorPane numWords = new JEditorPane();
		numWords.setFont(font);
		numWords.setBackground(Color.yellow);
		if(newGame != null){
			numWords.setText("Number of Words = " + String.valueOf(newGame.getNewGame().getNumberWords())
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
		if(newGame != null){
			JLabel titleLabel = new JLabel(newGame.getTitle() 
					+ " - (Duplicates = " + internalgui.tmpConfigSettings.get(4) + ")"
					+ " - (In Order = " + internalgui.tmpConfigSettings.get(5) + ")");
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
		if(newGame!= null){
			columnData = newGame.getNewGame().getColumnData();
		
			for(int i = 0; i < columnData.size(); i++){
				ArrayList<String> characters = columnData.get(i);
				JPanel column = new JPanel(new GridLayout(newGame.getNewGame().getNumberWords(), 1));
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
		if(newGame != null){
			for(int i = 0; i < columnData.size(); i++){
				JPanel ansRow = new JPanel(new GridLayout(1, columnData.size()));
				for(int j = 0; j < columnData.size(); j++){
					GridTile newTile = new GridTile("_");
					ansRow.add(newTile);
					//TODO add in the dragndrop features to receive the tiles...
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
//					CreateHTML ch = new CreateHTML(newGame.getNewGame());
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
					GameSaver gs = new GameSaver(newGame.getNewGame());
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
	public static void setNewGame(GameGenerator game){
		newGame = game;
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
}
