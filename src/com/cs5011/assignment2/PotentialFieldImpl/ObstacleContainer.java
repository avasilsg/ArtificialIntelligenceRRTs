package com.cs5011.assignment2.PotentialFieldImpl;

import easyGui.EasyGui;
import renderables.Renderable;
import renderables.RenderableOval;
import renderables.RenderableRectangle;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.HashMap;

public class ObstacleContainer
{
    private EasyGui             graphicalUserInt;
    HashMap<String, Renderable> obstacles     = new HashMap<String, Renderable>();
    
    private int                 rectanglesNum     = 4;
    private int                 circularShapes    = 2;
    
    int                         sqaureShapesLen;
    int                         circularShapesLen;
    
    public ObstacleContainer(EasyGui graphicalUserInt, int sqaureShapesLen,
            int circularShapesLen)
    {
        this.graphicalUserInt = graphicalUserInt;
        this.sqaureShapesLen = sqaureShapesLen;
        this.circularShapesLen = circularShapesLen;
    }
    
    public void drawObstacle()
    {
        generateRectangles(rectanglesNum);
        
        generateCirculars(circularShapes);
        
        graphicalUserInt.update();
        graphicalUserInt.show();
    }
    
    private void generateRectangles(int numberOfRectangles)
    {
        if (0 > numberOfRectangles)
        {
            // TODO: throw exception
        }
        for (int i = 1; i <= numberOfRectangles; i++)
        {
            int xCoord = (int) (Math.random() * 250 + 100);
            int yCoord = (int) (Math.random() * 300 + 100);
            System.out.println(xCoord + "  " + yCoord);
            RenderableRectangle rectangle = new RenderableRectangle(xCoord,
                    yCoord, sqaureShapesLen, sqaureShapesLen);
            rectangle.setProperties(Color.RED, 0.2f, true, true);
            obstacles.put("Rect" + i, rectangle);
            graphicalUserInt.draw(rectangle);
        }
    }
    
    private void generateCirculars(int numberOfCirculars)
    {
        if (0 > numberOfCirculars)
        {
            // TODO: throw exception
        }
        for (int i = 1; i <= numberOfCirculars; i++)
        {
            int xCoord = (int) (Math.random() * 350 + 60);
            int yCoord = (int) (Math.random() * 700 + 80);
            System.out.println(xCoord + "  " + yCoord);
            RenderableOval circular = new RenderableOval(xCoord, yCoord,
                    circularShapesLen, circularShapesLen);
            circular.setProperties(Color.BLACK, 0.1f, true);
            obstacles.put("circ" + i, circular);
            graphicalUserInt.draw(circular);
            
        }
    }
    
    public Boolean isInsideObstacle(double coordX, double coordY)
    {
        // Rectangle
        boolean flag = false;
        flag = isIntheRect(coordX, coordY);
        if (true == flag)
        {
            return true;
        }
        flag = isInCircular(coordX, coordY);
        
        return flag;
    }
    
    private boolean isInCircular(double coordX, double coordY)
    {
        for (int i = 1; i <= circularShapes; i++)
        {
            RenderableOval circle = (RenderableOval) obstacles.get("circ" + i);
            if (((coordX - circle.centreX) * (coordX - circle.centreX) + (coordY - circle.centreY)
                    * (coordY - circle.centreY)) <= Math.pow(circle.width + 1,
                    2))
            {
                return true;
            }
        }
        return false;
    }
    
    private boolean isIntheRect(double coordX, double coordY)
    {
        for (int i = 1; i <= rectanglesNum; i++)
        {
            RenderableRectangle rectangle = (RenderableRectangle) obstacles
                    .get("Rect" + i);
            if (rectangle.bottomLeftX - 0.5 <= coordX
                    && (rectangle.bottomLeftX + rectangle.width) + 0.5 >= coordX
                    && rectangle.bottomLeftY - 0.5 <= coordY
                    && (rectangle.bottomLeftY + rectangle.height + 0.5) >= coordY)
            {
                return true;
            }
        }
        
        return false;
    }
    
    public Boolean isIntersectObstacle(float upperLeftX, float upperLeftY,
            float downRightX, float downRightY)
    {
        boolean flag = false;
        Line2D path_line = new Line2D.Float(upperLeftX, upperLeftY, downRightX,
                downRightY);
        
        flag = interRect(path_line);
        if (flag)
        {
            return flag;
        }
        
        flag = isInCurcle(upperLeftX, upperLeftY, downRightX, downRightY);
        
        return flag;
    }
    
    private boolean isInCurcle(float upperLeftX, float upperLeftY,
            float downRightX, float downRightY)
    {
        for (int i = 1; i <= circularShapes; i++)
        {
            RenderableOval circular = (RenderableOval) obstacles
                    .get("circ" + i);
            
            // solving quadratic equation
            double firstParam = (downRightX - upperLeftX)
                    * (downRightX - upperLeftX) + (downRightY - upperLeftY)
                    * (downRightY - upperLeftY);
            double secondParam = 2 * Math.abs((downRightX - upperLeftX)
                    * (upperLeftX - circular.centreX)
                    + (downRightY - upperLeftY)
                    * (upperLeftY - circular.centreY));
            double thirdParam = circular.centreX
                    * circular.centreX
                    + circular.centreY
                    * circular.centreY
                    + upperLeftX
                    * upperLeftX
                    + upperLeftY
                    * upperLeftY
                    - 2
                    * Math.abs(circular.centreX * upperLeftX + circular.centreY
                            * upperLeftY) - Math.pow(circular.width + 1, 2);
            
            double squareRoot = (-secondParam + Math.sqrt(secondParam
                    * secondParam - 4 * firstParam * thirdParam))
                    / 2 * firstParam;
            double squareRoot2 = (-secondParam - Math.sqrt(secondParam
                    * secondParam - 4 * firstParam * thirdParam))
                    / 2 * firstParam;
            
            if ((squareRoot >= 0 && squareRoot <= 1)
                    || (squareRoot2 >= 0 && squareRoot2 <= 1))
            {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean interRect(Line2D path_line)
    {
        for (int i = 1; i <= rectanglesNum; i++)
        {
            RenderableRectangle rectangle = (RenderableRectangle) obstacles
                    .get("Rectangle" + i);
            
            Rectangle rect = new Rectangle(rectangle.bottomLeftX - 1,
                    rectangle.bottomLeftY - 1 + rectangle.height + 1,
                    rectangle.width + 2, rectangle.height + 2);
            
            if (path_line.intersects(rect))
            {
                return true;
            }
        }
        
        return false;
    }
    
    public double obstaclePotential(int exploreRange, float upperLeftX,
            float upperLeftY, float downRightX, float downRightY)
    {
        
        double obstaclePotential = 0.0;
        
        int distance = 0;
        double distancePointX = upperLeftX;
        double distancePointY = upperLeftY;
        
        for (int i = 0; i <= exploreRange; i++)
        {
            distancePointX = upperLeftX + (downRightX - upperLeftX) * distance
                    / exploreRange;
            distancePointY = upperLeftY + (downRightY - upperLeftY) * distance
                    / exploreRange;
            if (this.isInsideObstacle(distancePointX, distancePointY))
            {
                if (distance > 0 && distance < exploreRange)
                {
                    double calculateStep = exploreRange - distance;
                    obstaclePotential = Math.exp(-1 / calculateStep) / distance;
                    return obstaclePotential;
                }
                
            }
            else
            {
                distance++;
            }
        }
        
        obstaclePotential = calculatePotential(exploreRange, obstaclePotential,
                distance);
        
        return obstaclePotential;
    }
    
    private double calculatePotential(int exploreRange,
            double obstaclePotential, int distance)
    {
        if (distance > 0 && (distance < exploreRange))
        {
            double calculateStep = exploreRange - distance;
            obstaclePotential = Math.exp(-1 / calculateStep) / distance;
        }
        else if (distance == 0)
        {
            obstaclePotential = Math.pow(10, 10);
        }
        
        return obstaclePotential;
    }
    
    public void setObstacleSizes(int sqaureShapesLen, int circularShapesLen)
    {
        this.sqaureShapesLen = sqaureShapesLen;
        this.circularShapesLen = circularShapesLen;
    }
}
