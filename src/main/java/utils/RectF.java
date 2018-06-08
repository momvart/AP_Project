package utils;

public class RectF
{
    private float x, y, width, height;

    public RectF(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public static RectF fromLTRB(float left, float top, float right, float bottom)
    {
        return new RectF(Math.min(left, right), Math.min(top, bottom), Math.abs(right - left), Math.abs(bottom - top));
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public float getLeft()
    {
        return x;
    }

    public float getRight()
    {
        return x + width;
    }

    public float getTop()
    {
        return y;
    }

    public float getBottom()
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

    public boolean intersectsWith(float x, float y, float width, float height)
    {
        return getRight() >= x
                && getLeft() <= x + width
                && getTop() <= y + height
                && getBottom() >= y;
    }

    public boolean contains(float x, float y)
    {
        return x >= getLeft() && x <= getRight() && y >= getTop() && y <= getBottom();
    }


    @Override
    public String toString()
    {
        return String.format("%f, %f, %f, %f", getLeft(), getTop(), getRight(), getBottom());
    }
}
