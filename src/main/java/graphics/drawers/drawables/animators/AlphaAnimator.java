package graphics.drawers.drawables.animators;

import graphics.drawers.drawables.Drawable;
import graphics.drawers.drawables.IAlphaDrawable;

import java.util.*;

public class AlphaAnimator extends ValueAnimator
{
    private List<IAlphaDrawable> targets;

    public AlphaAnimator(double duration, double from, double to, IAlphaDrawable... targets)
    {
        super(duration, from, to);
        this.targets = Arrays.asList(targets);
    }

    @Override
    public void update(double deltaT)
    {
        super.update(deltaT);
        targets.forEach(t -> t.setAlpha(getCurrentValue()));
    }
}
