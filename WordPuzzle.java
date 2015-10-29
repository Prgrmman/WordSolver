import java.util.ArrayList;
import java.util.Arrays;


/****************************************************************************************
 * WordPuzzle:
 * 
 * This is the class that is responsible
 * for actually solving the puzzle. This class
 * is versatile, and gives the programmer
 * more than one way to show the solved puzzle
 * The WordPuzzle knows each coordinate list of every
 * found word. It also knows the which letters have
 * been "highlighted" or selected. The programmer could
 * show a solved puzzle based on which characters are
 * selected, or they could use and manipulate the coordinate lists from the
 * words. This implementation does the latter.
 * 
 * 
 * 
 * Constructed with a string (the puzzle text) and an arraylist of strings (the words)
 *
 ****************************************************************************************/


public class WordPuzzle 
{
	
	//===============================Fields================================\\
	private PointChar[][] grid;  			// the letter grid
	private ArrayList<Word> words;     		// the words from the word bank
	
	
	 
	
	//============================Constructor================================\\
	public WordPuzzle(String puzzle, ArrayList<String>list)
	{
		words = new ArrayList<Word>();
		for(String s: list)
			words.add(new Word(s));
		
		grid = constructGrid(puzzle);
		sortWords();
	}
	//================================Accessors=====================================\\
	
	//get the PointChar at a point
	public PointChar get(Point a)
	{
		return get(a.getRow(),a.getCol());
	}
	
	//get the PointChar at (row,col)
	public PointChar get(int row, int col)
	{
		return grid[row][col];
	}

	// returns a string with the puzzle text, formatted with newlines
	public String gridToString()
	{
		String result="";
		for (int r=0; r<grid.length; r++){
			for(int c=0; c<grid[0].length;c++){
				result+=""+grid[r][c].getChar();
			}
			result+="\n";
		}
		return result;
		
	}
	 // returns all unused characters 
	   public String getUnusedChar()
	   {
		   String result = "";
		   for (int r = 0; r <grid.length; r++){
			   for (int c = 0; c < grid[0].length; c++){
				  if(!get(r,c).getSelected())
					  result+=""+get(r,c).getChar();
			   }
		   }
		   return result;
	   }
	//returns a string made up of all words in the state var words
	public String getAllWordsToString()
	{
		String result ="";
		for (Word w: words)
			result+=w.getWord()+", ";
		return result.substring(0,result.length()-1);
			
	}
	// Returns an arraylist of words
	   public ArrayList<Word> getWords()
	   {
		   ArrayList<Word> list = new ArrayList<Word>();
		   for (Word w: words)
		   {
			   list.add(w.clone());
		   }
		   return list;
	   }
	//==========================Mutators==============================\\
	 // sets PointChars as being selected
	   private void setSelected(ArrayList<Point> points)
	   {
		   for (Point p: points)
		   {
			   get(p).selected();
		   }
	   }
	
	
/***************************************************************************************
 * USER METHODS SECTON 1: Helper Methods ~sortWords,getBiggestWordIndex
 *                        constructGrid, stripNewline, assembleChar, invert,
 *                        doesSpanOverlapOthers
 *
 * These methods are used during initialization and during the search process
 * 
 * sortWords: this sorts the words so they are ordered from biggest to smallest
 * 			  this is necessary to avoid errors in overlapping words
 * 
 * getBiggestWordIndex: used by sortWords, returns the longest word found in
 *                      the arraylist words
 * 
 * constructGrid: initialized state var grid to a 2D matrix of PointChar that
 *                represents the puzzle
 * 
 * stripNewLine: removes all newlines from a string. Used by constructGrid
 * 
 * assembleChar: returns a string of the letters represented by the Point arraylist
 *               in the parameter
 * 
 * invert: returns a string backwards. Used to find words spelled backward in
 * 		   the puzzle
 * 
 * doesSpanOverlapOthers: returns true if the arraylist in the parameter
 *                        can be completely contained by the span of an
 *                        existing, found word. Used in the solve methods
 *                        
 *                        
 *****************************************************************************************/
	
	
	
	//sorts words from largest to smallest size
	private void sortWords()
	{
		ArrayList<Word> sorted = new ArrayList<Word>();
		while (words.size()!=0)
		{
			int i = getBiggestWordIndex();
			sorted.add(words.remove(i));
		}
		words = sorted;
		
		
		
	}
	
	private int getBiggestWordIndex()
	{
		int biggest = 0;
		for( int i = 0; i < words.size();i++)
		{
			Word w = words.get(i);
			if (w.getWord().length()>words.get(biggest).getWord().length())
				biggest = i;
		}
		return biggest;
	}
	
	private PointChar[][] constructGrid(String str)
	{
		
		int nGap = str.indexOf('\n');
		int column = (nGap==-1)?str.length():nGap ;
		int rows = 1; // there will always be at least one row
		for(char a: str.toCharArray())
		{
			if (a=='\n')
				rows++;
		}
		
		
		PointChar[][] temp = new PointChar[rows][column];
		
		str = stripNewline(str);
		
		
		int count = 0;
		for(int r = 0; r < rows; r++){
			for (int c= 0; c < column; c++)
			{
				temp[r][c]= new PointChar(r,c,str.charAt(count++));
			}	
		}
		
	
		return temp;
	}
	
	private String stripNewline(String s)
	{
		String result ="";
		for(char a: s.toCharArray()){
			if(a!='\n')
				result+=""+a;
		}
		return result;
	}
	
	private String assembleChar(ArrayList<Point> list)
	{
		String result = "";
		for (Point c: list)
		{
			result+=""+get(c).getChar();
		}
		return result;
		
	}
	
	public static String invert(String str)

	{
		String result="";
		for(int i = str.length()-1; i>=0;i--)
			result+= ""+str.charAt(i);
		return result;
	}
	
	private boolean doesSpanOverlapOthers(ArrayList<Point> span)
	{
		  
		   for (Word w:words)
		   {
			   ArrayList<Point> first = w.getSpan();
			   ArrayList<Point> second = span;
			   if (Point.isContained(first, second))
				   return true;
		   }
		   return false;
	  }
	
	
/****************************************************************
 * USER METHODS SECTION 2: Solve Methods ~searchRows, searchCol,
 * 						   searchDiagForward, searchDiagBackward,
 * 						   solve
 * 
 * These methods are used to solve the puzzle. The end result is
 * that all located words will have properly set point lists that
 * identify their location and the letters within these solved words
 * will be set to selected.
 * 
 * 
 * 
 * While these may methods seem very similar, 
 * (in fact some parts are identical) they perform their designated
 * search operations slightly differently. Therefore, the consolidation of
 * these methods or creation of additional helper methods was unnecessary
 * 
 * 
 * 
 * seachRows: for the word given in the parameter, it will search for it in all of the rows,
 *            forward and backward. Returns an arraylist of points where word is located
 * 
 * searchCol: for the word given in the parameter, it will search for it in all of the
 * 			  columns, forward and backward. Returns an arraylist of points where word is located
 * 
 * searchDiagForward: for the word given in the parameter, it will search for in in the diagonals
 *                    that start in the direction of top left-hand corner and end in the direction of the
 *                    bottom right-hand corner, forward and backward.
 *                    Returns an arraylist of points where word is located.
 * 
 * searchDiagBackward: for the word given in the parameter, it will search for in in the diagonals
 *                    that start in the direction of the bottom left-hand corner and end in the direction of the
 *                     top right-hand corner, forward and backward.
 *                     Returns an arraylist of points where word is located
 * 
 * solve: uses all of the methods above as if searches for each word in the arraylist words. It returns the words
 * 	      that it doens't find
 * 
 
 */
	
	private ArrayList<Point> searchRows(Word word)
	{
	    // the point span of a given row
		ArrayList<Point> currentSpan = null;
		
		ArrayList<ArrayList<Point>>allFoundInstances = new ArrayList<ArrayList<Point>>();
		
		
		// if the word is longer than the firs row why bother
		if (word.getWord().length() <= grid[0].length){
		   
		   for(int r = 0; r < grid.length; r++){
			
			currentSpan = Point.getPointLine(r,0,r,grid[0].length-1);
		
			String wordText = word.getWord().toUpperCase();
			String line = assembleChar(currentSpan);
			if (word.getWord().length()> grid[0].length || (line.indexOf(wordText) == -1 && line.indexOf(invert(wordText))==-1))
			{
			
				//do nothing, because we didn't find the word
			}
			
			else{
			// else we find its span and set it
			// well there could be more than one
			//start of test code
			int start;
			int end;
			
				
				
			//end of test code
				start= (line.indexOf(wordText)==-1)?line.indexOf(invert(wordText)):line.indexOf(wordText);
				end = start + wordText.length()-1;
				ArrayList<Point> foundSpan = Point.getPointLine(new Point(r,start), new Point(r,end));
				allFoundInstances.add(foundSpan);
				}
		   }
		}
		
		//now look through allFoundInstances to see if there are overlaps
		for(ArrayList<Point>span:allFoundInstances)
		{
			if (!doesSpanOverlapOthers(span))
			{
				return span;
			}
		}
		
		
		return null;
	}
	
   private ArrayList<Point> searchCol(Word word)
   {
	   // the point span of a given col
		ArrayList<Point> currentSpan = null;
		ArrayList<ArrayList<Point>>allFoundInstances = new ArrayList<ArrayList<Point>>();
		
		
		// if the word is longer than the first col why bother
		if (word.getWord().length() <= grid.length){
		   
		   for(int c = 0; c < grid[0].length; c++){
			
			currentSpan = Point.getPointLine(0,c,grid.length-1,c);
		
			String wordText = word.getWord().toUpperCase();
			String col = assembleChar(currentSpan);
		
			if (word.getWord().length()> grid[0].length || (col.indexOf(wordText) == -1 && col.indexOf(invert(wordText))==-1))
			{
				//do nothing basically...we only care about if this doesn't happen
			}
			
			else{
			// else we find its span and set it
				int start= (col.indexOf(wordText)==-1)?col.indexOf(invert(wordText)):col.indexOf(wordText);
				int end = start + wordText.length()-1;
				ArrayList<Point> found =Point.getPointLine(new Point(start,c), new Point(end,c));
				allFoundInstances.add(found);
				}
		   }
		}
		for(ArrayList<Point>span:allFoundInstances)
		{
			if (!doesSpanOverlapOthers(span))
			{
				return span;
			}
		}
		return null;
   }

   private ArrayList<Point> searchDiagForward(Word word)
   {
	   //im gonna have to do this twice....maybe more 
	   ArrayList<Point> currentSpan = null;
		ArrayList<ArrayList<Point>>allFoundInstances = new ArrayList<ArrayList<Point>>();
	   boolean forwardDone = false;
	   // start on top left-hand corner and go down through the rows while remaining on the first column
	   String wordText = word.getWord().toUpperCase();
	   String diag = "";
	   int r = 0;
	   int c = grid[0].length-1;
	   
	   
	   
	   while (!forwardDone){
		   
		   	// we need to determine what the end point will be
		    
		   	int a = r; //col
		    int b = c; //row
		    while (a < grid.length-1 && b < grid[0].length-1)
		    {
		    	a++;
		    	b++;
		    }
		   
		    currentSpan = Point.getPointLine(r, c,a,b);
		    diag = assembleChar(currentSpan);
		    
		    
		    if (!(diag.indexOf(wordText)==-1 && diag.indexOf(invert(wordText))== -1))
		    {
		    
		    	int x1= (diag.indexOf(wordText)==-1)?diag.indexOf(invert(wordText)):diag.indexOf(wordText);
		    	Point start = currentSpan.get(x1);
		    	Point end = currentSpan.get(x1+wordText.length()-1);
		    	ArrayList<Point> found = Point.getPointLine(start,end);
		    	allFoundInstances.add(found);
		    }
		    // this that the first part of the search is done
		    if(c>0)
		    {
		    	c--;
		    }
		    else if (c==0)
		    {
		    	r++;
		    }
		    
		    if (c==0 && r==grid.length)
		    	forwardDone = true; 
	   }
		for(ArrayList<Point>span:allFoundInstances)
		{
			if (!doesSpanOverlapOthers(span))
			{
				return span;
			}
		}
	   return null;      
   }
  
   private ArrayList<Point> searchDiagBackward(Word word)
   {
	   ArrayList<Point> currentSpan = null;
	   ArrayList<ArrayList<Point>>allFoundInstances = new ArrayList<ArrayList<Point>>();
	   boolean backwardDone = false;
	   // start on the lower right hand corner
	   String wordText = word.getWord().toUpperCase();
	   String diag = "";
	   int r = grid.length-1;
	   int c = grid[0].length-1;
	   
	   while(!backwardDone){
		   // we need to determine what the end point will be
			int a = r; //col
		    int b = c; //row
		    while (a >0 && b <grid[0].length-1)
		    {
		    	a--;
		    	b++;
		    }
		    currentSpan = Point.getPointLine(r, c,a,b);
		    diag = assembleChar(currentSpan);
		    
		    if (!(diag.indexOf(wordText)==-1 && diag.indexOf(invert(wordText))== -1))
		    {
		    	int x1= (diag.indexOf(wordText)==-1)?diag.indexOf(invert(wordText)):diag.indexOf(wordText);
		    	Point start = currentSpan.get(x1);
		    	Point end = currentSpan.get(x1+wordText.length()-1);
		    	ArrayList<Point> found = Point.getPointLine(start,end);
		    	allFoundInstances.add(found);
		    }
		    if (c >0)
		    {
		    	c--;
		    }
		    else if (c==0)
		    {
		    	r--;
		    }
		    if (c==0 && r==-1)
		    {
		    	backwardDone = true;
		    }
	   }
	   
	   for(ArrayList<Point>span:allFoundInstances)
		{
			if (!doesSpanOverlapOthers(span))
			{
				return span;
			}
		}
	   return null;
   }
   
   public ArrayList<Word> solve()
	{
		ArrayList<Point> span;
		ArrayList<Word> missing = new ArrayList<Word>();  // a list of missing words
		
		for(int i = 0; i < words.size(); i++)
		{
			Word word = words.get(i);
			if (searchRows(word)!= null)
			{
				span = searchRows(word);
				word.setSpan(span);
				setSelected(span);
			}
			else if (searchCol(word)!= null)
			{
				span = searchCol(word);
				word.setSpan(span);
				setSelected(span);
			}
			else if (searchDiagForward(word)!= null)
			{
				span = searchDiagForward(word);
				word.setSpan(span);
				setSelected(span);
			}
			else if (searchDiagBackward(word)!= null)
			{
				span = searchDiagBackward(word);
				word.setSpan(span);
				setSelected(span);
			}
			else{
				missing.add(word);
			}
			
		}
		if (missing.size()==0)
			return null;
		return missing;
	}

}//end class body
