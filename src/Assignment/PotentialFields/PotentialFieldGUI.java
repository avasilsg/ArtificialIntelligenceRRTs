package Assignment.PotentialFields;

import geometry.IntPoint;

import javax.swing.*;
import java.util.Random;
import Math.DPoint2D;

import java.awt.*;
import java.util.*;

public class PotentialFieldGUI extends JFrame
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    JPanel                    contentPane;
    ArrayList<Obstacle>       obstacles;
    PotentialFields           potentialField;
    Thread                    functionalThread;
    BorderLayout              layout;
    JPanel                    commands;
    Set<Point>                pointsSet;
    JPanel                    drawingPanel     = new JPanel()
    {
    
           private static final long serialVersionUID = 1L;
           private int               flags;
           private boolean           autoscrolls;
           
           public void paint(
                   Graphics graphics)
           {
               super.paint(graphics);
               graphics.setColor(Color.GREEN);
               pointsSet
                       .add(new Point(
                               (int) potentialField.startPoint
                                       .getX(),
                               (int) potentialField.startPoint
                                       .getY()));
               Iterator<Obstacle> iter = obstacles
                       .iterator();
               int count = 0;
               while (iter.hasNext())
               {
                   Obstacle obstacles = (Obstacle) iter
                           .next();
                   if (0 == count)
                   {
                       graphics.setColor(Color.RED);
                       graphics.fillArc(
                               (int) (obstacles.leftPoint.x - obstacles.diam / 2),
                               (int) (obstacles.leftPoint.y - obstacles.diam / 2),
                               (int) obstacles.diam,
                               (int) obstacles.diam,
                               0, 360);
                       ++count;
                   }
                   else
                   {
                       graphics.setColor(Color.GREEN);
                       graphics.fillRect(
                               (int) (obstacles.leftPoint.x - obstacles.diam / 2),
                               (int) (obstacles.leftPoint.y - obstacles.diam / 2),
                               (int) obstacles.diam,
                               (int) obstacles.diam);
                   }
               }
               graphics.setColor(Color.BLUE);
               for (Point p : pointsSet)
               {
                   graphics.fillRoundRect(
                           (int) (p.x - potentialField.diameter / 4),
                           (int) (p.y - potentialField.diameter / 4),
                           (int) potentialField.diameter / 2,
                           (int) potentialField.diameter / 2,
                           0, flags);
               }
               
               graphics.setColor(Color.darkGray);
               graphics.fill3DRect(
                       (int) (potentialField.startPoint
                               .getX() - potentialField.diameter / 2),
                       (int) (potentialField.startPoint
                               .getY() - potentialField.diameter / 2),
                       (int) potentialField.diameter,
                       (int) potentialField.diameter,
                       autoscrolls);
           }
    };
    
    public PotentialFieldGUI()
    {
        try
        {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.pointsSet = new HashSet<Point>();
            initFunc();
            startAction();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        
    }
    
    private void startAction()
    {
        functionalThread = new Thread(new Runnable()
        {
            public void run()
            {
                while ((potentialField.startPoint.getX() > 1)
                        && (potentialField.startPoint.getY() > 1))
                {
                    if (true == potentialField.updatePosition())
                    {
                        reachedTheGoal();
                    }
                    drawingPanel.repaint();
                    try
                    {
                        Thread.sleep(80);
                    }
                    catch (InterruptedException ex)
                    {
                        System.out.println(ex);
                    }
                }
            }
        });
        functionalThread.start();
    }
    
    private void initFunc() throws Exception
    {
        obstacles = new ArrayList<Obstacle>();
        this.commands = new JPanel();
        this.layout = new BorderLayout();
        
        // add goal obstacle
        
        obstacles.add(new Obstacle(new IntPoint(45, 80), +100, 40));
        // add other obstacles
        addObstacles();
        setSize(new Dimension(1024, 768));
        setTitle("Potential Fields");
        this.setVisible(true);
        
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(layout);
        contentPane.add(drawingPanel, java.awt.BorderLayout.CENTER);
        contentPane.add(commands, java.awt.BorderLayout.NORTH);
        potentialField = new PotentialFields(new DPoint2D(800, 600), obstacles,
                0.1, 2, 8000, 18);
        functionalThread = new Thread();
    }
    
    private void addObstacles()
    {
        Random generator = new Random(150);
        int i = generator.nextInt();
        int j = generator.nextInt();
        obstacles.add(new Obstacle(new IntPoint(i, j), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(500, 511), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(524, 526), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(360, 600), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(450, 600), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(625, 450), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(269, 400), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(456, 150), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(524, 526), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(125, 566), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(368, 147), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(324, 450), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(524, 333), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(328, 800), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(147, 627), -150, 66));
        obstacles.add(new Obstacle(new IntPoint(475, 369), -150, 66));
    }
    
    public void reachedTheGoal()
    {
        if (true)
        {
            this.dispose();
        }
    }    
}