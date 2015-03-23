package paw;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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
import core.GameCollection;

public class PAWgui extends 	JFrame
{
	private		JTabbedPane tabbedPane;

	private		JPanel		welcomePanel;
	private 	JPanel		generatePanel;	
	private		JPanel		playPanel;
	private		JPanel		configPanel;
	private 	JPanel 		topPanel;
	private GameCollection gameCollection;
	private BigWordCollection origCollection = new BigWordCollection();
	private Font font;
	
    /** 
     * The main GUI for the Quiz Master.
     * This assembles all the invidivual Tabbed Panels
     * and handles the interactions between the tabs
     */
//	public QuizMasterGUI(){
//		initialize();
//	}
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
		// NOTE: Every group works on Welcome Tab and one another Tab as follows
		// Check the Assignment for details

	

		setTitle(Config.APP_TITLE);
		setSize( 1200, 800 );
		setBackground( Color.gray );
		setMinimumSize(new Dimension(1200, 800));
		
		// Create the tab pages
		topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );
		
		initialize();
	}

	public void initialize(){
		createWelcomePage();
		createGeneratePage();
		createPlayPage();
		createConfigPage();

		// Create a tabbed pane
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab( "Welcome", welcomePanel );
		tabbedPane.addTab( "Generate", generatePanel );
		tabbedPane.addTab( "Play", playPanel );
		tabbedPane.addTab( "Config", configPanel );
		topPanel.add( tabbedPane, BorderLayout.CENTER );
		
		ChangeListener changeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				 JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
			        int index = sourceTabbedPane.getSelectedIndex();
			        if(index == 1){
			        	generatePanel = new GeneratePanel();
			        	sourceTabbedPane.setComponentAt(index,generatePanel);
			        }
			        if(index == 2){
			        	playPanel = new PlayPanel();
			        	sourceTabbedPane.setComponentAt(index, playPanel);
			        }
			        if(index == 3){
			        	configPanel = new ConfigPanel();
			        	sourceTabbedPane.setComponentAt(index,configPanel);
			        }
			}
		};
		tabbedPane.addChangeListener(changeListener);
		
	}
	
	public void createWelcomePage()
	{
		welcomePanel = new WelcomePanel();
	}
	
	public void createGeneratePage()
	{
		generatePanel = new GeneratePanel();
	}

	public void createPlayPage()
	{
		playPanel = new PlayPanel();
	}

	public void createConfigPage()
	{
		configPanel = new ConfigPanel();
	}
	
    // Main method to get things started
	public static void main( String args[] )
	{
		// Create an instance of the test application
		PAWgui mainFrame	= new PAWgui();
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
			
			JButton setLevel1 = new JButton("Level 1");
			setLevel1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						gameCollection = new GameCollection();
						initialize();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			setLevel1.setSize(100, 100);
			setLevel1.setFont(new Font("Sitka Display", Font.BOLD, 16));
			setLevel1.setBackground(Color.yellow);
			button.add(setLevel1);
			
			JButton setLevel2 = new JButton("Level 2");
			setLevel2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						gameCollection = new GameCollection();
						initialize();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			setLevel2.setSize(100, 50);
			setLevel2.setFont(new Font("Sitka Display", Font.BOLD, 16));
			setLevel2.setBackground(Color.yellow);
			button.add(setLevel2);
			
			JButton setLevel3 = new JButton("Level 3");
			setLevel3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						gameCollection = new GameCollection();
						initialize();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			setLevel3.setSize(100, 50);
			setLevel3.setFont(new Font("Sitka Display", Font.BOLD, 16));
			setLevel3.setBackground(Color.yellow);
			button.add(setLevel3);
			
			text.add(button);
			add(text);
		}

	}
	
	class GeneratePanel extends JPanel{
	
	}

	class PlayPanel extends JPanel{
	
	}
	
	class ConfigPanel extends JPanel{
	
	}
}


