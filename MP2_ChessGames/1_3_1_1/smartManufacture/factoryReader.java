import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;

public class factoryReader
{
	private int colNumber;
	private int rowNumber;
	private int[][] factoryArray;
	private String stringToBeParsed;
	public factoryReader()
	{
		String stringCharacter="ABCDE";
		Random random=new Random();
		colNumber=8;
		rowNumber=8;
		factoryArray=new int[rowNumber][colNumber];
		
		stringToBeParsed="";
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				stringToBeParsed=stringToBeParsed+stringCharacter.charAt(random.nextInt(5));
			}
		}
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				factoryArray[i][j]=0;
			}
		}
	}
	public void readMaze()//read first and then initialize
	{			
		int i=0;
		int j=0;
		j=0;
		for(i=0;i<rowNumber;i++)
		{
			for(j=0;j<colNumber;j++)
			{
				if(stringToBeParsed.charAt(i*rowNumber+j)=='A')
				{
					factoryArray[i][j]=1;
				}
				else if(stringToBeParsed.charAt(i*rowNumber+j)=='B')
				{
					factoryArray[i][j]=2;
				}	
				else if(stringToBeParsed.charAt(i*rowNumber+j)=='C')
				{
					factoryArray[i][j]=3;
				}
				else if(stringToBeParsed.charAt(i*rowNumber+j)=='D')
				{
					factoryArray[i][j]=4;
				}
				else if(stringToBeParsed.charAt(i*rowNumber+j)=='E')
				{
					factoryArray[i][j]=5;
				}
				//System.out.println(j);
				//print maze
				//System.out.print(line.charAt(i));//clean it
			}
		}
			//print maze
		System.out.println();//clean it
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
					System.out.print(factoryArray[row][col]);
				}
				System.out.println();
		}
	}
	public int[][] getFactoryArray()
	{
		return factoryArray;
	}
}	