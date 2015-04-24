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

	private List<Boolean> wordListStatus;
	private List<String> wordList;
	private int numberOfWords;
	private int numberOfWordsFound;
	private int currentIndex;

	/**
	 * constructor for setting the puzzle to play
	 * 
	 * @param a_puzzle
	 */
	GameTracker(Game a_game)
	{
		// numberOfWords = puzzle.getWordList().size();
		numberOfWordsFound = 0;
		wordList = a_game.getWordList();
		numberOfWords = wordList.size();

		wordListStatus = new ArrayList<Boolean>(); // by default, all will be
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
	 * @param inputWord
	 * @return
	 */
	public boolean isWordInTheList(ArrayList<String> inputWord){
		WordProcessor wpin = new WordProcessor(inputWord);
		ArrayList<String> input = wpin.getLogicalChars();
		for (int i = 0; i < wordList.size(); i++) {
			WordProcessor wpg = new WordProcessor(wordList.get(i));
			wpg.stripSpaces();
			ArrayList<String> gw = wpg.getLogicalChars();
			int matchCount = 0;
			for(int j = 0; j < inputWord.size(); j++){
				if(input.get(j).equals(gw.get(j))){
					matchCount++;
				}
			}
			if(matchCount == inputWord.size()){
				currentIndex = i;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * this method sets the a_selected_word as FOUND precondition is
	 * isWordInTheList = true
	 */
	public void setCurrentWordAsFound()
	{
		wordListStatus.set(currentIndex, true);
		numberOfWordsFound++;

	}

	/**
	 * This method checks whether a word has already been marked as found
	 * @return
	 */
	public boolean isCurrentWordAlreadyFound()
	{
		if(wordListStatus.get(currentIndex)){
			return true;
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
