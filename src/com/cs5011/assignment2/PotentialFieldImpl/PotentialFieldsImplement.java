package com.cs5011.assignment2.PotentialFieldImpl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import renderables.Renderable;
import renderables.RenderablePoint;
import easyGui.EasyGui;
import geometry.FloatPoint;

public class PotentialFieldsImplement
{
    private final EasyGui graphInterfs;
    private int           currentAngle;
    private int           circleRadius;
    private int           solarRange;
    private int           solarSenson;
    private double        stepLen;
    private FloatPoint    startPoint;
    private FloatPoint    goalPoint;
    
    private double        goalPotentialCharge;
    private double        obstaclePotentialCharge;
    
    Random                random = new Random();
    ArrayList<Renderable> renderables;
    ObstacleContainer     obstacle;
    
    public PotentialFieldsImplement(EasyGui graphInterf,
            ObstacleContainer obstacle, FloatPoint robotPoint,
            FloatPoint goalPoint, double stepLen)
    {
        init();
        this.graphInterfs = graphInterf;
        this.obstacle     = obstacle;
        this.startPoint   = robotPoint;
        
        setGoalPoint(goalPoint.x, goalPoint.y);
        this.stepLen = stepLen;
        
        renderables = new ArrayList<Renderable>();
        drawPotentialFields();
        renderGoalPoint(graphInterf, goalPoint);
    }
    
    private void init()
    {
        currentAngle = 190;
        circleRadius = 10;
        solarRange = 300;
        solarSenson = 5;
        stepLen = 0;
        goalPotentialCharge = 0.5;
        obstaclePotentialCharge = 40;
    }
    
    private void renderGoalPoint(EasyGui gui, FloatPoint goalPoint)
    {
        RenderablePoint goal = new RenderablePoint(goalPoint.x, goalPoint.y);
        goal.setProperties(Color.GREEN, 20);
        gui.draw(goal);
    }
    
    private void setGoalPoint(float coordX, float coordY)
    {
        this.goalPoint = new FloatPoint(coordX, coordY);
        this.goalPoint.x = coordX;
        this.goalPoint.y = coordY;
    }
    
    public void runDemo()
    {
        
        while (getSquareDistanceToGoal(this.startPoint) > stepLen * stepLen)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            this.isReachedGoal();
            
            graphInterfs.update();
            graphInterfs.show();
        }
        updateRobotMovement();
        drawPotentialFields();
        graphInterfs.update();
        graphInterfs.show();
    }
    
    private void updateRobotMovement()
    {
        this.startPoint.x += goalPoint.x;
        this.startPoint.y += goalPoint.y;
    }
    
    public void drawPotentialFields()
    {
        graphInterfs.unDraw(renderables);
        renderables.clear();
        
        drawPathLine();
        
        calculateAngle();
        graphInterfs.draw(renderables);
        
    }
    
    private void calculateAngle()
    {
        for (int i = (int) (currentAngle - 90); i <= (int) (currentAngle + 90); i += 180 / (solarSenson - 1))
        {
            RenderablePoint solarSensorPoint = new RenderablePoint(
                    this.startPoint.x
                            + (int) (circleRadius * Math.cos(Math.toRadians(i))),
                    this.startPoint.y
                            + (int) (circleRadius * Math.sin(Math.toRadians(i))));
            solarSensorPoint.setProperties(Color.DARK_GRAY, 10);
            renderables.add(solarSensorPoint);
        }
    }
    
    private void drawPathLine()
    {
        RenderablePoint robot = new RenderablePoint(this.startPoint.x,
                this.startPoint.y);
        robot.setProperties(Color.CYAN, 8);
        graphInterfs.draw(robot);
    }
    
    public void isReachedGoal()
    {
        FloatPoint samplePoint = new FloatPoint();
        
        calculateSamplePoint(samplePoint);
        double goalPotent = calculateGoalCharge(samplePoint);
        double minPotential = calculateMinimumPotential(samplePoint, goalPotent);
        
        int directionAngle = 0;
        for (int i = (int) (currentAngle - 90); i <= (int) (currentAngle + 90); i += 180 / (solarSenson - 1))
        {
            FloatPoint sampleTemp = new FloatPoint();
            calculateSamplePoint(i, sampleTemp);
            double goalPotCharg = calculateGoalCharge(sampleTemp);
            double obstaclCharg = calculateObstacleCharge(i, sampleTemp);
            if (minPotential > goalPotCharg + obstaclCharg)
            {
                minPotential   = goalPotCharg + obstaclCharg;
                directionAngle = i % 360;
            }
        }
        
        currentAngle = directionAngle;
        startPoint.x += (stepLen * Math.cos(Math.toRadians(currentAngle)));
        startPoint.y += (stepLen * Math.sin(Math.toRadians(currentAngle)));
        
        drawPotentialFields();
    }
    
    private double calculateGoalCharge(FloatPoint sampleTemp)
    {
        return goalPotentialCharge
                * getSquareDistanceToGoal(new FloatPoint(sampleTemp.x,
                        sampleTemp.y));
    }
    
    private double calculateObstacleCharge(int angle, FloatPoint sampleTemp)
    {
        return obstaclePotentialCharge
                * obstacle.obstaclePotential(solarRange, sampleTemp.x,
                        sampleTemp.y,
                        calculate(sampleTemp.x, sampleTemp.y, angle % 360).x,
                        calculate(sampleTemp.x, sampleTemp.y, angle % 360).y);
    }
    
    private void calculateSamplePoint(int angle, FloatPoint sampleTemp)
    {
        sampleTemp.x = (float) (startPoint.x + circleRadius
                * Math.cos(Math.toRadians(angle % 360)));
        sampleTemp.y = (float) (startPoint.y + circleRadius
                * Math.sin(Math.toRadians(angle % 360)));
    }
    
    private double calculateMinimumPotential(FloatPoint samplePoint,
            double goalPotent)
    {
        return goalPotent
                + obstaclePotentialCharge
                * obstacle.obstaclePotential(
                        solarRange,
                        samplePoint.x,
                        samplePoint.y,
                        calculate(samplePoint.x, samplePoint.y,
                                currentAngle - 90).x,
                        calculate(samplePoint.x, samplePoint.y,
                                currentAngle - 90).y);
    }
    
    private void calculateSamplePoint(FloatPoint samplePoint)
    {
        samplePoint.x = this.startPoint.x
                + (int) (circleRadius * Math.cos(Math
                        .toRadians(currentAngle - 90)));
        samplePoint.y = this.startPoint.y
                + (int) (circleRadius * Math.sin(Math
                        .toRadians(currentAngle - 90)));
    }
    
    public FloatPoint calculate(float xCoord, float yCoord, int angle)
    {
        FloatPoint nextPoint = new FloatPoint();
        nextPoint.x = xCoord
                + (int) (this.solarRange * Math.cos(Math.toRadians(angle)));
        nextPoint.y = yCoord
                + (int) (solarRange * Math.sin(Math.toRadians(angle)));
        
        return nextPoint;
    }
    
    public double getSquareDistanceToGoal(FloatPoint currentRobotPoint)
    {
        return (currentRobotPoint.x - goalPoint.x)
                * (currentRobotPoint.x - goalPoint.x)
                + (currentRobotPoint.y - goalPoint.y)
                * (currentRobotPoint.y - goalPoint.y);
        
    }
}
