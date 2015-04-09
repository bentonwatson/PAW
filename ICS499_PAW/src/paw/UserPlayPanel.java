package paw;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import te.TeluguWordProcessor;
import core.Game;
import core.GameCollection;
import core.SpringUtility;
import core.WordProcessor;

public class UserPlayPanel extends JPanel implements MouseListener{
	private static final long serialVersionUID = 1L;
	private PAWgui internalgui;
	private Game currentGame;
	private JPanel gridPanel;
	private JPanel columnPanel;
	private JPanel answerPanel;
	private JPanel titlePanel;
	private JPanel wordListPanel;
	private JPanel buttonPanel;
	private JPanel buttPanel;
	private JLabel message;
	private Font font;
	
	private int userGameLevel;
	private String userGameLevelByName;
	private JComboBox<String> levelComboBox;
	private JLabel levelComboLabel;
	
	private JLabel jlbTime;
	private JPanel timerPanel;
	private final ClockListener cl = new ClockListener();
	private final Timer time = new Timer(1000, cl);
	private final JLabel jlbTimer = new JLabel("00:00:00");
	private GameCollection gameCollection;
	
	private int clickCount = 0;
	private List<GridTile> gridTiles = new ArrayList<>();
	private List<AnswerTile> answerTiles = new ArrayList<>();
	private String[] guessWord;
	private GameTracker tracker;
	private JEditorPane numWords;
	private JEditorPane words;
	
	ArrayList<String> notFoundWords = new ArrayList<String>();
	ArrayList<String> foundWordList = new ArrayList<String>();
	
	public UserPlayPanel(Color color, PAWgui paw){
		this.internalgui = paw;
		font = internalgui.getFont();
		gameCollection = internalgui.getGameCollection();
		userGameLevel = internalgui.getUserGameLevel();
		if(internalgui.getCurrentGame() != null){
			currentGame = internalgui.getCurrentGame();
			tracker = new GameTracker(currentGame);
		}
		
		setMinimumSize(new Dimension(1000,550));
		setBackground(color);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout());
		
		addMouseListener(this);
		
		generateButtonPanel();

		initialize();
	}
	public void initialize(){
		//if currentGame == null the panels will generate empty 
		generateWordListPanel();
		generateGridPanel();
		
		setVisible(true);
		time.start();
	}
	
	/**
	 * generates the panel to display words that have been guessed correctly
	 */
	public void generateWordListPanel(){
		wordListPanel = new JPanel();
		wordListPanel.addMouseListener(this);
		wordListPanel.setPreferredSize(new Dimension(200, 300));
		wordListPanel.setLayout(new BorderLayout());
		
		numWords = new JEditorPane();
		numWords.setFont(font);
		numWords.setBackground(Color.yellow);
		
		if(currentGame != null){
			numWords.setText("Number of Words = " + String.valueOf(currentGame.getNumberWords())
					+ "\n You Have Found = " + foundWordList.size());
		}else{
			numWords.setText("Number of Words = 0 \n You Have Found = 0");
		}
		wordListPanel.add(numWords, BorderLayout.NORTH);
		
		words = new JEditorPane();
		words.setFont(font);
		words.setBackground(Color.yellow);
		String foundList = "";
		if(foundWordList != null){
			for(int i = 0; i < foundWordList.size(); i++){
				foundList += foundWordList.get(i) + "\n";
			}
			words.setText(foundList);
		}
		wordListPanel.add(words, BorderLayout.CENTER);
		
		add(wordListPanel, BorderLayout.WEST);
	}
	
	/**
	 * Creates the panel to display the columns
	 * includes the answer row
	 */
	public void generateGridPanel(){
		gridPanel =  new JPanel();
		gridPanel.setLayout(new BorderLayout());
		gridPanel.setBorder(new LineBorder(Color.black, 2));
		
		titlePanel = new JPanel();
		if (currentGame != null) {
			JLabel titleLabel = new JLabel(currentGame.getTitle()
					+ " - (Duplicates = " + currentGame.getDuplicate() + ")"
					+ " - (In Order = " + currentGame.getCharOrder() + ")");
			titleLabel.setFont(font);
			titlePanel.add(titleLabel);
		}else{
			JLabel titleLabel = new JLabel(" No Game Selected ");
			titleLabel.setFont(font);
			titlePanel.add(titleLabel);
		}
		gridPanel.add(titlePanel, BorderLayout.NORTH);
		
		columnPanel = new JPanel();

		columnPanel.setLayout(new GridLayout(1, 10, 10, 0));
		columnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));

		ArrayList<ArrayList<String>> columnData = new ArrayList<ArrayList<String>>();
		if(currentGame!= null){
			guessWord = new String[currentGame.getNumberColumns()];
			columnData = currentGame.getColumnData();
			
			for(int i = 0; i < columnData.size(); i++){
				ArrayList<String> characters = columnData.get(i);
				JPanel column = new JPanel(new GridLayout(characters.size(), 1, 0, 5));
				for(int j = 0; j < characters.size(); j++){
					if(!currentGame.getDuplicate()){
						String tmp = characters.get(j);
						if(tmp.length() > 1){
							WordProcessor wp = new TeluguWordProcessor(tmp);
							ArrayList<String> ch = wp.getLogicalChars();
							int count = Integer.valueOf(ch.get(1));
							GridTile newTile = new GridTile(wp.logicalCharAt(0), j);
							newTile.columnNum = i;
							newTile.repeat = count;
							newTile.addMouseListener(this);
							column.add(newTile);
							gridTiles.add(newTile);
						}else{
							GridTile newTile = new GridTile("", j);
							newTile.columnNum = i;
							newTile.repeat = 1;
							newTile.setVisible(false);
							newTile.addMouseListener(this);
							column.add(newTile);
							gridTiles.add(newTile);
						}
					}else{
						GridTile newTile = new GridTile(characters.get(j), j);
						newTile.columnNum = i;
						newTile.repeat = 1;
						newTile.addMouseListener(this);
						column.add(newTile);
						gridTiles.add(newTile);
					}
				}
				columnPanel.add(column);
			}
			columnPanel.addMouseListener(this);
		}
		gridPanel.add(columnPanel, BorderLayout.CENTER);
		
		JPanel instructPanel = new JPanel();
		JLabel ansLabel = new JLabel("Click tile to quess word.");
		ansLabel.setFont(font);
		instructPanel.add(ansLabel);

		answerPanel = new JPanel(new BorderLayout());
		answerPanel.add(instructPanel, BorderLayout.NORTH);
		if (currentGame != null) {
			JPanel guessPanel = new JPanel(new GridLayout(1, 0, 5, 0));
			guessPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
			for (int i = 0; i < columnData.size(); i++) {
				AnswerTile newTile = new AnswerTile(" ", i);
				newTile.addMouseListener(this);
				
				newTile.setVisible(true);
				guessPanel.add(newTile);
				
				answerTiles.add(newTile);
			}
			answerPanel.add(guessPanel, BorderLayout.CENTER);
		}
		message = new JLabel();
		message.setFont(font);
		answerPanel.add(message, BorderLayout.SOUTH);
		gridPanel.add(answerPanel, BorderLayout.SOUTH);
	
		add(gridPanel, BorderLayout.CENTER);
	}
	
	public void setUserGameLevel(int level){
		internalgui.setUserGameLevel(level);
		userGameLevel = level;
	}
	
	public void endGame()
	{
		time.stop();
//		boolean status = tracker.isGameComplete();
//		Date date = new Date();
//		String timeElapsed = cl.toString();
//		String id = progress.getCurrentGameId();
//		progress.setPlayDate(date);
//		progress.setTimeElapsed(timeElapsed);
//		progress.setCompleted(status);
		 
		int answer = JOptionPane.showConfirmDialog(null,
				tracker.getGameStatusMsg(), "Awesome!!!",
				JOptionPane.YES_NO_OPTION);
		switch (answer)
		{
		case JOptionPane.OK_OPTION:
//			progress.writeToFile();
			setCurrentGame(gameCollection.getGameByLevel(userGameLevel));
			internalgui.initialize();
			internalgui.selectTabbedPaneIndex(1);
			break;
		case JOptionPane.NO_OPTION:
//			progress.writeToFile();
			System.exit(0);
			break;
		}// end switch

	}
	
	/**
	 * generates a panel to hold action buttons
	 */
	public void generateButtonPanel(){
		buttPanel = new JPanel();
		buttPanel.setBackground(Color.yellow);
		buttonPanel = new JPanel(new SpringLayout());
		buttonPanel.setBackground(Color.yellow);
		
		// Level
		levelComboLabel = new JLabel("Level");
		levelComboLabel.setFont(font);
		levelComboBox = new JComboBox<String>();
		String [] levelByName = {"Easy", "Medium", "Hard", "Impossible"};
		for(int i = 0; i < 4; i++){
			levelComboBox.addItem(levelByName[i]);
		}
		levelComboBox.setFont(font);
		if(currentGame != null){
			userGameLevel = currentGame.getLevel();
			userGameLevelByName = levelByName[currentGame.getLevel() - 1];
		}else{
			userGameLevelByName = levelByName[userGameLevel - 1];
		}
		userGameLevelByName = levelByName[userGameLevel - 1];
		levelComboBox.setSelectedItem(userGameLevelByName);
		levelComboBox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				userGameLevelByName = String.valueOf(levelComboBox.getSelectedItem());
				if(userGameLevelByName.equals("Easy")){
					setUserGameLevel(1);
//					System.out.println("change to 1");
				}
				if(userGameLevelByName.equals("Medium")){
					setUserGameLevel(2);
//					System.out.println("change to 2");
				}
				if(userGameLevelByName.equals("Hard")){
					setUserGameLevel(3);
//					System.out.println("change to 3");
				}
				if(userGameLevelByName.equals("Impossible")){
					setUserGameLevel(4);
//					System.out.println("change to 4");
				}
			}
		});
		
		buttonPanel.add(levelComboLabel);
		buttonPanel.add(levelComboBox);
		
		// calls for a new game using the userGameLevel variable
		JButton newGameBtn = new JButton("New Game");
		newGameBtn.setFont(font);
		newGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					internalgui.setCurrentGame(gameCollection.getGameByLevel(userGameLevel));
					internalgui.initialize();
					internalgui.selectTabbedPaneIndex(1);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(newGameBtn);
		
		timerPanel = new JPanel();
		jlbTime = new JLabel("time");
		jlbTime.setForeground(Color.black);
		jlbTime.setFont(new Font("Serif", Font.BOLD, 36));
		jlbTimer.setFont(new Font("Serif", Font.BOLD, 36));
		jlbTimer.setForeground(Color.black);
		timerPanel.add(jlbTime);
		timerPanel.add(jlbTimer);
		buttonPanel.add(timerPanel);
		timerPanel.setBounds(0, 330, 300, 70);
		time.start();

		SpringUtility.makeGrid(buttonPanel, 4, 1, 25, 15, 15, 15);
		buttPanel.add(buttonPanel);
		add(buttPanel, BorderLayout.EAST);
	}
	
	/**
	 * sets the currentGame for the User in Play Panel
	 * @param Game
	 */
	public void setCurrentGame(Game game){
		currentGame = game;
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getSource() instanceof GridTile) {
			GridTile pressedButton = (GridTile) arg0.getSource();
			for (GridTile gridTile : gridTiles) {
				if (gridTile.columnNum == pressedButton.columnNum
						&& gridTile.clickedPosition > -1
						&& gridTile.tileId != pressedButton.tileId) {
					return;
				}
			}
			if (clickCount == currentGame.getWordLength()) {
				if (pressedButton.clickedPosition < 0) {
					return;
				}
			}
			if (pressedButton.clickedPosition == -1 && currentGame.getCharOrder()) {
				
				pressedButton.clickedPosition += 1;
				clickCount++;
				AnswerTile at = answerTiles.get(pressedButton.columnNum);
				at.character = pressedButton.character;
				at.setText(pressedButton.character);
				at.setVisible(true);
				pressedButton.setBackground(Color.YELLOW);
				guessWord[pressedButton.columnNum] = pressedButton.character;
				
			}else if(pressedButton.clickedPosition == -1 && !currentGame.getCharOrder()){
				
				pressedButton.clickedPosition += 1;
				AnswerTile at = answerTiles.get(clickCount);
				at.character = pressedButton.character;
				at.tileId = pressedButton.columnNum;
				at.clickOrder = clickCount;
				at.setText(pressedButton.character);
				at.setVisible(true);
				pressedButton.setBackground(Color.YELLOW);
				guessWord[clickCount] = pressedButton.character;
				clickCount++;
				
			}else if (!currentGame.getCharOrder()){
				
				for(AnswerTile ans : answerTiles){
					if(ans.tileId == pressedButton.columnNum){
						ans.setText(" ");
						guessWord[ans.clickOrder] = "";
						pressedButton.setVisible(true);
						pressedButton.clickedPosition = -1;
						
						break;
					}
				}
				AnswerTile nt = answerTiles.get(pressedButton.columnNum);
				if (nt.getBackground().equals(Color.RED)) {
					for (AnswerTile answerTile : answerTiles) {
						answerTile.setSelected(false);
						answerTile.setBackground(Color.YELLOW);
					}
				}
				clickCount--;
				for (GridTile gridTile : gridTiles) {
					if (gridTile.clickedPosition > pressedButton.clickedPosition) {
						gridTile.clickedPosition -= 1;
					}
				}
				
			}else{
				AnswerTile nt = answerTiles.get(pressedButton.columnNum);
				if (nt.getBackground().equals(Color.RED)) {
					for (AnswerTile answerTile : answerTiles) {
						answerTile.setSelected(false);
						answerTile.setBackground(Color.YELLOW);
						pressedButton.setSelected(false);
					}
				}
				nt.setText(" ");
//				pressedButton.setVisible(true);
				clickCount--;
				guessWord[pressedButton.columnNum] = "";
				for (GridTile gridTile : gridTiles) {
					if (gridTile.clickedPosition > pressedButton.clickedPosition) {
						gridTile.clickedPosition -= 1;
					}
				}
				pressedButton.clickedPosition = -1;
			}
			
			if (clickCount == currentGame.getWordLength()) {
				
				if (tracker.isWordInTheList(guessWord)) {
					String found = "";
					for(String s: guessWord){
						found += s;
					}
					foundWordList.add(found);
					
					java.util.Timer timer = new java.util.Timer();
					for (AnswerTile answerTile : answerTiles) {
						answerTile.setSelected(false);
						answerTile.setBackground(Color.GREEN);
					}
					long pause = 500;
					for (GridTile gridTile : gridTiles) {
						if (gridTile.clickedPosition > -1) {
//							pause += 500; // creates a bug
							timer.schedule(new TimerTask() {
								@Override
								public void run() {
									if (currentGame.getDuplicate()) {
										gridTile.setVisible(false);
										
									} else {
										//i want to know if there are any more of this letter needed
										if(gridTile.repeat > 1){
											gridTile.setSelected(false);
											gridTile.setBackground(Color.YELLOW);
											gridTile.clickedPosition = -1;
											gridTile.setSelected(false);
											gridTile.repeat -= 1;
										}else{
											gridTile.setVisible(false);
										}
									}
									gridTile.clickedPosition = -1;
								}
							}, pause);
						}
					}
					clickCount = 0;
					guessWord = new String[currentGame.getWordLength()];
					for (AnswerTile answerTile : answerTiles) {
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								answerTile.setText(" ");
								answerTile.setSelected(false);
								answerTile.setBackground(Color.YELLOW);
							}
						}, pause);
//						pause -= 500; // creates a bug
					}
					generateWordListPanel();
					if(foundWordList.size() == currentGame.getNumberWords()){
						endGame();
					}
				} else {
					for (AnswerTile answerTile : answerTiles) {
						answerTile.setSelected(false);
						answerTile.setBackground(Color.RED);
						message.setText("Incorrect Keep Trying!");
					}
				}

			}
		}
		if (arg0.getSource() instanceof AnswerTile) {
			AnswerTile pressedButton = (AnswerTile) arg0.getSource();
			if (pressedButton.getText().equals(" ")) {
				pressedButton.setSelected(false);
				return;
			}
			if (pressedButton.getBackground().equals(Color.RED)) {
				for (AnswerTile answerTile : answerTiles) {
					answerTile.setSelected(false);
					answerTile.setBackground(Color.YELLOW);
					message.setText("");
				}
			}
			guessWord[pressedButton.tileId] = "";
			pressedButton.setText(" ");
			pressedButton.setSelected(false);
			clickCount--;
			for (GridTile gridTile : gridTiles) {
				if (gridTile.columnNum == pressedButton.tileId) {
					gridTile.clickedPosition = -1;
					gridTile.setSelected(false);
					gridTile.setBackground(Color.YELLOW);
				}
			}

		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}


	/**
	 * This defines the basic tile used to hold the logical characters in the game
	 *
	 */
	class Tile extends JToggleButton implements DragGestureListener,
	DragSourceListener {
		private static final long serialVersionUID = 1L;
		int clickedPosition = -1;
		int tileId = -1;
		DragSource ds = new DragSource();

		Tile() {
			setFont(font);
		}

		@Override
		public void dragDropEnd(DragSourceDropEvent arg0) {
			if (this instanceof GridTile) {
				GridTile PressedButton = (GridTile) this;
				if (arg0.getDropSuccess()) {
					PressedButton.clickedPosition = clickCount;
					PressedButton.setBackground(Color.WHITE);
				}
			}
			if (this instanceof GridTile) {
				GridTile PressedButton = (GridTile) this;
				PressedButton.clickedPosition = clickCount;
				PressedButton.setBackground(Color.WHITE);
			}
		}

		@Override
		public void dragEnter(DragSourceDragEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dragExit(DragSourceEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dragOver(DragSourceDragEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dropActionChanged(DragSourceDragEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dragGestureRecognized(DragGestureEvent arg0) {
			Transferable transferable = new StringSelection(getText());
			ds.startDrag(arg0, DragSource.DefaultCopyDrop, transferable, this);
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
		int repeat;
		Color pressedColor = Color.WHITE;
		String character;

		GridTile(String character, int iD) {
			super();
			this.character = character;
			setText(character);
			setBackground(Color.yellow);
			tileId = iD;
		}
		
	}

	class AnswerTile extends Tile {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/** The tile id. */
		int tileId = -1;
		int clickOrder;

		/** The character. */
		String character = "";

		/** The color. */
		Color color = Color.YELLOW;
		
		/**
		 * Instantiates a new answer tile.
		 * @param character, the character
		 * @param iD, the i d
		 */
		AnswerTile(String character, int iD) {
			super();
			setText(character);
			tileId = iD;
			setBackground(color);
			setVisible(false);
		}

//		DropTarget dt = new DropTarget(this, new DropTargetListener() {
//
//			@Override
//			public void dropActionChanged(DropTargetDragEvent dtde) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void drop(DropTargetDropEvent dtde) {
//				clickCount++;
//				System.out.println();
//				try {
//
//					Transferable transferable = dtde.getTransferable();
//
//					if (transferable
//							.isDataFlavorSupported(DataFlavor.stringFlavor)) {
//
//						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
//
//						String dragContents = (String) transferable
//								.getTransferData(DataFlavor.stringFlavor);
//
//						dtde.getDropTargetContext().dropComplete(true);
//
//						setText(dragContents);
//
//					} else {
//
//						dtde.rejectDrop();
//
//					}
//
//				} catch (IOException e) {
//
//					dtde.rejectDrop();
//
//				} catch (UnsupportedFlavorException e) {
//
//					dtde.rejectDrop();
//
//				}
//			}
//
//			@Override
//			public void dragOver(DropTargetDragEvent dtde) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void dragExit(DropTargetEvent dte) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void dragEnter(DropTargetDragEvent dtde) {
//				// TODO Auto-generated method stub
//
//			}
//		});

	}

	private class ClockListener implements ActionListener {
		int counter;

		@Override
		public void actionPerformed(ActionEvent e) {
			NumberFormat formatter = new DecimalFormat("00");
			String hour = formatter.format(counter / 3600);
			String minute = formatter.format(counter / 60 % 60);
			String second = formatter.format(counter % 60);
			jlbTimer.setText(String.valueOf(hour + ":" + minute + ":" + second));
			counter++;
		}
	}

	public void componentResized(ComponentEvent arg0) {

		// Dimension sidePanelSize = sidePanel.getSize();
		// wordsPanel.setBounds(0, 50, sidePanelSize.width,
		// sidePanelSize.height - 50 - 70);
		// Dimension wordsPanelSize = wordsPanel.getSize();
		// timerPanel.setBounds(0, wordsPanelSize.height + 50,
		// sidePanelSize.width, 70);
	}

	public static void errorMessage(String a_string) {

		JOptionPane.showMessageDialog(null, a_string, "Error",
				JOptionPane.ERROR_MESSAGE);
		System.exit(0);

	}

	class StraightDownDropClick implements MouseListener {
		long timer = 0;

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			timer = System.currentTimeMillis();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			if (System.currentTimeMillis() - timer < 150) {
				if (arg0.getSource() instanceof GridTile) {
					GridTile pressedButton = (GridTile) arg0.getSource();
					for (GridTile gridTile : gridTiles) {
						if (gridTile.columnNum == pressedButton.columnNum
								&& gridTile.clickedPosition > -1
								&& gridTile.tileId != pressedButton.tileId) {
							return;
						}
					}
					if (clickCount == currentGame.getWordLength()) {
						if (pressedButton.clickedPosition < 0) {
							return;
						}
					}
					if (pressedButton.clickedPosition == -1) {
						pressedButton.clickedPosition += 1;
						clickCount++;
						AnswerTile at = answerTiles
								.get(pressedButton.columnNum);
						at.character = pressedButton.character;
						at.setText(pressedButton.character);
						at.setVisible(true);
//						at.getParent().revalidate();
						pressedButton.setBackground(Color.WHITE);
						pressedButton.getParent().revalidate();
						guessWord[pressedButton.columnNum] = pressedButton.character;
					} else {
						AnswerTile nt = answerTiles
								.get(pressedButton.columnNum);
						if (nt.getBackground().equals(Color.RED)) {
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.YELLOW);
							}
						}
						nt.setText(" ");
//						nt.getParent().revalidate();
						pressedButton.setBackground(Color.YELLOW);
						pressedButton.setSelected(false);
//						pressedButton.getParent().revalidate();
						clickCount--;
						guessWord[pressedButton.columnNum] = "";
						for (GridTile gridTile : gridTiles) {
							if (gridTile.clickedPosition > pressedButton.clickedPosition) {
								gridTile.clickedPosition -= 1;
							}
						}
						pressedButton.clickedPosition = -1;
					}
					if (clickCount == currentGame.getWordLength()) {
						
						if (tracker.isWordInTheList(guessWord)) {
							message.setText("Correct!");
							
							numWords.setText("Number of Words = "
									+ String.valueOf(currentGame.getNumberWords())
									+ "\n You Have Found = "
									+ foundWordList.size());
							String foundList = "";
							for (String string : foundWordList) {
								foundList += string + "\n";
							}
							words.setText(foundList);
//							wordListPanel.revalidate();
							java.util.Timer timer = new java.util.Timer();
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.GREEN);
							}
							long pause = 0;
							for (GridTile gridTile : gridTiles) {
								if (gridTile.clickedPosition > -1) {
									pause += 500;
									timer.schedule(new TimerTask() {
										@Override
										public void run() {
											if (currentGame.getDuplicate()) {
												gridTile.setVisible(false);
												
											} else {
												gridTile.setBackground(Color.YELLOW);
											}
											gridTile.clickedPosition = -1;
										}
									}, pause);
								}
							}
							clickCount = 0;
							guessWord = new String[currentGame.getWordLength()];
							for (AnswerTile answerTile : answerTiles) {
								timer.schedule(new TimerTask() {
									@Override
									public void run() {
										answerTile.setText(" ");
										answerTile.setBackground(Color.YELLOW);
									}
								}, pause);
								pause -= 500;
							}
							//the panel shuts down at this point and reverts to original
							columnPanel.setVisible(true);
						} else {
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.RED);
								message.setText("Incorrect Keep Trying!");
							}
						}

					}
				}
				if (arg0.getSource() instanceof AnswerTile) {
					AnswerTile pressedButton = (AnswerTile) arg0.getSource();
					if (pressedButton.getText().equals(" ")) {
						return;
					}
					if (pressedButton.getBackground().equals(Color.RED)) {
						for (AnswerTile answerTile : answerTiles) {
							answerTile.setBackground(Color.YELLOW);
							message.setText("You can do it!");
						}
					}
					guessWord[pressedButton.tileId] = "";
					pressedButton.setText(" ");
//					pressedButton.getParent().revalidate();
					clickCount--;
					for (GridTile gridTile : gridTiles) {
						if (gridTile.columnNum == pressedButton.tileId) {
							gridTile.clickedPosition = -1;
							gridTile.setBackground(Color.YELLOW);
							gridTile.setSelected(false);
//							gridTile.getParent().revalidate();
						}
					}

				}
			}
		}
	}

	class InOrderDropCLick implements MouseListener {
		long timer = 0;

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			timer = System.currentTimeMillis();

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			if (System.currentTimeMillis() - timer < 150) {
				if (arg0.getSource() instanceof GridTile) {
					GridTile pressedButton = (GridTile) arg0.getSource();

					for (GridTile gridTile : gridTiles) {
						if (gridTile.columnNum == pressedButton.columnNum
								&& gridTile.clickedPosition > -1
								&& gridTile.tileId != pressedButton.tileId) {
							return;
						}
					}
					if (clickCount == currentGame.getWordLength()) {
						if (pressedButton.clickedPosition < 0) {
							return;
						}
					}

					if (pressedButton.clickedPosition == -1) {
						AnswerTile at = answerTiles.get(clickCount);
						at.character = pressedButton.character;
						at.setText(pressedButton.character);
						at.setVisible(true);
						at.getParent().revalidate();
						pressedButton.setBackground(Color.WHITE);
						pressedButton.getParent().revalidate();
						guessWord[clickCount] = pressedButton.character;
						pressedButton.clickedPosition = clickCount++;
					} else {
						AnswerTile nt = answerTiles
								.get(pressedButton.clickedPosition);
						if (nt.getBackground().equals(Color.RED)) {
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.YELLOW);
							}
						}
						shiftAnswerTilesLeft(answerTiles,
								pressedButton.clickedPosition);
						nt.getParent().revalidate();
						pressedButton.setBackground(Color.YELLOW);
						pressedButton.getParent().revalidate();
						clickCount--;
						guessWord[pressedButton.clickedPosition] = "";
						for (GridTile gridTile : gridTiles) {
							if (gridTile.clickedPosition > pressedButton.clickedPosition) {
								gridTile.clickedPosition -= 1;
							}
						}
						pressedButton.clickedPosition = -1;
					}
					if (clickCount == currentGame.getWordLength()) {
						if (tracker.isWordInTheList(guessWord)) {
							String foundWord = "";
							for (String string : guessWord) {
								foundWord += string;
							}
							foundWordList.add(foundWord);
							generateWordListPanel();
//							numWords.setText("Number of Words = "
//									+ String.valueOf(currentGame
//											.getNumberWords())
//											+ "\n You Have Found = "
//											+ foundWordList.size());
//							String foundList = "";
//							for (String string : foundWordList) {
//								foundList += string + "\n";
//							}
//							words.setText(foundList);
//							wordListPanel.revalidate();
							java.util.Timer timer = new java.util.Timer();
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.GREEN);
							}
							long pause = 0;
							for (GridTile gridTile : gridTiles) {
								pause = 500;
								if (gridTile.clickedPosition > -1) {
									pause = pause * (gridTile.columnNum + 1);
									timer.schedule(new TimerTask() {

										@Override
										public void run() {
											if (currentGame.getDuplicate()) {
												gridTile.setVisible(false);
											} else {
												gridTile.setBackground(Color.YELLOW);
											}
											gridTile.clickedPosition = -1;
										}
									}, pause);

								}
							}
							pause = 500 * guessWord.length;
							clickCount = 0;
							guessWord = new String[currentGame.getWordLength()];
							for (AnswerTile answerTile : answerTiles) {
								timer.schedule(new TimerTask() {

									@Override
									public void run() {
										answerTile.setText(" ");
										answerTile.setBackground(Color.YELLOW);
									}
								}, pause);
								pause -= 500;

							}
						} else {
							for (AnswerTile answerTile : answerTiles) {
								answerTile.setBackground(Color.RED);
							}
						}

					}
				}
				if (arg0.getSource() instanceof AnswerTile) {
					AnswerTile pressedButton = (AnswerTile) arg0.getSource();
					if (pressedButton.getText().equals(" ")) {
						return;
					}
					if (pressedButton.getBackground().equals(Color.RED)) {
						for (AnswerTile answerTile : answerTiles) {
							answerTile.setBackground(Color.YELLOW);
						}
					}
					guessWord[pressedButton.tileId] = "";
					shiftAnswerTilesLeft(answerTiles, pressedButton.tileId);
					pressedButton.getParent().revalidate();
					clickCount--;
					for (GridTile gridTile : gridTiles) {
						if (gridTile.clickedPosition == pressedButton.tileId) {
							gridTile.clickedPosition = -1;
							gridTile.setBackground(Color.YELLOW);
							gridTile.setSelected(false);
							gridTile.getParent().revalidate();
						}
						if (gridTile.clickedPosition > pressedButton.tileId) {
							gridTile.clickedPosition--;
						}
					}

				}
			}
		}
	}

	public class DragAndDrop implements MouseListener, MouseMotionListener {
		long timer = 0;
		boolean pickingup = true;

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	public void shiftAnswerTilesLeft(List<AnswerTile> atList,
			int currentLocation) {
		while (currentLocation < atList.size() - 1) {
			atList.get(currentLocation).setText(
					atList.get(++currentLocation).getText());
		}
		atList.get(currentLocation).setText(" ");
	}

	public void shiftAnswerTilesRight(List<AnswerTile> atList,
			int currentLocation) {
		while (currentLocation > 0) {
			atList.get(currentLocation).setText(
					atList.get(--currentLocation).getText());
		}
		atList.get(currentLocation).setText(" ");
	}
	
}
