import java.awt.List;
import java.util.ArrayList;
import javafx.util.Pair;

public class graphNode {

	graphNode parent;
	ArrayList<ArrayList< ArrayList<Integer>>> states;	//states of five factories
	ArrayList< Integer > actions;	//actions of each action layer
	ArrayList<ArrayList< ArrayList<ArrayList<Integer>>>> mutexStates;
	ArrayList< ArrayList<Integer> > mutexActions;
	ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>  preconds;		//preconditions of every actions for next layer
	int index;
	
	public graphNode(int index)
	{
		states = new ArrayList();
		actions = new ArrayList();
		mutexStates = new ArrayList();
		mutexActions = new ArrayList();
		preconds = new ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>();
		for(int i = 0; i < 20; i++)
			preconds.add(new ArrayList<ArrayList<ArrayList<Integer>>>());
		this.index = index;
	}
	
	//add part
	public void addStates(ArrayList<ArrayList<Integer>> state)
	{
		this.states.add(state);
	}
	
	public void addActions(Integer action)
	{
		this.actions.add(action);
	}
	
	public void addMutexStates(ArrayList<ArrayList<ArrayList<Integer>>> mutexState)
	{
		this.mutexStates.add(mutexState);
	}
	
	public void addMutexActions(ArrayList<Integer> mutexAction)
	{
		this.mutexActions.add(mutexAction);
	}
	
	public void addPrecond(ArrayList<ArrayList<ArrayList<Integer>>> precond, int index)
	{
		this.preconds.remove(index-1);
		this.preconds.add(index-1, precond);
	}
	
	//get part
	public ArrayList<ArrayList< ArrayList<Integer>>> getStates()
	{
		return (ArrayList<ArrayList<ArrayList<Integer>>>) this.states.clone();
	}
	
	public ArrayList<Integer> getActions()
	{
		return (ArrayList<Integer>) this.actions.clone();
	}
	
	public ArrayList< ArrayList<Integer> > getMutexActions()
	{
		return (ArrayList<ArrayList<Integer>>) this.mutexActions.clone();
	}
	
	public ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>> getMutexStates()
	{
		return (ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>) this.mutexStates.clone();
	}
	
	public ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>> getPreconds()
	{
		return (ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>) this.preconds.clone();
	}
	
	public ArrayList<Integer> getProducers(ArrayList<ArrayList<Integer>> state)
	{
		ArrayList<Integer> actionsTemp = new ArrayList<Integer>();
		
		for(Integer actionTemp : actions) {
			for(ArrayList<Integer> factory : state)
				if(factory.get(factory.size() - 1)-(factory.size() - 1) == actionTemp%5 || (factory.get(factory.size() - 1)-(factory.size() - 1) == 5 && actionTemp%5 == 0)) {
					actionsTemp.add(actionTemp);
					break;
				}
		}
		
		return actionsTemp;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public graphNode clone()
	{
		graphNode newNode = new graphNode(-1);
		newNode.states = (ArrayList<ArrayList<ArrayList<Integer>>>) this.states.clone();
		newNode.actions = (ArrayList<Integer>) this.actions.clone();
		newNode.mutexStates = (ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>) this.mutexStates.clone();
		newNode.mutexActions = (ArrayList<ArrayList<Integer>>) this.mutexActions.clone();
		newNode.preconds = (ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>) this.preconds.clone();
		newNode.index = this.index;
		
		return newNode;
	}
	
	//bool part
	
	public boolean canMove(int action)
	{
		boolean flag = false;
		ArrayList<ArrayList<Integer>> pair = new ArrayList<ArrayList<Integer>>();
		for(ArrayList<ArrayList<Integer>> state1: states) {
//			for(ArrayList<Integer> state2: states) 
//				if(state1.get(1) <= 5 && (state1.get(1) == action%5 || (action%5 == 0 && state1.get(1) == 5)) && state2.get(1) <= 5 && (state2.get(1) == action%5 || (action%5 == 0 && state2.get(1) == 5))) {
//					pair.add(state1); pair.add(state2);
//					if(!this.mutexStates.contains(pair)) {
//						flag = true;
//						break;
//					}
//					return true;
//				}
//			if(flag)
//				break;
			for(ArrayList<Integer> factory : state1) {
				if(factory.get(1) <= 5 && (factory.get(1) == action%5 || (action%5 == 0 && factory.get(1) == 5)))
					return true;
			}
		}
		return false;
	}
	
	//link precond
	public void linkPreconds(int action)
	{
		ArrayList<ArrayList<ArrayList<Integer>>> states = new ArrayList<ArrayList<ArrayList<Integer>>>();
		for(ArrayList<ArrayList<Integer>> state1: this.states) 
			for(ArrayList<Integer> factory : state1)
				if(factory.get(1) <= 5 && (factory.get(1) == action%5 || (factory.get(1) == 5 && action%5 == 0))) { 
					states.add(state1);
					break;
				}
		addPrecond(states, action);
	}
	
}
