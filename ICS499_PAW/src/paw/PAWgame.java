package paw;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

//import paw.Config.languages;
import core.WordProcessor;

//import WordProcessor;
//import te.TeluguWordProcessor;


public class PAWgame {
	private String language;
	private int wordLength;
	private int numberOfWords;
	private ArrayList<String> wordsForGame = new ArrayList<>();
	private WordProcessor wp;
	private ArrayList<String> charsForGameTiles = new ArrayList<>();
    private int numberOfWordsFound = 0;
	PAWgame(){
		language = Config.LANGUAGES[Config.DEFAULTLANGUAGE];
		wordLength = 5;
		numberOfWords = 10;
	}
	PAWgame(String _language,int _wordLength, int _numberOfWords){
		language = _language;
		wordLength = _wordLength;
		numberOfWords = _numberOfWords;
	}
	public ArrayList<String> newGameCharacters(){
		String filePath = "";
		if (language.equals("English")){
			filePath = Config.enWordsFileName;
		} else {
			filePath = Config.teWordsFileName;
		}
		try (
				Scanner in = new Scanner(new InputStreamReader(new FileInputStream(filePath)));
				){
			ArrayList<String> wordsOfCorrectLength = new ArrayList<>();
			while (in.hasNextLine()){
				String word1 = in.nextLine();
				if (language.equals("English")){
					word1 = word1.toUpperCase();
					wp = new WordProcessor(word1.trim());
					
				}
				else if (language.equals("Telugu")){
					wp = new te.TeluguWordProcessor(word1);
					wp.setWord(wp.stripAllSymbols());
				}
				if (wp.getLength() == wordLength){
					wordsOfCorrectLength.add(wp.getWord());
				}				
			}
			//Randomize list before choosing words
			Collections.shuffle(wordsOfCorrectLength);
			
			//choose selected amount of words to use in game
			for (int i = 0; i < numberOfWords; i++) {
				wordsForGame.add(wordsOfCorrectLength.get(i));
			}
			System.out.println(wordsForGame);
			//Parse words to Logical chars
			ArrayList<String> allNeededLogicalChars = new ArrayList<>();
			for (String string : wordsForGame) {				
				wp.setWord(string);
				allNeededLogicalChars.addAll(wp.getLogicalChars());
			}
			ArrayList<String> intervalShuffleArray = new ArrayList<>();
			for (int i = 0; i < wordLength; i++) {
				for (int j = 0; j < numberOfWords; j++) {
					intervalShuffleArray.add(allNeededLogicalChars.get((i+(wordLength * j))));
				}
				Collections.shuffle(intervalShuffleArray);
				for (int k = 0; k < intervalShuffleArray.size(); k++) {
					allNeededLogicalChars.remove(i+(wordLength*k));
					allNeededLogicalChars.add((i+(wordLength*k)), intervalShuffleArray.get(k));
				}
				intervalShuffleArray.clear();
			}
			charsForGameTiles.addAll(allNeededLogicalChars);
			//Shuffle Logical characters so they are randomly added to board later
			//Collections.shuffle(charsForGameTiles);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charsForGameTiles;
	}
	public boolean isCorrectWord(ArrayList<String> inputWord){
		String guessWord = "";
		for (String string : inputWord) {
			guessWord += string;
		}
		for (String gameWord : wordsForGame) {
			wp.setWord(gameWord);
				if(wp.equals(guessWord)){
					wordsForGame.remove(gameWord);
					numberOfWordsFound++;
					return true;
				}
		}
		return false;
	}
	public int getWordLength() {
		return wordLength;
	}
	public int getNumberOfWordsFound() {
		return numberOfWordsFound;
	}
	public int getNumberOfWords() {
		return numberOfWords;
	}

}
