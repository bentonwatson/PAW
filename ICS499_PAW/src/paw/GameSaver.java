package paw;

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
		//TODO	
	}
	 
	/**
	 * Method saves the new game to the gameset
	 * sets the success variable
	 */
	public void writeNewGame(){
		//TODO
	}
	
	/**
	 * Method to acknowledge successful save
	 * @return
	 */
	public boolean isSuccessful(){
		return saveSuccessful;
	}

}
