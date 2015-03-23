package paw;

import java.awt.Color;
import java.awt.Font;

public class Config {
	
	
	// Tab and Background Colors
		public static Color WELCOME_PANEL_BG_COLOR = Color.yellow;
		public static Color GENERATE_PANEL_BG_COLOR =Color.white;
		public static Color PLAY_PANEL_BG_COLOR = Color.yellow;
		public static Color CONFIG_PANEL_BG_COLOR = Color.magenta; 
		
		public static String APP_TITLE = "Pick and Assemble Words";
		public static String LOGO_FILE = "logo.jpg";
		public static String WELCOME_TITLE = "Welcome to Pick and Assemble Words";
		
		public static String WELCOME_MSG = "Choose your Mode";

		
	// font size for Telugu
	public static final float FONTSIZETELUGU = 26.0f;
	// Default Font Size
	public static final float FONTSIZEDEFAULT = 18.0f;
	// Languages to choose from
	public enum languages {
		English, Telugu
	}
	// requires a language class as well as editing a few lines of code in the
	// gui to incorporate more languages.
	public static final String[] LANGUAGES = { "English", "Telugu" };
	
	//default language is its position in the LANGUAGES[] (0 = english, 1 = telugu);
	public static final int DEFAULTLANGUAGE = 0;
	
	//works
//	public static String directoryForHTMLFiles = "/Users/";
	//above did not work...
	public static String directoryForHTMLFiles = System.getProperty("user.home") + "\\PAW\\";
	
	//works
	public static boolean createHTML = false;
	
	//0 = admin, 1 = user
	public static final String[] MODES = { "admin", "user" };
	
	//default mode is its position in the MODES[] (0 = admin, 1 = user);
	public static int DEFAULTMODE = 0;
	
	//the file to record completed games
	public static String progressFile = System.getProperty("user.home") + "\\PAW\\PAW_Progress.txt";

	// user mode gets game to play from this set
	public static final String GAME_SET = "src\\gameSet.txt";	
	
	// For Reading the input words to create new games
	public static final String DELIMETER = "\\|";
	public static final int MAX_ITEMS_PER_LINE = 7;
	
	//this is the file for BigWordCollection default
	public static final String INPUT_FILE = "src\\input_words.txt";	
	/*
	 * English Options
	 */
	// Help Message
		public static final String enHELPMESSAGE = "Help message..."; //TODO

		// Win Message
		public static final String enWINMESSAGE = "             YOU DID IT !!! GREAT JOB!!!"
				+ "\n Click on 'Start New Game' if you would like to play another game. ";
		
		public static final String enLOSEMESSAGE = "             YOU DID NOT FINISH!!";
		
		public static Font enTextFont = Font.getFont(Font.SANS_SERIF);
		
		public static String enWordsFileName = "src/EnglishWords.txt";
		
		public static String enAdminWordsFileName = "src/EN/adminInput.txt";
		
//		public static String enWordsFilePath = System.getProperty("user.home") + "\\7LW\\EN\\";
	
	/*
	 * Telugu options
	 */
	// Help Message
		public static final String teHELPMESSAGE = " in Telugu  Help Message... "; //TODO

		// Win Message
		public static final String teWINMESSAGE = "   in Telugu          YOU DID IT !!! GREAT JOB!!!"
				+ "\n Click on 'Start New Game' if you would like to play another game. ";
		
		public static String teTextFont = "src/TE/Gidugu.ttf";

		public static String teWordsFileName = "src/TeluguWords.txt";

//		public static String teWordsFilePath = System.getProperty("user.home") + "\\7LW\\TE\\";
}
