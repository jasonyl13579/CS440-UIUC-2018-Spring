import java.util.ArrayList;

public class aStarNode
{
	private aStarNode parent;
	private int row;
	private int col;
	private int travelDistance;
	private int targetDistance;
	private int totalDistance;
	private ArrayList<Integer> visitedFood;
	
	public aStarNode()
	{
		parent = null;
		row = -1;
		col = -1;
		travelDistance = 0;
		targetDistance = 0;
		totalDistance=0;
		visitedFood = new ArrayList<Integer>();
	}
	public void setParent(aStarNode parent)
	{
		this.parent=parent;
	}
	public aStarNode getParent()
	{
		return this.parent;
	}
	public void setRow(int row)
	{
		this.row=row;
	}
	public void setCol(int col)
	{
		this.col=col;
	}
	public int getRow()
	{
		return this.row;
	}
	public int getCol()
	{
		return this.col;
	}
	public void setTravelDistance(int travelDistance)
	{
		this.travelDistance = travelDistance;
	}
	public int getTravelDistance()
	{
		return this.travelDistance;
	}
	public void setTargetDistance(int targetDistance)
	{
		this.targetDistance=targetDistance;
	}
	public int getTargetDistance()
	{
		return this.targetDistance;
	}
	public void setTotalDistance()
	{
		this.totalDistance=this.travelDistance+this.targetDistance;
	}
	public void setTotalDistance(int totalDistance)
	{
		this.totalDistance=totalDistance;
	}
	public int getTotalDistance()
	{
		return this.totalDistance;
	}
	public void addFood(int food)
	{
		visitedFood.add(food);
	}
	public boolean isVisited(int foodIndex)
	{
		return this.visitedFood.contains(foodIndex);
	}
	public void copyFoods(aStarNode node)
	{
		this.visitedFood = (ArrayList<Integer>) node.visitedFood.clone();
	}
	public ArrayList getVisitedFoods()
	{
		return this.visitedFood;
	}
}