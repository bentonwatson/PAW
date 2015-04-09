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
	private	JTabbedPane tabbedPane;
	private	JPanel welcomePanel;
	private JPanel generatePanel;	
	private	JPanel playPanel;
	private	JPanel userPlayPanel;
	private	JPanel adminPlayPanel;
	private	JPanel configPanel;
	private JPanel topPanel;
	private GameCollection gameCollection;
	private String mode = Config.DEFAULTMODE;
	private Font font;
	private int userGameLevel;
	private Game currentGame;
	
	public ArrayList<String> tmpWordList = new ArrayList<String>();
	public ArrayList<String> tmpConfigSettings = new ArrayList<String>();
	
	public int numWordsFound;
	
    /** 
     * The main GUI for the PAW game.
     * This assembles all the invidivual Tabbed Panels
     * and handles the interactions between the tabs
     */

	public PAWgui()
	{
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("src/te/Gidugu.ttf"));
			font = font.deriveFont(Config.FONTSIZETELUGU);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setTitle(Config.APP_TITLE);
		setSize( 1050, 700 );
		setBackground( Color.gray );
		setMinimumSize(new Dimension(600, 400));
		
		topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(font);
		topPanel.add( tabbedPane, BorderLayout.CENTER );
		
		gameCollection = new GameCollection();
		
		
		//sets the default configurations to level 1
		for(String setting: Config.defaultSettings){
			tmpConfigSettings.add(setting);
			
		}
		setUserGameLevel(1);
		initialize();
	}

	public void initialize(){
		setLocationRelativeTo(null);
		tabbedPane.removeAll();
		createWelcomePage();
		tabbedPane.addTab( "Welcome", welcomePanel );
		
		if(mode.equals("user")){
			createUserPlayPage();
			tabbedPane.addTab( "Play", userPlayPanel );
			
			ChangeListener changeListener = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
					int index = sourceTabbedPane.getSelectedIndex();
					if(index == 1){
						createUserPlayPage();
						sourceTabbedPane.setComponentAt(index, userPlayPanel);
					}
				}
			};
			tabbedPane.addChangeListener(changeListener);
		}else if(mode.equals("admin")){
			
			createAdminPlayPage();
			tabbedPane.addTab( "Play", adminPlayPanel );
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
						createAdminPlayPage();
						sourceTabbedPane.setComponentAt(index, adminPlayPanel);
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
		welcomePanel = new WelcomePanel(Config.WELCOME_PANEL_BG_COLOR, PAWgui.this);
	}
	public void createGeneratePage()
	{
		generatePanel = new GeneratePanel(Config.GENERATE_PANEL_BG_COLOR, PAWgui.this);
	}
	public void createUserPlayPage()
	{
		userPlayPanel = new UserPlayPanel(Config.PLAY_PANEL_BG_COLOR, PAWgui.this);
	}
	public void createAdminPlayPage()
	{
		adminPlayPanel = new AdminPlayPanel(Config.PLAY_PANEL_BG_COLOR, PAWgui.this);
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
	
	public void setUserGameLevel(int a_level){
		userGameLevel = a_level;
	}
	
	public int getUserGameLevel(){
		return userGameLevel;
	}

	public void setCurrentGame(Game a_game){
		currentGame = a_game;
	}
	
	public Game getCurrentGame(){
		return currentGame;
	}

	/**
	 * resets the tmpConfigSettings
	 * @param args
	 */
	public void setTmpConfigSettings(ArrayList<String> tmp){
		this.tmpConfigSettings = tmp;
	}
	
	public GameCollection getGameCollection(){
		return gameCollection;
	}
	
	public Font getFont(){
		return font;
	}
	
	public static void errorMessage(String a_string) {
		JOptionPane.showMessageDialog(null, a_string, "Error",
				JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	
	// Main method to get things started
	public static void main( String args[] )
	{
		// Create an instance of the test application
		PAWgui mainFrame = new PAWgui();
		mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainFrame.setVisible( true );
		
	}
	
}


