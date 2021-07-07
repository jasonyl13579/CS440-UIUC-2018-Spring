import java.util.Set;
import java.util.Stack;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class aStarSearchM
{
	private int[][] array;
	private int rowNumber;
	private int colNumber;
	private point startLocation;
	private point currentLocation;
//	private List<point> foodLocations;
	private Set<point> foodLocations;
	private point[] foodLocationsArray;
	private Queue<aStarNode> path;
	private aStarNode root;
	private aStarNode goal;
	//for debug
	private plotGraph plotMaze;
	private double countPlot;
	
	//for multiple points
	private int[][] costMatrix;
	private ArrayList<ArrayList<Queue<aStarNode>>> pathMatrix;
	private List<aStarNode> oldFood;
	private List<aStarNode> newFood;
	
	public aStarSearchM(int[][] array,int rowNumber,int colNumber,point startLocation,Set<point> foodLocations)
	{
		this.array=array;
		this.rowNumber=rowNumber;
		this.colNumber=colNumber;
		this.startLocation=startLocation;
		this.currentLocation = startLocation;
		this.foodLocations = foodLocations;
		this.foodLocationsArray = new point[foodLocations.size()];
		this.foodLocationsArray = foodLocations.toArray(this.foodLocationsArray);
		this.root=new aStarNode();
		this.goal = new aStarNode();
		this.costMatrix = new int[foodLocations.size() + 1][foodLocations.size()];	//one more startLocation of src
		this.pathMatrix = new ArrayList();
		this.oldFood = new ArrayList();
		this.newFood = new ArrayList();
		path=new LinkedList();
	
		//for debug
		//plotMaze=new plotGraph(array,rowNumber,colNumber);
		countPlot=0;
		//for debug
		
		for(Iterator<point> it = foodLocations.iterator();it.hasNext();)
		{
			point foodTemp = it.next();
			this.foodLocations.add(foodTemp);
		}
		
//		System.out.println("Before sorting : ");
//		for(Iterator<point> it = this.foodLocations.iterator();it.hasNext();)
//		{
//			point foodTemp = it.next();
//			System.out.println(getManDistance(foodTemp,currentLocation));
//		}
		
//		sortList();
//		foodLocation = this.foodLocations.remove(0);
		
//		System.out.println("After sorting : ");
//		for(Iterator<point> it = this.foodLocations.iterator();it.hasNext();)
//		{
//			point foodTemp = it.next();
//			System.out.println(getManDistance(foodTemp,currentLocation));
//		}
//		
//		System.out.println("interrupt");
	}
	
	public void aStarSearchMAlgorithm()
	{
		double numNode = 0;
		//build the costMatrix and pathMatrix
		for(int i = 0; i < this.foodLocations.size() + 1; i++) {
			ArrayList<Queue<aStarNode>> path1DArray = new ArrayList(); 
					
			for(int j = 0; j < this.foodLocations.size(); j++) {
				int tempArray[][] = new int [rowNumber][colNumber];
				for(int k = 0; k < rowNumber; k++)
					for(int m = 0; m < colNumber ; m++) {
						if( (k!=foodLocationsArray[j].getRow() || m!=foodLocationsArray[j].getCol()) && this.array[k][m] == 4 ) 
							array[k][m] = 1;
						else
							tempArray[k][m] = array[k][m];
					}
				Set<point> tempSet = new HashSet<point>();
				tempSet.add(foodLocationsArray[j]);
				if(i == this.foodLocations.size()) {
					aStarSearch aStarTest = new aStarSearch(tempArray, this.rowNumber, this.colNumber, this.startLocation, tempSet);
					aStarTest.aStarSearchAlgorithm();
					costMatrix[i][j] = aStarTest.getCost();
					path1DArray.add(aStarTest.getPath());
				}
				else {
					aStarSearch aStarTest = new aStarSearch(tempArray, this.rowNumber, this.colNumber, foodLocationsArray[i], tempSet);
					aStarTest.aStarSearchAlgorithm();
					costMatrix[i][j] = aStarTest.getCost();
					path1DArray.add(aStarTest.getPath());
				}
			}
			pathMatrix.add(path1DArray);
		}
		
//		for(int i = 0; i < this.foodLocations.size() + 1; i++) {
//			for(int j = 0; j < this.foodLocations.size(); j++)
//				System.out.print(Integer.toString(costMatrix[i][j]) + " ");
//			System.out.println();
//		}
		
		//start searching
		int row, col;
		
		root.setRow(this.startLocation.getRow());
		root.setCol(this.startLocation.getCol());
		root.setTravelDistance(0);
		root.setTargetDistance(0);
		root.setTotalDistance();
		root.setParent(null);
		root.addFood(foodLocations.size()); 		//add startLocation
		newFood.add(root);
		numNode++;
		
		while(!newFood.isEmpty()) {
			countPlot++;
			aStarNode nodeNow = new aStarNode();
			nodeNow = newFood.get(0);
				
			newFood.remove(nodeNow);
			oldFood.add(nodeNow);
			
			//check termination
			if(isEnd()) {
//				System.out.println("countplot :" + Integer.toString(countPlot));
//				System.out.println(oldFood.get(oldFood.size()-1).getTotalDistance());
				goal = oldFood.get(oldFood.size()-1);
				constructPath();
				break;
			}
			
			//expanding, dest (nodeTemp), src (nodeNow)
			for(int i = 0; i < this.foodLocations.size(); i++) {
				aStarNode nodeTemp = new aStarNode();
				if(nodeNow.isVisited(i))
					continue;
					
				nodeTemp.setRow(foodLocationsArray[i].getRow());
				nodeTemp.setCol(foodLocationsArray[i].getCol());
				nodeTemp.copyFoods(nodeNow);
				
				if(nodeNow == root) {
					int cost = costMatrix[this.foodLocations.size()][indexInArray(nodeTemp)];
					nodeTemp.setTravelDistance(cost + nodeNow.getTravelDistance());
				}
				else {
					int cost = costMatrix[indexInArray(nodeNow)][indexInArray(nodeTemp)];
					nodeTemp.setTravelDistance(cost + nodeNow.getTravelDistance());
				}
				//System.out.print("Row :" + Integer.toString(nodeTemp.getRow()) + " Col :" + Integer.toString(nodeTemp.getCol()) + "\n");
				nodeTemp.setTargetDistance(getHeuristic(nodeTemp,i));
//				nodeTemp.setTargetDistance(getHeuristic(nodeTemp));
				nodeTemp.setTotalDistance();
				nodeTemp.addFood(i);
				newFood.add(nodeTemp);
				numNode++;
			}
			sortNewFood();
			//System.out.print("Expanded node : " + Double.toString(numNode) + "\n");
		}
		System.out.print("Expanded node : " + Double.toString(numNode) + "\n");
	}
	
	public boolean isInOldFood(aStarNode nodeTemp)
	{
		boolean existed = false;
		for(Iterator<aStarNode> it = oldFood.iterator();it.hasNext();)
		{
			aStarNode nodeInOld = it.next();
			if(nodeTemp.getRow() == nodeInOld.getRow() && nodeTemp.getCol() == nodeInOld.getCol())
				existed = true;
		}
		return existed;
	}
	public boolean isInOldFood(point foodTemp)
	{
		boolean existed = false;
		for(Iterator<aStarNode> it = oldFood.iterator();it.hasNext();)
		{
			aStarNode nodeInOld = it.next();
			if(foodTemp.getRow() == nodeInOld.getRow() && foodTemp.getCol() == nodeInOld.getCol())
				existed = true;
		}
		return existed;
	}

	public boolean isEnd()
	{
		boolean end = true;
		aStarNode lastNode = oldFood.get(oldFood.size()-1);
		for(int i = 0; i < foodLocations.size()+1; i++) {
			end = end && lastNode.isVisited(i);
		}
		
		return end;
	}
	
	public int getHeuristic(aStarNode newNode, int newFood)
	{
		int sum = 0;
		int min;
		int count = 0;
		
		//add all
		for (int col = 0; col < this.foodLocations.size(); col++) {
			min = -1;
			if (col == newFood || newNode.isVisited(col))
				continue;
			for(int row = 0; row < this.foodLocations.size(); row++) {
				if(newNode.isVisited(row))
					continue;
				if((min > costMatrix[row][col] || min == -1) && row != col) {
					min = costMatrix[row][col];
				}
			}
			sum += min;
			count++;
		}
		sum -= foodLocations.size() - count;
		
		return sum;
	}
//	public int getHeuristic(aStarNode newNode)
//	{
//		int sum = 0;
//		int colIndex;
//		int rowIndex;
//		
//		//add all
//		for (int i = 0; i < this.foodLocations.size(); i++)
//			for(int j = 0; j < this.foodLocations.size(); j++)
//				sum += costMatrix[i][j];
//		
//		//minus old and now
//		for(Iterator<aStarNode> it = oldFood.iterator(); it.hasNext();) {
//			aStarNode oldNowNode = it.next();
//			if(oldNowNode.getRow() == this.startLocation.getRow() && oldNowNode.getCol() == this.startLocation.getCol())
//				continue;
//			colIndex = indexInArray(oldNowNode);
//			rowIndex = colIndex;
//			//System.out.print("Row :" + Integer.toString(oldNowNode.getRow()) + " Col :" + Integer.toString(oldNowNode.getCol()) + "\n");
//			for(int i = 0; i < this.foodLocations.size(); i++) {
//				sum = sum - costMatrix[i][colIndex];
//				if(!isInOldFood(foodLocationsArray[i]))
//					sum = sum - costMatrix[rowIndex][i];
//			}
//		}
//		
//		//minus new
//		colIndex = indexInArray(newNode);
//		for(int i = 0; i < this.foodLocations.size(); i++) 
//			if(!isInOldFood(foodLocationsArray[i]))
//				sum = sum - costMatrix[i][colIndex];
//		
//		return sum;
//	}

	public int[][] getArray()
	{
		return array;
	}
	
	public int indexInArray(aStarNode p)
	{
		for(int i = 0; i < this.foodLocations.size(); i++) {
			if(foodLocationsArray[i].getRow() == p.getRow() && foodLocationsArray[i].getCol() == p.getCol())
				return i;
		}
		return -1;
	}
	
	public void sortNewFood()
	{
		newFood.sort(new Comparator<aStarNode>() {
			@Override
			public int compare(aStarNode n1, aStarNode n2) {
				int d1 = n1.getTotalDistance();
				int d2 = n2.getTotalDistance();
				if(d1 == d2)
					return 0;
				return (d1 > d2) ? 1:-1;
			}
		});
	}
	
	public void printPath()
	{
		for(Iterator<point> foodIt = foodLocations.iterator(); foodIt.hasNext();) {
			point foodTemp = foodIt.next();
			array[foodTemp.getRow()][foodTemp.getCol()] = 4;
		}
		for(Iterator<aStarNode> pathIt = path.iterator(); pathIt.hasNext();) {
			aStarNode pathNode = pathIt.next();
			System.out.print("Row:" + Integer.toString(pathNode.getRow()) + " Col:" + Integer.toString(pathNode.getCol()) + "\n");
			if(!isInOldFood(pathNode) || ((pathNode.getRow() == startLocation.getRow()) && (pathNode.getCol() == startLocation.getCol())) )
				array[pathNode.getRow()][pathNode.getCol()] = 99;
			if(isInOldFood(pathNode) && (array[pathNode.getRow()][pathNode.getCol()] != 5)) {
				System.out.print("Food -> Row:" + Integer.toString(pathNode.getRow()) + " Col:" + Integer.toString(pathNode.getCol()) + "\n");
				array[pathNode.getRow()][pathNode.getCol()] = 5;
			}
			//plotMaze = new plotGraph(array,rowNumber,colNumber);
		}
		
		System.out.print("Total cost : " + Integer.toString(goal.getTotalDistance() + foodLocations.size()) + "\n");
		
//		plotMaze=new plotGraph(array,rowNumber,colNumber);
	}
	
	public void constructPath()
	{
		int col, row;
		aStarNode nodeTemp;
		Queue<aStarNode> pathTemp = new LinkedList<aStarNode>();
		Stack<aStarNode> nodeStack = new Stack<aStarNode>();
		Integer [] visitedFoodArray = new Integer[foodLocations.size()+1];
		visitedFoodArray = (Integer[]) goal.getVisitedFoods().toArray(visitedFoodArray);
		
		for(int i = 0; i < foodLocations.size(); i++) {
			col = visitedFoodArray[i+1];			//dest
			row = visitedFoodArray[i];			//src
//			System.out.print("Row :" + Integer.toString(row) + " Col :" + Integer.toString(col) + "\n");
			pathTemp = this.pathMatrix.get(row).get(col);
			
//			System.out.print("countPlot : " + Integer.toString(countPlot) + "\n");
//			System.out.print("Food Row : " + Integer.toString(nodeNow.getRow()) + " | Food Col:" + Integer.toString(nodeNow.getCol()) + "\n");
//			for(Iterator<aStarNode> it = pathTemp.iterator(); it.hasNext();) {
//				aStarNode pathNode = it.next();
//				System.out.print("Row:"+Integer.toString(pathNode.getRow())+" Col:"+Integer.toString(pathNode.getCol()) + " | ");
//			}
//			System.out.println();
			
			Iterator<aStarNode> nodeIt = pathTemp.iterator();
			nodeTemp = nodeIt.next();
			while(!pathTemp.isEmpty()) 
				nodeStack.push(pathTemp.remove());
//			for(Iterator<aStarNode> it = nodeStack.iterator(); it.hasNext();) {
//				aStarNode stackNode = it.next();
//				System.out.print("Row:"+Integer.toString(stackNode.getRow())+" Col:"+Integer.toString(stackNode.getCol()) + " | ");
//			}
//			System.out.println();
			while(!nodeStack.isEmpty())
				path.add(nodeStack.pop());
		}
		path.add(goal);
	}
}
