package graphics.drawers.drawables.bars;

import graphics.drawers.drawables.Drawable;

public abstract class Bar extends Drawable
{
    protected double width;
    protected double height;

    public Bar(double width, double height)
    {
        this.width = width;
        this.height = height;
    }
}
