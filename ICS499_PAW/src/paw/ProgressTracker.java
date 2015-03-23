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

import core.GameCollection;
import core.ProgressTrackerInterface;


public class ProgressTracker  implements ProgressTrackerInterface{
	private boolean completed;
	private String currentGameId;
	private String playDate;
	private String timeElapsed;
	private GameCollection collection;
	private ArrayList<String> collectionIds;
	
	public ProgressTracker(){
		collection = new GameCollection();
		collectionIds = collection.getGameIds();
		readProgressFile();
	}
	
	
	@SuppressWarnings("resource")
	public void readProgressFile(){
		String sCurrentLine;
		ArrayList<String> playedGameIds = new ArrayList<String>();
		ArrayList<Boolean> completedGames = new ArrayList<Boolean>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(Config.progressFile));
			while ((sCurrentLine = br.readLine()) != null){
				playedGameIds.add(sCurrentLine.split(",")[0]);
				completedGames.add(Boolean.valueOf(sCurrentLine.split(",")[1]));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int lastIndex = playedGameIds.size() - 1;
		setPlayDate(new Date());
		if(lastIndex == -1 || playedGameIds.get(lastIndex).length() <= 1){
			currentGameId = collectionIds.get(0);
			completed = false;
			writeToFile();
		}else if (completedGames.get(lastIndex) == false){
			currentGameId = playedGameIds.get(lastIndex);
		}else if (completedGames.get(lastIndex) == true) {
			String tmp = playedGameIds.get(lastIndex);
			int tmpIndex = collectionIds.indexOf(tmp);
			if(tmpIndex == collectionIds.size() - 1){
				currentGameId = collectionIds.get(0);
			}else{
				currentGameId = collectionIds.get(tmpIndex + 1);
			}
			if (lastIndex < playedGameIds.size() - 1){
				currentGameId = playedGameIds.get(lastIndex + 1);
			}
			completed = false;
			writeToFile();
		}
	}

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
	
	public void setCompleted(boolean b){
		completed = b;
	}
	
	public String getCurrentGameId(){
		return currentGameId;
	}
	
	public void setPlayDate(Date date){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd"); 
		playDate = (dateFormat.format(date));
	}
	
	public void setTimeElapsed(String time){
		timeElapsed = time;
	}

}
