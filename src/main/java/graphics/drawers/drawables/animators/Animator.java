package graphics.drawers.drawables.animators;

import graphics.IFrameUpdatable;
import graphics.drawers.drawables.Drawable;
import javafx.animation.FadeTransition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public abstract class Animator implements IFrameUpdatable
{
    private ArrayList<Drawable> targets = new ArrayList<>();
    private boolean paused;

    private boolean reversible;
    private boolean reversed;
    protected double timeStack;
    private double duration;


    public Animator(double duration, Drawable... targets)
    {
        this(duration, false, targets);
    }

    public Animator(double duration, boolean reversible, Drawable... targets)
    {
        this(duration, reversible, Arrays.asList(targets));
    }

    public Animator(double duration, boolean reversible, Collection<Drawable> targets)
    {
        this.reversible = reversible;
        this.duration = duration;
        this.targets = new ArrayList<>(targets);
    }

    public ArrayList<Drawable> getTargets()
    {
        return targets;
    }

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


    public double getDuration()
    {
        return duration;
    }

    public void setDuration(double duration)
    {
        this.duration = duration;
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
                timeStack = 0;
        }
        else
        {
            timeStack += reversed ? -deltaT : deltaT;
            if (timeStack >= duration)
            {
                timeStack = duration;
                reversed = true;
            }
            else if (timeStack <= 0)
            {
                timeStack = 0;
                reversed = false;
            }
        }
    }
}
