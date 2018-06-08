package utils;

public class SizeF
{
    private float width;
    private float height;

    public SizeF(float width, float height)
    {
        this.width = width;
        this.height = height;
    }

    public SizeF(double width, double height)
    {
        this.width = (float)width;
        this.height = (float)height;
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
}
