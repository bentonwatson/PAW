package core;
/*
 * Benton Watson
 * WordProcessor Class
 */

// The complete implementation along with the documentation 
// will count towards two software engineering assignments
// Software Engineering Assignment 2
// Software Engineering Assignment 3

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This class provides several operations in the context of single word.
 * This class is expected to work against multiple languages so the implementation
 * should not rely on the assumption that the string contain characters.
 */

public class WordProcessor {
	
	//word represents the string we are processing in this class
	private String word;
	
	// logicalChars are derived from the word
	// word can also be derived from logicalChars
	// these two are dependent on each other
	// if one changes, the other changes
	private ArrayList<String> logicalChars;
	
	/**
	 * Default constructor
	 */
	public WordProcessor()
	{
		
	}
	
	/**
	 * Overloaded constructor that takes the word
	 * @param a_word
	 */
	
	public WordProcessor(String a_word)
	{
		setWord(a_word);
	}
	
	/**
	 * Overloaded constructor that takes the logical characters as input
	 * @param some_logical_chars
	 */
	public WordProcessor(ArrayList<String> some_logical_chars)
	{
		setLogicalChars(some_logical_chars);
	}
	
	/**
	 * set method for the word
	 * @param a_word
	 */
	public void setWord(String a_word)
	{
		word = a_word;
		parseToLogicalChars();
	}
	
	/**
	 * set method for the logical characters
	 * @param some_logical_chars
	 */
	public void setLogicalChars(ArrayList<String> some_logical_chars)
	{
		logicalChars = some_logical_chars;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < logicalChars.size(); i++){
			sb.append(logicalChars.get(i));
		}
		word = sb.toString();
		
	}
	
	/**
	 * get method for the word
	 * @return
	 */
	public String getWord()
	{
		return word;
	}
	
	/**
	 * get method for the logical characters
	 * @return
	 */
	public ArrayList<String> getLogicalChars()
	{
		return logicalChars;
	}
	
	/**
	 * Returns the length of the word
	 * length = number of logical characters
	 * @return
	 */
	public int getLength()
	{
		return logicalChars.size();
	}
	
	/**
	 * Returns the number of code points in the word
	 * @return
	 */
	public int getCodePointLength()
	{
		char[] tmp = word.toCharArray();
		return tmp.length;
	}
	
	/**
	 * This method breaks the input word into logical characters
	 * Changes for other languages...override with other language packages
	 * For Engligh,
	 * 	  convert the string to char array
	 * 	  and convert each char to a string
	 *    and populate logicalChars
	 */
	public void parseToLogicalChars()
	{
		logicalChars = new ArrayList<String>();
		char[] tmp = word.toCharArray();
		for(int i = 0; i < tmp.length; i++){
			String s = String.valueOf(tmp[i]);
			logicalChars.add(s);
		}
	}
	
	/**
	 * If the word starts with the logical character, 
	 * this method returns true.
	 * @param start_sub_string
	 * @return
	 */
	public boolean startsWith(String start_sub_string)
	{
		return word.startsWith(start_sub_string);
	}
	
	/**
	 * If the word ends with the logical character, 
	 * this method returns true.
	 * @param start_char
	 * @return
	 */
	public boolean endsWith(String end_string)
	{
		return word.endsWith(end_string);
	}
	
	/**
	 * This method checks whether the sub_string or logical character
	 * is contained within the word
	 * @param sub_string
	 * @return
	 */
	public boolean containsString(String sub_string)
	{
		return word.contains(sub_string);
	}
	
	/**
	 * This method checks whether the sub_string or logical character
	 * is contained within the word
	 * @param sub_string
	 * @return
	 */
	public boolean containsChar(String sub_string)
	{
		return containsString(sub_string);
	}
	
	/**
	 * This method checks whether the logical characters
	 * are contained within the string/word.
	 * is contained within the word
	 * @param sub_string
	 * @return
	 */
	public boolean containsLogicalChars(ArrayList<String> logical_chars)
	{
		return (containsAllLogicalChars(logical_chars));
	}
	
	
	/**
	 * This method checks whether *ALL* the logical characters
	 * are contained within the string/word.
	 * is contained within the word
	 * @param sub_string
	 * @return
	 */
	public boolean containsAllLogicalChars(ArrayList<String> logical_chars)
	{
		int count = 0;
		for(int i = 0; i < getLength(); i++){
			for(int j = 0; j < logical_chars.size(); j++){
				if(logicalChars.get(i).equals(logical_chars.get(j))){
					count++;
				}
			}
		}
		return (count == logical_chars.size());
	}
	
	/**
	 * This method checks whether *ALL* the logical characters
	 * in the sequence specified is in the word
	 * @param sub_string
	 * @return
	 */
	public boolean containsLogicalCharSequence(ArrayList<String> logical_chars)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < logical_chars.size(); i++){
			sb.append(logical_chars.get(i));
		}
		String tmp = sb.toString();
		
		return containsString(tmp);
	}
	
	/**
	 * This method checks whether a word can be made out of the original word
	 * example:  original word = POST;   a_word = POT
	 * @param a_word
	 * @return
	 */
	public boolean canMakeWord(String a_word)
	{
		
		//parse the a_word into logical characters
		ArrayList<String> lc = new ArrayList<String>();
		char[] tmp = a_word.toCharArray();
		for(int i = 0; i < tmp.length; i++){
			String s = String.valueOf(tmp[i]);
			lc.add(s);
		}
		ArrayList<String> origlc = new ArrayList<String>();
		char[] tmp1 = word.toCharArray();
		for(int i = 0; i < tmp1.length; i++){
			String s = String.valueOf(tmp1[i]);
			origlc.add(s);
		}
		
		for(int j = 0; j<tmp.length; j++){
			if(origlc.contains(lc.get(j))){
				origlc.remove(lc.get(j));
			}else{
				return false;
			}
		}
		return true;

	}
	
	/**
	 * This method checks whether all the words in the collection
	 * can be made out of the original word
	 * example:  original word = POST;   a_word = POT; STOP; TOP; SOP
	 * @param a_word
	 * @return
	 */
	public boolean canMakeAllWords(ArrayList<String> some_words)
	{
		// same as above method 
		// but works on the entire collection
		for(int j = 0; j<some_words.size(); j++){
			if(!canMakeWord(some_words.get(j))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * returns true if the word contains the space
	 * @return
	 */
	public boolean containsSpace()
	{
		return word.contains(" ");
	};
	
	/**
	 * returns true if the word is a palimdrome
	 * @return
	 */
	public boolean isPalindrome()
	{
		int count = logicalChars.size() - 1;
		for(int i = 0; i < logicalChars.size(); i++){
			if(!logicalChars.get(i).equals(logicalChars.get(count))){
				return false;
			}else{
				count--;
			}
		}
		return true;
	}
	
	/**
	 * returns true if the word_2 is an anagram of the word
	 * @return
	 */
	public boolean isAnagram(String word_2)
	{
		ArrayList<String> newlc = new ArrayList<String>();

		char[] tmp = word_2.toCharArray();
		char[] tmp2 = word.toCharArray();
		if (tmp.length == tmp2.length){
			for(int i = 0; i < tmp.length; i++){
				if(!containsString(String.valueOf(tmp[i]))){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * returns true if the logical_chars are contained with in the word
	 * @return
	 */
	
	public boolean isAnagram(ArrayList<String>  logical_chars)
	{
		if(logical_chars.size() == logicalChars.size()){
			return containsAllLogicalChars(logical_chars);
		} else {
			return false;
		}
	}
	
	

	
	// String manipulation methods
	/**
	 * strips of leading and trailing spaces
	 * @return
	 */
	public String trim()
	{
		return word.trim();
	}
	
	/**
	 * strips of all spaces in the word
	 * @return
	 */
	public String stripSpaces()
	{
		StringBuilder sb = new StringBuilder(); 
		char[] lc = word.toCharArray();
		for(int i = 0; i < lc.length; i++){
			Character c = lc[i];
			if(!Character.isSpaceChar(c)){
				sb.append(lc[i]);
			}
		}
		return sb.toString();
	}
	
	/**
	 * ?? should we be stripping spaces too or not??
	 * strips of all special characters and symbols from the word
	 * @return
	 */
	public String stripAllSymbols()
	{
		StringBuilder sb = new StringBuilder(); 
		ArrayList<String> orig = new ArrayList<String>(logicalChars);
		for(int i = 0; i < orig.size(); i++){
			String s = orig.get(i);
			if(s.length() == 1){
				Character c = s.charAt(0);
				if(Character.isLetterOrDigit(c)){
					sb.append(s);
				}
			}else{
				sb.append(s);
			}
		}
		return sb.toString();
	}
	
	/**
	 * Reverse the word and returns a new word
	 * @return
	 */
	public String reverse()
	{
		ArrayList<String> reverse = new ArrayList<String>();
		for(int i = logicalChars.size()-1; i >= 0; i--){
			reverse.add(logicalChars.get(i));
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < reverse.size(); i++){
			sb.append(reverse.get(i));
		}
		return sb.toString();
	}; 
	
	/**
	 * Replaces a specific sub-string with a substitute_string
	 * if the sub-string is not found, it does nothing
	 * It does not disturb the original string
	 * It returns a new string
	 * @return
	 */
	public String replace(String sub_string, String substitute_string)
	{
		String newWord = new String(word);
		if(containsString(sub_string)){
			newWord = newWord.replace(sub_string, substitute_string);
		}
		return newWord;
	}
	
	/**
	 * Add a logical character at the specified index
	 * It does not disturb the original string
	 * It returns a new string
	 * @return
	 */
	public String addCharacterAt(int index, String a_logical_char)
	{
		ArrayList<String> lc = new ArrayList<String>(logicalChars);
		lc.add(index, a_logical_char);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lc.size(); i++){
			sb.append(lc.get(i));
		}
		
		return sb.toString();
	}
	
	/**
	 * Add a logical character at the end of the word
	 * It does not disturb the original string
	 * It returns a new string
	 * @return
	 */
	public String addCharacterAtEnd(String a_logical_char)
	{
		ArrayList<String> lc = new ArrayList<String>(logicalChars);
		lc.add(a_logical_char);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lc.size(); i++){
			sb.append(lc.get(i));
		}
		
		return sb.toString();
	}
	
	/**
	 * Compares the given word with the original word
	 * If there is a match on any logical character, it returns true
	 * @return
	 */
	public boolean isIntersecting(String word_2)
	{
		for(int i = 0; i < logicalChars.size(); i++){
			if(word_2.contains(logicalChars.get(i))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Compares the given word with the original word
	 * And returns the count of matches on the logical characters
	 * @return
	 */
	public int getIntersectingRank(String word_2)
	{
		int count = 0;
		for(int i = 0; i < logicalChars.size(); i++){
			if(word_2.contains(logicalChars.get(i))){
				count++;
			}
		}
		return count;
	}
	
	
	/**
	 * This method gets a logical character at the specified index
	 * @param index
	 * @return
	 */
	public String logicalCharAt(int index)
	{
		return logicalChars.get(index);
	}
	
	/**
	 * This method gets a unicode code point at the specified index
	 * @param index
	 * @return
	 */ 
	public int	codePointAt(int index)
	{
		
		return (int)word.charAt(index);
	}
	
	// Returns the position at which the first logical character is appearing in the string
	
	/**
	 * This method returns the index at which the logical character is appearing
	 * It returns the first appearance of the logical character
	 * @param index
	 * @return
	 * @throws Exception 
	 */ 
	public int indexOf(String logical_char) throws Exception
	{
		int counter = 0;
		for(int i = 0; i < logicalChars.size(); i++){
			if(logical_char.equals(logicalChars.get(i))){
				counter++;
				return i;
			}
		}
		if(counter == 0){
			System.out.println("Character not in Word");
		}
		return -1;
	}
	
	/**
	 * This method compares two strings lexicographically.
	 * It is simplay a wrapper on java compareTo
	 * @param word_2
	 * @return
	 */
	public int	compareTo(String word_2)
	{
		return word.compareTo(word_2);
	}
	
	
	/**
	 * This method compares two strings lexicographically, ignoring case differences.
	 * It is simplay a wrapper on java compareTo
	 * @param word_2
	 * @return
	 */
	public int	compareToIgnoreCase(String word_2)
	{
		return word.compareToIgnoreCase(word_2);
	}
	
	/**
	 * This method takes one collection and returns another randomized collection
	 * of string (or logical characters)
	 * @param some_strings
	 * @return
	 */
	public ArrayList<String> randomize(ArrayList<String> some_strings)
	{
		ArrayList<String> random = new ArrayList<String>();
		int length = some_strings.size();
		Random r = new Random();
		for(int i = 0; i < length; i++){
			int j = r.nextInt(some_strings.size());
			random.add(some_strings.get(j));
			some_strings.remove(j);
		}
		return random;
	}
	
	/**
	 * This method splits the word into a 2-dimensional matrix
	 * based on the number of columns
	 * spaces are included and any empty at the end are null
	 * @param no_of_columns
	 * @return
	 */
	public String[][]  splitWord(int no_of_columns)
	{
		int rows = 0;
		if(logicalChars.size() % no_of_columns > 0){
			rows = (int)(logicalChars.size() / no_of_columns)+1;
		} else{
			rows = (int)(logicalChars.size() / no_of_columns);
		}
		
		String[][] split = new String[rows][no_of_columns];
		int count = 0;
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < no_of_columns; j++){
				if(count < logicalChars.size()){
					split[i][j] = logicalChars.get(0 + count);
					count++;
				}else{
					break;
				}
			}
		}
		return split;
	}
	
	/**
	 * Returns the string representation of WordProcessor
	 * Basically, prints the word and logicalChars
	 */
	public String toString()
	{
		String s = "word = "+ word + "; Logical characters = " + logicalChars;
		return s;
	}
	
	/**
	 * compares two strings; wrapper on the java method
	 */
	public boolean equals(String word_2)
	{
		return word.equals(word_2);
	}
	
	/**
	 * compares two strings after reversing the original word
	 */
	public boolean reverseEquals(String word_2)
	{
		String s = reverse();
		if(s.equals(word_2)){
			return true;
		}
		return false;
	}

	/**
	 * helper method to match telugu wordprocessor methods
	 * English this is same as length
	 * @return int
	 */
	public int getWordStrength() {
		return this.getLength();
	}
	
	/**
	 * helper method to shuffle all characters in the word
	 * @return ArrayList<String>
	 */
	public ArrayList<String> shuffleChars(){
		ArrayList<String> chars = new ArrayList<String>();
		chars.addAll(logicalChars);
		Collections.shuffle(chars);
		return chars;
	}
	
	
	/**
	 * Broke Testing into another class, Testing.java
	 * This is the main method for testing all of the above methods
	 */
	

}
