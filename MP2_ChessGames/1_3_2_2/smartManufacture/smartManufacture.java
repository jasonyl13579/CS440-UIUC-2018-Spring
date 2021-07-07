import java.util.Set;
import java.util.HashSet;
import java.util.Stack;
import java.util.Iterator;
import java.util.Queue;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;

public class smartManufacture
{
	private int[][] array;
	private int[][] arrayUniform;
	private int rowNumber;
	private int colNumber;
	private int rowCount;
	private int colCount;
	private int rowSize;
	private int colSize;
	private Queue<smartManufactureState> stateQueue;
	private Queue<smartManufactureState> stateQueueUniform;
	private plotGraph plotFactory;
	private int SLEEP_TIME = 200;
	private int round = 0;
	private int roundUniform = 0;
	private int heuristicChoice=1;
	private int value;
	private int last=0;
	private Queue<Integer> path;
	private smartManufactureState nextState[];
	private smartManufactureState nextStateUniform[];
	private ArrayList<ArrayList<Integer>> gadgets;
	private ArrayList<ArrayList<Integer>> gadgetsUniform;
	private List<smartManufactureState> closeList;
	private List<smartManufactureState> closeListUniform;
	private int[][] costMatrix;
	
	public smartManufacture(int[][] array,int rowNumber,int colNumber)
	{
		this.array=array;
		this.rowNumber=rowNumber;
		this.colNumber=colNumber;
		this.rowCount=rowNumber;
		this.colCount=colNumber;
		this.rowSize=rowNumber;
		this.colSize=colNumber;
		this.gadgets=new ArrayList();
		this.path=new LinkedList();
		closeList=new ArrayList<smartManufactureState>();
		closeListUniform=new ArrayList<smartManufactureState>();
		this.stateQueue=new PriorityQueue<>(1, valueComparator);
		nextState = new smartManufactureState[5];
		
		this.costMatrix=new int[rowNumber][colNumber];
		this.costMatrix[0][0]=0;
		this.costMatrix[0][1]=1064;
		this.costMatrix[0][2]=673;
		this.costMatrix[0][3]=1401;
		this.costMatrix[0][4]=277;
		
		this.costMatrix[1][0]=1064;
		this.costMatrix[1][1]=0;
		this.costMatrix[1][2]=958;
		this.costMatrix[1][3]=1934;
		this.costMatrix[1][4]=337;
		
		this.costMatrix[2][0]=673;
		this.costMatrix[2][1]=958;
		this.costMatrix[2][2]=0;
		this.costMatrix[2][3]=1001;
		this.costMatrix[2][4]=399;
		
		this.costMatrix[3][0]=1401;
		this.costMatrix[3][1]=1934;
		this.costMatrix[3][2]=1001;
		this.costMatrix[3][3]=0;
		this.costMatrix[3][4]=387;
		
		this.costMatrix[4][0]=277;
		this.costMatrix[4][1]=337;
		this.costMatrix[4][2]=399;
		this.costMatrix[4][3]=387;
		this.costMatrix[4][4]=0;
		
		this.stateQueueUniform=new PriorityQueue<>(1, valueComparator);
		arrayUniform=new int[rowNumber][colNumber];
		nextStateUniform = new smartManufactureState[5];
		this.gadgetsUniform=new ArrayList();
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				arrayUniform[i][j]=array[i][j];
			}
		}
		//costMatrix={{0,1064,673,1401,277},{1064,0,958,1934,337},{673,958,0,1001,399},{1401,1934,1001,0,387},{277,337,399,387,0}};
		//smartManufactureState state=new smartManufactureState(array,rowNumber,colNumber,rowCount,colCount,gadgets);
		//printAnyArrayList(state.getGadgets());
		//System.out.println(calculateHeuristic());
		/*
		for(int i=0;i<rowCount;i++)
		{
			System.out.println(gadgets.get(i).get(0));
		}
		*/
	}
	public void smartManufactureAlgorithm()
	{
		for(int i = 0; i < this.rowSize;i++) 
		{
			ArrayList<Integer> gadget = new ArrayList();
			for(int j=0;j<this.colSize;j++)
			{
				gadget.add(new Integer(array[i][j]));
			}
			gadgets.add(gadget);
		}
		
		smartManufactureState root = new smartManufactureState(array,rowNumber,colNumber,rowCount,colCount,gadgets);
		
		smartManufactureState stateNow = root;
		
		int nodeExpanded=0;
		int end=0;
		
		while(end!=1)
		{
			int affectA=0;
			int affectB=0;
			int affectC=0;
			int affectD=0;
			int affectE=0;

			int existA=0;
			int existB=0;
			int existC=0;
			int existD=0;
			int existE=0;

			recoverState(stateNow);
			//printArray();
						
			//test
			try
			{
				Thread.sleep(SLEEP_TIME);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			
			plotFactory=new plotGraph(array,rowNumber,colNumber);
			//test
			//end Condition
			if(calculateHeuristic()==0)
			{
				end=1;
				System.out.println("Succeed");
				break;
			}
			//end Condition
			//produce A*************************************
			for(int i = 0; i < this.rowNumber;i++) 
			{
				while(gadgets.get(i).get(0)==1)
				{
					affectA=affectA+1;
					gadgets.get(i).remove(0);
					gadgets.get(i).add(-1);
				}
			}
			listToArray();
			nextState[0] = new smartManufactureState(array,rowNumber,colNumber,rowCount,colCount,gadgets);
			nextState[0].setEvaluation(calculateHeuristic()+calculatePathCost(stateNow));
			recoverState(stateNow);
			//System.out.println(nextState[0].getEvaluation());
			//printArray();
			//produce A*************************************
			
			//produce B*************************************
			for(int i = 0; i < this.rowNumber;i++) 
			{
				while(gadgets.get(i).get(0)==2)
				{
					affectB=affectB+1;
					gadgets.get(i).remove(0);
					gadgets.get(i).add(-1);
				}
			}
			listToArray();
			nextState[1] = new smartManufactureState(array,rowNumber,colNumber,rowCount,colCount,gadgets);
			nextState[1].setEvaluation(calculateHeuristic()+calculatePathCost(stateNow));
			recoverState(stateNow);	
			//produce B*************************************
			
			//produce C*************************************
			for(int i = 0; i < this.rowNumber;i++) 
			{
				while(gadgets.get(i).get(0)==3)
				{
					affectC=affectC+1;
					gadgets.get(i).remove(0);
					gadgets.get(i).add(-1);
				}
			}
			listToArray();
			nextState[2] = new smartManufactureState(array,rowNumber,colNumber,rowCount,colCount,gadgets);
			nextState[2].setEvaluation(calculateHeuristic()+calculatePathCost(stateNow));
			recoverState(stateNow);						
			//produce C*************************************
			
			//produce D*************************************
			for(int i = 0; i < this.rowNumber;i++) 
			{
				while(gadgets.get(i).get(0)==4)
				{
					affectD=affectD+1;
					gadgets.get(i).remove(0);
					gadgets.get(i).add(-1);
				}
			}
			listToArray();
			nextState[3] = new smartManufactureState(array,rowNumber,colNumber,rowCount,colCount,gadgets);
			nextState[3].setEvaluation(calculateHeuristic()+calculatePathCost(stateNow));
			recoverState(stateNow);									
			//produce D*************************************
			
			//produce E*************************************
			for(int i = 0; i < this.rowNumber;i++) 
			{
				while(gadgets.get(i).get(0)==5)
				{
					affectE=affectE+1;
					gadgets.get(i).remove(0);
					gadgets.get(i).add(-1);
				}
			}
			listToArray();
			nextState[4] = new smartManufactureState(array,rowNumber,colNumber,rowCount,colCount,gadgets);
			nextState[4].setEvaluation(calculateHeuristic()+calculatePathCost(stateNow));
			recoverState(stateNow);									
			//produce E*************************************
			//checkCloseList
			if(round!=0)
			{
				for(Iterator<smartManufactureState> it=closeList.iterator();it.hasNext();)
				{
					smartManufactureState nodeTemp=it.next();
					int countA=0;
					int countB=0;
					int countC=0;
					int countD=0;
					int countE=0;
					for(int i=0;i<rowNumber;i++)
					{
						for(int j=0;j<colNumber;j++)
						{
							if(nodeTemp.getArray()[i][j]==nextState[0].getArray()[i][j])
							{
								countA++;
							}
							if(nodeTemp.getArray()[i][j]==nextState[1].getArray()[i][j])
							{
								countB++;
							}
							if(nodeTemp.getArray()[i][j]==nextState[2].getArray()[i][j])
							{
								countC++;
							}
							if(nodeTemp.getArray()[i][j]==nextState[3].getArray()[i][j])
							{
								countD++;
							}
							if(nodeTemp.getArray()[i][j]==nextState[4].getArray()[i][j])
							{
								countE++;
							}
						}
					}
					if(countA==rowNumber*colNumber)
					{
						existA=1;
					}
					if(countB==rowNumber*colNumber)
					{
						existB=1;
					}
					if(countC==rowNumber*colNumber)
					{
						existC=1;
					}
					if(countD==rowNumber*colNumber)
					{
						existD=1;
					}
					if(countE==rowNumber*colNumber)
					{
						existE=1;
					}
				}
			}
			System.out.println(closeList.size());
			System.out.println(existA);
			System.out.println(existB);
			System.out.println(existC);
			System.out.println(existD);
			System.out.println(existE);
			//checkCloseList
			//stateQueue*************************************
			if(affectA!=0&&existA==0||round==0)
			{
				nextState[0].setParent(stateNow);
				nextState[0].setMove(1);
				if(round!=0)
				{
					nextState[0].setPathCount(stateNow.getPathCount()+costMatrix[stateNow.getCurrentPlace()][0]);
					nextState[0].setCost(costMatrix[stateNow.getCurrentPlace()][0]);
				}
				nextState[0].setCurrentPlace(0);
				stateQueue.add(nextState[0]);
				nodeExpanded++;
			}
			if(affectB!=0&&existB==0||round==0)
			{
				nextState[1].setParent(stateNow);
				nextState[1].setMove(2);
				if(round!=0)
				{
					nextState[1].setPathCount(stateNow.getPathCount()+costMatrix[stateNow.getCurrentPlace()][1]);
					nextState[1].setCost(costMatrix[stateNow.getCurrentPlace()][1]);
				}
				nextState[1].setCurrentPlace(1);
				stateQueue.add(nextState[1]);
				nodeExpanded++;
			}
			if(affectC!=0&&existC==0||round==0)
			{
				nextState[2].setParent(stateNow);
				nextState[2].setMove(3);
				if(round!=0)
				{
					nextState[2].setPathCount(stateNow.getPathCount()+costMatrix[stateNow.getCurrentPlace()][2]);
					nextState[2].setCost(costMatrix[stateNow.getCurrentPlace()][2]);
				}
				nextState[2].setCurrentPlace(2);
				stateQueue.add(nextState[2]);
				nodeExpanded++;
			}
			if(affectD!=0&&existD==0||round==0)
			{
				nextState[3].setParent(stateNow);
				nextState[3].setMove(4);
				if(round!=0)
				{
					nextState[3].setPathCount(stateNow.getPathCount()+costMatrix[stateNow.getCurrentPlace()][3]);
					nextState[3].setCost(costMatrix[stateNow.getCurrentPlace()][3]);
				}
				nextState[3].setCurrentPlace(3);
				stateQueue.add(nextState[3]);
				nodeExpanded++;
			}
			if(affectE!=0&&existE==0||round==0)
			{
				nextState[4].setParent(stateNow);
				nextState[4].setMove(5);
				if(round!=0)
				{
					nextState[4].setPathCount(stateNow.getPathCount()+costMatrix[stateNow.getCurrentPlace()][4]);
					nextState[4].setCost(costMatrix[stateNow.getCurrentPlace()][4]);
				}
				nextState[4].setCurrentPlace(4);
				stateQueue.add(nextState[4]);
				nodeExpanded++;
			}
			//stateQueue*************************************
			//StateChoosing
			smartManufactureState newState = stateQueue.poll();
			stateNow = newState;
			closeList.add(newState);
			//stateChoosing
			
			//System.out.println(calculateHeuristic());
			round++;
		}
		//print path
		System.out.println(nodeExpanded);
		int pathTotal=0;
		Stack<Integer> nodeStack = new Stack<Integer>();
		Stack<smartManufactureState> resultStack = new Stack<smartManufactureState>();
		nodeStack.push(stateNow.getMove());
		resultStack.push(stateNow);
		while((stateNow=stateNow.getParent())!=null)
		{
			nodeStack.push(stateNow.getMove());
			resultStack.push(stateNow);
			pathTotal=pathTotal+stateNow.getCost();
		}
		
		int start=0;
		while(nodeStack.size()!=0)
		{
			int temp=nodeStack.pop();
			if(start!=0)
			{
				System.out.print(temp);
			}
			start++;
		}
		//print path
		System.out.println();
		System.out.println(pathTotal);
		printFinalAnimation(resultStack);
		//************************************************************************************************
		//uniform cost
		for(int i = 0; i < this.rowSize;i++) 
		{
			ArrayList<Integer> gadgetUniform = new ArrayList();
			for(int j=0;j<this.colSize;j++)
			{
				gadgetUniform.add(new Integer(arrayUniform[i][j]));
			}
			gadgetsUniform.add(gadgetUniform);
		}
		
		smartManufactureState rootUniform = new smartManufactureState(arrayUniform,rowNumber,colNumber,rowCount,colCount,gadgetsUniform);
		
		smartManufactureState stateNowUniform = rootUniform;
		
		int endUniform=0;
		
		while(endUniform!=1)
		{
			int affectAUniform=0;
			int affectBUniform=0;
			int affectCUniform=0;
			int affectDUniform=0;
			int affectEUniform=0;

			int existAUniform=0;
			int existBUniform=0;
			int existCUniform=0;
			int existDUniform=0;
			int existEUniform=0;

			recoverStateUniform(stateNowUniform);
			
			try
			{
				Thread.sleep(SLEEP_TIME);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			
			plotFactory=new plotGraph(arrayUniform,rowNumber,colNumber);
			
			//end Condition
			int endResult=1;
			for(int i=0;i<rowNumber;i++)
			{
				for(int j=0;j<colNumber;j++)
				{
					//System.out.print(arrayUniform[i][j]);
					//System.out.print(" ");
					if(arrayUniform[i][j]!=-1)
					{
						endResult=0;
					}
				}
				//System.out.println();
			}
			//System.out.println();
			if(endResult==1)
			{
				endUniform=1;
				System.out.println("Succeed");
				break;
			}
			//end Condition

			//produce A*************************************
			for(int i = 0; i < this.rowNumber;i++) 
			{
				while(gadgetsUniform.get(i).get(0)==1)
				{
					affectAUniform=affectAUniform+1;
					gadgetsUniform.get(i).remove(0);
					gadgetsUniform.get(i).add(-1);
				}
			}
			listToArrayUniform();
			nextStateUniform[0] = new smartManufactureState(arrayUniform,rowNumber,colNumber,rowCount,colCount,gadgetsUniform);
			nextStateUniform[0].setEvaluation(calculatePathCost(stateNowUniform));
			recoverStateUniform(stateNowUniform);
			//System.out.println(nextState[0].getEvaluation());
			//printArray();
			//produce A*************************************

			//produce B*************************************
			for(int i = 0; i < this.rowNumber;i++) 
			{
				while(gadgetsUniform.get(i).get(0)==2)
				{
					affectBUniform=affectBUniform+1;
					gadgetsUniform.get(i).remove(0);
					gadgetsUniform.get(i).add(-1);
				}
			}
			listToArrayUniform();
			nextStateUniform[1] = new smartManufactureState(arrayUniform,rowNumber,colNumber,rowCount,colCount,gadgetsUniform);
			nextStateUniform[1].setEvaluation(calculatePathCost(stateNowUniform));
			recoverStateUniform(stateNowUniform);
			//System.out.println(nextState[0].getEvaluation());
			//printArray();
			//produce B*************************************

			//produce C*************************************
			for(int i = 0; i < this.rowNumber;i++) 
			{
				while(gadgetsUniform.get(i).get(0)==3)
				{
					affectCUniform=affectCUniform+1;
					gadgetsUniform.get(i).remove(0);
					gadgetsUniform.get(i).add(-1);
				}
			}
			listToArrayUniform();
			nextStateUniform[2] = new smartManufactureState(arrayUniform,rowNumber,colNumber,rowCount,colCount,gadgetsUniform);
			nextStateUniform[2].setEvaluation(calculatePathCost(stateNowUniform));
			recoverStateUniform(stateNowUniform);
			//System.out.println(nextState[0].getEvaluation());
			//printArray();
			//produce C*************************************

			//produce D*************************************
			for(int i = 0; i < this.rowNumber;i++) 
			{
				while(gadgetsUniform.get(i).get(0)==4)
				{
					affectDUniform=affectDUniform+1;
					gadgetsUniform.get(i).remove(0);
					gadgetsUniform.get(i).add(-1);
				}
			}
			listToArrayUniform();
			nextStateUniform[3] = new smartManufactureState(arrayUniform,rowNumber,colNumber,rowCount,colCount,gadgetsUniform);
			nextStateUniform[3].setEvaluation(calculatePathCost(stateNowUniform));
			recoverStateUniform(stateNowUniform);
			//System.out.println(nextState[0].getEvaluation());
			//printArray();
			//produce D*************************************

			//produce E*************************************
			for(int i = 0; i < this.rowNumber;i++) 
			{
				while(gadgetsUniform.get(i).get(0)==5)
				{
					affectEUniform=affectEUniform+1;
					gadgetsUniform.get(i).remove(0);
					gadgetsUniform.get(i).add(-1);
				}
			}
			listToArrayUniform();
			nextStateUniform[4] = new smartManufactureState(arrayUniform,rowNumber,colNumber,rowCount,colCount,gadgetsUniform);
			nextStateUniform[4].setEvaluation(calculatePathCost(stateNowUniform));
			recoverStateUniform(stateNowUniform);
			//System.out.println(nextState[0].getEvaluation());
			//printArray();
			//produce E*************************************

			if(roundUniform!=0)
			{
				for(Iterator<smartManufactureState> it=closeListUniform.iterator();it.hasNext();)
				{
					smartManufactureState nodeTemp=it.next();
					int countA=0;
					int countB=0;
					int countC=0;
					int countD=0;
					int countE=0;
					for(int i=0;i<rowNumber;i++)
					{
						for(int j=0;j<colNumber;j++)
						{
							if(nodeTemp.getArray()[i][j]==nextStateUniform[0].getArray()[i][j])
							{
								countA++;
							}
							if(nodeTemp.getArray()[i][j]==nextStateUniform[1].getArray()[i][j])
							{
								countB++;
							}
							if(nodeTemp.getArray()[i][j]==nextStateUniform[2].getArray()[i][j])
							{
								countC++;
							}
							if(nodeTemp.getArray()[i][j]==nextStateUniform[3].getArray()[i][j])
							{
								countD++;
							}
							if(nodeTemp.getArray()[i][j]==nextStateUniform[4].getArray()[i][j])
							{
								countE++;
							}
						}
					}
					if(countA==rowNumber*colNumber)
					{
						existAUniform=1;
					}
					if(countB==rowNumber*colNumber)
					{
						existBUniform=1;
					}
					if(countC==rowNumber*colNumber)
					{
						existCUniform=1;
					}
					if(countD==rowNumber*colNumber)
					{
						existDUniform=1;
					}
					if(countE==rowNumber*colNumber)
					{
						existEUniform=1;
					}
				}
			}
			System.out.println(closeListUniform.size());
			System.out.println(existAUniform);
			System.out.println(existBUniform);
			System.out.println(existCUniform);
			System.out.println(existDUniform);
			System.out.println(existEUniform);
			//checkCloseList
			//stateQueue*************************************
			if(affectAUniform!=0&&existAUniform==0||roundUniform==0)
			{
				nextStateUniform[0].setParent(stateNowUniform);
				nextStateUniform[0].setMove(1);
				if(roundUniform!=0)
				{
					nextStateUniform[0].setPathCount(costMatrix[stateNowUniform.getCurrentPlace()][0]);
					nextStateUniform[0].setCost(costMatrix[stateNowUniform.getCurrentPlace()][0]);
				}
				nextStateUniform[0].setCurrentPlace(0);
				stateQueueUniform.add(nextStateUniform[0]);
			}
			if(affectBUniform!=0&&existBUniform==0||roundUniform==0)
			{
				nextStateUniform[1].setParent(stateNowUniform);
				nextStateUniform[1].setMove(2);
				if(roundUniform!=0)
				{
					nextStateUniform[1].setPathCount(costMatrix[stateNowUniform.getCurrentPlace()][1]);
					nextStateUniform[1].setCost(costMatrix[stateNowUniform.getCurrentPlace()][1]);
				}
				nextStateUniform[1].setCurrentPlace(1);
				stateQueueUniform.add(nextStateUniform[1]);
			}
			if(affectCUniform!=0&&existCUniform==0||roundUniform==0)
			{
				nextStateUniform[2].setParent(stateNowUniform);
				nextStateUniform[2].setMove(3);
				if(roundUniform!=0)
				{
					nextStateUniform[2].setPathCount(costMatrix[stateNowUniform.getCurrentPlace()][2]);
					nextStateUniform[2].setCost(costMatrix[stateNowUniform.getCurrentPlace()][2]);
				}
				nextStateUniform[2].setCurrentPlace(2);
				stateQueueUniform.add(nextStateUniform[2]);
			}
			if(affectDUniform!=0&&existDUniform==0||roundUniform==0)
			{
				nextStateUniform[3].setParent(stateNowUniform);
				nextStateUniform[3].setMove(4);
				if(roundUniform!=0)
				{
					nextStateUniform[3].setPathCount(costMatrix[stateNowUniform.getCurrentPlace()][3]);
					nextStateUniform[3].setCost(costMatrix[stateNowUniform.getCurrentPlace()][3]);
				}
				nextStateUniform[3].setCurrentPlace(3);
				stateQueueUniform.add(nextStateUniform[3]);
			}
			if(affectEUniform!=0&&existEUniform==0||roundUniform==0)
			{
				nextStateUniform[4].setParent(stateNowUniform);
				nextStateUniform[4].setMove(5);
				if(roundUniform!=0)
				{
					nextStateUniform[4].setPathCount(costMatrix[stateNowUniform.getCurrentPlace()][4]);
					nextStateUniform[4].setCost(costMatrix[stateNowUniform.getCurrentPlace()][4]);
				}
				nextStateUniform[4].setCurrentPlace(4);
				stateQueueUniform.add(nextStateUniform[4]);
			}
			//stateQueue*************************************
			//StateChoosing
			smartManufactureState newStateUniform = stateQueueUniform.poll();
			stateNowUniform = newStateUniform;
			closeListUniform.add(newStateUniform);
			//stateChoosing
			
			//System.out.println(calculateHeuristic());
			roundUniform++;
		}
		int pathTotalUniform=0;
		Stack<Integer> nodeStackUniform = new Stack<Integer>();
		Stack<smartManufactureState> resultStackUniform = new Stack<smartManufactureState>();
		nodeStackUniform.push(stateNowUniform.getMove());
		resultStackUniform.push(stateNowUniform);
		while((stateNowUniform=stateNowUniform.getParent())!=null)
		{
			nodeStackUniform.push(stateNowUniform.getMove());
			resultStackUniform.push(stateNowUniform);
			pathTotalUniform=pathTotalUniform+stateNowUniform.getCost();
		}
		
		int startUniform=0;
		while(nodeStackUniform.size()!=0)
		{
			int temp=nodeStackUniform.pop();
			if(startUniform!=0)
			{
				System.out.print(temp);
			}
			startUniform++;
		}
		//print path
		System.out.println();
		System.out.println(pathTotalUniform);
		printFinalAnimation(resultStackUniform);
	}
	public int calculateHeuristic()
	{
		int result=0;
		int existA=0;
		int existB=0;
		int existC=0;
		int existD=0;
		int existE=0;
		int sum=0;
		for(int i=0;i<colNumber;i++)
		{
			existA=0;
			existB=0;
			existC=0;
			existD=0;
			existE=0;
			for(int j=0;j<rowNumber;j++)
			{				
				if(gadgets.get(j).get(i)==1)
				{
					existA=277;
				}
				if(gadgets.get(j).get(i)==2)
				{
					existB=337;
				}
				if(gadgets.get(j).get(i)==3)
				{
					existC=399;
				}
				if(gadgets.get(j).get(i)==4)
				{
					existD=387;
				}
				if(gadgets.get(j).get(i)==5)
				{
					existE=277;
				}
			}
			result=result+existA+existB+existC+existD+existE;
		}
		return result*2;
	}
	public int calculatePathCost(smartManufactureState state)
	{
		return state.getPathCount();
	}
	public void printAnyArray(int[][] anyArray)
	{
		int i=0;
		int j=0;
		for(i=0;i<rowNumber;i++)
		{
			for(j=0;j<colNumber;j++)
			{
				System.out.print(anyArray[i][j]);
			}
			System.out.println();
		}
	}
	public void printAnyArrayList(ArrayList<ArrayList<Integer>> anyArrayList)
	{
		int i=0;
		int j=0;
		for(i=0;i<rowCount;i++)
		{
			for(j=0;j<colCount;j++)
			{
				System.out.print(anyArrayList.get(i).get(j));
			}
			System.out.println();
		}
	}
	public void printArray()
	{
		int i=0;
		int j=0;
		for(i=0;i<rowNumber;i++)
		{
			for(j=0;j<colNumber;j++)
			{
				System.out.print(array[i][j]);
			}
			System.out.println();
		}
	}
	public void printArrayList()
	{
		int i=0;
		int j=0;
		for(i=0;i<rowCount;i++)
		{
			for(j=0;j<colCount;j++)
			{
				System.out.print(gadgets.get(i).get(j));
			}
			System.out.println();
		}
	}
	public void recoverState(smartManufactureState state)
	{
		this.array = new int[rowNumber][colNumber];
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				this.array[i][j]=state.getArray()[i][j];
				//System.out.print(","+this.array[i][j]);
			}
		}
		this.rowNumber=state.getRowNumber();
		this.colNumber=state.getColNumber();
		this.rowCount=state.getRowCount();
		this.colCount=state.getColCount();
		this.value = state.getEvaluation();
		
		this.gadgets=null;
		this.gadgets=state.getGadgets();
		//	write pointInt
	}
	public void recoverStateUniform(smartManufactureState state)
	{
		this.arrayUniform = new int[rowNumber][colNumber];
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				this.arrayUniform[i][j]=state.getArray()[i][j];
				//System.out.print(","+this.array[i][j]);
			}
		}
		this.rowNumber=state.getRowNumber();
		this.colNumber=state.getColNumber();
		this.rowCount=state.getRowCount();
		this.colCount=state.getColCount();
		this.value = state.getEvaluation();
		
		this.gadgetsUniform=null;
		this.gadgetsUniform=state.getGadgets();
		//	write pointInt
	}
	public void listToArray()
	{
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				array[i][j]=gadgets.get(i).get(j);
			}
		}
	}
	public void listToArrayUniform()
	{
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				arrayUniform[i][j]=gadgetsUniform.get(i).get(j);
			}
		}
	}
	public void printFinalAnimation(Stack<smartManufactureState> resultStack)
	{	
		smartManufactureState currentState;
		while((currentState=resultStack.pop())!=null)
		{
			try
			{
				Thread.sleep(750);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			plotFactory=new plotGraph(currentState.getArray(),currentState.getRowNumber(),currentState.getColNumber());
			if(resultStack.size()==0)
			{
				break;
			}
		}
	}
	public void printFormatCostArray()
	{
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				System.out.print(costMatrix[i][j]);
				System.out.print("\t");
			}
			System.out.println();
		}
	}
	public static Comparator<smartManufactureState> valueComparator = new Comparator<smartManufactureState>()
	{
        @Override
        public int compare(smartManufactureState c1, smartManufactureState c2) 
		{
			return (int)(c1.getEvaluation()-c2.getEvaluation());
        }
    };
	public static Comparator<smartManufactureState> valueComparatorUniform = new Comparator<smartManufactureState>()
	{
        @Override
        public int compare(smartManufactureState c1, smartManufactureState c2) 
		{
			return (int)(c1.getPathCount()-c2.getPathCount());
        }
    };
}