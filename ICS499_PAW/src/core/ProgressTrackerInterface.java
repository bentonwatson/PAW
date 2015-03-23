package core;
import java.util.ArrayList;
import java.util.Date;


/* 
 * Constructor gets the collection and collectionIds from the PuzzleCollection class
 * makes a call to readProgressFile() to get the starting currentGameId
 */

public interface ProgressTrackerInterface {
	boolean completed = false;
	String currentGameId = null;
	String playDate = null;
	String timeElapsed = null;
	GameCollection collection = null;
	ArrayList<String> collectionIds = null;
	
	/*
	 * retrieves information from the progress file
	 * sets the currentGameId 
	 */
	public void readProgressFile();

	/*
	 * send game information to the file
	 * which file is determined in Config
	 * game data is saved when game is started and when game is completed
	 */
	public void writeToFile();
	
	/*
	 * sets the status of the currentGameId
	 */
	public void setCompleted(boolean b);
	
	/*
	 * returns the currentGameId
	 * must be set within readProgressFile first
	 */
	public String getCurrentGameId();
	
	/*
	 * sets the playDate
	 */
	public void setPlayDate(Date date);
	
	/*
	 * sets the timeElapsed  
	 */
	public void setTimeElapsed(String time);
	
}
