package paw;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.util.ArrayList;

import core.Game;

public class Config {
	

	// Tab and Background Colors
		public static Color WELCOME_PANEL_BG_COLOR = Color.lightGray;
		public static Color GENERATE_PANEL_BG_COLOR =Color.white;
		public static Color PLAY_PANEL_BG_COLOR = Color.yellow;
		public static Color CONFIG_PANEL_BG_COLOR = Color.pink; 
		
		public static String APP_TITLE = "Pick and Assemble Words";
		public static String LOGO_FILE = "logo.jpg";
		public static String WELCOME_TITLE = "Welcome to Pick and Assemble Words";
		
		public static String WELCOME_MSG = "Choose your Level and Select Play Tab to get started.";
		public static String ADMIN_WELCOME = "Levels chosen here pre-populate a game from 'Any' topic."
												+" Select Config tab to refine the results,"
												+ "Generate tab to see the results,"
												+ " or Play tab to play the results.";

		
//	// font size for Telugu
	public static final float FONTSIZETELUGU = 26.0f;

	// some gui code will need additions to incorporate more languages.
	//default language  (0 = english, 1 = telugu);
	public static final int DEFAULTLANGUAGE = 0;
	
	//works
//	public static String directoryForHTMLFiles = "/Users/";
	//above did not work...
	public static String directoryForHTMLFiles = System.getProperty("user.home") + "\\PAW\\";
	
	//works
	public static boolean createHTML = false;
	
	// modes may be "user" or "admin"
	public static String DEFAULTMODE = "user";
	
	//the file to record completed games
//	public static String progressFile = System.getProperty("user.home") + "\\PAW\\PAW_Progress.txt";
	public static String progressFile = "src\\PAW_Progress.txt";

	// user mode gets game to play from this set
	public static final String GAME_SET = "src\\enGameSet.txt";	
	
	//default game configurations
	public static String[] defaultSettings = new String[]{"Any", "1", "4", "4", "true", "true", "false", "5"};

	// When using bigwordcollection
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
