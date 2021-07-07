import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class readWeight
{
	public static double[] weights;
	public readWeight()
	{
		this.weights=new double[147];
		for(int i=0;i<147;i++)
		{
			weights[i]=0;
		}
		try
		{
			String line;
			BufferedReader weightReader=new BufferedReader(new FileReader("weight.txt"));
			int i=0;
			while((line=weightReader.readLine())!=null)
            {
				//System.out.println(line);
				//System.out.println(Double.parseDouble(line));
				weights[i]=Double.parseDouble(line);
				i=i+1;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}