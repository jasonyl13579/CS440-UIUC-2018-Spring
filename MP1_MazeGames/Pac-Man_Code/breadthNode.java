import java.util.Queue;
import java.util.LinkedList;

public class breadthNode
{
	private breadthNode parent;
	private Queue<breadthNode> children;
	private int row;
	private int col;
	public breadthNode()
	{
		children=new LinkedList();
	}
	public void setParent(breadthNode parent)
	{
		this.parent=parent;
	}
	public void addChild(breadthNode child)
	{
		children.add(child);
	}
	public breadthNode getParent()
	{
		return parent;
	}
	public Queue<breadthNode> getChildren()
	{
		return children;
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
}