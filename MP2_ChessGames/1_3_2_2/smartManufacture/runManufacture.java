public class runManufacture
{
	public static void main(String[] args)
	{
		factoryReader factoryData=new factoryReader();
		factoryData.readMaze();
		int[][] factoryArray=factoryData.getFactoryArray();
		int rowNumber=factoryData.getRowNumber();
		int colNumber=factoryData.getColNumber();
		
		plotGraph plotFactory=new plotGraph(factoryArray,rowNumber,colNumber);
		
		smartManufacture smartFactory=new smartManufacture(factoryArray,rowNumber,colNumber);
		smartFactory.smartManufactureAlgorithm();
	}
}