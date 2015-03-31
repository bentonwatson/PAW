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
			br = new BufferedReader(new FileReader(Config.GAME_SET));
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.contains("ID")){
					id = sCurrentLine.split(" ")[1];
				}
			}
		} catch (IOException e) { e.printStackTrace(); } 
		
		int idSetup= (Integer.parseInt(id)+1);
		gameId = ("00"+ idSetup);
	}
	 
	/**
	 * Method saves the new game to the gameset
	 * sets the success variable
	 */
	public void writeNewGame(){
		saveSuccessful = false;
		String path= Config.GAME_SET;  //  set the value to the game get folder by path
		String title= newGame.getTitle();
		generateNewId();
		String level= newGame.getLevel()+"";
		String wordList= formatWordList(newGame.getWordList());
		String columns= formatColumnData(newGame.getColumnData());
		String data="\n"+"ID: "+gameId+"\n"
				+ "Title:  "+ title+"\n"
				+ "Level: "+ level+ "\n"
				+ "Words:   "+ wordList+"\n"
				+ columns
				+"-----------------------------------------------";
		try {
			File file = new File(path);					 
			FileWriter fileWritter = new FileWriter(file.getPath(),true);
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
//		gg.setWords(5);
//		Game game = gg.getNewGame();
//		System.out.println(game.toString());
//		
//		GameSaver gs = new GameSaver(game);
//		System.out.println(gs.isSuccessful());
//		
//		
//	}

}
