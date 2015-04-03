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
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import core.SpringUtility;

/**
 * this tab will display the list of available words in left text type panel
 * the list may be from the config output or the manual input
 * field for number of words to select with a generate button
 * after game is displayed a save button is available to call saveGame
 * @author Ben
 *
 */
class GeneratePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private PAWgui internalgui;
	private static GameGenerator newGame;
	private JPanel gridPanel;
	private JPanel titlePanel;
	private JPanel wordListPanel;
	private JPanel buttonPanel;
	private Font font;
	
	public GeneratePanel(Color color, PAWgui paw) {
		this.internalgui = paw;
		font = internalgui.getFont();
		
		setMinimumSize(new Dimension(1000,550));
		setBackground(color);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout());
		
//		String topic = internalgui.tmpConfigSettings.get(0);
//		int level = Integer.valueOf(internalgui.tmpConfigSettings.get(1));
//		int len = Integer.valueOf(internalgui.tmpConfigSettings.get(2));
//		int stren = Integer.valueOf(internalgui.tmpConfigSettings.get(3));
//		boolean dup = Boolean.valueOf(internalgui.tmpConfigSettings.get(4));
//		boolean order = Boolean.valueOf(internalgui.tmpConfigSettings.get(5));
//		String showAll = internalgui.tmpConfigSettings.get(6);
//		int numWords = Integer.valueOf(internalgui.tmpConfigSettings.get(7));
//		newGame = new GameGenerator(topic, level, len, stren, dup, order);
//		if(Boolean.valueOf(showAll)){
//			newGame.chooseNumberOfWords(newGame.getNumBigWordList());
//		}else{
//			newGame.chooseNumberOfWords(numWords);
//		}
//	
		generateWordListPanel();
		generateGridPanel();
		generateButtonPanel();
	}
	
	/**
	 * generates the panel to display words returned from config panel
	 */
	//TODO FUTURE: this panel will be an input panel that can generate a new Game from input words
	public void generateWordListPanel(){
		wordListPanel = new JPanel();
		wordListPanel.setPreferredSize(new Dimension(200, 300));
		wordListPanel.setLayout(new BorderLayout());
		
		JEditorPane numWords = new JEditorPane();
		numWords.setFont(font);
		if(newGame != null){
			numWords.setText("Number of Words = " + String.valueOf(newGame.getNewGame().getNumberWords()));
		}else{
			numWords.setText("Number of Words = 0 \n You Have Found = 0");
		}		wordListPanel.add(numWords, BorderLayout.NORTH);
		
		JEditorPane words = new JEditorPane();
		words.setFont(font);
		String list = "";
		if(newGame!= null){
			ArrayList<String> wordList = newGame.getNewGame().getWordList();
			for(int i = 0; i < wordList.size(); i++){
				list += wordList.get(i) + "\n";
			}
		}
		words.setText(list);
		wordListPanel.add(words, BorderLayout.CENTER);
		
		add(wordListPanel, BorderLayout.WEST);
	}
	
	/**
	 * Creates the panel to display the columns
	 * includes the answer row
	 * @return 
	 */
	public void generateGridPanel(){
		gridPanel =  new JPanel();
		gridPanel.setBorder(new LineBorder(Color.black, 2));
		gridPanel.setLayout(new BorderLayout());
		
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
		
		JPanel columnPanel = new JPanel(new SpringLayout());
		JScrollPane sp = new JScrollPane(columnPanel);
		
		if(newGame!= null){
			ArrayList<ArrayList<String>> columnData = newGame.getNewGame().getColumnData();
			columnData = newGame.getNewGame().getColumnData();
			for(int i = 0; i < columnData.size(); i++){
				ArrayList<String> characters = columnData.get(i);
				JPanel column = new JPanel(new GridLayout(newGame.getNewGame().getNumberWords(), 1));
				
				for(int j = 0; j < characters.size(); j++){
					GridTile newTile = new GridTile(characters.get(j));
					column.add(newTile);
				}
				columnPanel.add(column);
			}
			SpringUtility.makeGrid(columnPanel, 1, columnData.size(), 5, 5, 5, 5);
		}
		
		gridPanel.add(sp, BorderLayout.CENTER);
		add(gridPanel, BorderLayout.CENTER);
	}
	
	/**
	 * generates a panel to hold action buttons
	 */
	public void generateButtonPanel(){
		JPanel buttPanel = new JPanel();
		buttonPanel = new JPanel(new SpringLayout());
		
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
		
		//sends the game to play Tab and opens play tab
		JButton playGameBtn = new JButton("Play\nGame");
		playGameBtn.setFont(font);
		playGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AdminPlayPanel.setNewGame(newGame);
					internalgui.selectTabbedPaneIndex(1);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(playGameBtn);
		

		SpringUtility.makeGrid(buttonPanel, 3, 1, 25, 15, 15, 15);
		
		buttPanel.add(buttonPanel);
		add(buttPanel, BorderLayout.EAST);
		
	}
	
	/**
	 * sets the newGame for the Generate Panel
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
