import java.util.Set;
import java.util.HashSet;
import java.util.Stack;
import java.util.Iterator;
import java.util.Queue;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.LinkedList;

public class smartManufacture
{
	private int[][] array;
	private int rowNumber;
	private int colNumber;
	private int rowCount;
	private int colCount;
	private int rowSize;
	private int colSize;
	private Queue<smartManufactureState> stateQueue;
	private plotGraph plotFactory;
	private int SLEEP_TIME = 200;
	private int round = 0;
	private int heuristicChoice=1;
	private int value;
	private int last=0;
	private Queue<Integer> path;
	private smartManufactureState nextState[];
	private ArrayList<ArrayList<Integer>> gadgets;
	
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
		this.stateQueue=new PriorityQueue<>(1, valueComparator);
		nextState = new smartManufactureState[5];
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
		
		int end=0;
		
		while(end!=1)
		{
			int affectA=0;
			int affectB=0;
			int affectC=0;
			int affectD=0;
			int affectE=0;
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
			
			//stateQueue*************************************
			if(affectA!=0)
			{
				nextState[0].setParent(stateNow);
				nextState[0].setMove(1);
				nextState[0].setPathCount(stateNow.getPathCount()+1);
				stateQueue.add(nextState[0]);
			}
			if(affectB!=0)
			{
				nextState[1].setParent(stateNow);
				nextState[1].setMove(2);
				nextState[1].setPathCount(stateNow.getPathCount()+1);
				stateQueue.add(nextState[1]);
			}
			if(affectC!=0)
			{
				nextState[2].setParent(stateNow);
				nextState[2].setMove(3);
				nextState[2].setPathCount(stateNow.getPathCount()+1);
				stateQueue.add(nextState[2]);
			}
			if(affectD!=0)
			{
				nextState[3].setParent(stateNow);
				nextState[3].setMove(4);
				nextState[3].setPathCount(stateNow.getPathCount()+1);
				stateQueue.add(nextState[3]);
			}
			if(affectE!=0)
			{
				nextState[4].setParent(stateNow);
				nextState[4].setMove(5);
				nextState[4].setPathCount(stateNow.getPathCount()+1);
				stateQueue.add(nextState[4]);
			}
			//stateQueue*************************************
			//StateChoosing
			smartManufactureState newState = stateQueue.poll();
			stateNow = newState;
			//stateChoosing
			
			//System.out.println(calculateHeuristic());
			round++;
		}
		//print path
		int pathCount=0;
		Stack<Integer> nodeStack = new Stack<Integer>();
		Stack<smartManufactureState> resultStack = new Stack<smartManufactureState>();
		nodeStack.push(stateNow.getMove());
		resultStack.push(stateNow);
		while((stateNow=stateNow.getParent())!=null)
		{
			nodeStack.push(stateNow.getMove());
			resultStack.push(stateNow);
			pathCount++;
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
		System.out.println(pathCount);
		printFinalAnimation(resultStack);
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
					existA=1;
				}
				if(gadgets.get(j).get(i)==2)
				{
					existB=1;
				}
				if(gadgets.get(j).get(i)==3)
				{
					existC=1;
				}
				if(gadgets.get(j).get(i)==4)
				{
					existD=1;
				}
				if(gadgets.get(j).get(i)==5)
				{
					existE=1;
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
	public static Comparator<smartManufactureState> valueComparator = new Comparator<smartManufactureState>()
	{
        @Override
        public int compare(smartManufactureState c1, smartManufactureState c2) 
		{
			return (int)(c1.getEvaluation()-c2.getEvaluation());
        }
    };
}