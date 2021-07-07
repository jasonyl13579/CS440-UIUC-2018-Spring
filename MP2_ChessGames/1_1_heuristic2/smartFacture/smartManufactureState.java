import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.ArrayList;

public class smartManufactureState
{
	private smartManufactureState parent;
	private int[][] array;
	private int rowNumber;
	private int colNumber;
	private int rowCount;
	private int colCount;
	private int pathCount;
	private int evaluation;
	private int move;
	public int currentPlace=0;
	public int cost;
	private ArrayList<ArrayList<Integer>> gadgets;
	
	public smartManufactureState(int[][] array,int rowNumber,int colNumber,int rowCount,int colCount,ArrayList<ArrayList<Integer>> gadgets)
	{
		this.array=array.clone();
		this.rowNumber=rowNumber;
		this.colNumber=colNumber;
		this.rowCount=rowCount;
		this.colCount=colCount;
		this.pathCount=0;
		this.gadgets=new ArrayList();
		
		for(int i=0;i<rowCount;i++)
		{
			ArrayList<Integer> gadget = new ArrayList();
			for(int j=0;j<colCount;j++)
			{
				gadget.add(gadgets.get(i).get(j));
			}
			this.gadgets.add(gadget);
		}
	}
	public void setParent(smartManufactureState parent)
	{
		this.parent=parent;
	}
	public smartManufactureState getParent()
	{
		return parent;
	}
	public ArrayList<ArrayList<Integer>> getGadgets()
	{
		ArrayList<ArrayList<Integer>> result=new ArrayList();
		for(int i=0;i<rowCount;i++)
		{
			ArrayList<Integer> temp = new ArrayList();
			for(int j=0;j<colCount;j++)
			{
				temp.add(this.gadgets.get(i).get(j));
			}
			result.add(temp);
		}
		return result;
	}
	public int[][] getArray()
	{
		return array.clone();
	}
	public int getRowNumber()
	{
		return rowNumber;
	}
	public int getColNumber()
	{
		return colNumber;
	}
	public int getRowCount()
	{
		return rowCount;
	}
	public int getColCount()
	{
		return colCount;
	}
	public void printArray()
	{
		int i=0;
		int j=0;
		for(i=0;i<rowNumber;i++)
		{
			for(j=0;j<colNumber;j++)
			{
				System.out.print(array[i][j]);
			}
			System.out.println();
		}
	}
	public void printArrayList()
	{
		int i=0;
		int j=0;
		for(i=0;i<rowCount;i++)
		{
			for(j=0;j<colCount;j++)
			{
				System.out.print(gadgets.get(i).get(j));
			}
			System.out.println();
		}
	}
	public void setEvaluation(int evaluation)
	{
		this.evaluation=evaluation;
	}
	public int getEvaluation()
	{
		return this.evaluation;
	}
	public void setMove(int move)
	{
		this.move=move;
	}
	public int getMove()
	{
		return this.move;
	}
	public void setPathCount(int pathCount)
	{
		this.pathCount=pathCount;
	}
	public int getPathCount()
	{
		return this.pathCount;
	}
	public void setCurrentPlace(int currentPlace)
	{
		this.currentPlace=currentPlace;
	}
	public int getCurrentPlace()
	{
		return this.currentPlace;
	}
	public void setCost(int cost)
	{
		this.cost=cost;
	}
	public int getCost()
	{
		return this.cost;
	}
}