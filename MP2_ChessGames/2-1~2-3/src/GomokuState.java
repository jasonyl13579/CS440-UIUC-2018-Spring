import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.ArrayList;

public class GomokuState
{
	int[][] array;
	int rowNumber;
	int colNumber;
	int rowNow;
	int colNow;
	int nextMove;
	int lastMove;
	int winner;
	int player;
	Set<point> emptyLocations;
	Set<point> player1Locations;
	Set<point> player2Locations;
	int evaluation;
	public GomokuState(int[][] array,int rowNumber,int colNumber,int rowNow,int colNow,int player)
	{
		this.rowNumber=rowNumber;
		this.colNumber=colNumber;
		this.rowNow=rowNow;
		this.colNow=colNow;
		this.player=player;
		this.emptyLocations=new HashSet<point>();
		this.player1Locations=new HashSet<point>();
		this.player2Locations=new HashSet<point>();
		this.array=new int[rowNumber][colNumber];
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				this.array[i][j]=array[i][j];
			}
		}
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				if(array[i][j]==0)
				{
					emptyLocations.add(new point(i,j));
				}
				else if(array[i][j]==1)
				{
					player1Locations.add(new point(i,j));
				}
				else if(array[i][j]==2)
				{
					player2Locations.add(new point(i,j));
				}
			}
		}
	}
	public GomokuState(GomokuState other)
	{
		this.nextMove=other.nextMove;
		this.lastMove=other.lastMove;
		this.rowNumber=other.rowNumber;
		this.colNumber=other.colNumber;
		this.rowNow=other.rowNow;
		this.colNow=other.colNow;
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				this.array[i][j]=other.array[i][j];
			}
		}
	}
	public void moving(int player,int rowMove,int colMove)
	{
		array[rowMove][colMove]=player;
		for(Iterator<point> it=emptyLocations.iterator();it.hasNext();)
		{
			point emptyTemp=it.next();
			if(emptyTemp.getRow()==rowMove&&emptyTemp.getCol()==colMove)
			{
				it.remove();
			}
		}
		if(player==1)
		{
			player1Locations.add(new point(rowMove,colMove));
		}
		else if(player==2) // changed
		{
			player2Locations.add(new point(rowMove,colMove));
		}
	}
	public ArrayList neighbor(int row,int col)
	{
		ArrayList<point> neighbors = new ArrayList<point>();
		if(row-1>=0&&col-1>=0)
		{
			if(array[row-1][col-1]==0)
			{
				neighbors.add(new point(row-1,col-1));
			}
		}
		if(row-1>=0)
		{
			if(array[row-1][col]==0)
			{
				neighbors.add(new point(row-1,col));
			}
		}
		if(row-1>=0&&col+1<=colNumber-1)
		{
			if(array[row-1][col+1]==0)
			{
				neighbors.add(new point(row-1,col+1));
			}
		}

		if(col-1>=0)
		{
			if(array[row][col-1]==0)
			{
				neighbors.add(new point(row,col-1));
			}
		}
		if(col+1<=colNumber-1)
		{
			if(array[row][col+1]==0)
			{
				neighbors.add(new point(row,col+1));
			}
		}

		if(row+1<=rowNumber-1&&col-1>=0)
		{
			if(array[row+1][col-1]==0)
			{
				neighbors.add(new point(row+1,col-1));
			}
		}
		if(row+1<=rowNumber-1)
		{
			if(array[row+1][col]==0)
			{
				neighbors.add(new point(row+1,col));
			}
		}
		if(row+1<=rowNumber-1&&col+1<=colNumber-1)
		{
			if(array[row+1][col+1]==0)
			{
				neighbors.add(new point(row+1,col+1));
			}
		}
		
		return neighbors;
	}
	public void setEvaluation(int evaluation)
	{
		this.evaluation=evaluation;
	}
	public int getEvaluation()
	{
		return this.evaluation;
	}
}