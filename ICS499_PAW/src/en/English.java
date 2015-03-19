package en;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;

/**
 * An extension of language that implements its abstract methods.
 * 
 * @author Dan Kruse 9-24-14
 */
public class English extends core.Language {
	
	int mode;
	
	public English(int mode) throws FileNotFoundException{
		this.font = new JLabel().getFont();
		this.languageAbbreviation = "EN";
		this.helpMessage = paw.Config.enHELPMESSAGE;
		this.winMessage = paw.Config.enWINMESSAGE;
		
		//added portion to detect mode and seed file.
		String adminFile = paw.Config.enAdminWordsFileName;
//		String staticFile = core.Config.enWordsFilePath + core.Config.enWordsFileName;
		String staticFile = paw.Config.enWordsFileName;
		try {
			if (mode == 0 && new File(adminFile).exists() && new File(adminFile).isFile() 
					&& new FileInputStream(adminFile).available() > 0) {
				this.seedFile = new FileInputStream(adminFile);
			} else if (new File(staticFile).exists() && new File(staticFile).isFile()) {
				this.seedFile = new FileInputStream(staticFile);
			} else {
				System.out
				.println("invalid english seed file or path specified, reverting to default");
				this.seedFile = getClass().getClassLoader().getResourceAsStream(
						"EN/words.txt");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Splits a word into an ArrayList of logical characters.
	 * 
	 * @author Dan-kruse 9-3-14
	 * @param word
	 *            - The word to be split into logical characters.
	 * @return - The logical characters of the word as elements of an
	 *         ArrayList<String>
	 */
	public ArrayList<String> splitToLogicalCharacters(String word) {
		ArrayList<String> splitWord = new ArrayList<String>();
		for (int i = 0; i < word.length(); i++) {
			splitWord.add(String.valueOf(word.charAt(i)));
		}
		return splitWord;
	}
}
