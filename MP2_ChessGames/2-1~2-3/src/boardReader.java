import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;

public class boardReader
{
	private int colNumber;
	private int rowNumber;
	private int[][] array;

	public boardReader()
	{
		colNumber=7;
		rowNumber=7;
		array=new int[rowNumber][colNumber];

		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				array[i][j]=0;
			}
		}
	}
	public void readMaze()//read first and then initialize
	{			

	}
	public int getRowNumber()
	{
		return this.rowNumber;
	}
	public int getColNumber()
	{
		return this.colNumber;
	}
	public void printMaze()
	{
		int col=0;
		int row=0;
		for(row=0;row<rowNumber;row++)
		{
				for(col=0;col<colNumber;col++)
				{
					System.out.print(array[row][col]);
				}
				System.out.println();
		}
	}
	public int[][] getArray()
	{
		return array;
	}
}	