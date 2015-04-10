package paw;

import java.util.ArrayList;
import java.util.List;

import core.Game;
import core.WordProcessor;

/**
 * A java class that acts as a wrapper to the Puzzle to track the play progress
 * and completion
 * 
 * @author srj Course: ICS 240 Programming With Elementary Data Structure
 *         Semester: Summer 2014 Instructor: Silva Jasthi Student: James
 *         Lindstrom Assignment: Final Compiler: JDK 1.7 with Eclipse
 *         
 * changes made to suite PAW game to GameTracker from PuzzleTracker
 * 
 */
public class GameTracker
{

	private Game game; // this is the selected puzzle;
	private List<Boolean> wordListStatus;
	private List<String> wordList;
	private int numberOfWords;
	private int numberOfWordsFound;
	private int language; // 1=Telugu 0=English

	/**
	 * constructor for setting the puzzle to play
	 * 
	 * @param a_puzzle
	 */
	GameTracker(Game a_game)
	{
		language = Config.DEFAULTLANGUAGE;
		wordListStatus = new ArrayList<Boolean>(); // by default, all will be
													// FALSE
		// numberOfWords = puzzle.getWordList().size();
		numberOfWordsFound = 0;
		wordList = a_game.getWordList();
		numberOfWords = wordList.size();
		for (int i = 0; i < numberOfWords; i++)
		{
			wordListStatus.add(false);
		}
	}

	/**
	 * This method checks whether a_selected_word is in the list of the words
	 * When a match is found, wordListStatus is updated at the corresponding
	 * location
	 * 
	 * @param a_selected_word
	 * @return
	 */
	public boolean isWordInTheList(String a_selected_word)
	{
		boolean theReturn = false;
		
		for (int i = 0; i < numberOfWords; i++)
		{
			String word = wordList.get(i);
			word = word.replaceAll("\\s+", "");
			// System.out.println(word);
			if (word.equalsIgnoreCase(a_selected_word.replaceAll("\\s+", "")))
			{
				theReturn = true;
				// set the word status
				setSelectedWordAsFound(i);
			}
		}
		
		return theReturn;
	}
	
	
	public boolean isWordInTheList(String[] inputWord){
		WordProcessor wp = new WordProcessor("");
		if(language == 1){
			wp = new WordProcessor("");
		}
		String guessWord = "";
		for (String string : inputWord) {
			guessWord += string;
		}
		
		for (String gameWord : wordList) {
			wp.setWord(gameWord);
				if(wp.equals(guessWord)){
					numberOfWordsFound++;
					return true;
				}
		}
		return false;
	}
	
	public boolean wordHasAlreadyBeenFound(String a_selected_word)
	{
		for (int i=0; i< wordList.size(); i++) {
			// THe word is in the word list
			if (wordList.get(i).equalsIgnoreCase(a_selected_word)) {
				if (wordListStatus.get(i)) {
					return true;
				}
			}
		} 
		return false;
	}

	/**
	 * this method sets the a_selected_word as FOUND precondition is
	 * isWordInTheList = true
	 */
	public void setSelectedWordAsFound(int a_index)
	{
		wordListStatus.add(a_index, true);
		numberOfWordsFound++;

	}

	/**
	 * This method checks whether a word has already been marked as found
	 * 
	 * @param a_selected_word
	 * @return
	 */
	public boolean isWordAlreadyFound(String a_selected_word)
	{
		for (int i = 0; i < numberOfWords; i++)
		{
			if (wordListStatus.get(i).equals(true))
			{
				if (wordList.get(i).equals(a_selected_word))
				{
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<String> getWordsNotFound(){
		ArrayList<String> words = new ArrayList<String>();
		for(int i = 0; i < wordList.size(); i++){
			if(wordListStatus.get(i).equals(false)){
				words.add(wordList.get(i));
			}
		}
		return words;
	}
	
	/**
	 * Checks whether all words have been found by the user
	 */
	public boolean areAllWordsFound()
	{
		return (numberOfWords == numberOfWordsFound);
	}

	/**
	 * Checks the game Status
	 */
	public boolean isGameComplete()
	{
		return (numberOfWords == numberOfWordsFound);
	}

	/**
	 * Gets the game completed message only if the game is completed
	 */
	public String getGameStatusMsg()
	{
		if (isGameComplete() == true)
			return (Config.enWINMESSAGE);
		else
			return (Config.enLOSEMESSAGE);
	}

}
