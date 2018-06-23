package graphics.drawers.drawables.animators;

import graphics.drawers.drawables.Drawable;

import java.util.Collection;

public class AlphaAnimator extends Animator
{
    private double from = 0;
    private double to = 1;

    public AlphaAnimator(double duration, Drawable... targets)
    {
        super(duration, targets);
    }

    public AlphaAnimator(double duration, boolean reversible, Drawable... targets)
    {
        super(duration, reversible, targets);
    }

    public AlphaAnimator(double duration, boolean reversible, Collection<Drawable> targets)
    {
        super(duration, reversible, targets);
    }

    public AlphaAnimator(double duration, boolean reversible, Collection<Drawable> targets, double from, double to)
    {
        super(duration, reversible, targets);
        this.from = from;
        this.to = to;
    }

    public AlphaAnimator(double duration, double from, double to, Drawable... targets)
    {
        super(duration, targets);
        this.from = from;
        this.to = to;
    }

    @Override
    public void update(double deltaT)
    {
        super.update(deltaT);
        getTargets().forEach(t -> t.setAlpha(timeStack / getDuration() * (to - from) + from));
    }
}
