package graphics.drawers.drawables;

import graphics.IFrameUpdatable;
import javafx.scene.canvas.GraphicsContext;

public abstract class AnimationDrawable extends Drawable implements IFrameUpdatable
{
    private boolean paused;

    private boolean reversible;
    private boolean reversed;
    protected double timeStack;
    private double duration;

    public AnimationDrawable(double duration)
    {
        this.duration = duration;
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

    public void setReversible(boolean reversible)
    {
        this.reversible = reversible;
    }

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
