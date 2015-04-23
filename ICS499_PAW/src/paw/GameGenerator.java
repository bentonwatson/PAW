package paw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
	private ArrayList<String> customWords;
	private ArrayList<ArrayList<String>> columnData;
	private ArrayList<BigWord> bigWordList;
	private ArrayList<String> returnedWordList;
	
	// game criteria variables
	private int level;
	private String topic;
	private boolean custom;
	private int wordLength;
	private int minWordStrength; 
	private int maxWordStrength; 
	private boolean duplicates; // true if duplicates are allowed within the
	// columns
	private boolean charOrder; // true if characters are displayed in logical
	// word order

	private int numWords;
	private String language = Config.DEFAULTLANGUAGE;

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
	public GameGenerator(String a_topic, int a_level, int a_length, int min_strength,
			int max_strength, boolean dup, boolean order){
		topic = a_topic;
		wordLength = a_length;
		if (language.equals("en")) {
			minWordStrength = a_length;
			maxWordStrength = a_length;
		}else{
			minWordStrength = min_strength;
			maxWordStrength = max_strength;
		}
		level = a_level;
		duplicates = dup;
		charOrder = order;
		custom = false;
		setTitle();
		setBigWordList();
		chooseNumberOfWords(bigWordList.size());
		setColumnData();
		setNewGame();
	}
	
	public GameGenerator(String a_topic, int a_level, int a_length, int min_strength,
			int max_strength, boolean dup, boolean order, int a_num){
		topic = a_topic;
		wordLength = a_length;
		if (language.equals("en")) {
			minWordStrength = a_length;
			maxWordStrength = a_length;
		}else{
			minWordStrength = min_strength;
			maxWordStrength = max_strength;
		}
		level = a_level;
		duplicates = dup;
		charOrder = order;
		custom = false;
		setTitle();
		setBigWordList();
		chooseNumberOfWords(a_num);
		setColumnData();
		setNewGame();
	}
	
	public GameGenerator(String[] defaultSetting){
		topic = defaultSetting[0];
		wordLength = Integer.valueOf(defaultSetting[2]);
		minWordStrength = Integer.valueOf(defaultSetting[3]);
		maxWordStrength = Integer.valueOf(defaultSetting[4]);
		level = Integer.valueOf(defaultSetting[1]);
		duplicates = Boolean.valueOf(defaultSetting[5]);
		charOrder = Boolean.valueOf(defaultSetting[6]);
		custom = false;
		setTitle();
		setBigWordList();
		chooseNumberOfWords(Integer.valueOf(defaultSetting[8]));
		setColumnData();
		setNewGame();
	}

	public GameGenerator(String a_topic, int a_level, int a_length,
			int min_strength, int max_strength, 
			boolean dup, boolean order, ArrayList<String> words) {
		topic = a_topic;
		wordLength = a_length;
		if (language.equals("en")) {
			minWordStrength = a_length;
			maxWordStrength = a_length;
		}else{
			minWordStrength = min_strength;
			maxWordStrength = max_strength;
		}
		level = a_level;
		duplicates = dup;
		charOrder = order;
		custom = true;
		customWords = words;
		setTitle();
		chooseNumberOfWords(words.size());
		setColumnData();
		setNewGame();
	}

	/**
	 * Method to randomly choose the number of words requested
	 * and set the column data
	 * @param num
	 */
	public void chooseNumberOfWords(int num){
		numWords = num;
		ArrayList<String> wordsOfCorrectLength = new ArrayList<String>();
		returnedWordList = new ArrayList<String>();
		WordProcessor wp = new WordProcessor("");
		if(custom){
			for(String word : customWords){
				wp.setWord(word);
				if (language.equals("en")){
					wp.stripAllSymbols();
					wp.toCaps();
				}
				wp.trim();
				wp.stripSpaces();
				if (wp.getLength() == wordLength) {
					wordsOfCorrectLength.add(wp.getWord());
				}
			}
		}else{
			for (BigWord bigWord : bigWordList) {
				if (language.equals("en")){
					wp.setWord(bigWord.getEnglish());
					wp.stripAllSymbols();
					wp.toCaps();
				}else if (language.equals("te")){
					wp.setWord(bigWord.getTelugu());
				}
				wp.trim();
				wp.stripSpaces();
				if (wp.getLength() == wordLength) {
					wordsOfCorrectLength.add(wp.getWord());
				}
			}
		}
		returnedWordList = wordsOfCorrectLength;
		chooseRandomListOfWords(wordsOfCorrectLength);
		
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
	 * Method to create the columns
	 * @return ArrayList<ArrayList<String>>
	 */
	public ArrayList<ArrayList<String>> createColumns(){
		ArrayList<ArrayList<String>> columns = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < wordLength; i++){
			ArrayList<String> single = new ArrayList<String>();
			for(String word : wordList){
				WordProcessor wp = new WordProcessor(word);
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
			ArrayList<String> uniqueCharacters = new ArrayList<String>();
			for(int i = 0; i < arrayList.size(); i++){
				String c = arrayList.get(i);
				int count = 0;
				for(int j = 0; j < arrayList.size(); j++){
					if(c.equals(arrayList.get(j))){
						count++;
					}
				}
				uniqueCharacters.add(arrayList.get(i) + count);
			}
			HashSet<String> uniqueChar = new HashSet<>(uniqueCharacters);
			arrayList.clear();
			arrayList.addAll(uniqueChar);
			
			Collections.shuffle(arrayList);
		}
	}

	/**
	 * Method to set the title
	 * 
	 */
	public void setTitle() {
		title = "Topic = " + topic;
	}

	/**
	 * Method to set the level 
	 */
	public void setLevel(int a_level) {
		level = a_level;
	}

	/**
	 * Method to read input file, find words based on criteria and 
	 * set the bigWordList variable
	 */
	public void setBigWordList() {
		BigWordCollection bwc = new BigWordCollection();
		BigWordCollection bwcByCriteria = bwc.getBigWordCollectionByCriteria(topic, 
				wordLength, wordLength, minWordStrength, maxWordStrength);
		bigWordList = new ArrayList<BigWord>();
		if (language.equals("en") ){
			if (!bwcByCriteria.containsDuplicateEnglishWords()) {
				bigWordList.addAll(bwcByCriteria.getAllBigWords());
			}else if(bwcByCriteria.containsDuplicateEnglishWords()){
				//TODO what to do if duplicate words are returned
				bigWordList.addAll(bwcByCriteria.getAllBigWords());
			}
		}
		if (language.equals("te")) {
			if (!bwcByCriteria.containsDuplicateTeluguWords()) {
				bigWordList.addAll(bwcByCriteria.getAllBigWords());
			} else if (bwcByCriteria.containsDuplicateTeluguWords()){
				//TODO what to do if duplicate words are returned
				bigWordList.addAll(bwcByCriteria.getAllBigWords());
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
			for (ArrayList<String> arrayList : columnData) {
				while (arrayList.size() < numWords) {				
					arrayList.add(" ");
				}
			}
		}
		if(!charOrder){
			shuffleColumns();
		}
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

	public String getTopic(){
		return topic;
	}
	
	/**
	 * Method get the Title
	 * @return
	 */
	public String getTitle(){
		return title;
	}
	
	/**
	 * Method to return the game level
	 * @return
	 */
	public int getLevel(){
		return level;
	}
	
	/**
	 * Method to return the number of words found by criteria
	 * @return
	 */
	public int getNumBigWordList(){
		return bigWordList.size();
	}
	
	public int getNumCustomWords() {
		return customWords.size();
	}
	
	public int getWordLength(){
		return wordLength;
	}
	
	public int getMaxWordStrength(){
		return maxWordStrength;
	}
	
	public int getMinWordStrength(){
		return minWordStrength;
	}
	
	public boolean getDuplicates(){
		return duplicates;
	}
	
	public boolean getCharOrder(){
		return charOrder;
	}
	
	public int getNumWords(){
		return numWords;
	}
	
	public ArrayList<String> getWordList(){
		return wordList;
	}
	
	public boolean getCustom(){
		return custom;
	}
	
	public ArrayList<String> getCustomWords(){
		return customWords;
	}

	/**
	 * Method to return a list of all words found by criteria
	 * @return
	 */
	public ArrayList<String> getWordsBigWordList(){
		ArrayList<String> words = new ArrayList<String>();
		if(language.equals("en")){
			for(BigWord bigWord : bigWordList){
				words.add(bigWord.getEnglish());
			}
		}else {
			for(BigWord bigWord : bigWordList){
				words.add(bigWord.getTelugu());
			}
		}
		return words;
	}

	public ArrayList<String> getReturnedWordList(){
		return returnedWordList;
	}
	
//	/**
//	 * Main method to test the generator
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		GameGenerator gg = new GameGenerator("BodyParts", 1, 4, 4, false, false);
//		gg.chooseNumberOfWords(5);
//		Game game = gg.getNewGame();
//		System.out.println(game.toString());
//	}
}
