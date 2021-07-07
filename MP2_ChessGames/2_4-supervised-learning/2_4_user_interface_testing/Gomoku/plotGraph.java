import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
public class plotGraph extends JFrame
{
	public static int flagKeyin=0;
	public static int xLocation=0;
	public static int yLocation=0;
	
	private JPanel panel=null;
	private int[][] array;
	private int rowNumber;
	private int colNumber;
	private int flag=0;
	private int gridWidth=0;
	private int gridHeight=0;
	public plotGraph(int[][] array,int rowNumber,int colNumber)
	{
		super("Maze");
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
	public plotGraph(int[][] array,int rowNumber,int colNumber,int flag)
	{
		super("Maze");
		this.array=array;
		this.rowNumber=rowNumber;
		this.colNumber=colNumber;
		this.flag=flag;
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
				if(flag==1)
				{
					JOptionPane.showMessageDialog(null, "Computer goes first!");
					flag=0;
				}
				gridWidth=getWidth()/colNumber;
				gridHeight=getHeight()/rowNumber;
				for(int i=0;i<colNumber;i++)
				{
					for(int j=0;j<rowNumber;j++)
					{
						if(array[j][i]==0)
						{
							g.setColor(Color.WHITE);
							g.fillRect(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
							g.setColor(Color.BLACK);
							g.drawRect(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
						else if(array[j][i]==1)
						{
							g.setColor(Color.RED);
							g.fillOval(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
							g.setColor(Color.BLACK);
							g.drawRect(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
						else if(array[j][i]==2)
						{
							g.setColor(Color.BLUE);
							g.fillOval(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
							g.setColor(Color.BLACK);
							g.drawRect(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
					}
				}
			}
		};
		panel.addMouseListener(new MouseAdapter() 
		{
			Graphics g=panel.getGraphics();
			public void mouseClicked(MouseEvent e) 
			{
				for(int i=0;i<colNumber;i++)
				{
					for(int j=0;j<rowNumber;j++)
					{
						if(e.getX()>=i*gridWidth&&e.getX()<=i*gridWidth+gridWidth&&e.getY()>=j*gridHeight&&e.getY()<=j*gridHeight+gridHeight)
						{
							xLocation=j;
							yLocation=i;
						}
					}
				}
				flagKeyin=1;
				
				System.out.println(xLocation);
				System.out.println(yLocation);
			}
			public void mouseEntered(MouseEvent e) 
			{
				System.out.println("mouseEntered");
				/*
				for(int i=0;i<colNumber;i++)
				{
					for(int j=0;j<rowNumber;j++)
					{
						if(e.getX()>=i*gridWidth&&e.getX()<=i*gridWidth+gridWidth&&e.getY()>=j*gridHeight&&e.getY()<=j*gridHeight+gridHeight&&array[j][i]==0)
						{
							panel.getGraphics().setColor(Color.GREEN);
							panel.getGraphics().fillRect(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
							panel.getGraphics().setColor(Color.BLACK);
							panel.getGraphics().drawRect(i*gridWidth,j*gridHeight,gridWidth,gridHeight);
						}
					}
				}
				*/
				if(flagKeyin==2)
				{
					JOptionPane.showMessageDialog(null, "Red wins!");
				}
				else if(flagKeyin==3)
				{
					JOptionPane.showMessageDialog(null, "Blue wins!");
				}
			}
			public void mouseExited(MouseEvent e) 
			{
				System.out.println("mouseExited");
				panel.repaint();
			}
			public void mousePressed(MouseEvent e) 
			{
				System.out.println("mousePressed");
			}
			public void mouseReleased(MouseEvent e) 
			{
				System.out.println("mouseReleased");
			}
		});

	}
	public void deletePanel()
	{
		panel.setVisible(false);
		this.dispose();
	}
}