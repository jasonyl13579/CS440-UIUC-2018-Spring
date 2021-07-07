import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class planningGraph {
	
	private int [][]array;
	private ArrayList<graphNode> layers;
	private Queue<Integer> path;
	private ArrayList<ArrayList<ArrayList<Integer>>> noGoodTable;
	private int rowSize;
	private int colSize;
	private ArrayList<ArrayList<Integer>> gadgets;
	private int layerIndex;
	private graphNode nodeNow;
	private Stack<Integer> plan;
	private ArrayList<ArrayList<ArrayList<Integer>>> goals;
	
	//actions
	public final static int BtoA = 1; public final static int CtoA = 6; public final static int DtoA = 11; public final static int EtoA = 16;
	public final static int AtoB = 2; public final static int CtoB = 7; public final static int DtoB = 12; public final static int EtoB = 17;
	public final static int AtoC = 3; public final static int BtoC = 8; public final static int DtoC = 13; public final static int EtoC = 18;
	public final static int AtoD = 4; public final static int BtoD = 9; public final static int CtoD = 14; public final static int EtoD = 19;
	public final static int AtoE = 5; public final static int BtoE = 10; public final static int CtoE = 15; public final static int DtoE = 20;

	public planningGraph(int [][]array, int rowNumber, int colNumber)
	{
		this.array = array.clone();
		this.rowSize = rowNumber;
		this.colSize = colNumber;
		layers = new ArrayList();
		goals = new ArrayList<ArrayList<ArrayList<Integer>>>();
		gadgets = new ArrayList<ArrayList<Integer>>();
		plan = new Stack<Integer>();
		
		
		ArrayList<Integer> goal = new ArrayList<Integer>();
		for(int i = 0; i < this.rowSize;i++) 
		{
			ArrayList<Integer> gadget = new ArrayList();
			gadget.add(i);
			for(int j=0;j<this.colSize;j++)
			{
				gadget.add(array[i][j]);
			}
			gadgets.add(gadget);
		}
		
		ArrayList<ArrayList<Integer>> goalTemp = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < this.rowSize; i++) {
			goal.add(i);
			for(int j = 0; j < this.colSize; j++)
				goal.add(array[i][j] + colSize);
			goalTemp.add((ArrayList<Integer>) goal.clone());
			goal.clear();
		}
		goals.add(goalTemp);
		layerIndex = -1;
	}
	
	//graphPlan
	public void graphPlan()
	{
		graphNode initNode = new graphNode(0);
		nodeNow = initNode;
		graphNode nodePre = new graphNode(0);
		ArrayList<ArrayList<Integer>> statesTemp = new ArrayList<ArrayList<Integer>>();
		
		for(int i = 0; i < rowSize; i++) {
			statesTemp.add(gadgets.get(i));
		}
		initNode.addStates(statesTemp);
		layers.add(initNode);
		layerIndex++;
		
		//first expand to goal
		while(layerIndex == 0 || !(reachGoal(nodeNow))) {
			nodePre = nodeNow;
			expand();
		}
		
		while(!extract(mapGoals(this.goals), layerIndex)) {
			expand();
			if(!plan.isEmpty())
				plan.clear();
		}
		printPath();
	}
		
	//expand
	public void expand()
	{
		graphNode preLayer = layers.get(layers.size() - 1);
		graphNode curLayer = new graphNode(layers.size());
		
		//create action layer
		for(int i = 1; i <= 20; i++) {
			if(preLayer.canMove(i)) {
				curLayer.addActions(i);
			}		
		}
		
		//create mutex action layer
		ArrayList<Integer> mutexAction = new ArrayList<Integer>();
		for(Integer action1 : curLayer.getActions()) 
			for(Integer action2 : curLayer.getActions()) 
				if(action1 != action2) 
					if(mutex(action1, action2, preLayer)) {
						mutexAction.add(action1);
						mutexAction.add(action2);
						curLayer.addMutexActions((ArrayList<Integer>) mutexAction.clone());
						mutexAction.clear();
					}
		
		//create proposition layer
		ArrayList<ArrayList<Integer>> stateTemp  = new ArrayList<ArrayList<Integer>>();
		for(ArrayList<ArrayList<Integer>> state : preLayer.getStates()) {
			for(Integer action : curLayer.getActions()) {
				stateTemp = map(state);
				for(ArrayList<Integer> factory : stateTemp) {
					if(factory.get(1) <= 5)
						move(factory, action);
				}
				if(!curLayer.getStates().contains(stateTemp))
					curLayer.addStates(stateTemp);
				if(!curLayer.getStates().contains(state))
					curLayer.addStates(map(state));
			}
		}
			
		//create mutex proposition layer
		ArrayList<ArrayList<ArrayList<Integer>>> mutexState = new ArrayList<ArrayList<ArrayList<Integer>>>();
		for(ArrayList<ArrayList<Integer>> state1 : curLayer.getStates())
			for(ArrayList<ArrayList<Integer>> state2 : curLayer.getStates()) {
				if(state1 != state2)
					if(mutex(state1, state2, curLayer)) {
						mutexState.add(map(state1));
						mutexState.add(map(state2));
						curLayer.addMutexStates((ArrayList<ArrayList<ArrayList<Integer>>>) mutexState.clone());
						mutexState.clear();
					}
			}
		
		//----------------add preconditions to each action----------------
		for(Integer action : curLayer.actions)
			preLayer.linkPreconds(action);
		
		layers.add(curLayer);
		nodeNow = curLayer;
		layerIndex++;
		System.out.println("Layer : " + layerIndex + " Size : " + curLayer.getStates().size());
	}
		
		
	//backward search
	public boolean extract(ArrayList<ArrayList<ArrayList<Integer>>> goal, int curIndex)
	{
		if(curIndex == 0)
			return true;
		if(gpSearch(goal, new Stack<Integer>(), curIndex, 1) || gpSearch(goal, new Stack<Integer>(), curIndex, 4) || gpSearch(goal, new Stack<Integer>(), curIndex, 5))
			return true;
		
		return false;
	}
	
	public boolean gpSearch(ArrayList<ArrayList<ArrayList<Integer>>> goal, Stack<Integer> planTemp, int curIndex, int curLoc)
	{
		if(reachStart(goal)) {
			this.plan.addAll(planTemp);
			return true;
		}
		ArrayList<ArrayList<Integer>> state = goal.get(0);
		ArrayList<Integer> mutexAction = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> mutexActions = layers.get(curIndex).getMutexActions();
		ArrayList<Integer> providers = new ArrayList<Integer>();
		Integer provider; 
		Stack<Integer> planTemp2 = (Stack<Integer>) planTemp.clone();
		
		for(Integer action1 : layers.get(curIndex).getActions()) {
            if(planTemp.isEmpty()) {
                boolean movable = false;
                for(ArrayList<Integer> factory : state) {
                    if(factory.get(factory.size()-1)-(factory.size()-1) == action1%5 || (factory.get(factory.size()-1)-(factory.size()-1) == 5 && action1%5 == 0)) {
                        providers.add(action1);
                        break;
                    }
                }
            }
            else {
                for(Integer action2 : planTemp) {
                    boolean movable = false;
                    for(ArrayList<Integer> factory : state) {
                        if(factory.get(factory.size()-1)-(factory.size()-1) == action1%5 || (factory.get(factory.size()-1)-(factory.size()-1) == 5 && action1%5 == 0)) {
                            movable = true;
                            break;
                        }
                    }
                    if(movable) {
                        mutexAction.add(action1);
                        mutexAction.add(action2);
                        if(!mutexActions.contains(mutexAction))
                            providers.add(action1);
                        mutexAction.clear();
                    }
                }
            }
        if(!providers.isEmpty()) {
            provider = providers.get(0);
            for(ArrayList<ArrayList<Integer>> goalTemp : goal)
                for(ArrayList<Integer> factory : goalTemp)
                    backMove(factory, provider);
            planTemp.add(provider);
            return gpSearch(goal, planTemp, curIndex);
        }
        else {
            if(extract(precond(planTemp, curIndex), curIndex - 1)) {
                this.plan.addAll(planTemp);
                return true;
            }
                return false;
        }
	}
	
	public ArrayList<ArrayList<ArrayList<Integer>>> precond(Stack<Integer> actions, int curIndex)
	{
		graphNode curLayer = layers.get(curIndex);
		ArrayList<ArrayList<ArrayList<Integer>>> precondsTemp = new ArrayList<ArrayList<ArrayList<Integer>>>();
		for(Integer action : actions) 
			for(ArrayList<ArrayList<Integer>> precondTemp : curLayer.getPreconds().get(action-1)) 
				precondsTemp.add(precondTemp);
			
		return precondsTemp;	
	}

	public ArrayList<ArrayList<Integer>> map(ArrayList<ArrayList<Integer>> state)
	{
		ArrayList<ArrayList<Integer>> stateTemp = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> factoryTemp = new ArrayList<Integer>();
		
		for(ArrayList<Integer> factory : state) {
			for(Integer comp : factory)
				factoryTemp.add(comp);
			stateTemp.add((ArrayList<Integer>) factoryTemp.clone());
			factoryTemp.clear();
		}
		
		return stateTemp;
	}
	
	public ArrayList<ArrayList<ArrayList<Integer>>> mapGoals(ArrayList<ArrayList<ArrayList<Integer>>> goals)
	{
		ArrayList<ArrayList<ArrayList<Integer>>> goalsTemp = new ArrayList<ArrayList<ArrayList<Integer>>>();
		ArrayList<ArrayList<Integer>> goalTemp = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> factoryTemp = new ArrayList<Integer>();
		
		for(ArrayList<ArrayList<Integer>> goal : goals) {
			for(ArrayList<Integer> factory : goal) {
				for(Integer comp : factory)
					factoryTemp.add(comp);
				goalTemp.add((ArrayList<Integer>) factoryTemp.clone());
				factoryTemp.clear();
			}
			goalsTemp.add((ArrayList<ArrayList<Integer>>) goalTemp.clone());
			goalTemp.clear();
		}
		return goalsTemp;
	}
	//check for mutex
	public boolean mutex(int a1, int a2, graphNode preLayer)
	{
		boolean XtoAtoX = ((a1 == BtoA)||(a1 == CtoA)||(a1 == DtoA)||(a1 == EtoA))&&((a2 == AtoB)||(a2 == AtoC)||(a2 == AtoD)||(a2 == AtoE));
		boolean XtoBtoX = ((a1 == AtoB)||(a1 == CtoB)||(a1 == DtoB)||(a1 == EtoB))&&((a2 == BtoA)||(a2 == BtoC)||(a2 == BtoD)||(a2 == BtoE));
		boolean XtoCtoX = ((a1 == AtoC)||(a1 == BtoC)||(a1 == DtoC)||(a1 == EtoC))&&((a2 == CtoA)||(a2 == CtoB)||(a2 == CtoD)||(a2 == CtoE));
		boolean XtoDtoX = ((a1 == AtoD)||(a1 == BtoD)||(a1 == CtoD)||(a1 == EtoD))&&((a2 == DtoA)||(a2 == DtoB)||(a2 == DtoC)||(a2 == DtoE));
		boolean XtoEtoX = ((a1 == AtoE)||(a1 == BtoE)||(a1 == CtoE)||(a1 == DtoE))&&((a2 == EtoA)||(a2 == BtoE)||(a2 == CtoE)||(a2 == DtoE));
		
		ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>> mutexStates = preLayer.getMutexStates();
		ArrayList<ArrayList<ArrayList<Integer>>> statePair = new ArrayList<ArrayList<ArrayList<Integer>>>();
		
		if(XtoAtoX || XtoBtoX || XtoCtoX || XtoDtoX || XtoEtoX) {
			boolean flag = false;
			boolean allStatesDelete = true;
			for(ArrayList<ArrayList<Integer>> preState : preLayer.getStates()) {
				flag = false;
				for(ArrayList<Integer> factory : preState)
					if((factory.get(1) == a1%5 || (factory.get(1)==5 && a1%5==0)) && (factory.get(2) == a2%5 || (factory.get(2)==5 && a2%5==0)))
						flag = true;
				allStatesDelete = allStatesDelete && flag;
			}
			
			if(allStatesDelete)		//check if delete other's result
				return true;
			else
				return false;
		}
		return true;
	}
	
	public boolean mutex(ArrayList<ArrayList<Integer>> state1, ArrayList<ArrayList<Integer>> state2, graphNode curLayer)
	{
		ArrayList<Integer> actionPair = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> mutexActions = curLayer.getMutexActions();
		
		for(Integer producer1 : curLayer.getProducers(state1))
			for(Integer producer2 : curLayer.getProducers(state2)) {
				actionPair.add(producer1);
				actionPair.add(producer2);
				if(!mutexActions.contains(actionPair))
					return false;
				actionPair.clear();
			}
		
		return true;
	}
	
	public boolean move(ArrayList<Integer> factory, Integer action)
	{
		if(factory.get(1) == action%5 || (factory.get(1) == 5 && action%5 == 0)) {
			int temp = factory.remove(1);
			factory.add(temp+colSize);
			return true;
		}
		return false;
	}
	
	public void backMove(ArrayList<Integer> factory, Integer action)
	{
		int temp = factory.get(factory.size()-1);
		if(temp - (factory.size()-1) == action%5 || (action%5 == 0 && temp - (factory.size()-1) == 5)) {
			factory.remove(factory.size()-1);	//factory.size()-1 = colSize
			factory.add(1, temp - factory.size());	//factory.size() = colSize
		}
	}
	public void plotMove(int action)
	{
		for(int[] stateArray : this.array)
			if(stateArray[0] == action%5 || (stateArray[0] == 5 && action%5 == 0)) {
				for(int i = 0; i < colSize - 1; i++)
					stateArray[i] = stateArray[i+1];
				stateArray[colSize - 1] = 0;
			}
    }
	
	public boolean reachGoal(graphNode curLayer)
	{
		boolean factorySame;
		boolean stateSame;
		for(ArrayList<ArrayList<Integer>> state: curLayer.getStates()) {
			stateSame = true;
			for(ArrayList<Integer> factory : state) {
				int i = 0;
				factorySame = true;
				for(Integer comp : factory) {
					
					if(comp != this.goals.get(0).get(factory.get(0)).get(i)) {
						factorySame = false;
						break;
					}
					i++;
				}
				stateSame = stateSame && factorySame;
			}
			if(stateSame)
				return true;
		}
		
		return false;
	}
	
	public boolean reachStart(ArrayList<ArrayList<ArrayList<Integer>>> goal)
	{
		boolean factorySame;
		boolean stateSame;
		for(ArrayList<ArrayList<Integer>> state: goal) {
			stateSame = true;
			for(ArrayList<Integer> factory : state) {
				int i = 0;
				factorySame = true;
				for(Integer comp : factory) {
					if(comp != this.gadgets.get(factory.get(0)).get(i)) {
						factorySame = false;
						break;
					}
					i++;
				}
				stateSame = stateSame && factorySame;
			}
			if(stateSame)
				return true;
		}
		
		return false;
	}
	
	public boolean rightLoc(int action, int location)
	{
		switch(location)
		{
		case 1 :
			if(action == AtoB || action == AtoC || action == AtoD || action == AtoE)
				return true;
			break;
		case 2 :
			if(action == BtoA || action == BtoC || action == BtoD || action == BtoE)
				return true;
			break;
		case 3 : 
			if(action == CtoA || action == CtoB || action == CtoD || action == CtoE)
				return true;
			break;
		case 4 :
			if(action == DtoA || action == DtoB || action == DtoC || action == DtoE)
				return true;
			break;
		case 5 : 
			if(action == EtoA || action == EtoB || action == EtoC || action == EtoD)
				return true;
			break;
		}
		return false;
	}
	
	public void printPath()
	{
		plotGraph plotFactory = new plotGraph(this.array, rowSize, colSize);
		Integer action;
		
		while((action=this.plan.pop())!=null)
		{
			try
			{
				Thread.sleep(750);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			plotMove(action);
			plotFactory=new plotGraph(this.array,rowSize,colSize);
			if(this.plan.size()==0)
			{
				break;
			}
		}
		
	}
}
