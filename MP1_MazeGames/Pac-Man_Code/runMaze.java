import java.util.Set;
import java.util.HashSet;
import java.lang.InterruptedException;

public class runMaze
{
	public static void main(String[] args)
	{
        //type the file name of input
		mazeReader maze=new mazeReader(".txt");
		maze.readMaze();
		int[][] ourMazeArray=maze.getMazeArray();
		int rowNumber=maze.getRowNumber();
		int colNumber=maze.getColNumber();
		int algorithmChoice=1;//1 for depthFirst, 2 for breadthFirst, 3 for bestFirst, 4 for aStar, 5 for multiplePoints aStar, 6 for suboptimal search
		long time1 = 0, time2 = 0;
		
		point currentLocation=maze.getPosition();
		Set<point> foodPoints=maze.getFoodPoints();
		
		plotGraph plotMaze=new plotGraph(ourMazeArray,rowNumber,colNumber);//plot graph
		
		time1 = System.currentTimeMillis();
		try
		{
			Thread.sleep(100);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		if(algorithmChoice==1)
		{
			depthFirst depthFirstSearch=new depthFirst(ourMazeArray,rowNumber,colNumber,currentLocation,foodPoints);
			depthFirstSearch.depthFirstAlgorithm();
		}
		else if(algorithmChoice==2)
		{
			breadthFirst breadthFirstSearch=new breadthFirst(ourMazeArray,rowNumber,colNumber,currentLocation,foodPoints);
			breadthFirstSearch.breadthFirstAlgorithm();
			breadthFirstSearch.printPath();
		}
		else if(algorithmChoice==3)
		{
			bestFirst bestFirstSearch=new bestFirst(ourMazeArray,rowNumber,colNumber,currentLocation,foodPoints);
			bestFirstSearch.bestFirstAlgorithm();
		}
		else if(algorithmChoice==4)
		{
			aStarSearch aStarSearchTest=new aStarSearch(ourMazeArray,rowNumber,colNumber,currentLocation,foodPoints);
			aStarSearchTest.aStarSearchAlgorithm();
			aStarSearchTest.printPath();
		}
		else if(algorithmChoice == 5)
		{
			aStarSearchM aStarSearchMTest=new aStarSearchM(ourMazeArray,rowNumber,colNumber,currentLocation,foodPoints);
			aStarSearchMTest.aStarSearchMAlgorithm();
			aStarSearchMTest.printPath();
		}
        else if(algorithmChoice==6)
        {
            subOptimal subOptimalTest=new subOptimal(ourMazeArray,rowNumber,colNumber,currentLocation,foodPoints);
            subOptimalTest.subOptimalAlgorithm();
            subOptimalTest.printPath();
        }
        
		time2 = System.currentTimeMillis();
		System.out.print("Total time : " + (time2-time1)/1000 + "seconds\n");;
	}
}
