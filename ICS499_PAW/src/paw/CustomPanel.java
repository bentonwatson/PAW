package paw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import core.BigWordCollection;

public class CustomPanel extends JPanel {
	private PAWgui internalgui;
	private Font font;
	private JPanel main;
	private ArrayList<String> customWords = new ArrayList<String>();
	
	/**
    * Creates new form NewJPanel
    */
	public CustomPanel(Color color, PAWgui paw) {
		
		this.internalgui = paw;
		font = internalgui.getFont();
		
		initialize(color);
	}
	
	public void initialize(Color color){
		setBackground(color);
		
		main = new JPanel();
		main.setLayout(new BorderLayout());
		main.setVisible(true);
		main.setMinimumSize(new Dimension(400, 500));
		
		JLabel label = new JLabel();
		label.setFont(font);
		label.setText("Enter a New Topic and Some words.");
		main.add(label, BorderLayout.NORTH);
		
		JPanel input = new JPanel();
		input.setLayout(new BorderLayout());
		
		JTextField tp = new JTextField();
		tp.setFont(font);
		input.add(tp, BorderLayout.NORTH);
		
		JTextArea ta = new JTextArea(13, 1);
		ta.setFont(font);
		ta.setSize(450, 225);
		JScrollPane scroll = new JScrollPane(ta);

		input.add(scroll, BorderLayout.CENTER);
		main.add(input, BorderLayout.CENTER);
		
		JButton setBtn = new JButton("Click to use words!");
		setBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//read textPane and set each word to list
				String topic = tp.getText();
				internalgui.customTopic = topic;

				String [] tmp = ta.getText().split("\r?\n|\r");
				for(String line : tmp){
					customWords.add(line.trim());
				}
				internalgui.customWords = customWords;
				if(customWords.get(0).length() > 1 && customWords.size() > 0){
					internalgui.tmpConfigSettings.set(0, "Custom Topic");
				}
				internalgui.selectTabbedPaneIndex(3);
			}
		});
		main.add(setBtn, BorderLayout.SOUTH);
			
		add(main);
	}
}
