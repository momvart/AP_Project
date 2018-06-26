package graphics.drawers.drawables.animators;

import graphics.drawers.drawables.Drawable;

import java.util.Collection;

public class ValueAnimator extends Animator
{
    private double from;
    private double to;

    private double currentValue = 0;

    public ValueAnimator(double duration, double from, double to)
    {
        super(duration);
        this.from = from;
        this.to = to;
    }

    public double getCurrentValue()
    {
        return currentValue;
    }

    @Override
    public void update(double deltaT)
    {
        super.update(deltaT);
        currentValue = timeStack / getDuration() * (to - from) + from;
    }
}
