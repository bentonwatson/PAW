package paw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import core.Game;

/**
 * 
 * @author Ben
 * 
 * When Save is selected on gui the game data is formatted and saved to the gameSet file
 * Dependent on data from Game Generator
 */
public class GameSaver {
	private int language = Config.DEFAULTLANGUAGE;
	private Game newGame;
	private String gameId;
	private boolean saveSuccessful;
	
	public GameSaver(Game a_game){
		newGame = a_game; //this game will NOT have an id assigned yet
		generateNewId();
		writeNewGame();
	}
	
	/**
	 * Method generates a new Id based on existing Ids in the gameSet
	 */
	public void generateNewId(){
		String id = "";

		BufferedReader br = null;
		try {
			String sCurrentLine;
			if(language == 0){
				br = new BufferedReader(new FileReader(Config.EN_GAME_SET));
			}else{
				br = new BufferedReader(new FileReader(Config.TE_GAME_SET));
			}
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.contains("ID")){
					id = sCurrentLine.split(" ")[1];
				}
			}
		} catch (IOException e) { e.printStackTrace(); } 
		if(!id.equals("")){
			int idSetup= (Integer.parseInt(id)+1);
			gameId = ("00"+ idSetup);
		}else{
			gameId = "001";
		}
	}
	 
	/**
	 * Method saves the new game to the gameset
	 * sets the success variable
	 */
	public void writeNewGame() {
		saveSuccessful = false;
		String path;
		if(language == 0){
			path = Config.EN_GAME_SET;  //  set the value to the game get folder by path
		}else{
			path = Config.TE_GAME_SET;  //  set the value to the game get folder by path
		}
		String title = newGame.getTitle();
		generateNewId();
		String level = newGame.getLevel()+"";
		String dup = String.valueOf(newGame.getDuplicate());
		String order = String.valueOf(newGame.getCharOrder());
		String wordList = formatWordList(newGame.getWordList());
		String columns = formatColumnData(newGame.getColumnData());
		String data="\n\r"+"ID: "+gameId+"\n\r"
				+ "Title:  "+ title+"\n\r"
				+ "Level: "+ level+ "\n\r"
				+ "Other: "+ dup + "," + order + "\n\r"
				+ "Words:   "+ wordList+"\n\r"
				+ columns
				+"-----------------------------------------\r";
		try {
			FileWriter fileWritter = new FileWriter(path,true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(data);
			bufferWritter.close();
			saveSuccessful = true;// true value when the data is saved. 

		} catch (IOException e) {
			e.printStackTrace();
		}	

	}
	/**
	 * Format the word list for writing to file
	 * @param list
	 * @return
	 */
	public String formatWordList(ArrayList<String> list){
		String input = "";
		for(int i = 0; i < list.size(); i++){
			input += list.get(i) + ",";
		}
		return input;
	}

	/**
	 * format the column data for writing to file
	 * @param input
	 * @return
	 */
	public String formatColumnData(ArrayList<ArrayList<String>> input){
		String data = "";
		for(int i = 0; i < input.size(); i++){
			data += "C: ";
			for(int k = 0; k < input.get(i).size(); k++){
				data += input.get(i).get(k) + ",";
			}
			data += "\n";
		}  
		return data;  
	}

	
	/**
	 * Method to acknowledge successful save
	 * @return
	 */
	public boolean isSuccessful(){
		//TODO
		return saveSuccessful;
	}

	/**
	 * main method to test the saver
	 */
//	public static void main(String[] args) {
//		GameGenerator gg = new GameGenerator("BodyParts", 1, 4, 4, false, false);
//		gg.chooseNumberOfWords(5);
//		Game game = gg.getNewGame();
//		System.out.println(game.toString());
//		
//		GameSaver gs = new GameSaver(game);
//		System.out.println(gs.isSuccessful());
//		
//		
//	}

}
