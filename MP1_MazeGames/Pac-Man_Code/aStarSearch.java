import java.util.Set;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
public class aStarSearch
{
	private int[][] array;
	private int rowNumber;
	private int colNumber;
	private int upChoice;
	private int downChoice;
	private int leftChoice;
	private int rightChoice;
	private int upExisted;
	private int downExisted;
	private int leftExisted;
	private int rightExisted;
	private point currentLocation;
	private point startLocation;
	private aStarNode goal;
	private Set<point> foodLocations;
	private point foodLocation;
	private Queue<aStarNode> traversal;
	private List<aStarNode> openList;
	private List<aStarNode> closeList;
	private Queue<aStarNode> path;
	private aStarNode root;
	//for debug
	private plotGraph plotMaze;
	private int countPlot;
	//for debug
	
	public aStarSearch(int[][] array,int rowNumber,int colNumber,point currentLocation,Set<point> foodLocations)
	{
		this.array=array;
		this.rowNumber=rowNumber;
		this.colNumber=colNumber;
		this.currentLocation=currentLocation;
		this.startLocation=currentLocation;
		this.foodLocations=foodLocations;
		this.foodLocation=new point(-1,-1);
		this.traversal=new LinkedList();
		this.root=new aStarNode();
		
		openList=new ArrayList<aStarNode>();
		closeList=new ArrayList<aStarNode>();
		path=new LinkedList();
	
		//for debug
		//plotMaze=new plotGraph(array,rowNumber,colNumber);
		countPlot=0;
		//for debug
		
		for(Iterator<point> it=foodLocations.iterator();it.hasNext();)
		{
			point foodTemp=it.next();
			foodLocation=foodTemp;
		}
	}
	
	public void aStarSearchAlgorithm()
	{
		root.setRow(currentLocation.getRow());
		root.setCol(currentLocation.getCol());
		root.setTravelDistance(0);
		root.setTargetDistance(0);
		root.setTotalDistance();
		root.setParent(null);
		
		openList.add(root);
		
		while(!openList.isEmpty())
		{
			int flag=0;
			int existUp=0;
			int existDown=0;
			int existLeft=0;
			int existRight=0;
			int minCost=1000000;
			upChoice=0;
			downChoice=0;
			leftChoice=0;
			rightChoice=0;
			upExisted=0;
			downExisted=0;
			leftExisted=0;
			rightExisted=0;
			
			aStarNode nodeNow=new aStarNode();
			//nodeNow.setTotalDistance(2147483600);
			nodeNow=openList.remove(0);
			closeList.add(nodeNow);
			countPlot++;
			//System.out.println(nodeNow.getTotalDistance());
			if(countPlot%500==0||((nodeNow.getRow()==foodLocation.getRow())&&(nodeNow.getCol()==foodLocation.getCol())))
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
				//plotMaze=new plotGraph(array,rowNumber,colNumber);
			}
			/*
			for(Iterator<aStarNode> it=openList.iterator();it.hasNext();)
			{
				aStarNode nodeTemp=it.next();
				if(nodeTemp.getTotalDistance()<nodeNow.getTotalDistance())
				{
					nodeNow=nodeTemp;
				}
			}
			*/
			if((nodeNow.getRow()==foodLocation.getRow())&&(nodeNow.getCol()==foodLocation.getCol()))
			{
				goal=nodeNow;
//				int count = 0;
//				if((goal!=null))
//				while(((goal = goal.getParent())!=null))
//					count++;
//				System.out.println(count);
				break;
			}
			
			//openList.remove(nodeNow);
			//closeList.add(nodeNow);
			
			
			aStarNode upPoint=new aStarNode();
			upPoint.setRow(nodeNow.getRow()-1);
			upPoint.setCol(nodeNow.getCol());
			upPoint.setParent(nodeNow);
			upPoint.setTravelDistance(getHeuristicG(upPoint));
			upPoint.setTargetDistance(getHeuristicH(upPoint.getRow(),upPoint.getCol()));
			upPoint.setTotalDistance();

			aStarNode downPoint=new aStarNode();
			downPoint.setRow(nodeNow.getRow()+1);
			downPoint.setCol(nodeNow.getCol());				
			downPoint.setParent(nodeNow);
			downPoint.setTravelDistance(getHeuristicG(downPoint));
			downPoint.setTargetDistance(getHeuristicH(downPoint.getRow(),downPoint.getCol()));
			downPoint.setTotalDistance();

			aStarNode leftPoint=new aStarNode();
			leftPoint.setRow(nodeNow.getRow());
			leftPoint.setCol(nodeNow.getCol()-1);				
			leftPoint.setParent(nodeNow);
			leftPoint.setTravelDistance(getHeuristicG(leftPoint));
			leftPoint.setTargetDistance(getHeuristicH(leftPoint.getRow(),leftPoint.getCol()));
			leftPoint.setTotalDistance();

			aStarNode rightPoint=new aStarNode();
			rightPoint.setRow(nodeNow.getRow());
			rightPoint.setCol(nodeNow.getCol()+1);				
			rightPoint.setParent(nodeNow);
			rightPoint.setTravelDistance(getHeuristicG(rightPoint));
			rightPoint.setTargetDistance(getHeuristicH(rightPoint.getRow(),rightPoint.getCol()));
			rightPoint.setTotalDistance();

			for(Iterator<aStarNode> it=closeList.iterator();it.hasNext();)
			{
				aStarNode nodeTemp=it.next();
				if((nodeTemp.getRow()==upPoint.getRow())&&(nodeTemp.getCol()==upPoint.getCol()))
				{
					upExisted=1;
				}
				if((nodeTemp.getRow()==downPoint.getRow())&&(nodeTemp.getCol()==downPoint.getCol()))
				{
					downExisted=1;
				}
				if((nodeTemp.getRow()==leftPoint.getRow())&&(nodeTemp.getCol()==leftPoint.getCol()))
				{
					leftExisted=1;
				}
				if((nodeTemp.getRow()==rightPoint.getRow())&&(nodeTemp.getCol()==rightPoint.getCol()))
				{
					rightExisted=1;
				}
			}
			/*
			if(upPoint.getTotalDistance()<=minCost&&((array[nodeNow.getRow()-1][nodeNow.getCol()]!=2)&&(array[nodeNow.getRow()-1][nodeNow.getCol()]!=99)&&(array[nodeNow.getRow()-1][nodeNow.getCol()]!=100))&&upExisted==0)
			{
				minCost=upPoint.getTotalDistance();
			}
			if(downPoint.getTotalDistance()<=minCost&&((array[nodeNow.getRow()+1][nodeNow.getCol()]!=2)&&(array[nodeNow.getRow()+1][nodeNow.getCol()]!=99)&&(array[nodeNow.getRow()+1][nodeNow.getCol()]!=100))&&downExisted==0)
			{
				minCost=downPoint.getTotalDistance();
			}
			if(leftPoint.getTotalDistance()<=minCost&&((array[nodeNow.getRow()][nodeNow.getCol()-1]!=2)&&(array[nodeNow.getRow()][nodeNow.getCol()-1]!=99)&&(array[nodeNow.getRow()][nodeNow.getCol()-1]!=100))&&leftExisted==0)
			{
				minCost=leftPoint.getTotalDistance();
			}
			if(rightPoint.getTotalDistance()<=minCost&&((array[nodeNow.getRow()][nodeNow.getCol()+1]!=2)&&(array[nodeNow.getRow()][nodeNow.getCol()+1]!=99)&&(array[nodeNow.getRow()][nodeNow.getCol()+1]!=100))&&rightExisted==0)
			{
				minCost=rightPoint.getTotalDistance();
			}
			
			if(upPoint.getTotalDistance()==minCost&&((array[nodeNow.getRow()-1][nodeNow.getCol()]!=2)&&(array[nodeNow.getRow()-1][nodeNow.getCol()]!=99)&&(array[nodeNow.getRow()-1][nodeNow.getCol()]!=100))&&upExisted==0)
			{
				upChoice=1;
			}
			if(downPoint.getTotalDistance()==minCost&&((array[nodeNow.getRow()+1][nodeNow.getCol()]!=2)&&(array[nodeNow.getRow()+1][nodeNow.getCol()]!=99)&&(array[nodeNow.getRow()+1][nodeNow.getCol()]!=100))&&downExisted==0)
			{
				downChoice=1;
			}
			if(leftPoint.getTotalDistance()==minCost&&((array[nodeNow.getRow()][nodeNow.getCol()-1]!=2)&&(array[nodeNow.getRow()][nodeNow.getCol()-1]!=99)&&(array[nodeNow.getRow()][nodeNow.getCol()-1]!=100))&&leftExisted==0)
			{
				leftChoice=1;
			}
			if(rightPoint.getTotalDistance()==minCost&&((array[nodeNow.getRow()][nodeNow.getCol()+1]!=2)&&(array[nodeNow.getRow()][nodeNow.getCol()+1]!=99)&&(array[nodeNow.getRow()][nodeNow.getCol()+1]!=100))&&rightExisted==0)
			{
				rightChoice=1;
			}
			
			System.out.print("Start:");
			System.out.print(nodeNow.getRow());
			System.out.print(",");
			System.out.println(nodeNow.getCol());
			
			System.out.println(upPoint.getTotalDistance());
			System.out.println(downPoint.getTotalDistance());
			System.out.println(leftPoint.getTotalDistance());
			System.out.println(rightPoint.getTotalDistance());
			System.out.println();
			*/
			upChoice=1;
			upExisted=0;
			downChoice=1;
			downExisted=0;
			leftChoice=1;
			leftExisted=0;
			rightChoice=1;
			rightExisted=0;
			if((array[nodeNow.getRow()-1][nodeNow.getCol()]!=2)&&(array[nodeNow.getRow()-1][nodeNow.getCol()]!=99)&&(array[nodeNow.getRow()-1][nodeNow.getCol()]!=100)&&upChoice==1&&upExisted==0)
			{
				aStarNode point=new aStarNode();
				array[nodeNow.getRow()][nodeNow.getCol()]=100;
				point.setRow(nodeNow.getRow()-1);
				point.setCol(nodeNow.getCol());
				
				aStarNode nodeTemp;
				aStarNode nodeFind=null;
				int nodeId=0;
				
				for(Iterator<aStarNode> it=openList.iterator();it.hasNext();)
				{
					nodeTemp=it.next();
					if((nodeTemp.getRow()==point.getRow())&&(nodeTemp.getCol()==point.getCol()))
					{
						nodeFind=nodeTemp;
						nodeId=openList.indexOf(nodeTemp);
						existUp=1;
					}
				}
				if(existUp==0)
				{
					point.setParent(nodeNow);
					point.setTravelDistance(getHeuristicG(point));
					point.setTargetDistance(getHeuristicH(point.getRow(),point.getCol()));
					point.setTotalDistance();
					openList.add(point);
					/*System.out.print("Up:");
					System.out.print(point.getRow());
					System.out.print(",");
					System.out.println(point.getCol());	*/				
				}
				else if(existUp==1)
				{
					if(point.getTotalDistance()<nodeFind.getTotalDistance())
					{
						openList.get(nodeId).setTravelDistance(getHeuristicG(point));
						openList.get(nodeId).setTargetDistance(getHeuristicH(point.getRow(),point.getCol()));
						openList.get(nodeId).setTotalDistance();
						openList.get(nodeId).setParent(nodeNow);
					}
				}
				flag=1;
			}
			if((array[nodeNow.getRow()][nodeNow.getCol()+1]!=2)&&(array[nodeNow.getRow()][nodeNow.getCol()+1]!=99)&&(array[nodeNow.getRow()][nodeNow.getCol()+1]!=100)&&rightChoice==1&&rightExisted==0)
			{
				aStarNode point=new aStarNode();
				array[nodeNow.getRow()][nodeNow.getCol()]=100;
				point.setRow(nodeNow.getRow());
				point.setCol(nodeNow.getCol()+1);				
				
				aStarNode nodeTemp;
				aStarNode nodeFind=null;
				int nodeId=0;
				
				for(Iterator<aStarNode> it=openList.iterator();it.hasNext();)
				{
					nodeTemp=it.next();
					if((nodeTemp.getRow()==point.getRow())&&(nodeTemp.getCol()==point.getCol()))
					{
						nodeFind=nodeTemp;
						nodeId=openList.indexOf(nodeTemp);
						existRight=1;
					}
				}
				
				if(existRight==0)
				{
					point.setParent(nodeNow);
					point.setTravelDistance(getHeuristicG(point));
					point.setTargetDistance(getHeuristicH(point.getRow(),point.getCol()));
					point.setTotalDistance();
					openList.add(point);
					/*System.out.print("Right:");
					System.out.print(point.getRow());
					System.out.print(",");
					System.out.println(point.getCol());	*/				
				}
				else if(existRight==1)
				{
					if(point.getTotalDistance()<nodeFind.getTotalDistance())
					{
						openList.get(nodeId).setTravelDistance(getHeuristicG(point));
						openList.get(nodeId).setTargetDistance(getHeuristicH(point.getRow(),point.getCol()));
						openList.get(nodeId).setTotalDistance();
						openList.get(nodeId).setParent(nodeNow);
					}
				}
				flag=1;
			}

			
			if((array[nodeNow.getRow()+1][nodeNow.getCol()]!=2)&&(array[nodeNow.getRow()+1][nodeNow.getCol()]!=99)&&(array[nodeNow.getRow()+1][nodeNow.getCol()]!=100)&&downChoice==1&&downExisted==0)
			{
				aStarNode point=new aStarNode();
				array[nodeNow.getRow()][nodeNow.getCol()]=100;
				point.setRow(nodeNow.getRow()+1);
				point.setCol(nodeNow.getCol());
				
				aStarNode nodeTemp;
				aStarNode nodeFind=null;
				int nodeId=0;
				
				for(Iterator<aStarNode> it=openList.iterator();it.hasNext();)
				{
					nodeTemp=it.next();
					if((nodeTemp.getRow()==point.getRow())&&(nodeTemp.getCol()==point.getCol()))
					{
						nodeFind=nodeTemp;
						nodeId=openList.indexOf(nodeTemp);
						existDown=1;
					}
				}
				if(existDown==0)
				{
					point.setParent(nodeNow);
					point.setTravelDistance(getHeuristicG(point));
					point.setTargetDistance(getHeuristicH(point.getRow(),point.getCol()));
					point.setTotalDistance();
					openList.add(point);
					/*System.out.print("Down:");
					System.out.print(point.getRow());
					System.out.print(",");
					System.out.println(point.getCol());*/					
				}
				else if(existDown==1)
				{
					if(point.getTotalDistance()<nodeFind.getTotalDistance())
					{
						openList.get(nodeId).setTravelDistance(getHeuristicG(point));
						openList.get(nodeId).setTargetDistance(getHeuristicH(point.getRow(),point.getCol()));
						openList.get(nodeId).setTotalDistance();
						openList.get(nodeId).setParent(nodeNow);
					}
				}

				flag=1;
			}
			if((array[nodeNow.getRow()][nodeNow.getCol()-1]!=2)&&(array[nodeNow.getRow()][nodeNow.getCol()-1]!=99)&&(array[nodeNow.getRow()][nodeNow.getCol()-1]!=100)&&leftChoice==1&&leftExisted==0)
			{
				aStarNode point=new aStarNode();
				array[nodeNow.getRow()][nodeNow.getCol()]=100;
				point.setRow(nodeNow.getRow());
				point.setCol(nodeNow.getCol()-1);
				
				aStarNode nodeTemp;
				aStarNode nodeFind=null;
				int nodeId=0;
				
				for(Iterator<aStarNode> it=openList.iterator();it.hasNext();)
				{
					nodeTemp=it.next();
					if((nodeTemp.getRow()==point.getRow())&&(nodeTemp.getCol()==point.getCol()))
					{
						nodeFind=nodeTemp;
						nodeId=openList.indexOf(nodeTemp);
						existLeft=1;
					}
				}
				
				if(existLeft==0)
				{
					point.setParent(nodeNow);
					point.setTravelDistance(getHeuristicG(point));
					point.setTargetDistance(getHeuristicH(point.getRow(),point.getCol()));
					point.setTotalDistance();
					openList.add(point);
					/*System.out.print("Left:");
					System.out.print(point.getRow());
					System.out.print(",");
					System.out.println(point.getCol());*/					
				}
				else if(existLeft==1)
				{
					if(point.getTotalDistance()<nodeFind.getTotalDistance())
					{
						openList.get(nodeId).setTravelDistance(getHeuristicG(point));
						openList.get(nodeId).setTargetDistance(getHeuristicH(point.getRow(),point.getCol()));
						openList.get(nodeId).setTotalDistance();
						openList.get(nodeId).setParent(nodeNow);
					}
				}
				flag=1;
			}
			if(flag==0)
			{
				array[nodeNow.getRow()][nodeNow.getCol()]=100;
			}
			//System.out.println();
			sortList();
		}
		printPath();
	}
	public int getHeuristicG(aStarNode point)
	{
		int count=0;
		while((point=point.getParent())!=null)
		{
			count++;
		}
		return count;
	}
	public int getHeuristicH(int row,int col)
	{
		return Math.abs(foodLocation.getRow()-row)+Math.abs(foodLocation.getCol()-col);
	}
	public void printPath()
	{
		aStarNode point=new aStarNode();
		point=goal;
		while((point=point.getParent())!=null)
		{
			/*System.out.print("Row:");
			System.out.print(point.getRow());
			System.out.print(" Col:");
			System.out.println(point.getCol());*/
			array[point.getRow()][point.getCol()]=99;
			path.add(point);
		}
		//plotMaze=new plotGraph(array,rowNumber,colNumber);
	}
	public int[][] getArray()
	{
		return array;
	}
	public void sortList()
	{
		openList.sort(new Comparator<aStarNode>() 
		{
			@Override
			public int compare(aStarNode n1, aStarNode n2) 
			{
				int d1 = n1.getTotalDistance();
				int d2 = n2.getTotalDistance();
				if(d1 == d2)
					return 0;
				return (d1 > d2) ? 1:-1;
			}
		});
	}
    public int getCost()
    {
        return getHeuristicG(goal);
    }
    public Queue<aStarNode> getPath()
    {
        return this.path;
    }
}
