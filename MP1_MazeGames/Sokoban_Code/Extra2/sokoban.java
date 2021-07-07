import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.LinkedList;
public class sokoban
{
	private int[][] array;
	private int rowNumber;
	private int colNumber;
	private point currentPosition;
	private point startPosition;
	private Set<Integer> storagePointsInt;
	private Set<point> storagePoints;
	private Set<Integer> boxPointsInt;
	private Set<point> boxPoints;
	private Set<point> finishPoints;
	private Set<Integer> forbidPoints;
	private LinkedList<Integer> path;
	private Set<sokobanState> forbidState;
	private sokobanState nextState[];
	private Queue<sokobanState> stateQueue;
	private plotGraph plotMaze;
	private int round = 1;
	private int maxQueueSize = 0;
	private int SLEEP_TIME = 500;
	private static final int DIR_UP = 0;
	private static final int DIR_RIGHT = 1;
	private static final int DIR_DOWN = 2;
	private static final int DIR_LEFT = 3;
	
	private static final int MAZE_ROAD = 1;
	private static final int MAZE_WALL = 2;
	private static final int MAZE_PEOPLE = 3;
	private static final int MAZE_STORAGE = 4;
	private static final int MAZE_BOX= 5;
	private static final int MAZE_BOX_ON_STORAGE= 6;
	private static final int MAZE_PEOPLE_ON_STORAGE= 7;
	
	private boolean direction[];
	private float value;
	public sokoban(int[][] array,int rowNumber,int colNumber,point currentPosition,Set<point> storagePoints,Set<point> boxPoints,Set<point> finishPoints)
	{
		this.array=array;
		this.rowNumber=rowNumber;
		this.colNumber=colNumber;
		this.currentPosition=currentPosition;
		this.startPosition = currentPosition;
		this.storagePoints=storagePoints;
		this.boxPoints=boxPoints;
		this.finishPoints=finishPoints;
		this.storagePointsInt = new HashSet<Integer>();
		this.boxPointsInt = new HashSet<Integer>();
		this.forbidPoints = new HashSet<Integer>();
		path = new LinkedList<Integer>();
		for (point p: storagePoints) {
			storagePointsInt.add(p.getInt(colNumber));
		}
		for (point p: boxPoints) {
			boxPointsInt.add(p.getInt(colNumber));
		}
		stateQueue = new PriorityQueue<>(1, valueComparator);
		forbidState = new HashSet<sokobanState>();
		nextState = new sokobanState[4];
		direction = new boolean[4];
		for (int i=0 ; i<4 ; i++) direction[i] = true;
	}
	public void sokobanAlgorithm()
	{
		sokobanState root = new sokobanState(array,rowNumber,colNumber,currentPosition.getRow(),currentPosition.getCol(),boxPoints,path,0,-1);
		sokobanState stateNow = root;
		//setDirectionbyWall(startPosition, stateNow);
		//path.add(currentPosition.getInt(colNumber));
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				boolean count = false;
				if (array[i][j] == MAZE_ROAD || array[i][j] == MAZE_PEOPLE) {
					if (array[i+1][j] == MAZE_WALL && array[i][j+1] == MAZE_WALL) count = true;
					if (array[i][j+1] == MAZE_WALL && array[i-1][j] == MAZE_WALL) count = true;
					if (array[i-1][j] == MAZE_WALL && array[i][j-1] == MAZE_WALL) count = true;
					if (array[i][j-1] == MAZE_WALL && array[i+1][j] == MAZE_WALL) count = true;
					if (count) forbidPoints.add(i*colNumber+j);
				}
			}
		}
		int round=0;
	/*	for (int p : forbidPoints) {
			System.out.print(p+",");
		}*/
		int end = 0;
		boolean readOnly = false;
		while(round !=100000 && end !=1){
			recoverState(stateNow);
			round++;
			if (boxPointsInt.equals(storagePointsInt)){
				System.out.print("Find solution for this maze.\n");
				end = 1;
				break;
			}		
			
			if ((round % 100) == 1){
                 plotMaze=new plotGraph(array,rowNumber,colNumber);
			}
			//plotMaze=new plotGraph(array,rowNumber,colNumber);
			/*System.out.print("\nDir:"+stateNow.getParentDir()+"\n");
			if  (stateNow.getParentDir() == DIR_UP) {upMove(false);}
			else if  (stateNow.getParentDir() == DIR_DOWN) {downMove(false);}
			else if  (stateNow.getParentDir() == DIR_LEFT) {leftMove(false);}
			else if  (stateNow.getParentDir() == DIR_RIGHT){rightMove(false);}*/
			
			System.out.print("Round:" + round + " Cur:");
			currentPosition.print();
			/*
			System.out.print(".\n");*/
			/*System.out.print("(3:2)"+this.array[3][2]);
			System.out.print("(3:3)"+this.array[3][3]);
			System.out.print("(4:2)"+this.array[4][2]);
			System.out.print("(2:2)"+this.array[2][2]);*/

			boolean jump = false;
			if (!upMove(readOnly)){
				stateNow.setDirection(DIR_UP,false);
				nextState[DIR_UP] = null;
				//properMove--;
			}
			else
			{		
				for (sokobanState state: forbidState) {
					if (state.compareForbidState(boxPointsInt,currentPosition.getRow(),currentPosition.getCol())){
						stateNow.setDirection(DIR_UP,false);
						jump = true;
						//properMove--;
					}
				}
				for (int p : stateNow.getBoxPointsInt()) {
					if (forbidPoints.contains(p)) {
						stateNow.setDirection(DIR_UP,false);
						jump = true;
					}
				}
				if (!jump){
					currentPosition.print();
					path.add(DIR_UP);
					nextState[DIR_UP] = new sokobanState(array,rowNumber,colNumber,currentPosition.getRow(),currentPosition.getCol(),boxPoints,path,0,DIR_UP);
					//nextState[DIR_UP].setDirection(DIR_DOWN,false);
					nextState[DIR_UP].setHeuristic(calculateHeuristic(nextState[DIR_UP]));
					nextState[DIR_UP].setValue(path.size()+calculateHeuristic(nextState[DIR_UP]));
					System.out.print("value:"+ nextState[DIR_UP].getValue() +"\n");					
				}
				recoverState(stateNow);
			}	
			jump = false;
			readOnly = false;
			if (!rightMove(readOnly)){
				stateNow.setDirection(DIR_RIGHT,false);
				nextState[DIR_RIGHT] = null;
				//properMove--;
			}
			else
			{		
				for (sokobanState state: forbidState) {
					if (state.compareForbidState(boxPointsInt,currentPosition.getRow(),currentPosition.getCol())){
						stateNow.setDirection(DIR_RIGHT,false);
						jump = true;
						//properMove--;
					}
				}
				for (int p : stateNow.getBoxPointsInt()) {
					if (forbidPoints.contains(p)) {
						stateNow.setDirection(DIR_RIGHT,false);
						jump = true;
					}
				}
				if (!jump){
					currentPosition.print();
					path.add(DIR_RIGHT);
					nextState[DIR_RIGHT] = new sokobanState(array,rowNumber,colNumber,currentPosition.getRow(),currentPosition.getCol(),boxPoints,path,0,DIR_RIGHT);
				//	nextState[DIR_RIGHT].setDirection(DIR_LEFT,false);
					nextState[DIR_RIGHT].setHeuristic(calculateHeuristic(nextState[DIR_RIGHT]));
					nextState[DIR_RIGHT].setValue(path.size()+calculateHeuristic(nextState[DIR_RIGHT]));
					System.out.print("value:"+ nextState[DIR_RIGHT].getValue() +"\n");
					
				}
				recoverState(stateNow);
			}	
			jump = false;
			if (!downMove(readOnly)){
				stateNow.setDirection(DIR_DOWN,false);
				nextState[DIR_DOWN] = null;
				//properMove--;
			}
			else
			{		
				for (sokobanState state: forbidState) {
					if (state.compareForbidState(boxPointsInt,currentPosition.getRow(),currentPosition.getCol())){
						stateNow.setDirection(DIR_DOWN,false);
						jump = true;
						//properMove--;
					}
				}
				for (int p : stateNow.getBoxPointsInt()) {
					if (forbidPoints.contains(p)) {
						stateNow.setDirection(DIR_DOWN,false);
						jump = true;
					}
				}
				if (!jump){
					currentPosition.print();
					path.add(DIR_DOWN);
					nextState[DIR_DOWN] = new sokobanState(array,rowNumber,colNumber,currentPosition.getRow(),currentPosition.getCol(),boxPoints,path,0,DIR_DOWN);
				//	nextState[DIR_DOWN].setDirection(DIR_UP,false);
					nextState[DIR_DOWN].setHeuristic(calculateHeuristic(nextState[DIR_DOWN]));
					nextState[DIR_DOWN].setValue(path.size()+calculateHeuristic(nextState[DIR_DOWN]));
					System.out.print("value:"+ nextState[DIR_DOWN].getValue() +"\n");
				}
				recoverState(stateNow);
			}	
			jump = false;			
			if (!leftMove(readOnly)){
				stateNow.setDirection(DIR_LEFT,false);
				nextState[DIR_LEFT] = null;
				//properMove--;
			}
			else
			{			
				for (sokobanState state: forbidState) {
					if (state.compareForbidState(boxPointsInt,currentPosition.getRow(),currentPosition.getCol())){
						stateNow.setDirection(DIR_LEFT,false);
						jump = true;
						//properMove--;
					}
				}
				for (int p : stateNow.getBoxPointsInt()) {
					if (forbidPoints.contains(p)) {
						stateNow.setDirection(DIR_LEFT,false);
						jump = true;
					}
				}
				if (!jump){
					currentPosition.print();
					path.add(DIR_LEFT);
					nextState[DIR_LEFT] = new sokobanState(array,rowNumber,colNumber,currentPosition.getRow(),currentPosition.getCol(),boxPoints,path,0,DIR_LEFT);
				//	nextState[DIR_LEFT].setDirection(DIR_RIGHT,false);
					nextState[DIR_LEFT].setHeuristic(calculateHeuristic(nextState[DIR_LEFT]));
					nextState[DIR_LEFT].setValue(path.size()+calculateHeuristic(nextState[DIR_LEFT]));
					System.out.print("value:"+ nextState[DIR_LEFT].getValue() +"\n");
				}
				recoverState(stateNow);
			}		
			direction = stateNow.getDirection();
			for (int i=0 ; i<4 ; i++){
				System.out.print(direction[i]+".");
			}
			System.out.print("\n");
			/*int min_value_index = -1;
			int value[] = new int[4];
			for (int i=0 ; i<4 ; i++) {
				if (direction[i]==false)  value[i] = 1000000; 
				else{
					value[i] = nextState[i].getValue();
				}
			}
			
			for (int i=0 ; i<properMove ; i++){
				stateQueue.add(nextState[]);
			}*/
			for (int i=0 ; i<4 ; i++) {
				if (direction[i]){
					assert(nextState[i] != null);
					stateQueue.add(nextState[i]);
					stateNow.setDirection(i,false);
				}
			}	
			if (maxQueueSize < stateQueue.size()) maxQueueSize = stateQueue.size();		
			forbidState.add(stateNow);	
			sokobanState newState = stateQueue.poll();
			if (newState == null){
				System.out.print("No solution for this maze.\n");
				end = 1;
			}
			stateNow = newState;	
			
			
			round++;
		}//end of while
		System.out.println(Arrays.toString(stateNow.getPath().toArray()));
		System.out.println("Max queue size:" + maxQueueSize );	
		System.out.println("path length:" + stateNow.getPath().size());
		plotMaze=new plotGraph(array,rowNumber,colNumber);
		endAnimate(root,stateNow.getPath());	
	}
	public void recoverState(sokobanState state)
	{
		this.array = new int[rowNumber][colNumber];
		for(int i=0;i<rowNumber;i++)
		{
			for(int j=0;j<colNumber;j++)
			{
				this.array[i][j]=state.getArrayState()[i][j];
				//System.out.print(","+this.array[i][j]);
			}
		}
		//this.array  = state.getArrayState();
		this.currentPosition.setPoint(state.getCurrentRow(),state.getCurrentCol());
		this.boxPoints=state.getBoxPoints();
		this.boxPointsInt = state.getBoxPointsInt();
		//this.storagePoints=state.getStoragePositionsState();
		//this.finishPoints=state.getFinishPositionsState();
		this.finishPoints.clear();
        this.finishPoints.addAll(this.boxPoints);
        this.finishPoints.retainAll(this.storagePoints);
		this.value = state.getValue();
		this.path = state.getPath();
		this.direction = state.getDirection();
		//	write pointInt
	}
	public float calculateHeuristic(sokobanState state)
	{
		float heuristic=0;
		int stateArray[][] = state.getArrayState();
		
		
		Set<Integer> result = new HashSet<>();
		 result.clear();
	     result.addAll(storagePointsInt);
	     result.retainAll(boxPointsInt);
		heuristic -= result.size()*5000;
		
		int row, col, up, down, left, right,leftUp,leftDown,rightUp,rightDown, now;
		//box is beside the wall
		boolean besideWall = false;
		//box is beside other boxes which aren't on the storage point
		boolean besideBox = false;
		//box is in the corner of two boxes on storage point or boxes
		boolean cantMove = false;
		//box is on storage point and other boxes are at storage point around it
		boolean aroundStorage = false;
		
		for(Iterator<point> itBox=state.getBoxPoints().iterator();itBox.hasNext();)
		{
			point boxTemp=itBox.next();
			row = boxTemp.getRow();	col = boxTemp.getCol();
			up = stateArray[row - 1][col];
			down = stateArray[row + 1][col];
			left = stateArray[row][col - 1];
			right = stateArray[row][col + 1];
			
			leftUp = stateArray[row - 1][col-1];
			leftDown = stateArray[row + 1][col-1];
			rightUp = stateArray[row-1][col +1];
			rightDown = stateArray[row+1][col + 1];
			
			now = stateArray[row][col];
			besideBox = false;
			//up , down, left , right
			besideWall = besideWall || (up == MAZE_WALL);
			besideWall = besideWall || (down == MAZE_WALL);
			besideWall = besideWall || (left == MAZE_WALL);
			besideWall = besideWall || (right == MAZE_WALL);
			
			cantMove = false;	
			cantMove = cantMove || ((up == MAZE_BOX_ON_STORAGE) && ((left == MAZE_BOX_ON_STORAGE)||(right == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((down == MAZE_BOX_ON_STORAGE) && ((left == MAZE_BOX_ON_STORAGE)||(right == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((up == MAZE_BOX_ON_STORAGE) && ((left == MAZE_BOX)||(right == MAZE_BOX)));
			cantMove = cantMove || ((down == MAZE_BOX_ON_STORAGE) && ((left == MAZE_BOX)||(right == MAZE_BOX)));
			
			cantMove = cantMove || ((up == MAZE_BOX) && ((left == MAZE_BOX_ON_STORAGE)||(right == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((down == MAZE_BOX) && ((left == MAZE_BOX_ON_STORAGE)||(right == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((up == MAZE_BOX) && ((left == MAZE_BOX)||(right == MAZE_BOX)));
			cantMove = cantMove || ((down == MAZE_BOX) && ((left == MAZE_BOX)||(right == MAZE_BOX)));

			cantMove = cantMove || ((leftUp == MAZE_BOX_ON_STORAGE) && ((left == MAZE_BOX_ON_STORAGE)&&(up == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((leftDown == MAZE_BOX_ON_STORAGE) && ((left == MAZE_BOX_ON_STORAGE)&&(down == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((rightUp == MAZE_BOX_ON_STORAGE) && ((right == MAZE_BOX)&&(up == MAZE_BOX)));
			cantMove = cantMove || ((rightDown == MAZE_BOX_ON_STORAGE) && ((right == MAZE_BOX)&&(down == MAZE_BOX)));
			
			cantMove = cantMove || ((leftUp == MAZE_BOX) && ((left == MAZE_BOX_ON_STORAGE)&&(up == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((leftDown == MAZE_BOX) && ((left == MAZE_BOX_ON_STORAGE)&&(down == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((rightUp == MAZE_BOX) && ((right == MAZE_BOX)&&(up == MAZE_BOX)));
			cantMove = cantMove || ((rightDown == MAZE_BOX) && ((right == MAZE_BOX)&&(down == MAZE_BOX)));

			if((!besideWall))
				heuristic -= 10000;
			if(!cantMove)
				heuristic -= 1000;
		}
		return heuristic;
	}
	public void hard_coded_sokobanAlgorithm()
	{
		right();
		down();
		right();
		down();
		right();
		down();
		down();
		left();
		left();
		right();
		right();
		up();
		up();
		left();
		down();
		left();
		right();
		up();
		up();
		left();
		up();
		up();
		right();
		down();
		down();
		left();
		down();
		down();
		up();
		up();
		right();
		down();
		left();
		down();
		right();
		up();
		right();
		down();
	}
	
	public boolean upMove(boolean readOnly)
	{
		//test
		/*
		System.out.print(currentPosition.getRow());
		System.out.print(",");
		System.out.println(currentPosition.getCol());
		*/
		//System.out.println(array[currentPosition.getRow()][currentPosition.getCol()]);
		//test
		if(array[currentPosition.getRow()-1][currentPosition.getCol()]==2)//next:walls
		{
			System.out.println("Run into the wall");
			return false;
		}
		else if(array[currentPosition.getRow()-1][currentPosition.getCol()]==1)//next:space
		{
			if(array[currentPosition.getRow()][currentPosition.getCol()]==3)//not stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()-1][currentPosition.getCol()]=3;
					array[currentPosition.getRow()][currentPosition.getCol()]=1;
				}
				currentPosition.setPoint(currentPosition.getRow()-1,currentPosition.getCol());
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)//stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()-1][currentPosition.getCol()]=3;
					array[currentPosition.getRow()][currentPosition.getCol()]=4;
				}
				currentPosition.setPoint(currentPosition.getRow()-1,currentPosition.getCol());
			}
		}
		else if(array[currentPosition.getRow()-1][currentPosition.getCol()]==4)//next:storage
		{
			if(array[currentPosition.getRow()][currentPosition.getCol()]==3)//not stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()-1][currentPosition.getCol()]=7;
					array[currentPosition.getRow()][currentPosition.getCol()]=1;
				}
				currentPosition.setPoint(currentPosition.getRow()-1,currentPosition.getCol());
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)//stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()-1][currentPosition.getCol()]=7;
					array[currentPosition.getRow()][currentPosition.getCol()]=4;
				}
				currentPosition.setPoint(currentPosition.getRow()-1,currentPosition.getCol());
			}
		}
		else if(array[currentPosition.getRow()-1][currentPosition.getCol()]==5)//complex case:next:box
		{
			if(array[currentPosition.getRow()-2][currentPosition.getCol()]==2)//push to walls
			{
				System.out.println("Push to walls");
				return false;
			}
			else if(array[currentPosition.getRow()-2][currentPosition.getCol()]==5)//push to boxes
			{
				System.out.println("Push to another box");
				return false;
			}
			else if(array[currentPosition.getRow()-2][currentPosition.getCol()]==6)//push to storage boxes
			{
				System.out.println("Push to another box already on storage");
				return false;
			}
			else if(array[currentPosition.getRow()-2][currentPosition.getCol()]==1)//push to space
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow()-2,currentPosition.getCol()));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()-2][currentPosition.getCol()]=5;
						array[currentPosition.getRow()-1][currentPosition.getCol()]=3;
						
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow()-1,currentPosition.getCol());
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow()-2,currentPosition.getCol()));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()-2][currentPosition.getCol()]=5;
						array[currentPosition.getRow()-1][currentPosition.getCol()]=3;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow()-1,currentPosition.getCol());
				}
			}
			else if(array[currentPosition.getRow()-2][currentPosition.getCol()]==4)
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					finishPoints.add(new point(currentPosition.getRow()-2,currentPosition.getCol()));
					boxPoints.add(new point(currentPosition.getRow()-2,currentPosition.getCol()));
					/*for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-2))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()-2][currentPosition.getCol()]=6;
						array[currentPosition.getRow()-1][currentPosition.getCol()]=3;
						
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow()-1,currentPosition.getCol());
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					finishPoints.add(new point(currentPosition.getRow()-2,currentPosition.getCol()));
					boxPoints.add(new point(currentPosition.getRow()-2,currentPosition.getCol()));
					/*for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-2))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()-2][currentPosition.getCol()]=6;
						array[currentPosition.getRow()-1][currentPosition.getCol()]=3;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow()-1,currentPosition.getCol());
				}				
			}
		}
		else if(array[currentPosition.getRow()-1][currentPosition.getCol()]==6)
		{
			if(array[currentPosition.getRow()-2][currentPosition.getCol()]==2)//push to walls
			{
				System.out.println("Push to walls");
				return false;
			}
			else if(array[currentPosition.getRow()-2][currentPosition.getCol()]==5)//push to boxes
			{
				System.out.println("Push to another box");
				return false;
			}
			else if(array[currentPosition.getRow()-2][currentPosition.getCol()]==6)//push to storage boxes
			{
				System.out.println("Push to another box already on storage");
				return false;
			}
			else if(array[currentPosition.getRow()-2][currentPosition.getCol()]==1)//push to space
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					//storagePoints.add(new point(currentPosition.getRow()-1,currentPosition.getCol()));
					boxPoints.add(new point(currentPosition.getRow()-2,currentPosition.getCol()));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()-2][currentPosition.getCol()]=5;
						array[currentPosition.getRow()-1][currentPosition.getCol()]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow()-1,currentPosition.getCol());
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					//storagePoints.add(new point(currentPosition.getRow()-1,currentPosition.getCol()));
					boxPoints.add(new point(currentPosition.getRow()-2,currentPosition.getCol()));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()-2][currentPosition.getCol()]=5;
						array[currentPosition.getRow()-1][currentPosition.getCol()]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow()-1,currentPosition.getCol());
				}
			}
			else if(array[currentPosition.getRow()-2][currentPosition.getCol()]==4)
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					finishPoints.add(new point(currentPosition.getRow()-2,currentPosition.getCol()));
					boxPoints.add(new point(currentPosition.getRow()-2,currentPosition.getCol()));
					//storagePoints.add(new point(currentPosition.getRow()-1,currentPosition.getCol()));
					/*for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-2))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()-2][currentPosition.getCol()]=6;
						array[currentPosition.getRow()-1][currentPosition.getCol()]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow()-1,currentPosition.getCol());
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					
					boxPoints.add(new point(currentPosition.getRow()-2,currentPosition.getCol()));
					finishPoints.add(new point(currentPosition.getRow()-2,currentPosition.getCol()));
					//storagePoints.add(new point(currentPosition.getRow()-1,currentPosition.getCol()));
				/*	for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()-2))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()-2][currentPosition.getCol()]=6;
						array[currentPosition.getRow()-1][currentPosition.getCol()]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow()-1,currentPosition.getCol());
				}				
			}
		}
		assert (storagePoints.size()== boxPoints.size());
		return true;
	}//end upMove
	
	public boolean downMove(boolean readOnly)
	{
		//test
		/*
		System.out.print(currentPosition.getRow());
		System.out.print(",");
		System.out.println(currentPosition.getCol());
		*/
		//System.out.println(array[currentPosition.getRow()][currentPosition.getCol()]);
		//test
		if(array[currentPosition.getRow()+1][currentPosition.getCol()]==2)//next:walls
		{
			System.out.println("Run into the wall");
			return false;
		}
		else if(array[currentPosition.getRow()+1][currentPosition.getCol()]==1)//next:space
		{
			if(array[currentPosition.getRow()][currentPosition.getCol()]==3)//not stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()+1][currentPosition.getCol()]=3;
					array[currentPosition.getRow()][currentPosition.getCol()]=1;
				}
				currentPosition.setPoint(currentPosition.getRow()+1,currentPosition.getCol());
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)//stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()+1][currentPosition.getCol()]=3;
					array[currentPosition.getRow()][currentPosition.getCol()]=4;
				}
				currentPosition.setPoint(currentPosition.getRow()+1,currentPosition.getCol());
			}
		}
		else if(array[currentPosition.getRow()+1][currentPosition.getCol()]==4)//next:storage
		{
			if(array[currentPosition.getRow()][currentPosition.getCol()]==3)//not stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()+1][currentPosition.getCol()]=7;
					array[currentPosition.getRow()][currentPosition.getCol()]=1;
				}
				currentPosition.setPoint(currentPosition.getRow()+1,currentPosition.getCol());
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)//stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()+1][currentPosition.getCol()]=7;
					array[currentPosition.getRow()][currentPosition.getCol()]=4;
				}
				currentPosition.setPoint(currentPosition.getRow()+1,currentPosition.getCol());
			}
		}
		else if(array[currentPosition.getRow()+1][currentPosition.getCol()]==5)//complex case:next:box
		{
			if(array[currentPosition.getRow()+2][currentPosition.getCol()]==2)//push to walls
			{
				System.out.println("Push to walls");
				return false;
			}
			else if(array[currentPosition.getRow()+2][currentPosition.getCol()]==5)//push to boxes
			{
				System.out.println("Push to another box");
				return false;
			}
			else if(array[currentPosition.getRow()+2][currentPosition.getCol()]==6)//push to storage boxes
			{
				System.out.println("Push to another box already on storage");
				return false;
			}
			else if(array[currentPosition.getRow()+2][currentPosition.getCol()]==1)//push to space
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow()+2,currentPosition.getCol()));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()+2][currentPosition.getCol()]=5;
						array[currentPosition.getRow()+1][currentPosition.getCol()]=3;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow()+1,currentPosition.getCol());
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow()+2,currentPosition.getCol()));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()+2][currentPosition.getCol()]=5;
						array[currentPosition.getRow()+1][currentPosition.getCol()]=3;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow()+1,currentPosition.getCol());
				}
			}
			else if(array[currentPosition.getRow()+2][currentPosition.getCol()]==4)
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					finishPoints.add(new point(currentPosition.getRow()+2,currentPosition.getCol()));
					boxPoints.add(new point(currentPosition.getRow()+2,currentPosition.getCol()));
					/*for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+2))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()+2][currentPosition.getCol()]=6;
						array[currentPosition.getRow()+1][currentPosition.getCol()]=3;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow()+1,currentPosition.getCol());
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					finishPoints.add(new point(currentPosition.getRow()+2,currentPosition.getCol()));
					boxPoints.add(new point(currentPosition.getRow()+2,currentPosition.getCol()));
					/*for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+2))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()+2][currentPosition.getCol()]=6;
						array[currentPosition.getRow()+1][currentPosition.getCol()]=3;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow()+1,currentPosition.getCol());
				}				
			}
		}
		else if(array[currentPosition.getRow()+1][currentPosition.getCol()]==6)
		{
			if(array[currentPosition.getRow()+2][currentPosition.getCol()]==2)//push to walls
			{
				System.out.println("Push to walls");
				return false;
			}
			else if(array[currentPosition.getRow()+2][currentPosition.getCol()]==5)//push to boxes
			{
				System.out.println("Push to another box");
				return false;
			}
			else if(array[currentPosition.getRow()+2][currentPosition.getCol()]==6)//push to storage boxes
			{
				System.out.println("Push to another box already on storage");
				return false;
			}
			else if(array[currentPosition.getRow()+2][currentPosition.getCol()]==1)//push to space
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					//storagePoints.add(new point(currentPosition.getRow()+1,currentPosition.getCol()));
					boxPoints.add(new point(currentPosition.getRow()+2,currentPosition.getCol()));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()+2][currentPosition.getCol()]=5;
						array[currentPosition.getRow()+1][currentPosition.getCol()]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow()+1,currentPosition.getCol());
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					//storagePoints.add(new point(currentPosition.getRow()+1,currentPosition.getCol()));
					boxPoints.add(new point(currentPosition.getRow()+2,currentPosition.getCol()));
					//box positions
					if (!readOnly){	
						array[currentPosition.getRow()+2][currentPosition.getCol()]=5;
						array[currentPosition.getRow()+1][currentPosition.getCol()]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow()+1,currentPosition.getCol());
				}
			}
			else if(array[currentPosition.getRow()+2][currentPosition.getCol()]==4)
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow()+2,currentPosition.getCol()));
					/*finishPoints.add(new point(currentPosition.getRow()+2,currentPosition.getCol()));
					storagePoints.add(new point(currentPosition.getRow()+1,currentPosition.getCol()));
					for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+2))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()+2][currentPosition.getCol()]=6;
						array[currentPosition.getRow()+1][currentPosition.getCol()]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow()+1,currentPosition.getCol());
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+1))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow()+2,currentPosition.getCol()));
					finishPoints.add(new point(currentPosition.getRow()+2,currentPosition.getCol()));
				/*	storagePoints.add(new point(currentPosition.getRow()+1,currentPosition.getCol()));
					for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()+2))&&(temp.getCol()==currentPosition.getCol()))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()+2][currentPosition.getCol()]=6;
						array[currentPosition.getRow()+1][currentPosition.getCol()]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow()+1,currentPosition.getCol());
				}				
			}
		}
		assert(storagePoints.size() == boxPoints.size());
		return true;
	}//end downMove
	
	public boolean leftMove(boolean readOnly)
	{
		//test
		/*
		System.out.print(currentPosition.getRow());
		System.out.print(",");
		System.out.println(currentPosition.getCol());
		*/
		//System.out.println(array[currentPosition.getRow()][currentPosition.getCol()]);
		//test
		if(array[currentPosition.getRow()][currentPosition.getCol()-1]==2)//next:walls
		{
			System.out.println("Run into the wall");
			return false;
		}
		else if(array[currentPosition.getRow()][currentPosition.getCol()-1]==1)//next:space
		{
			if(array[currentPosition.getRow()][currentPosition.getCol()]==3)//not stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()][currentPosition.getCol()-1]=3;
					array[currentPosition.getRow()][currentPosition.getCol()]=1;
				}
				currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()-1);
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)//stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()][currentPosition.getCol()-1]=3;
					array[currentPosition.getRow()][currentPosition.getCol()]=4;
				}
				currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()-1);
			}
		}
		else if(array[currentPosition.getRow()][currentPosition.getCol()-1]==4)//next:storage
		{
			if(array[currentPosition.getRow()][currentPosition.getCol()]==3)//not stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()][currentPosition.getCol()-1]=7;
					array[currentPosition.getRow()][currentPosition.getCol()]=1;
				}
				currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()-1);
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)//stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()][currentPosition.getCol()-1]=7;
					array[currentPosition.getRow()][currentPosition.getCol()]=4;
				}
				currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()-1);
			}
		}
		else if(array[currentPosition.getRow()][currentPosition.getCol()-1]==5)//complex case:next:box
		{
			if(array[currentPosition.getRow()][currentPosition.getCol()-2]==2)//push to walls
			{
				System.out.println("Push to walls");
				return false;
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()-2]==5)//push to boxes
			{
				System.out.println("Push to another box");
				return false;
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()-2]==6)//push to storage boxes
			{
				System.out.println("Push to another box already on storage");
				return false;
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()-2]==1)//push to space
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-1))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-2));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()-2]=5;
						array[currentPosition.getRow()][currentPosition.getCol()-1]=3;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()-1);
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-1))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-2));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()-2]=5;
						array[currentPosition.getRow()][currentPosition.getCol()-1]=3;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()-1);
				}
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()-2]==4)
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-1))
						{
							it.remove();
						}
					}
					finishPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-2));
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-2));
					/*for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-2))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()-2]=6;
						array[currentPosition.getRow()][currentPosition.getCol()-1]=3;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()-1);
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-1))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-2));
					finishPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-2));
					/*for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-2))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()-2]=6;
						array[currentPosition.getRow()][currentPosition.getCol()-1]=3;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()-1);
				}				
			}
		}
		else if(array[currentPosition.getRow()][currentPosition.getCol()-1]==6)
		{
			if(array[currentPosition.getRow()][currentPosition.getCol()-2]==2)//push to walls
			{
				System.out.println("Push to walls");
				return false;
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()-2]==5)//push to boxes
			{
				System.out.println("Push to another box");
				return false;
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()-2]==6)//push to storage boxes
			{
				System.out.println("Push to another box already on storage");
				return false;
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()-2]==1)//push to space
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-1))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-1))
						{
							it.remove();
						}
					}
				//	storagePoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-1));
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-2));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()-2]=5;
						array[currentPosition.getRow()][currentPosition.getCol()-1]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()-1);
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-1))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-1))
						{
							it.remove();
						}
					}
				//	storagePoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-1));
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-2));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()-2]=5;
						array[currentPosition.getRow()][currentPosition.getCol()-1]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()-1);
				}
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()-2]==4)
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-1))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-1))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-2));
					finishPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-2));
				/*	storagePoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-1));
					for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-2))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()-2]=6;
						array[currentPosition.getRow()][currentPosition.getCol()-1]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()-1);
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-1))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-1))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-2));
					finishPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-2));
					/*storagePoints.add(new point(currentPosition.getRow(),currentPosition.getCol()-1));
					for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()-2))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()-2]=6;
						array[currentPosition.getRow()][currentPosition.getCol()-1]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()-1);
				}				
			}
		}
		assert(storagePoints.size() == boxPoints.size());
		return true;
	}//end leftMove
	
	public boolean rightMove(boolean readOnly)
	{
		//test
		/*
		System.out.print(currentPosition.getRow());
		System.out.print(",");
		System.out.println(currentPosition.getCol());
		*/
		//System.out.println(array[currentPosition.getRow()][currentPosition.getCol()]);
		//test
		if(array[currentPosition.getRow()][currentPosition.getCol()+1]==2)//next:walls
		{
			System.out.println("Run into the wall");
			return false;
		}
		else if(array[currentPosition.getRow()][currentPosition.getCol()+1]==1)//next:space
		{
			if(array[currentPosition.getRow()][currentPosition.getCol()]==3)//not stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()][currentPosition.getCol()+1]=3;
					array[currentPosition.getRow()][currentPosition.getCol()]=1;
				}
				currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()+1);
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)//stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()][currentPosition.getCol()+1]=3;
					array[currentPosition.getRow()][currentPosition.getCol()]=4;
				}
				currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()+1);
			}
		}
		else if(array[currentPosition.getRow()][currentPosition.getCol()+1]==4)//next:storage
		{
			if(array[currentPosition.getRow()][currentPosition.getCol()]==3)//not stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()][currentPosition.getCol()+1]=7;
					array[currentPosition.getRow()][currentPosition.getCol()]=1;
				}
				currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()+1);
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)//stand on storage points
			{
				if (!readOnly){
					array[currentPosition.getRow()][currentPosition.getCol()+1]=7;
					array[currentPosition.getRow()][currentPosition.getCol()]=4;
				}
				currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()+1);
			}
		}
		else if(array[currentPosition.getRow()][currentPosition.getCol()+1]==5)//complex case:next:box
		{
			if(array[currentPosition.getRow()][currentPosition.getCol()+2]==2)//push to walls
			{
				System.out.println("Push to walls");
				return false;
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()+2]==5)//push to boxes
			{
				System.out.println("Push to another box");
				return false;
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()+2]==6)//push to storage boxes
			{
				System.out.println("Push to another box already on storage");
				return false;
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()+2]==1)//push to space
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					//System.out.println("spacetest\n");
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+1))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+2));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()+2]=5;
						array[currentPosition.getRow()][currentPosition.getCol()+1]=3;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()+1);
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+1))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+2));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()+2]=5;
						array[currentPosition.getRow()][currentPosition.getCol()+1]=3;
						
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()+1);
				}
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()+2]==4)
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+1))
						{
							it.remove();
						}
					}
					finishPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+2));
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+2));
					/*for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+2))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()+2]=6;
						array[currentPosition.getRow()][currentPosition.getCol()+1]=3;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()+1);
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+1))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+2));
					finishPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+2));
					/*for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+2))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()+2]=6;
						array[currentPosition.getRow()][currentPosition.getCol()+1]=3;
						
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()+1);
				}				
			}
		}
		else if(array[currentPosition.getRow()][currentPosition.getCol()+1]==6)
		{
			if(array[currentPosition.getRow()][currentPosition.getCol()+2]==2)//push to walls
			{
				System.out.println("Push to walls");
				return false;
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()+2]==5)//push to boxes
			{
				System.out.println("Push to another box");
				return false;
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()+2]==6)//push to storage boxes
			{
				System.out.println("Push to another box already on storage");
				return false;
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()+2]==1)//push to space
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+1))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+1))
						{
							it.remove();
						}
					}
				//	storagePoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+1));
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+2));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()+2]=5;
						array[currentPosition.getRow()][currentPosition.getCol()+1]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()+1);
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+1))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+1))
						{
							it.remove();
						}
					}
					//storagePoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+1));
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+2));
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()+2]=5;
						array[currentPosition.getRow()][currentPosition.getCol()+1]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()+1);
				}
			}
			else if(array[currentPosition.getRow()][currentPosition.getCol()+2]==4)
			{
				if(array[currentPosition.getRow()][currentPosition.getCol()]==3)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+1))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+1))
						{
							it.remove();
						}
					}
					finishPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+2));
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+2));
				//	storagePoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+1));
				/*	for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+2))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()+2]=6;
						array[currentPosition.getRow()][currentPosition.getCol()+1]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=1;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()+1);
				}
				else if(array[currentPosition.getRow()][currentPosition.getCol()]==7)
				{
					//box positions
					for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+1))
						{
							it.remove();
						}
					}
					for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+1))
						{
							it.remove();
						}
					}
					boxPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+2));
					finishPoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+2));
					//storagePoints.add(new point(currentPosition.getRow(),currentPosition.getCol()+1));
					/*for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
					{
						point temp=it.next();
						if((temp.getRow()==(currentPosition.getRow()))&&(temp.getCol()==currentPosition.getCol()+2))
						{
							it.remove();
						}
					}*/
					//box positions
					if (!readOnly){
						array[currentPosition.getRow()][currentPosition.getCol()+2]=6;
						array[currentPosition.getRow()][currentPosition.getCol()+1]=7;
					
						array[currentPosition.getRow()][currentPosition.getCol()]=4;
					}
					currentPosition.setPoint(currentPosition.getRow(),currentPosition.getCol()+1);
				}				
			}
		}
		assert (storagePoints.size()== boxPoints.size());
		return true;
	}//end rightMove
	
	
	public Set<point> getStoragePoints()
	{
		return storagePoints;
	}
	public void printStoragePositions()
	{
		int count=1;
		for(Iterator<point> it=storagePoints.iterator();it.hasNext();)
		{
			point storageTemp=it.next();
			System.out.print("Storage point:");
			System.out.print(count++);
			System.out.print(" Row:");
			System.out.print(storageTemp.getRow());
			System.out.print(" Col:");
			System.out.println(storageTemp.getCol());
		}
	}
	public Set<point> getBoxPoints()
	{
		return boxPoints;
	}
	public void printBoxPositions()
	{
		int count=1;
		for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
		{
			point boxTemp=it.next();
			System.out.print("Box point:");
			System.out.print(count++);
			System.out.print(" Row:");
			System.out.print(boxTemp.getRow());
			System.out.print(" Col:");
			System.out.println(boxTemp.getCol());
		}
	}
	public Set<point> getFinishPoints()
	{
		return finishPoints;
	}
	public void printFinishPositions()
	{
		int count=1;
		for(Iterator<point> it=finishPoints.iterator();it.hasNext();)
		{
			point finishTemp=it.next();
			System.out.print("Box point:");
			System.out.print(count++);
			System.out.print(" Row:");
			System.out.print(finishTemp.getRow());
			System.out.print(" Col:");
			System.out.println(finishTemp.getCol());
		}
	}
	public void up()
	{
		try
		{
			Thread.sleep(SLEEP_TIME);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		upMove(false);
		plotMaze=new plotGraph(array,rowNumber,colNumber);
	/*	printFinishPositions();

		System.out.print(currentPosition.getRow());
		System.out.print(",");
		System.out.println(currentPosition.getCol());*/
	}
	public void down()
	{
		try
		{
			Thread.sleep(SLEEP_TIME);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		downMove(false);
		plotMaze=new plotGraph(array,rowNumber,colNumber);
	/*	printFinishPositions();

		System.out.print(currentPosition.getRow());
		System.out.print(",");
		System.out.println(currentPosition.getCol());*/
	}
	public void left()
	{
		try
		{
			Thread.sleep(SLEEP_TIME);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		leftMove(false);
		plotMaze=new plotGraph(array,rowNumber,colNumber);
		/*printFinishPositions();

		System.out.print(currentPosition.getRow());
		System.out.print(",");
		System.out.println(currentPosition.getCol());*/
	}
	public void right()
	{
		try
		{
			Thread.sleep(SLEEP_TIME);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		rightMove(false);
		plotMaze=new plotGraph(array,rowNumber,colNumber);
		/*printFinishPositions();

		System.out.print(currentPosition.getRow());
		System.out.print(",");
		System.out.println(currentPosition.getCol());*/
	}
	public static Comparator<sokobanState> valueComparator = new Comparator<sokobanState>(){

        @Override
        public int compare(sokobanState c1, sokobanState c2) {
			return (int)(c1.getValue()-c2.getValue());
        }
    };
	private void print(Set<point> p_s)
	{
		for (point p: p_s) {
			p.print();
		}
	}
	public boolean deadField()//true for deadField
	{
		boolean result=false;
		for(Iterator<point> it=boxPoints.iterator();it.hasNext();)
		{
			point boxPoint=it.next();
			int currentRow=boxPoint.getRow();
			int currentCol=boxPoint.getCol();
			if((array[currentRow+1][currentCol]==2)&&(array[currentRow][currentCol+1]==2))
			{
				result=true;
			}
			if((array[currentRow-1][currentCol]==2)&&(array[currentRow][currentCol+1]==2))
			{
				result=true;
			}
			if((array[currentRow+1][currentCol]==2)&&(array[currentRow][currentCol-1]==2))
			{
				result=true;
			}
			if((array[currentRow-1][currentCol]==2)&&(array[currentRow][currentCol-1]==2))
			{
				result=true;
			}
		}
		return result;
	}
	private void endAnimate(sokobanState state, LinkedList<Integer>path)
	{
		recoverState(state);
		for (int p : path) {
			if (p == DIR_UP) up();
			if (p == DIR_DOWN) down();
			if (p == DIR_LEFT) left();
			if (p == DIR_RIGHT) right();
		}
	}
}
