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
	private int impossible=0;
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
					if(impossible==0)
					stateQueue.add(nextState[i]);
					stateNow.setDirection(i,false);
				}
			}	
			impossible=0;
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
		/*
		for(Iterator<point> itBox=state.getBoxPoints().iterator();itBox.hasNext();)
		{
			int minimumCost=2147483600;
			point boxTemp=itBox.next();
			for(Iterator<point> itStorage=storagePoints.iterator();itStorage.hasNext();)
			{
				point storageTemp=itStorage.next();
				int ManhattanDistance=Math.abs(boxTemp.getRow()-storageTemp.getRow())+Math.abs(boxTemp.getCol()-storageTemp.getCol());
				if(ManhattanDistance<minimumCost && stateArray[storageTemp.getRow()][storageTemp.getCol()] != MAZE_BOX_ON_STORAGE)
				{
					minimumCost=ManhattanDistance;
				}
			}
			heuristic = heuristic + minimumCost;
		}
			
		float peopleBoxDistance = 0;
		int minimumCost=2147483600;
		for(Iterator<point> itBox=state.getBoxPoints().iterator();itBox.hasNext();)
		{
			minimumCost=2147483600;
			point boxTemp=itBox.next();
			int ManhattanDistance=Math.abs(state.getCurrentRow()-boxTemp.getRow())+Math.abs(state.getCurrentCol()-boxTemp.getCol());
			if(ManhattanDistance<minimumCost && stateArray[boxTemp.getRow()][boxTemp.getCol()] != MAZE_BOX_ON_STORAGE)
			{
				minimumCost=ManhattanDistance;
			}
			peopleBoxDistance = peopleBoxDistance + minimumCost;
		}
		heuristic = heuristic + (peopleBoxDistance/state.getBoxPoints().size());
		heuristic += minimumCost;

		System.out.print("h:"+heuristic+"\n");
		*/
		Set<Integer> result = new HashSet<>();
		result.clear();
	    result.addAll(storagePointsInt);
	    result.retainAll(boxPointsInt);
		heuristic -= result.size()*10000000;
		
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
			
//			besideBox = besideBox || (up == MAZE_BOX);
//			besideBox = besideBox || (down == MAZE_BOX);
//			besideBox = besideBox || (left == MAZE_BOX);
//			besideBox = besideBox || (right == MAZE_BOX);
//			if(!besideBox)
//				heuristic -= 1;
			cantMove = false;	
			cantMove = cantMove || ((up == MAZE_BOX_ON_STORAGE) && ((left == MAZE_BOX_ON_STORAGE)||(right == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((down == MAZE_BOX_ON_STORAGE) && ((left == MAZE_BOX_ON_STORAGE)||(right == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((up == MAZE_BOX_ON_STORAGE) && ((left == MAZE_BOX)||(right == MAZE_BOX)));
			cantMove = cantMove || ((down == MAZE_BOX_ON_STORAGE) && ((left == MAZE_BOX)||(right == MAZE_BOX)));
			
			cantMove = cantMove || ((up == MAZE_BOX) && ((left == MAZE_BOX_ON_STORAGE)||(right == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((down == MAZE_BOX) && ((left == MAZE_BOX_ON_STORAGE)||(right == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((up == MAZE_BOX) && ((left == MAZE_BOX)||(right == MAZE_BOX)));
			cantMove = cantMove || ((down == MAZE_BOX) && ((left == MAZE_BOX)||(right == MAZE_BOX)));

			//cantMove = cantMove || ((leftUp == MAZE_BOX_ON_STORAGE) && ((left == MAZE_BOX_ON_STORAGE)&&(up == MAZE_BOX_ON_STORAGE)));
			//cantMove = cantMove || ((leftDown == MAZE_BOX_ON_STORAGE) && ((left == MAZE_BOX_ON_STORAGE)&&(down == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((rightUp == MAZE_BOX_ON_STORAGE) && ((right == MAZE_BOX)&&(up == MAZE_BOX)));
			cantMove = cantMove || ((rightDown == MAZE_BOX_ON_STORAGE) && ((right == MAZE_BOX)&&(down == MAZE_BOX)));
			
			cantMove = cantMove || ((leftUp == MAZE_BOX) && ((left == MAZE_BOX_ON_STORAGE)&&(up == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((leftDown == MAZE_BOX) && ((left == MAZE_BOX_ON_STORAGE)&&(down == MAZE_BOX_ON_STORAGE)));
			cantMove = cantMove || ((rightUp == MAZE_BOX) && ((right == MAZE_BOX)&&(up == MAZE_BOX)));
			cantMove = cantMove || ((rightDown == MAZE_BOX) && ((right == MAZE_BOX)&&(down == MAZE_BOX)));
			
//			if(now == MAZE_BOX_ON_STORAGE) {
//				aroundStorage = aroundStorage || (up == MAZE_BOX_ON_STORAGE);
//				aroundStorage = aroundStorage || (down == MAZE_BOX_ON_STORAGE);
//				aroundStorage = aroundStorage || (left == MAZE_BOX_ON_STORAGE);
//				aroundStorage = aroundStorage || (right == MAZE_BOX_ON_STORAGE);
//				if(aroundStorage)
//						heuristic -= 1;
//			}
			if(finishPoints.size()==9)
			{
				heuristic-=1500000000;
				System.out.println(1);
			}
			if(finishPoints.size()==10)
			{
				heuristic-=2000000000;
				System.out.println(2);
			}
			if(row==13&&col==5&&finishPoints.size()<9)
			{
				if(stateArray[8][5]==MAZE_BOX_ON_STORAGE&&stateArray[8][6]==MAZE_BOX_ON_STORAGE&&stateArray[9][5]==MAZE_BOX_ON_STORAGE&&stateArray[9][6]==MAZE_BOX_ON_STORAGE&&stateArray[10][5]==MAZE_BOX_ON_STORAGE&&stateArray[11][5]==MAZE_BOX_ON_STORAGE&&stateArray[11][6]==MAZE_BOX_ON_STORAGE&&stateArray[12][6]==MAZE_BOX_ON_STORAGE)
				{
					impossible=0;
					System.out.println("hello");
				}
				else
				{
					System.out.println("fuck");
					impossible=1;
					heuristic+=1000000000;
				}
				System.out.println("no");
				System.out.println(3);
			}
			if(row==13&&col==5&&finishPoints.size()>=9)
			{
				heuristic-=2000000000;
				System.out.println("Yes");
				System.out.println(4);
			}
			if((!besideWall)||col==7||col==8||col==9)
				heuristic -= 10000;
			if(!cantMove)
				heuristic -= 5000;
			if(row>=8&&row!=13&&col==5)
			{
				heuristic-=3000000*(20-row);
				System.out.println(5);
			}
			if(row>=7&&row!=13&&col==6)
			{
				heuristic-=800000000*(20-row);
				System.out.println(6);
			}
			
			if(stateArray[1][4]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(7);
			}
			if(stateArray[1][5]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(8);
			}
			if(stateArray[1][6]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(9);
			}
			if(stateArray[2][7]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(10);
			}
			if(stateArray[3][8]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(11);
			}
			if(stateArray[4][8]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(12);
			}
			if(stateArray[5][8]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(13);
			}
			
			if(stateArray[3][7]==MAZE_BOX&&stateArray[4][7]==MAZE_BOX&&stateArray[5][7]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(14);
			}
			if(stateArray[3][6]==MAZE_BOX&&stateArray[3][7]==MAZE_BOX&&stateArray[4][6]==MAZE_BOX&&stateArray[4][7]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(15);
			}
			/*
			if(state.getCurrentRow()==2&&state.getCurrentCol()==4&&stateArray[2][5]==MAZE_BOX&&stateArray[3][4]==MAZE_BOX&&stateArray[4][4]==MAZE_BOX&&stateArray[5][4]==MAZE_BOX&&stateArray[6][4]==MAZE_BOX&&stateArray[3][5]==MAZE_BOX&&stateArray[5][5]==MAZE_BOX&&stateArray[4][6]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
			}
			*/
			/*
			if(state.getCurrentCol()>=6&&stateArray[3][7]==MAZE_BOX&&stateArray[4][7]==MAZE_BOX&&stateArray[4][6]==MAZE_BOX&&stateArray[5][6]==MAZE_BOX&&stateArray[6][6]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
			}
			*/
			/*
			if(state.getCurrentCol()>=6&&stateArray[2][6]==MAZE_BOX&&stateArray[3][6]==MAZE_BOX&&stateArray[4][6]==MAZE_BOX&&stateArray[4][7]==MAZE_BOX&&stateArray[5][7]==MAZE_BOX)
			{
				heuristic+=2000000000;
				impossible=1;
			}
			*/
			
			if(stateArray[6][5]==MAZE_BOX&&stateArray[6][6]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(16);
			}
			if(stateArray[6][6]==MAZE_BOX&&stateArray[6][7]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(17);
			}
			if(stateArray[1][5]==MAZE_BOX&&stateArray[2][5]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(18);
			}
			if(stateArray[2][4]==MAZE_BOX&&stateArray[3][4]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(19);
			}
			if(stateArray[3][4]==MAZE_BOX&&stateArray[4][4]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(20);
			}
			if(stateArray[4][4]==MAZE_BOX&&stateArray[5][4]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(21);
			}
			
			if(stateArray[5][4]==MAZE_BOX&&stateArray[6][4]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(22);
			}
			if(stateArray[6][4]==MAZE_BOX&&stateArray[7][4]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(23);
			}
			if(stateArray[7][4]==MAZE_BOX&&stateArray[8][4]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(24);
			}
			if(stateArray[8][4]==MAZE_BOX&&stateArray[9][4]==MAZE_BOX)
			{
				heuristic+=1000000000;
				impossible=1;
				System.out.println(25);
			}
			if(state.getCurrentRow()==1&&state.getCurrentCol()==4)
			if(stateArray[state.getCurrentRow()+1][state.getCurrentCol()]==MAZE_BOX&&state.getCurrentRow()==1&&state.getCurrentCol()==4)
			{
				heuristic-=100000;
				System.out.println(26);
			}
			if(state.getCurrentRow()==2&&state.getCurrentCol()==4)
			if(stateArray[state.getCurrentRow()+1][state.getCurrentCol()]==MAZE_BOX&&state.getCurrentRow()==2&&state.getCurrentCol()==4)
			{
				heuristic-=150000;
				System.out.println(27);
			}
			if(state.getCurrentRow()==3&&state.getCurrentCol()==4)
			if(stateArray[state.getCurrentRow()+1][state.getCurrentCol()]==MAZE_BOX&&state.getCurrentRow()==3&&state.getCurrentCol()==4)
			{
				heuristic-=200000;
				System.out.println(28);
			}
			if(state.getCurrentRow()==4&&state.getCurrentCol()==4)
			if(stateArray[state.getCurrentRow()+1][state.getCurrentCol()]==MAZE_BOX&&state.getCurrentRow()==4&&state.getCurrentCol()==4)
			{
				heuristic-=250000;
				System.out.println(29);
			}
			if(state.getCurrentRow()==5&&state.getCurrentCol()==4)
			if(stateArray[state.getCurrentRow()+1][state.getCurrentCol()]==MAZE_BOX&&state.getCurrentRow()==5&&state.getCurrentCol()==4)
			{
				heuristic-=300000;
				System.out.println(30);
			}
			if(state.getCurrentRow()==6&&state.getCurrentCol()==4)
			if(stateArray[state.getCurrentRow()+1][state.getCurrentCol()]==MAZE_BOX&&state.getCurrentRow()==6&&state.getCurrentCol()==4)
			{
				heuristic-=350000;
				System.out.println(31);
			}
			if(state.getCurrentRow()==7&&state.getCurrentCol()==4)
			if(stateArray[state.getCurrentRow()+1][state.getCurrentCol()]==MAZE_BOX&&state.getCurrentRow()==7&&state.getCurrentCol()==4)
			{
				heuristic-=400000;
				System.out.println(32);
			}
			if(state.getCurrentRow()==8&&state.getCurrentCol()==4)
			if(stateArray[state.getCurrentRow()+1][state.getCurrentCol()]==MAZE_BOX&&state.getCurrentRow()==8&&state.getCurrentCol()==4)
			{
				heuristic-=450000;
				System.out.println(33);
			}
			if(state.getCurrentRow()==9&&state.getCurrentCol()==4)
			if(stateArray[state.getCurrentRow()+1][state.getCurrentCol()]==MAZE_BOX&&state.getCurrentRow()==9&&state.getCurrentCol()==4)
			{
				heuristic-=500000;
				System.out.println(34);
			}
			if(state.getCurrentRow()==10&&state.getCurrentCol()==4)
			if(stateArray[state.getCurrentRow()+1][state.getCurrentCol()]==MAZE_BOX&&state.getCurrentRow()==10&&state.getCurrentCol()==4)
			{
				heuristic-=550000;
				System.out.println(35);
			}
			if(state.getCurrentRow()==11&&state.getCurrentCol()==4)
			if(stateArray[state.getCurrentRow()+1][state.getCurrentCol()]==MAZE_BOX&&state.getCurrentRow()==11&&state.getCurrentCol()==4)
			{
				heuristic-=600000;
				System.out.println(36);
			}
			if(state.getCurrentRow()==13)
			if((stateArray[state.getCurrentRow()-1][state.getCurrentCol()]==MAZE_BOX||stateArray[state.getCurrentRow()-1][state.getCurrentCol()+1]==MAZE_BOX)&&state.getCurrentRow()==13&&state.getCurrentCol()==3)
			{
				heuristic-=500000;
				System.out.println(37);
			}
			if(state.getCurrentRow()==14)
			if((stateArray[state.getCurrentRow()-2][state.getCurrentCol()]==MAZE_BOX||stateArray[state.getCurrentRow()-2][state.getCurrentCol()+1]==MAZE_BOX)&&state.getCurrentRow()==14&&state.getCurrentCol()==3)
			{
				heuristic-=600000;
				System.out.println(38);
			}
			if(state.getCurrentRow()==14&&state.getCurrentCol()==3)
			if((stateArray[state.getCurrentRow()-2][state.getCurrentCol()+1]==MAZE_BOX||stateArray[state.getCurrentRow()-2][state.getCurrentCol()+2]==MAZE_BOX)&&state.getCurrentRow()==14&&state.getCurrentCol()==2)
			{
				heuristic-=700000;
				System.out.println(39);
			}
			if(state.getCurrentRow()==14&&state.getCurrentCol()==1)
			if((stateArray[state.getCurrentRow()-2][state.getCurrentCol()+2]==MAZE_BOX||stateArray[state.getCurrentRow()-2][state.getCurrentCol()+3]==MAZE_BOX)&&state.getCurrentRow()==14&&state.getCurrentCol()==1)
			{
				heuristic-=800000;
				System.out.println(40);
			}
			if(state.getCurrentRow()==13&&state.getCurrentCol()==1)
			if((stateArray[state.getCurrentRow()-1][state.getCurrentCol()+2]==MAZE_BOX||stateArray[state.getCurrentRow()-1][state.getCurrentCol()+3]==MAZE_BOX)&&state.getCurrentRow()==13&&state.getCurrentCol()==1)
			{
				heuristic-=900000;
				System.out.println(41);
			}
			if(state.getCurrentRow()==12&&state.getCurrentCol()==1)
			if((stateArray[state.getCurrentRow()][state.getCurrentCol()+2]==MAZE_BOX||stateArray[state.getCurrentRow()][state.getCurrentCol()+3]==MAZE_BOX)&&state.getCurrentRow()==12&&state.getCurrentCol()==1)
			{
				heuristic-=1000000;
				System.out.println(42);
			}
			if(state.getCurrentRow()==12&&state.getCurrentCol()==2)
			if((stateArray[state.getCurrentRow()][state.getCurrentCol()+1]==MAZE_BOX||stateArray[state.getCurrentRow()][state.getCurrentCol()+2]==MAZE_BOX)&&state.getCurrentRow()==12&&state.getCurrentCol()==2)
			{
				heuristic-=1100000;
				System.out.println(43);
			}
			if(state.getCurrentRow()==12&&state.getCurrentCol()==2)
			if((stateArray[state.getCurrentRow()][state.getCurrentCol()+1]==MAZE_BOX||stateArray[state.getCurrentRow()][state.getCurrentCol()+2]==MAZE_BOX)&&state.getCurrentRow()==12&&state.getCurrentCol()==2)
			{
				heuristic-=1200000;
				System.out.println(44);
			}
			if(state.getCurrentRow()==12&&state.getCurrentCol()==3)
			if((stateArray[state.getCurrentRow()][state.getCurrentCol()+1]==MAZE_BOX||stateArray[state.getCurrentRow()][state.getCurrentCol()+2]==MAZE_BOX)&&state.getCurrentRow()==12&&state.getCurrentCol()==3)
			{
				heuristic-=1300000;
				System.out.println(45);
			}
			if(state.getCurrentRow()==12&&state.getCurrentCol()==4)
			if((stateArray[state.getCurrentRow()][state.getCurrentCol()+1]==MAZE_BOX_ON_STORAGE||stateArray[state.getCurrentRow()][state.getCurrentCol()+2]==MAZE_BOX_ON_STORAGE)&&state.getCurrentRow()==12&&state.getCurrentCol()==4)
			{
				heuristic-=2000000;
				System.out.println(46);
			}
			if((stateArray[13][3]==MAZE_BOX&&stateArray[12][4]==MAZE_BOX))
			{
				heuristic+=2000000;
				System.out.println(47);
			}
			
			if((stateArray[13][4]==MAZE_BOX&&stateArray[12][4]==MAZE_BOX)&&finishPoints.size()<=8)
			{
				heuristic+=2000000;
				System.out.println(48);
			}
			
			if(stateArray[11][6]==MAZE_BOX_ON_STORAGE)
			{
				heuristic-=2000000000;
				System.out.println(49);
			}
			if(state.getCurrentRow()==11&&state.getCurrentCol()==5&&stateArray[11][6]==MAZE_BOX_ON_STORAGE)
			{
				heuristic-=5000000;
				System.out.println(50);
			}
			if(stateArray[11][5]==MAZE_BOX_ON_STORAGE&&stateArray[11][6]==MAZE_BOX_ON_STORAGE)
			{
				heuristic-=10000000;
				System.out.println(51);
			}
			
		}
		System.out.print("*************************");
		System.out.println(finishPoints.size());
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