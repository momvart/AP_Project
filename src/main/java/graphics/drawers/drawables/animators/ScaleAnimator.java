package graphics.drawers.drawables.animators;

import graphics.drawers.drawables.Drawable;

import java.util.Arrays;
import java.util.List;

public class ScaleAnimator extends ValueAnimator
{
    private List<Drawable> targets;

    public ScaleAnimator(double duration, double from, double to, Drawable... targets)
    {
        super(duration, from, to);
        this.targets = Arrays.asList(targets);
    }

    @Override
    public void update(double deltaT)
    {
        super.update(deltaT);
        targets.forEach(t -> t.setScale(getCurrentValue(), getCurrentValue()));
    }
}
