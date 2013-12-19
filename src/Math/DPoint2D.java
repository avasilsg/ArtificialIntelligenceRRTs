package Math;

public class DPoint2D
{
    public double distance(double x, double y)
    {
        this.distance = Math.sqrt((x)*(x) + (y)*(y));
        return distance;
    }
    
    public double getX()
    {
        return this.x;
    }

    public void setX(double d)
    {
        this.x = d;
    }

    public double getY()
    {
        return this.y;
    }

    public void setY(double y)
    {
        this.y = y;
    }
  
    public DPoint2D(double x, double y)
    {
        this.x = x;
        this.y = y;
        this.distance = 0;
    }
    
    public double getDistance()
    {
        return this.distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }
    
    private double x;
    private double y;  
    private double distance;
}
