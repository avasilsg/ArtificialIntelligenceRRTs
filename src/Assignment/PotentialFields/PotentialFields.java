package Assignment.PotentialFields;

import java.util.*;

import Math.DPoint2D;

public class PotentialFields
{
    private final static int minStep = 7;
    private final static int maxStep = 101;
    
    DPoint2D                 startPoint;
    DPoint2D                 currentPoint;
    double                   stepLenght;
    double                   median;
    double                   maximumSteps;
    ArrayList<Obstacle>      obstacles;
    public double            diameter;
    Obstacle                 goalPoint;
    
    public PotentialFields(DPoint2D startPoint, ArrayList<Obstacle> obstacles,
            double stepLenght, double median, double maximumSteps,
            double diameter)
    {
        this.diameter = diameter;
        this.maximumSteps = maximumSteps;
        this.median = median;
        this.stepLenght = stepLenght;
        setStartPoint(startPoint);
        this.obstacles = obstacles;
        this.currentPoint = new DPoint2D(0, 0);
        this.goalPoint = (Obstacle) obstacles.get(0);
    }
    
    private void setStartPoint(DPoint2D point)
    {
        this.startPoint = new DPoint2D(point.getX(), point.getY());
    }
    
    public boolean updatePosition()
    {
        DPoint2D direction = new DPoint2D(0, 0);
        double sampleStep = 130;
        Iterator<Obstacle> iter = obstacles.iterator();
        
        setObstacleCoords(direction, iter);
        
        calculateNominalValue(direction);
        
        sampleStep = calculateDifferencesFromObstacle(direction, sampleStep);
        if (sampleStep < minStep)
        {
            double currentCoordinate = goalPoint.charge;
            goalPoint.charge *= sampleStep / minStep;
            if (goalPoint.charge == currentCoordinate)
            {
                return true;
            }
        }
        
        if (sampleStep > maxStep)
        {
            goalPoint.charge *= sampleStep / maxStep;
        }
        
        double subStep = sampleStep / 2;
        // TODO:
        DPoint2D vertexCoord = new DPoint2D(0, 0);
        vertexCoord.setX(subStep * direction.getX());
        vertexCoord.setY(subStep * direction.getY());
        double readPosCoordX = median
                * (vertexCoord.getX() - currentPoint.getX()) / stepLenght;
        double readPosCoordY = median
                * (vertexCoord.getY() - currentPoint.getY()) / stepLenght;
        double norm = Math.sqrt(readPosCoordX * readPosCoordX + readPosCoordY
                * readPosCoordY);
        if (norm > maximumSteps)
        {
            readPosCoordX *= maximumSteps / norm;
            readPosCoordY *= maximumSteps / norm;
        }
        updateCurrentPoint(readPosCoordX, readPosCoordY);
        
        return false;
    }
    
    private void updateCurrentPoint(double fx, double fy)
    {
        currentPoint.setX(currentPoint.getX() + ((fx * stepLenght) / median));
        currentPoint.setY(currentPoint.getY() + ((fy * stepLenght) / median));
        startPoint.setX(startPoint.getX() + currentPoint.getX() * stepLenght);
        startPoint.setY(startPoint.getY() + currentPoint.getY() * stepLenght);
    }
    
    private double calculateDifferencesFromObstacle(DPoint2D direction,
            double sampleStep)
    {
        Iterator<Obstacle> iterator;
        iterator = obstacles.iterator();
        while (iterator.hasNext())
        {
            Obstacle currentObstacle = (Obstacle) iterator.next();
            DPoint2D differenciateFromObstacle = new DPoint2D(0, 0);
            double distance = currentObstacle.calcDist(this);
            differenciateFromObstacle.setX(currentObstacle.leftPoint.x
                    - (int) startPoint.getX());
            differenciateFromObstacle.setY(currentObstacle.leftPoint.x
                    - (int) startPoint.getY());
            
            differenciateFromObstacle
                    .setX(repelTheRobot(differenciateFromObstacle.getX()));
            differenciateFromObstacle
                    .setY(repelTheRobot(differenciateFromObstacle.getY()));
            
            double moveForward = distance
                    / ((differenciateFromObstacle.getX() * direction.getX() + differenciateFromObstacle
                            .getY() * direction.getY()));
            if ((moveForward > 0) && (moveForward < sampleStep))
                sampleStep = moveForward;
        }
        return sampleStep;
    }
    
    private void calculateNominalValue(DPoint2D direction)
    {
        double nominalValue = Math.sqrt(direction.getX() * direction.getX()
                + direction.getY() * direction.getY());
        direction.setX(direction.getX() / nominalValue);
        direction.setY(direction.getY() / nominalValue);
    }
    
    private void setObstacleCoords(DPoint2D direction, Iterator<Obstacle> iter)
    {
        while (iter.hasNext())
        {
            DPoint2D differenciateFromObstacle = new DPoint2D(0, 0);
            Obstacle currentObstacle = (Obstacle) iter.next();
            double distance = currentObstacle.calcDist(this);
            differenciateFromObstacle.setX(currentObstacle.charge
                    * (currentObstacle.leftPoint.x - (int) startPoint.getX())
                    / distance);
            differenciateFromObstacle.setY(currentObstacle.charge
                    * (currentObstacle.leftPoint.x - (int) startPoint.getY())
                    / distance);
            direction.setX(direction.getX() + differenciateFromObstacle.getX());
            direction.setY(direction.getY() + differenciateFromObstacle.getY());
        }
    }
    
    double repelTheRobot(double coord)
    {
        Random point = new Random();
        
        return coord + point.nextGaussian();
    }
}