package utils;

public class Point
{
    private int x;
    private int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public static double euclideanDistance(Point p1, Point p2)
    {
        return Math.sqrt(euclideanDistance2nd(p1, p2));
    }

    public static double euclideanDistance2nd(Point p1, Point p2)
    {
        return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Point))
            return false;
        if (obj == this)
            return true;
        Point p = (Point)obj;
        return this.x == p.x && this.y == p.y;
    }

    @Override
    public String toString()
    {
        return String.format("(%d,%d)", x, y);
    }
}
