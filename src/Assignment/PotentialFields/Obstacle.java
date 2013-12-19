package Assignment.PotentialFields;

import java.awt.Point;

import geometry.IntPoint;

public class Obstacle
{
    double   diam      = 15;
    double   mass      = 10;
    IntPoint leftPoint = null;
    double   charge    = -10;
    
    public Obstacle(IntPoint leftPoint, double charge, double diam)
    {
        this.diam = diam;
        this.leftPoint = leftPoint;
        this.charge = charge;
    }
    
    public Obstacle(IntPoint leftPoint)
    {
        this.leftPoint = leftPoint;
    }
    



    public double calcDist(PotentialFields field)
    {
        double currentDistance = distance(field);
        return currentDistance * currentDistance;
    }
    
    public double distance(PotentialFields field)
    {
        Point point = new Point();
        point.x = leftPoint.x;
        point.y = leftPoint.y;
        double currentDistance = point.distance((int) field.startPoint.getX(),
                (int) field.startPoint.getY()) - (diam + field.diameter) / 2;
        return currentDistance > 0 ? currentDistance : 0.0001;
    }
}