package paw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import core.Game;
import core.GameCollection;

public class WelcomePanel extends JPanel {
	
	PAWgui internalgui;
	private int gameLevel;
	private int userGameLevel;
	private GameCollection gameCollection;
	public WelcomePanel(Color color, PAWgui paw) {
		
		internalgui = paw;
		gameCollection = internalgui.getGameCollection();
		
		setMinimumSize(new Dimension(640,480));
		setBackground(color);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new GridLayout(1, 2));
		
		JPanel pic = new JPanel();
		pic.setBackground(color);
		JLabel logoImage = new JLabel("", new ImageIcon("src/logo.jpg"), SwingConstants.CENTER);
		pic.add(logoImage);
		add(pic);
		
		JPanel text = new JPanel();
		text.setBackground(color);
		text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
		text.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		JTextPane title = new JTextPane();
		title.setBackground(color);
		title.setText(Config.WELCOME_TITLE);
		title.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 30));
		text.add(title);
		
		JTextPane body = new JTextPane();
		body.setBackground(color);
		if(Config.DEFAULTMODE.equals("admin")){
			body.setText(Config.ADMIN_WELCOME);
		}else{
			body.setText(Config.WELCOME_MSG);
		}
		body.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 30));
		text.add(body);
		
		
		JPanel button = new JPanel();
		button.setBackground(color);
		button.setLayout(new FlowLayout());
//		gameLevel = getGameLevel();
		if(Config.DEFAULTMODE.equals("user")){
			JButton setLevelOne = new JButton("Easy");
			setLevelOne.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						internalgui.setUserGameLevel(1);
						//TODO get next level 1 game
						internalgui.setCurrentGame(gameCollection.getGameByLevel(1));
						internalgui.initialize();
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
						internalgui.setUserGameLevel(2);
						//TODO get next level 2 game
						internalgui.setCurrentGame(gameCollection.getGameByLevel(2));
						internalgui.initialize();
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
						internalgui.setUserGameLevel(3);
						//TODO get next level 3 game
						internalgui.setCurrentGame(gameCollection.getGameByLevel(3));
						internalgui.initialize();
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
						internalgui.setUserGameLevel(4);
						//TODO get next level 4 game
						internalgui.setCurrentGame(gameCollection.getGameByLevel(4));
						internalgui.initialize();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			setLevelFour.setSize(100, 50);
			setLevelFour.setFont(new Font("Sitka Display", Font.BOLD, 16));
			setLevelFour.setBackground(Color.yellow);
			button.add(setLevelFour);
			
		}else{
			JButton goToConfig = new JButton("Ready to Configure New Games!");
			goToConfig.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						internalgui.selectTabbedPaneIndex(3);		
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			goToConfig.setSize(100, 50);
			goToConfig.setFont(new Font("Sitka Display", Font.BOLD, 16));
			goToConfig.setBackground(Color.yellow);
			button.add(goToConfig);
		}
		text.add(button);
		add(text);
	}


}