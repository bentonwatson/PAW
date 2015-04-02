package paw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import core.Game;

/**
 * this tab will display the list of available words in left text type panel
 * the list may be from the config output or the manual input
 * field for number of words to select with a generate button
 * after game is displayed a save button is available to call saveGame
 * @author Ben
 *
 */
class GeneratePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private PAWgui internalgui;
	private static GameGenerator newGame;
	private JPanel gridPanel;
	private JPanel answerPanel;
	private JPanel titlePanel;
	private JPanel wordListPanel;
	private JPanel buttonPanel;
	private JTable table;
	
	public GeneratePanel(Color color, PAWgui paw) {
		this.internalgui = paw;
		setMinimumSize(new Dimension(1000,550));
		setBackground(color);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout());
		
		String topic = internalgui.tmpConfigSettings.get(0);
		int level = Integer.valueOf(internalgui.tmpConfigSettings.get(1));
		int len = Integer.valueOf(internalgui.tmpConfigSettings.get(2));
		int stren = Integer.valueOf(internalgui.tmpConfigSettings.get(3));
		boolean dup = Boolean.valueOf(internalgui.tmpConfigSettings.get(4));
		boolean order = Boolean.valueOf(internalgui.tmpConfigSettings.get(5));
		String showall = internalgui.tmpConfigSettings.get(6);
		int numWords = Integer.valueOf(internalgui.tmpConfigSettings.get(7));
		newGame = new GameGenerator(topic, level, len, stren, dup, order);
		newGame.chooseNumberOfWords(numWords);
		
		generateWordListPanel();
		generateGridPanel();
		generateButtonPanel();
	}
	
	/**
	 * generates the panel to display words returned from config panel
	 */
	//TODO FUTURE: this panel will be an input panel that can generate a new Game from input words
	public void generateWordListPanel(){
		wordListPanel = new JPanel();
		wordListPanel.setPreferredSize(new Dimension(200, 300));
		wordListPanel.setLayout(new BorderLayout());
		
		JEditorPane numWords = new JEditorPane();
		numWords.setFont(Config.LABELFONT);
		numWords.setText("Number of Words = " + String.valueOf(newGame.getNewGame().getNumberWords()));
		wordListPanel.add(numWords, BorderLayout.NORTH);
		
		JEditorPane words = new JEditorPane();
		words.setFont(Config.UNDERFONT);
		ArrayList<String> wordList = newGame.getNewGame().getWordList();
		String list = "";
		for(int i = 0; i < wordList.size(); i++){
			list += wordList.get(i) + "\n";
		}
		words.setText(list);
		wordListPanel.add(words, BorderLayout.CENTER);
		
		add(wordListPanel, BorderLayout.WEST);
	}
	
	/**
	 * Creates the panel to display the columns
	 * includes the answer row
	 * @return 
	 */
	public void generateGridPanel(){
		gridPanel =  new JPanel();
		gridPanel.setBorder(new LineBorder(Color.black, 2));
		gridPanel.setLayout(new BorderLayout());
		
		titlePanel = new JPanel();
		JLabel titleLabel = new JLabel(newGame.getTitle() 
						+ " - (Duplicates = " + internalgui.tmpConfigSettings.get(4) + ")"
						+ " - (In Order = " + internalgui.tmpConfigSettings.get(5) + ")");
		titleLabel.setFont(Config.LABELFONT);
		titlePanel.add(titleLabel);
		gridPanel.add(titlePanel, BorderLayout.NORTH);
		
		JPanel columnPanel = new JPanel(new SpringLayout());
		JScrollPane sp = new JScrollPane(columnPanel);
		
		ArrayList<ArrayList<String>> columnData = newGame.getNewGame().getColumnData();
		for(int i = 0; i < columnData.size(); i++){
			JPanel column = new JPanel(new SpringLayout());
			
			ArrayList<String> characters = columnData.get(i);
			for(String character : characters){
				GridTile newTile = new GridTile(character);
				column.add(newTile);
			}
			makeGrid(column, characters.size(), 1, 5, 5, 5, 5);
			columnPanel.add(column);
		}
		makeGrid(columnPanel, 1, columnData.size(), 5, 5, 5, 5);
		
		gridPanel.add(sp, BorderLayout.CENTER);
		add(gridPanel, BorderLayout.CENTER);
	}
	
	/**
	 * generates a panel to hold action buttons
	 */
	public void generateButtonPanel(){
		JPanel buttPanel = new JPanel();
		buttonPanel = new JPanel(new SpringLayout());
		
		// generates the HTML
		JButton createHTMLBtn = new JButton("Create\nHTML");
		createHTMLBtn.setFont(Config.LABELFONT);
		createHTMLBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
//					CreateHTML ch = new CreateHTML(newGame.getNewGame());
					//TODO ?? add a popup to confirm the file was written
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(createHTMLBtn);
		
		//calls gameSaver and displays saved message
		JButton saveGameBtn = new JButton("Save\nGame");
		saveGameBtn.setFont(Config.LABELFONT);
		saveGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					GameSaver gs = new GameSaver(newGame.getNewGame());
					//TODO ?? add a popup to confirm the file was written
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		buttonPanel.add(saveGameBtn);
		
		//sends the game to play Tab and opens play tab
		JButton playGameBtn = new JButton("Play\nGame");
		playGameBtn.setFont(Config.LABELFONT);
		playGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PlayPanel.setNewGame(newGame);
					internalgui.selectTabbedPaneIndex(1);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(playGameBtn);
		

		makeGrid(buttonPanel, 3, 1, 25, 15, 15, 15);
		
		buttPanel.add(buttonPanel);
		add(buttPanel, BorderLayout.EAST);
		
	}
	
	/**
	 * sets the newGame for the Generate Panel
	 */
	public static void setNewGame(GameGenerator game){
		newGame = game;
	}
	
	/**
	 * This defines the basic tile used to hold the logical characters in the game
	 *
	 */
	class Tile extends JToggleButton {
		private static final long serialVersionUID = 1L;
		int clickedPosition = -1;
		int tileId = -1;
		
		Tile(){
			setFont(new Font("Arial Unicode MS", Font.PLAIN, 24));
		}

	}
	
	/**
	 * extends tile to further define the tiles in the game
	 * sets text to the tile and colors
	 *
	 */
	class GridTile extends Tile {
		private static final long serialVersionUID = 1L;
		int clickedPosition = -1;
		int tileId = -1;
		int columnNum;
		String character = "";
		Color pressedColor = Color.WHITE;

		GridTile(String character){
			super();
			setText(character);			
			setBackground(Color.yellow);
		}

	}
	
	/**
	 * @author From Java Docs Example Codes
     * Aligns the first <code>rows</code> * <code>cols</code>
     * components of <code>parent</code> in
     * a grid. Each component is as big as the maximum
     * preferred width and height of the components.
     * The parent is made just big enough to fit them all.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeGrid(Container parent,
                                int rows, int cols,
                                int initialX, int initialY,
                                int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeGrid must use SpringLayout.");
            return;
        }
 
        Spring xPadSpring = Spring.constant(xPad);
        Spring yPadSpring = Spring.constant(yPad);
        Spring initialXSpring = Spring.constant(initialX);
        Spring initialYSpring = Spring.constant(initialY);
        int max = rows * cols;
 
        //Calculate Springs that are the max of the width/height so that all
        //cells have the same size.
        Spring maxWidthSpring = layout.getConstraints(parent.getComponent(0)).
                                    getWidth();
        Spring maxHeightSpring = layout.getConstraints(parent.getComponent(0)).
                                    getWidth();
        for (int i = 1; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                            parent.getComponent(i));
 
            maxWidthSpring = Spring.max(maxWidthSpring, cons.getWidth());
            maxHeightSpring = Spring.max(maxHeightSpring, cons.getHeight());
        }
 
        //Apply the new width/height Spring. This forces all the
        //components to have the same size.
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                            parent.getComponent(i));
 
            cons.setWidth(maxWidthSpring);
            cons.setHeight(maxHeightSpring);
        }
 
        //Then adjust the x/y constraints of all the cells so that they
        //are aligned in a grid.
        SpringLayout.Constraints lastCons = null;
        SpringLayout.Constraints lastRowCons = null;
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                                 parent.getComponent(i));
            if (i % cols == 0) { //start of new row
                lastRowCons = lastCons;
                cons.setX(initialXSpring);
            } else { //x position depends on previous component
                cons.setX(Spring.sum(lastCons.getConstraint(SpringLayout.EAST),
                                     xPadSpring));
            }
 
            if (i / cols == 0) { //first row
                cons.setY(initialYSpring);
            } else { //y position depends on previous row
                cons.setY(Spring.sum(lastRowCons.getConstraint(SpringLayout.SOUTH),
                                     yPadSpring));
            }
            lastCons = cons;
        }
 
        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH,
                            Spring.sum(
                                Spring.constant(yPad),
                                lastCons.getConstraint(SpringLayout.SOUTH)));
        pCons.setConstraint(SpringLayout.EAST,
                            Spring.sum(
                                Spring.constant(xPad),
                                lastCons.getConstraint(SpringLayout.EAST)));
    }
}
