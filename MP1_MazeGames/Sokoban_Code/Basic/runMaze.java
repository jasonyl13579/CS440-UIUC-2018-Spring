import java.util.Set;
import java.util.HashSet;
import java.lang.InterruptedException;

public class runMaze
{
	public static void main(String[] args)
	{
        //type the file name here
		mazeReader maze=new mazeReader("sokoban1.txt");
		maze.readMaze();
		int[][] ourMazeArray=maze.getMazeArray();
		int rowNumber=maze.getRowNumber();
		int colNumber=maze.getColNumber();
		int algorithmChoice = 1;//1 for first heuristic, 2 for second heuristic
		
		point currentLocation=maze.getPosition();
		Set<point> storagePoints=maze.getStoragePoints();
		Set<point> boxPoints=maze.getBoxPoints();
		Set<point> finishPoints=maze.getFinishPoints();
		
		plotGraph plotMaze=new plotGraph(ourMazeArray,rowNumber,colNumber);//plot graph
		try
		{
			Thread.sleep(100);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		//test
		//maze.printFinishPositions();
		//test
        sokoban sokobanTest=new sokoban(ourMazeArray,rowNumber,colNumber,currentLocation,storagePoints,boxPoints,finishPoints,algorithmChoice);
        sokobanTest.sokobanAlgorithm();
	}
}
