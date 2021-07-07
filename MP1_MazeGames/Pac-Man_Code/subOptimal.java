import java.util.Set;
import java.util.Stack;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class subOptimal
{
	private int[][] array;
	private int[][] visitedArray;
	private int rowNumber;
	private int colNumber;
	private int upCost;
	private int downCost;
	private int leftCost;
	private int rightCost;
	private int direction;//up:1,down:2,left:3,right:4
	private point startLocation;
	private point currentLocation;
//	private List<point> foodLocations;
	private Set<point> foodLocations;
	private point[] foodLocationsArray;
	private Queue<aStarNode> path;
	private aStarNode root;
	//for debug
	private plotGraph plotMaze;
	private int countPlot;
	
	//for multiple points
	private int[][] costMatrix;
	private ArrayList<ArrayList<Queue<aStarNode>>> pathMatrix;
	private Set<point> oldFood;
	private Set<point> newFood;
	private aStarNode pointNow;
	public subOptimal(int[][] array,int rowNumber,int colNumber,point startLocation,Set<point> foodLocations)
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
		this.costMatrix = new int[foodLocations.size() + 1][foodLocations.size()];	//one more startLocation of src
		this.pathMatrix = new ArrayList();
		this.oldFood = new HashSet();
		this.newFood = new HashSet();
		this.pointNow = new aStarNode();
		path=new LinkedList();
	
		//for debug
		//plotMaze=new plotGraph(array,rowNumber,colNumber);
		countPlot=0;
		this.newFood=(Set)((HashSet)foodLocations).clone();
		//for debug
		pointNow.setRow(startLocation.getRow());
		pointNow.setCol(startLocation.getCol());
		for(Iterator<point> it = foodLocations.iterator();it.hasNext();)
		{
			point foodTemp = it.next();
			this.foodLocations.add(foodTemp);
		}
		this.visitedArray=new int[rowNumber][colNumber];
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				visitedArray[i][j]=0;
			}
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
	
	public void subOptimalAlgorithm()
	{
		int row, col;
		root.setRow(this.startLocation.getRow());
		root.setCol(this.startLocation.getCol());
		root.setTravelDistance(0);
		root.setTargetDistance(0);
		root.setTotalDistance();
		root.setParent(null);
		path.add(root);
		while(!newFood.isEmpty()) 
		{
			int minCost=21400000;
			int repeat=0;
			countPlot++;
			Queue<aStarNode> pathTemp = new LinkedList();
			Stack<aStarNode> nodeStack = new Stack();
			aStarNode pathNow=new aStarNode();
			pathNow.setRow(pointNow.getRow());
			pathNow.setCol(pointNow.getCol());
			if(countPlot%500==0||isEnd())
			{
				
				//wait 100ms
				/*
				try
				{
					Thread.sleep(100);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				*/
				//wait 100ms
				//plotMaze.deletePanel();
				
				//plotMaze=new plotGraph(array,rowNumber,colNumber);
				//printPath();
			}
			System.out.print("New food size:");
			System.out.println(newFood.size());
			System.out.print("Old food size:");
			System.out.println(oldFood.size());
			System.out.print("Path size:");
			System.out.println(path.size());
			System.out.print("Count:");
			System.out.println(countPlot);
			if(isEnd()) 
			{
				break;
			}
			
			upCost=getHeuristic(new point(pointNow.getRow()-1,pointNow.getCol()));
			downCost=getHeuristic(new point(pointNow.getRow()+1,pointNow.getCol()));
			leftCost=getHeuristic(new point(pointNow.getRow(),pointNow.getCol()-1));
			rightCost=getHeuristic(new point(pointNow.getRow(),pointNow.getCol()+1));
			System.out.print("Current location:");
			System.out.print(pointNow.getRow());
			System.out.print(",");
			System.out.println(pointNow.getCol());
			
			System.out.print(upCost);
			System.out.print(" ");
			System.out.print(downCost);
			System.out.print(" ");
			System.out.print(leftCost);
			System.out.print(" ");
			System.out.println(rightCost);
			
			
			if(upCost<minCost)
			{
				minCost=upCost;
				direction=1;
			}
			if(rightCost<minCost)
			{
				minCost=rightCost;
				direction=4;
			}
			if(downCost<minCost)
			{
				minCost=downCost;
				direction=2;
			}
			if(leftCost<minCost)
			{
				minCost=leftCost;
				direction=3;
			}
			
			if((array[pointNow.getRow()-1][pointNow.getCol()]!=2)&&direction==1)
			{
				pointNow.setRow(pointNow.getRow()-1);
				pointNow.setCol(pointNow.getCol());
				pointNow.setParent(pathNow);
				for(Iterator<point> it=newFood.iterator();it.hasNext();)
				{
					point nodeTemp=it.next();
					if((nodeTemp.getRow()==pointNow.getRow())&&(nodeTemp.getCol()==pointNow.getCol()))
					{
						it.remove();
						oldFood.add(nodeTemp);
					}
				}
				visitedArray[pointNow.getRow()][pointNow.getCol()]++;
				path.add(pathNow);
			}
			else if((array[pointNow.getRow()+1][pointNow.getCol()]!=2)&&direction==2)
			{
				pointNow.setRow(pointNow.getRow()+1);
				pointNow.setCol(pointNow.getCol());
				pointNow.setParent(pathNow);
				for(Iterator<point> it=newFood.iterator();it.hasNext();)
				{
					point nodeTemp=it.next();
					if((nodeTemp.getRow()==pointNow.getRow())&&(nodeTemp.getCol()==pointNow.getCol()))
					{
						it.remove();
						oldFood.add(nodeTemp);
					}
				}
				visitedArray[pointNow.getRow()][pointNow.getCol()]++;								
				path.add(pathNow);
			}
			else if((array[pointNow.getRow()][pointNow.getCol()-1]!=2)&&direction==3)
			{
				pointNow.setRow(pointNow.getRow());
				pointNow.setCol(pointNow.getCol()-1);
				pointNow.setParent(pathNow);
				for(Iterator<point> it=newFood.iterator();it.hasNext();)
				{
					point nodeTemp=it.next();
					if((nodeTemp.getRow()==pointNow.getRow())&&(nodeTemp.getCol()==pointNow.getCol()))
					{
						it.remove();
						oldFood.add(nodeTemp);
					}
				}
				visitedArray[pointNow.getRow()][pointNow.getCol()]++;				
				path.add(pathNow);
			}
			else if((array[pointNow.getRow()][pointNow.getCol()+1]!=2)&&direction==4)
			{
				pointNow.setRow(pointNow.getRow());
				pointNow.setCol(pointNow.getCol()+1);
				pointNow.setParent(pathNow);
				for(Iterator<point> it=newFood.iterator();it.hasNext();)
				{
					point nodeTemp=it.next();
					if((nodeTemp.getRow()==pointNow.getRow())&&(nodeTemp.getCol()==pointNow.getCol()))
					{
						it.remove();
						oldFood.add(nodeTemp);
					}
				}
				visitedArray[pointNow.getRow()][pointNow.getCol()]++;
				path.add(pathNow);				
			}

		}
		path.add(pointNow);
	}
	
	public boolean isInOldFood(aStarNode nodeTemp)
	{
		boolean existed = false;
		for(Iterator<point> it = oldFood.iterator();it.hasNext();)
		{
			point nodeInOld = it.next();
			if(nodeTemp.getRow() == nodeInOld.getRow() && nodeTemp.getCol() == nodeInOld.getCol())
				existed = true;
		}
		return existed;
	}
	public boolean isInOldFood(point foodTemp)
	{
		boolean existed = false;
		for(Iterator<point> it = oldFood.iterator();it.hasNext();)
		{
			point nodeInOld = it.next();
			if(foodTemp.getRow() == nodeInOld.getRow() && foodTemp.getCol() == nodeInOld.getCol())
				existed = true;
		}
		return existed;
	}

	public boolean isEnd()
	{
		boolean end = true;
		boolean inPath;
		for(Iterator<point> foodIt = this.foodLocations.iterator();foodIt.hasNext();)
		{
			point foodTemp = foodIt.next();
			inPath = false;
			for(Iterator<aStarNode> nodeIt = path.iterator();nodeIt.hasNext();) 
			{
				aStarNode nodeTemp = nodeIt.next();
				inPath = inPath || ( (nodeTemp.getRow() == foodTemp.getRow()) && (nodeTemp.getCol() == foodTemp.getCol()) );
			}
			end = end && inPath;
//			if(!end)
//				 System.out.println("End is false");
		}
		
		return end;
	}
	
	public int getHeuristic(point newNode)
	{
		int nodeCost=1000;
		int nearestDistance=1000000;
		int heuristic=0;
		int visitTimes=0;
		for(Iterator<point> it=newFood.iterator();it.hasNext();)
		{
			point temp=it.next();
			if(array[newNode.getRow()][newNode.getCol()]==2)
			{
				nodeCost=10000000;
				break;
			}
			if((newNode.getRow()==temp.getRow())&&(newNode.getCol()==temp.getCol()))
			{
				nodeCost=0;
				nearestDistance=0;
			}
			else
			{
				if((Math.abs(newNode.getRow()-temp.getRow())+Math.abs(newNode.getCol()-temp.getCol()))<nearestDistance)
				{
					nearestDistance=Math.abs(newNode.getRow()-temp.getRow())+Math.abs(newNode.getCol()-temp.getCol());
				}
			}
		}
		visitTimes=visitedArray[newNode.getRow()][newNode.getCol()];
		heuristic=nodeCost+nearestDistance+visitTimes;
		return heuristic;
	}
//	public void printPath()
//	{
//		aStarNode point=new aStarNode();
//		point=goal;
//		while((point=point.getParent())!=null)
//		{
//			System.out.print("------------" + Integer.toString(countPlot) + "-------------\n");
//			System.out.print("Row:");
//			System.out.print(point.getRow());
//			System.out.print(" Col:");
//			System.out.println(point.getCol());
//			array[point.getRow()][point.getCol()]=99;
//			path.add(point);
//		}
//		countPlot++;
//		plotMaze=new plotGraph(array,rowNumber,colNumber);
//	}
	public int[][] getArray()
	{
		return array;
	}
	
	public int indexInArray(aStarNode p)
	{
		for(int i = 0; i < this.foodLocations.size(); i++) 
		{
			if(foodLocationsArray[i].getRow() == p.getRow() && foodLocationsArray[i].getCol() == p.getCol())
				return i;
		}
		return -1;
	}
	public void printPath()
	{
		int count=0;
		for(Iterator<point> foodIt = foodLocations.iterator(); foodIt.hasNext();) 
		{
			point foodTemp = foodIt.next();
			array[foodTemp.getRow()][foodTemp.getCol()] = 4;
			
		}
		for(Iterator<aStarNode> pathIt = path.iterator(); pathIt.hasNext();) 
		{
			count++;
			aStarNode pathNode = pathIt.next();
			//System.out.print("Row:" + Integer.toString(pathNode.getRow()) + " Col:" + Integer.toString(pathNode.getCol()) + "\n");
			//System.out.print("Food -> Row:" + Integer.toString(pathNode.getRow()) + " Col:" + Integer.toString(pathNode.getCol()) + "\n");
			if(isInOldFood(pathNode))
			{
				System.out.print("inside path:(");
				System.out.print(pathNode.getRow());
				System.out.print(",");
				System.out.print(pathNode.getCol());
				System.out.print(")");
			}
			if(!isInOldFood(pathNode) || ((pathNode.getRow() == startLocation.getRow()) && (pathNode.getCol() == startLocation.getCol())) )
			{
				array[pathNode.getRow()][pathNode.getCol()] = 99;
			}			
			if(isInOldFood(pathNode) && (array[pathNode.getRow()][pathNode.getCol()] != 5)) 
			{
				try
				{
					Thread.sleep(30);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				array[pathNode.getRow()][pathNode.getCol()] = 5;
				plotMaze = new plotGraph(array,rowNumber,colNumber);
			}
		}
	}
}