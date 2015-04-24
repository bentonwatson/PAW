package paw;

import java.awt.Color;

public class Config {
	
	public static Color WELCOME_PANEL_BG_COLOR = Color.lightGray;
	public static Color WELCOME_PANEL_BUTTONS = Color.pink;
	public static Color GENERATE_PANEL_BG_COLOR =Color.white;
	public static Color PLAY_PANEL_BG_COLOR = Color.yellow;
	public static Color PLAY_TILE_COLOR = Color.yellow; // avoid RED, GREEN, and WHITE
	public static Color CONFIG_PANEL_BG_COLOR = Color.lightGray; 
	public static Color CONFIG_PANEL_BUTTONS = Color.pink;
	
	public static String APP_TITLE = "Pick and Assemble Words";
	public static String LOGO_FILE = System.getProperty("user.home") + "\\PAW\\logo.JPG";
	public static String WELCOME_TITLE = "Welcome to Pick and Assemble Words";
	
	public static String WELCOME_MSG = "Choose your Level and Select Play Tab to get started.";
	
	public static String ADMIN_WELCOME = "Levels chosen here pre-populate a game from 'Any' topic."
											+" Select Config tab to refine the results,"
											+ "Generate tab to see the results,"
											+ " or Play tab to play the results.";
		
//	// font size for Telugu
	public static final float FONTSIZETELUGU = 26.0f;

	// some gui code will need additions to incorporate more languages.
	//default language  ("en" = english, "te" = telugu); change input default when language changes
	public static final String DEFAULTLANGUAGE = "te";
	
	//this is the file for BigWordCollection default to English
	public static final String INPUT_FILE = System.getProperty("user.home") + "\\PAW\\en\\input_words.txt";
	//this is the file for BigWordCollection default to Telugu
//	public static final String INPUT_FILE = System.getProperty("user.home") + "\\PAW\\te\\input_words.txt";
	
	// When using bigwordcollection
	public static final String DELIMETER = "\\|";
	public static final int MAX_ITEMS_PER_LINE = 7;
	
	// modes may be "user" or "admin"
	public static String DEFAULTMODE = "admin";
	
	//sets max number of words er game
	public static int maxNumberWords = 10;
	
	//default game configurations String[]  {0 - topic, 1- level, 2 - wordLength, 3 - minStrength, 4 - maxStrength, 
	//								5 - duplicates, 6 - charOrder, 7 - allWords, 8 - numWords}
	public static String[] defaultSettings = new String[]{"Any", "1", "4", "1", "7", "true", "true", "false", "5"};
	
	//default directory  and images for creating and storing HTML games
	public static String directoryForHTMLFiles = System.getProperty("user.home") + "\\PAW\\HTMLFiles\\";
	public static String HTML_BG_IMAGE_ONE = System.getProperty("user.home") + "\\PAW\\HTMLFiles\\back_ground.jpg";
	public static String HTML_BG_IMAGE_TWO = System.getProperty("user.home") + "\\PAW\\HTMLFiles\\back_ground_2.jpg";
	public static String HTML_LOGO_IMAGE_ONE = System.getProperty("user.home") + "\\PAW\\HTMLFiles\\logo_1.jpg";
	
	//the file to record completed games
	public static String progressFile = System.getProperty("user.home") + "\\PAW\\PAW_Progress.txt";
//	public static String progressFile = "src\\PAW_Progress.txt";

	// user mode gets game to play from this set
	public static final String EN_GAME_SET = System.getProperty("user.home") + "\\PAW\\en\\enGameSet.txt";
	public static final String TE_GAME_SET = System.getProperty("user.home") + "\\PAW\\te\\teGameSet.txt";
//	public static final String GAME_SET = "src\\enGameSet.txt";	
	/*
	 * English Options
	 */
	// Help Message
		public static final String enHELPMESSAGE = "Help message..."; //TODO

		// Win Message
		public static final String enWINMESSAGE = "             YOU DID IT !!! GREAT JOB!!!"
				+ "\n Would you like to play again?. ";
		
		public static final String enLOSEMESSAGE = "             YOU DID NOT FINISH!!";

		public static String enAdminWordsFileName = "src/EN/adminInput.txt";
	
	/*
	 * Telugu options
	 */
	// Help Message
		public static final String teHELPMESSAGE = " in Telugu  Help Message... "; //TODO

		// Win Message
		public static final String teWINMESSAGE = "   in Telugu          YOU DID IT !!! GREAT JOB!!!"
				+ "\n Click on 'Start New Game' if you would like to play another game. ";
		
		public static String teTextFont = "src/TE/Gidugu.ttf";

}
