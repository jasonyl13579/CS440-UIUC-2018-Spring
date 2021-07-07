import java.util.Set;
import java.util.HashSet;
import java.lang.InterruptedException;

public class runMaze
{
	public static void main(String[] args)
	{
        //type the file name here
		mazeReader maze=new mazeReader("sokoban_extra2.txt");
		maze.readMaze();
		int[][] ourMazeArray=maze.getMazeArray();
		int rowNumber=maze.getRowNumber();
		int colNumber=maze.getColNumber();
		int algorithmChoice = 2;//1 for hardcode, 2 for algorithm
		
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
		if (algorithmChoice == 1){
			sokoban sokobanTest=new sokoban(ourMazeArray,rowNumber,colNumber,currentLocation,storagePoints,boxPoints,finishPoints);
			sokobanTest.hard_coded_sokobanAlgorithm();		
		}
		else if (algorithmChoice == 2)
		{
			sokoban sokobanTest=new sokoban(ourMazeArray,rowNumber,colNumber,currentLocation,storagePoints,boxPoints,finishPoints);
			sokobanTest.sokobanAlgorithm();
		}
	}
}
