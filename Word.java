import java.util.ArrayList;

/******************************************************************
 * 
 * Word:
 * A word class was a string in it and an
 * arraylist of Points.
 * 
 * The string is a word from the word bank.
 * 
 * The Point list represents the word's "span", or how the word
 * is positioned in the puzzle. This span has an intial state of
 * being unfound, so it is set to null
 * 
 * Constructed with a string
 ******************************************************************/




public class Word 
{
	
	//=================================Fields====================================================\\
	private String word;				// the text of the word
	private ArrayList<Point> span; 		// the coordinates over which a word, if found, spans	
	
	//==============================The Constructor============================================\\
	public Word(String str)
	{
		word = str;
		span = null; // null unless the word is found in the puzzle
	}
	
	//==============================Accessos=====================================================\\
	public String getWord(){return word;}
	
	//returns a copy of the span
	public ArrayList<Point> getSpan()
	{
		if (span == null)
			return null;
		
		ArrayList<Point> temp = new ArrayList<Point>();
		for (Point p: span)
			temp.add(p.clone());
		return temp;
	}
	
	//===========================Mutators===================================================\\
	public void setSpan(Point a, Point b)
	{
		span = Point.getPointLine(a, b);
		
	}
	// overload of setSPan
	public void setSpan(ArrayList<Point> p)
	{
		span = p;
	}


/******************************************************************
 * USER METHODS ~ clone, isPointInWord, toString
 * 
 * clone: makes a deep copy of the current word. Used to prevent
 *        the confusion of multiple references to one word
 *        
 * isPointInWord: returns true if the Point in the parameter is
 *                found within the current word's span
 *                
 * toString: this functions the same as the method getWord, and
 * 			 the two may be used interchangeably 
 * 
 *******************************************************************/
	
	
	public Word clone()
	{
		Word w = new Word(word);
		w.setSpan(span);
		return w;
	}
	
	public boolean isPointInWord(Point a)
	{
		if (span ==null)
			return false;
		
		for (Point p: span)
		{
			if (p.equals(a))
				return true;
		}
		return false;
	}
	
	public String toString()
	{
		return getWord();
	}
	

}//end class body