import java.util.Stack;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class depthFirst
{
	private int[][] array;
	private int rowNumber;
	private int colNumber;
	private Stack<point> path;
	private point currentLocation;
	private Set<point> foodLocations;
	//for debug
	private plotGraph plotMaze;
	private int countPlot;
	//for debug
	public depthFirst(int[][] array,int rowNumber,int colNumber,point currentLocation,Set<point> foodLocations)
	{
		this.array=array;
		this.rowNumber=rowNumber;
		this.colNumber=colNumber;
		this.currentLocation=currentLocation;
		this.foodLocations=foodLocations;
		path=new Stack();
		//for debug
		plotMaze=new plotGraph(array,rowNumber,colNumber);
		countPlot=0;
		//for debug
	}
	public void depthFirstAlgorithm()//main function
	{
		//path.push(currentLocation);
		int currentRow=currentLocation.getRow();
		int currentCol=currentLocation.getCol();
		int choiceNumber=0;
		//single food point
		point foodLocation=new point(-1,-1);
		for(Iterator<point> it=foodLocations.iterator();it.hasNext();)
		{
			point foodTemp=it.next();
			foodLocation=foodTemp;
		}
		//single food point
		
		while((((array[currentRow-1][currentCol]!=2)&&(array[currentRow-1][currentCol]!=100))||((array[currentRow+1][currentCol]!=2)&&(array[currentRow+1][currentCol]!=100))||((array[currentRow][currentCol-1]!=2)&&(array[currentRow][currentCol-1]!=100))||((array[currentRow][currentCol+1]!=2)&&(array[currentRow][currentCol+1]!=100)))&&((currentRow!=foodLocation.getRow())||(currentCol!=foodLocation.getCol())))
		{
			printPosition();
			choiceNumber=0;
			if((array[currentRow-1][currentCol]!=2)&&(array[currentRow-1][currentCol]!=99)&&(array[currentRow-1][currentCol]!=100))
			{
				path.push(currentLocation);
				//change the maze element
				array[currentRow][currentCol]=99;
				//change the maze element
				currentLocation=new point(currentRow-1,currentCol);
				currentRow=currentRow-1;
			}
			else if((array[currentRow+1][currentCol]!=2)&&(array[currentRow+1][currentCol]!=99)&&(array[currentRow+1][currentCol]!=100))
			{
				path.push(currentLocation);
				//change the maze element
				array[currentRow][currentCol]=99;
				//change the maze element
				currentLocation=new point(currentRow+1,currentCol);
				currentRow=currentRow+1;
				array[currentRow][currentCol]=3;
			}
			else if((array[currentRow][currentCol-1]!=2)&&(array[currentRow][currentCol-1]!=99)&&(array[currentRow][currentCol-1]!=100))
			{
				path.push(currentLocation);
				//change the maze element
				array[currentRow][currentCol]=99;
				//change the maze element
				currentLocation=new point(currentRow,currentCol-1);
				currentCol=currentCol-1;
				array[currentRow][currentCol]=3;
			}
			else if((array[currentRow][currentCol+1]!=2)&&(array[currentRow][currentCol+1]!=99)&&(array[currentRow][currentCol+1]!=100))
			{
				path.push(currentLocation);
				//change the maze element
				array[currentRow][currentCol]=99;
				//change the maze element
				currentLocation=new point(currentRow,currentCol+1);
				currentCol=currentCol+1;
				array[currentRow][currentCol]=3;
			}
			else
			{
				array[currentRow][currentCol]=100;//never visited again
				currentLocation=path.pop();
				currentRow=currentLocation.getRow();
				currentCol=currentLocation.getCol();
				array[currentRow][currentCol]=3;
			}
			//for test
			countPlot++;
			if(countPlot%30==0||((currentRow==foodLocation.getRow())&&(currentCol==foodLocation.getCol())))
			{
				/*
				//wait 100ms
				try
				{
					Thread.sleep(100);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				//wait 100ms
				plotMaze.deletePanel();
				*/
				plotMaze=new plotGraph(array,rowNumber,colNumber);
			}
			//for test
		}
	}
	public void printPosition()
	{
		System.out.print("Row:");
		System.out.print(currentLocation.getRow());
		System.out.print(" Col:");
		System.out.println(currentLocation.getCol());
	}
	public int[][] getArray()
	{
		return array;
	}
}