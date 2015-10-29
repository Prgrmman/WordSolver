import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



/***********************************************************************
 * SolveWindow:
 * This is the window that will be displayed with the found
 * words and the puzzle itself. The user will have the option
 * of showing different found words within the puzzle
 * If the puzzle it too big to show on the users screen, then the puzzle
 * may be displayed in HTML
 *
 * Constructed with a string that represents the word search text
 * 	and an arraylist of words that represents the words from the word bank
 ***************************************************************************/

public class SolveWindow extends JFrame implements ActionListener, ItemListener, WindowListener{
	
	
	
	//====================Fields=========================================\\
	private WordPuzzle wordPuzz; 					// the actual puzzle
	private ArrayList<Word> foundWords;				// the words that were found by the solver
	private JPanel SolvePane;						// the solve panel and its contents for selecting and finding words
	private JComboBox wordSelector;					// a selection box to pick the word to search for
	private JButton resetButton; 					// a button that resets the puzzle grid from a faded state
	private PuzzleGUI backPane;						// responsible for drawing and animating puzzle
	private JButton showLeftOverChar; 				// shows all letters that are not part of a found word in the puzzle
	private JButton htmlButton;    					// a button that show the html output of the program
	private boolean isWindowSelect;					// represents if the window is active or inactive

	
	//=========================The Constructor================================\\
	public SolveWindow(String p, ArrayList<String>s)
	{
		
		//==================Initialize basic properties===========================\\
		super("Solver Window");
		setLookAndFeel();
		// time to design the window
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(this);
		isWindowSelect=  true;
		
		
		
		//=========================Initialize MouseMotionListener=========================\\
		// this allows SolveWindow to react to a mouse hover over the drawn puzzle
		MouseMotionListener backPaneListener = new MouseMotionListener(){
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			public void mouseMoved(MouseEvent arg0) {
				
				if (!backPane.animationInProcess() && backPane.isReset()&&backPane.isMouseInPuzzle(arg0) && isWindowSelect )
				{
					findWords();
					
				}
				
			}		
		};
		//=========================Create a new WordPuzzle Object, Initialize, and solve=============================\\
		
		wordPuzz = new WordPuzzle(p,s);
	

		// Tell the user if any words were missing
		ArrayList<Word> leftOver = wordPuzz.solve();  // solve it!
		if (leftOver != null)
			JOptionPane.showMessageDialog(null, "The Following word(s) were not found:\n"+(""+leftOver).substring(1,(""+leftOver).length()-1),
					"Missing Words", JOptionPane.INFORMATION_MESSAGE);
		

		//=============================Initialize backPane as PuzzleGUI===============================\\
		backPane = new PuzzleGUI(wordPuzz.gridToString());
		backPane.setBackground(new Color(0,0,0));
		
	
		
		//=============================Initialize Window and its size===============================\\
		// make the window fit the puzzle
		// if it gets to big, i may add a scroll bar or something...maybe
		int width = backPane.width+50;
		int height = backPane.height+wordPuzz.getWords().size()+100;
		// the min width is 302
		width = (width< 502)? 502: width;
		setSize(width,height); 
		
		
		add(backPane); // add the drawn puzzle
		setResizable(false);
		
		//===============================Initialize found words=======================\\
		foundWords = new ArrayList<Word>();
		for (Word w: wordPuzz.getWords())
		{
			if (w.getSpan() != null)
				foundWords.add(w);
		}
	
		
		//===============================Initialize the SovlvePane and its components================\\
		SolvePane = new JPanel();
		SolvePane.setLayout(new FlowLayout());
		
	
		if (foundWords.size()!=0 && doesWindowFitResolution()){
			
		
			String[] comboLabels = new String[foundWords.size()];
			for (int i = 0; i < foundWords.size();i++)
			{
				comboLabels[i] = foundWords.get(i).getWord();
			}
			
			wordSelector = new JComboBox(comboLabels);
			wordSelector.addItemListener(this);
			
			
			
			resetButton = new JButton("Reset");
			showLeftOverChar = new JButton("LeftOver");
			htmlButton = new JButton("Show HTML");
			resetButton.addActionListener(this);
			showLeftOverChar.addActionListener(this);
			htmlButton.addActionListener(this);
			SolvePane.add(resetButton);
			
			SolvePane.add(showLeftOverChar);
			SolvePane.add(htmlButton);
			SolvePane.add(wordSelector);
			add(SolvePane,BorderLayout.SOUTH);
			backPane.addMouseMotionListener(backPaneListener);
			
			
		//===================Handle Window/Word Errors====================================\\	
		}else{ //we found no words
		
			
			
			JLabel errorMes = new JLabel("No words found",SwingConstants.CENTER);
			errorMes.setForeground(Color.red);
			add(errorMes,BorderLayout.SOUTH);
			
		}
	
		 // we can't fit the puzzle in the screen
		if (!doesWindowFitResolution())
		{
			String[] option = {"Open Html view","Exit Program"};
			int response = JOptionPane.showOptionDialog(null,"Word Search Solver is unable to display the puzzle in your current screen resolution"
					,"Screen Resolution Problem",0,JOptionPane.ERROR_MESSAGE,null,option,option[1]);
		
			if (response == 0)
				showHTML();
			this.dispose();
		}
		else
			setVisible(true); // we are good to display as planned
		  this.setIconImage(null);
			ImageIcon img = new ImageIcon("iconMain.gif");
			
			setIconImage(img.getImage());

	}
/********************************************************
 *  COMPONENT EVENT LISTENER ~ actionPreformed, itemStateChanged
 *  
 *  actionPreformed:
 *  This method dictates what happens when the reset button is pushed,
 *  when the showLeftOverChar button is pushed, or when the htmlButton
 *  is pushed
 *  
 *  reset button clears out the result of the fade animation
 *  
 *  showLeftOverChar button show the characters not selected in the found words
 *  
 *  htmlButton opens an html file with the solved puzzle
 *  
 *  itemStateChanged:
 *  This method will also cause the drawn puzzle to reset if a new
 *  word is selected from the combo box
 *  
 *  
 **************************************************************/
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if (source == resetButton)
			backPane.reset();
		if (source == showLeftOverChar)
		{
			//format the left over letters so that they are easier to read
			// i am putting them on different lines after every 10th character
			
			String letterPrintout = "";
			int n = 0;
			for (char c: wordPuzz.getUnusedChar().toCharArray())
			{
				if (n!= 30)
				{
					letterPrintout+= c;
					n++;
				}
				else
				{
					letterPrintout+="\n";
					n= 0;
					
				}
			}
			//it's possible all letters were used
			String message = "Letters not used:\n";
			if (letterPrintout.length()==0)
				message = "All Leters were used";
			JOptionPane.showMessageDialog(null,message+letterPrintout,
					"LeftOver", JOptionPane.INFORMATION_MESSAGE);
		}
		// we want to open the html now
		if (source == htmlButton)
		{
			showHTML();
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange()==ItemEvent.SELECTED)
			backPane.reset();
		
	}
/****************************************************************************
 * WINDOW EVENT LISTENER ~ windowActivated, windowDeactivated
 * 						   windowClosded, windowClosing, windowDeiconified
 * 						   windowIconified, windowOpened
 * 
 * Ignored methods: windowClosded, windowClosing, windowDeiconified
 * 						   windowIconified, windowOpened
 * 
 * windowActivated: when the user has clicked the SolveWindow, that means that
 * 					they are actively using it. This sets isWindowSelect to true
 * 					and allows for the mouse hovering to trigger the fade animation
 * 
 * windowDeactivated: when the user clicks somewhere else away from the SolveWindow,
 * 					  that means they are not actively using it. This sets 
 * 					  isWindowSelect to false and disables the mouse hovering from
 * 					  triggering the fade animation
 * 			
 ****************************************************************************/
	public void windowActivated(WindowEvent e) {
		isWindowSelect = true;
	}
	public void windowDeactivated(WindowEvent e) {
		isWindowSelect = false;
	}
	
	
	public void windowClosed(WindowEvent e) {}

	public void windowClosing(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {}

	public void windowOpened(WindowEvent e) {}	
	
/******************************************************************************************
 * USER METHODS ~ setLookAndFeel, findWords, doesWindowFitResolution
 * 				  showHTML
 * 
 *  setLookandFeel: sets the style of the window to the second style found in the
 * 				   clients default style list
 *  
 *  findWords: finds a word in the drawn puzzle based on the current word in the combo box.
 *  
 *  doesWindowFitResolution: returns true if the SolveWindow can fit on a screen with a
 *  					     a given resolution. Prior to calling this method, the size
 *  					 	 of the SolveWindow should be set
 *  
 * showHTML: this method calls outputHTML in the PuzzleGUI backPane. It then opens the
 * 			 resulting html file with the client's default web browser
 *  
 ********************************************************************************************/
	
		private void setLookAndFeel()  
		{
			UIManager.LookAndFeelInfo[] laf = UIManager.getInstalledLookAndFeels();
			try{
				UIManager.setLookAndFeel(laf[1].getClassName());
				SwingUtilities.updateComponentTreeUI(this);
			}catch (Exception e){
				System.err.println("Couldn't set the look and feel"+e);
				
			}
		}
		
		private void findWords()
		{
			backPane.fade();
			String wordToFind = (String)wordSelector.getSelectedItem();
			for(Word w: foundWords)
			{
				if (w.getWord().equals(wordToFind))
					backPane.loadWord(w);
			}
			
			
		}

		public boolean doesWindowFitResolution()
		{
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			return (this.getWidth() < screenSize.getWidth() && this.getHeight() < screenSize.getHeight());
			
		}
		
		private void showHTML()
		{
			backPane.outputHTML(foundWords,wordPuzz.getUnusedChar()); 
			String fileName = "HtmlOutput.html";
			
			File htmlFile = new File(fileName);
			try {
				Desktop.getDesktop().open(htmlFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// say that we had a problem
				JOptionPane.showMessageDialog(null, "HTML file could not be opened","ERROR!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
			
		}

}//end of class body
