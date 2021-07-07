public class runGomoku
{
	public static void main(String[] args)
	{
		boardReader board=new boardReader();
		int rowNumber=board.getRowNumber();
		int colNumber=board.getColNumber();
		int[][] array=board.getArray();
		
		readWeight reader=new readWeight();
		
		plotGraph plotBoard=new plotGraph(array,rowNumber,colNumber);
		
		Gomoku gomoku=new Gomoku(array,rowNumber,colNumber);
		gomoku.GomokuAlgorithm();
	}
}