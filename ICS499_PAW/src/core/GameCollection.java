package core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import paw.Config;
import paw.PAWgui;

public class GameCollection
{

	// for maintaining a puzzle collection
	private List<Game> allGames = new ArrayList<Game>();
	private int currentIndex = 0;
	private String currentID = null;

	/*
	 * No-arg constructor uses the default file name
	 */
	public GameCollection()
	{
		try
		{
			readGame(Config.GAME_SET);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * Constructor takes the input file name and * Reads each set of the puzzles
	 * * Creates a Puzzle using Puzzle.java * and adds the Puzzle to the
	 * collection allPuzzles * NOTE: Please make sure that you remove * from
	 * puzzle.txt before using it * NOTE: If you are keeping it, then you have
	 * to handle it here (additional complexity) * That is there in the puzzle
	 * text file for representation only The file is a UTF-8 file so that
	 * multi-byte characters can be handled
	 */
	public GameCollection(String a_file_name)
	{
		try
		{
			readGame(a_file_name);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Method for adding a game to a game collection
	 * 
	 * @param a_game
	 */
	
	public void add(Game a_game)
	{
		allGames.add(a_game);
	}

	public void readGame(String a_pathName) throws IOException
	{
	
		FileInputStream in = new FileInputStream(a_pathName);
//		InputStream in = getClass().getResourceAsStream(a_pathName);
		Scanner input = new Scanner(in, "UTF-8");
		String line2 = "";
		boolean delimiterFound = false;
	
		while ((input.hasNext()))
		{
			String line = input.nextLine();
			if (!line.startsWith("-"))
			{
	
				line2 += line + "\n";
	
			} else if (line.startsWith("-"))
			{
				parsePuzzle(line2);
				line2 = "";
				delimiterFound = true;
			}
		}
	
		if (!delimiterFound)
			PAWgui.errorMessage("The file " + a_pathName + " "
					+ " does not contain a puzzle separetor (---).");
	
		input.close();
	
	}

	private void parsePuzzle(String a_text)
	{
		String id = new String();
		String title = new String();
		int level = 0;
		boolean duplicate = false;
		boolean charOrder = false;
		ArrayList<String> wordList = new ArrayList<String>();
		ArrayList<ArrayList<String>> columnList = new ArrayList<ArrayList<String>>();
		String[][] puzzleGrid;
		ArrayList<String[]> charGrid = new ArrayList<String[]>();
		boolean idFound = false;
		boolean titleFound = false;
		boolean levelFound = false;
		boolean otherFound = false;
		boolean wordsFound = false;
		boolean columnFound = false;
		
		Scanner input = new Scanner(a_text);
		
		while (input.hasNext())	{
			String line = input.nextLine();
			
			if (line.contains("ID:")) {
				String[] split = line.split("ID:");
				id = split[1].trim();
				idFound = true;
				
			}else if (line.contains("Title:")) {
				String[] split = line.split("Title:");
				title = split[1].trim();
				titleFound = true;
				
			}else if (line.contains("Level:")) {
				String[] split = line.split("Level:");
				level = Integer.valueOf(split[1].trim());
				levelFound = true;
				
			}else if (line.contains("Other:")) {
				String[] split = line.split("Other:");
				String other = split[1].trim();
				String[] list = other.split("\\,");
				duplicate = Boolean.valueOf(list[0]);
				charOrder = Boolean.valueOf(list[1]);
				
				otherFound = true;
			}else if (line.contains("Words:")) {
				String[] split = line.split("Words:");
				String words = split[1].trim();
				String[] list = words.split("\\,");
				for (int i = 0; i < list.length; i++)
				{
					wordList.add(list[i].trim());					
				}			
				wordsFound = true;
			}else if (line.contains("C:")) {
				ArrayList<String> singleColumn = new ArrayList<String>();
				String[] split = line.split("C:");
				String words = split[1].trim();
				String[] list = words.split("\\,");
				for (int i = 0; i < list.length; i++)
				{
					singleColumn.add(list[i].trim());					
				}
				columnList.add(singleColumn);
				columnFound = true;
			}
		}//end while 
		
		if (!idFound || !titleFound || !levelFound || !wordsFound || !columnFound){
			PAWgui.errorMessage("The line: " +  a_text + "\n" +
					"does not contain required data for game collection");
		}
		input.close();
		Game game = new Game(id, level, title, wordList, columnList, duplicate, charOrder);
		allGames.add(game);
		
	}

	/**
	 * method returns a game based ont he level
	 * @param a_level
	 * @return Game
	 */
	public Game getGameByLevel(int a_level){
		
		for (int i = 0; i < allGames.size(); i++)
		{
			int level = allGames.get(i).getLevel();

			if (level == a_level)
			{
				this.currentIndex = i;
				return allGames.get(i);
			} 
		}
		return getRandomGame();
	}
	
	/*
	 * Method that returns a puzzle based on an Id from the puzzle collection
	 * Ifthere is no puzzle matching that id, then a random puzzle will be
	 * returned
	 */
	public Game getGameByID(String an_id)
	{
		if (an_id == null)
		{
			return this.getRandomGame();
		} else
		{
			for (int i = 0; i < allGames.size(); i++)
			{
				String current = allGames.get(i).getId();

				if (an_id == null)
				{
					this.currentIndex = i;
					return allGames.get(i);
				} else
				{
					if (current.endsWith(an_id))
					{
						this.currentIndex = i;
						return allGames.get(i);
					}
				}
			}
		}
		return null;
	}

	/**
	 * The indexes are 0...N The ID's are 001, 002, 003
	 */
	public Game getGameByID()
	{
		if (this.currentIndex < 0)
		{
			return this.getRandomGame();
		} else
		{
			return getGameByID(this.currentID);
		}
	}

	/*
	 * Method that returns a puzzle based on an Id from the puzzle collection
	 * 
	 * If there are multiple puzzles matching the title, then only the first
	 * match will be returned.
	 * 
	 * If there is no match found, then a random puzzle will be returned
	 */
	public Game getGameByTitle(String a_title)
	{

		for (int i = 0; i < allGames.size(); i++)
		{
			String current = allGames.get(i).getTitle();

			if (current.contains(a_title))
			{
				return allGames.get(i);
			}
		}
		return null;

	}

	/*
	 * Method that returns a a random game from allGames
	 */

	public Game getRandomGame()
	{
		int randomIndex = (int) (Math.random() * allGames.size());
		return allGames.get(randomIndex);
	}

	public boolean isDelimiter(String a_String)
	{
		if (a_String.startsWith("---") || a_String == null
				|| a_String.equals("") || a_String.length() < 5)
		{
			return true;
		}
		return false;
	}

	public Game nextGame()
	{
		if (this.currentIndex + 1 <= this.allGames.size())
		{
			return allGames.get(++this.currentIndex);
		} else
		{
			this.currentIndex = 0;
			return this.getGameByID();
		}
	}

	public void setCurrentId(String myCurrentId)
	{
		this.currentID = myCurrentId;

		if (this.currentID == null)
		{
			this.currentIndex = -1;
		} else
		{
			this.currentIndex = 0;
		}
	}
	
	//PAW group added
	public String getCurrentID(){
		return currentID;
	}
	
	//PAW added
	public ArrayList<String> getGameIds(){
		ArrayList<String> gameIds = new ArrayList<String>();
		for(Game game : allGames){
			gameIds.add(game.getId());
		}
		return gameIds;
	}
	

}
