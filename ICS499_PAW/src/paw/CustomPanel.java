package paw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CustomPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private PAWgui internalgui;
	private Font font;
	private Color bgColor;
	private JPanel main;
	private ArrayList<String> customWords = new ArrayList<String>();
	
	/**
    * Creates new form NewJPanel
    */
	public CustomPanel(Color color, PAWgui paw) {
		bgColor = color;
		this.internalgui = paw;
		font = internalgui.getFont();
		
		initialize(bgColor);
	}
	
	public void initialize(Color color){
		setBackground(color);
		
		main = new JPanel();
		main.setLayout(new BorderLayout());
		main.setVisible(true);
		main.setMinimumSize(new Dimension(400, 500));
		
		JPanel topic = new JPanel();
		topic.setLayout(new GridLayout(2,1));
		JLabel label = new JLabel();
		label.setFont(font);
		label.setText("Custom Topic");
		
		JTextField tp = new JTextField();
		tp.setFont(font);
		tp.setText(internalgui.customTopic);
		topic.add(label);
		topic.add(tp);
		
		main.add(topic, BorderLayout.NORTH);
		
		JPanel input = new JPanel();
		input.setLayout(new BorderLayout());
		
		JLabel wlabel = new JLabel();
		wlabel.setFont(font);
		wlabel.setText("Custom Word List");
		input.add(wlabel, BorderLayout.NORTH);
		
		JTextArea ta = new JTextArea(13, 50);
		ta.setFont(font);
		String words = "";
		if(internalgui.customWords.size() > 0){
			for(String word : internalgui.customWords){
				words += word + "\n";
			}
		}
		ta.setText(words);
		
		ta.setSize(450, 400);
		JScrollPane scroll = new JScrollPane(ta);

		input.add(scroll, BorderLayout.CENTER);
		main.add(input, BorderLayout.CENTER);
		
		JButton setBtn = new JButton("Click to use words!");
		setBtn.setBackground(Config.CONFIG_PANEL_BUTTONS);
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
