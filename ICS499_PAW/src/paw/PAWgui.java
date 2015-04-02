package paw;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.BigWordCollection;
import core.Game;
import core.GameCollection;

public class PAWgui extends 	JFrame
{
	private		JTabbedPane tabbedPane;

	private		JPanel		welcomePanel;
	private 	JPanel		generatePanel;	
	private		JPanel		playPanel;
	private		JPanel		configPanel;
	private 	JPanel 		topPanel;
	private String mode = Config.DEFAULTMODE;
	private GameCollection gameCollection;
	private BigWordCollection origCollection = new BigWordCollection();
	private Font font;
	private int gameLevel;
	public int numWordsFound = 0; //value set by Config panel as a result after setting new config
//	public Game tmpGame = new Game();
	public ArrayList<String> tmpConfigSettings = new ArrayList<String>();
	public ArrayList<String> tmpWordList = new ArrayList<String>();
	
    /** 
     * The main GUI for the PAW game.
     * This assembles all the invidivual Tabbed Panels
     * and handles the interactions between the tabs
     */

	public PAWgui()
	{
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("src/te/Gidugu.ttf"));
			font = font.deriveFont(60f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameCollection = new GameCollection();

		setTitle(Config.APP_TITLE);
		setSize( 1050, 700 );
		setBackground( Color.gray );
		setMinimumSize(new Dimension(600, 400));
		
		topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(Config.LABELFONT);
		topPanel.add( tabbedPane, BorderLayout.CENTER );
		
		initialize();
	}

	public void initialize(){
		tabbedPane.removeAll();
		createWelcomePage();
		tabbedPane.addTab( "Welcome", welcomePanel );
		
		if(mode.equals("user")){
			createPlayPage();
			tabbedPane.addTab( "Play", playPanel );
			
			ChangeListener changeListener = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
					int index = sourceTabbedPane.getSelectedIndex();
					if(index == 1){
						createPlayPage();
						sourceTabbedPane.setComponentAt(index, playPanel);
					}
				}
			};
			tabbedPane.addChangeListener(changeListener);
		}else if(mode.equals("admin")){
			createPlayPage();
			tabbedPane.addTab( "Play", playPanel );
			createGeneratePage();
			tabbedPane.addTab( "Generate", generatePanel );
			createConfigPage();
			tabbedPane.addTab( "Config", configPanel );
			ChangeListener changeListener = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
					int index = sourceTabbedPane.getSelectedIndex();
					if(index == 1){
						createPlayPage();
						sourceTabbedPane.setComponentAt(index, playPanel);
					}
					if(index == 2){
						createGeneratePage();
						sourceTabbedPane.setComponentAt(index, generatePanel);
					}
					if(index == 3){
						createConfigPage();
						sourceTabbedPane.setComponentAt(index, configPanel);
					}
				}
			};
			tabbedPane.addChangeListener(changeListener);
		}
		
	}
	
	public void createWelcomePage()
	{
		tmpConfigSettings.add(Config.topic);
		tmpConfigSettings.add(Config.level);
		tmpConfigSettings.add(Config.wordLength);
		tmpConfigSettings.add(Config.wordStrength);
		tmpConfigSettings.add(Config.allowDuplicates);
		tmpConfigSettings.add(Config.charOrder);
		tmpConfigSettings.add(Config.allWords);
		tmpConfigSettings.add(Config.numWords);
		welcomePanel = new WelcomePanel();
	}
	
	public void createGeneratePage()
	{
		generatePanel = new GeneratePanel(Config.GENERATE_PANEL_BG_COLOR, PAWgui.this);
	}

	public void createPlayPage()
	{
		playPanel = new PlayPanel(Config.PLAY_PANEL_BG_COLOR, PAWgui.this);
	}

	public void createConfigPage()
	{
		configPanel = new ConfigPanel(Config.CONFIG_PANEL_BG_COLOR, PAWgui.this);
	}
	
	/** 
    *public method that other classes can call to change tab
    *@author ISRAEL.Yemer
    */
	public void selectTabbedPaneIndex (int i) 
	{ 
		tabbedPane.setSelectedIndex (i);
	}

	public void setNumWordsFound(int num){
		numWordsFound = num;
	}
	
	public int getGameLevel(){
		if(tmpConfigSettings.size() > 0){
			return Integer.valueOf(tmpConfigSettings.get(1));
		}
		return Integer.valueOf(Config.level);
	}

	// Main method to get things started
	public static void main( String args[] )
	{
		// Create an instance of the test application
		PAWgui mainFrame = new PAWgui();
		mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainFrame.setVisible( true );
		
	}
	
	
	class WelcomePanel extends JPanel
	{
		public WelcomePanel() {
			setMinimumSize(new Dimension(640,480));
			setBackground((Config.WELCOME_PANEL_BG_COLOR));
			setBorder(new EmptyBorder(5, 5, 5, 5));
			setLayout(new GridLayout(1, 2));
			
			JPanel pic = new JPanel();
			pic.setBackground((Config.WELCOME_PANEL_BG_COLOR));
			JLabel logoImage = new JLabel("", new ImageIcon("src/logo.jpg"), SwingConstants.CENTER);
			pic.add(logoImage);
			add(pic);
			
			JPanel text = new JPanel();
			text.setBackground((Config.WELCOME_PANEL_BG_COLOR));
			text.setLayout(new GridLayout(3,0));
			text.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			
			JTextPane title = new JTextPane();
			title.setBackground((Config.WELCOME_PANEL_BG_COLOR));
			title.setText(Config.WELCOME_TITLE);
			title.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 30));
			text.add(title);
			
			JTextPane body = new JTextPane();
			body.setBackground((Config.WELCOME_PANEL_BG_COLOR));
			body.setText(Config.WELCOME_MSG);
			body.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 30));
			text.add(body);
			
			
			JPanel button = new JPanel();
			button.setBackground((Config.WELCOME_PANEL_BG_COLOR));
			button.setLayout(new FlowLayout());
			gameLevel = getGameLevel();
			
			JButton setLevelOne = new JButton("Easy");
			setLevelOne.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						tmpConfigSettings.clear();
						tmpConfigSettings.add("Any");
						tmpConfigSettings.add("1");
						tmpConfigSettings.add("4");
						tmpConfigSettings.add("4");
						tmpConfigSettings.add("true");
						tmpConfigSettings.add("true");
						tmpConfigSettings.add("false");
						tmpConfigSettings.add("5");
						initialize();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			setLevelOne.setSize(100, 100);
			setLevelOne.setFont(new Font("Sitka Display", Font.BOLD, 16));
			setLevelOne.setBackground(Color.yellow);
			button.add(setLevelOne);
			
			JButton setLevelTwo = new JButton("Medium");
			setLevelTwo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						tmpConfigSettings.clear();
						tmpConfigSettings.add("Any");
						tmpConfigSettings.add("2");
						tmpConfigSettings.add("6");
						tmpConfigSettings.add("6");
						tmpConfigSettings.add("true");
						tmpConfigSettings.add("true");
						tmpConfigSettings.add("false");
						tmpConfigSettings.add("5");
						initialize();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			setLevelTwo.setSize(100, 50);
			setLevelTwo.setFont(new Font("Sitka Display", Font.BOLD, 16));
			setLevelTwo.setBackground(Color.yellow);
			button.add(setLevelTwo);

			JButton setLevelThree = new JButton("Hard");
			setLevelThree.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						tmpConfigSettings.clear();
						tmpConfigSettings.add("Any");
						tmpConfigSettings.add("3");
						tmpConfigSettings.add("6");
						tmpConfigSettings.add("6");
						tmpConfigSettings.add("false");
						tmpConfigSettings.add("true");
						tmpConfigSettings.add("false");
						tmpConfigSettings.add("8");
						initialize();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			setLevelThree.setSize(100, 50);
			setLevelThree.setFont(new Font("Sitka Display", Font.BOLD, 16));
			setLevelThree.setBackground(Color.yellow);
			button.add(setLevelThree);

			JButton setLevelFour = new JButton("Impossible");
			setLevelFour.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						tmpConfigSettings.clear();
						tmpConfigSettings.add("Any");
						tmpConfigSettings.add("4");
						tmpConfigSettings.add("6");
						tmpConfigSettings.add("6");
						tmpConfigSettings.add("false");
						tmpConfigSettings.add("false");
						tmpConfigSettings.add("false");
						tmpConfigSettings.add("5");
						initialize();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			setLevelFour.setSize(100, 50);
			setLevelFour.setFont(new Font("Sitka Display", Font.BOLD, 16));
			setLevelFour.setBackground(Color.yellow);
			button.add(setLevelFour);
			
			text.add(button);
			add(text);
		}

	}
	
}


