import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class sokobanState
{
//	private int[][] arrayState;
//	private int[][] visitedArrayState;
//	private point currentPositionState;
	private Set<point> boxPoints;
	private Set<Integer> boxPointsInt;
//	private Set<point> storagePositionsState;
//	private Set<point> finishPositionsState;
	private float heuristic;
	private LinkedList<Integer> path;
	private int[][] arrayState;
	private float value;
	private int parentDir;
	private int rowNumber;
	private int colNumber;
	private int currentRow;
	private int currentCol;
	private boolean direction[];
	public sokobanState()
	{
		this.colNumber = 1;
		this.currentRow = 1;
		this.currentCol = 1;
		this.heuristic = 100000;
		this.parentDir = -1;
		direction = new boolean[4];
		for (int i=0 ; i<4 ; i++) direction[i] = true;
	}
	public sokobanState(int[][] arrayState,int rowNumber,int colNumber, int currentRow, int currentCol, Set<point> boxPoints, LinkedList<Integer> path, float heuristic, int parentDir)
	{
		this.rowNumber = rowNumber;
		this.colNumber = colNumber;
		this.currentRow = currentRow;
		this.currentCol = currentCol;
		this.heuristic = heuristic;
		this.parentDir = parentDir;
		this.boxPoints = (Set)((HashSet)boxPoints).clone();
		this.path = (LinkedList<Integer>) path.clone();
		this.boxPointsInt = new HashSet<Integer>();
		for (point p: boxPoints) {
			boxPointsInt.add(p.getInt(colNumber));
		}
		direction = new boolean[4];
		for (int i=0 ; i<4 ; i++) direction[i] = true;
		this.arrayState=new int[rowNumber][colNumber];
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				this.arrayState[i][j]=arrayState[i][j];
				
			}
		}
		//System.out.print("(2,2):"+this.arrayState[2][2]+"\n");
		//System.out.print("(2,1):"+this.arrayState[2][1]+"\n");
	}
	public void setDirection(int i, boolean x)
	{
		direction[i] = x;
	}
	public boolean[] getDirection()
	{
		return direction.clone();
	}
	public boolean getDirection(int i)
	{
		return direction[i];
	}
	public float getHeuristic()
	{
		return heuristic;
	}
	public void setHeuristic(float heuristic)
	{
		this.heuristic = heuristic;
	}	
	public LinkedList<Integer> getPath()
	{
		return (LinkedList)path.clone();
	}
	public float getValue()
	{
		return value;
	}
	public void setValue(float value)
	{
		this.value = value;
	}
	public Set<point> getBoxPoints()
	{
		return (Set)((HashSet)boxPoints).clone();
	}
	public Set<Integer> getBoxPointsInt()
	{
		return (Set)((HashSet)boxPointsInt).clone();
	}
	public int getCurrentRow()
	{
		return currentRow;
	}
	public int getCurrentCol()
	{
		return currentCol;
	}
	public int getParentDir()
	{
		return parentDir;
	}
	public boolean compareForbidState(Set<Integer>boxPointsInt,int currentRow,int currentCol)
	{
		/*for (Integer i: boxPointsInt) {
			System.out.print(","+i);
		}
		System.out.print("\n");
		for (Integer i: this.boxPointsInt) {
			System.out.print(","+i);
		}
		System.out.print("\n");*/
		if (boxPointsInt.equals(this.boxPointsInt) && currentCol == this.currentCol && currentRow == this.currentRow){
			System.out.print("Dead path! Col:" + currentCol + "Row:" + currentRow + "\n");
			return true;
		}
		return false;
	}
	public void printDirection()
	{
		for (int i=0 ; i<4 ; i++){
			System.out.print(direction[i]+".");
		}
		System.out.print("\n");
	}
	public void setArrayState(int[][] assignArray)
	{
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				this.arrayState[i][j]=assignArray[i][j];
			}
		}
	}
	public int[][] getArrayState()
	{
		return arrayState.clone();
	}
}