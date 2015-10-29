import java.util.ArrayList;

/*********************************************
 * Point:
 * A Point class has a row and a column
 * Point is used to represent a location on a grid
 *
 * Constructed with a row and column
 *
 *********************************************/



public class Point 
{
		
	//========================Fields=================\\
	
	private int row;   					// the row
	private int column;  				// the column

	   
	   
	   //============================The Constructor===============\\
	   public Point(int a, int b)
	   {
		   row = a;
		   column = b;
	   }
	   

	   
	   //==========================Accessors==============================\\
	   public int getRow(){return row;}
	   public int getCol(){return column;}
 
	   
	   
	   
/*************************************************************************************************************
 * USER METHODS ~ clone, getPointLine,equals,isContained, toString
 * 
 * clone: makes a deep copy of the current point. It is used to prevent multiple references to the same object
 * 
 * getPointLine: used to determine a continuous span of points between two anchor points
 * 				 the method is overloaded to accept a pair of row/column as anchors as well
 *
 * equals: returns true if two points have the same contents
 * 
 * isContained: returns true if the points in the arraylist of points in the second parameter can all be found in
 * 				the arraylist of points in the first parameter.
 * 				Used to prevent overlapping
 * toString: returns a string with the point's row and column
 *****************************************************************************************************************/
	 
	   public Point clone()
	   {
		   return new Point(row,column);
	   }
	   
	   public static ArrayList<Point> getPointLine(Point a, Point b)
	   {
		  ArrayList<Point> list = new ArrayList<Point>();
		  if(a.getCol()==b.getCol())
			{
				for(int i = Math.min(a.getRow(), b.getRow()); i<= Math.max(a.getRow(),b.getRow()); i++){
					list.add(new Point(i,a.getCol()));
				}
				return list;
			}
			// do the anchor points stipulate a horizontal line sequence?
			else if (a.getRow()==b.getRow())
			{
				for(int i = Math.min(a.getCol(), b.getCol()); i<= Math.max(a.getCol(),b.getCol()); i++){
					list.add(new Point(a.getRow(),i));
				}
				return list;
			}
			// or do we have a diagonal?
			else if (Math.abs(a.getRow()-b.getRow())==Math.abs(a.getCol()-b.getCol()))
			{
				if (a.getCol() < b.getCol() && a.getRow()<b.getRow())
				{
					for (int i = 0; i + a.getCol() <= b.getCol();i++)
						list.add(new Point(a.getRow()+i, a.getCol()+i));
					return list;
				}
				else if (a.getCol() > b.getCol() && a.getRow() <b.getRow())
				{
					for (int i =0; a.getCol()-1 >= b.getCol(); i++)
						list.add(new Point(a.getRow()+i, a.getCol()-i));
					return list;
				}
				else if (a.getCol()< b.getCol() && a.getRow()> b.getRow())
				{
					for (int i=0; a.getRow()-i >= b.getRow(); i++)
						list.add(new Point(a.getRow()-i,a.getCol()+i));
					return list;
						
				}
				else if(a.getCol() > b.getCol() && a.getRow() > b.getRow())
				{
					for (int i =0; a.getRow() -i >= b.getRow(); i++)
						list.add(new Point(a.getRow()-i,a.getCol()-i));
					return list;
				}
				
				
			
			}
			return null;
	   }
	   
	   public static ArrayList<Point> getPointLine(int row1, int col1, int row2, int col2)
	   {
		   return  getPointLine(new Point(row1,col1), new Point(row2,col2));
	   }
	
	   public boolean equals(Point p)
	   {
		   return this.getCol()==p.getCol() && this.getRow()==p.getRow();
	   }
	 
	   public static boolean isContained(ArrayList<Point>a, ArrayList<Point>b)
	   {
		   //if one is null, then it is impossible
		   if (a==null || b==null || b.size()> a.size())
			   return false;
		   boolean isMatch = false;
		
		   //consider each point in b...
		   for (Point pB: b)
		   {
			 
		   //consider each point in a....
			   for(Point pA: a){
				 
				   if (pA.equals(pB))
					   isMatch = true;
			   }
		   
		   //if there wasn't a match return false;
		   if (!isMatch)
			   return false;
		   isMatch = false;
		   }
		   return true;
	   }
	   
	   public String toString()
	   {
		   return "Point at row: "+row+" column: "+column;
	   }
}//end of class body

