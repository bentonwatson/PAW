package paw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import te.TeluguWordProcessor;
import core.BigWord;
import core.BigWordCollection;
import core.Game;
import core.WordProcessor;

/**
 * 
 * @author Ben Watson/Landon Larch
 * Class to generate a new game
 * Takes game criteria input from gui, retrieves the words from input (file or gui),
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
	private ArrayList<BigWord> bigWordList;
	
	// game criteria variables
	private int level;
	private String topic;
	private int wordLength;
	private int wordStrength; //in English the wordStrength must match the wordLength
	private boolean duplicates; // true if duplicates are allowed within the columns
	private boolean charOrder; // true if characters are displayed in logical word order
	
	private int numWords;

	/**
	 * Constructor starts the game generation
	 * chooseNumberOfWords(int i) must be called to complete the game
	 * @param a_topic
	 * @param a_level
	 * @param a_length
	 * @param a_strength
	 * @param dup
	 * @param order
	 */
	@SuppressWarnings("unused")
	public GameGenerator(String a_topic, int a_level, int a_length, int a_strength,
			boolean dup, boolean order){
		topic = a_topic;
		wordLength = a_length;
		if(Config.DEFAULTLANGUAGE == 0){
			wordStrength = a_length;
		}else{
			wordStrength = a_strength;
		}
		level = a_level;
		duplicates = dup;
		charOrder = order;
		setTitle();
		setBigWordList();
		chooseNumberOfWords(bigWordList.size());
		setNewGame();
	}
	
	@SuppressWarnings("unused")
	public GameGenerator(String a_topic, int a_level, int a_length, int a_strength,
			boolean dup, boolean order, int a_num){
		topic = a_topic;
		wordLength = a_length;
		if(Config.DEFAULTLANGUAGE == 0){
			wordStrength = a_length;
		}else{
			wordStrength = a_strength;
		}
		level = a_level;
		duplicates = dup;
		charOrder = order;
		setTitle();
		setBigWordList();
		chooseNumberOfWords(a_num);
		setNewGame();
	}
	/**
	 * method to create a new game
	 */
	public void setNewGame(){
		newGame = new Game(level, title, wordList, columnData, duplicates, charOrder);
	}
	/**
	 * method returns the new game generated
	 * does not have an id assigned
	 * @return
	 */
	public Game getNewGame() {
		return newGame;
	}

	/**
	 * Method to set the title
	 * 
	 */
	public void setTitle() {
		title = "(Topic = " + topic + ") -" + " (Level = " + level + ")";
	}
	/**
	 * Method get the Title
	 * @return
	 */
	public String getTitle(){
		return title;
	}
	
	/**
	 * Method to set the level 
	 */
	public void setLevel(int a_level) {
		level = a_level;
	}
	
	/**
	 * Method to return the game level
	 * @return
	 */
	public int getLevel(){
		return level;
	}
	
	/**
	 * Method to randomly choose the number of words requested
	 * and set the column data
	 * @param num
	 */
	public void chooseNumberOfWords(int num){
		numWords = num;
		ArrayList<String> wordsOfCorrectLength = new ArrayList<String>();
		for (BigWord bigWord : bigWordList) {
			if(Config.DEFAULTLANGUAGE == 0){
				wordsOfCorrectLength.add(bigWord.getEnglish().toUpperCase());
			}else if(Config.DEFAULTLANGUAGE == 1){
				wordsOfCorrectLength.add(bigWord.getTelugu());
			}
		}
		chooseRandomListOfWords(wordsOfCorrectLength);
		setColumnData();
	}

	/**
	 * Method to read input file, find words based on criteria and 
	 * set the bigWordList variable
	 */
	@SuppressWarnings("unused")
	public void setBigWordList() {
		BigWordCollection bwc = new BigWordCollection();
		BigWordCollection bwcByCriteria = bwc.getBigWordCollectionByCriteria(topic, 
				wordLength, wordLength, wordStrength, wordStrength);
		bigWordList = new ArrayList<BigWord>();
		if (Config.DEFAULTLANGUAGE == 0){
			if(!bwcByCriteria.containsDuplicateEnglishWords()){
				bigWordList.addAll(bwcByCriteria.getAllBigWords());
			}else if(bwcByCriteria.containsDuplicateEnglishWords()){
				//TODO what to do if duplicate words are returned
				bigWordList.addAll(bwcByCriteria.getAllBigWords());
			}
		}
		if (Config.DEFAULTLANGUAGE == 1){
			if(!bwcByCriteria.containsDuplicateTeluguWords()){
				bigWordList.addAll(bwcByCriteria.getAllBigWords());
			} else if (bwcByCriteria.containsDuplicateTeluguWords()){
				//TODO what to do if duplicate words are returned
				bigWordList.addAll(bwcByCriteria.getAllBigWords());
			}
		}
	}
	
	/**
	 * Method to return the number of words found by criteria
	 * @return
	 */
	public int getNumBigWordList(){
		return bigWordList.size();
	}
	
	/**
	 * Method to return a list of all words found by criteria
	 * @return
	 */
	@SuppressWarnings("unused")
	public ArrayList<String> getWordsBigWordList(){
		ArrayList<String> words = new ArrayList<String>();
		if(Config.DEFAULTLANGUAGE == 0){
			for(BigWord bigWord : bigWordList){
				words.add(bigWord.getEnglish());
			}
		}else if(Config.DEFAULTLANGUAGE == 1){
			for(BigWord bigWord : bigWordList){
				words.add(bigWord.getTelugu());
			}
		}
		return words;
	}

	/**
	 * method called by chooseNumberOfWords 
	 * randomly chooses the requested number of words
	 * from the returned bigWordList
	 * When bigWordList is < number requested all bigWords are used
	 * @param wordsOfCorrectLength
	 */
	public void chooseRandomListOfWords(List<String> wordsOfCorrectLength) {
		if(wordsOfCorrectLength.size() >= numWords){
			Collections.shuffle(wordsOfCorrectLength);
			wordList = new ArrayList<String>();
			for (int i = 0; i < numWords; i++) {
				wordList.add(wordsOfCorrectLength.get(i));
			}
		}else{
			Collections.shuffle(wordsOfCorrectLength);
			wordList = new ArrayList<String>();
			for (int i = 0; i < wordsOfCorrectLength.size(); i++) {
				wordList.add(wordsOfCorrectLength.get(i));
			}
			
		}
	}

	/**
	 * Method to set the columnData
	 */
	public void setColumnData() {
		columnData = createColumns();
		
		if(!duplicates){
			removeDuplicates();
			
		}
		if(!charOrder){
			shuffleColumns();
		}
	}

	/**
	 * Method to create the columns
	 * @return ArrayList<ArrayList<String>>
	 */
	public ArrayList<ArrayList<String>> createColumns(){
		ArrayList<ArrayList<String>> columns = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < wordLength; i++){
			ArrayList<String> single = new ArrayList<String>();
			for(String word : wordList){
				WordProcessor wp = new TeluguWordProcessor(word);
				single.add(wp.logicalCharAt(i));
			}
			single = shuffleChars(single);
			columns.add(single);
		}
		return columns;
	}
	
	/**
	 * Shuffle characters within a single column.
	 * @param charsToShuffle: the chars to shuffle
	 */
	public ArrayList<String> shuffleChars(ArrayList<String> charsToShuffle) {
		Collections.shuffle(charsToShuffle);
		return charsToShuffle;
	}

	/**
	 * Shuffles the column order
	 */
	public void shuffleColumns() {
		Collections.shuffle(columnData);
	}

	/**
	 * removes the duplicate letters within a single column
	 */
	public void removeDuplicates() {
		for (ArrayList<String> arrayList : columnData) {
			HashSet<String> uniqueCharacters = new HashSet<>(arrayList);
			arrayList.clear();
			arrayList.addAll(uniqueCharacters);
			Collections.shuffle(arrayList);
		}
	}
	
	/**
	 * Main method to test the generator
	 * @param args
	 */
//	public static void main(String[] args) {
//		GameGenerator gg = new GameGenerator("BodyParts", 1, 4, 4, false, false);
//		gg.chooseNumberOfWords(5);
//		Game game = gg.getNewGame();
//		System.out.println(game.toString());
//	}
}
