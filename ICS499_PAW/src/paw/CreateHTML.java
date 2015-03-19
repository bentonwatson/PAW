//NOT Setup for PAW yet.

package paw;

import java.awt.Desktop;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Creates an HTML Answer page for a Bonza puzzle Future could create a
 * wordsearch based upon the puzzle.
 * 
 * @author Dan Kruse 9-13-14
 * 
 */
public class CreateHTML {

	Date date = new Date();// Create a new date.
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	// Create a file named by creation date.
	File file;
	PrintWriter out;

	/**
	 * Sole CreateHtml constructor.
	 * 
	 * @author Dan Kruse 9-13-14
	 * @param puzzleGrid
	 *            - Words to display on the HTML table.
	 * @param title
	 *            - The categore that resembles the words in the grid.
	 * @throws IOException
	 */
	public CreateHTML(ArrayList<String> clues, ArrayList<Integer> answerSize,
			ArrayList<String> tiles) throws IOException {
		if (new File(Config.directoryForHTMLFiles).exists()
				&& new File(Config.directoryForHTMLFiles).isDirectory()) {
			file = new File(Config.directoryForHTMLFiles
					+ dateFormat.format(date) + ".html");
		} else {
			System.out.println("invalid path to output html");
			file = new File(dateFormat.format(date) + ".html");
		}

		out = new PrintWriter(file, "UTF-8");

		addHeader("Multi 7 Little Words");// Adds header info to the HTML file.
		addMainTable(clues, answerSize);// Adds the puzzle grid to the HTML
										// file.
		addTiles(tiles);
		addClosingTags();// Add closing remarks.
		out.close();// Close the BufferedWriter.
		// Open the file in a web browser.
		Desktop.getDesktop().browse(file.toURI());
	}

	/*
	 * Adds closing tags to the HTML file.
	 * 
	 * @author Dan Kruse 9-13-14
	 */
	private void addClosingTags() {
		out.write("</body></html>");
	}

	/*
	 * Adds the header and beginning elements to the HTML.
	 * 
	 * @author Dan Kruse 9-13-14
	 */
	private void addHeader(String title) throws IOException {

		out.write("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN''http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en' lang='en'>");
		out.write("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/><title>table</title><style type='text/css'>.words {height: 50px;text-align: center;}h1, h2, h3 {text-align: center;}");
		out.write("table {border:1px solid black;border-collapse: separate;}table td, table th {font-size:20px;padding: 10px;}.answerkey td{width: 30px;height: 30px;border: 1px solid black;padding: none</style></head><body>");
		out.write("<h1>" + title + "</h1>");

	}

	/*
	 * Adds the clues to the html.
	 * 
	 * @author Dan Kruse 9-13-14
	 * 
	 * @param selectedWords
	 * 
	 * @throws IOException
	 */
	private void addMainTable(ArrayList<String> clues,
			ArrayList<Integer> answerSize) throws IOException {
		out.write("<table align='center'>");
		out.write("<tr class='words'><th>#</th><th>Clue</th><th>Size</th><th>Answer</th>");
		// Holds one line of table text at a time.
		StringBuilder textRow = null;
		for (int i = 0; i < clues.size(); i++) {
			textRow = new StringBuilder();
			textRow.append("<tr class='words'>");// start row
			textRow.append("<td>" + (i+1) + "</td>");// add rownumber
			textRow.append("<td align='left'>" + clues.get(i) + "</td>");// add clue
			textRow.append("<td>(" + answerSize.get(i) + ")</td>");// add answer
																	// size
			textRow.append("<td> _________________________</td>");// add blank
																	// to fill
																	// in answer
			textRow.append("</tr>");
			out.write(textRow.toString());
			textRow = null;
		}
		out.write("</table>");
	}
	/*
	 * Adds the tiles to the html.
	 * 
	 * @author Dan Kruse 9-13-14
	 * 
	 * @param selectedWords
	 * 
	 * @throws IOException
	 */
	private void addTiles(ArrayList<String> tiles) throws IOException {
		out.write("<table align='center'>");
		
		// Holds one line of table text at a time.
		StringBuilder textRow = new StringBuilder();
		int counter = 1;
		while(counter < 21){
			if (counter%5 == 1){
				textRow.append("<tr class='words'>");// start row
			}
			textRow.append("<td>" + tiles.get(counter - 1) + "</td>");// add tile
			if (counter%5 == 0 && counter < 20){ //if end of row
				textRow.append("</tr><tr class='words'>");
			}
			counter ++;
		}
		textRow.append("</tr>");
		out.write(textRow.toString());
		out.write("</table>");
	}
}
