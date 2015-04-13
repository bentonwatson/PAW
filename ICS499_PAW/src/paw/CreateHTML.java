
package paw;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import core.Game;


public class CreateHTML {

	Date date = new Date();// Create a new date.
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	// Create a file named by creation date.
	File file;
	PrintWriter out;
	Game currentGame;
	String bgImageOne;
	String bgImageTwo;
	String logoImage;

	public CreateHTML(Game newGame) throws IOException {
		currentGame = newGame;
		bgImageOne = Config.HTML_BG_IMAGE_ONE;
		bgImageTwo = Config.HTML_BG_IMAGE_TWO;
		logoImage = Config.HTML_LOGO_IMAGE_ONE;
		
		if (new File(Config.directoryForHTMLFiles).exists()
				&& new File(Config.directoryForHTMLFiles).isDirectory()) {
			file = new File(Config.directoryForHTMLFiles
					+ dateFormat.format(date) + ".html");
		} else {
			System.out.println("invalid path to output html");
			file = new File(dateFormat.format(date) + ".html");
		}

		out = new PrintWriter(file, "UTF-8");

		addHeader();// Adds header info to the HTML file.
		addTable("Pick A Word", currentGame);
		
		addClosingTags();// Add closing remarks.
		out.close();// Close the BufferedWriter.
		// Open the file in a web browser.
		Desktop.getDesktop().browse(file.toURI());
	}



	private void addClosingTags() {
		out.write("</body></html>");
	}


	private void addHeader() throws IOException {

		out.write("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN''http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>"
				+ "<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en' lang='en'>");
		out.write("<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>"
				+ "<title>table</title>"
				+ "<style type='text/css'>"
				+ ".words {height: 50px;text-align: center;}"
				+ "table {background-image: url('"+ bgImageOne +"');  background-repeat:repeat;}"
				+ "body {background-image: url('"+ bgImageTwo +"');   background-repeat:repeat;}"
				+ "h2 {text-align: center;}");
		out.write("table #none {border:1px solid black;border-collapse: separate;}"
				+ "table td, table th {font-size:15px;padding: 10px;align: center;}"
				+ ".answerkey td{width: 30px;height: 30px;border: 1px solid black;padding: none;}"
				+ "#inner{width: 30px;height: 30px;border-bottom: 1px solid black;"
					+"border-top: 0px solid black; border-left: 0px solid black;"
					+ "border-right: 0px solid black; padding: none;align: center;}"
				+ "#tableHtml tr{border: 1px solid black;padding: none;}"
				+ "</style></head>"
				+ "<body>");
	}

	private void addTable(String name, Game game){

		String title = game.getTitle(); 
		int numWords = game.getNumberWords();
		String order;
		if(game.getCharOrder()){
			order = "Characters Are In Order";
		}else{
			order = "Characters Out Of Order";
		}
		String dup;
		if(game.getDuplicate()){
			dup = "Duplicate Characters Included";
		}else{
			dup = "Duplicate Characters Removed";
		}
		
		out.write("<table align='center'>"
			+ "<tr><td colspan='4'><h2>" + name +"</h2></td></tr>"
			+ "<tr><td colspan='4'>" + title
				+ " / Word Count = "+ numWords +" / " + order +" / " + dup +"</td>"
			+ "</tr>"
			+ "<tr><td colspan='4'>" + answerKey(game)	+ "</td></tr>"
			+ "<tr>");
				addRowsToHtml(game); 
//				out.write("<td><img src='"+ logoImage +"'</td>");
			out.write("</tr>"
					+ "</table>");
	}



	private void addRowsToHtml(Game game) {

		out.write("<td><table align='center'><tr><td>");
		StringBuilder textRow = new StringBuilder();


		for (int i = 0; i < game.getNumberWords(); i++) {
			textRow.append("<tr class='answerkey'>");
			for (int j = 0; j < game.getWordLength(); j++) {
				textRow.append("<td>" + game.getColumnData().get(j).get(i) + "</td>");
			}
			textRow.append("</tr>");
		}
		textRow.append("</td></tr></table></td></tr>"
				+ "<tr><td><table align='center'><tr><td>");

		for (int i = 0; i < game.getNumberWords(); i++) {
			textRow.append("<tr class='answerkey'>");
			for (int j = 0; j < game.getWordLength(); j++) {
				textRow.append("<td id='inner'>" + "    " + "</td>");		
			}
			textRow.append("</tr>");
		}
		
		out.write(textRow.toString());
		// add closing remarks
		out.write("</td></tr></table></td>");
	}

	private String answerKey(Game game){
		String list="Answer Key: ";

		for(int i=0; i< game.getWordList().size(); i++){
			if(i==(game.getWordList().size()-1)){
				list+= game.getWordList().get(i).toLowerCase();
			}else{
				list+= game.getWordList().get(i).toLowerCase()+", ";
			}
		}
		return list;
	}
}
