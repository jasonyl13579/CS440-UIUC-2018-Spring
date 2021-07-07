import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class plotGraph extends JFrame
{
	private JPanel panel=null;
	private int[][] array;
	private int rowNumber;
	private int colNumber;
	
	public plotGraph(int[][] array,int rowNumber,int colNumber)
	{
		super("Sokoban");
		this.array=array;
		this.rowNumber=rowNumber;
		this.colNumber=colNumber;
		
		paintGrid();
		getContentPane().add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024,768);
		setLocationRelativeTo(null);
		setVisible(true);
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
				for(int i=0;i<colNumber;i++)
				{
					for(int j=0;j<rowNumber;j++)
					{
						if(array[j][i]==1)
						{
							g.setColor(Color.WHITE);
							g.fillRect(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
						else if(array[j][i]==2)
						{
							g.setColor(Color.BLACK);
							g.fillRect(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
						else if(array[j][i]==3)
						{
							g.setColor(Color.GREEN);
							g.fillOval(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
						else if(array[j][i]==4)
						{
							g.setColor(Color.YELLOW);
							g.fillOval(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
						else if(array[j][i]==5)
						{
							g.setColor(Color.MAGENTA);
							g.fillOval(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
						else if(array[j][i]==6)
						{
							g.setColor(Color.RED);
							g.fillOval(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
						else if(array[j][i]==7)
						{
							g.setColor(Color.CYAN);
							g.fillOval(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
						/*
						else if(array[j][i]==99)
						{
							g.setColor(Color.BLUE);
							g.fillRect(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
						else if(array[j][i]==100)
						{
							g.setColor(Color.RED);
							g.fillRect(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
						*/
					}
				}
			}
		};
	}
	public void deletePanel()
	{
		panel.setVisible(false);
		this.dispose();
	}
}