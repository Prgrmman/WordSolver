/****************************************************
 * PointChar:
 * An extension of point to include a character and
 * a boolean that functions as an on and off switch
 * 
 * Constructed with a row, column, and letter
 * 
 **************************************************/



public class PointChar extends Point 
{
	  
	   
	//========================Fields========================================\\
	private char letter; 			// the letter at the current grid spot
	private boolean isSelected; 	// determines if the point location has been selected as part of a found word
	   
	   
	 //============================The Constructors===============\\
	   public PointChar(int a, int b, char c)
	   {
		   super(a,b);
		   letter = c;
		   isSelected = false;
	   }
	   
	   //this constructor is only used to create deep copies of a PointChar object.
	   // note: during initialization in a puzzle grid, isSelected should ALWAYS be false
	   public PointChar(int a, int b, char c, boolean d)
	   {
		   super(a,b);
		   letter = c;
		   isSelected = d;
		   
		   
	   }
	   
	   //==========================Accessors==============================\\
	   public char getChar(){return letter;}
	   public boolean getSelected(){return isSelected;}
	   
	   //========================Mutators=================================\\
	   //one a PointChar is turned on, it will not turn off
	   public void selected(){isSelected = true;}
	   
	   
	   
	   
/*************************************************************************************
 *  USER METHODS ~ clone, equals, toString
 *  
 *  clone: used to make a deep copy of PointChar to prevent confusion of references
 *  
 *  equals: returns true if the contents of two PointChar are equal
 *  
 *  toString: returns a string with the row, column, letter, and selected state
 *  
 ***************************************************************************************/
	
	   public PointChar clone()
	   {
		   return new PointChar(getRow(),getCol(),letter,isSelected);
	   }
	  
	   public boolean equals(PointChar p)
	   {
		   return super.equals(p) && this.getChar() == p.getChar();
	   }
	   
	   public String toString()
	   {
		   return "PointChar Letter "+letter+" at row: " + getRow()+" column: " +getCol()+" selected: "+getSelected();
	   }
}//end of class body
