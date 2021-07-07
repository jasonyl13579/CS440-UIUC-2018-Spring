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
	public int getInt(int col_size)
	{
		return this.row * col_size+this.col;
	}
	public void print()
	{
		System.out.print("Row:"+ row + ",Col:"+ col + ",int:" + this.getInt(6) +"\n");
	}
}