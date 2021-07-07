import java.util.Set;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class singlePixel
{
	private int rowNumber=32;
	private int colNumber=32;
	private String trainingFilePath;
	private String testingFilePath;
	private String digits="";
	private String testDigits="";
	private ArrayList<String> dataset;
	private ArrayList<String> testset;
	private ArrayList<Integer> trainLabel;
	private ArrayList<Integer> testLabel;
	private ArrayList<Integer> trainNumber;
	private ArrayList<Integer> testNumber;

	private double[][] weights;
	private double[][] confusionMatrix;
	private double[] number;
	private double[] accuracy;
	private double[] outputs;
	private double testAccuracy;
	private Random rand;

	//tuning parameters
	private int biasNumber = 0;
	private double inputBias = 1;
	private double weightBias = 1;
	private boolean fixedLR = false;
	private double learningRate=0.01;
	private boolean randomWeights = false;
	private boolean randomInputOrder = true;
	private int epochs = 30;
	public singlePixel(String trainingFilePath,String testingFilePath)
	{
		this.trainingFilePath=trainingFilePath;
		this.testingFilePath=testingFilePath;
		this.dataset = new ArrayList();
		this.testset = new ArrayList();
		this.trainLabel = new ArrayList();
		this.testLabel = new ArrayList();
		this.trainNumber = new ArrayList();
		this.testNumber = new ArrayList();
		this.rand = new Random();

		weights=new double[10][rowNumber*colNumber + biasNumber];
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<rowNumber*colNumber;j++)
			{
				if(randomWeights)
					weights[i][j] = rand.nextDouble() * 0.1;
				else
					weights[i][j]=0;
			}
			if(biasNumber == 1)
				weights[i][rowNumber*colNumber] = weightBias;
		}
		confusionMatrix=new double[10][10];
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				confusionMatrix[i][j]=0;
			}
		}

		for(int i = 0; i < 10; i++) {
			trainNumber.add(0);
			testNumber.add(0);
		}
		outputs=new double[10];
		for(int i=0;i<10;i++)
		{
			outputs[i]=0;
		}
		number=new double[10];
		for(int i=0;i<10;i++)
		{
			number[i]=0;
		}
		accuracy=new double[10];
		for(int i=0;i<10;i++)
		{
			accuracy[i]=0;
		}
	}
	public void singlePixelAlgorithm()
	{
		int index=0;		
		int countEpoch=0;
		int randomInt;
		double[] outcome;
		String strTemp;
		outcome=new double[10];
		ArrayList<Float> accuracyArray = new ArrayList<Float>();

		for(int i=0;i<10;i++)
		{
			outcome[i]=0;
		}

		double[] errorTemp;
		errorTemp=new double[2436];
		for(int i=0;i<2436;i++)
		{
			errorTemp[i]=0;
		}

		for(int i = 0; i < epochs; i++)
			accuracyArray.add((float) 0);

		//read the file
		try
		{
			BufferedReader trainingReader=new BufferedReader(new FileReader(trainingFilePath));
			String line;

			while((line=trainingReader.readLine())!=null)
			{
				if(line.charAt(0)==' ')
				{
					index=line.charAt(1)-'0';
					if(biasNumber == 1)
						digits += Integer.toString((int) inputBias);
					dataset.add(digits);
					trainLabel.add(index);
					trainNumber.set(index, trainNumber.get(index)+1);
					digits="";
				}
				else
				{
					digits=digits+line;
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		while(countEpoch < epochs)
		{
			//fixed or function learning rate
			if(!fixedLR && countEpoch!=0)
				learningRate = learningRate*(countEpoch)/(countEpoch+1);

			//fixed or random order
			if(randomInputOrder)
				for(int i = 0; i < dataset.size(); i++) {
					randomInt = rand.nextInt(dataset.size());
					Collections.swap(dataset, randomInt, i);
					Collections.swap(trainLabel, randomInt, i);
				}

			float trainAccuracy = 0;
			int maxNum = 0;
			double maxValue = 0;
			for(int i = 0; i < dataset.size(); i++) {

				maxNum = 0;
				maxValue = 0;
				//calculate probability for 0~9
				for(int num = 0; num < 10; num++) {
					outcome[num] = 0;
					for(int k = 0; k < rowNumber*colNumber + biasNumber; k++) 
						outcome[num] += weights[num][k] * (float)(dataset.get(i).charAt(k)-'0');
					outcome[num] = (float)1/(1+Math.exp(-outcome[num]));

					//find maximum probability
					if(outcome[num] > maxValue) {
						maxNum = num;
						maxValue = outcome[num];
					}
					else if(outcome[num] == maxValue) {
						if(rand.nextInt(2) > 0.5) {
							maxNum = num;
							maxValue = outcome[num];
						}	
					}
				}

				//misclassify c' as c
				if(maxNum != trainLabel.get(i)) {
					for(int k = 0; k < rowNumber*colNumber; k++) {
						//update c' weight
						weights[maxNum][k] -= learningRate * (float)(dataset.get(i).charAt(k)-'0');

						//update c weight
						weights[trainLabel.get(i)][k] += learningRate * (float)(dataset.get(i).charAt(k)-'0');
					}
				}
				else
					trainAccuracy++;

			}
			trainAccuracy /= 2436;
			accuracyArray.set(countEpoch, trainAccuracy);
			System.out.println(countEpoch + " : " + trainAccuracy);
			countEpoch+=1;
		}

		//*************************
		//test set read
		System.out.println();
		index=0;
		
//		double max = 0, min = 0; 
//		int number = 1;
//		for(int i = 0; i < 1024;i++) {
//			if(weights[number][i] > max)
//				max = weights[number][i];
//			if(weights[number][i] < min)
//				min = weights[number][i];
//		}
//
//		System.out.println("Maximum weight : " + max);
//		System.out.println("Minimum weight : " + min);

		try
		{
			BufferedReader testReader=new BufferedReader(new FileReader(testingFilePath));
			String line;

			while((line=testReader.readLine())!=null)
			{
				if(line.charAt(0)==' ')
				{
					index=line.charAt(1)-'0';
					testLabel.add(index);
					testset.add(testDigits);
					testNumber.set(index, testNumber.get(index)+1);
					testDigits="";
				}
				else
				{
					testDigits=testDigits+line;
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		testAccuracy = 0;
		int maxNum = 0;
		double maxValue = 0;
		for(int i = 0; i < testset.size(); i++) {
			maxNum = 0;
			maxValue = 0;
			//calculate probability for 0~9
			for(int num = 0; num < 10; num++) {
				outcome[num] = 0;
				for(int k = 0; k < rowNumber*colNumber; k++) 
					outcome[num] += weights[num][k] * (float)(testset.get(i).charAt(k)-'0');
				if(biasNumber == 1)
					outcome[num] += weights[num][rowNumber*colNumber] * inputBias;
				outcome[num] = (float)1/(1+Math.exp(-outcome[num]));

				//find maximum probability
				if(outcome[num] > maxValue) {
					maxNum = num;
					maxValue = outcome[num];
				}
				else if(outcome[num] == maxValue) {
					if(rand.nextInt(2) > 0.5) {
						maxNum = num;
						maxValue = outcome[num];
					}	
				}
			}

			if(maxNum == testLabel.get(i)) 
				testAccuracy++;
			confusionMatrix[testLabel.get(i)][maxNum] ++;
		}
		testAccuracy /= 444;

		//Print results
		System.out.println("Testing Accuracy : " + testAccuracy);
		System.out.println("-------------Times------------");
		System.out.println("\t0\t1\t2\t3\t4\t5\t6\t7\t8\t9");
		for(int i=0;i<10;i++)
		{
			System.out.print(i+"\t");
			for(int j=0;j<10;j++)
			{
				System.out.print(confusionMatrix[i][j]+"\t");
			}
			System.out.println();
		}

		System.out.println("----------Probability---------");
		System.out.println("\t0\t1\t2\t3\t4\t5\t6\t7\t8\t9");
		for(int i=0;i<10;i++)
		{
			System.out.print(i+"\t");
			for(int j=0;j<10;j++)
			{
				System.out.printf("%.3f\t",confusionMatrix[i][j]/(float)testNumber.get(i));
			}
			System.out.println();
		}

		//write training accuracy to the file
		String result = "";
		for(int i = 0 ; i < accuracyArray.size(); i++) {
			result += Float.toString(accuracyArray.get(i));
			result += "\n";
		}
		try
		{
			BufferedWriter trainingWriter=new BufferedWriter(new FileWriter("TrainingAccuracy.txt"));
			trainingWriter.write(result);
			trainingWriter.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		//plot the graph of each digits
		double[] weightsTemp = new double[rowNumber*colNumber];
		for(int i = 0 ; i < 10; i++) {
			for(int j = 0; j < rowNumber*colNumber; j++)
				weightsTemp[j] = weights[i][j];
			plotGraph plot = new plotGraph(weightsTemp, rowNumber, colNumber, i);
		}
	}

	public void clearRecord()
	{
		this.trainingFilePath=trainingFilePath;
		this.testingFilePath=testingFilePath;
		this.dataset = new ArrayList();
		this.testset = new ArrayList();
		this.trainLabel = new ArrayList();
		this.testLabel = new ArrayList();
		this.trainNumber = new ArrayList();
		this.testNumber = new ArrayList();
		this.rand = new Random();

		weights=new double[10][rowNumber*colNumber + biasNumber];
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<rowNumber*colNumber;j++)
			{
				if(randomWeights)
					weights[i][j] = rand.nextDouble() * 0.1;
				else
					weights[i][j]=0;
			}
			if(biasNumber == 1)
				weights[i][rowNumber*colNumber] = weightBias;
		}
		confusionMatrix=new double[10][10];
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				confusionMatrix[i][j]=0;
			}
		}

		for(int i = 0; i < 10; i++) {
			trainNumber.add(0);
			testNumber.add(0);
		}
		outputs=new double[10];
		for(int i=0;i<10;i++)
		{
			outputs[i]=0;
		}
		number=new double[10];
		for(int i=0;i<10;i++)
		{
			number[i]=0;
		}
		accuracy=new double[10];
		for(int i=0;i<10;i++)
		{
			accuracy[i]=0;
		}
	}
	
	public void singlePixelAlgorithmNtimes(int n)
	{
		ArrayList<Double> accuracyArray = new ArrayList();
		for(int i = 0; i < n; i++) {
			clearRecord();
			singlePixelAlgorithm();
			accuracyArray.add(testAccuracy);
		}

		//write training accuracy to the file
		String result = "";
		for(int i = 0 ; i < accuracyArray.size(); i++) {
			result += Double.toString(accuracyArray.get(i));
			result += "\n";
		}
		try
		{
			BufferedWriter trainingWriter=new BufferedWriter(new FileWriter("TestingAccuracy.txt"));
			trainingWriter.write(result);
			trainingWriter.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
