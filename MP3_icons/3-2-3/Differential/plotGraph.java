import java.awt.Graphics;
import java.awt.Color;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;


public class plotGraph extends JFrame
{
	private JPanel panel = null;
	private double[] array;
	private int rowNumber;
	private int colNumber;
	private int digit;

	public plotGraph(double[] array, int rowNumber, int colNumber, int digit)
	{
		this.array = array;
		this.rowNumber = rowNumber;
		this.colNumber= colNumber;
		this.digit = digit;
		
		paintGrid();
		getContentPane().add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024,1024);
		setLocationRelativeTo(null);
		setVisible(true);
		try
		{
			Thread.sleep(100);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void paintGrid()
	{
		panel=new JPanel()
		{
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				int gridWidth=getWidth()/colNumber;
				int gridHeight=getHeight()/rowNumber;
				for(int i=0;i<rowNumber;i++)
				{
					for(int j=0;j<colNumber;j++)
					{
						if(array[i*colNumber + j] == 0)
						{
							g.setColor(Color.WHITE);
							g.fillOval(j*gridWidth,i*gridHeight,gridWidth,gridHeight);
						}
						else if(array[i*colNumber + j] > 0 && array[i*colNumber + j] <= 0.1)
						{
							g.setColor(Color.ORANGE);
							g.fillOval(j*gridWidth,i*gridHeight,gridWidth,gridHeight);
						}
						else if(array[i*colNumber + j] > 0.1)
						{
							g.setColor(Color.RED);
							g.fillOval(j*gridWidth,i*gridHeight,gridWidth,gridHeight);
						}
						else if(array[i*colNumber + j] < 0 && array[i*colNumber + j] >= -0.1)
						{
							g.setColor(Color.BLUE);
							g.fillOval(j*gridWidth,i*gridHeight,gridWidth,gridHeight);
						}
						else if(array[i*colNumber + j] < -0.1)
						{
							g.setColor(Color.MAGENTA);
							g.fillOval(j*gridWidth,i*gridHeight,gridWidth,gridHeight);
						}
					}
				}
			}
		};
		String title = "Digit : " + Integer.toString(digit);
		Border border = BorderFactory.createTitledBorder(title);
		panel.setBorder(border);
	}

	public void deletePanel()
	{
		panel.setVisible(false);
		this.dispose();
	}
}
