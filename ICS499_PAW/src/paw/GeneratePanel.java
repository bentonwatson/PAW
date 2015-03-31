package paw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * this tab will display the list of available words in left text type panel
 * the list may be from the config output or the manual input
 * field for number of words to select with a generate button
 * after game is displayed a save button is available to call saveGame
 * @author Ben
 *
 */
class GeneratePanel extends JPanel{
	private PAWgui internalgui;

	public GeneratePanel(Color color, PAWgui paw) {
		this.internalgui = paw;
		setMinimumSize(new Dimension(640,480));
		setBackground(color);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new GridLayout(1, 2));
		
		JPanel test = new JPanel();
		test.setBackground(color);
		JLabel testText = new JLabel(String.valueOf(internalgui.numWordsFound), 
				SwingConstants.CENTER);
		JTextPane tp = new JTextPane();
		tp.setText(internalgui.tmpWordList.toString());
		test.add(testText);
		test.add(tp);
		add(test);
	}
}
