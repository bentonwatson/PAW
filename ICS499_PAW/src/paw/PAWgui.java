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
	
	private String mode = Config.DEFAULTMODE;
	private Font font;
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
			font = font.deriveFont(60f);
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
		tabbedPane.setFont(Config.LABELFONT);
		topPanel.add( tabbedPane, BorderLayout.CENTER );
		
		//sets the default configurations to level 1
		tmpConfigSettings.add(Config.topic);
		tmpConfigSettings.add(Config.level);
		tmpConfigSettings.add(Config.wordLength);
		tmpConfigSettings.add(Config.wordStrength);
		tmpConfigSettings.add(Config.allowDuplicates);
		tmpConfigSettings.add(Config.charOrder);
		tmpConfigSettings.add(Config.allWords);
		tmpConfigSettings.add(Config.numWords);
		
		initialize();
	}

	public void initialize(){
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
	public void createPlayPage()
	{
		playPanel = new PlayPanel(Config.PLAY_PANEL_BG_COLOR, PAWgui.this);
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
	
	/**
	 * resets the tmpConfigSettings
	 * @param args
	 */
	public void setTmpConfigSettings(ArrayList<String> tmp){
		this.tmpConfigSettings = tmp;
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


