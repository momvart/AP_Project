package graphics.drawers.drawables.animators;

import graphics.IFrameUpdatable;
import graphics.drawers.drawables.Drawable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Animator implements IFrameUpdatable
{
    private boolean paused;
    private boolean repeatable;

    private boolean reversible;
    private boolean reversed;

    protected double timeStack;
    private double duration;

    private Runnable onFinish;


    public Animator(double duration)
    {
        this(duration, false, Collections.emptyList());
    }

    public Animator(double duration, boolean reversible, Drawable... targets)
    {
        this(duration, reversible, Arrays.asList(targets));
    }

    public Animator(double duration, boolean reversible, Collection<Drawable> targets)
    {
        this.reversible = reversible;
        this.duration = duration;
    }

    //region Start, Stop, Pause

    public boolean isPaused()
    {
        return paused;
    }

    public void pause() { this.paused = true; }

    public void start() { this.paused = false; }

    public void restart()
    {
        this.paused = false;
        timeStack = 0;
    }

    public void stop()
    {
        this.pause();
        callOnFinish();
    }

    private void callOnFinish()
    {
        if (onFinish != null)
            onFinish.run();
    }

    public void setOnFinish(Runnable onFinish)
    {
        this.onFinish = onFinish;
    }

    //endregion

    public double getDuration()
    {
        return duration;
    }

    public void setDuration(double duration)
    {
        this.duration = duration;
    }

    public void setRepeatable(boolean repeatable)
    {
        this.repeatable = repeatable;
    }

    public void setReversible(boolean reversible)
    {
        this.reversible = reversible;
    }

    public void reverse()
    {
        reversible = true;
        reversed = !reversed;
    }

    @Override
    public void update(double deltaT)
    {
        if (isPaused())
            return;

        //For performance we separate them completely.
        if (!reversible)
        {
            timeStack += deltaT;
            if (timeStack >= duration)
                if (repeatable)
                    timeStack = 0;
                else
                    stop();
        }
        else
        {
            timeStack += reversed ? -deltaT : deltaT;
            if (timeStack >= duration)
            {
                timeStack = duration;
                reverse();
            }
            else if (timeStack <= 0)
            {
                timeStack = 0;
                reverse();
                if (!repeatable)
                    stop();
            }
        }
    }
}
