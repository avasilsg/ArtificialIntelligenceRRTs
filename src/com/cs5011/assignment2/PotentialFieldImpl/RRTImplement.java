package com.cs5011.assignment2.PotentialFieldImpl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import renderables.RenderablePoint;
import renderables.RenderablePolyline;
import dataStructures.RRNode;
import dataStructures.RRTree;
import easyGui.EasyGui;
import geometry.FloatPoint;
import geometry.IntPoint;

public class RRTImplement
{
    
    private final EasyGui graphicInterface;
    private final RRTree  tree;
    
    private double               stepLen;
    
    private FloatPoint           goalPoint;
    private int                  goalRadius;
    private int                  goalBiasValue;
    Random                       random = new Random();
    private  ArrayList<IntPoint> reversedPath;
    private double               calculateRadious;
    private FloatPoint           samplePoint;
    private int                  samplePointNum;
    
    IntPoint                     newNode;
    RRNode                       nearNode;
    ObstacleContainer            obstacle;
    
    public RRTImplement(EasyGui graphInterface, ObstacleContainer obstacle,
            FloatPoint robotPoint, FloatPoint goal, double stepLen)
    {
        this.graphicInterface = graphInterface;
        this.obstacle = obstacle;
        this.goalPoint = goal;
        this.stepLen = stepLen;
        this.goalBiasValue = 5;
        this.goalRadius = 80;
        this.newNode = new IntPoint(0, 0);
        this.reversedPath = new ArrayList<>();
        calculateRadious(robotPoint);
        
        samplePoint = new FloatPoint((goalPoint.x + robotPoint.x) / 2,
                (goalPoint.y + robotPoint.y) / 2);
        
        tree = new RRTree(Color.BLACK);
        
        IntPoint goalInt = new IntPoint((int) goalPoint.x, (int) goalPoint.y);
        tree.setStartAndGoal(new IntPoint((int) robotPoint.x,
                (int) robotPoint.y), goalInt, goalRadius);
        
        graphInterface.draw(tree);
        
    }
    
    private void calculateRadious(FloatPoint robotPoint)
    {
        calculateRadious = Math.sqrt((goalPoint.x - robotPoint.x)
                * (goalPoint.x - robotPoint.x) + (goalPoint.y - robotPoint.y)
                * (goalPoint.x - robotPoint.y));
    }
    
    public void searchingTree()
    {
        while (getSquareDistanceToGoal(this.newNode) > 100)
        {
            IntPoint samplePoint = generateNextSamplePoint();
            expandNode(samplePoint.x, samplePoint.y);
        }
        reachedGoalRegion();
    }
    
    public void searchingWithBias()
    {
        while (getSquareDistanceToGoal(newNode) > 100)
        {
            IntPoint point = genBiasPoint();
            expandNode(point.x, point.y);
        }
        reachedGoalRegion();
    }
    
    public double getSquareDistanceToGoal(IntPoint newNode)
    {
        return (newNode.x - goalPoint.x) * (newNode.x - goalPoint.x)
                + (newNode.y - goalPoint.y) * (newNode.y - goalPoint.y);
    }
    
    public IntPoint generateNextSamplePoint()
    {
        int randomAngle = random.nextInt(360);
        int generateLen = random.nextInt((int) calculateRadious);
        IntPoint point = new IntPoint((int) (samplePoint.x + generateLen
                * Math.cos(Math.toRadians(randomAngle))),
                (int) (samplePoint.y + generateLen
                        * Math.sin(Math.toRadians(randomAngle))));
        return point;
    }
    
    public IntPoint genBiasPoint()
    {
        IntPoint point;
        
        if (0 == (samplePointNum % goalBiasValue))
        {
            point = new IntPoint((int) goalPoint.x, (int) goalPoint.y);
            return point;
        }
        else
        {
            point = generateRandomBiasPoint();
        }
        ++samplePointNum;
        
        return point;
    }
    
    private IntPoint generateRandomBiasPoint()
    {
        IntPoint point = new IntPoint(0, 0);
        int randomAngle = random.nextInt(270);
        int randomLen = random.nextInt((int) calculateRadious);
        point.x = ((int) (samplePoint.x + randomLen
                * Math.cos(Math.toRadians(randomAngle))));
        point.y = (int)  (samplePoint.y + randomLen
                        * Math.sin(Math.toRadians(randomAngle)));
        return point;
    }
    
    public void expandNode(int coordX, int coordY)
    {
        
        RenderablePoint samplePoint = new RenderablePoint(coordX, coordY);
        samplePoint.setProperties(Color.BLUE, 3.0f);
        
        graphicInterface.draw(samplePoint);
        
        nearNode = tree.getNearestNeighbour(new IntPoint(coordX, coordY));
        
        double distance = Math.sqrt((coordX - nearNode.x)
                * (coordX - nearNode.x) + (coordY - nearNode.y)
                * (coordY - nearNode.y));
        FloatPoint expectedNode = new FloatPoint(nearNode.x
                + (int) ((coordX - nearNode.x) / distance * stepLen),
                nearNode.y + (int) ((coordY - nearNode.y) / distance * stepLen));
        
        if (!obstacle.isInsideObstacle(expectedNode.x, expectedNode.y))
        {
            moveToTheNextNode(coordX, coordY, distance);
        }
    }
    
    private void moveToTheNextNode(int coordX, int coordY, double distance)
    {
        if (distance > stepLen)
        {
            this.newNode.x = nearNode.x
                    + (int) ((coordX - nearNode.x) / distance * stepLen);
            this.newNode.y = nearNode.y
                    + (int) ((coordY - nearNode.y) / distance * stepLen);
        }
        else
        {
            this.newNode.x = coordX;
            this.newNode.y = coordY;
        }
        
        tree.addNode(nearNode, new IntPoint(newNode.x, newNode.y));
    }
    
    public void reachedGoalRegion()
    {
        
       reversedPath = tree.getPathFromRootTo(nearNode);
        
        nearNode = tree.getNearestNeighbour(new IntPoint((int) goalPoint.x,
                (int) goalPoint.y));
        tree.addNode(nearNode, new IntPoint((int) goalPoint.x,
                (int) goalPoint.y));
        
        for (int i = 0; i < reversedPath.size() - 1; i++)
        {
            try
            {
                Thread.sleep(80);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            RenderablePolyline iPath = new RenderablePolyline();
            iPath.addPoint(reversedPath.get(i).x, reversedPath.get(i).y);
            iPath.addPoint(reversedPath.get(i + 1).x, reversedPath.get(i + 1).y);
            iPath.setProperties(Color.RED, 2);
            graphicInterface.draw(iPath);
            graphicInterface.update();
            graphicInterface.show();
        }
    }
    
}
