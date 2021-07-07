public class point
{
	private int row;
	private int col;
	
	public point(int row,int col)
	{
		this.row=row;
		this.col=col;
	}
	public void setPoint(int row,int col)
	{
		this.row=row;
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