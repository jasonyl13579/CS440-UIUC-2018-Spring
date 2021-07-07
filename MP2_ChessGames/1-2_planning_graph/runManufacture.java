public class runManufacture
{
	public static void main(String[] args)
	{
        int version = 3;
		factoryReader factoryData=new factoryReader();
		factoryData.readMaze();
		int[][] factoryArray=factoryData.getFactoryArray();
		int rowNumber=factoryData.getRowNumber();
		int colNumber=factoryData.getColNumber();
		
//		plotGraph plotFactory=new plotGraph(factoryArray,rowNumber,colNumber);
		
        switch(version){
            case 1 :
                smartManufacture smartFactory=new smartManufacture(factoryArray,rowNumber,colNumber);
                smartFactory.smartManufactureAlgorithm();
                break;
            case 2 :
                smartManufacture2 smartFactory2=new smartManufacture2(factoryArray,rowNumber,colNumber);
                smartFactory2.smartManufactureAlgorithm();
                break;
            case 3 : 
            		planningGraph smartFactory3 = new planningGraph(factoryArray,rowNumber,colNumber);
            		smartFactory3.graphPlan();
            		break;
        }
	}
}
