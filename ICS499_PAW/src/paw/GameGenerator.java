package paw;

import java.util.ArrayList;

import core.Game;

/**
 * 
 * @author Ben
 * Class to generate a new game
 * Takes game criteris input from gui, retrieves the words from input (file or gui),
 * Selects words matching the criteria that will be used in the game.
 * determines a title and level
 * creates the column data for display
 *   
 */
public class GameGenerator {
	private Game newGame;
	private String title;
	private ArrayList<String> wordList;
	private ArrayList<ArrayList<String>> columnData;
	
	// congif parameters
	private String level;
	private String topic;
	private int wordLength;
	private int numWords;
	private int wordStrength;
	private int wordWeight;
	private boolean duplicates; // true if duplicates are allowed within the columns
	private boolean charOrder; // true if characters are displayed in logical word order
	private boolean showAllWords; // false when only words matching criteria are to be returned
	
	public GameGenerator(String a_topic, int a_length, int a_numWords, int a_strength,
			int a_weight, boolean dup, boolean order, boolean all){
		topic = a_topic;
		wordLength = a_length;
		numWords = a_numWords;
		wordStrength = a_strength;
		wordWeight = a_weight;
		duplicates = dup;
		charOrder = order;
		showAllWords = all;
		
		setTitle();
		setLevel();
		setWordList();
		setColumnData();
	}
	
	/**
	 * method returns the game generated
	 * @return
	 */
	public Game getNewGame(){
		return newGame;
	}
	
	/**
	 * Method to set the title 
	 * 
	 */
	public void setTitle(){
		//TODO
	}
	
	/**
	 * Method to set the level
	 * level will be determined based on ?? criteria right now
	 */
	public void setLevel(){
		//TODO
	}
	
	/**
	 * Method to read input file, choose words based on criteria
	 * set the wordList variable
	 */
	public void setWordList(){
		//TODO
	}
	
	/**
	 * Method to set the columnData
	 */
	public void setColumnData(){
		//TODO
	}
}
