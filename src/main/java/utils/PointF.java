package utils;

public class PointF
{
    private double x;
    private double y;

    public PointF(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public static double euclideanDistance(PointF p1, PointF p2)
    {
        return Math.sqrt(euclideanDistance2nd(p1, p2));
    }

    public static double euclideanDistance2nd(PointF p1, PointF p2)
    {
        return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof PointF))
            return false;
        if (obj == this)
            return true;
        PointF p = (PointF)obj;
        return this.x == p.x && this.y == p.y;
    }

    @Override
    public String toString()
    {
        return String.format("(%f,%f)", x, y);
    }
}
