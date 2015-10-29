import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


/********************************************************
 * PuzzleGUI:
 * This an extension of a JPanel. It overloads
 * the default paint method in order to show the
 * word search grid and to perform animations
 * 
 * Constructed with a string that represents the text
 * of the word search puzzle
 * 
 *********************************************************/




public class PuzzleGUI extends JPanel {
	
	//========================Fields=================\\
	private String puzzText;	 	// the puzzle text to be rendered
	public final int width;			// width of the puzzle in pixels
	public final int height;		// height of the puzzle in pixels
	private Color textColor;		// the color of the text within the shown puzzle
	private int drawMode;			// set to 0 or 1, with 0 as default mode 1 is used to draw an individual word
	private Word currentWord;   	// the word that will be drawn
	private Timer effectTimer;		// a timer used to control a text fade animation
	private Thread stopper;			// a thread used to stop fade animation after 2 seconds
	private boolean isAnimated;		// represents the state of the animation process
	private boolean isInBaseState; 	// represents the state of the PuzzleGUI
	private boolean isDrawing; 		// tells if the PuzzleGUI is currently rendering itself


	//======================The Constructor===============================\\
	public PuzzleGUI(String a)
	{
		
		super();
		puzzText = a;
		width = obtainWidth();
		height = obtainHeight();
		textColor = Color.black;
		drawMode = 0;
		currentWord = null;

		isAnimated = false;
		isInBaseState = true;
		isDrawing = false;
	
	}
	//========================Accessors====================================\\
	//tells if animation is occurring
	public boolean animationInProcess()
	{
		return isAnimated;
	}
	
	//tells if the PuzzleGUI is an reset orientation
	public boolean isReset()
	{
		return isInBaseState;
	}
	
/*******************************************************************************************************88
 * USER METHODS: paint, obtainWidth, obtainHight, drawPuzz, fade,loadWord
 * 				 reset, isMouseInPuzzle, outputHTML
 * 
 * paint: overrides the JPanel default paint method in order to draw
 * 		  raw text onto the screen
 * 
 * obtainWidth: returns the puzzle's width in pixels
 * 
 * obtainHeight: returns the puzzle's height in pixels
 * 
 * drawPuzz: draws the word search. Draw mode 0 draws the whole puzzle
 * 		     Draw mode 1 draws the word found in the state var "currentWord"
 * 
 * fade: opens a separate thread and repaints the puzzle in draw mode 0, making the text
 * 		 more white each time. After 2 seconds, the fade animation is terminated 
 * 
 * loadWord: works in conjunction with fade. It sets currentWord equal to the word supplied in its
 * 		     parameter. Then it sets draw mode to 1 and draws currentWord at the same time that the fade
 * 			 method is fading the puzzle in draw mode 0. The effect is that the puzzle fades except for
 * 			 the loaded word. The word in the parameter must not be null
 * 
 * reset: used to redraw the puzzle in draw mode 0 with black text. This method will only run if their is no
 * 		  animation or drawing occurring. This precaution makes it relatively thread-safe
 * 
 * isMouseInPuzzle: returns true if the mouse event in the parameter has a position inside of the drawn puzzle
 * 
 * outputHTML: outputs the puzzle in an html format instead of drawing the puzzle. Its functionality is explained
 * 			   below
 **********************************************************************************************************************/
	public void paint(Graphics g)
	{
		super.paint(g);
		isDrawing =true;
		drawPuzz(g);
		isDrawing = false;
	}
	
	private int obtainWidth()
	{
		return (puzzText.indexOf('\n'))*18;
	}
	
	private int obtainHeight()
	{
		int count = 0;
		for (char c: puzzText.toCharArray())
		{
			if(c=='\n')
				count++;
		}
		return  count*18;
	}
	
	public void drawPuzz(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D)g;
			g2d.setFont(new Font("Courier New",Font.PLAIN, 16));
		
			g.setColor(new Color(255,255,255));
			g.fillRect(18,0, width, height);
			if (drawMode ==0)
			{
		
		
			
	
			g.setColor(new Color(255,255,255));
			g.fillRect(18,0, width, height);
			int x = 18;
			int y = 18;
			g.setColor(textColor);
			char[] grid = puzzText.toCharArray();
			for (int i =0; i < grid.length; i++)
			{
				char c = grid[i];
				if (c != '\n')
				{
				
					g2d.drawString(""+c,x,y);
					x += 18;
				}
				else
				{
					x = 18;
					y += 18;
				}
			}
			
		}
		else if (drawMode==1)
		{
		 //otherwise we draw only the word specified as currentWord
			Color oldCol = textColor; // save the current color for later
			g.setColor(Color.black);
			int r=0; // the puzzle row
			int c=0;  // the puzzle column
			int x = 18;
			int y = 18;
			for (char letter: puzzText.toCharArray())
			{
				Point point = new Point(r,c);
				if (letter != '\n')
				{
					//is this in the word
					
						if((currentWord.isPointInWord(point))){
							g.setColor(Color.black);
							g2d.drawString(""+letter,x,y);
							x += 18;
							c++;
						
						}else{
							g.setColor(oldCol);
							g2d.drawString(""+letter,x,y);
							x += 18;
							c++;
						}
					
				}
				else
				{
					x = 18;
					y+= 18;
					c = 0;
					r++;
				}
				
			}
			
		}
			
	}
	
	public void fade()
	{
		
		
		ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                textColor = textColor.brighter();
            	repaint();
            }
        };
       
        
       
		
        effectTimer = new Timer();
        effectTimer.scheduleAtFixedRate(new TimerTask() {
        	  @Override
        	  public void run() {
        	    // Your database code here
        		  textColor = textColor.brighter();
        		  repaint();
        	  }
        	},0, 75);
        
       
		stopper = new Thread(){
			public void run()
			{
				isAnimated = true;
				isInBaseState = false;
				try {
					sleep(990);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
				
					effectTimer.cancel();
					effectTimer.purge();
				
					try {
						sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					isAnimated = false;
					
			
				
				
			}
		};
		stopper.start();
		
		

		
		
	
        
       
       
        
        
	}
	
	public void reset()
	{
	
		if (!isAnimated && !isDrawing)
		{
			textColor = Color.black;
			drawMode = 0;
			currentWord = null;
			repaint();
			isInBaseState = true;
			
		}
	}
	
	public void loadWord(Word w)
	{
		currentWord = w;
		drawMode = 1;
		repaint();
	}
	
	public boolean isMouseInPuzzle(MouseEvent e)
	{
		return e.getX() >= 18 && e.getX() <= width+18 && e.getY() >= 0 && e.getY() <= height;
	}

	
/********************************************************************************************************
 *  this is a very important method
 *  	If the PuzzleGUI deems the puzzle too big to render in a traditional window,
 *  it will create an html file. The html file will show multiple grids with all of the words found
 *  separately in different grids. If the file cannot be written, an error message will be shown
 *  
 * The first parameter in outputHTML is an arraylist or words. For every word in the list, an individual puzzle grid
 * will be generated. Within each grid, each respective word will be found  
 * 
 * The second parameter is a string that takes represents unused letters. As PuzzleGUI does not contain
 * a WordPuzzle field, it is quicker for an outside object to pass it this string than it is to determine the
 * unused characters again.
 *********************************************************************************************************/
	public void outputHTML(ArrayList<Word> words, String unusedChar)
	{
		try {
	        BufferedWriter out = new BufferedWriter(new FileWriter("HtmlOutput.html"));
	           //the first opening line of html
	           	out.write("<html><title>WORD SOLVER</title>\n");
	           	
	           	// open the head
	           	out.write("<head>\n");
	           	
	           	//set the style
	           	out.write("<style>");
	           	out.write("h2 {text-align:left}\n");
	           	//close style
	           	out.write("</style>\n");
	           	//close the head
	           	out.write("</head>\n");
	           	
	           	if (words.size()!= 0)
	           	{
		           	for (Word findMe: words)
		           	{
		           		out.write("<h2>The word found is: "+findMe.getWord()+"</h2>");
		           	
		           		int r = 0;   // the current row in the puzzle grid
		           		int c = 0;  // the current column in the puzzle grid
		           	
		           		//start the table
		           		out.write("<table border = \"0\">\n");
		           	
		           		for (char letter: puzzText.toCharArray())
		           		{
		           		
		           			//start a row
		          
		           		
		           			// this handles a line break
		           			if (letter == '\n')
		           			{
		           				out.write("</tr>\n");
		           				out.write("<tr>");
		           				c = 0;
		           				r++;
		           			}
		           			else
		           			{
		           				Point point = new Point(r,c);
		           				if (findMe.isPointInWord(point))
		           				{
		           				out.write("<td><font color = \"#AA0000\">"+ letter+"</font></td>" );
		           				}
		           				else
		           					out.write("<td><font color = \"#CCCCCC\">"+ letter+"</font></td>" );
		           				c++;
		           			}
		           		}
		           		out.write("</tr></table><br><br>\n");
		           	}
	           	}
	           	
	           	else
	           		out.write("<h1>No words Found</h1>"); // if we didn't find any words
	           	//now we write the unused characters
	           	if (unusedChar.length()==0)
	           		out.write("<h2>All letters were used</h2>");
	           	else
	           	{
	           		out.write("<h2>Unused Letters:</h2>");
	           		int x= 0;
	           		String line = "";
	           		for (char letter: unusedChar.toCharArray())
	           		{
	           			if (x != 50)
	           			{
	           				line+=""+letter;
	           				x++;
	           			}
	           			else
	           			{
	           				out.write("<p>"+line+"</p><br>\n");
	           				line = "";
	           				x = 0;
	           			}
	           			
	           		}
	           		out.write("<p>"+line+"</p><br>\n");
	           		
	           	}
	           	out.write("</html>");
	            
	            out.close();
	        } catch (IOException e) {
	        	//we couldn't write the file, so show error message
	        	JOptionPane.showMessageDialog(null, "HTML file could not be written","ERROR!",JOptionPane.ERROR_MESSAGE);
	        }
	    
	}
}//end class body