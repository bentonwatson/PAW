package core;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import paw.Config;

/**
 * This class manages the collection of Big Words
 * These Words are supplied through an input file.
 * TODO / FUTURE: input file can be substituted with a SQL string
 * @author srj
 *
 */

public class BigWordCollection 
{    
	    // FUTURE: We can serialize it for a later/faster retrieval 
	    // this is the single collection we hold in memory
		private ArrayList<BigWord>  bigWordsList = new ArrayList<BigWord>();
		
		// Another collection - hashtable for a faster retrieval of the BigWord based on the key
		private Hashtable<String, BigWord>  bigWordsIDTable = new Hashtable<String,BigWord>();
			
		// Another collection - hashtable for a faster retrieval of the BigWord based on the key
		private Hashtable<String, ArrayList<BigWord>>  bigWordsTopicsTable = new Hashtable<String,ArrayList<BigWord>>();
		
		/**
		 * Default constructor calls the first overloaded constructor with the file path
		 */
		public BigWordCollection()
		{
			this(Config.INPUT_FILE);
		}

		
		/**
		 * A constructor that loads test data from a file path that is provided
		 * @param a_file_name is the file path to the test data file.
		 */
		public BigWordCollection(String a_file_name)
		{			
			// Create the bigWordsList first
			try 
			{	
				processBigWordsInputFile(a_file_name);
			} 
			catch (IOException e) 
			{
				System.out.println("There was an error reading or opening the file. Perhaps the file is empty or the path is bad.");
				System.exit(0);
			}
			
			// Then process the bigWordsList to create other collections as needed
			makeAllCollections();
		}
	
		
	/**
	 * if the array list is provided directly, use this constructor
	 * This also creates all the other collections needed
	 */
	public BigWordCollection(ArrayList<BigWord> an_array_list)
	{
		bigWordsList = an_array_list;
		makeAllCollections();
	}

		
		/**
	 * Reads lines from a text file one by one and sends them to the addBigWord method. Catches a BigWordAdditionException if one is thrown,
	 * and exits the program as per instructions.  It is possible however, to not exit and skip to the next line. This is a one line code change
	 * that involves removing the exit statement. reads UTF-8
	 * @param filename is a string that represents the path of the file to read
	 * @throws IOException is thrown if the file fails to load
	 */
	private void processBigWordsInputFile(String filename) throws IOException 
	{
		String line_read = "";
		BufferedReader reader = new BufferedReader(
			       new InputStreamReader(
			                  new FileInputStream(filename), "UTF-8"));
		
		// igore the first line. we don't need the header line
		line_read = reader.readLine(); 
		
		//System.out.println(line_read);
		
		int lineNumber = 0;
		while ((line_read = reader.readLine()) != null) 
		{
			//System.out.println(line_read);
			
			lineNumber++;
			try 
			{
				addBigWord(line_read);
			} 
			catch (BigWordAdditionException e) 
			{
				System.out.println(e.getMessage() + "Exiting with error code 0 at test data line # " + lineNumber);
				System.exit(0);
			}
		}
		reader.close();
	}
	
	/**
	 * Adds a BigWord to the collection 
	 * @param a_big_word is added to the collection
	 * */
	public void addBigWord(BigWord a_big_word) 
	{
		bigWordsList.add(a_big_word);
	}
	
	/** NEW
	 * For getting a Big Word based on index 
	 * @param a_big_word is added to the collection
	 * */
	public BigWord getBigWord(int index) 
	{
		return bigWordsList.get(index);
	}
	

		/**
		 * Adds a BigWord to the collection 
		 * @param a_line is a single line of text with 16 values separated by 15 commas with values thirteen and fourteen being space delimited lists
		 * @throws BigWordAdditionException is thrown if the file is empty, a line has other than 15 commas, or the file contains duplicate items
		 */
		private void addBigWord(String a_line) throws BigWordAdditionException
		{
			if(a_line.equals(""))
			{
				throw new BigWordAdditionException("Line is empty. Check the empty lines in the file!");
			}
			
			String[] list = a_line.split(Config.DELIMETER);
			List<String> tokens = Arrays.asList(list);
			
		
			// TODO: For now, this code can not handle unstructured data
			// it is required to have delimiters
			if(tokens.size() != Config.MAX_ITEMS_PER_LINE)
			{
				System.out.println("ERROR: Not enough separators | " + a_line);
				return;
			}
			
			// here are the fields in the input text file
			String token1 = tokens.get(0).trim(); // ID
			String token2 = tokens.get(1).trim(); // topic
			String token3 = tokens.get(2).trim(); // telugu
			String token4 = tokens.get(3).trim(); // english
			String token5 = tokens.get(4).trim(); // clue
			String token6 = tokens.get(5).trim(); // image
			String token7 = tokens.get(6).trim(); // sound
			
			//Now create the BigWord
			BigWord new_BigWord = new BigWord(token1,token2,token3,token4,token5,
					                 token6,token7);

			
		    //	System.out.println(new_BigWord);
			
			// add the BigWord to the BigWord collection
			// Once it is fully created, create the other tables as needed
			// FUTURE: We can optimize it so that all the collections are created in one pass
			bigWordsList.add(new_BigWord);
			
		}
		/**
		 * Returning the Array List / All BigWords of a BigWordCollection
		 * @return
		 */
		
		public ArrayList<BigWord> getAllBigWords()
		{
			return bigWordsList;
		}
	
		/**
		 * This method creates all the needed collections
		 * to be used in different games
		 * 
		 * If any other specific collections are needed, 
		 * those can be built here.
		 */
		private void makeAllCollections()
		{
			makeIDHashtable();
			makeTopicHashtable();
		}
		
		/**
		 * This method makes a hashtable from the array list
		 * Key = Topic; value = Big Word
		 */
		private void makeIDHashtable()
		{
			// Now create our hashtable; 
			// One time upfront cost for faster retrievals later on
			for (int i = 0; i < bigWordsList.size(); i++)
			{
				BigWord big_word = bigWordsList.get(i);
				String ID_str = big_word.getID();
				bigWordsIDTable.put(ID_str,  big_word);
			}
		}
		
		/**
		 * This method makes a hashtable from the array list
		 * Key = Topic; value = Big Word Collection
		 */
		private void makeTopicHashtable()
		{
			// Now create our hashtable; 
			// One time upfront cost for faster retrievals later on
			for (int i = 0; i < bigWordsList.size(); i++)
			{
				BigWord big_word = bigWordsList.get(i);
				String topic_str = big_word.getTopic();
				
				// check whether the key exists
				boolean key_exists = bigWordsTopicsTable.containsKey(topic_str);
				
				// if it exists, get the value and add the new Big Word to the collection
				if (key_exists)
				{
					ArrayList<BigWord> temp = bigWordsTopicsTable.get(topic_str);
					temp.add(big_word);
					bigWordsTopicsTable.put(topic_str, temp);
				}
				// if the key doesn't exist, then we need to create a new collection 
				// and then add the word to that new collection
				else {
					ArrayList<BigWord> temp = new ArrayList<BigWord>();
					temp.add(big_word);
					bigWordsTopicsTable.put(topic_str, temp);
				}  // end else
			} // end for
			
//			System.out.println("topic hashtable size " + bigWordsTopicsTable.size());
		} // end method makeTopicHashtable
		
	
		
		/**
		 * Retrieve all Big Words based on key word search
		 */
		
		public BigWordCollection getBigWordCollectionByKeyWord(String a_key)
		{
			ArrayList<BigWord> mini_collection = new ArrayList<BigWord>(); 
			for (int i = 0; i < bigWordsList.size(); i++)
			{
				BigWord b = bigWordsList.get(i);
				
				boolean match_found = b.toString().toLowerCase().contains(a_key.toLowerCase());
				if (match_found)
				{
					mini_collection.add(b);
				}

			}
			return new BigWordCollection(mini_collection);
		}
		
	
		/**
		 * Retrieve the BigWord from hashtable based on the key
		 */
		
		public BigWord getBigWordByKey(String an_ID)
		{
			return bigWordsIDTable.get(an_ID);
		}
	
		/**
		 * get the size of the Big Word Collection
		 */
		
		public int size() 
		{
			return bigWordsList.size();
		}
	
		/**
		 * Returns whether the Big Word Collection is empty
		 */
		
		public boolean isEmpty() 
		{
			return (bigWordsList.size()==0);
		}
		
		
		/** For printing the entire collection
		 */

		public String toString( )
		{
			System.out.println("Size of the collection = " + bigWordsList.size());
			System.out.println("==============================");
			System.out.println(bigWordsList);
			return "";
		}
		
		
		
		
		
		
		//=========================================================================
		//********** To be done by Students for SE Assignment 4 (Group Project)
		
		
		/**
		 * Returns the Big Word Collection based on the Topic
		 * If some_topic is "Any", then it returns the entire collection
		 * If some_topic is null or "", then it returns a null collection.
		 */
		public BigWordCollection getBigWordCollectionByTopic(String some_topic)
		{
			if(some_topic.equalsIgnoreCase("any")){
				
				return this;
			} 
			return new BigWordCollection(bigWordsTopicsTable.get(some_topic));
		}
		
		/**
		 * Returns the Big Word Collection based on the length of the word
		 * This method matches the exact length. All other words are discarded
		 */
		@SuppressWarnings("unused")
		public BigWordCollection getBigWordCollectionByWordLength(int a_length)
		{
			ArrayList<BigWord> bwList = new ArrayList<BigWord>();
			String word = null;
			WordProcessor wp = null;
			for(int i = 0; i < bigWordsList.size(); i++){
				BigWord bw = bigWordsList.get(i);
				if(Config.DEFAULTLANGUAGE == 0){
					word = bw.getEnglish();
					wp = new WordProcessor(word);
				}else if(Config.DEFAULTLANGUAGE == 1){
					word = bw.getTelugu();
					wp = new te.TeluguWordProcessor(word);
				}
				if(wp.getLength() == a_length){
					bwList.add(bw);
				}
			}
			BigWordCollection collection = new BigWordCollection(bwList);
			
			return new BigWordCollection(bwList);
		}
		
		/**
		 * Returns the Big Word Collection based on the length (min and max) of the word
		 * This method matches all the strings between MIN and MAX (including)
		 */
		@SuppressWarnings("unused")
		public BigWordCollection getBigWordCollectionByWordLength(int min, int max)
		{
			ArrayList<BigWord> bwList = new ArrayList<BigWord>();
			String word = null;
			WordProcessor wp = null;
			for(int i = 0; i < bigWordsList.size(); i++){
				BigWord bw = bigWordsList.get(i);
				if(Config.DEFAULTLANGUAGE == 0){
					word = bw.getEnglish();
					wp = new WordProcessor(word);
				}else if(Config.DEFAULTLANGUAGE == 1){
					word = bw.getTelugu();
					wp = new te.TeluguWordProcessor(word);
				}
				if((wp.getLength() >= min || min == 0) && (wp.getLength() <= max || max == 0)){
					bwList.add(bw);
				}
			}
			return new BigWordCollection(bwList);
		}
		

		/**
		 * Returns the Big Word Collection based on the strength of the Word
		 * For English, strength = length
		 * For Telugu, different algorithm is already provided
		 * */
		@SuppressWarnings("unused")
		public BigWordCollection getBigWordCollectionByWordStrength(int strength)
		{
			ArrayList<BigWord> bwList = new ArrayList<BigWord>();
			String word = null;
			WordProcessor wp = null;
			for(int i = 0; i < bigWordsList.size(); i++){
				BigWord bw = bigWordsList.get(i);
				if(Config.DEFAULTLANGUAGE == 0){
					word = bw.getEnglish();
					wp = new WordProcessor(word);
				}else if(Config.DEFAULTLANGUAGE == 1){
					word = bw.getTelugu();
					wp = new te.TeluguWordProcessor(word);
				}
				if(wp.getWordStrength() == strength){
					bwList.add(bw);
				}
			}	
			return new BigWordCollection(bwList);
		}
		
		/**
		 * Returns the Big Word Collection based on the strength of the Word
		 * It returns all the words between min and max strengths
		 * For English, strength = length
		 * For Telugu, different algorithm is already provided
		 * */
		@SuppressWarnings("unused")
		public BigWordCollection getBigWordCollectionByWordStrength(int min, int max)
		{
			ArrayList<BigWord> bwList = new ArrayList<BigWord>();
			String word = null;
			WordProcessor wp = null;
			for(int i = 0; i < bigWordsList.size(); i++){
				BigWord bw = bigWordsList.get(i);
				if(Config.DEFAULTLANGUAGE == 0){
					word = bw.getEnglish();
					wp = new WordProcessor(word);
				}else if(Config.DEFAULTLANGUAGE == 1){
					word = bw.getTelugu();
					wp = new te.TeluguWordProcessor(word);
				}
				if((wp.getWordStrength() >= min || min == 0) && (wp.getWordStrength() <= max || max == 0)){
					bwList.add(bw);
				}
			}
			return new BigWordCollection(bwList);
		}
		
		/**
		 * Returns the Big Word Collection based on these search parameters
		 * 		topic
		 * 		length of the word
		 * 		word types
		 * Any of these parameters can be null or empty
		 * */
		public BigWordCollection getBigWordCollectionByCriteria
		                 (String a_topic, int min_len, int max_len, int min_strength, int max_strength)
		{
			ArrayList<BigWord> returnList = new ArrayList<BigWord>();
			BigWordCollection bwcTopics = getBigWordCollectionByTopic(a_topic);
//			System.out.println("by topic size - " + bwcTopics.size());
			BigWordCollection bwcLength = bwcTopics.getBigWordCollectionByWordLength(min_len, max_len);
//			System.out.println("by length size - " + bwcLength.size());
			BigWordCollection bwcStrength = bwcLength.getBigWordCollectionByWordStrength(min_strength, max_strength);
//			System.out.println("by strength size - " + bwcStrength.size());
			
			return bwcStrength;
		}
		
		/**
		 * Returns the Big Word Collection based on the level
		 * There are only 3 levels - Level 1, Level 2 and Level 3
		 * level 1: (Topic = Any; Strength = 1; Min/Max Length = 2/10)
		 * level 2: (Topic = Any; Strength = 2; Min/Max Length = 2/10))
		 * level 3: (Topic = Any; Min/Max Strength = 3/10; ; Min/Max Length = 2/10)
		 * * */
		public BigWordCollection getBigWordCollectionByLevel(int a_level)
		{
			switch (a_level) {
			case 1: return getBigWordCollectionByCriteria("Any", 2, 10, 1, 1);
			case 2: return getBigWordCollectionByCriteria("Any", 2, 10, 2, 2);
			case 3: return getBigWordCollectionByCriteria("Any", 2, 10, 3, 10);
			default:
				break;
			}
			return null;
		}
		/**
		 * Checks whether Big Word Collection has any duplicate IDs
		 * @return true if it does contain duplicates, else false
		 */
		public boolean containsDuplicateIDs()
		{
			
			if(bigWordsIDTable.size() != bigWordsList.size()){
				return true;
			}
           return false;
        }
		
		/**
		 * Checks whether Big Word Collection has any duplicate Telugu words
		 */
		public boolean containsDuplicateTeluguWords()
		{
			BWCIterator iterator = new BWCIterator(this);
			int count = this.size();
			ArrayList<String> words = new ArrayList<String>();
			for(int i = 0; i < count; i++){
				if(words.contains(iterator.getCurrent().getTelugu())){
					return true;
				}else{
					words.add(iterator.getCurrent().getTelugu());
				}
				if(i < count-1){
					iterator.next();
				}
				
			}
            return false;
        }
		
		/**
		 * Checks whether Big Word Collection has any duplicate English words
		 */
		public boolean containsDuplicateEnglishWords()
		{
			BWCIterator iterator = new BWCIterator(this);
			int count = this.size();
			ArrayList<String> words = new ArrayList<String>();
			for(int i = 0; i < count; i++){
				if(words.contains(iterator.getCurrent().getEnglish())){
					return true;
				}else{
					words.add(iterator.getCurrent().getEnglish());
				}
				if(i < count-1){
					iterator.next();
				}
				
			}
			return false;
		}
		
		/**
		 * Checks whether Big Word Collection has any duplicate Clues
		 */
		public boolean containsDuplicateClues()
		{
			BWCIterator iterator = new BWCIterator(this);
			int count = this.size();
			ArrayList<String> words = new ArrayList<String>();
			for(int i = 0; i < count; i++){
				if(words.contains(iterator.getCurrent().getClue())){
					return true;
				}else{
					words.add(iterator.getCurrent().getClue());
				}
				if(i < count-1){
					iterator.next();
				}
			}
            return false;
        }
		
		/**
		 * Gets the Big Word Collection where the Big Word has an image file associated with it
		 * */
		public BigWordCollection getBigWordCollectionWithImages()
		{
			ArrayList<BigWord> bwList = new ArrayList<BigWord>();
			for(int i = 0; i < bigWordsList.size(); i++){
				if(bigWordsList.get(i).hasImage()){
					bwList.add(bigWordsList.get(i));
				}
			}
			return new BigWordCollection(bwList);
        }
		
		/**
		 * Gets the Big Word Collection where the Big Word has a sound file associated with it
		 * */
		public BigWordCollection getBigWordCollectionWithSounds()
		{
			ArrayList<BigWord> bwList = new ArrayList<BigWord>();
			for(int i = 0; i < bigWordsList.size(); i++){
				if(bigWordsList.get(i).hasSound()){
					bwList.add(bigWordsList.get(i));
				}
			}
			return new BigWordCollection(bwList);
		}

		public Hashtable<String, ArrayList<BigWord>> getBigWordsTopicsTable() {
			return bigWordsTopicsTable;
		}

		
		
		//***********************************************************
		
		
		/*
		 * Test routine for BigWordCollection
		 */
//		public static void main(String[] args) throws Exception
//		{
//			System.out.println("Opening ..." + Config.INPUT_FILE);
//			
//					
//			BigWordCollection x = new BigWordCollection();
//			System.out.println(x);
//		}
}
