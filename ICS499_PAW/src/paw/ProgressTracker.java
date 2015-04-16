package paw;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import core.GCIterator;
import core.Game;
import core.GameCollection;
import core.ProgressTrackerInterface;


public class ProgressTracker  implements ProgressTrackerInterface{
	private String currentGameId;
	private boolean completed;
	private String playDate;
	private String timeElapsed;
	private GameCollection incompleteGames;
	private ArrayList<String> playedGameIds;
	
	/**
	 * default constructor 
	 */
	public ProgressTracker(){
		readProgressFile();
	}
	
	/**
	 * Method to return only unplayed games from the passed in collection
	 * @param gc
	 * @return GameCollection 
	 */
	public GameCollection removeCompletedGamesFromGameCollection(GameCollection gc) {
		ArrayList<Game> tmplist = new ArrayList<Game>();
		tmplist.addAll(gc.getAllGames());
		GameCollection games = new GameCollection(tmplist);
		if(gc.size() > 0){
			GCIterator iterator = new GCIterator(gc);
			iterator.start();
			if(playedGameIds.contains(iterator.getCurrent().getId())){
				games.removeGame(iterator.getCurrent());
			}
			while(iterator.hasNext()){
				if(playedGameIds.contains(iterator.getNext().getId())){
					games.removeGame(iterator.getCurrent());
				}
			}
		}
		incompleteGames = games;
		return incompleteGames;
	}
	
	/**
	 * reads and loads the played game ids arraylist
	 */
	@SuppressWarnings("resource")
	public void readProgressFile(){
		String sCurrentLine;
		playedGameIds = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(Config.progressFile));
			while ((sCurrentLine = br.readLine()) != null){
				playedGameIds.add(sCurrentLine.split(",")[0]);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * method to write info to file when a game is completed
	 * these must be called first
	 * setCompleted(), setPlayDate(), and setTimeElapsed()
	 * 
	 * call removeGameFromIncompleteCollection() after this one
	 */
	public void writeToFile(){
		try {
			FileWriter fw = new FileWriter(Config.progressFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(currentGameId +","+ completed +"," + playDate +","+ timeElapsed);
				bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getCurrentGameId(){
		return currentGameId;
	}
	
	public void setCurrentGameId(String id){
		currentGameId = id;
	}
	
	public void setCompleted(boolean b){
		completed = b;
	}
	
	public void setPlayDate(Date date){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd"); 
		playDate = (dateFormat.format(date));
	}
	
	public void setTimeElapsed(String time){
		timeElapsed = time;
	}
	
	/**
	 * method to remove a game form the incomplete collection
	 * called when a game is completed
	 * @param a_game
	 * @return true if a game was removed 
	 */
	public boolean removeGameFromIncompleteCollection(Game a_game){
		if(incompleteGames.size() > 0){
			incompleteGames.removeGame(a_game);
			return true;
		}
		return false;
	}
	
	public boolean allGameSetPlayed(){
		GameCollection gc = new GameCollection();
		if(gc.size() == playedGameIds.size()){
			return true;
		}
		return false;
	}
	
	public ArrayList<Integer> levelsNotCompleted(){
		ArrayList<Integer> levels = new ArrayList<Integer>();
		if(!allGameSetPlayed()){
			GameCollection gc = new GameCollection();
			this.removeCompletedGamesFromGameCollection(gc);
			ArrayList<Game> list = new ArrayList<Game>();
			list.addAll(gc.getAllGames());
			for(int i = 0; i < gc.size(); i++){
				if(!levels.contains(list.get(i).getLevel())){
					levels.add(list.get(i).getLevel());
				}
			}
		}
		return levels;
	}

}
