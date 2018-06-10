package utils;

public class RectF
{
    private double x, y, width, height;

    public RectF(double x, double y, double width, double height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public static RectF fromLTRB(double left, double top, double right, double bottom)
    {
        return new RectF(Math.min(left, right), Math.min(top, bottom), Math.abs(right - left), Math.abs(bottom - top));
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

    public double getWidth()
    {
        return width;
    }

    public void setWidth(double width)
    {
        this.width = width;
    }

    public double getHeight()
    {
        return height;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public double getLeft()
    {
        return x;
    }

    public double getRight()
    {
        return x + width;
    }

    public double getTop()
    {
        return y;
    }

    public double getBottom()
    {
        return y + height;
    }

    public boolean intersectsWith(RectF other)
    {
        return getRight() >= other.getLeft()
                && getLeft() <= other.getRight()
                && getTop() <= other.getBottom()
                && getBottom() >= other.getTop();
    }

    public boolean intersectsWith(double x, double y, double width, double height)
    {
        return getRight() >= x
                && getLeft() <= x + width
                && getTop() <= y + height
                && getBottom() >= y;
    }

    public boolean contains(double x, double y)
    {
        return x >= getLeft() && x <= getRight() && y >= getTop() && y <= getBottom();
    }


    @Override
    public String toString()
    {
        return String.format("%f, %f, %f, %f", getLeft(), getTop(), getRight(), getBottom());
    }
}
