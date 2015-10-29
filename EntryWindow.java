import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/*****************************************************
 * EntryWindow:
 * This is the first user the window sees
 * The user will use this window to enter
 * their word search and words.
 * 
 *
 ********************************************************/



public class EntryWindow extends JFrame implements ActionListener
{
	private ArrayList<String> words; //list of words from the puzzle
	private String puzzle;           // the puzzle text iteself
	
	
	//====================Fields=========================================\\
	
	private JPanel Frame;   		 	// the container that holds everything
	private	JTextArea puzzInput;		// the puzzle input area
	private JTextArea wordBank;			// the input box for words
	private JButton solve; 				// the solve button
	
	
	//============The Constructor======================\
	public EntryWindow()
	{
		
		//Set basic properties
		super("Solver v1.0 by Jonathan Terner");
		setLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel puzzContainer;  	// container that holds the contents of the puzzle grid

		JPanel titleBar;		// the title bar
		JScrollPane wordPanel;  // the left hand panel that contains the wordbank
		JPanel  wordContainer;  // the scrollpanel that contains the wordbank
		JScrollPane puzzPanel;   // the scroll panel that holds the puzzle
		
		
		//======================Initialize title bar=============================\\
		titleBar = new JPanel();
		titleBar.setLayout(new BorderLayout());
		titleBar.setBackground(new Color(185,185,255));
		JPanel botPanel = new JPanel();
		botPanel.setBackground(new Color(0,0,0));
		titleBar.add(botPanel,BorderLayout.SOUTH);
		JLabel text = new JLabel("WORD SEARCH SOLVER ENTRY",SwingConstants.CENTER);
	
		text.setFont(new Font("Arial",Font.BOLD, 15));
		titleBar.add(text);
		
		
		//========================Initialize word bank===============================\\
		wordContainer = new JPanel();
		wordContainer.setLayout(new BorderLayout(50,25));
		wordContainer.setBackground(new Color(210,210,210));
		wordContainer.setPreferredSize(new Dimension(300,500));
		wordContainer.add(new JLabel("ENTER WORDS HERE",SwingConstants.CENTER),BorderLayout.NORTH);
		
		wordBank = new JTextArea();
		wordBank.setFont(new Font("Arial Black",Font.PLAIN, 14));
		wordPanel = new JScrollPane(wordBank,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		wordPanel.setPreferredSize(new Dimension(200,400));
		wordContainer.add(wordPanel, BorderLayout.CENTER);
		
		
		//add the button in the bottom
		solve = new JButton("Solve!");
		solve.addActionListener(this);
		solve.setBounds(0, 0, 100, 100);
		JPanel butHolder = new JPanel();
		butHolder.setBackground(new Color(50,50,50));
	
		butHolder.add(solve,BorderLayout.CENTER);
		wordContainer.add(butHolder,BorderLayout.SOUTH);
	
		//==================Initialize the puzzle grid space===========================\\
		puzzContainer = new JPanel();
		puzzContainer.setLayout(new BorderLayout(50,25));
		puzzContainer.setBackground(new Color(210,210,210));
		puzzInput = new JTextArea();
		puzzInput.setFont(new Font("Courier New",Font.PLAIN, 16));
		
		// makes all text in input upper case
		puzzInput.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e) {
			    char keyChar = e.getKeyChar();
			    if (Character.isLowerCase(keyChar)) {
			      e.setKeyChar(Character.toUpperCase(keyChar));
			    }
			}
			  
		});
		
		puzzPanel = new JScrollPane(puzzInput);
		puzzPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		puzzPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		puzzPanel.setBackground(new Color(255,255,255));
		puzzPanel.setPreferredSize(new Dimension(500,500));
		puzzContainer.add(new JLabel("ENTER PUZZLE HERE",SwingConstants.CENTER),BorderLayout.NORTH);
		puzzContainer.add(puzzPanel,BorderLayout.CENTER);
		
		
		
		//==========================Initialize the frame=================================\\
		Frame = new JPanel();
		Frame.setBackground(new Color(100,100,100));
		setSize(800,600);
		setResizable(false);
		setLayout(new BorderLayout());
		Frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20,0));
		Frame.add(wordContainer);
		Frame.add(wordContainer);
		Frame.add(puzzContainer);
		add(Frame, BorderLayout.CENTER);
		add(titleBar, BorderLayout.NORTH);
		pack();
		setVisible(true);
		
		  this.setIconImage(null);
		ImageIcon img = new ImageIcon("iconMain.gif");
		setIconImage(img.getImage());
		
	
	
		
	}
	
/*****************************************************************************************************
 * COMPONENT EVENT LISTENER ~ actionPreformed
 * 
 * This method dictates what happens when the solve button is pushed
 * 
 * The solve button will not work as desired if:
 * 		-> The puzzle is improperly formatted (the puzzle cannot be a ragged grid, no numbers either)
 * 		-> No words have been entered
 * 		-> The user has entered the same word twice
 * 
 * In all of these cases, an error dialog will be shown, and the
 * user will be presented with the opportunity to fix their mistakes
 ***************************************************************************************************/
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
	
		boolean isError = false; // no error is assumed
		if (source ==solve)
		{
			words = new ArrayList<String>();
			String bank = wordBank.getText();
			bank = superTrim(bank);
			bank = removeAllSpace(bank);
			
			if (bank.length()==0)
			{
				JOptionPane.showMessageDialog(null, "Please Enter at least one word in the word bank","ERROR!",JOptionPane.ERROR_MESSAGE);
				isError = true;
			}else{
			//make a list from the words in the bank
			String newWord = "";
			for(int i =0; i < bank.length();i++)
			{
				if (bank.charAt(i)!='\n')
					newWord+= bank.charAt(i);
				else{
						words.add(newWord);
						newWord ="";
					}
				
			}
			words.add(newWord);
			}
			
			
			
			puzzle = puzzInput.getText();
			
			//get rid of all spaces
		
		
			puzzle = superTrim(puzzle);
			puzzle = removeAllSpace(puzzle);
			if (!isPuzzleValid(puzzle))
			{
				JOptionPane.showMessageDialog(null,"Your puzzle has not been properly formatted","ERROR!",JOptionPane.ERROR_MESSAGE);
				isError = true;
			}
			if (isWordbankError(bank))
			{
				JOptionPane.showMessageDialog(null,"Your words have not been properly formatted","ERROR!",JOptionPane.ERROR_MESSAGE);
				isError = true;
			}
			if (isDuplicateWords() && !isWordbankError(bank))
			{
				JOptionPane.showMessageDialog(null, "You have entered at least one word multiple times","ERROR!",JOptionPane.ERROR_MESSAGE);
				isError = true;
				
			}
			
				if (!isError)
				{
					//proceed to the next step
					
					this.dispose();
					//start new window
					SolveWindow solveWin = new SolveWindow(puzzle.toUpperCase(),words);
				
					
				
				}
		}
	}
	
/********************************************************************
 * 
 * USER METHODS ~ isPuzzleValid, superTrim, isDuplicateWords, setLookAndFeel,
 *                removeAllSpace, isWordbankError
 * 
 * isPuzzleValid: used to determine if the puzzle has been formatted correctly
 * 				  it will return false if:
 * 						-> there are extra newlines before the puzzle
 * 						-> the line widths are inconsistent
 * 						-> there is empty space between lines
 * 				  note: a puzzle is still properly formatted if it has mutliple new lines
 * 						at the end of it (the user pushed ENTER a bunch of times)
 * 
 * superTrim: removes extra newlines from the end of the puzzle text
 * 
 * isDuplicate: returns false if the user entered the same word more than once
 * 
 * setLookAndFeel: sets the style of the window to the second style found in the
 * 				   clients default style list
 * removeAllSpace: gets rid of all space in a string
 * 
 * isWordbankError: returns true if there is blank space between words in the word bank
 * 
 ********************************************************************/
	
	
	private boolean isPuzzleValid(String puzz)
	{
		
		if (puzz.length()<1)
			return false;
		if (puzz.charAt(puzz.length()-1)=='\n')
			puzz= puzz.substring(0,puzz.length()-1);
		
		int nGap = puzz.indexOf("\n");
	
		if (nGap ==-1)
			return true;
		int nCount = 0;
		for (int i =0; i < puzz.length(); i++)
		{
			if(puzz.charAt(i) != '\n')
			{
				nCount++;
			}else{
				if (nCount != nGap)
					return false;
				else
					nCount=0;
			}
		}
		return nGap == nCount;
	}
	
	private String superTrim(String str)
	{
		if (str.length()==0)
			return str;
		
		
		if (str.lastIndexOf("\n")== str.length()-1)
			return superTrim(str.substring(0,str.length()-1));
		else return str;
		
				
	}
	
	private boolean isDuplicateWords()
	{
		int count =0;
		for (String s: words)
		{
			for (String s2: words)
			{
				
				if (s2.equalsIgnoreCase(s))
					count++;
			}
			if (count > 1 )
				return true;
			count = 0;
		}
		return false;
	}
	
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
	private String removeAllSpace(String str)
	{
		String result="";
		for (char c: str.toCharArray())
		{
			if (Character.isLetter(c) || c == '\n')
				
				result+=c;
		}
		return result;
	}
	
	private boolean isWordbankError(String str)
	{
		// if two newlines in a row, than error

		if (!Character.isAlphabetic(str.charAt(0)))
				return true;
		
		
		int nCount = 0;
		for(char c: str.toCharArray())
		{
			if (c =='\n')
			{
				nCount++;
				System.out.println(nCount);
			}else
				nCount = 0;
			if (nCount == 2)
				return true;
			
		}
		return false;
	}
	
/*************************************
 * 
 * Main method:
 * 		constructs a new EntryWindow
 ***************************************/
	public static void main(String args[])
	{
		EntryWindow win = new EntryWindow();
	}
}//end of class body
